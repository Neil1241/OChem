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
	private Palette palette; //instance of the palette

	private Node[][] nodes; //all the nodes on the screen
	private final int NODE_RADIUS; //node radius
	public static Color BACKGROUND_COLOR;
	
	private ArrayList<Node> singleBonds; //all the single bonded nodes in a list
	private ArrayList<Node> doubleBonds; //all the double bonded nodes in a list
	private ArrayList<Node> tripleBonds; //all the double bonded nodes in a list
	
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
		//all regular arrays
		nodes = new Node[6][10];
		int widthChunk = (width - 100) / (nodes.length+1);
		int heightChunk = (height - 50) / (nodes.length+1);
		
		int tag = 0;
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				int x = widthChunk * (i+1);
				int y = heightChunk * (j+1);
				
				nodes[i][j] = new Node(x, y, NODE_RADIUS);
				nodes[i][j].setTag(tag);
				tag++;
			}
		} //outer
		
		//instantiate lists
		singleBonds = new ArrayList<Node>();
		doubleBonds = new ArrayList<Node>();
		tripleBonds = new ArrayList<Node>();
	} //end createNodes
	
	/*
	 * Draw a node to the screen
	 * Graphics2D g2 - object to use for drawing
	 * Node n - node to draw
	 */
	private void drawNode(Graphics2D g2, Node n) {
		g2.setColor(n.getColor());
		
		//draw the node using its position and dimensions
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
		g2.setStroke(new BasicStroke(10.0F));
		g2.setColor(Color.BLACK);
		//draw lines between everything in the single bonds list
		for (int d = 0; d < singleBonds.size()-1; d++) { 
			g2.drawLine(singleBonds.get(d).getX(), singleBonds.get(d).getY(), 
					singleBonds.get(d+1).getX(), singleBonds.get(d+1).getY());
		} //loop

		g2.setColor(Color.RED);
		//draw lines between everything in the double bonds list
		for (int d = 0; d < doubleBonds.size()-1; d++) { 
			double angle = calcAngle(doubleBonds.get(d), doubleBonds.get(d+1));
			System.out.println(angle);
			g2.drawLine(doubleBonds.get(d).getX(), doubleBonds.get(d).getY(), 
					doubleBonds.get(d+1).getX(), doubleBonds.get(d+1).getY());
		} //loop

		g2.setColor(Color.BLUE);
		//draw lines between everything in the single bonds list
		for (int d = 0; d < tripleBonds.size()-1; d++) { 
			
			//rotate to draw bonds
			g2.drawLine(tripleBonds.get(d).getX(), tripleBonds.get(d).getY(), 
					tripleBonds.get(d+1).getX(), tripleBonds.get(d+1).getY());
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
	
	//NODES
	
	/*
	 * Get all the nodes in the canvas
	 * return nodes - nodes in the canvas
	 */
	public Node[][] getNodes() {
		return nodes;
	} //end getNodes
	
	/*
	 * Get all the bonded nodes depending on the type passed
	 * NodeType type - type of bonds to return
	 * return output - list of nodes depending on the type
	 */
	public ArrayList<Node> getBonds(NodeType type) {
		ArrayList<Node> output = new ArrayList<Node>();
		switch (type) {
			case SINGLE_BOND:
				output = singleBonds;
				break;
			
			case DOUBLE_BOND:
				output = doubleBonds;
				break;
			
			case TRIPLE_BOND:
				output = tripleBonds;
				break;
		} //switch
		
		return output;
	} //end getNodes
	
	/*
	 * Get the selected node type
	 * return - palette's selected type
	 */
	public NodeType getSelectedType() {
		return palette.getSelectedType();
	} //end getSelectedType
	
	/*
	 * Remove a node from all lists
	 * Node n - node to remove
	 */
	public void clearNode(Node n) {
		//remove the node from all the lists
		singleBonds.remove(n);
		doubleBonds.remove(n);
		tripleBonds.remove(n);
	} //end clearNode
	
	/*
	 * Calculate the angle between two nodes for drawing double and triple bonds
	 * Node p1 - first node
	 * Node p2 - secondd node
	 * return - angle between the two values in radians
	 */
	public static double calcAngle(Node p1, Node p2) {
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		
		return Math.atan2(dy,dx);
	} //end calcAngleRad
} //end class
