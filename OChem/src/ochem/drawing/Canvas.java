package ochem.drawing;

/*
 * Canvas
 * Created by: Neil Balaskandarajah
 * Last modified: 05/13/2019
 * Components that handles drawing components to the screen
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import ochem.organic.Chain;
import ochem.organic.Compound;

public class Canvas extends JComponent {
	//Attributes
	private int width; //width of component
	private int height; //height of component
	private Palette palette; //instance of the palette
	
	private Node mouse; //for hovering effects
	
	public static Color BACKGROUND_COLOR; //background color
	
	private Compound master; //compound being drawn
	private Chain mainChain; //main chain
	private ArrayList<Node> mainNodes; //nodes for the main chain
	
	private ArrayList<Chain> sideChains; //side chains
	private ArrayList<Node> sideNodes; //nodes for the side chain
	
	private ActionType type; //type of action
	
	private boolean mainOnScreen; //whether a main chain is on the screen
	
	private int mainStep; //step for the "main" button
	
	/*
	 * Types of action to determine different drawing features
	 */
	public static enum ActionType {
		CLEAR,
		MAIN,
		SIDE,
		FUNC_GROUP
	} //end enum
	
	/*
	 * Different directions the chain can be drawn in
	 */
	private enum DrawDirection {
		UP_RIGHT,
		RIGHT,
		DOWN_RIGHT,
		DOWN_LEFT,
		LEFT,
		UP_LEFT
	} //end enum
	
	/*
	 * Create a canvas with its parent's width and height
	 * int parentWidth - width of the parent panel
	 * int parentHeight - height of the parent panel
	 */
	public Canvas(int width, int height, Palette palette) {
		super();
		
		//set attributes
		this.width = width;
		this.height = height;
		this.palette = palette;
		
		//set the size of the component
		this.setPreferredSize(new Dimension(this.width, this.height));
		
		//instantiate the nodes list, create the mouse node
		mouse = new Node(20);
		
		//pale blue
		BACKGROUND_COLOR = new Color(224, 255, 253);
		
		//set the type
		type = ActionType.CLEAR;
		
		//set the main on screen to false
		mainOnScreen = false;
		
		//instantiate the chains
		mainChain = new Chain(0, "-1");		
		sideChains = new ArrayList<Chain>();
		
		//instantiate lists for the nodes on the main chain and side chains
		mainNodes = new ArrayList<Node>();
		sideNodes = new ArrayList<Node>();
		
		//set the step numbers to zero when not being used, increment when needed
		mainStep = 0;
		
		//add the controllers to the canvas
		registerControllers();
	} //end constructor
	
	/*
	 * Draw all nodes, bonds and functional groups to the screen
	 * Graphics g - AWT object responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//background
		g2.setBackground(BACKGROUND_COLOR);
		g2.clearRect(0,0, width + 30, height + 30);
		
		g2.setColor(Color.BLACK);
		
		//test
		drawChain(g2, new Node(width/2,height/2, 10), DrawDirection.UP_RIGHT, 4);
		drawGhost(g2);
		
	}  //end paintComponent
	
	private void mainAction(Graphics2D g2) {
		switch (mainStep) {
			//size definition step
			case 1: 
			/*
			 * palette sets selected type
			 * canvas gets that type
			 * dialog box says "enter main size"
			 * user types the #
			 */
			break;
			
			//location selection step
			case 2: 
			/*
			 * draws the ghost with the size previously selected
			 * dialog box says to choose a location
			 */
			
			break;
			
			//fixed on screen step
			case 3: 
			/*
			 * draw main to the screen
			 * dialog goes blank
			 * mainOnScreen is true (so click doesn't set mainStep to zero)
			 */
			
			break;
			
		}
	}
	
	/*
	 * Draw the ghost
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void drawGhost(Graphics2D g2) {
		
		switch (type) {
			case CLEAR:
				g2.setColor(new Color(50,238,50, 100));
				g2.fillOval(mouse.getCenterX(), mouse.getCenterY(), mouse.getDia(), mouse.getDia());
				break;
		
			//main arm
			case MAIN:
					//change the color and draw the nodes
//					drawChain(g2, mouse, 30);
				break;
				
			case SIDE:
				
				break;
				
			case FUNC_GROUP:
				
				break;
		} //switch
	} //end drawGhost
	
	/*
	 * Draws the main chain from a starting point
	 * Node start - starting point
	 */
	private void drawChain(Graphics2D g2, Node start, DrawDirection dir, int chainSize) {
		BasicStroke bs = new BasicStroke(12.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(bs);
		
		//starting x and y coordinates
		int x1 = start.getX();
		int y1 = start.getY();
		
		double[] angles = angleFromDirection(dir); //get the angles based on the direction
		int arm = 120; //length of bonds in pixels
		
		Color c = new Color(0,0,0); //dbg
		
		//test
		g2.fillOval(start.getCenterX(), start.getCenterY(), start.getDia(), start.getDia());
		
		for (int i = 0; i < chainSize-1; i++) {
			//current angle to draw (alternate between the 2 angles)
			double ang = angles[i % 2]; 
			
			//dbg with progressively lightening colors
			int inc = 40;
			Color newClr = new Color(c.getRed() + inc, c.getGreen() + inc, c.getBlue() + inc);
			g2.setColor(newClr);
			
			//calculate the end points for the line
			int x2 = (int) (arm * Math.cos(Math.toRadians(ang))) + x1;
			int y2 = (int) (arm * Math.sin(Math.toRadians(ang))) + y1;
			
			//draw the line
			g2.drawLine(x1, y1, x2, y2);
			
			//change the start point to this end for next loop
			x1 = x2;
			y1 = y2;
			
			c = newClr;
		} //loop
	} //end drawChain
	
	/*
	 * Choose the pair of angles from the direction chosen
	 */
	private double[] angleFromDirection(DrawDirection dir) {
		double[] angles;
		
		//different directions require different pairs of numbers to switch between
		switch (dir) {
			case DOWN_LEFT: //good
				angles = new double[] {90,120};
				break;
				
			case DOWN_RIGHT: //good
				angles = new double[] {90,60};
				break;
				
			case LEFT: //good
				angles = new double[] {-150,150};
				break;
				
			case RIGHT: //good
				angles = new double[] {-30,30};
				break;
				
			case UP_LEFT: //good
				angles = new double[] {270,240};
				break;
				
			case UP_RIGHT: //good
				angles = new double[] {270,300};
				break;
				
			default:
				throw new IllegalArgumentException("What direction is this???");
		} //switch
		
		return angles;
	} //end angleFromDirection
	
	private void drawSides(Graphics2D g2) {
		g2.setColor(Color.ORANGE);
		for (Node n : sideNodes) {
			int startX = n.getCenterX();
			int startY = n.getCenterY();
			
			double angle = Math.toRadians(90);
			int arm = 150;
			
			for (int i = 1; i < mainChain.getSize(); i++) {
				//change angle based on even/odd
				if (i % 2 == 0) { 
					angle = 90;
				} else {
					angle = 60;
				}
				
				//calculate the arm offset
				int endX = (int) (arm * Math.cos(angle)) + startX;
				int endY = (int) (arm * Math.sin(angle)) + startY;
				
				//draw the line
				g2.drawLine(startX, startY, endX, endY);
				
				//change the start point
				mainNodes.add(new Node(startX, startY, 30));
				
				startX = endX;
				startY = endY;
			}
		}
		System.out.println("drawSides");
	}
	
	public void setMainSize(int main) {
		mainChain.setSize(main);
	}
	
	/*
	 * Update the screen
	 */
	public void update() {
		repaint();
	} //end update
	
	/*
	 * Add the CanvasController to this component
	 */
	private void registerControllers() {
		CanvasController cc = new CanvasController(this);
		this.addMouseListener(cc);
		this.addMouseMotionListener(cc);
	} //end registerControllers
	
	/*
	 * Set the x and y of the mouse
	 * int x - mouse x
	 * int y - mouse y
	 */
	public void setMouseXY(int x, int y) {
		mouse.setXY(x, y);
		
		if (!mainOnScreen) {
			mainNodes.add(0, mouse);
		} 
	} //end setMouseXY
	
	/*
	 * Tell the canvas that there is a main chain on the screen already
	 */
	public void setMainOnScreen(boolean val) {
		mainOnScreen = val;
	} //end setMainOnScreen
	
	/*
	 * Get whether there is a main chain on the screen
	 */
	public boolean getMainOnScreen() {
		return mainOnScreen;
	} //end getMainOnScreen
	
	/*
	 * Get the current type for the canvas
	 */
	public ActionType getType() {
		return type;
	} //end getType
	
	/*
	 * Get the nodes on the main chain
	 */
	public ArrayList<Node> getMainNodes() {
		return mainNodes;
	} //end getMainNodes
	
	/*
	 * Add side node to the side nodes list
	 */
	public void addSideNode(Node n) {
		sideNodes.add(n);
	} //end addSideNode
	
	/*
	 * Get the side chains
	 */
	public ArrayList<Chain> getSideChains() {
		return sideChains;
	} //end getSideChains
	
	/*
	 * Update the type of the canvas from the palette type
	 */
	public void updateActionType() {
		type = palette.getSelectedType();
		if (type == ActionType.CLEAR) {
			mainOnScreen = false;
		} //if
	} //end updateActionType
	
	/*
	 * Get the step for main drawing
	 */
	public int getMainStep() {
		return mainStep;
	} //end getMainStep
	
	/*
	 * Set the step for main drawing
	 */
	public void setMainStep(int step) {
		mainStep = step;
	} //end setMainStep
	
} //end class
