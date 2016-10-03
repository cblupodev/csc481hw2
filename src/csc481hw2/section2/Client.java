package csc481hw2.section2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import processing.core.PApplet;

public class Client extends PApplet {
	
	private static final int PORT = 6789;
	private Drawing drawing = new Drawing();
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private Socket socket = null;
	private String address = "";
	private ServerClientMessage lastMessage;
	
	private Gson gson;
	private Type type;

	// start the program
	public static void main(String[] args) {
		Client c = new Client();
		c.address = args[0];
		PApplet.main("csc481hw2.section2.Client");
	}

	private int windowWidth = 600;
	private int windowHeight = 400;
	private float[] foundation;
	private float[] floatRect;
	
	public void settings() {
		size(windowWidth, windowHeight); // set the window demensions
	}
	
	public void setup() {		
		try {
			gson  = new Gson();
	        type = new TypeToken<ServerClientMessage>() {}.getType();
			
			socket = new Socket(address, PORT);
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		foundation = new float[] {0, windowHeight*.9f, windowWidth, windowHeight*.1f};
		floatRect = new float[] {windowWidth * .7f, windowHeight*.7f, windowWidth * .2f, windowHeight*.025f};
		fill(120,50,240);
	}
	
	int j = -1;
	public void draw() {
		j++;
		// TODO get inputs and send them to the server
		// TODO read and write to the server
		// read the character object from the server. the server does the updating
		ServerClientMessage message;
		sendInputToServer();
		message = readMessageFromServer();
		
		// render -->
		background(0); // reset the background each frame
		drawing.drawFill(new int[] {221,221,221}); // light gray
		drawing.drawRect(foundation);
		// move the floating rectangle
		try {
			floatRect[0] = message.floatingRectX;
		} catch (NullPointerException e1) { }
		drawing.drawRect(floatRect); // draw the floating rectangle
		//drawing.drawFill(new int[] {50,50,50}); // light gray
		try {
			for (Character i : message.characters) { // draw the characters
				drawing.drawFill(i.getColor());
				drawing.drawRect(i.getShape()); 
			}
		} catch (NullPointerException e) { }
	}
	
	// send keyboard input to the server so it can update character
	private void sendInputToServer() {
		if (keyPressed) { // move the agent if the key is pressed
			if (keyCode == LEFT) {
				writer.println("LEFT");
			}
			if (keyCode == RIGHT) {
				writer.println("RIGHT");
			}
			if (key == ' ') { // begin jumping
				writer.println("SPACE");
			}
		}
		writer.flush();
	}

	// read an updated message from the server
	int i = 0;
	private ServerClientMessage readMessageFromServer() {
		try {
			if (reader.ready()) {
				System.out.println("read from server "+i++);
				String i = reader.readLine();
				ServerClientMessage message = gson.fromJson(i,type);
				//System.out.println(i);
				lastMessage = message;
				return message;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lastMessage;
	}
	
	private class Drawing {	// wrapper to easily call rect() from just passing an array
		public void drawRect(float[] rect) {
			rect(rect[0], rect[1], rect[2], rect[3]);
		}
		// wrapper to easily call line() from just passing an array	
		public void drawLine(float[] line) {
			line(line[0], line[1], line[2], line[3]);
		}
		// wrapper to easily call fill() from just passing an array	
		public void drawFill(int[] rgb) {
			fill(rgb[0], rgb[1], rgb[2]);
		}
		// wrapper to easily call stroke() from just passing an array	
		public void drawStroke(int[] rgb) {
			stroke(rgb[0], rgb[1], rgb[2]);
		}
	}

}
