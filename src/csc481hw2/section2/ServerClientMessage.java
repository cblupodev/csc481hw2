package csc481hw2.section2;

import java.util.concurrent.CopyOnWriteArrayList;

public class ServerClientMessage {
	FloatingPlatform floatPlatformMessage;
	CopyOnWriteArrayList<Character> charactersMessage = new CopyOnWriteArrayList<>();
}