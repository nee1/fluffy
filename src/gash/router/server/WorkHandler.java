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

//import gash.router.server.election.ElectionManager;
import com.google.protobuf.GeneratedMessage;
import gash.router.server.election.RaftManager;
import gash.router.server.queue.ChannelQueue;
import gash.router.server.queue.PerChannelGlobalCommandQueue;
import gash.router.server.queue.QueueFactory;
import gash.router.server.resources.Query;
import global.Global;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import gash.router.server.edges.EdgeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pipe.common.Common;

import pipe.common.Common.Failure;
import pipe.work.Work;

import java.util.HashMap;

/**
 * The message handler processes json messages that are delimited by a 'newline'
 *
 * TODO replace println with logging!
 *
 * @author gash
 *
 */
public class WorkHandler extends SimpleChannelInboundHandler<Work.WorkRequest> {
	protected static Logger logger = LoggerFactory.getLogger("work");
	protected ServerState state;
	protected boolean debug = false;
	private ChannelQueue queue;
	public static HashMap<String, Channel> workClientChannel = new HashMap<>();

	public WorkHandler(ServerState state) {
		if (state != null) {
			this.state = state;
		}
	}

	/**
	 * override this method to provide processing behavior.
	 *
	 * @param msg
	 */
	 public void handleMessage(Work.WorkRequest msg, Channel channel) {
	 	if (msg == null) {
	 		// TODO add logging
	 		logger.error("ERROR: Unexpected content - " + msg);
	 		return;
	 	}

	 	if (debug)


	 	// TODO How can you implement this without if-else statements?
	 	try {

	 	} catch (Exception e) {
	 		// TODO add logging
	 		Failure.Builder eb = Failure.newBuilder();
	 		eb.setId(state.getConf().getNodeId());
	 		eb.setRefId(msg.getHeader().getNodeId());
	 		eb.setMessage(e.getMessage());
	 		Work.WorkRequest.Builder rb = Work.WorkRequest.newBuilder(msg);
	 		rb.setPayload(Work.Payload.newBuilder().setErr(eb));
	 		channel.write(rb.build());
	 	}

	 	System.out.flush();

	 }

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
	protected void channelRead0(ChannelHandlerContext ctx, Work.WorkRequest msg) throws Exception {
//	protected void channelRead0(ChannelHandlerContext ctx, GeneratedMessage msg) throws Exception {
		//handleMessage(msg, ctx.channel());
		if(msg.hasFile()){
			logger.info("File Name "+ msg.getFile().getFilename());
		}
		if (msg.getPayload().hasQuery()){
			workClientChannel.put(msg.getPayload().getQuery().getRequestId(), ctx.channel());
		}
		System.out.println("REQUEST ID: "+ msg.getPayload().getResponse().getRequestId());
		System.out.println("CONTAIN OR NOT: "+ GlobalCommandHandler.globalClientChannel.containsKey(msg.getPayload().getResponse().getRequestId()));
		System.out.println("TYPE: "+ msg.getPayload().getResponse().getRequestType());
		if(msg.hasBroadCast() && !msg.getBroadCast()){
			if(Query.broadCast){
				int remainNodes = Query.broadCastMap.get(msg.getPayload().getResponse().getRequestId());
				if(remainNodes > 1) {
					if (msg.getPayload().getResponse().getSuccess()) {
						Query.broadCast = false;
						Query.broadCastMap.remove(msg.getPayload().getResponse().getRequestId());

						if (GlobalCommandHandler.globalClientChannel.containsKey(msg.getPayload().getResponse().getRequestId())) {
							Channel res = GlobalCommandHandler.globalClientChannel.get(msg.getPayload().getResponse().getRequestId());
//				GlobalCommandHandler.globalClientChannel.remove(msg.getPayload().getResponse().getRequestId());
							System.out.println("SENT BACK TO CLIENT BOSSSS");
							Query q = new Query();
							Global.GlobalMessage gms = q.workToGlobalResponse(msg, msg.getFile());
							res.writeAndFlush(gms);
						}
					}
				}else if(remainNodes == 1){
					if (msg.getPayload().getResponse().getSuccess()) {
						Query.broadCast = false;
						Query.broadCastMap.remove(msg.getPayload().getResponse().getRequestId());

						if (GlobalCommandHandler.globalClientChannel.containsKey(msg.getPayload().getResponse().getRequestId())) {
							Channel res = GlobalCommandHandler.globalClientChannel.get(msg.getPayload().getResponse().getRequestId());
//				GlobalCommandHandler.globalClientChannel.remove(msg.getPayload().getResponse().getRequestId());
							System.out.println("SENT BACK TO CLIENT BOSSSS");
							Query q = new Query();
							Global.GlobalMessage gms = q.workToGlobalResponse(msg, msg.getFile());
							res.writeAndFlush(gms);
						}
					}else{

						if(msg.getPayload().getQuery().getRequestType()==Common.RequestType.READ){
							//ghb.setClusterId(((PerChannelGlobalCommandQueue) sq).getRoutingConf().getNodeId());
							//ghb.set(clientMessage.getHeader().getSourceHost()); // would be used to return message back to client

							//((PerChannelGlobalCommandQueue) sq).enqueueResponse(cb.build(),null);

							Common.Request.Builder crb = Common.Request.newBuilder();
							crb.setRequestId(msg.getPayload().getQuery().getRequestId());
							crb.setRequestType(msg.getPayload().getQuery().getRequestType());
							if(msg.getPayload().getQuery().hasFile()){
								crb.setFile(msg.getPayload().getQuery().getFile());
							}

							Global.GlobalHeader.Builder ghb = Global.GlobalHeader.newBuilder();
							ghb.setClusterId(msg.getHeader().getNodeId());
							ghb.setDestinationId(msg.getHeader().getDestination());// wont be available in case of request from client. but can be determined based on log replication feature
							ghb.setTime(msg.getHeader().getTime());

							Global.GlobalMessage.Builder cb = Global.GlobalMessage.newBuilder(); // message to be returned to actual client
							cb.setGlobalHeader(ghb);
							cb.setRequest(crb); // set the reponse to the client

							state.getGemon().pushMessagesIntoCluster(cb.build());
						}
					}
				}
			}


		}else {
			queueInstance(ctx.channel(), state).enqueueRequest(msg, ctx.channel());
		}

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
			queue = QueueFactory.getInstance(channel,state);

			// on close remove from queue
			channel.closeFuture().addListener(new ConnectionCloseListener(queue));
		}

		return queue;
	}

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