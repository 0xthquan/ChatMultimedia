package client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Capture implements Runnable {

	private AudioInputStream ais;
	private static TargetDataLine targetLine;
	public Thread thread;

	public Capture() {
		thread = new Thread(this);
		
		try {
			AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			if (!AudioSystem.isLineSupported(info)) {
				this.stopCapture();
			}
			targetLine = (TargetDataLine) AudioSystem.getLine(info);
			targetLine.open(format, targetLine.getBufferSize());
			System.out.println("Starting Recording");
			targetLine.start();
			thread.start();

			
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

	public void stopCapture() {
		thread = null;
		targetLine.stop();
		targetLine.close();
		System.out.println("Stop Recording");
	}

	@Override
	public void run() {
		// ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		AudioInputStream audioStream = new AudioInputStream(targetLine);
		File audioFile = new File("record.wav");
		try {
			AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Capture cap = new Capture();
		Scanner s = new Scanner(System.in);
		s.nextLine();
		cap.stopCapture();
		
	}

}
