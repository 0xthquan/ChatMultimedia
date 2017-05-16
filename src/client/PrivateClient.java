package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class PrivateClient extends JFrame implements ActionListener {
	private JButton btn1, btnFile, btnShareDesktop;
	/* client */
	public JFrame frame;
	private JTextField textField;
	private JButton btnLogOut, btnSend, btnClear;
	public JTextArea msgSend, msgArea;

	private Client client;

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
		frame.setBounds(100, 100, 600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Name: ");
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

		msgArea = new JTextArea();
		msgArea.setBounds(26, 62, 545, 225);
		msgArea.setLineWrap(true);
		msgArea.setWrapStyleWord(true);
		msgArea.setEditable(false);
		frame.getContentPane().add(msgArea);

		JScrollPane scrollPane = new JScrollPane(msgArea);
		scrollPane.setBounds(26, 62, 545, 225);
		frame.getContentPane().add(scrollPane);

		btn1 = new JButton("New button");
		btn1.setBounds(80, 298, 40, 28);
		frame.getContentPane().add(btn1);

		btnFile = new JButton("Add Files");
		btnFile.setBounds(148, 298, 83, 28);
		frame.getContentPane().add(btnFile);

		btnShareDesktop = new JButton("Share Desktop");
		btnShareDesktop.setBounds(263, 298, 116, 28);
		frame.getContentPane().add(btnShareDesktop);

		btnLogOut.addActionListener(this);
		btnSend.addActionListener(this);
		btnClear.addActionListener(this);
		btn1.addActionListener(this);
		btnFile.addActionListener(this);
		btnShareDesktop.addActionListener(this);
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
				msgArea.append("Me: " + msgSend.getText() + "\n");
				msgSend.setText("");
			}
		} else if (e.getSource() == btnClear) {
			this.msgSend.setText("");
		} else if (e.getSource() == btnShareDesktop) {
			client.sendData(new Data(Command.SHARE_DESKTOP, "", client.txtName.getText(), pUser));
		} else if (e.getSource() == btnLogOut) {
			exit();
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
	
	
}
