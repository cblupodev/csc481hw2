package csc481hw2.section3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.naming.RefAddr;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import csc481hw2.section1.Drawing;
import processing.core.PApplet;

public class Client extends PApplet {
	
	private static final int PORT = 6789;
	private Drawing drawing = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private Socket socket = null;
	private String address = "";
	private ServerClientMessage lastMessage;
	private ArrayList<Character> characters = new ArrayList<>(); 
	
	private Gson gson;
	private Type ServerClientMessage;
	private Type ServerClientInitializationMessage;

	// start the program
	public static void main(String[] args) {
		Client c = new Client();
		c.address = args[0];
		PApplet.main("csc481hw2.section3.Client");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		initializeFinalValues();
		size(windowWidth, windowHeight); // set the window dimensions
	}
	
	public void setup() {
		fill(120,50,240);
	}
	
	// this should only read the first message the server ever sends
	private void initializeFinalValues() {
		String i = "";
		try {
				System.out.println("is reader ready?   " + reader.ready());
				i = reader.readLine();
				ServerClientInitializationMessage initMessage = gson.fromJson(i,ServerClientInitializationMessage);
				rectFoundation1 = initMessage.rectFoundation1;
				rectFoundation2 = initMessage.rectFoundation2;
				windowWidth = initMessage.windowWidth;
				windowHeight = initMessage.windowHeight;
				writer.println("initialized");
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	int j = 0;
	FloatingPlatform fp = new FloatingPlatform(windowWidth, windowHeight);
	Character c;
	public void draw() {
			j++;
			// read the character object from the server. the server does the updating
			ServerClientMessage message;
			sendInputToServer();
			message = readMessageFromServer();
			// render -->
			background(0); // reset the background each frame
			drawing.drawFill(new int[] { 221, 221, 221 }); // light gray
			drawing.drawRect(rectFoundation1);
			drawing.drawRect(rectFoundation2);
			drawing.drawFill(new int[] { 50, 50, 50 }); // light gray
			try {
				fp.shape = message.floatPlatformShapeMessage;
				fp.draw(this);
				for (int i = 0; i < message.cShapes.size(); i++) { // draw the characters
					if (message.cShapes.size() > characters.size()) { // if a new client connected and thus character added, then add to the list
						Character c = new Character(windowWidth, windowHeight);
						characters.add(c);
					}
					// update the characters
					c = characters.get(i);
					c.shape = message.cShapes.get(i);
					c.jumping = message.cJumping.get(i);
					c.jumpingAngle = message.cjumpingAngle.get(i);
					c.color = message.cColor.get(i);
					c.draw(this);
				}

				//for (Character c : message.charactersMessage) { // draw the characters
				//	c.draw(this);
				//}
			} catch (NullPointerException e) {
			} 
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
