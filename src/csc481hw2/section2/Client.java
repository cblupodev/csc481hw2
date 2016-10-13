package csc481hw2.section2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import processing.core.PApplet;

public class Client extends PApplet {
	
	private static final int PORT = 6789;
	private Drawing drawing = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private Socket socket = null;
	private String address = "";
	private ServerClientMessage lastMessage;
	
	private Gson gson;
	private Type ServerClientMessage;
	private Type ServerClientInitializationMessage;

	// start the program
	public static void main(String[] args) {
		Client c = new Client();
		c.address = args[0];
		PApplet.main("csc481hw2.section2.Client");
	}

	private int windowWidth;
	private int windowHeight;
	private float[] rectFoundation1;
	private float[] rectFoundation2;
	
	public void settings() {
		try {
			drawing = new Drawing(this);
			gson  = new Gson();
	        ServerClientMessage = new TypeToken<ServerClientMessage>() {}.getType();
	        ServerClientInitializationMessage = new TypeToken<ServerClientInitializationMessage>() {}.getType();
			
			socket = new Socket(address, PORT);
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			initializeFinalValues();
		} catch (IOException e) {
			e.printStackTrace();
		}
		size(windowWidth, windowHeight); // set the window dimensions
	}
	
	public void setup() {
		fill(120,50,240);
	}
	
	// this should only read the first message the server ever sends
	private void initializeFinalValues() {
		try {
			while(!reader.ready()); // wait until it is ready
				String i = reader.readLine();
				ServerClientInitializationMessage initMessage = gson.fromJson(i,ServerClientInitializationMessage);
				rectFoundation1 = initMessage.rectFoundation1;
				rectFoundation2 = initMessage.rectFoundation2;
				windowWidth = initMessage.windowWidth;
				windowHeight = initMessage.windowHeight;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	int j = 0;
	public void draw() {
		j++;
		// read the character object from the server. the server does the updating
		ServerClientMessage message;
		sendInputToServer();
		message = readMessageFromServer();
		
		// render -->
		background(0); // reset the background each frame
		drawing.drawFill(new int[] {221,221,221}); // light gray
		drawing.drawRect(rectFoundation1);
		drawing.drawRect(rectFoundation2);
		
		drawing.drawFill(new int[] {50,50,50}); // light gray
		try {
			FloatingPlatform fp = message.floatPlatformMessage;
			fp.draw(this);
			for (Character c : message.charactersMessage) { // draw the characters
				c.draw(this);
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
	private ServerClientMessage readMessageFromServer() {
		try {
			if (reader.ready()) {
				String i = reader.readLine();
				ServerClientMessage message = gson.fromJson(i,ServerClientMessage);
				//System.out.println(i);
				lastMessage = message;
				return message;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lastMessage;
	}
	


}
