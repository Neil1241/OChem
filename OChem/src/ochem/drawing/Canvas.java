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

import ochem.View;
import ochem.drawing.CanvasUtil.ActionType;
import ochem.drawing.CanvasUtil.DrawDirection;
import ochem.organic.Chain;
import ochem.organic.Compound;

public class Canvas extends JComponent {
	//Attributes
	private int width; //width of component
	private int height; //height of component
	private Palette palette; //instance of the palette
	
	private Node mouse; //for hovering effects
	
	public static Color BACKGROUND_COLOR; //background color
	
	//main 
	private Compound compound; //compound being drawn
	private ArrayList<Node> mainNodes; //nodes for the main chain
	
	//side
	private ArrayList<Chain> sideChains; //side chains
	private ArrayList<Node> sideNodes; //nodes for the side chain
	private ArrayList<DrawDirection> directions; //directions for side chains
	
	private boolean mainOnScreen; //whether a main chain is on the screen

	private ActionType type; //type of action
	private int mainStep; //step for the "main" button
	private int sideStep; //step for the "side" button
	
	private DrawDirection ghostDir; //direction of the ghost chain
	
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
		compound = new Compound(0);		
		
		sideChains = new ArrayList<Chain>();
		
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
			
			//clear the directions
			directions.clear();
			ghostDir = DrawDirection.RIGHT;
			
			//clear the compound and the chains
			compound = new Compound(0);
			sideChains.clear();
			
			//reset the steps
			mainStep = 0;
			sideStep = 0;
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
				drawChain(g2, mouse, DrawDirection.RIGHT, compound.getMainSize(), false);
			break;
			
			//determine cyclo step
			case 3:
				mainOnScreen = false;
				DrawingGUI.showMessage("Select location for main chain: (CLICK)");
				g2.setColor(new Color(200,200,200, 100));
				
				if (compound.getMainChain().isCyclo()) {
					drawCyclo(g2, mouse, compound.getMainSize(), false, null);
				} else {
					drawChain(g2, mouse, DrawDirection.RIGHT, compound.getMainSize(), false);					
				} //if
				
				drawSides(g2);
				break;
			
			//fixed on screen step
			case 4: 
				DrawingGUI.clear();
				g2.setColor(Color.BLACK);
				
				if (!mainOnScreen) { //first time called					
					if (compound.getMainChain().isCyclo()) { 
						mainNodes = drawCyclo(g2, mainNodes.get(0), compound.getMainSize(), false, null);
					} else {
						mainNodes = drawChain(g2, mainNodes.get(0), DrawDirection.RIGHT, compound.getMainSize(), false);			
					} //if
					mainOnScreen = true;
					
				} else { //all other times
					if (compound.getMainChain().isCyclo()) {
						drawCyclo(g2, mainNodes.get(0), compound.getMainSize(), false, null);
					} else {
						drawChain(g2, mainNodes.get(0), DrawDirection.RIGHT, compound.getMainSize(), false);	
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
				DrawingGUI.showMessage("Cyclo? (Y/N)");
				g2.setColor(new Color(200,200,200, 100));
				drawChain(g2, mouse, DrawDirection.RIGHT, sideChains.get(sideChains.size()-1).getSize() + 1, true);
				
				drawSides(g2);
			break;
			
			//determine cyclo step
			case 3:				
				DrawingGUI.showMessage("Select location for side chain: (CLICK)");
				g2.setColor(new Color(200,200,200, 100)); //faint gray
				
				//create and draw the would-be chain
				Chain ghost = sideChains.get(sideChains.size()-1);
				if (ghost.isCyclo()) {
					drawCyclo(g2, mouse, ghost.getSize(), true, ghostDir);
				} else {
					drawChain(g2, mouse, ghostDir, ghost.getSize()+1, true);
				}
				
				//draw the side chains already on the compound
				drawSides(g2);
				
				int start;
				if (compound.getMainChain().isCyclo()) {
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
//		g2.setColor(Color.black);
		for (int i = 0; i < sideNodes.size(); i++) {
			if (sideChains.get(i).isCyclo()) {
				g2.setColor(Color.BLACK);
				drawCyclo(g2, sideNodes.get(i), sideChains.get(i).getSize(), true, directions.get(i));
			} else {
				g2.setColor(Color.BLACK);
				drawChain(g2, sideNodes.get(i), directions.get(i), sideChains.get(i).getSize() + 1, true);
			} //if
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
	 * boolean extend - whether the cyclo is a side chain or not
	 * DrawDirection dir - direction to draw in
	 * return nodes - nodes for that chain
	 */
	private ArrayList<Node> drawCyclo(Graphics2D g2, Node start, int chainSize, boolean extend, DrawDirection dir) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		//offset length in pixels
		int r = 120;
		
		//start point
		int x1 = start.getX();
		int y1 = start.getY();
		
		//end point
		int x2 = 0;
		int y2 = 0;
		
		//value to increment angle by each step
		double theta = Math.toRadians(-360.0 / chainSize);
		
		//offsets based on side and size
		int xOffset;
		int yOffset;
		double angOffset;
		double rotAngle;
		
		//if a side chain
		if (extend) { //calculate offsets
			angOffset = 2*Math.toRadians(CanvasUtil.cycloAngle(dir));
			rotAngle = CanvasUtil.cycloAngOffset(chainSize);
			xOffset = (int) (r * Math.cos(angOffset));
			yOffset = (int) (r * Math.sin(angOffset));
			
		} else { //otherwise no offsets
			angOffset = 0;
			rotAngle = 0;
			xOffset = 0;
			yOffset = 0;
		} //if
		
		//translate start point
		x1 += xOffset;
		y1 += yOffset;
 		
		//draw the cyclo
		for (int i = 0; i < chainSize; i++) {
			x2 = x1 + (int) (r * Math.cos(theta * i + angOffset + rotAngle));
			y2 = y1 + (int) (r * Math.sin(theta * i + angOffset + rotAngle));
			
			g2.drawLine(x1, y1, x2, y2);
			
			//add the node to the list
			nodes.add(new Node(x1, y1, 20, ""+(i+1)));
			
			x1 = x2;
			y1 = y2;
		} //loop
		
//		nodes.add(new Node(x1, y1, 20, ""+(chainSize)));
		
		//draw the final bonds
		g2.drawLine(start.getX(), start.getY(), x2, y2);
		if (extend) {
			g2.drawLine(start.getX(), start.getY(), start.getX() + xOffset, start.getY() + yOffset);
		} //if
		
		g2.setFont(g2.getFont().deriveFont(50.0F));
		g2.setColor(Color.RED);
		for (Node n : mainNodes) {
			g2.drawString(n.getTag(), n.getCenterX(), n.getCenterY());
		}
		g2.setColor(Color.BLACK);
		
		return nodes;
	} //end drawCyclo
	
	/*
	 * Draws the main chain from a starting point
	 * Node start - starting point
	 * DrawDirection dir - direction to draw in
	 * int chainSize - size of chain to draw
	 * boolean side - whether the chain is on the side or not
	 * return nodes - list of nodes for that chain
	 */
	private ArrayList<Node> drawChain(Graphics2D g2, Node start, DrawDirection dir, int chainSize, boolean side) {
		
		//starting x and y coordinates
		int x1 = start.getX();
		int y1 = start.getY();
		
		double[] angles = CanvasUtil.angleFromDirection(dir); //get the angles based on the direction
		int arm = 120; //length of bonds in pixels
		
		ArrayList<Node> nodes = new ArrayList<Node>(); //list of nodes
		
		int tagStart;
		if (side) {
			tagStart = 1;
		} else {
			tagStart = 0;
		}
		
		for (int i = 0; i < chainSize-1; i++) {
			//current angle to draw (alternate between the 2 angles)
			double ang = angles[i % 2]; 
			
			//calculate the end points for the line
			int x2 = (int) (arm * Math.cos(Math.toRadians(ang))) + x1;
			int y2 = (int) (arm * Math.sin(Math.toRadians(ang))) + y1;
			
			//draw the line
			g2.drawLine(x1, y1, x2, y2);
			
			if (i >= tagStart) {
				if (side) {
					nodes.add(new Node(x1,y1,20, "" + (i))); //add the current node to the list
				} else {
					nodes.add(new Node(x1,y1,20, "" + (i+1))); //add the current node to the list
				} //small if
			} //big if
			
			//change the start point to this end for next loop
			x1 = x2;
			y1 = y2;
		} //loop
		
		//add the last node
		if (side) 
			nodes.add(new Node(x1,y1,20, "" + (chainSize-1))); 
		else 
			nodes.add(new Node(x1,y1,20, "" + (chainSize)));
		
		return nodes;
	} //end drawChain

	
	/*
	 * Set the size of the main chain
	 * int main - size of the main chain
	 */
	public void setMainSize(int main) {
		compound.setMainSize(main);
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
	} //end setMouseXY
	
	/*
	 * Set the node for the start position of the main chain
	 * int x - starting x
	 * int y - starting y
	 */
	public void setMainStart(int x, int y) {
		if (mainNodes.isEmpty()) {
			mainNodes.add(new Node(mouse.getX(), mouse.getY(), 20));
		} else {
			mainNodes.set(0, new Node(mouse.getX(), mouse.getY(), 20));
		} //inner if
	} //end setMainStart
	
	
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
	 * Set the main chain to be cycloidal
	 * boolean val - whether main chain is cyclo or not
	 */
	public void setMainCyclo(boolean val) {
		compound.getMainChain().setCyclo(val);
	} //end setMainCyclo
	
	/*
	 * Get whether the main chain is a cyclo or not
	 * return - whether main chain is cycloidal or not
	 */
	public boolean getMainCyclo() {
		return compound.getMainChain().isCyclo();
	} //end getMainCyclo

	/*
	 * Add a side cyclo to the list
	 * boolean val - value to add to the side lists
	 */
	public void addSideCyclo(boolean val) {
		sideChains.get(sideChains.size() - 1).setCyclo(val);
	} //end addSideCyclo
	
	/*
	 * Add a direction for a side chain
	 * DrawDirection dir - direction for latest side chain
	 */
	public void addSideDirection(DrawDirection dir) {
		directions.add(dir);
	} //end addSideDirection
	
	/*
	 * Set the step for side drawing
	 * int step - step for side drawing
	 */
	public void setSideStep(int step) {
		if (step < 0 || step > 4) {
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
		if (sideChains.isEmpty()) {
			sideChains.add(new Chain(size, ""));
		} else {
			sideChains.add(new Chain(size, ""));			
		} //if
	} //end addSideSize
	
	/*
	 * Add side node to the side nodes list
	 * Node n - node to add to the side Nodes
	 */
	public void addSideNode(Node n) {
		sideNodes.add(n);
		Chain lastSide = sideChains.get(sideChains.size()-1);
		lastSide.setLocation(n.getTag());
		compound.addSideChain(lastSide.getSize(), lastSide.getLocation(), lastSide.isCyclo(), lastSide.isBenzene());
		compound.addFunctionalLocation(lastSide.getLocation());
	} //end addSideNode
	
	/*
	 * Get the side chains
	 * return sideChains - list of all the side chains
	 */
	public ArrayList<Chain> getSideChains() {
		return sideChains;
	} //end getSideChains
	
	/*
	 * Get the compound
	 * return compound - compound being created
	 */
	public Compound getCompound() {
		return compound;
	} //end getCompound
} //end class
