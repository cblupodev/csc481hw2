package csc481hw2.section2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import processing.core.PApplet;

public class Character {
	
	float originalX;
	float originalY;
	float[] shape;
	int[] color;
	boolean jumping = false;
	float jumpingAngle = 180f;
	
	transient Physics physics = new Physics();
	transient Drawing drawing = new Drawing();

	public Character(float originalX, float originalY, float[] rect, int[] color) {
		this.originalX = originalX;
		this.originalY = originalY;
		this.shape = rect;
		this.color = color;
	}
	
	public Character(int windowWidth, int windowHeight) {
		this.shape = new float[] {windowWidth * .1f, windowHeight*.9f - 50, 25, 50};
		this.originalX = shape[0];
		this.originalY = shape[1];
	}
	
	public Character updateCharacter(int i, Character c, PrintWriter writer, int windowHeight) {
		// redraw the agent if it's in the process of jumping
		if (c.jumping) {
			// used that colliding circles example from processing.org
			float newY = windowHeight*.9f - 50 + (200 * physics.sinWrap(physics.radiansWrap(c.jumpingAngle)));
			c.shape[1] = newY;// set a new y position
			c.jumpingAngle = c.jumpingAngle+3; // increment the jumping angle
			if (c.jumpingAngle == 360) { // stop jumping if reached the ground
				c.jumping = false;
				c.jumpingAngle = 180;
				c.shape[1] = c.originalY;
			}
		}
		
		// check if the agent has collided with the boundaries and other objects
		// if it has then reset to its original position
		if (physics.collision(this)) {
			setToSpawnPoint();
		}
		
		return this;	
	}

	public boolean updateInput(String message) {
		if(message.equals("LEFT")) {
			shape[0] -= 5; // move x position left
			return true;
		}
		if (message.equals("RIGHT")) {
			shape[0] += 5; // move x position right
			return true;
		}
		if (message.equals("SPACE")) {
			if (jumping == false) {
				jumping = true;
			}
			return true;
		}
		return false;
	}
	
	public void draw(PApplet p) {
		setParent(p);
		drawing.drawFill(this.color);
		drawing.drawRect(this.shape);
	}
	
	public void setParent(PApplet parent) {
		if (getDrawing().parent == null) {
			drawing.parent = parent;
		}
	}
	
	public Drawing getDrawing() {
		if (drawing == null) {
			this.drawing = new Drawing();
		}
		return this.drawing;
	}
	
	public Physics getPhysics() {
		if (physics == null) {
			this.physics = new Physics();
		}
		return this.physics;
	}
	
	// set a character to its spawn position and state
	private void setToSpawnPoint() {
		jumping = false;
		shape[0] = originalX;
		shape[1] = originalY;
	}
}
