package csc481hw2.section1;

public class Agent {
	
	private float originalX;
	private float originalY;
	private float[] rect;
	private float[] color;
	private boolean keyPressed = false;
	private boolean jumping = false;
	private float jumpingAngle = 0f;

	public Agent(float originalX, float originalY, float[] rect, float[] color) {
		this.originalX = originalX;
		this.originalY = originalY;
		this.rect = rect;
		this.color = color;
	}
	
	public Agent() {};

}
