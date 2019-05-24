package ochem.drawing;

/*
 * CanvasUtil
 * Created by: Neil Balaskandarajah
 * Last modified: 05/23/2019
 * Utility methods and values for the Canvas object
 */

public class CanvasUtil {
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
	 * Choose the pair of angles from the direction chosen
	 * DrawDirection dir - direction to draw in
	 * return angle - angle to draw chain with
	 */
	public static double cycloAngle(DrawDirection dir) {
		double angles; //array of angles to draw with
		
		//different directions require different pairs of numbers to switch between
		switch (dir) {
			case DOWN_LEFT: 
				angles = 120;
				break;
				
			case DOWN_RIGHT:
				angles = 60;
				break;
				 
			case LEFT: 
				angles = 180;
				break;
				
			case RIGHT:
				angles = 0;
				break;
				
			case UP_LEFT:
				angles = 240;
				break;
				
			case UP_RIGHT:
				angles = 300;
				break;
				
			default:
				throw new IllegalArgumentException("What direction is this???");
		} //switch
		
		return angles;
	} //end angleFromDirection

} //end class
