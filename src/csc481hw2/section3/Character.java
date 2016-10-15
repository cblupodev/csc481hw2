package csc481hw2.section3;

import csc481hw2.section1.GameObject;
import csc481hw2.section1.Movable;
import csc481hw2.section1.Physics;
import processing.core.PApplet;

public class Character extends Movable implements GameObject {
	
	float originalX; // original x position
	float originalY; // original y position
	int[] color; // character color
	boolean jumping = false; //  is it jumping?
	float jumpingAngle = 180f; //  the jumping angle
	int windowHeight; //  sketch height
	
	Physics physics = new Physics(); // keep reference to physics so it can update the character 

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
		return this;	
	}

	// send keyboard inputs to server
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
