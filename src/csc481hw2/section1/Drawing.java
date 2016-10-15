package csc481hw2.section1;

import processing.core.PApplet;

public class Drawing {	// wrapper to easily call rect() from just passing an array
	
	public PApplet parent = null;
	
	public Drawing (PApplet parent) {
		this.parent = parent;
	}
	
	public Drawing(){}
	
	public void drawRect(float[] rect) {
		parent.rect(rect[0], rect[1], rect[2], rect[3]);
	}
	// wrapper to easily call line() from just passing an array	
	public void drawLine(float[] line) {
		parent.line(line[0], line[1], line[2], line[3]);
	}
	// wrapper to easily call fill() from just passing an array	
	public void drawFill(int[] rgb) {
		parent.fill(rgb[0], rgb[1], rgb[2]);
	}
	// wrapper to easily call stroke() from just passing an array	
	public void drawStroke(int[] rgb) {
		parent.stroke(rgb[0], rgb[1], rgb[2]);
	}
}