package csc481hw2.section2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.CopyOnWriteArrayList;
import csc481hw2.section2.Server;
import csc481hw2.section2.ServerAccept;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Server {
	
	public static CopyOnWriteArrayList<Character> agents = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<BufferedReader> inStream = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<PrintWriter> outStream = new CopyOnWriteArrayList<>();
	
	private int windowWidth = 600;
	private int windowHeight = 400;
	Gson gson;
	
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
				/*if (agents.get(i).getShape() == null) {
					agents.set(i, new Character(windowWidth, windowHeight));
				}*/
				int read;
				/*if (inStream.get(i).available() != 0) { // check if there is anything to read
					read = inStream.get(i).readInt(); // read a simple message from a client
					System.out.println("message from client:   " + read);
				}*/
				//System.out.println("34578");
				Gson gson = new Gson();
				Type type = new TypeToken<Character>() {}.getType();
				String a = gson.toJson(agents.get(i), type);
					//outStream.get(i).writeObject(new Integer(6));
					//outStream.get(i).writeInt(7);
				
					outStream.get(i).write(a+"\n"); // write character to the client
				outStream.get(i).flush();
			}
		}
		
	}

}
