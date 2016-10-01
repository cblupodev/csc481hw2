package csc481hw2.section2;

import java.io.Serializable;

public class Character implements Serializable {
	
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
		this.setShape(new float[] {windowWidth * .1f, windowHeight*.9f - 50, 25, 50});
		this.setOriginalX(getShape()[0]);
		this.setOriginalY(getShape()[1]);
		this.setColor(new int[] {255,255,255}); //white
	}

	public Character() {} // empty constructor

	public float[] getShape() {
		return shape;
	}

	public void setShape(float[] shape) {
		this.shape = shape;
	}

	private float getOriginalX() {
		return originalX;
	}

	private void setOriginalX(float originalX) {
		this.originalX = originalX;
	}

	private float getOriginalY() {
		return originalY;
	}

	private void setOriginalY(float originalY) {
		this.originalY = originalY;
	}

	private int[] getColor() {
		return color;
	}

	private void setColor(int[] color) {
		this.color = color;
	}

	private boolean isKeyPressed() {
		return keyPressed;
	}

	private void setKeyPressed(boolean keyPressed) {
		this.keyPressed = keyPressed;
	}

	private boolean isJumping() {
		return jumping;
	}

	private void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	private float getJumpingAngle() {
		return jumpingAngle;
	}

	private void setJumpingAngle(float jumpingAngle) {
		this.jumpingAngle = jumpingAngle;
	};

}
