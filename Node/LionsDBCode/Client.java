package LionsDBCode;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client implements Serializable {
	
	public static String MasterServerIP = "localhost";// the ip address for the master server
	public  static int MasterServerLoginPort =32005;// login port for master server
	
	public static String SessionPort;
	public static String clientID;
	public static String clientIP ="localhost";
	
	public static void main(String args[]) throws IOException{
	//Author: Michael Bise
	//Date:11/25/2021
		
	//Description: Client Starter file for first prototype, should establish most basic funtions
	//
	//User auth and password
	//Retrieve file list
	//Insert file(upload)?
	//Delete file
	//
	// First, establish function for connecting to server and sending data, could have different modes as well such as
	//new user, existing user login, 
	
	//Start main with args like login , password , and desired action,
	//most likely does NOT need to be threaded
	// arguments -options -action- username -password 	
		//Authenticate("testUser","password");
	}
	
	public void Authenticate(String username,String password) throws IOException {
		//Author:Michael Bise
		//Start login authorization procedure
		//Create session object 
		Session userLogin = new Session(username,password,clientIP,MasterServerIP,"authenticate",null); // 
		//Create sockets 
		Socket client = new Socket(MasterServerIP, MasterServerLoginPort);// open a socket for client side to connect to server login thread
		//Message object
		Message input;
		
		try {	
		//Create object output Stream to send session object
		BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());//
		ObjectOutputStream obj = new ObjectOutputStream(bos);
		
		//Create Object input Stream to recieve message object(with data)
		obj.writeObject(userLogin);
		obj.flush();// Send session object without closing port
		System.out.println("Sent login Session Object");
		//Get a message back from Server
		//Open object Input Streams
		BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
		ObjectInputStream messageStream = new ObjectInputStream(bis);
		System.out.println("Client Recieving message");
		input = (Message) messageStream.readObject();
		//Close connections
		client.close();
		
		///////////////////////////////////////////////////////////////////////////////
		String message = input.getMessage();
		System.out.println(message);
		//Auth success, Failed.etc/////////////////////////////////////////////////////
		}
		catch(Exception ex) {
			//Appropriate call back methods here
			System.out.println("Master Server auth failed");
			ex.printStackTrace();
		}
		// Any other methods called here
	}
	
	public static void createUser(String username,String password) throws IOException {
		//Author:Michael Bise
		//Create a new user on LionsDB
		//Create session object 
		Session NewUser = new Session(username,password,clientIP,MasterServerIP,"newUser",null); // 
		//Create sockets 
		Socket client = new Socket(MasterServerIP, MasterServerLoginPort);// open a socket for client side to connect to server login thread
		//Message object
		Message input;
		try {	
		//Create object output Stream to send session object
		BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());//
		ObjectOutputStream obj = new ObjectOutputStream(bos);
				
		//Create Object input Stream to recieve message object(with data)
		obj.writeObject(NewUser);
		obj.flush();// Send session object without closing port
		System.out.println("Sent Session Object");
		//Get a message back from Server
		//Open object Input Streams
		BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
		ObjectInputStream messageStream = new ObjectInputStream(bis);
		System.out.println("Client Recieving message");
		input = (Message) messageStream.readObject();
		//Close connections
		client.close();
				
		///////////////////////////////////////////////////////////////////////////////
		String message = input.getMessage();
		System.out.println(message);
		//New User success, Failed.etc/////////////////////////////////////////////////////
		}
		catch(Exception ex) {
		//Appropriate call back methods here
		System.out.println("Master Server NewUser failed");
		ex.printStackTrace();
		}
		// Any other methods called here
		}
	
	public String retrieveFileList(String username,String password) throws IOException {
		//Author:Michael Bise
		//returns comma-delimited list of files
		//Get FileList on LionsDB
		//Create session object 
		Session ListFiles = new Session(username,password,clientIP,MasterServerIP,"listFiles",null); // 
		//Create sockets 
		Socket client = new Socket(MasterServerIP, MasterServerLoginPort);// open a socket for client side to connect to server login thread
		//Message object
		Message input;
		String fileList ="";
		try {	
		//Create object output Stream to send session object
		BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());//
		ObjectOutputStream obj = new ObjectOutputStream(bos);			
		//Create Object input Stream to recieve message object(with data)
		obj.writeObject(ListFiles);
		obj.flush();// Send session object without closing port
		System.out.println("Sent Session Object");
				//Get a message back from Server
				//Open object Input Streams
				BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
				ObjectInputStream messageStream = new ObjectInputStream(bis);
				System.out.println("Client Recieving message");
				input = (Message) messageStream.readObject();
				//Close connections
				client.close();
						
				///////////////////////////////////////////////////////////////////////////////
				String message = input.getMessage();
				System.out.println(message);
				fileList = (String)input.getData();
				//New User success, Failed.etc/////////////////////////////////////////////////////
				}
				catch(Exception ex) {
				//Appropriate call back methods here
				System.out.println("Master Server NewUser failed");
				ex.printStackTrace();
				}
				// Any other methods called here
				
		return fileList;
	}
	public void insertFile(String username,String password, String filepath,String filename,File file) throws IOException {
		//Author:Michael Bise
		//Create a new file on LionsDB
		//Create session object 
		Session InsertFile = new Session(username,password,clientIP,MasterServerIP, "insertFile" , file); // 
		InsertFile.setData(getBytes(filepath,filename));
		//Create sockets 
		Socket client = new Socket(MasterServerIP, MasterServerLoginPort);// open a socket for client side to connect to server login thread
		//Message object
		Message input;
		try {	
		//Create object output Stream to send session object
		BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());//
		ObjectOutputStream obj = new ObjectOutputStream(bos);
						
		//Create Object input Stream to recieve message object(with data)
		obj.writeObject(InsertFile);
		obj.flush();// Send session object without closing port
		System.out.println("Sent Session Object");
		//Get a message back from Server
		//Open object Input Streams
		BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
		ObjectInputStream messageStream = new ObjectInputStream(bis);
		System.out.println("Client Recieving message");
		input = (Message) messageStream.readObject();
		//Close connections
		client.close();
						
		///////////////////////////////////////////////////////////////////////////////
		String message = input.getMessage();
		System.out.println(message);
		//New User success, Failed.etc/////////////////////////////////////////////////////
		}
		catch(Exception ex) {
		//Appropriate call back methods here
		System.out.println("Master Server InsertFile failed");
		ex.printStackTrace();
		}
		// Any other methods called here
	}
	
	public void deleteFile(String username,String password,String filename)throws IOException {
		//Author:Michael Bise
		//Delete a File on LionsDB
		//Create session object 
		Session DeleteFile = new Session(username,password,clientIP,MasterServerIP,"deleteFile",null); // 
		DeleteFile.setFilename(filename);
		//Create sockets 
		Socket client = new Socket(MasterServerIP, MasterServerLoginPort);// open a socket for client side to connect to server login thread
		//Message object
		Message input;
		try {	
		//Create object output Stream to send session object
		BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());//
		ObjectOutputStream obj = new ObjectOutputStream(bos);
		
		//Create Object input Stream to recieve message object(with data)
		obj.writeObject(DeleteFile);
		obj.flush();// Send session object without closing port
		System.out.println("Sent Session Object");
		//Get a message back from Server
		//Open object Input Streams
		BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
		ObjectInputStream messageStream = new ObjectInputStream(bis);
		System.out.println("Client Recieving message");
		input = (Message) messageStream.readObject();
		//Close connections
		client.close();
						
		///////////////////////////////////////////////////////////////////////////////
		String message = input.getMessage();
		System.out.println(message);
		//New User success, Failed.etc/////////////////////////////////////////////////////
		}
		catch(Exception ex) {
		//Appropriate call back methods here
		System.out.println("Master Server NewUser failed");
		ex.printStackTrace();
		}
		// Any other methods called here
		}
	
	public static byte[] getBytes(String filename, String filepath) throws IOException {
		// get bytes of a file to send over object stream 
		// this is a terrible way of doing this, use another data stream
		byte[] b;
		
		Path fileloc = Paths.get(filepath+filename);
		b = Files.readAllBytes(fileloc);

		return b;
	}
	
	
		
}