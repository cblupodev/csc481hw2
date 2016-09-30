package csc481hw2.section2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import csc481hw2.section2.Server;

public class ServerAccept extends Thread {
	
	private static final int PORT = 6789;
	
	public void run() {
        ServerSocket serverSocket = null;
        Socket client = null;
        try {
			serverSocket = new ServerSocket(PORT);
			while (true) { // never stop listening
				System.out.println("server waiting to connect");
				client = serverSocket.accept();
				System.out.println("a client was connected");
				
				ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
				oos.flush(); // stackoverflow said just do it
				ObjectInputStream  ois = new ObjectInputStream(client.getInputStream());
			    
				// pass the object streams to the server
				Server.inStream.add(ois); 
			    Server.outStream.add(oos);
			    
			    Server.agents.add(new Agent());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
