package ochem.drawing;

import java.awt.Color;

public class Node {
	private int x;
	private int y;
	private int rad;
	
	private NodeType type;
	
	public static enum NodeType {
		BLANK,
		SELECTED
	}
	
	public Node(int x, int y, int rad) {
		this.x = x;
		this.y = y;
		this.rad = rad;
		
		this.type = NodeType.BLANK;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getRad() {
		return rad;
	}
	
	public void setType(NodeType type) {
		this.type = type;
	}
}
