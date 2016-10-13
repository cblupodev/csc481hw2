package csc481hw2.section2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import csc481hw2.section2.Server;
import csc481hw2.section2.ServerAccept;
import processing.core.PApplet;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Server {
	
	public static int windowWidth = 600;
	public static int windowHeight = 400;
	
	public static CopyOnWriteArrayList<Character> characters = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<BufferedReader> inStream = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<PrintWriter> outStream = new CopyOnWriteArrayList<>();
	public FloatingPlatform floatingPlatform = new FloatingPlatform(windowWidth, windowHeight);
	private Physics physics = new Physics();
	
	private Gson gson;
	private Type ServerClienInitializationtMessageType;
	private Type ServerClientMessageType;
	
	public static void main(String[] args) {
		Server m = new Server();
		m.run();
		PApplet.main("csc481hw2.section2.Server");
	}
	
	public void run() {
		// temps
		float[] rectFoundation1 = new float[] {0, windowHeight*.9f, windowWidth*.75f, windowHeight*.1f};
		float[] rectFoundation2 = new float[] {windowWidth - (windowWidth*.15f), windowHeight*.9f, windowWidth*.15f, windowHeight*.1f};
		
		float[] boundaryLeft = new float[] {0, 0, 0, windowHeight};
		float[] boundaryRight = new float[] {windowWidth, 0, windowWidth, windowHeight};
		float[] rectFloat = new FloatingPlatform(windowWidth, windowHeight).shape;
		float[] rectPit = new float[] {
				rectFoundation1[2]+20, 
				rectFoundation1[1],
				windowWidth - (rectFoundation1[2]+rectFoundation2[2]) - 20, 
				rectFoundation1[3]
		};
		
		// add collidable stuff to the physics component
		physics.addToCollidables(boundaryLeft);
		physics.addToCollidables(boundaryRight);
		physics.addToCollidables(rectFloat);
		physics.addToCollidables(rectPit);
		
		
		gson = new Gson();
		ServerClienInitializationtMessageType = new TypeToken<ServerClientInitializationMessage>() {}.getType();
		ServerClientMessageType = new TypeToken<ServerClientMessage>() {}.getType();
		
		// start the thread that accepts incoming connections
		Thread t = new Thread(new ServerAccept());
		t.start();
		
		// read from clients
		int frame = -1;
		PrintWriter out;
		Character c;
		while (true) { // never stop looking
			frame++;
			for (int i = 0; i < inStream.size(); i++) { // iterate over the client streams
				out = outStream.get(i);
				// initialize the agent if the number of streams and agents aren't the same size
				if (characters.size() != inStream.size()) { // add a character
					characters.add(i, new Character(windowWidth, windowHeight));
					c = characters.get(i);
					Random r = new Random();
					c.setColor(new int[] {r.nextInt(255), r.nextInt(255), r.nextInt(255)});
					
					// send the non changing values to the client
					ServerClientInitializationMessage scim = new ServerClientInitializationMessage();
					scim.rectFloat = rectFloat;
					scim.rectFoundation1 = rectFoundation1;
					scim.rectFoundation2 = rectFoundation2;
					scim.windowWidth = windowWidth;
					scim.windowHeight = windowHeight;
					out.println(gson.toJson(scim, ServerClienInitializationtMessageType));
				} else {
					c = characters.get(i);
					//if (frame % 100 == 0) {
						characters.set(i, readInputFromClient(i, c, inStream.get(i), out)); // read input from client
					//}
					if (frame % 10000 == 0) { // need the frames or else it will update everything to quickly before you can read input
						characters.set(i, c.updateCharacter(i, c, out, windowHeight));
						writeMessageToClient(out);
					}
				}
			}
		}
		
	}
	
	
	// write a message to the client
	// mostly including updated info to draw
	ServerClientMessage message = new ServerClientMessage();
	private void writeMessageToClient(PrintWriter writer) {
		floatingPlatform.update();
		message.floatPlatformMessage = floatingPlatform;
		message.charactersMessage = characters;
		writer.println(gson.toJson(message, ServerClientMessageType));
	}

	private Character readInputFromClient(int i, Character c, BufferedReader r, PrintWriter writer) {
		try {
			if (r.ready()) {
				String message = r.readLine();
				boolean keypressed = false;
				if(message.equals("LEFT")) {
					c.getShape()[0] -= 5; // move x position left
					keypressed = true;
				}
				if (message.equals("RIGHT")) {
					c.getShape()[0] += 5; // move x position right
					keypressed = true;			
				}
				if (message.equals("SPACE")) {
					keypressed = true;
					if (c.isJumping() == false) {
						c.setJumping(true);
					}
				}
				if (keypressed) {
					characters.set(i, c);
					return c;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return c;
		
	}
}
