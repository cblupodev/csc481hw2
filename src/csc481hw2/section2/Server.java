package csc481hw2.section2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

import csc481hw2.section2.Server;
import csc481hw2.section2.ServerAccept;

public class Server {
	
	public static CopyOnWriteArrayList<Agent> agents = new CopyOnWriteArrayList<Agent>();
	public static CopyOnWriteArrayList<ObjectInputStream> inStream = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<ObjectOutputStream> outStream = new CopyOnWriteArrayList<>();
	
	private int windowWidth = 600;
	private int windowHeight = 400;
	
	public static void main(String[] args) {
		Server m = new Server();
		m.run();
	}
	
	public void run() {
		
		// start the thread that accepts incoming connections
		Thread t = new Thread(new ServerAccept());
		t.start();
		
		// read from clients
		while (true) { // never stop looking
			for (int i = 0; i < inStream.size(); i++) { // iterate over the client streams
				// initialize the agent
				if (agents.get(i).getShape() == null) {
					agents.set(i, new Agent(windowWidth, windowHeight));
				}
				int read;
				try {
					if (inStream.get(i).available() != 0) { // check if there is anything to read
						read = inStream.get(i).readInt(); // read a simple message from a client
						System.out.println("message from client:   " + read);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
