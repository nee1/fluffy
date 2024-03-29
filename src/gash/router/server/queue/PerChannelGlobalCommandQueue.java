/*
 * copyright 2015, gash
 *
 * Gash licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package gash.router.server.queue;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import gash.router.container.GlobalConf;
import gash.router.container.RoutingConf;
import gash.router.server.MessageServer;
import gash.router.server.ServerState;
import gash.router.server.edges.EdgeInfo;
import global.Global;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * A server queue exists for each connection (channel). A per-channel queue
 * isolates clients. However, with a per-client model. The server is required to
 * use a master scheduler/coordinator to span all queues to enact a QoS policy.
 *
 * How well does the per-channel work when we think about a case where 1000+
 * connections?
 *
 * @author gash
 *
 */
public class PerChannelGlobalCommandQueue implements ChannelQueue {
	protected static Logger logger = LoggerFactory.getLogger("PerChannelCommandQueue");

	// The queues feed work to the inboundWork and outboundWork threads (workers). The
	// threads perform a blocking 'get' on the queue until a new event/task is
	// enqueued. This design prevents a wasteful 'spin-lock' design for the
	// threads
	//
	// Note these are directly accessible by the workers
	LinkedBlockingDeque<GeneratedMessage> inboundWork;
	LinkedBlockingDeque<GeneratedMessage> outboundWork;

	Channel channel;
	RoutingConf conf;
	GlobalConf globalConf;

	ServerState state;

	// This implementation uses a fixed number of threads per channel
	private ArrayList<GlobalCommandOutboundAppWorker> oworkerList;
	private ArrayList<GlobalCommandInboundAppWorker> iworkerList;

	// not the best method to ensure uniqueness
	private ThreadGroup tgroup = new ThreadGroup("PerChannelQ-" + System.nanoTime());

	public PerChannelGlobalCommandQueue(Channel channel, ServerState state) {
		this.channel = channel;
		this.conf = state.getConf();
		this.globalConf = state.getGlobalConf();
		this.state = state;
		init();
	}

	protected void init() {
		inboundWork = new LinkedBlockingDeque<GeneratedMessage>();
		outboundWork = new LinkedBlockingDeque<GeneratedMessage>();

		oworkerList = new ArrayList<>(3);
		iworkerList = new ArrayList<>(3);

		logger.info("Starting to listen to Command worker");
		for(int i=0;i<3;i++){
			GlobalCommandInboundAppWorker tempWorker = new GlobalCommandInboundAppWorker(tgroup, i+1, this);
			iworkerList.add(tempWorker);
			tempWorker.start();
		}

		for(int i=0;i<3;i++){
			GlobalCommandOutboundAppWorker tempWorker = new GlobalCommandOutboundAppWorker(tgroup, i+1, this);
			oworkerList.add(tempWorker);
			tempWorker.start();
		}

	}

	public Channel getChannel() {
		return channel;
	}

	public RoutingConf getRoutingConf(){
		return conf;
	}

	public GlobalConf getGlobalConf(){
		return globalConf;
	}

	public void setState(ServerState state) {
		//Nothing to do with this class
	}

	public ServerState getState() {
		//Nothing to do with this class
		return state;
	}

	@Override
	public void setRouteConfig(RoutingConf config) {
		this.conf = config;
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see poke.server.ChannelQueue#shutdown(boolean)
	 */
	@Override
	public void shutdown(boolean hard) {
		logger.info("Command channel is shutting down");

		channel = null;

		if (hard) {
			// drain queues, don't allow graceful completion
			inboundWork.clear();
			outboundWork.clear();
		}

		for(int i=0;i<iworkerList.size();i++) {
			GlobalCommandInboundAppWorker iworker = iworkerList.get(0);
			if (iworker != null) {
				iworker.forever = false;
				if (iworker.getState() == State.BLOCKED || iworker.getState() == State.WAITING)
					iworker.interrupt();
			}
		}
		iworkerList.clear();

		for(int i=0;i<oworkerList.size();i++) {
			GlobalCommandOutboundAppWorker oworker = oworkerList.get(0);
			if (oworker != null) {
				oworker.forever = false;
				if (oworker.getState() == State.BLOCKED || oworker.getState() == State.WAITING)
					oworker.interrupt();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see poke.server.ChannelQueue#enqueueRequest(eye.Comm.Finger)
	 */
	@Override
	public void enqueueRequest(GeneratedMessage req, Channel notused) {
		try {
//			EdgeInfo ei = new EdgeInfo(((Global.GlobalMessage)req).getGlobalHeader().getClusterId(),Integer.toString(((Global.GlobalMessage)req).getGlobalHeader().getDestinationId()),-1);
//			ei.setChannel(notused);
//			ei.setClientChannel(true);
//			state.getEmon().addToInbound(ei);
//			System.out.println(state);
			//System.out.println(MessageServer.);
			inboundWork.put(req);
		} catch (InterruptedException e) {
			logger.error("message not enqueued for processing", e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see poke.server.ChannelQueue#enqueueResponse(eye.Comm.Response)
	 */
	@Override
	public void enqueueResponse(GeneratedMessage reply, Channel notused) {
		if (reply == null)
			return;

		try {
			outboundWork.put(reply);
		} catch (InterruptedException e) {
			logger.error("message not enqueued for reply", e);
		}
	}

	public class CloseListener implements ChannelFutureListener {
		private ChannelQueue sq;

		public CloseListener(ChannelQueue sq) {
			this.sq = sq;
		}

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			sq.shutdown(true);
		}
	}

	public LinkedBlockingDeque<GeneratedMessage> getInboundWork() {
		return inboundWork;
	}

	public LinkedBlockingDeque<GeneratedMessage> getOutboundWork() {
		return outboundWork;
	}
}
