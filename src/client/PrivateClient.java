package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Font;

public class PrivateClient extends JFrame implements ActionListener {
	private JButton btnAddFile, btnShareDesktop;
	/* client */
	public JFrame frame;
	private JTextField textField;
	private JButton btnLogOut, btnSend, btnClear;
	public JTextArea msgSend;
	public JTextPane msgArea;
	private JFileChooser fileChooser;
	private Client client;
	String rgb = "style=\"color: rgb(64,128,255);\"";
	private String pUser;

	public PrivateClient(String pUser, Client client) {
		initialize();
		this.pUser = pUser;
		this.client = client;
		frame.setTitle("Chat with " + pUser);
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		/* Frame Private Client */
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(102, 102, 102));
		frame.setBounds(100, 100, 600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Name: ");
		lblName.setForeground(new Color(255, 255, 255));
		lblName.setBounds(26, 11, 46, 14);
		frame.getContentPane().add(lblName);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(67, 8, 280, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		btnLogOut = new JButton("Log out");
		btnLogOut.setBounds(356, 7, 81, 23);
		frame.getContentPane().add(btnLogOut);

		btnSend = new JButton("Send");
		btnSend.setBounds(420, 337, 74, 50);
		frame.getContentPane().add(btnSend);

		btnClear = new JButton("Clear");
		btnClear.setBounds(504, 337, 70, 51);
		frame.getContentPane().add(btnClear);

		msgSend = new JTextArea();
		msgSend.setBounds(26, 337, 384, 50);
		msgSend.setLineWrap(true);
		msgSend.setWrapStyleWord(true);
		frame.getContentPane().add(msgSend);

		msgArea = new JTextPane();
		msgArea.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		msgArea.setBounds(26, 62, 384, 225);
		msgArea.setEditable(false);
		frame.getContentPane().add(msgArea);
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
		scrollPane.setBounds(26, 62, 545, 225);
		frame.getContentPane().add(scrollPane);

		btnAddFile = new JButton("Add Files");
		btnAddFile.setBounds(127, 298, 104, 28);
		frame.getContentPane().add(btnAddFile);

		btnShareDesktop = new JButton("Share Desktop");
		btnShareDesktop.setBounds(254, 298, 134, 28);
		frame.getContentPane().add(btnShareDesktop);

		btnLogOut.addActionListener(this);
		btnSend.addActionListener(this);
		btnClear.addActionListener(this);
		btnAddFile.addActionListener(this);
		btnShareDesktop.addActionListener(this);
	}

	protected void getFilefromServer(String fileName) {
		client.sendData(new Data(Command.GET_FILE, fileName, null, null, null));
	}

	public void processData(Data data) {
		switch (data.command) {
		case Command.CHECK_LOGIN:
			break;
		default:
			break;
		}
	}

	public void sendFile() {

	}

	public void exit() {
		frame.dispose();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSend) {
			if (!msgSend.getText().equals("")) {
				client.sendData(
						new Data(Command.SEND_MESSAGE_P, msgSend.getText() + "\n", client.txtName.getText(), pUser));
				appendMsgArea("<b "+rgb+">&lt; Me &gt;</b>: " + msgSend.getText() + "\n");
				msgSend.setText("");
			}
		} else if (e.getSource() == btnClear) {
			this.msgSend.setText("");
		} else if (e.getSource() == btnShareDesktop) {
			client.sendData(new Data(Command.SHARE_DESKTOP, "", client.txtName.getText(), pUser));
		} else if (e.getSource() == btnLogOut) {
			exit();
		} else if (e.getSource() == btnAddFile){
			fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Choose a File");
			int result = fileChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				byte[] buffer = Client.getByteFile(file); // Noi dung file duoc gui
				client.sendData(new Data(Command.P_SEND_FILE, file.getName(), buffer, client.txtName.getText(), pUser));
				String url = "file://" + file.getName();
				appendMsgArea("<b "+rgb+">&lt; Me &gt;</b>: " + "<a href='" + url + "'>" + file.getName() + "</a>");
			}
		}
	}
	
	public boolean confirmDialog(String sender){
		int option = JOptionPane.showConfirmDialog(null, "Would you like to see " +sender+ "'s Desktop" ,
				"Share Desktop", JOptionPane.YES_NO_OPTION);
		if(option == JOptionPane.YES_OPTION){
			return true;
		} else {
			return false;
		}
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
	
	
}
