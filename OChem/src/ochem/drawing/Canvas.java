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
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JComponent;

import ochem.View;
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
	
	//main 
	private Chain mainChain; //main chain
	private ArrayList<Node> mainNodes; //nodes for the main chain
	private boolean mainCyclo; //whether the main chain is cycloidal
	
	//side
	private ArrayList<Chain> sideChains; //side chains
	private ArrayList<Node> sideNodes; //nodes for the side chain
	private ArrayList<Integer> sideSizes; //size for the side chains
	private ArrayList<Boolean> sideCyclos; //whether cyclo chains are boolean or not
	
	private ActionType type; //type of action
	
	private boolean mainOnScreen; //whether a main chain is on the screen
	
	private int mainStep; //step for the "main" button
	private int sideStep; //step for the "side" button
	
	private DrawDirection ghostDir; //direction of the ghost chain
	private ArrayList<DrawDirection> directions; //directions for all the chains
	
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
	 * Create a canvas with its parent's width and height
	 * int parentWidth - width of the parent panel
	 * int parentHeight - height of the parent panel
	 * Palette palette - instance of the palette
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
		sideSizes = new ArrayList<Integer>();
		
		//instantiate lists for the nodes on the main chain and side chains
		mainNodes = new ArrayList<Node>();
		sideNodes = new ArrayList<Node>();
		
		//set the step numbers to zero when not being used, increment when needed
		mainStep = 0;
		sideStep = 0;
		
		//default the ghost direction to up right
		ghostDir = DrawDirection.UP_RIGHT;
		
		//initialize the directions list
		directions = new ArrayList<DrawDirection>();
		
		//set the cyclo values to false
		mainCyclo = false;
		
		//add the controllers to the canvas
		registerControllers();
	} //end constructor
	
	/*
	 * Draw all nodes, bonds and functional groups to the screen
	 * Graphics g - AWT object responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		//cast the more capable Graphics2D onto g
		Graphics2D g2 = (Graphics2D) g;
		
		//stroke object for drawing
		BasicStroke bs = new BasicStroke(15.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(bs);
		
		//background
		g2.setBackground(BACKGROUND_COLOR);
		g2.clearRect(View.PAD/2,View.PAD, width - 2*View.PAD, height - 2*View.PAD);
		
		//update the type to the palette's type
		this.type = palette.getSelectedType();
		
		//handle the actions for each type
		clearAction(g2);
		mainAction(g2);
		sideAction(g2);
	}  //end paintComponent
	
	/*
	 * Handles the actions for the clear flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void clearAction(Graphics2D g2) {
		//only draw the node if the type is CLEAR
		if (type == ActionType.CLEAR) {
			g2.setColor(new Color(50,238,50, 100)); //transparent green
			drawNode(g2, mouse);
			
			//set the main step to zero and for there to be no main chain on screen
			mainOnScreen = false;
			mainStep = 0;
			
			//set the side step to zero
			sideStep = 0;
			
			//clear the lists of nodes
			mainNodes.clear();
			sideNodes.clear();
		} //if
	} //end clearAction
	
	
	/*
	 * Handles the actions for the main flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void mainAction(Graphics2D g2) {
		//change what to draw based on what the value of main step is
		switch (mainStep) {
			//do nothing step
			case 0: 
			break;
				
			//size definition step
			case 1: 
				mainOnScreen = false;
				DrawingGUI.showMessage("Enter size of main chain: (ENTER)");
				g2.setColor(new Color(50,50,238, 100));
				drawNode(g2, mouse);
			break;
			
			//location selection step
			case 2: 
				mainOnScreen = false;
				DrawingGUI.showMessage("Cyclo? (Y/N)");
				g2.setColor(new Color(200,200,200, 100));
				drawChain(g2, mouse, DrawDirection.RIGHT, mainChain.getSize());
			break;
			
			//determine cyclo step
			case 3:
				mainOnScreen = false;
				DrawingGUI.showMessage("Select location for main chain: (CLICK)");
				g2.setColor(new Color(200,200,200, 100));
				
				if (mainCyclo) {
					drawCyclo(g2, mouse, mainChain.getSize());
				} else {
					drawChain(g2, mouse, DrawDirection.RIGHT, mainChain.getSize());					
				} //if
				
				break;
			
			//fixed on screen step
			case 4: 
				DrawingGUI.clear();
				g2.setColor(Color.BLACK);
				
				if (!mainOnScreen) { //first time called					
					if (mainCyclo) {
						mainNodes = drawCyclo(g2, mainNodes.get(0), mainChain.getSize());
					} else {
						mainNodes = drawChain(g2, mainNodes.get(0), DrawDirection.RIGHT, mainChain.getSize());			
					} //if
					mainOnScreen = true;
					
				} else { //all other times
					if (mainCyclo) {
						drawCyclo(g2, mainNodes.get(0), mainChain.getSize());
					} else {
						drawChain(g2, mainNodes.get(0), DrawDirection.RIGHT, mainChain.getSize());			
					} //if
					
				} //big if
				
			break;
			
		} //switch
	} //end mainAction
	
	/*
	 * Handles the actions for the side flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void sideAction(Graphics2D g2) {
		//change what to draw based on what the value of main step is
		switch (sideStep) {
			//do nothing step
			case 0: 
			break;
				
			//size definition step
			case 1: 
				DrawingGUI.showMessage("Enter size of side chain: (ENTER)");
				g2.setColor(new Color(238,50,50, 100));
				drawNode(g2, mouse);
				
				drawSides(g2);
			break;
			
			//location selection step
			case 2: 
				DrawingGUI.showMessage("Select location for side chain: (CLICK)");
				g2.setColor(new Color(200,200,200, 100)); //faint gray
				
				if (sideNodes.isEmpty()) {
					drawChain(g2, mouse, ghostDir, sideSizes.get(0)+1);
				} else {
					drawChain(g2, mouse, ghostDir, sideSizes.get(sideSizes.size()-1)+1);
				} //if
				
				//draw the side chains
				drawSides(g2);
				
				int start;
				if (mainCyclo) {
					start = 0;
				} else {
					start = 1;
				}
				
				//show nodes that can be clicked
				for (int i = start; i < mainNodes.size()-1; i++) {
					g2.setColor(mainNodes.get(i).getColor());
					drawNode(g2, mainNodes.get(i));
				} //loop
			break;
			
			//determine cyclo step
			case 3:
				DrawingGUI.showMessage("Cyclo? (Y/N)");
				
			break;
			
			//fixed on screen step
			case 4: 
				DrawingGUI.showMessage("");
				
				//draw the side chains	
				drawSides(g2);
			break;
		} //switch
	} //end sideAction
	
	/*
	 * Draw all the side chains to the screen
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void drawSides(Graphics2D g2) {
		//draw side chains
		g2.setColor(Color.black);
		for (int i = 0; i < sideNodes.size(); i++) {
			drawChain(g2, sideNodes.get(i), directions.get(i), sideSizes.get(i) + 1);
		} //loop
	} //end drawSides
	
	/*
	 * Draw a node to the screen
	 * Graphics2D g2 - object responsible for drawing
	 * Node n - node to draw
	 */
	private void drawNode(Graphics2D g2, Node n) {
		g2.fillOval(n.getCenterX(), n.getCenterY(), n.getDia(), n.getDia());
	} //end drawNode
	
	/*
	 * Draw a cycloidal chain
	 * Graphics2D g2 - object responsible for drawing
	 * Node start - starting node
	 * int chainSize - size of chain
	 */
	private ArrayList<Node> drawCyclo(Graphics2D g2, Node start, int chainSize) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		g2.setColor(Color.BLACK);
		
		int[] xPts = new int[chainSize+1];
		int[] yPts = new int[chainSize+1];
		
		int arm = 120;
		
	    for (int i = 0; i < chainSize+1; i++) {
	    	
	    	xPts[i] = start.getX() + (int) (arm - arm * Math.cos(Math.toRadians(360.0/chainSize * i)));
	    	yPts[i] = start.getY() + (int) (arm * Math.sin(Math.toRadians(360.0/chainSize * i)));
	    	
	    	nodes.add(new Node(xPts[i], yPts[i], 20));
	    } //loop
	    
	    g2.drawPolyline(xPts, yPts, chainSize+1);
	    
	    return nodes;
	} //end drawCyclo
	
	/*
	 * Draws the main chain from a starting point
	 * Node start - starting point
	 * DrawDirection dir - direction to draw in
	 * int chainSize - size of chain to draw
	 * return nodes - list of nodes for that chain
	 */
	private ArrayList<Node> drawChain(Graphics2D g2, Node start, DrawDirection dir, int chainSize) {
		
		//starting x and y coordinates
		int x1 = start.getX();
		int y1 = start.getY();
		
		double[] angles = angleFromDirection(dir); //get the angles based on the direction
		int arm = 120; //length of bonds in pixels
		
		ArrayList<Node> nodes = new ArrayList<Node>(); //list of nodes
		
		for (int i = 0; i < chainSize-1; i++) {
			//current angle to draw (alternate between the 2 angles)
			double ang = angles[i % 2]; 
			
			//calculate the end points for the line
			int x2 = (int) (arm * Math.cos(Math.toRadians(ang))) + x1;
			int y2 = (int) (arm * Math.sin(Math.toRadians(ang))) + y1;
			
			//draw the line
			g2.drawLine(x1, y1, x2, y2);
			
			nodes.add(new Node(x1,y1,20)); //add the current node to the list
			
			//change the start point to this end for next loop
			x1 = x2;
			y1 = y2;
		} //loop
		
		nodes.add(new Node(x1,y1,20)); //add the last node
	    
		return nodes;
	} //end drawChain
	
	
	/*
	 * Choose the pair of angles from the direction chosen
	 * DrawDirection dir - direction to draw in
	 */
	private double[] angleFromDirection(DrawDirection dir) {
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
	 * Set the size of the main chain
	 * int main - size of the main chain
	 */
	public void setMainSize(int main) {
		mainChain = new Chain(main, "-1");
	} //end setMainSize
	
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
		
		//if there is no main chain on the screen, set the potential position to the mouse point
		if (mainStep == 3) {
			if (mainNodes.isEmpty()) {
				mainNodes.add(new Node(mouse.getX(), mouse.getY(), 20));
			} else {
				mainNodes.set(0, new Node(mouse.getX(), mouse.getY(), 20));
			}

		    System.out.println(toString() + " start position: " + mainNodes.get(0).getX() +" "+ mainNodes.get(0).getY());
		} //if 
	} //end setMouseXY
	
	/*
	 * Tell the canvas that there is a main chain on the screen already
	 * boolean val - whether the main chain is on the screen
	 */
	public void setMainOnScreen(boolean val) {
		mainOnScreen = val;
	} //end setMainOnScreen
	
	/*
	 * Get whether there is a main chain on the screen
	 * return mainOnScreen - whether the main chain is on the screen
	 */
	public boolean getMainOnScreen() {
		return mainOnScreen;
	} //end getMainOnScreen
	
	/*
	 * Get the current type for the canvas
	 * return type - action type of the canvas
	 */
	public ActionType getType() {
		return type;
	} //end getType
	
	/*
	 * Get the nodes on the main chain
	 * return nodes - list of all the nodes of the main chain
	 */
	public ArrayList<Node> getMainNodes() {
		return mainNodes;
	} //end getMainNodes
	
	/*
	 * Set the nodes on the main chain to the list passed
	 * ArrayList<Node> mainNodes - list to change nodes to
	 */
	public void setMainNodes(ArrayList<Node> mainNodes) {
		this.mainNodes = mainNodes;
	} //end setMainNodes h
	
	/*
	 * Add side node to the side nodes list
	 * Node n - node to add to the side Nodes
	 */
	public void addSideNode(Node n) {
		sideNodes.add(n);
	} //end addSideNode
	
	/*
	 * Get the side chains
	 * return sideChains - list of all the side chains
	 */
	public ArrayList<Chain> getSideChains() {
		return sideChains;
	} //end getSideChains
	
	/*
	 * Update the type of the canvas from the palette type
	 */
	public void updateActionType() {
		type = palette.getSelectedType();
		
		//if the type was set to clear, wipe the main chain
		if (type == ActionType.CLEAR) {
			mainOnScreen = false;
		} //if
	} //end updateActionType
	
	/*
	 * Set the step for main drawing
	 * int step - step for main drawing
	 */
	public void setMainStep(int step) {
		//if outside of the range
		if (step < 0 || step > 4) {
			throw new IllegalArgumentException("Too big for me :(");
		} else {
			mainStep = step;
		}  //if
		
		this.update();
	} //end setMainStep
	
	/*
	 * Get the step for main drawing
	 * return mainStep - step for the main drawing
	 */
	public int getMainStep() {
		return mainStep;
	} //end getMainStep
	
	/*
	 * Set the step for side drawing
	 * int step - step for side drawing
	 */
	public void setSideStep(int step) {
		if (step < 0 || step > 3) {
			throw new IllegalArgumentException("Too big for me :(");
		} else {
			sideStep = step;
		} //if 
		
		this.update();
	} //end sideStep
	
	/*
	 * Get the side step for drawing
	 * return sideStep - step for drawing side chains
	 */
	public int getSideStep() {
		return sideStep;
	} //end getSideStep
	
	/*
	 * Add a side size to the screen
	 * int size - new size of side chain
	 */
	public void addSideSize(int size) {
		sideSizes.add(size);
	} //end addSideSize
	
	/*
	 * Get the size of the side nodes list
	 * return - size of side nodes list
	 */
	public int getSideNodesSize() {
		return sideNodes.size();
	} //end getSideNodesSize
	
	public String toString() {
		return "Canvas"; //dbg
	}
	
	/*
	 * Set the direction for the ghost to be drawn
	 * DrawDirection dir - new direction for ghost chain
	 */
	public void setGhostDirection(DrawDirection dir) {
		ghostDir = dir;
	} //end setGhostDirection
	
	/*
	 * Add a direction for a side chain
	 * DrawDirection dir - direction for latest side chain
	 */
	public void addSideDirection(DrawDirection dir) {
		directions.add(dir);
	} //end addSideDirection
	
	/*
	 * Set the main chain to be cycloidal
	 * boolean val - whether the main chain is cyclo or not
	 */
	public void setMainCyclo(boolean val) {
		mainCyclo = val;
	} //end setMainCyclo
	
	/*
	 * Get whether the main chain is a cyclo or not
	 * return mainCyclo - whether main chain is cycloidal or not
	 */
	public boolean getMainCyclo() {
		return mainCyclo;
	} //end getMainCyclo
} //end class
