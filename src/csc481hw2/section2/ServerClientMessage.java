package csc481hw2.section2;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;

public class ServerClientMessage {
	FloatingPlatform floatPlatformMessage;
	CopyOnWriteArrayList<Character> charactersMessage = new CopyOnWriteArrayList<>();
	private Gson gson;
	private Type ServerClientMessage;
	
	public void send(PrintWriter writer, FloatingPlatform floatingPlatform, CopyOnWriteArrayList<Character> characters) {
		floatPlatformMessage = floatingPlatform;
		charactersMessage = characters;
		writer.println(gson.toJson(this, ServerClientMessage));
	}
}