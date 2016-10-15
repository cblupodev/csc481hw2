package csc481hw2.section3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import csc481hw2.section1.Immovable;
import csc481hw2.section1.Movable;
import csc481hw2.section1.Physics;
import csc481hw2.section3.Server;
import csc481hw2.section3.ServerAccept;
import processing.core.PApplet;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Server {
	
	public static int windowWidth = 600;
	public static int windowHeight = 400;
	
	// use the thread safe collections because they are being added and read from different threads
	public static CopyOnWriteArrayList<Character> characters = new CopyOnWriteArrayList<>(); // list of characters
	public static CopyOnWriteArrayList<BufferedReader> inStream = new CopyOnWriteArrayList<>(); // list of socket input streams
	public static CopyOnWriteArrayList<PrintWriter> outStream = new CopyOnWriteArrayList<>(); // list of socket output streams
	public FloatingPlatform floatingPlatform = new FloatingPlatform(windowWidth, windowHeight);
	private Physics physics;
	
	public static ArrayList<Immovable> immovables = new ArrayList<>(); // list of specific objects to collide with
	public static ArrayList<Movable> movables = new ArrayList<>(); // not used yet

	private Gson gson; // google json parser
	private Type ServerClientMessageType; // type for gson parsing
	private Type ServerClientInitializationMessageType; // type for json parsing
	
	public static void main(String[] args) {
		Server m = new Server();
		m.run();
		PApplet.main("csc481hw2.section2.Server");
	}
	
	public void run() {
		// initialize the static objects
		Immovable rectFoundation1 = new Immovable("rect", new float[] {0, windowHeight*.9f, windowWidth*.75f, windowHeight*.1f});
		Immovable rectFoundation2 = new Immovable("line", new float[] {windowWidth - (windowWidth*.15f), windowHeight*.9f, windowWidth*.15f, windowHeight*.1f});
		
		Immovable boundaryLeft = new Immovable("line", new float[] {0, 0, 0, windowHeight});
		Immovable boundaryRight = new Immovable("line", new float[] {windowWidth, 0, windowWidth, windowHeight});
		Immovable rectPit = new Immovable("rect", new float[] {
				rectFoundation1.shape[2]+20, 
				rectFoundation1.shape[1],
				windowWidth - (rectFoundation1.shape[2]+rectFoundation2.shape[2]) - 20, 
				rectFoundation1.shape[3]
		});
		
		immovables.add(boundaryLeft);
		immovables.add(boundaryRight);
		immovables.add(rectPit);
		
		// add collidable stuff to the physics component
		physics = new Physics();
		
		gson = new Gson();
		ServerClientInitializationMessageType = new TypeToken<ServerClientInitializationMessage>() {}.getType();
		ServerClientMessageType = new TypeToken<ServerClientMessage>() {}.getType();
		
		// start the thread that accepts incoming connections
		Thread t = new Thread(new ServerAccept());
		t.start();
		
		// read from clients
		int frame = -1;
		PrintWriter out = null;
		Character c;
		while (true) { // never stop looking
			frame++;
			for (int i = 0; i < inStream.size(); i++) { // iterate over the client streams
				while(outStream.size() != inStream.size()); // busy wait until they are the same size
				out = outStream.get(i);
					
				// initialize the agent if the number of streams and agents aren't the same size
				if (characters.size() != inStream.size()) { // add a character
					characters.add(i, new Character(windowWidth, windowHeight));
					c = characters.get(i);
					// select random character
					Random r = new Random();
					c.color = new int[] {r.nextInt(255), r.nextInt(255), r.nextInt(255)};
					c.physics = this.physics;
					
					// send the non changing values to the client
					ServerClientInitializationMessage scim = new ServerClientInitializationMessage();
					scim.rectFloat = floatingPlatform.shape;
					scim.rectFoundation1 = rectFoundation1.shape;
					scim.rectFoundation2 = rectFoundation2.shape;
					scim.windowWidth = windowWidth;
					scim.windowHeight = windowHeight;
					out.println(gson.toJson(scim, ServerClientInitializationMessageType));
					out.flush();
					System.out.println("init sent");
				} else {
					c = characters.get(i);
					characters.set(i, readInputFromClient(i, c, inStream.get(i), out)); // read input from client
					// UPDATE
					if (frame % 10000 == 0) { // need the frames or else it will update everything to quickly before you can read input
						physics.collision();
						characters.set(i, c.update());
						floatingPlatform.update();
						physics.floatingPlatform = floatingPlatform; // update the platform in the physics component
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
		message.cShapes.clear();
		message.cJumping.clear();
		message.cjumpingAngle.clear();
		message.cColor.clear();
		message.floatPlatformShapeMessage = floatingPlatform.shape;
		for (Character c : characters) {
			message.cShapes.add(c.shape);
			message.cJumping.add(c.jumping);
			message.cjumpingAngle.add(c.jumpingAngle);
			message.cColor.add(c.color);
		}
		writer.println(gson.toJson(message, ServerClientMessageType));
	}

	// read keyboard input from client
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
