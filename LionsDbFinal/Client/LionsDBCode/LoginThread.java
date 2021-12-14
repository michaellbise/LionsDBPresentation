package LionsDBCode;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
public class LoginThread implements Runnable,Serializable{
	//Server Connections thread
	
	public static int loginServerPort = 32005;
	
	// login thread Object
	// handles logins moves them somewhere else? we may need to implement this in the class in order to achieve other 
	public LoginThread() {
		
	}
	
	
	public void run() {
		try {
			//Create socket
			ServerSocket ss = new ServerSocket(loginServerPort);
			Socket server = ss.accept();
			System.out.println("server started");
			//create session object to hold input stream
			Object in = new Session();
			
			// create input object stream
			BufferedInputStream CliIn = new BufferedInputStream(server.getInputStream());
			ObjectInputStream sessIn = new ObjectInputStream(CliIn);
			in = sessIn.readObject();//get sent object
			//process object ie get command, from there call appropriate action method
			String command = ((Session) in).getCommand();//
			//System.out.println(((Session) in).getCommand());
			//System.out.println(((Session) in).getUsername());
			
			
			MasterServer.printMessage("here");//ensure access to masterserver methods
			//MasterServer.StartLoginThread();//Re-Start Login thread after this one dies.
			//Pass socket to masterserver session thread
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
