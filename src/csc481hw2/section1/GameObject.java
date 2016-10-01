package csc481hw2.section1;

import java.util.ArrayList;

public class GameObject {
	
	public ArrayList<Component> components = new ArrayList<>();
	
	public void run() {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).update();
		}
	}
	
}
