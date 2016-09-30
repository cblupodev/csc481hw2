package csc481hw2.section1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import processing.core.PApplet;

public class Client extends PApplet {
	
	private static final int PORT = 6789;

	// start the program
	public static void main(String[] args) {
		Client c = new Client();
		c.run(args[0]);
		PApplet.main("csc481hw2.section1.Client");
	}

	private int windowWidth = 600;
	private int windowHeight = 400;
	
	public void run(String address) {
		Socket socket;
		try {
			socket = new Socket(address, PORT);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ObjectInputStream  ois = new ObjectInputStream(socket.getInputStream());
			oos.writeInt(5);oos.flush();
			int timeout = 1;
			
			while (true) {
				Thread.sleep(1000);
				if (timeout++ == 5) break; // if haven't read anything new from the server for a certain amount of time then quit
				System.out.println();
				
				try {
					if (ois.available() != 0) {
						timeout = 1;
						int line = ois.readInt(); // read from the server
						System.out.println("message from server:   " + line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		
	}
	
	public void settings() {
		size(windowWidth, windowHeight); // set the window demensions
		
	}
	
	public void setup() {
		
	}
	
	public void draw() {
	}
}
