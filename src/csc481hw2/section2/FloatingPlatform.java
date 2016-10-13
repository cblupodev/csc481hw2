package csc481hw2.section2;

import processing.core.PApplet;

public class FloatingPlatform {
	
	public float[] shape;
	public float width;
	float windowWidth;
	
	transient Drawing drawing = new Drawing();
	
	public void setParent(PApplet parent) {
		if (getDrawing().parent == null) {
			drawing.parent = parent;
		}
	}
	
	public FloatingPlatform(int windowWidth, int windowHeight) {
		this.shape = new float[] {-1000, windowHeight*.7f, windowWidth * .2f, windowHeight*.025f};
		this.width = windowWidth * .2f;
		this.windowWidth = (float) windowWidth;
	}
	
	public void update() {
		shape[0] -= 1;
		if (shape[0] + width < 0) {
			shape[0] = windowWidth;
		}
	}
	
	public void draw() {
		drawing.drawRect(shape);
	}
	
	public Drawing getDrawing() {
		if (drawing == null) {
			this.drawing = new Drawing();
		}
		return this.drawing;
	}

}
