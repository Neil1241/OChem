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

	private ArrayList<Node> nodes; //all the nodes on the screen
	private Node mouse; //for hovering effects
	
	public static Color BACKGROUND_COLOR; //background color
	
	private Compound master; //compound being drawn
	private Chain mainChain; //main chain
	private ArrayList<Chain> sideChains; //side chains
	
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
		nodes = new ArrayList<Node>();
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
		
		drawGhost(g2); //draw the selected chain by the mouse
		drawMain(g2); //draw the main chain
		drawSides(g2); //draw the side chains
		
		//draw all the nodes
		g2.setColor(Color.BLACK);
		for (Node n : nodes) {
			g2.fillOval(n.getX() - n.getRad(), n.getY() - n.getRad(), n.getRad() * 2, n.getRad() * 2);
		} //loop
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
				if (!mainOnScreen) {
					//calculating constants for rotated arm
					int arm = 150;
					int xOffset = (int) (arm * Math.cos(Math.toRadians(-30)));
					int yOffset = (int) (arm * Math.sin(Math.toRadians(-30)));
					
					//create stroke object for drawing lines
					BasicStroke bs = new BasicStroke(15.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
					g2.setStroke(bs);
	
					//set the color and draw the line
					g2.setColor(new Color(50,238,50, 255));
					g2.drawLine(mouse.getX(), mouse.getY(), mouse.getX() + xOffset, mouse.getY() + yOffset);
	
					//change the color and draw the nodes
					g2.setColor(new Color(50,50,50, 100));
					g2.fillOval(mouse.getCenterX(), mouse.getCenterY(), mouse.getDia(), mouse.getDia());
					g2.fillOval(mouse.getCenterX() + xOffset, mouse.getCenterY() + yOffset, mouse.getDia(), mouse.getDia());
				}
				break;
				
			case SIDE:
				
				break;
				
			case FUNC_GROUP:
				
				break;
		} //switch
	} //end drawGhost
	
	private void drawMain(Graphics2D g2) {
		
	}
	
	private void drawSides(Graphics2D g2) {
		
	}
	
	public void setMainSize(int main) {
		mainChain.setSize(main);
	}
	
	/*
	 * Update the screen
	 */
	public void update() {
		type = palette.getSelectedType();
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
	 * Add a node to the canvas
	 * Node n - new addition to Node list
	 */
	public void addNode(Node n) {
		nodes.add(n);
	} //end addNode
	
	/*
	 * Set the x and y of the mouse
	 * int x - mouse x
	 * int y - mouse y
	 */
	public void setMouseXY(int x, int y) {
		mouse.setXY(x, y);
	} //end setMouseXY
	
	/*
	 * Tell the canvas that there is a main chain on the screen already
	 */
	public void setMainOnScreen(boolean val) {
		mainOnScreen = val;
	} //end setMainOnScreen
} //end class
