package LionsDBCode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import LionsDBCode.ConnectionHandler;
public class Node {
	static String path;
	public static void main(String args[]) throws IOException {
		
		StartNodeIncomingConnectionHandler();
		
	}
	public static void StartNodeIncomingConnectionHandler() throws IOException {
		//Starts a Thread to handle Incoming Connections
		// we may want to keep the thread handle somewhere in case we need it...
		NodeConnectionHandler NodeconHan = new NodeConnectionHandler();
		Thread t1 = new Thread(NodeconHan);
		t1.start();
		
	}
	public static void saveFile(File file,String username){
	}
	public static void deleteFile(File file,String username) {
		if(file.exists()){
			file.delete();
			try {
				updateDataLog(username,file.getName(),"[delete]","");
				deleteDataFileLog(username,file.getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static String fileList(String username) {
		String list = "";
		File dir = new File(path+"/"+username);
		if(!dir.exists()){// check if file exists
			dir.mkdir();
		}else{
			//loop through directory add to list string 
			File[] directoryListing = dir.listFiles();
			for(int i = 0; i<directoryListing.length; i++) {
				if(i==directoryListing.length-1) {
					list = list+ directoryListing[i].getName();
				}else{
					list = list+ directoryListing[i].getName()+",";
				}
			}
		}
		
		//return a comma delimited file list
		return list;
	}
	public static void createUser(String username) {
		File dir = new File(path+"/"+username);
		if(!dir.exists()){// check if file exists
			dir.mkdir();
		}
	}
	public static void updateDataLog(String username,String filename, String command,String other) throws IOException { 
		BufferedWriter br = new BufferedWriter(new FileWriter("src/testUser1/someFile.txt", true));	 
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String line = String.format("%-15s%-25s%", command,timeStamp)+username+","+filename+","+other+"\n";
		br.write(line);
		br.close();
	}
	public static void addDataFile(String username,String filename, String pathToFile) throws IOException { 
		File file = new File(pathToFile);
		BufferedWriter br = new BufferedWriter(new FileWriter(path, true));	 
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String line = username+"::"+filename+"::"+timeStamp+"::"+pathToFile+"::"+file.length()+"::"+"0"+"\n";
		br.write(line);
		br.close();
	}
	public static void deleteDataFileLog(String username,String filename ){ 
		try {
			File log = new File(path);// still need to figure out node pathing
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
	public static void accessDataFileLog(String username,String filename, String pathToFile) throws IOException { 
		try {
			File log = new File(path);// still need to figure out node pathing
			File tempFile = new File(log.getAbsolutePath() + ".tmp");//temp file
			BufferedReader br = new BufferedReader(new FileReader(log)); 
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
	  
			String line = null;
	  
			while ((line = br.readLine()) != null) {
	  
			  if (!getLogFileName(line).equals(filename)) {
	  
				pw.println(line);
				pw.flush();
			  }else{
				
				pw.println(getAccessesCount(line));
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
	public static String getAccessesCount(String line){
		
		String[] split = line.split("::");
		int temp = Integer.parseInt(split[5]);
		temp++;
		String count = Integer.toString(temp);
		String update = "";
		for(int i = 0; i<split.length-1;i++){
			update = update + split[i]+"::";
		}
		update = update + count;
		return update;
	}
	public static String getLogFileName(String line){
		String[] split = line.split("::");
		return split[1];
	}
	public static void printMessage(String message) {
		System.out.println(message);
	}

	public static void writeFileToDisk(byte[] b ,String filename,String username,String path) throws IOException {
		if(b== null) {
			System.out.println("File Write Null");
		}
		else {
		File saved = new File(filename);
		Files.write(saved.toPath(),b);
		updateDataLog(username,filename,"[insert]",(saved.length()+","+path));
		addDataFile(username,filename,path);
		}
	}
}