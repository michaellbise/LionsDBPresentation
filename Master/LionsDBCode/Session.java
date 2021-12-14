package LionsDBCode;
import java.io.Serializable;
import java.io.File;

public class Session implements Serializable{
	private String filename;
	private String username;
	private String passwd;
	private String ClientIP;
	private String MasterServerIP;
	private String Command;/// the command we use to instruct the servers
	private File File; // the file object to be sent if applicable
	private Object Message;
	private byte[] b;
	public Object getMessage() {
		return Message;
	}
	public byte[] getData() {
		return b;
	}
	public void setData(byte[] b) {
		this.b = b;
	}
	public void setMessage(Object message) {
		Message = message;
	}
	public Session() {
		
	}
	public Session(String username) {
		this.username = username;
	}
	public Session(String username,String passwd,String ClientIP,String MasterServerIP,String Command,File File) {
		
		this.username = username;
		this.passwd = passwd;
		this.ClientIP = ClientIP;
		this.MasterServerIP = MasterServerIP;
		this.Command = Command;
		this.File = File;	
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public void setFile(File file) {	
		this.File = file;
		}
	public String getCommand() {
		return Command;
	}
	public void setCommand(String command) {
		Command = command;
	}
	public Object getFile() {
		return File;
	}
	public void setFile(Object File) {
		this.File = (java.io.File) File;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getClientIP() {
		return ClientIP;
	}
	public void setClientIP(String ClientIP) {
		this.ClientIP = ClientIP;
	}
	public String getMasterServerIP() {
		return MasterServerIP;
	}
	public void setMasterServerIP(String MasterServerIP) {
		this.MasterServerIP = MasterServerIP;
	}

}
