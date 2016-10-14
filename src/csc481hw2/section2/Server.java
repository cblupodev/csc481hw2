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
	private Physics physics;

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
		EnvironmentShape rectFoundation1 = new EnvironmentShape("rect", new float[] {0, windowHeight*.9f, windowWidth*.75f, windowHeight*.1f});
		EnvironmentShape rectFoundation2 = new EnvironmentShape("line", new float[] {windowWidth - (windowWidth*.15f), windowHeight*.9f, windowWidth*.15f, windowHeight*.1f});
		
		EnvironmentShape boundaryLeft = new EnvironmentShape("line", new float[] {0, 0, 0, windowHeight});
		EnvironmentShape boundaryRight = new EnvironmentShape("line", new float[] {windowWidth, 0, windowWidth, windowHeight});
		EnvironmentShape rectPit = new EnvironmentShape("rect", new float[] {
				rectFoundation1.shape[2]+20, 
				rectFoundation1.shape[1],
				windowWidth - (rectFoundation1.shape[2]+rectFoundation2.shape[2]) - 20, 
				rectFoundation1.shape[3]
		});

		// add collidable stuff to the physics component
		physics = new Physics();
		physics.floatingPlatform = floatingPlatform;
		physics.addToEnvironmentShapes(boundaryLeft);
		physics.addToEnvironmentShapes(boundaryRight);
		physics.addToEnvironmentShapes(rectPit);
		
		
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
					c.physics = this.physics;
					
					// send the non changing values to the client
					ServerClientInitializationMessage scim = new ServerClientInitializationMessage();
					scim.rectFloat = floatingPlatform.shape;
					scim.rectFoundation1 = rectFoundation1.shape;
					scim.rectFoundation2 = rectFoundation2.shape;
					scim.windowWidth = windowWidth;
					scim.windowHeight = windowHeight;
					out.println(gson.toJson(scim, ServerClienInitializationtMessageType));
				} else {
					c = characters.get(i);
					characters.set(i, readInputFromClient(i, c, inStream.get(i), out)); // read input from client
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
	private void writeMessageToClient(PrintWriter writer) {
		ServerClientMessage message = new ServerClientMessage();
		floatingPlatform.update();
		physics.floatingPlatform = floatingPlatform; // update the platform in the physics component
		message.floatPlatformShapeMessage = floatingPlatform.shape;
		for (Character c : characters) {
			message.cShapes.add(c.shape);
			message.cJumping.add(c.jumping);
			message.cjumpingAngle.add(c.jumpingAngle);
		}
		writer.println(gson.toJson(message, ServerClientMessageType));
	}

	private Character readInputFromClient(int i, Character c, BufferedReader r, PrintWriter writer) {
		try {
			if (r.ready()) {
				String message = r.readLine();
				boolean keypressed = false;
				keypressed = c.updateInput(message);
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
