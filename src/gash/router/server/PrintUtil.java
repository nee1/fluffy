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

import gash.router.server.resources.Query;
import global.Global;
import pipe.common.Common;
import pipe.common.Common.Failure;
import pipe.common.Common.Header;
import pipe.work.Work;
import routing.Pipe;

public class PrintUtil {
	private static final String gap = "   ";

	public static void printHeader(Header hdr) {
		System.out.println("\n-------------------------------------------------------");
		System.out.println("ID:        " + hdr.getNodeId());
		System.out.println("Time:      " + hdr.getTime());
		//System.out.println("Sorc Host: " + hdr.getSourceHost());
		//System.out.println("Dest Host: " + hdr.getDestination());
		if (hdr.hasMaxHops())
			System.out.println("Hops: " + hdr.getMaxHops());
		if (hdr.hasDestination())
			System.out.println("Dest: " + hdr.getDestination());

	}

	public static void printCommand(Pipe.CommandRequest msg) {
		PrintUtil.printHeader(msg.getHeader());

		Pipe.Payload py = msg.getPayload();

		System.out.print("\nCommand: ");
		if (py.hasErr()) {
			System.out.println("Failure");
			System.out.println(PrintUtil.gap + "Code:    " + py.getErr().getId());
			System.out.println(PrintUtil.gap + "Ref ID:  " + py.getErr().getRefId());
			System.out.println(PrintUtil.gap + "Message: " + py.getErr().getMessage());
		} else if (py.hasPing())
			System.out.println("Ping");
		else if (py.hasMessage()) {
			System.out.println("Message");
			System.out.println(PrintUtil.gap + "Msg:  " + py.getMessage());
		} else
			System.out.println("Unknown");
	}

	public static void printGlobalCommand(Global.GlobalMessage msg) {
		//PrintUtil.printHeader(msg.getGlobalHeader());
		Global.GlobalHeader hdr = msg.getGlobalHeader();

		System.out.println("\n-------------------------------------------------------");
		System.out.println("ID:        " + hdr.getClusterId());
		System.out.println("Time:      " + hdr.getTime());
		//System.out.println("Sorc Host: " + hdr.getSourceHost());
		//System.out.println("Dest Host: " + hdr.getDestination());
//		if (hdr.hasMaxHops())
//			System.out.println("Hops: " + hdr.getMaxHops());
		if (hdr.hasDestinationId())
			System.out.println("Dest: " + hdr.getDestinationId());

		System.out.print("\nGlobalMessage: ");
		if (msg.getResponse().hasFailure()) {
			System.out.println("Failure");
			System.out.println(PrintUtil.gap + "Code:    " + msg.getResponse().getFailure().getId());
			System.out.println(PrintUtil.gap + "Ref ID:  " + msg.getResponse().getFailure().getRefId());
			System.out.println(PrintUtil.gap + "Message: " + msg.getResponse().getFailure().getMessage());
		} else if (msg.hasPing())
			System.out.println("Ping");
		else if (msg.hasMessage()) {
			System.out.println("Message");
			System.out.println(PrintUtil.gap + "Msg:  " + msg.getMessage());
		} else if(msg.hasRequest()){
			printQuery(msg.getRequest());
		}else{
			System.out.println("Unknown");
		}
	}

	public static void printQuery(Common.Request req){
		System.out.println("Query");
		switch(req.getRequestType()){
			case READ:
				System.out.println(PrintUtil.gap + "Search File:  " + req.getRequestId());
				break;
			case WRITE:
				//System.out.println(PrintUtil.gap + " File to Store:  " + req.getRequestId());
				if(req.hasFile()){
					System.out.println(PrintUtil.gap + " Sequence:  " + req.getFile().getChunkId());
				}
				if(req.hasFileName()){
					System.out.println(PrintUtil.gap + " Filename:  " + req.getFileName());
				}
				break;
			case UPDATE:
				break;
			case DELETE:
				break;
		}

	}

	public static void printWork(Work.WorkRequest msg) {
		PrintUtil.printHeader(msg.getHeader());

		Work.Payload payload = msg.getPayload();
		System.out.print("\nWork: ");
		if (payload.hasErr())
			System.out.println("Failure");
		else if (payload.hasPing())
			System.out.println("Ping");
		else
			System.out.println("Unknown");

		System.out.println(PrintUtil.gap + "Sec:  " + msg.getSecret());
	}

	public static void printFailure(Failure f) {
		System.out.println("ERROR: " + f.getId() + "." + f.getRefId() + " : " + f.getMessage());
	}
}
