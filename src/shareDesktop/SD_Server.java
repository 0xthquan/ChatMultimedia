package shareDesktop;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

public class SD_Server {
	public static void go() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Robot rob = new Robot();
					Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
					while(true){
						
						ServerSocket ssocket = new ServerSocket(1080);
						Socket s = ssocket.accept();
						BufferedImage img = rob.createScreenCapture(new Rectangle(0, 0, 
								(int) d.getWidth(),(int) d.getHeight()));
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						ImageIO.write(img, "png", os);
						s.getOutputStream().write(os.toByteArray());
						ssocket.close();
						try{
							Thread.sleep(1);
						} catch(Exception e){
							
						}
					}
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
	}
}
