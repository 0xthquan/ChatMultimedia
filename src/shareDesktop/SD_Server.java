package shareDesktop;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import client.Command;
import server.Server;

public class SD_Server {
	public static boolean run = true;
	public static ServerSocket ssocket;
	public static Socket s;

	public static void go(int sdSocket) {
		System.out.println(sdSocket);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Robot rob = new Robot();
					Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
					while (run) {
						
						ssocket = new ServerSocket(sdSocket);
						s = ssocket.accept();

						BufferedImage img = rob
								.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						ImageIO.write(img, "png", os);
						s.getOutputStream().write(os.toByteArray());
						ssocket.close();
						try {
							Thread.sleep(1);
						} catch (Exception e) {

						}

					}

				} catch (AWTException e) {
					run = false;
					try {
						s.close();
						ssocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} catch (IOException e) {
					run = false;
					try {
						s.close();
						ssocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		thread.start();
	}

}
