package csc481hw2.section2;

import csc481hw2.section1.GameObject;
import csc481hw2.section1.Movable;
import csc481hw2.section1.Physics;
import processing.core.PApplet;

public class Character extends Movable implements GameObject {
	
	float originalX;
	float originalY;
	int[] color;
	boolean jumping = false;
	float jumpingAngle = 180f;
	int windowHeight;
	
	Physics physics = new Physics();

	public Character(int windowWidth, int windowHeight) {
		this.type = "rect";
		this.shape = new float[] {windowWidth * .1f, windowHeight*.9f - 50, 25, 50};
		this.originalX = shape[0];
		this.originalY = shape[1];
		this.windowHeight = windowHeight;
	}
	
	public Character update() {
		// redraw the agent if it's in the process of jumping
		if (jumping) {
			// used that colliding circles example from processing.org
			float newY = windowHeight*.9f - 50 + (200 * physics.sinWrap(physics.radiansWrap(jumpingAngle)));
			shape[1] = newY;// set a new y position
			jumpingAngle = jumpingAngle+3; // increment the jumping angle
			if (jumpingAngle == 360) { // stop jumping if reached the ground
				jumping = false;
				jumpingAngle = 180;
				shape[1] = originalY;
			}
		}
		
		// check if the agent has collided with the boundaries and other objects
		// if it has then reset to its original position
//		if (physics.collision(this)) {
//			setToSpawnPoint();
//		}
		
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
		getDrawing().drawFill(this.color);
		getDrawing().drawRect(this.shape);
	}
	
	public Physics getPhysics() {
		if (physics == null) {
			this.physics = new Physics();
		}
		return this.physics;
	}
	
	// set a character to its spawn position and state
	public void setToSpawnPoint() {
		jumping = false;
		shape[0] = originalX;
		shape[1] = originalY;
	}
}
