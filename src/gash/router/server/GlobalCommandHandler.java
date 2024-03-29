/**
 * Copyright 2016 Gash.
 *
 * This file and intellectual content is protected under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package gash.router.server;

import gash.router.container.GlobalConf;
import gash.router.container.RoutingConf;
import gash.router.server.queue.ChannelQueue;
import gash.router.server.queue.QueueFactory;
import global.Global;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pipe.common.Common.Failure;
import routing.Pipe;

import java.util.HashMap;

/**
 * The message handler processes json messages that are delimited by a 'newline'
 *
 * TODO replace println with logging!
 *
 * @author gash
 *
 */
public class GlobalCommandHandler extends SimpleChannelInboundHandler<Global.GlobalMessage> {
	protected static Logger logger = LoggerFactory.getLogger("cmd");
	protected RoutingConf conf;
	protected GlobalConf globalConf;
	private ChannelQueue queue;
	protected ServerState state;

	//private HashMap<Integer,String> map = new HashMap<>();
	public static HashMap<String, Channel> globalClientChannel = new HashMap<>();

    public static HashMap<String, Channel> allChannels = new HashMap<>();

	public GlobalCommandHandler(ServerState state) {
		if (state != null) {
			this.conf = state.getConf();
			this.globalConf = state.getGlobalConf();
			this.state = state;
		}

	}


    public GlobalCommandHandler(){   }

	/**
	 * override this method to provide processing behavior. This implementation
	 * mimics the routing we see in annotating classes to support a RESTful-like
	 * behavior (e.g., jax-rs).
	 *
	 * @param msg
	 */
	// public void handleMessage(Global.GlobalCommandMessage msg, Channel channel) {
	// 	if (msg == null) {
	// 		// TODO add logging
	// 		logger.error("ERROR: Unexpected content - " + msg);
	// 		return;
	// 	}
	//
	// 	//PrintUtil.printCommand(msg);
	//
	// 	try {
	//
	// 	} catch (Exception e) {
	// 		// TODO add logging
	// 		Failure.Builder eb = Failure.newBuilder();
	// 		eb.setId(conf.getNodeId());
	// 		eb.setRefId(msg.getHeader().getNodeId());
	// 		eb.setMessage(e.getMessage());
	// 		Global.GlobalCommandMessage.Builder rb = Global.GlobalCommandMessage.newBuilder(msg);
	// 		Pipe.Payload.Builder pb = Pipe.Payload.newBuilder();
	// 		pb.setErr(eb);
	//
	// 		channel.write(rb.build());
	// 	}
	//
	// 	System.out.flush();
	// }

	/**
	 * a message was received from the server. Here we dispatch the message to
	 * the client's thread pool to minimize the time it takes to process other
	 * messages.
	 *
	 * @param ctx
	 *            The channel the message was received from
	 * @param msg
	 *            The message
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Global.GlobalMessage msg) throws Exception {
		//handleMessage(msg, ctx.channel());if(msg.hasRequest()) {
		if(msg.hasRequest() && msg.getGlobalHeader().hasIntraCluster() && msg.getGlobalHeader().getIntraCluster()) {
			System.out.println("SAVED CHANNEL IN GLOBAL HANDLER");
			globalClientChannel.put(msg.getRequest().getRequestId(), ctx.channel());
			System.out.println("GOT MSG TO ME GLOBALCOMMANDHANDLER");
			//allChannels.put(msg.getResponse().getRequestId(), ctx.channel());
		}
		queueInstance(ctx.channel(), state).enqueueRequest(msg,ctx.channel());
	}

	public HashMap<String, Channel> getAllChannels(){
        return allChannels;
    }

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Unexpected exception from downstream.", cause);
		ctx.close();
	}

	/**
	 * Isolate how the server finds the queue. Note this cannot return null.
	 *
	 * @param channel
	 * @return
	 */
	private ChannelQueue queueInstance(Channel channel, ServerState state) {
		// if a single queue is needed, this is where we would obtain a
		// handle to it.

		if (queue != null)
			return queue;
		else {
			queue = QueueFactory.getInstance(channel, state, globalConf);

			// on close remove from queue
			channel.closeFuture().addListener(new ConnectionCloseListener(queue));
		}

		return queue;
	}

//	@Override
//	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Global.GlobalMessage globalMessage) throws Exception {
//
//	}

	public static class ConnectionCloseListener implements ChannelFutureListener {

		ChannelQueue inQueue;

		public ConnectionCloseListener(ChannelQueue queue){
			inQueue = queue;
		}

		@Override
		public void operationComplete(ChannelFuture channelFuture) throws Exception {

			if(inQueue!=null)
				inQueue.shutdown(false);
			inQueue = null;
		}
	}

}
