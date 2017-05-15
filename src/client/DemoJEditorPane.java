package client;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;

public class DemoJEditorPane extends JFrame {

	private JFrame frame;
	JTextPane editorPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DemoJEditorPane window = new DemoJEditorPane();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DemoJEditorPane() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		editorPane = new JTextPane();
		editorPane.setEditable(false);
		editorPane.setBounds(36, 22, 346, 209);
		frame.getContentPane().add(editorPane);

		editorPane.setContentType("text/html");
		editorPane.setText("<a href=\"\" >record.wav</a>");
		appendMsgArea("asd");

		editorPane.addHyperlinkListener((HyperlinkEvent event) -> {
			if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				System.out.println("Click and clik");
			}
		});
	}

	public void appendMsgArea(String s) {
		s = "<br>" + s;
		try {
			HTMLDocument doc = (HTMLDocument) editorPane.getStyledDocument();
			doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), s);
		} catch (IOException | BadLocationException exc) {
			exc.printStackTrace();
		}
	}
}
