package LionsDBCode;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SessionHandler implements Runnable,Serializable{

	private Socket socket;
	private Session session;
	private String nodeIP = "10.183.240.12";
	private int nodePort = 32004;
	private String action;// set for logging
	private String path; // file path for logging
	private String other;// other message for logging
	public SessionHandler(Socket socket) {
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
		System.out.println("SessionHandler object recieved");
		
		/////////////////////do this later//////////////////////////////
		//Get node list/load balance 
		//node list get list of nodes, get #connections per node
		//choose smallest number of connections
		// set node ip
		//nodeIP = "getnodelist~~~";
		///////////////////////////////////////////////////
		
		//decision tree for incoming requests, Get data
		String command = ((Session) in).getCommand();//get action
		String username=((Session)in).getUsername();
		String password=((Session)in).getPasswd();
		System.out.println(command);
		if(command.equalsIgnoreCase("authenticate")) {
			String log = username+((Session) in).getFilename();
			MasterServer.updateMasterFile(username,((Session) in).getFilename(),log , "");
			log = username+((Session) in).getFilename()+log;
			MasterServer.updateLog("[fileinsert]",log);
			System.out.println("auth done")	;
			SendMessageToClient("auth done","",null);
		}
		if(command.equalsIgnoreCase("insertFile")) {
			// call balancer method for this method only
			String log = "~/"+username+"/"+((Session) in).getFilename();
			MasterServer.updateMasterFile(username,((Session) in).getFilename(),log , "");
			log = username+((Session) in).getFilename()+log;
			MasterServer.updateLog("[fileinsert]",log);
				
			SendMessageToClient("","", sendSessionToNodeServer((Session) in));
			
		}
		if(command.equalsIgnoreCase("newUser")) {
			SendMessageToClient("","", sendSessionToNodeServer((Session) in));
		}
		if(command.equalsIgnoreCase("listFiles")) {
			SendMessageToClient("","", sendSessionToNodeServer((Session) in));
			//connect to node server
			// request fileslist
			// call message to client
		}
		if(command.equalsIgnoreCase("deleteFile")) {
			SendMessageToClient("","", sendSessionToNodeServer((Session) in));
			MasterServer.deleteMasterFileLog(username,((Session) in).getFilename() );
			String log = username+","+((Session) in).getFilename() ;
			
			MasterServer.updateLog("[filedelete]",log );
			Message a = new Message();
			a = sendSessionToNodeServer((Session) in); 
			SendMessageToClient(a.getMessageType(),a.getMessage(),null);
			// connect to node server with the file listed
			// request delete file
			// call message to client	
		}
		
		System.out.println(((Session) in).getUsername());
		//Make an output stream to send a message
		
		
		//Send Messages Back to Client
//		BufferedOutputStream messageOut = new BufferedOutputStream(socket.getOutputStream());
//		ObjectOutputStream messageToClient = new ObjectOutputStream(messageOut);
//		Message toClient = new Message();
//		messageToClient.writeObject(toClient);
//		messageToClient.flush();
		
		
		System.out.println("SessionHandler object recieved");
		
		//SendMessageToClient("Object recieved,This Message sent!","messageType", null);
		//DO SESSION TASKS HERE
		//get command from 
		
		
		}
		catch(Exception e) {
			System.out.println("Error!, Session Handler");
			e.printStackTrace();
		}
	}
	
	
	public Message createUser(Session session) throws IOException {
		Socket msNode = new Socket(nodeIP, nodePort);
		Message nodeMessage;
		try {	
		//Create object output Stream to send session object
		BufferedOutputStream bos = new BufferedOutputStream(msNode.getOutputStream());//
		ObjectOutputStream obj = new ObjectOutputStream(bos);
						
		//Create Object input Stream to recieve message object(with data)
		obj.writeObject(session);
		obj.flush();// Send session object without closing port
		System.out.println("Sent Session Object");
		//Get a message back from Server
		//Open object Input Streams
		BufferedInputStream bis = new BufferedInputStream(msNode.getInputStream());
		ObjectInputStream messageStream = new ObjectInputStream(bis);
		System.out.println("Ms Recieving message");
		nodeMessage = (Message) messageStream.readObject();
		//Close connections
		msNode.close();
						
		///////////////////////////////////////////////////////////////////////////////
		String message = nodeMessage.getMessage();
		System.out.println(message);
		//New User success, Failed.etc/////////////////////////////////////////////////////
		}
		catch(Exception ex) {
		//Appropriate call back methods here
		System.out.println("Master Server InsertFile failed");
		ex.printStackTrace();
		}
		
		//check node server list
		
		return null;
	}
	//Communication Methods
	public Message sendSessionToNodeServer(Session session) throws IOException {
		// send a session to the node server get a message back, returns null upon error
		Socket nodeSend = new Socket(nodeIP,nodePort);
		
		try {
		BufferedOutputStream sessionOut = new BufferedOutputStream(nodeSend.getOutputStream());
		ObjectOutputStream sessionToNode = new ObjectOutputStream(sessionOut);
		sessionToNode.writeObject(session);
		sessionToNode.flush();
		BufferedInputStream bis = new BufferedInputStream(nodeSend.getInputStream());
		ObjectInputStream messageIn = new ObjectInputStream(bis);
		Message in = new Message();
		in = (Message) messageIn.readObject();
		//log 
		MasterServer.updateLog("dnconnect",nodeIP);
		return in;
		}
		catch(Exception e) {
			System.out.println("Message to master Failed");
			e.printStackTrace();
		}
		MasterServer.updateLog("dnfailure",nodeIP); //log
		nodeSend.close();
		return null;// null if cant do it
	}
	public void SendMessageToClient(String Message,String messageType,Object data) throws IOException {
		// send a message back to the client, should be the last method called for an action
		try {
			BufferedOutputStream messageOut = new BufferedOutputStream(socket.getOutputStream());
			ObjectOutputStream messageToClient = new ObjectOutputStream(messageOut);
			Message toClient = new Message(Message,messageType,data);
			messageToClient.writeObject(toClient);
			messageOut.flush();
			System.out.println("sent message to client");
			}
			catch(Exception e) {
				System.out.println("Message to client Failed");
				e.printStackTrace();
			}
		socket.close();
	}
	

}
