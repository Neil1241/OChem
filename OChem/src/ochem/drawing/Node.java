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
	
	/*
	 * Creates a blank node at origin with a radius
	 */
	public Node(int rad) {
		this.x = 0;
		this.y = 0;
		this.rad = rad;
	} //end constructor
	
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
	 * Get the center x of the node
	 * return - center x value
	 */
	public int getCenterX() {
		return x - rad;
	} //end getCenterX
	
	/*
	 * Get the center y of the node
	 * return - center y value
	 */
	public int getCenterY() {
		return y - rad;
	} //end getCenterY
	
	/*
	 * Get the radius of the node
	 * return rad - radius of the circle
	 */
	public int getRad() {
		return rad;
	} //end getRad
	
	/*
	 * Get the diameter of the node
	 * return dia - diameter of the circle
	 */
	public int getDia() {
		return 2 * rad;
	} //end getDia
} //end class
