package csc481hw2.section2;

import processing.core.PApplet;

public class Drawing extends PApplet {	// wrapper to easily call rect() from just passing an array
	public void drawRect(float[] rect) {
		rect(rect[0], rect[1], rect[2], rect[3]);
	}
	// wrapper to easily call line() from just passing an array	
	public void drawLine(float[] line) {
		line(line[0], line[1], line[2], line[3]);
	}
	// wrapper to easily call fill() from just passing an array	
	public void drawFill(int[] rgb) {
		fill(rgb[0], rgb[1], rgb[2]);
	}
	// wrapper to easily call stroke() from just passing an array	
	public void drawStroke(int[] rgb) {
		stroke(rgb[0], rgb[1], rgb[2]);
	}
}
