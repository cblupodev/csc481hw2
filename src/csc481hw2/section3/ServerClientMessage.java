package csc481hw2.section3;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerClientMessage {
	float[] floatPlatformShapeMessage;
	ArrayList<float[]> cShapes = new ArrayList<>();
	ArrayList<Boolean> cJumping = new ArrayList<>();
	ArrayList<Float>   cjumpingAngle = new ArrayList<>();
	ArrayList<int[]>   cColor = new ArrayList<>();
}