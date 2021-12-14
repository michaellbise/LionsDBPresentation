
package LionsDBCode;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
public class NodeConnectionHandler implements Runnable,Serializable {
	//Author:Michael Bise
	//Accepts incomming connections the assigns them to their own session thread.
	//A must for handling multiple clients!
	public static int NodeServerPort = 32004;
	private ServerSocket ss;
	
	
	private boolean active = true;
	public NodeConnectionHandler() throws IOException{
		//Start a server socket upon creation
		ss = new ServerSocket(NodeServerPort);
		
		
	}
	public void run() {
		try {
			while(active) {//always run to accept connections.
						
			//Accept connection
			Socket sessionSocket = ss.accept();
			System.out.println("Node connection server started");
			
			//Pass to its own thread ,session handler with messaging
			NodeSessionHandler session = new NodeSessionHandler(sessionSocket);
			Thread t1 = new Thread(session);
			t1.start();
			MasterServer.printMessage("Node Connection Handler,session started");//ensure access to masterserver methods
			
			//Call Logs etc. to add a connected user
			// add session to list to keep track of active sessions
			}
			
			//MasterServer.StartLoginThread();//Re-Start Login thread after this one dies.
			//Pass socket to masterserver session thread
		}
		catch(Exception e) {
			System.out.println("Node Connection Handler Failed!");
			e.printStackTrace();
		}
		System.out.println("Connection Handler Stopped via call");
	
	}
	public void setActive(boolean active) {
		// allow stopping of while loop without killing thread 
		this.active = active;
	}
	
	
}
