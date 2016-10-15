package csc481hw2.section1;

import processing.core.PApplet;

// for objects that move

public class Movable implements Component {
	
	public String type; // the object type to use for collisions
	public float[] shape; // the object shape to use for drawing and updating
	
	// have a drawing component
	transient private Drawing drawing = new Drawing();
	
	// update object position and stuff
	// this should be overwritten
	public Movable update() {
		return this;
	}

	// draw the object
	// this should be overwritten
	public void draw(PApplet p) { 
		
	}

	// make sure the drawing component has  reference to the sketch
	public void setParent(PApplet parent) {
		if (getDrawing().parent == null) {
			drawing.parent = parent;
		}
	}
	
	// make sure drawing is instantiated
	// I ran into problems with a null drawing component when passing info between client and server
	public Drawing getDrawing() {
		if (drawing == null) {
			this.drawing = new Drawing();
		}
		return this.drawing;
	}
	

}
