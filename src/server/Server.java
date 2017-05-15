package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.plaf.ListUI;

import client.Command;
import client.Data;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class Server {

	private JFrame frame;
	public JTextArea statusArea;
	ServerSocket serverSocket;
	
	public static String folderStoreFilePath;
	public Hashtable<String, ClientConnect> listUser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Server server = new Server();
		server.frame.setVisible(true);
		server.go();
	}

	/**
	 * Create the application.
	 */
	public Server() {
		initialize();
//		folderStoreFilePath = this.getClass().getResource("StoreFile").toString().replaceAll("[/]", "\\\\").substring(6);
		folderStoreFilePath = "F:\\Documents";
		System.out.println(folderStoreFilePath);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(10, 11, 46, 14);
		frame.getContentPane().add(lblStatus);
		
		statusArea = new JTextArea();
		statusArea.setEditable(false);
		statusArea.setText("");
		statusArea.setBounds(10, 36, 364, 263);
		frame.getContentPane().add(statusArea);
		
		JButton btnNewButton = new JButton("Close Server");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnNewButton.setBounds(104, 321, 181, 29);
		frame.getContentPane().add(btnNewButton);
	}
	
	public void go(){
		try {
			listUser = new Hashtable<String, ClientConnect>();
			serverSocket = new ServerSocket(9999);
			statusArea.append("Server is started!\n");
			while(true){
				Socket s = serverSocket.accept();
				new ClientConnect(this, s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendAll(int command, String sender, String msg){
		Enumeration<String> e = listUser.keys();
		String user;
		ClientConnect c;
		while(e.hasMoreElements()){
			user = e.nextElement();
			c = listUser.get(user);
			if(!user.equals(sender)){
				c.sendData(new Data(command, msg, sender, ""));
			}
		}
	}
	
	public void sendPrivate(Data data){
		ClientConnect c = listUser.get(data.receiver);
		c.sendData(data);
	}
	
	public void updateListUser(String from){
		Enumeration<String> e = listUser.keys();
		String name=null;
		while(e. hasMoreElements()){
			name= e.nextElement();
			//System.out.println(name);
			if(!name.equals(from)) listUser.get(name).sendData(new Data(Command.UPDATE_LIST,from));
		}
	}
	
	public String getAllName(){
		Enumeration<String> e = listUser.keys();
		String name="";
		while(e. hasMoreElements()){
			name+= e.nextElement()+" ";
		}
		return name;
	}
	
	public void deleteUser(String from){
		Enumeration<String> e = listUser.keys();
		String name=null;
		while(e. hasMoreElements()){
			name= e.nextElement();
			//System.out.println(name);
			if(!name.equals(from)) listUser.get(name).sendData(new Data(Command.LOG_OUT,from));
		}
	}

}
