package client;

import java.io.Serializable;

public class Data implements Serializable {
	public String msg;
	public int command;
	public String receiver;
	public String sender;
	
	public String fileName;
	public byte[] fileContent;
	
	public Data(int command, String msg) {
		this.command = command;
		this.msg = msg;
	}
	
	public Data(int command, String msg, String sender, String receiver){
		this.command = command;
		this.msg = msg;
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public Data(int command, String fileName, byte[] fileContent){
		this.command = command;
		this.fileName = fileName;
		this.fileContent = fileContent;
	}
	
}