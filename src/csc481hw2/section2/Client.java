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
	private Character lastCharacter;
	
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
	        type = new TypeToken<CopyOnWriteArrayList<Character>>() {}.getType();
			
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
	
	public void draw() {
		// TODO get inputs and send them to the server
		// TODO read and write to the server
		// read the character object from the server. the server does the updating
		sendInputToServer();
		Character c = readCharacterFromServer();
		
		// render -->
		background(0); // reset the background each frame
		drawing.drawFill(new int[] {221,221,221}); // light gray
		drawing.drawRect(foundation);
		drawing.drawRect(floatRect);
		drawing.drawFill(new int[] {255,255,255}); // white
		if (c != null) {
			drawing.drawRect(c.getShape());
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

	// read an updated character from the server
	private Character readCharacterFromServer() {
		try {
			if (reader.ready()) {
				String i = reader.readLine();
				CopyOnWriteArrayList<Character> c = gson.fromJson(i,type);
				System.out.println(i);
				//lastCharacter = c;
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lastCharacter;
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
