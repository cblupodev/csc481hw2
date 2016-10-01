package csc481hw2.section2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import processing.core.PApplet;

public class Client extends PApplet {
	
	private static final int PORT = 6789;
	private Drawing drawing = new Drawing();
	private Physics physics = new Physics();
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private Socket socket = null;
	private String address = "";

	// start the program
	public static void main(String[] args) {
		Client c = new Client();
		c.address = args[0];
		PApplet.main("csc481hw2.section2.Client");
	}

	private int windowWidth = 600;
	private int windowHeight = 400;
	private float[] foundation;
	private float[] floatRect;
	private float[] topBoundary;
	private float[] leftBoundary;
	private float[] rightBoundary;
	
	public void settings() {
		size(windowWidth, windowHeight); // set the window demensions
	}
	
	public void setup() {		
		try {
			socket = new Socket(address, PORT);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			oos.writeInt(5);
			oos.flush();
			int timeout = 1;
			
			try {
				System.out.println(ois.available());
				if (ois.available() != 0) {
					timeout = 1;
					int line = ois.readInt(); // read from the server
					System.out.println("message from server:   " + line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		foundation = new float[] {0, windowHeight*.9f, windowWidth, windowHeight*.1f};
		floatRect = new float[] {windowWidth * .7f, windowHeight*.7f, windowWidth * .2f, windowHeight*.025f};
		
		topBoundary = new float[] {0, 0, windowWidth, 0};
		leftBoundary = new float[] {0, 0, 0, windowHeight};
		rightBoundary = new float[] {windowWidth, 0, windowWidth, windowHeight};
		fill(120,50,240);
		try {
			//if (ois.available() != 0) {
			System.out.println(ois.available());
				Character2 i = (Character2) ois.readObject();
				System.out.println(i);
			//}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw() {
		System.out.println(PORT);
		// TODO get inputs and send them to the server
		// TODO read and write to the server
		// read the character object from the server. the server does the updating
		Character c = null;
		
		// render -->
		background(0); // reset the background each frame
		drawing.drawFill(new int[] {221,221,221}); // light gray
		drawing.drawRect(foundation);
		drawing.drawRect(floatRect);
		drawing.drawFill(new int[] {255,255,255}); // white
		if (c != null) {
			drawing.drawRect(c.getShape());
		}
	}
	
	private class Drawing {	// wrapper to easily call rect() from just passing an array
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
	
	private class Physics {
		// collision detection between two lines
		// copied from https://github.com/jeffThompson/CollisionDetection
		// LINE/LINE
		boolean lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

		  // calculate the direction of the lines
		  float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
		  float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));

		  // if uA and uB are between 0-1, lines are colliding
		  if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {

		    // optionally, draw a circle where the lines meet
		    float intersectionX = x1 + (uA * (x2-x1));
		    float intersectionY = y1 + (uA * (y2-y1));
		    fill(255,0,0);
		    noStroke();
		    ellipse(intersectionX, intersectionY, 20, 20);

		    return true;
		  }
		  return false;
		}
		
		// collision detection between a rectangle and rectangle
		// copied from https://github.com/jeffThompson/CollisionDetection
		// RECTANGLE/RECTANGLE
		boolean rectRect(float r1x, float r1y, float r1w, float r1h, float r2x, float r2y, float r2w, float r2h) {
		  
		  // are the sides of one rectangle touching the other?
		  
		  if (r1x + r1w >= r2x &&    // r1 right edge past r2 left
		      r1x <= r2x + r2w &&    // r1 left edge past r2 right
		      r1y + r1h >= r2y &&    // r1 top edge past r2 bottom
		      r1y <= r2y + r2h) {    // r1 bottom edge past r2 top
		        return true;
		  }
		  return false;
		}
		
		// collision detection between a line and rectangle
		// copied from https://github.com/jeffThompson/CollisionDetection
		// LINE/RECTANGLE
		boolean lineRect(float x1, float y1, float x2, float y2, float rx, float ry, float rw, float rh) {

		  // check if the line has hit any of the rectangle's sides
		  // uses the Line/Line function below
		  boolean left =   lineLine(x1,y1,x2,y2, rx,ry,rx, ry+rh);
		  boolean right =  lineLine(x1,y1,x2,y2, rx+rw,ry, rx+rw,ry+rh);
		  boolean top =    lineLine(x1,y1,x2,y2, rx,ry, rx+rw,ry);
		  boolean bottom = lineLine(x1,y1,x2,y2, rx,ry+rh, rx+rw,ry+rh);

		  // if ANY of the above are true, the line 
		  // has hit the rectangle
		  if (left || right || top || bottom) {
		    return true;
		  }
		  return false;
		}
		
		boolean lineRectWrap(float[] r1, float[] r2) {
			return lineRect(r1[0], r1[1], r1[2], r1[3], r2[0], r2[1], r2[2], r2[3]);
		}
		
		boolean rectRectWrap(float[] r1, float[] r2) {
			return rectRect(r1[0], r1[1], r1[2], r1[3], r2[0], r2[1], r2[2], r2[3]);
		}
		
		boolean lineLineWrap(float[] l1, float[] l2) {
			return lineLine(l1[0], l1[1], l1[2], l1[3], l2[0], l2[1], l2[2], l2[3]);
		}
	}
}
