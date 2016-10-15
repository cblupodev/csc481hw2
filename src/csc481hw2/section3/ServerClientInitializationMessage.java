package csc481hw2.section3;

// The initialization message the server sends to the client
// This is used because we haven't implemented an event system yet
// I'm passing primitive because if I send the whole objects it really slows down the message passing

public class ServerClientInitializationMessage {

	public int windowWidth = 600;
	public int windowHeight = 400;
	public float[] rectFoundation1;
	public float[] rectFoundation2;
	public float[] rectFloat;
	
}
