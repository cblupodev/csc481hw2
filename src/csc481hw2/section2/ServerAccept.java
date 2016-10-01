package csc481hw2.section2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
				
				PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
				// pass the object streams to the server
				Server.inStream.add(reader); 
			    Server.outStream.add(writer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
