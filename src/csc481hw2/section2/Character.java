package csc481hw2.section2;

import java.io.PrintWriter;

import processing.core.PApplet;

public class Character {
	
	private float originalX;
	private float originalY;
	private float[] shape;
	private int[] color;
	private boolean keyPressed = false;
	private boolean jumping = false;
	private float jumpingAngle = 180f;
	
	transient Physics physics = new Physics();
	transient Drawing drawing = new Drawing();

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
	
	public Character updateCharacter(int i, Character c, PrintWriter writer, int windowHeight) {
		// redraw the agent if it's in the process of jumping
		if (c.isJumping()) {
			// used that colliding circles example from processing.org
			float newY = windowHeight*.9f - 50 + (200 * physics.sinWrap(physics.radiansWrap(c.getJumpingAngle())));
			c.getShape()[1] = newY;// set a new y position
			c.setJumpingAngle(c.getJumpingAngle()+3); // increment the jumping angle
			if (c.getJumpingAngle() == 360) { // stop jumping if reached the ground
				c.setJumping(false);
				c.setJumpingAngle(180);
				c.getShape()[1] = c.getOriginalY();
			}
		}
		
		// check if the agent has collided with the boundaries and other objects
		// if it has then reset to its original position
		if (physics.collision(this)) {
			setToSpawnPoint();
		}
		
		return this;	
	}
	
	public void draw() {
		drawing.drawFill(this.getColor());
		drawing.drawRect(this.getShape());
	}
	

	
	// set a character to its spawn position and state
	private void setToSpawnPoint() {
		setJumping(false);
		getShape()[0] = getOriginalX();
		getShape()[1] = getOriginalY();
	}

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
