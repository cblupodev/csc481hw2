package csc481hw2.section1;

import processing.core.PApplet;

public class Drawing implements Component {
	
	// have a reference to the sketch to draw to
	public PApplet parent = null;
	
	// set the papplet parent
	public Drawing (PApplet parent) {
		this.parent = parent;
	}
	
	public Drawing(){}
	
	// wrapper to easily call line() from just passing an array
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
