package ochem.drawing;

import java.awt.BasicStroke;

/*
 * Canvas
 * Created by: Neil Balaskandarajah
 * Last modified: 05/08/2019
 * Component that draws all the nodes to the screen
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import ochem.organic.Compound;

public class Canvas extends JComponent {
	//Attributes
	private int width; //width of component
	private int height; //height of component
	private Palette palette; //instance of the palette

	private ArrayList<Node> nodes; //all the nodes on the screen
	private Node mouse; //for hovering effects
	
	public static Color BACKGROUND_COLOR;
	
	private Compound master; //compound being drawn
	private ActionType type;
	
	public static enum ActionType {
		MAIN,
		SIDE,
		FUNC_GROUP
	}
	
	/*
	 * Create a canvas with its parent's width and height
	 * int parentWidth - width of the parent panel
	 * int parentHeight - height of the parent panel
	 */
	public Canvas(int width, int height, Palette palette) {
		super();
		this.width = width;
		this.height = height;
		this.palette = palette;
		
		this.setPreferredSize(new Dimension(this.width, this.height));
		
		nodes = new ArrayList<Node>();
		mouse = new Node(20);
		
		BACKGROUND_COLOR = new Color(224, 255, 253); //pale blue
		
		type = ActionType.MAIN;
		
		registerControllers();
	} //end constructor
	
	/*
	 * Draw all nodes, bonds and functional groups to the screen
	 * Graphics g - AWT object responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//background
//		g2.setColor(BACKGROUND_COLOR); 
//		g2.fillRect(0, 0, width, height);
		g2.setBackground(BACKGROUND_COLOR);
		g2.clearRect(0,0, width + 30, height + 30);
		
		drawGhost(g2); //draw the selected chain by the mouse
		drawMain(g2); //draw the main chain
		drawSides(g2); //draw the side chains
		
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
			case MAIN:
				int arm = 150;
				int xOffset = (int) (arm * Math.cos(Math.toRadians(-30)));
				int yOffset = (int) (arm * Math.sin(Math.toRadians(-30)));
				
				BasicStroke bs = new BasicStroke(15.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
				g2.setStroke(bs);

				g2.setColor(new Color(50,238,50, 255));
				g2.drawLine(mouse.getX(), mouse.getY(), mouse.getX() + xOffset, mouse.getY() + yOffset);

				g2.setColor(new Color(50,50,50, 100));
				g2.fillOval(mouse.getCenterX(), mouse.getCenterY(), mouse.getDia(), mouse.getDia());
				g2.fillOval(mouse.getCenterX() + xOffset, mouse.getCenterY() + yOffset, mouse.getDia(), mouse.getDia());
				
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
	 * Calculate the angle between two nodes for drawing double and triple bonds
	 * Node p1 - first node
	 * Node p2 - secondd node
	 * return - angle between the two values in radians
	 */
	public static double calcAngle(Node p1, Node p2) {
		double dx = p2.getX() - p1.getX();
		double dy = p1.getY() - p2.getY(); //1 - 2 instead because y is flipped in Graphics
		
		double angle = 0;
		
		//if goal point lies on x or y axis (dx or dy equal to zero)
		if(dy == 0) { //if no change in y
			if (dx > 0) {
				angle = 0; //dead right
			} else if (dx < 0) {
				angle = 180; //dead left
			} //if
		} else if (dx == 0) { //if no change in x
			if (dy > 0) {
				angle = 90; //dead ahead
			} else if (dy < 0) {
				angle = 270; //dead behind
			} //if
		} else {
			angle = Math.atan2(dy,dx);
		} //big if
		
		return angle;
	} //end calcAngleRad
	
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
} //end class
