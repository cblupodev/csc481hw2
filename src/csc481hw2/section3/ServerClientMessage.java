package csc481hw2.section3;

import java.util.ArrayList;

// This is used because we haven't implemented an event system yet
// I'm passing fields because if I send the whole objects it really slows down the message passing

public class ServerClientMessage {
	float[] floatPlatformShapeMessage;
	ArrayList<float[]> cShapes = new ArrayList<>();
	ArrayList<Boolean> cJumping = new ArrayList<>();
	ArrayList<Float>   cjumpingAngle = new ArrayList<>();
	ArrayList<int[]>   cColor = new ArrayList<>();
}