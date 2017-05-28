package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import server.Server;
import shareDesktop.SD_Client;
import shareDesktop.SD_Server;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;

public class Client extends JFrame implements ActionListener {
	/* login */
	private JFrame frame1;
	private JTextField txtLocalhost;
	private JTextField textField1;
	private JTextField txtchatID;
	private JButton btnOk, btnCancel, btnPrivateChat, btnAddFile;
	/* client */
	private JFrame frame2;
	public JTextField txtName;
	private JButton btnLogOut, btnSend, btnClear;
	private JTextArea msgSend;
	private JList<String> jListUser;
	private JTextPane msgArea;

	private Socket clientSocket;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private DataStream dataStream;
	private String selectedUser;
	
	String rgb = "style=\"color: rgb(64,128,255);\"";
	
	PrivateClient pClient;
	JFileChooser fileChooser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Client client = new Client();
		client.frame1.setVisible(true);
		client.go();
	}

	/**
	 * Create the application.
	 */

	public Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		/* Login */
		frame1 = new JFrame("Client Login");
		frame1.setBounds(100, 100, 269, 211);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.getContentPane().setLayout(null);

		JLabel lblHost = new JLabel("Host:");
		lblHost.setBounds(23, 29, 46, 14);
		frame1.getContentPane().add(lblHost);

		txtLocalhost = new JTextField();
		txtLocalhost.setEditable(false);
		txtLocalhost.setText("localhost");
		txtLocalhost.setBounds(90, 26, 86, 20);
		frame1.getContentPane().add(txtLocalhost);
		txtLocalhost.setColumns(10);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(23, 61, 46, 14);
		frame1.getContentPane().add(lblPort);

		textField1 = new JTextField();
		textField1.setEditable(false);
		textField1.setText("9999");
		textField1.setBounds(90, 58, 86, 20);
		frame1.getContentPane().add(textField1);
		textField1.setColumns(10);

		JLabel lblChatId = new JLabel("Chat ID:");
		lblChatId.setBounds(23, 95, 46, 14);
		frame1.getContentPane().add(lblChatId);

		txtchatID = new JTextField();
		txtchatID.setBounds(90, 92, 138, 20);
		frame1.getContentPane().add(txtchatID);
		txtchatID.setColumns(10);

		btnOk = new JButton("OK");
		btnOk.setBounds(33, 138, 89, 23);
		frame1.getContentPane().add(btnOk);

		btnCancel = new JButton("CANCEL");
		btnCancel.setBounds(132, 138, 89, 23);
		frame1.getContentPane().add(btnCancel);

		btnOk.addActionListener(this);
		btnCancel.addActionListener(this);

		/* Frame Client */
		frame2 = new JFrame();
		frame2.getContentPane().setBackground(new Color(224, 255, 255));
		frame2.setBounds(100, 100, 600, 450);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Name: ");
		lblName.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblName.setBounds(26, 11, 46, 14);
		frame2.getContentPane().add(lblName);

		txtName = new JTextField();
		txtName.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 13));
		txtName.setForeground(new Color(255, 0, 0));
		txtName.setEditable(false);
		txtName.setBounds(67, 8, 280, 22);
		frame2.getContentPane().add(txtName);
		txtName.setColumns(10);

		JLabel lblOnlineList = new JLabel("Member List");
		lblOnlineList.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblOnlineList.setBounds(455, 37, 91, 14);
		frame2.getContentPane().add(lblOnlineList);

		btnLogOut = new JButton("Log out");
		btnLogOut.setBounds(356, 7, 81, 23);
		frame2.getContentPane().add(btnLogOut);

		btnSend = new JButton("Send");
		btnSend.setBounds(420, 337, 74, 50);
		frame2.getContentPane().add(btnSend);

		btnClear = new JButton("Clear");
		btnClear.setBounds(504, 337, 70, 51);
		frame2.getContentPane().add(btnClear);

		msgSend = new JTextArea();
		msgSend.setBackground(new Color(255, 255, 255));
		msgSend.setBounds(26, 337, 384, 50);
		msgSend.setLineWrap(true);
		msgSend.setWrapStyleWord(true);
		frame2.getContentPane().add(msgSend);

		msgArea = new JTextPane();
		msgArea.setBounds(26, 62, 384, 225);
		msgArea.setEditable(false);
		frame2.getContentPane().add(msgArea);
		// msgArea.setContentType("text/html");
		msgArea.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));

		msgArea.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String fileName = e.getURL().toString().substring(7);
					getFilefromServer(fileName);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(msgArea);
		scrollPane.setBounds(26, 62, 384, 225);
		frame2.getContentPane().add(scrollPane);

		DefaultListModel<String> model = new DefaultListModel<>();
		jListUser = new JList<String>(model);
		jListUser.setFont(new Font("Tahoma", Font.BOLD, 14));
		jListUser.setBounds(420, 62, 154, 225);
		frame2.getContentPane().add(jListUser);

		JScrollPane scrollPane1 = new JScrollPane(jListUser);
		scrollPane1.setBounds(420, 62, 154, 225);
		frame2.getContentPane().add(scrollPane1);

		btnAddFile = new JButton("Add Files");
		btnAddFile.setBounds(126, 298, 118, 28);
		frame2.getContentPane().add(btnAddFile);

		btnPrivateChat = new JButton("Private Chat");
		btnPrivateChat.setBounds(447, 298, 110, 23);
		frame2.getContentPane().add(btnPrivateChat);

		btnLogOut.addActionListener(this);
		btnSend.addActionListener(this);
		btnClear.addActionListener(this);
		btnPrivateChat.addActionListener(this);
		btnAddFile.addActionListener(this);
		
	}

	public void go() {
		try {
			clientSocket = new Socket("localhost", 9999);
			toServer = new ObjectOutputStream(clientSocket.getOutputStream());
			fromServer = new ObjectInputStream(clientSocket.getInputStream());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi kết nối xem lại mạng hoặc server chưa hoạt động", "Message Dialog",
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}

	public void processData(Data data) {
		switch (data.command) {
		case Command.CHECK_LOGIN:
			break;
		case Command.SEND_MESSAGE:
			appendMsgArea(data.msg);
			break;
		case Command.UPDATE_LIST:
			DefaultListModel<String> model = (DefaultListModel<String>) jListUser.getModel();
			model.addElement(data.msg);
			jListUser.setModel(model);
			// txtlistUser.setText(data.msg);
			break;
		case Command.LIST_USER:
			String[] name = data.msg.split("\\s");

			model = (DefaultListModel<String>) jListUser.getModel();
			for (String n : name) {
				model.addElement(n);
			}
			jListUser.setModel(model);
			break;
		case Command.LOG_OUT:

			model = (DefaultListModel<String>) jListUser.getModel();
			model.removeElement(data.msg);
			jListUser.setModel(model);
			break;
		case Command.SEND_FILE:
			String fileName = data.msg;
			String url = "file://" + fileName;
			String msg = "<b "+rgb+">&lt; "+ data.sender + " &gt;</b>: "  + " <a href='" + url + "'>" + fileName + "</a>";
			appendMsgArea(msg);
			break;
		case Command.GET_FILE:
			fileName = data.fileName;
			byte[] fileContent = data.fileContent;	
			fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File(fileName));
			int result = fileChooser.showSaveDialog(null);
			if(result == JFileChooser.APPROVE_OPTION){
						
				FileOutputStream fileOut;
				
				String path = fileChooser.getCurrentDirectory().toString();			
				File file = new File(path + "\\" + fileName );
				System.out.println(file.getAbsolutePath());
//				fileChooser.setCurrentDirectory(file);

				try {
					fileOut = new FileOutputStream(path + "\\" + fileName);
					fileOut.write(fileContent);
					fileOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}	
			break;
		case Command.P_CHAT_REQUEST:
			String sender = data.sender;

			int option = JOptionPane.showConfirmDialog(null, sender + " Want to private chat with you, Do u want?",
					"Chat private", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				model = (DefaultListModel<String>) jListUser.getModel();
				pClient = new PrivateClient(sender, this);
				sendData(new Data(Command.P_CHAT_YES, "", "", sender));
			} else if (option == JOptionPane.NO_OPTION) {
				sendData(new Data(Command.P_CHAT_NO, "", "", sender));
			}
			break;
		case Command.P_CHAT_YES:
			pClient = new PrivateClient(data.sender, this);
			break;
		case Command.P_CHAT_NO:
			JOptionPane.showMessageDialog(this, data.sender + "dont want to chat private with you!", "Message Dialog",
					JOptionPane.INFORMATION_MESSAGE);
			break;
		case Command.SEND_MESSAGE_P:
			pClient.appendMsgArea(data.sender + ": " + data.msg);
			break;
		case Command.SHARE_DESKTOP:
			if (pClient.confirmDialog(data.sender)) {
				int sdSocket = Server.getSocket();
				SD_Server.go(sdSocket);
				sendData(new Data(Command.SD_YES, Integer.toString(sdSocket), txtName.getText(), data.sender));
			}
			break;
		case Command.SD_YES:
			int sdSocket = Integer.parseInt(data.msg);
			SD_Client sdClient = new SD_Client(sdSocket);
			break;
		case Command.P_SEND_FILE:
			fileName = data.msg;
			url = "file://" + fileName;
			msg = data.sender + ": <a href='" + url + "'>" + fileName + "</a>";
			pClient.appendMsgArea(msg);
			break;
		default:
			break;
		}
	}

	public void sendData(Data data) {
		try {
			toServer.writeObject(data);
			toServer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendFile() {

	}

	public void exit() {
		sendData(new Data(Command.EXIT, ""));
		try {
			toServer.close();
			fromServer.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOk) {
			if (checkLogin(txtchatID.getText())) {
				txtName.setText(txtchatID.getText());
				frame1.dispose();
				frame2.setVisible(true);
				appendMsgArea("Log in successed!");
				dataStream = new DataStream(this, this.fromServer);
			} else {
				JOptionPane.showMessageDialog(this, "This name exist in room. Please choose another one",
						"Message Dialog", JOptionPane.WARNING_MESSAGE);
			}
		} else if (e.getSource() == btnCancel) {
			exit();
			this.exit();
		} else if (e.getSource() == btnSend) {
			if (!msgSend.getText().equals("")) {
				sendData(new Data(Command.SEND_MESSAGE, msgSend.getText()));
				appendMsgArea("<b "+rgb+">&lt; Me &gt;</b>: " + msgSend.getText());
				msgSend.setText("");
			}

		} else if (e.getSource() == btnClear) {
			this.msgSend.setText("");
		} else if (e.getSource() == btnLogOut) {
			dataStream.stopThread();
			exit();
		} else if (e.getSource() == btnPrivateChat) {
			selectedUser = jListUser.getSelectedValue();
			if(selectedUser.equals(txtName.getText())){
				JOptionPane.showMessageDialog(this, "You cant chat with yourself",
						"Message Dialog", JOptionPane.WARNING_MESSAGE);
			} else {
				sendData(new Data(Command.P_CHAT_REQUEST, "", "", selectedUser));
			}
			
		} else if (e.getSource() == btnAddFile) {
			fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Choose a File");
			int result = fileChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				
				File file = fileChooser.getSelectedFile();
				byte[] buffer = getByteFile(file); // Noi dung file duoc gui
				sendData(new Data(Command.SEND_FILE, file.getName(), buffer, null, null));
				String url = "file://" + file.getName();
				appendMsgArea("<b "+rgb+">&lt; Me &gt;</b>: " + "<a href='" + url + "'>" + file.getName() + "</a>");
			}

		}
	}

	public boolean checkLogin(String id) {
		boolean b = true;
		Data data = new Data(Command.CHECK_LOGIN, id);
		if (id.equals("") == true) {
			b = false;
		} else {
			try {
				toServer.writeObject(data);
				;
				data = (Data) fromServer.readObject();
				if (data.command == Command.OK) {
					b = true;
				} else {
					b = false;
				}
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	public static byte[] getByteFile(File file) {
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

	public void appendMsgArea(String s) {
		s = "<br>" + s;
		try {
			HTMLDocument doc = (HTMLDocument) msgArea.getStyledDocument();
			doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), s);
		} catch (IOException | BadLocationException exc) {
			exc.printStackTrace();
		}
	}

	public void getFilefromServer(String fileName) {
		sendData(new Data(Command.GET_FILE, fileName, null, null, null));
	}
	

}
