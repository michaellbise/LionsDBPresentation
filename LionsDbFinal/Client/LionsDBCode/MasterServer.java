
package LionsDBCode;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dyocum01
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;
public class MasterServer {
	static File userDB = new File("userDB.txt");
	static HashMap<String, String> userList = new HashMap<>();
	public static String[][] nodeBalance;
	public static String node1IP = "localHost";
	public static String node2IP = "localHost";
	public static String node3IP = "localHost";
	public static void main(String[] args) {
		try {
			StartIncomingConnectionHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void printMessage(String message) {// simple message for debug
		System.out.println(message);
	}
	public static void StartIncomingConnectionHandler() throws IOException {
		//Starts a Thread to handle Incoming Connections
		// we may want to keep the thread handle somewhere in case we need it...
		ConnectionHandler conHan = new ConnectionHandler();
		Thread t1 = new Thread(conHan);
		t1.start();
		
	}
	public static int NodeBalancer(int decrement) {
		// returns a int of index of the node with the least active operations
		//prameter 
		int userCount = Integer.MAX_VALUE;
		int index =-1; 
		if(decrement == -1) {
		// step through array
		for(int i =0;i< nodeBalance[0].length;i++) {
			if(Integer.parseInt(nodeBalance[1][i]) <= userCount)
			{
				userCount = Integer.parseInt(nodeBalance[1][i]);
				index = i;
			}

		}
		}
		else {
			
			int value = Integer.parseInt(nodeBalance[1][index]);
			value --;
			nodeBalance[1][index] = Integer.toString(value);
			
		}
		return  index;	
	}
    public static void updateMasterfile() {
    	//Code to update masterfiles.txt here
    }
    public static void updateLog() {
    	// code to update logs
    }
    
    /*
    Author: Derek Yocum
    Method functionality: allows existing users to login, invoked by button press from GUI.
    */
    public boolean userLogin(String user, String pass, String ip) throws IOException {
    	String log = user + "," + ip;
    	MasterServer.updateLog("[userauth]", log);
    	
        boolean status = false;
        String userLog = user+","+pass;
    	Scanner sc = new Scanner(userDB);
		while (sc.hasNextLine()) {
                        if(userLog.equals(sc.next())){
                            status = true;
                        }
		}
                sc.close();
                
       if(status = true) {
    	   MasterServer.updateLog("[userconnect]",log);
       }
        return status;
    }
    
    /*
    Author: Derek Yocum
    Method functionality: allows new users to create an account, invoked by button press from GUI.
    */
    public boolean userCreateAccount(String user, String pass) throws IOException {
                boolean status = false;
		FileWriter fw = new FileWriter(userDB, true);
		BufferedWriter bw = new BufferedWriter(fw);
		userList.put(user, pass);
		if (userExists(user) == false) {
			fw.write("\n" + user + "," + pass);
                        status = true;
		}
                
		bw.close();
		fw.close();
                
                return status;
    }
    
    /*
    Author: Derek Yocum
    Method functionality: Backend functionality, no GUI representation, 
    simply checks if a user already exists on file when a new user tries to create an account
    */
    public boolean userExists(String user) throws FileNotFoundException {
		Scanner sc = new Scanner(userDB);
		while (sc.hasNextLine()) {
			if (sc.next().contains(user)) {
				return true;
			}
		}
		
		sc.close();
		return false;
    }
    
    
    
	// file managament function go here
	
	// need individual methods for updating logs,masterfile editing, etc.
	public static void deleteMasterFileLog(String username,String filename ){ 
		
		try {
			File log = new File("deleteLog");// still need to figure out node pathing
			File tempFile = new File(log.getAbsolutePath() + ".tmp");//temp file
			BufferedReader br = new BufferedReader(new FileReader(log)); 
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
	
			String line = null;
			while ((line = br.readLine()) != null) {
	  
			  if (!getLogFileName(line).equals(filename)) {
	  
				pw.println(line);
				pw.flush();
			  }
			}
			pw.close();
			br.close();
	  
			//Delete file
			log.delete();
	  
			//Rename the new file to the filename the original file had.
			tempFile.renameTo(log);
		}
		  catch (FileNotFoundException ex) {
			ex.printStackTrace();
		  }
		  catch (IOException ex) {
			ex.printStackTrace();
		  }
	}
	public static String getLogFileName(String line){
		String[] split = line.split("::");
		return split[1];
	}
	public static void updateMasterFile(String username,String filename, String pathToFile, String servers) throws IOException { 
		BufferedWriter br = new BufferedWriter(new FileWriter("masterLog", true));	 
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String line = String.format("%-15s%-15s%-25s%-25s", username,filename,timeStamp,pathToFile)+"\n";
		br.write(line);
		br.close();
	}
    public static void updateLog(String command, String other) throws IOException { //"other" is formatting unique to command
		BufferedWriter br = new BufferedWriter(new FileWriter("updateLog", true));	 
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String line = String.format("%-15s%-25s", command,timeStamp)+other+"\n";
		br.write(line);
		br.close();
	}
}
