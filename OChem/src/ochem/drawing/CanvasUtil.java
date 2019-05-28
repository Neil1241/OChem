package ochem.drawing;

import java.awt.Color;

/*
 * CanvasUtil
 * Created by: Neil Balaskandarajah
 * Last modified: 05/23/2019
 * Utility methods and values for the Canvas object
 */

public class CanvasUtil {
	//Static values
	//transparent colors
	public static Color TRANS_RED = new Color(238,50,50,100);
	public static Color TRANS_GREEN = new Color(50,238,50,100);
	public static Color TRANS_BLUE = new Color(50,50,238,100);
	public static Color TRANS_GREY = new Color(200,200,200,100);
	public static Color TRANS_YELLOW = new Color(255,242,0,100);
	
	//light and dark colors
	//yellow
	public static Color DARK_YELLOW = new Color(219, 194, 52);
	public static Color LIGHT_YELLOW = new Color(244, 217, 66);
	
	//red
	public static Color DARK_RED = new Color(183, 33, 33);
	public static Color LIGHT_RED = new Color(255, 45, 45);
	
	//chain colors
	public static Color CHAIN_COLOR = Color.BLACK;
	
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
	 * Choose the pair of angles from the direction chosen
	 * DrawDirection dir - direction to draw in
	 * return angles - pair of angles to alternate between when drawing chains
	 */
	public static double[] angleFromDirection(DrawDirection dir) {
		double[] angles; //array of angles to draw with
		
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
				
			default:
				throw new IllegalArgumentException("What direction is this???");
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
			
			//soft limited to not go here
			default:
				return 0;
		} //switch
	} //end cycloAngOffset

} //end class
