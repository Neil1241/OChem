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
	
	/*
	 *  Types of action to determine different drawing features
	 */
	public static enum ActionType {
		CLEAR,
		MAIN,
		SIDE,
		FUNC_GROUP
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
		mainChain = new Chain(0, -1);		
		sideChains = new ArrayList<Chain>();
		
		//instantiate lists for the nodes on the main chain and side chains
		mainNodes = new ArrayList<Node>();
		sideNodes = new ArrayList<Node>();
		
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
		
		//if there is no main on screen
		if (!mainOnScreen) {
			drawGhost(g2); //draw the selected chain by the mouse
		} else {
			drawChain(g2, mainNodes.get(0), 30); //draw the main chain
			drawSides(g2); //draw the side chains
		} //if
		
		
	}  //end paintComponent
	
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
					drawChain(g2, mouse, 30);
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
	private void drawChain(Graphics2D g2, Node start, double startAngle) {
		BasicStroke bs = new BasicStroke(15.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(bs);
		
		if (mainOnScreen) {
			g2.setColor(Color.BLACK);
		} else {
			g2.setColor(new Color(50,50,50, 100));
		}
		
		int startX = start.getCenterX();
		int startY = start.getCenterY();
		
		double angle = Math.toRadians(startAngle);
		int arm = 150;
		
		for (int i = 1; i < mainChain.getSize(); i++) {
			//change angle based on even/odd
			angle *= -1;
			
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
		
		//add last node
		mainNodes.add(new Node(startX, startY, 30));
	}
	
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
		type = palette.getSelectedType();
		if (type == ActionType.CLEAR) {
			mainOnScreen = false;
		}
		
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
	}
} //end class
