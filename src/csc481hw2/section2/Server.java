package csc481hw2.section2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CopyOnWriteArrayList;
import csc481hw2.section2.Server;
import csc481hw2.section2.ServerAccept;
import processing.core.PApplet;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Server extends PApplet {
	
	public static CopyOnWriteArrayList<Character> agents = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<BufferedReader> inStream = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<PrintWriter> outStream = new CopyOnWriteArrayList<>();
	private Physics physics = new Physics();
	
	private int windowWidth = 600;
	private int windowHeight = 400;
	private float[] topBoundary;
	private float[] leftBoundary;
	private float[] rightBoundary;
	private float[] floatRect;
	
	Gson gson;
	Type type;
	public static void main(String[] args) {
		Server m = new Server();
		m.run();
		PApplet.main("csc481hw2.section2.Server");
	}
	
	public void run() {
		
		topBoundary = new float[] {0, 0, windowWidth, 0};
		leftBoundary = new float[] {0, 0, 0, windowHeight};
		rightBoundary = new float[] {windowWidth, 0, windowWidth, windowHeight};
		floatRect = new float[] {windowWidth * .7f, windowHeight*.7f, windowWidth * .2f, windowHeight*.025f};
		
		gson = new Gson();
		type = new TypeToken<CopyOnWriteArrayList<Character>>() {}.getType();
		
		// start the thread that accepts incoming connections
		Thread t = new Thread(new ServerAccept());
		t.start();
		
		// read from clients
		int frame = -1;
		while (true) { // never stop looking
			frame++;
			for (int i = 0; i < inStream.size(); i++) { // iterate over the client streams
				// initialize the agent if the number of streams and agents aren't the same size
				if (agents.size() != inStream.size()) {
					agents.add(i, new Character(windowWidth, windowHeight));
					writeCharactersToClient(outStream.get(i));
				} else {
					agents.set(i, readInputFromClient(i, agents.get(i), inStream.get(i), outStream.get(i)));
					
					if (frame % 10000 == 0) { // need the frames or else it will update everything to quickly before you can read input
						agents.set(i, updateCharacter(i, agents.get(i), outStream.get(i)));
					}
				}
			}
		}
		
	}	
	private void writeCharactersToClient(PrintWriter writer) {
		String x = gson.toJson(agents, type);
		writer.println(x);
	}

	int j = 0;
	private Character updateCharacter(int i, Character c, PrintWriter writer) {
		// redraw the agent if it's in the process of jumping
		if (c.isJumping()) {
			
			// used that colliding circles example from processing.org
			float newY = windowHeight*.9f - 50 + (200 * sin(radians(c.getJumpingAngle())));
			c.getShape()[1] = newY;// set a new y position
			c.setJumpingAngle(c.getJumpingAngle()+1); // increment the jumping angle
			if (c.getJumpingAngle() == 360) { // stop jumping if reached the ground
				c.setJumping(false);
				c.setJumpingAngle(180);
				c.getShape()[1] = c.getOriginalY();
			}
			writeCharactersToClient(writer);
		}
		
		// check if the agent has collided with the boundaries
		// if it has then reset to its original position
		if (physics.lineRectWrap(leftBoundary,c.getShape()) || physics.lineRectWrap(rightBoundary,c.getShape()) ||
				physics.rectRectWrap(floatRect, c.getShape())) {
			c.setJumping(false);
			c.getShape()[0] = c.getOriginalX();
			c.getShape()[1] = c.getOriginalY();
		}
		return c;	
	}

	private Character readInputFromClient(int i, Character c, BufferedReader r, PrintWriter writer) {
		try {
			if (r.ready()) {
				String message = r.readLine();
				boolean keypressed = false;
				if(message.equals("LEFT")) {
					c.getShape()[0] -= 5; // move x position left
					keypressed = true;
				}
				if (message.equals("RIGHT")) {
					c.getShape()[0] += 5; // move x position right
					keypressed = true;			
				}
				if (message.equals("SPACE")) {
					keypressed = true;
					if (c.isJumping() == false) {
						c.setJumping(true);
					}
				}
				if (keypressed) {
					agents.set(i, c);
					writeCharactersToClient(writer);
					return c;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return c;
		
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
