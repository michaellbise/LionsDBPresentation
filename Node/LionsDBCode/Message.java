package LionsDBCode;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

public class Message implements Serializable{

	// This class allows for back and forth messages between the client,master and node servers
	// Message type: Really just a string what is being sent
	// Object:
	private String messageType;
	private String message;
	private Object data;
	public Message() {
		//
		messageType ="generic";
		message ="Connection Successful";
	}
	public Message(String messageType,String message,Object data) {
		this.message = message;
		this.messageType = messageType;
		this.data = data;
	}
	public String toString() {
		return messageType+":"+"message";
		
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
