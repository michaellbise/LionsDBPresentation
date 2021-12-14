package LionsDBCode;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
public class ConnectionHandler implements Runnable,Serializable {
	//Author:Michael Bise
	//Accepts incomming connections the assigns them to their own session thread.
	//A must for handling multiple clients!
	public static int loginServerPort = 32005;
	private ServerSocket ss;
	
	
	private boolean active = true;
	public ConnectionHandler() throws IOException{
		//Start a server socket upon creation
		ss = new ServerSocket(loginServerPort);
		
		
	}
	public void run() {
		try {
			while(active) {//always run to accept connections.
						
			//Accept connection
			Socket sessionSocket = ss.accept();
			System.out.println("server started");
			
			//Pass to its own thread ,session handler with messaging
			SessionHandler session = new SessionHandler(sessionSocket);
			Thread t1 = new Thread(session);
			t1.start();
			MasterServer.printMessage("Connection Handler,session started");//ensure access to masterserver methods
			
			//Call Logs etc. to add a connected user
			// add session to list to keep track of active sessions
			}
			
			//MasterServer.StartLoginThread();//Re-Start Login thread after this one dies.
			//Pass socket to masterserver session thread
		}
		catch(Exception e) {
			System.out.println("Connection Handler Failed!");
			e.printStackTrace();
		}
		System.out.println("Connection Handler Stopped via call");
	
	}
	public void setActive(boolean active) {
		// allow stopping of while loop without killing thread 
		this.active = active;
	}
	
	
}
