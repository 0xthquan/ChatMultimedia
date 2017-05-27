package shareDesktop;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import client.Command;
import server.Server;

public class SD_Client {

	private JFrame frame;
	Socket s;
	boolean run = true;
	int sdSocket;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public SD_Client(int sdSocket) {
		this.sdSocket = sdSocket;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 500);
		frame.getContentPane().setLayout(null);
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 11, 764, 439);
		frame.getContentPane().add(panel);
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		frame.setSize(800, 400);
		frame.pack();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGap(0, 806, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGap(0, 486, Short.MAX_VALUE));
		panel.setLayout(gl_panel);
		
		System.out.println(sdSocket);

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (run) {
						s = new Socket("localhost", sdSocket);
						BufferedImage img = ImageIO.read(s.getInputStream());
						panel.getGraphics().drawImage(img, 0, 0, panel.getWidth(), panel.getHeight(), null);
						s.close();
						
						try{
							Thread.sleep(1);
						} catch (Exception e){
							
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		
		thread.start();
		frame.addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                
                try {
					s.close();
					run = false;
					frame.dispose();
					e.getWindow().dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
		});
	}
	
}
