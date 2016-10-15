package csc481hw2.section1;

// for objects that don't move

public class Immovable implements Component {
	
	public String type = null; // the object type to use for collisions
	public float[] shape = null; // the object shape to use for drawing and updating

	public Immovable(String type, float[] shape) {
		this.type = type;
		this.shape = shape;
	}

}
