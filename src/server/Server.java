package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.plaf.ListUI;

import client.Command;
import client.Data;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ShutdownChannelGroupException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;

public class Server extends JFrame implements ActionListener {

	private JFrame frame;
	public JTextArea statusArea;
	ServerSocket serverSocket;

	private JButton btnClose;

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
		// folderStoreFilePath =
		// this.getClass().getResource("StoreFile").toString().replaceAll("[/]",
		// "\\\\").substring(6);
		folderStoreFilePath = "F:\\Documents";
		System.out.println(folderStoreFilePath);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 540, 484);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblStatus = new JLabel("Server Control Center");
		lblStatus.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		lblStatus.setBounds(160, 11, 225, 45);
		frame.getContentPane().add(lblStatus);

		statusArea = new JTextArea();
		statusArea.setEditable(false);
		statusArea.setForeground(new Color(0, 0, 0));
		statusArea.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		statusArea.setLineWrap(true);
		statusArea.setBackground(SystemColor.control);
		statusArea.setBounds(24, 112, 477, 269);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		statusArea
				.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		frame.getContentPane().add(statusArea);

		btnClose = new JButton("Close Server");
		btnClose.addActionListener(this);
		btnClose.setBounds(174, 392, 181, 29);
		frame.getContentPane().add(btnClose);

		JLabel lblNewLabel = new JLabel(
				"________________________________________________________________________________");
		lblNewLabel.setBounds(24, 67, 490, 14);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblConsole = new JLabel("Console:");
		lblConsole.setBounds(24, 92, 67, 14);
		frame.getContentPane().add(lblConsole);
	}

	public void go() {
		try {
			listUser = new Hashtable<String, ClientConnect>();
			serverSocket = new ServerSocket(9999);
			this.statusArea.append("Server is started!\n");
			while (true) {
				Socket s = serverSocket.accept();
				new ClientConnect(this, s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendAll(int command, String sender, String msg) {
		Enumeration<String> e = listUser.keys();
		String user;
		ClientConnect c;
		while (e.hasMoreElements()) {
			user = e.nextElement();
			c = listUser.get(user);
			if (!user.equals(sender)) {
				c.sendData(new Data(command, msg, sender, ""));
			}
		}
	}

	public void sendPrivate(Data data) {
		ClientConnect c = listUser.get(data.receiver);
		c.sendData(data);
	}

	public void updateListUser(String from) {
		Enumeration<String> e = listUser.keys();
		String name = null;
		while (e.hasMoreElements()) {
			name = e.nextElement();
			// System.out.println(name);
			if (!name.equals(from))
				listUser.get(name).sendData(new Data(Command.UPDATE_LIST, from));
		}
	}

	public String getAllName() {
		Enumeration<String> e = listUser.keys();
		String name = "";
		while (e.hasMoreElements()) {
			name += e.nextElement() + " ";
		}
		return name;
	}

	public void deleteUser(String from) {
		Enumeration<String> e = listUser.keys();
		String name = null;
		while (e.hasMoreElements()) {
			name = e.nextElement();
			// System.out.println(name);
			if (!name.equals(from))
				listUser.get(name).sendData(new Data(Command.LOG_OUT, from));
		}
	}

	public byte[] getByteFile(File file) {
		try {
			byte[] buffer = new byte[(int) file.length()];
			DataInputStream din = new DataInputStream(new FileInputStream(file));
			din.readFully(buffer);

			return buffer;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void getFile(Data data, String sender) {
		ClientConnect c = listUser.get(sender);
		c.sendData(data);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnClose){
			System.exit(0);
		} 
	}
	
	public static int getSocket(){
		Random r=new Random();
        int i;
        i=r.nextInt(1024);
        System.out.println(" Random : " + i);
        return i;
	}
}
