package csc481hw2.section1;

import processing.core.PApplet;

public class Movable implements Component {
	
	public String type;
	public float[] shape;
	
	transient private Drawing drawing = new Drawing();
	
	// this should be overwritten
	public Movable update() {
		return this;
	}

	// this should be overwritten
	public void draw(PApplet p) {
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
	

}
