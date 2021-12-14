package LionsDBCode;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.*;

public class NodeSessionHandler implements Runnable,Serializable{
	private String action;// set for logging
	private String path; // file path for logging
	private String other;// other message for logging
	private Socket socket;
	private Session session;
	private String nodeIP = "localhost";
	public NodeSessionHandler(Socket socket) {
		//Get socket from connection handler
		this.socket = socket;
		
	}
	public void run() {
		//use socket handle, get inputStream,perform action, message client
		try {
		//Create local Session object
		Object in = new Session();// Make a session object to hold our session comming in.
		BufferedInputStream CliIn = new BufferedInputStream(socket.getInputStream());
		System.out.println("Input stream opened");
		ObjectInputStream sessIn = new ObjectInputStream(CliIn);
		in = sessIn.readObject();//Assign Session Stream to Local Variable
		System.out.println("Node SessionHandler object recieved");
		
		
		//decision tree for incoming requests, Get data
		String command = ((Session) in).getCommand();//get action
		String username=((Session)in).getUsername();
		String password=((Session)in).getUsername();
		String filenames=((Session)in).getFilename();
		
		
		if(command.equalsIgnoreCase("newUser")) {
			Node.createUser(username);
			SendMessageToMasterServer("User on LionsDB","string",null);
		}
		if(command.equalsIgnoreCase("listFiles")) {
			String list = Node.fileList(username);
			SendMessageToMasterServer("", "list", list);
		}
		if(command.equalsIgnoreCase("deleteFile")) {
			String filename = ((Session) in).getFilename();
			Node.deleteFile(filename, username);
			SendMessageToMasterServer("","string",null);
			
		}
		if(command.equalsIgnoreCase("insertFile")) {
			byte[] b  = ((Session)in).getData();// get data
			// add logs
			Node.writeFileToDisk(b ,filenames,username,path);
			SendMessageToMasterServer("","",null);
		}
		
		//Make an output stream to send a message
		
		//Send Messages Back to Client
		//BufferedOutputStream messageOut = new BufferedOutputStream(socket.getOutputStream());
		//ObjectOutputStream messageToClient = new ObjectOutputStream(messageOut);
		//Message toClient = new Message();
		//messageToClient.writeObject(toClient);
		//messageToClient.flush();
		//SendMessageToClient("Object recieved,This Message sent!","messageType", null);
		//DO SESSION TASKS HERE
		//get command from 
		
		}
		catch(Exception e) {
			System.out.println("Error!, Node Session Handler");
			e.printStackTrace();
		}
	}
	public void SendMessageToMasterServer(String Message,String messageType,Object data) {
		// Sends a message back to the master server. Should be the last method called for an action
		try {
		BufferedOutputStream messageOut = new BufferedOutputStream(socket.getOutputStream());
		ObjectOutputStream messageToClient = new ObjectOutputStream(messageOut);
		Message toClient = new Message(Message,messageType,data);
		messageToClient.writeObject(toClient);//
		
		
		}
		catch(Exception e) {
			System.out.println("Message to master Failed");
			e.printStackTrace();
		}
		
	}
	

}
