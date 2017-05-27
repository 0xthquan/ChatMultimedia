package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import javax.net.ssl.SSLEngineResult.Status;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import client.Command;
import client.Data;

public class ClientConnect extends Thread {
	public Socket s;
	public Server server;
	private ObjectOutputStream toClient;
	private ObjectInputStream fromClient;
	private boolean run;
	private String user;
	String rgb = randColor();

	public ClientConnect(Server server, Socket s) {
		this.server = server;
		this.s = s;
		try {
			toClient = new ObjectOutputStream(s.getOutputStream());
			fromClient = new ObjectInputStream(s.getInputStream());
			run = true;
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (run) {
			Data data = getData();
			processData(data);
		}
	}

	public void processData(Data data) {
		switch (data.command) {
		case Command.CHECK_LOGIN:
			user = data.msg;
			checkUser(user);
			break;
		case Command.SEND_MESSAGE:
			server.sendAll(Command.SEND_MESSAGE,user, "<b "+rgb+">&lt; "+ user + " &gt;</b>: " + ": " + data.msg);
			break;
		case Command.EXIT:
			run = false;
			exit(user);
			break;
		case Command.SEND_FILE:
			String fileName = data.fileName;
			byte[] fileContent = data.fileContent;
			FileOutputStream fileOut;
			try {
				fileOut = new FileOutputStream(server.folderStoreFilePath + "\\" + fileName);
				fileOut.write(fileContent);
				fileOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			server.sendAll(Command.SEND_FILE, "<b "+rgb+">&lt; "+ user + " &gt;</b>: ", fileName);
			
			break;
		case Command.GET_FILE:
			fileName = data.fileName;
			fileContent = server.getByteFile(new File(server.folderStoreFilePath + "\\" + fileName));
			server.getFile(new Data(Command.GET_FILE, fileName, fileContent, null, null), this.user);	
			break;
		case Command.PRIVATE_CHAT:

			break;
		case Command.P_CHAT_REQUEST:
			server.sendPrivate(new Data(Command.P_CHAT_REQUEST, "", this.user, data.receiver));
			break;
		case Command.P_CHAT_YES:
			server.sendPrivate(new Data(Command.P_CHAT_YES, "", this.user, data.receiver));
			break;
		case Command.P_CHAT_NO:
			server.sendPrivate(new Data(Command.P_CHAT_NO, "", this.user, data.receiver));
			break;
		case Command.SEND_MESSAGE_P:
			server.sendPrivate(new Data(Command.SEND_MESSAGE_P, data.msg, "<b "+rgb+">&lt; "+ data.sender + " &gt;</b> ", data.receiver));
			break;
		case Command.SHARE_DESKTOP:
			server.sendPrivate(new Data(Command.SHARE_DESKTOP, "", data.sender, data.receiver));
			break;
		case Command.SD_YES:
			server.sendPrivate(new Data(Command.SD_YES, data.msg, data.sender, data.receiver));
			break;
		case Command.P_SEND_FILE:
			fileName = data.fileName;
			fileContent = data.fileContent;
			try {
				fileOut = new FileOutputStream(server.folderStoreFilePath + "\\" + fileName);
				fileOut.write(fileContent);
				fileOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			server.sendPrivate(new Data(Command.P_SEND_FILE, fileName, "<b "+rgb+">&lt; "+ data.sender + " &gt;</b> ", data.receiver));
			
			break;
		default:
			break;
		}
	}

	private void checkUser(String user) {
		if (!server.listUser.containsKey(user)) {
			server.statusArea.append(user + " connected!\n");
			server.listUser.put(user, this);
			server.sendAll(Command.SEND_MESSAGE,user, user + " join to room!\n");
			server.updateListUser(user);
			sendData(new Data(Command.OK, ""));
			displayAllName();
		} else{
			sendData(new Data(999,""));
		}
	}

	public Data getData() {
		Data data = null;
		try {
			data = (Data) fromClient.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void sendData(Data data) {
		try {
			toClient.writeObject(data);
			toClient.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void closeConnect() {
		try {
			toClient.close();
			fromClient.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void exit(String user) {
		try {
			server.listUser.remove(user);
			server.deleteUser(user);
			server.statusArea.append(user + " Logged out!\n");
			server.sendAll(Command.SEND_MESSAGE,user, "<b "+rgb+">&lt; "+ user + " &gt;</b> " + " Logged out!\n");
			toClient.close();
			fromClient.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void displayAllName() {
		String name = server.getAllName();
		sendData(new Data(Command.LIST_USER, name));
	}
	
	public static String randColor() {
        try {
            Random rn = new Random();
            int range = 255 - 0 + 1;
            int randomNum1 = 0 + rn.nextInt(range);
            int randomNum2 = 0 + rn.nextInt(range);
            String rgb = "style=\"color: rgb(43," +randomNum1+","+randomNum2+");\"";
            return rgb;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
