package ochem.drawing;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import ochem.OChem;

/*
 * CanvasUtil
 * Created by: Neil Balaskandarajah
 * Last modified: 05/23/2019
 * Utility methods and values for all drawing operations
 */

public class DrawingUtil {
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
	
	//blue
	public static final Color DARK_BLUE = new Color(0, 0, 255);
	public static final Color LIGHT_BLUE = new Color(51, 102, 255);
	
	//chain colors
	public static final Color CHAIN_COLOR = new Color(44, 62, 80);
	
	//drawing colors
	public static final Color BACKGROUND_COLOR = new Color(253, 254, 254); //canvas 224, 255, 253 old
	
	//angles for bond drawing
	public static final double DOWN_BOND_ODD = Math.toRadians(45);
	public static final double DOWN_BOND_EVEN = Math.toRadians(105);
	public static final double UP_BOND_ODD = Math.toRadians(225);
	public static final double UP_BOND_EVEN = Math.toRadians(315);
	
	//scalable constants
	//scale ratio
	private static final double SCALE_RATIO = OChem.width/3840.0;
	
	//canvas
	public static final float FONT_SIZE = (float) (80.0F * SCALE_RATIO);
	public static final int NODE_RAD = (int) (20 * SCALE_RATIO);
	
	//lengths
	public static final int CHAIN_ARM = (int) (120 * SCALE_RATIO);
	public static final int CYCLO_RAD = (int) (140 * SCALE_RATIO);
	
	//thicknesses
	public static final float CHAIN_STROKE = (float) (15.0F * SCALE_RATIO);
	public static final float BOND_STROKE = (float) (8.0F * SCALE_RATIO);
	
	//file locations
	public static final String OXYGEN_LOCATION = "src/resources/Oxygen-Regular.ttf";
	
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
	 * Different directions the chain can be drawn in (corresponds to pairs of angles)
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
		//haloalkanes
		FLUORINE,
		CHLORINE,
		BROMINE,
		IODINE, 
		
		//double bonded oxygens
		ALDEHYDE,
		KETONE,
		
		//hydroxide
		ALCOHOL,
		
		//double oxygen and hydroxide
		CARBOXYLIC_ACID,
		
		//single oxygen
		ETHER,
		
		//double oxygen and single oxygen
		ESTER,
		
		//single nitrogen
		AMINE,
		
		//double oxygen and single nitrogen
		AMIDE
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
	
	/*
	 * Checks if a String is a valid number
	 * String text - String to check for number
	 */
	public static boolean isNumber(String text) {
		//if parse succeeds, string is a number and true is returned
		try {
			Integer.parseInt(text); //no need to save; if error isn't caught, text is number
			return true;
		} catch (NumberFormatException n) {
			return false;
		} //try-catch
	} //end isNumber
	
	/*
	 * Convert a String to an integer
	 * String str - string to convert
	 * return num - numerical form of the String OR default value
	 */
	public static int stringToNum(String str) {
		int num;
		
		try { //set parsed number
			num = Integer.parseInt(str);
		} catch (NumberFormatException n) { //set default
			num = 0;
		}
		
		return num;
	} //end stringToNum
	
	/*
	 * Check whether one point is within a radius around a target point
	 * int x1 - current point x
	 * int y1 - current point y
	 * int x2 - goal point x
	 * int y2 - goal point y
	 * int range - distance to check within
	 */
	public static boolean isWithinBounds(int x1, int y1, int x2, int y2, int range) {

		// calculate the differences in x and y
		double xDiff = Math.abs(x2 - x1);
		double yDiff = Math.abs(y2 - y1);

		if (xDiff < range && yDiff < range) {
			return true;
		} else {
			return false;
		} // if
	} // end isWithinBounds
	
	/*
	 * Get a font from a file
	 * String location - location of the file
	 * return font - font retrieved from file OR default font
	 */
	public static Font getFileFont(String location) {
		Font f;
		
		//try to create and set the font if the file is present
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File(location));
			f = f.deriveFont(DrawingUtil.FONT_SIZE);
		} catch (Exception e) {
			e.printStackTrace();
			f = new Font(Font.SANS_SERIF, Font.PLAIN, (int) DrawingUtil.FONT_SIZE);
		} //try-catch
		
		return f;
	} //end getFileFont
	
	/*
	 * Calculate the x offset from rotating a line 'arm' long by 'angRad' CCW
	 * int arm - length of line in pixels
	 * angRad - angle to rotate line CCW in radians
	 */
	public static int rCos(int arm, double angRad) {
		return (int) (arm * Math.cos(angRad));
	} // end rCos

	/*
	 * Calculate the y offset from rotating a line 'arm' long by 'angRad' CCW
	 * int arm - length of line in pixels
	 * angRad - angle to rotate line CCW in radians
	 */
	public static int rSin(int arm, double angRad) {
		return (int) (arm * Math.sin(angRad));
	} // end rSin
} //end class
