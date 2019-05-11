package ochem.drawing;

/*
 * Node
 * Created by: Neil Balaskandarajah
 * Last modified: 05/08/2019
 * A node for clicking and connecting items when drawing
 */

import java.awt.Color;

public class Node {
	//Attributes
	private int x; //x value of the top left point of the circle
	private int y; //y value of the top left point of the circle
	private int rad; //radius of the node
	private Color color; //color of the node
	private NodeType type; //the type of node
	
	private int tag; //tag for node location
	
	/*
	 * Enum containing all the states a node can be in
	 */
	public static enum NodeType {
		BLANK,
		SINGLE_BOND,
		DOUBLE_BOND,
		TRIPLE_BOND
	} //end enum
	
	/*
	 * Creates a node with an (x,y) coordinate for its top left corner and a radius
	 * int x - x value for top left
	 * int y - y value for top left
	 * int rad - radius of the circle
	 */
	public Node(int x, int y, int rad) {
		this.x = x;
		this.y = y;
		this.rad = rad;
		
		this.type = NodeType.BLANK;
		this.color = Color.BLACK;
	} //end constructor
	
	/*
	 * Set the (x,y) of the node
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	} //end setXY
	
	/*
	 * Get the x value of the node
	 * return x - x value of top left corner
	 */
	public int getX() {
		return x;
	} //end getX
	
	/*
	 * Get the y value of the node
	 * return y - y value of top left corner
	 */
	public int getY() {
		return y;
	} //end getY
	
	/*
	 * Get the radius of the node
	 * return rad - radius of the circle
	 */
	public int getRad() {
		return rad;
	} //end getRad
	
	/*
	 * Get the color of the node
	 * return color - Node color
	 */
	public Color getColor() {
		updateColor();
		return color;
	} //end getColor
	
	/*
	 * Get the type of the node
	 * return type
	 */
	public NodeType getType() {
		return type;
	} //end getType
	
	/*
	 * Change node color based on the type
	 */
	public void updateColor() {
		switch (type) {
		//blank case
		case BLANK: 
			color = Color.GRAY;
			break;
		
		//single bond case
		case SINGLE_BOND:
		case DOUBLE_BOND:
		case TRIPLE_BOND:
			//slightly darker than background color
			int r = Canvas.BACKGROUND_COLOR.getRed();
			int g = Canvas.BACKGROUND_COLOR.getGreen();
			int b = Canvas.BACKGROUND_COLOR.getBlue();
			
			color = new Color(r - 60, g, b - 3);
			break;
		} //switch
	} //end updateColor
	
	/*
	 * Set the type of the node
	 * NodeType type - new type of the node
	 */
	public void setType(NodeType type) {
		this.type = type;
	} //end setType
	
	/*
	 * Set the tag of the node
	 * int tag - node tag
	 */
	public void setTag(int tag) {
		this.tag = tag;
	} //end setTag
	
	/*
	 * Get the tag of the node
	 * return tag - node tag
	 */
	public int getTag() {
		return tag;
	} //end getTag
} //end class
