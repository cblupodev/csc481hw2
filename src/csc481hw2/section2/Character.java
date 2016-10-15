package csc481hw2.section2;

public class Character {
	
	private float originalX;
	private float originalY;
	private float[] shape;
	private int[] color;
	private boolean keyPressed = false;
	private boolean jumping = false;
	private float jumpingAngle = 180f;
	
	public Character(float originalX, float originalY, float[] rect, int[] color) {
		this.setOriginalX(originalX);
		this.setOriginalY(originalY);
		this.setShape(rect);
		this.setColor(color);
	}
	
	public Character(int windowWidth, int windowHeight) {
		//this.setShape(new float[] {windowWidth * .7f, windowHeight*.7f, windowWidth * .2f, windowHeight*.025f});
		this.setShape(new float[] {windowWidth * .1f, windowHeight*.9f - 50, 25, 50});
		this.setOriginalX(getShape()[0]);
		this.setOriginalY(getShape()[1]);
		this.setColor(new int[] {255,255,255}); //white
	}

/*	// convert the object to json so it can send over the object stream
	// TODO
	private Object writeReplace() throws ObjectStreamException {
        return gson.toJson(this, type);
	}
	
	// TODO
	private Object readResolve() throws ObjectStreamException {
		return "";
	}*/

	public float[] getShape() {
		return shape;
	}

	public void setShape(float[] shape) {
		this.shape = shape;
	}

	public float getOriginalX() {
		return originalX;
	}

	public void setOriginalX(float originalX) {
		this.originalX = originalX;
	}

	public float getOriginalY() {
		return originalY;
	}

	public void setOriginalY(float originalY) {
		this.originalY = originalY;
	}

	public int[] getColor() {
		return color;
	}

	public void setColor(int[] color) {
		this.color = color;
	}

	public boolean isKeyPressed() {
		return keyPressed;
	}

	public void setKeyPressed(boolean keyPressed) {
		this.keyPressed = keyPressed;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public float getJumpingAngle() {
		return jumpingAngle;
	}

	public void setJumpingAngle(float jumpingAngle) {
		this.jumpingAngle = jumpingAngle;
	}

}
