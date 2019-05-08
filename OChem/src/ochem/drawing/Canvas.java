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

import ochem.drawing.Node.NodeType;

public class Canvas extends JComponent {
	//Attributes
	private int width; //width of component
	private int height; //height of component

	private Node[][] nodes; //all the nodes on the screen
	private final int NODE_RADIUS; //node radius
	public static Color BACKGROUND_COLOR;
	
	/*
	 * Create a canvas with its parent's width and height
	 * int parentWidth - width of the parent panel
	 * int parentHeight - height of the parent panel
	 */
	public Canvas(int parentWidth, int parentHeight) {
		super();
		this.width = (int) (parentWidth * 0.8);
		this.height = (int) (parentHeight);
		
		this.setPreferredSize(new Dimension(width, height));
		
		NODE_RADIUS = 20;
		createNodes();
		
		BACKGROUND_COLOR = new Color(224, 255, 253); //pale blue
		
		registerControllers();
	} //end constructor
	
	/*
	 * Draw all nodes, bonds and functional groups to the screen
	 * Graphics g - AWT object responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//background
		g2.setColor(BACKGROUND_COLOR); 
		g2.fillRect(0, 0, width, height);
		
		//draw all nodes on the screen
		drawNodes(g2);
		
		//draw bonds based on the node states
		drawBonds(g2);
	}  //end paintComponent
	
	/*
	 * Create all the nodes in the array
	 */
	private void createNodes() {
		nodes = new Node[10][6];
		int widthChunk = (width - 100) / (nodes.length+1);
		int heightChunk = (height - 50) / (nodes[0].length+1);
		
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				nodes[i][j] = new Node(widthChunk * (i+1), heightChunk * (j+1), NODE_RADIUS);
			} //inner
		} //outer
	} //end createNodes
	
	/*
	 * Draw a node to the screen
	 * Graphics2D g2 - object to use for drawing
	 * Node n - node to draw
	 */
	private void drawNode(Graphics2D g2, Node n) {
		g2.setColor(n.getColor());
		
		g2.fillOval(n.getX() - n.getRad(), n.getY() - n.getRad(), 2 * n.getRad(), 2 * n.getRad());
	} //end drawNode

	/*
	 * Draw all the nodes to the screen
	 * Graphics2D g2 - object to use for drawing
	 */
	private void drawNodes(Graphics2D g2) {
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				drawNode(g2, nodes[i][j]);
			} //inner
		} //outer
	} //end drawNodes
	
	/*
	 * Draw all bonds to the screen
	 */
	private void drawBonds(Graphics2D g2) {
		ArrayList<Node> bondedNodes = new ArrayList<Node>();
		
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				
				//if a bond type, add it to the bonded nodes list
				if (nodes[i][j].getType() == NodeType.SINGLE_BOND) {
					bondedNodes.add(nodes[i][j]);
				} //if
			} //inner
		} //outer
		
		g2.setStroke(new BasicStroke(10.0F));
		g2.setColor(Color.BLACK);
		for (int d = 0; d < bondedNodes.size()-1; d++) {
			g2.drawLine(bondedNodes.get(d).getX(), bondedNodes.get(d).getY(), 
						bondedNodes.get(d+1).getX(), bondedNodes.get(d+1).getY());
		} //loop
	} //end drawBonds
	
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
	} //end registerControllers
	
	/*
	 * Get all the nodes
	 */
	public Node[][] getNodes() {
		return nodes;
	} //end getNodes
} //end class
