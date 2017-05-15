package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DataStream extends Thread {
	private boolean run;
	private ObjectInputStream fromServer;
	private Client client;
	private PrivateClient privateClient;
	private boolean isPrivate = false;
	
	public DataStream(Client client, ObjectInputStream fromServer) {
		run = true;
		this.client = client;
		this.fromServer = fromServer;
		isPrivate = false;
		this.start();
	}
	
	public DataStream(PrivateClient privateClient, ObjectInputStream fromServer) {
		run = true;
		this.privateClient = privateClient;
		this.fromServer = fromServer;
		isPrivate = true;
		this.start();
	}
	
	public void run(){
		while(run){
			try {
				Data data = (Data) fromServer.readObject();
				if(isPrivate){
					privateClient.processData(data);
				} else {
					client.processData(data);
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			fromServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopThread(){
		this.run = false;
	}
}
