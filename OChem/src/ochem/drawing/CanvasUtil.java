package ochem.drawing;

import java.awt.Color;

import ochem.OChem;
import ochem.drawing.CanvasUtil.DrawDirection;

/*
 * CanvasUtil
 * Created by: Neil Balaskandarajah
 * Last modified: 05/23/2019
 * Utility methods and values for the Canvas object
 */

public class CanvasUtil {
	//Static values
	//transparent colors
	public static final Color TRANS_RED = new Color(238,50,50,100);
	public static final Color TRANS_GREEN = new Color(50,238,50,100);
	public static final Color TRANS_BLUE = new Color(50,50,238,100);
	public static final Color TRANS_GREY = new Color(200,200,200,100);
	public static final Color TRANS_YELLOW = new Color(255,242,0,100);
	
	//light and dark colors
	//yellow
	public static final Color DARK_YELLOW = new Color(219, 194, 52);
	public static final Color LIGHT_YELLOW = new Color(244, 217, 66);
	
	//red
	public static final Color DARK_RED = new Color(183, 33, 33);
	public static final Color LIGHT_RED = new Color(255, 45, 45);
	
	//chain colors
	public static final Color CHAIN_COLOR = new Color(44, 62, 80);
	
	//drawing colors
	public static final Color BACKGROUND_COLOR = new Color(253, 254, 254); //canvas 224, 255, 253 old
	
	//angles for bond drawing
	public static final double DOWN_BOND_ODD = Math.toRadians(45);
	public static final double DOWN_BOND_EVEN = Math.toRadians(105);
	public static final double UP_BOND_ODD = Math.toRadians(225);
	public static final double UP_BOND_EVEN = Math.toRadians(315);
	
	//font size
	public static final float fontSize = (float) (96.0F * OChem.width/3840.0);
	
	//lengths
	public static final int CHAIN_ARM = 120;
	public static final int CYCLO_RAD = 140;
	
	/*
	 * Types of action to determine different drawing features
	 */
	public static enum ActionType {
		CLEAR,
		MAIN,
		SIDE,
		FUNC_GROUP,
		BOND
	} //end enum
	
	/*
	 * Different directions the chain can be drawn in
	 */
	public static enum DrawDirection {
		UP_RIGHT,
		RIGHT,
		DOWN_RIGHT,
		DOWN_LEFT,
		LEFT,
		UP_LEFT
	} //end enum
	
	/*
	 * Different functional groups
	 */
	public static enum FuncGroup {
		FLUORINE,
		CHLORINE,
		BROMINE,
		IODINE, 
		ALDEHYDE,
		KETONE,
		ALCOHOL,
		CARBOXYLIC_ACID
	} //end enum
	
	/*
	 * Choose the pair of angles from the direction chosen
	 * DrawDirection dir - direction to draw in
	 * return angles - pair of angles to alternate between when drawing chains
	 */
	public static double[] angleFromDirection(DrawDirection dir) {
		double[] angles = new double[2]; //array of angles to draw with
		
		//different directions require different pairs of numbers to switch between
		switch (dir) {
			case DOWN_LEFT: 
				angles = new double[] {90,150};
				break;
				
			case DOWN_RIGHT:
				angles = new double[] {90,30};
				break;
				 
			case LEFT: 
				angles = new double[] {-150,150}; 
				break;
				
			case RIGHT:
				angles = new double[] {-30,30};
				break;
				
			case UP_LEFT:
				angles = new double[] {270,210};
				break;
				
			case UP_RIGHT:
				angles = new double[] {270,330};
				break;
		} //switch
		
		return angles;
	} //end angleFromDirection
	
	/*
	 * Calculate an angle to rotate a side cyclo chain based on a direction
	 * DrawDirection dir - direction to draw in
	 * return - angle based on the direction
	 */
	public static double cycloAngle(DrawDirection dir) {
		return dir.ordinal() * 30 - 30; //ordinal is the enum's position in its parent set
	} //end angleFromDirection
	
	/*
	 * Calculate an angle to rotate a functional group based on a direction
	 * DrawDirection dir - direction to draw in
	 * return - angle based on the direction
	 */
	public static double funcAngle(DrawDirection dir) {
		return Math.toRadians(dir.ordinal() * 60 - 90); //ordinal is the enum's position in its parent set
	} //end angleFromDirection
	
	/*
	 * Get an angle offset from the size of a chain
	 * int size - size of the chain
	 * return - angle to offset by
	 */
	public static double cycloAngOffset(int size) {
		switch(size) {
			//triangle
			case 3:
				return Math.toRadians(30);
			
			//square
			case 4:
				return Math.toRadians(45);
			
			//pentagon
			case 5:
				return Math.toRadians(60);
			
			//hexagon
			case 6:
				return Math.toRadians(60);
			
			//heptagon
			case 7:
				return Math.toRadians(45);
			
			//pentagon
			case 8:
				return Math.toRadians(60);
				
			//default; numbers not in the cases are never passed
			default:
				return 0;
		} //switch
	} //end cycloAngOffset
	
	/*
	 * Calculate the angle between two nodes
	 * Node n1 - first node
	 * Node n2 - second node
	 * return angle - angle in radians between n1 and n2
	 */
	public static double angleBetweenNodes(Node n1, Node n2) {
		double angle = 0;
		double dx = n2.getX() - n1.getX();
		double dy = n2.getY() - n1.getY();
		
		//cases where angle is straight, atan2 would return null
		if (dy == 0) { //no change in y
			if (dx > 0) //right
				angle = 0;
			else if (dx < 0) //left
				angle = Math.PI;
			
		} else if (dx == 0) { //no change in x
			if (dy > 0) //up
				angle = Math.PI * 0.5; 
			else if (dy < 0) //down
				angle = Math.PI * 1.5;
			
		} else { //regular case
			angle = Math.atan2(dy, dx);
		} //if
		
		return angle;
	} //end angleBetweenNodes
	
	/*
	 * Get the position in the direction enum 'n' steps ahead (rolls back to zero if near end)
	 * DrawDirection dir - direction to increase
	 * int n - number of steps forward
	 */
	public static DrawDirection incDirection(DrawDirection dir, int n) {
		int pos = dir.ordinal();
		
		while (n > 0) {
			if (pos + 1 >= DrawDirection.values().length) {
				pos = -1;
			} //if
			pos++;
			
			n--;
		} //loop
		
		return DrawDirection.values()[pos];
	} //end incDirection
} //end class
