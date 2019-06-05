package ochem.naming;
/* NamingGUI.java
 * Jordan Lin
 * May 24, 2019
 */

import java.awt.Dimension;
import java.util.ArrayList;

//import packages
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ochem.OChem;
import ochem.View;
import ochem.drawing.Canvas;
import ochem.drawing.DrawingUtil;
import ochem.drawing.DrawingUtil.DrawDirection;
import ochem.drawing.Node;
import ochem.organic.Chain;
import ochem.organic.OrganicUtil;

public class NamingGUI extends JPanel {
	//declare instance variables
	private static JTextField input;
	private NamingModel model = new NamingModel();
	private JLabel test= new JLabel("");
	private Canvas c;
	private int width = (int) (0.5 * OChem.width + 2 * View.PAD);
	private int height = (int) (0.5 * OChem.height + 2 * View.PAD);

	public NamingGUI() {
		super();
		this.layoutView();
		this.model.setGUI(this);
		this.registerControllers();
	}
	
	private void layoutView()
	{
		//create and set layout
		BoxLayout shell = new BoxLayout(this,BoxLayout.Y_AXIS);
		this.setLayout(shell);
	
		//configure the heights and widths
		input= new JTextField(20);
		input.setPreferredSize(new Dimension(OChem.width/10,OChem.height/20));
		input.setFont(DrawingUtil.getFileFont(DrawingUtil.OXYGEN_LOCATION));
		test.setFont(DrawingUtil.getFileFont(DrawingUtil.OXYGEN_LOCATION));
		c = new Canvas(this.width,this.height);
		
		//add components to panel
		this.add(c);
		this.add(test);
		this.add(input);

	}
	
	private void registerControllers() {
		TextInputController t = new TextInputController(this.model);
		input.addActionListener(t);
	}
	
	public void update() {
		if (!this.model.isValid()) {
			this.test.setText("Not A Valid Compound");
			this.model.setValid(true);
		}
		else {
			this.test.setText(OrganicUtil.nameFromCompound(this.model.getCompound()));
			this.input.setText(null);
			
			//clear the canvas of all data so it can be given data only for the new compound
			c.reset();
			
			this.c.setCompound(this.model.getCompound()); //set the compound
			
			//set up the drawing
			this.setUpMain(this.calcMainStart()); //set up main drawing
			this.setUpSides(); //set up side drawing
			this.setUpBonds(); //set up bond drawing
			
			this.c.updateDisplay();
		}
	}
	
	/*
	 * Request the focus of the input field
	 */
	public static void requestFieldFocus() {
		input.requestFocus();
	} //end getFieldFocus
	
	//main for testing purposes
	public static void main(String[] args) {
		JFrame f= new JFrame();
		NamingGUI g= new NamingGUI();
		
		
		f.setLocation(OChem.width/2 - g.width/2, OChem.height/2 - 3*g.height/4);
		f.setContentPane(g);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();		
	}
	
	//SET UP CANVAS//
	/*
	 * Set the canvas main start position based on the compound size
	 * return - starting node for the main chain
	 */
	private Node calcMainStart() {
		double angle = Math.toRadians(DrawingUtil.angleFromDirection(DrawDirection.RIGHT)[0]);
		
		//width of the chain
		int xOffset;
		int yOffset;
		
		//if a regular chain
		if (!model.getCompound().getMainChain().isBenzene() && !model.getCompound().getMainChain().isCyclo()) {
			//width of chain
			xOffset = (model.getCompound().getMainSize() - 1) * 
					DrawingUtil.rCos(DrawingUtil.CHAIN_ARM, angle);
			
			//height of chain
			yOffset = -DrawingUtil.rSin(DrawingUtil.CHAIN_ARM, angle);
			
		} else { //benzene or cycloidal chain
			//offsets equal to the radius of the circumscribed circle
			xOffset = (int) (DrawingUtil.CYCLO_RAD);
			yOffset = (int) (DrawingUtil.CYCLO_RAD);
		} //if
		
		//center the chain on the screen
		int x = (width - xOffset) / 2;
		int y = (height + yOffset) / 2;
		
		return new Node(x, y, DrawingUtil.NODE_RAD);
	} //end setMainStart
	
	/*
	 * Set all the main nodes to the canvas
	 * Node start - starting node for the chain
	 */
	private void setUpMain(Node start) {
		ArrayList<Node> nodes = new ArrayList<Node>(); //list of nodes
		
		if (!model.getCompound().getMainChain().isBenzene() && !model.getCompound().getMainChain().isCyclo()) {
			double[] angles = DrawingUtil.angleFromDirection(DrawDirection.RIGHT); // get the angles based on the direction
			int arm = DrawingUtil.CHAIN_ARM;
			
			int x1 = start.getX();
			int y1 = start.getY();
			
			for (int i = 0; i < model.getCompound().getMainSize() - 1; i++) {
				// current angle to draw (alternate between the 2 angles)
				double ang = angles[i % 2];

				// calculate the end points for the line
				int x2 = (int) (arm * Math.cos(Math.toRadians(ang))) + x1;
				int y2 = (int) (arm * Math.sin(Math.toRadians(ang))) + y1;
				
				// set the location, radius, tag, color and save the node to the list
				nodes.add(new Node(x1, y1, DrawingUtil.NODE_RAD, "" + (i + 1)));	

				// change the start point to this end for next loop
				x1 = x2;
				y1 = y2;
			} // loop
			
			//add the last node
			nodes.add(new Node(x1, y1, DrawingUtil.NODE_RAD, "" + (model.getCompound().getMainSize())));
			
		} else {
			// offset length in pixels
			int r = DrawingUtil.CYCLO_RAD;

			// start point
			int x1 = start.getX();
			int y1 = start.getY();

			// end point
			int x2 = 0;
			int y2 = 0;

			// value to increment angle by each step
			double theta = Math.toRadians(-360.0 / model.getCompound().getMainSize());

			// draw the cyclo
			for (int i = 0; i < model.getCompound().getMainSize(); i++) {
				x2 = x1 + (int) (r * Math.cos(theta * i));
				y2 = y1 + (int) (r * Math.sin(theta * i));

				// add the node to the list
				nodes.add(new Node(x1, y1, 20, "" + (i + 1)));

				x1 = x2;
				y1 = y2;
			} // loop
		} //if
		
		c.setMainNodes(nodes);
	} //end setMainNodes
	
	/*
	 * Set the start positions for all the side chains
	 */
	private void setUpSides() {	
		System.out.println("setSideNodes: " + c.getMainNodes().size());
		//all the chains from the compound
		Chain[] chains = model.getCompound().getSideChains();
		
		for (Node n : c.getMainNodes()) {
			System.out.println(n.toString());
		}
		
		//loop through all the chains adding them to the list
		for (int i = 0; i < chains.length; i++) {
			if (chains[i].getSize() > 0) { //(+) size for alkyl chain
				int location = DrawingUtil.stringToNum(chains[i].getLocation());
				
				Node side = c.getMainNodes().get(location - 1);
				
				c.addSideSize(chains[i].getSize());
				
				if (!model.getCompound().getMainChain().isBenzene() || !model.getCompound().getMainChain().isCyclo()) {
					if (location % 2 == 0) {
						c.addSideDirection(DrawDirection.UP_RIGHT);
					} else {
						c.addSideDirection(DrawDirection.DOWN_RIGHT);
					} //if
					
				} else {
					c.addSideDirection(nameDir(location));
					
				} //big if
				
				c.addSideNode(side);
				
			} else { //(-) size for functional group
				
			}
		} //loop
	} //end setSideNodes
	
	/*
	 * Set the bond nodes to the canvas
	 */
	private void setUpBonds() {
		ArrayList<String> endings = model.getCompound().getMainChain().getEndings();
		
		System.out.println("setUpBonds:");
		for (String s : endings) {
			System.out.println(s +" | "+ s.substring(0,6));
			//if substring 0 -> n is alkene, bond nodes ...
			//if "" alkyne, bond nodes ...
			//location after colon
			if (s.substring(0,6).equalsIgnoreCase("alkene")) {
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); //two after the colon
				int index = Integer.parseInt(loc) - 1; //size minus one
				
				c.addBondNode(index);
				c.setBondSize(2);
						
			} else if (s.substring(0,6).equalsIgnoreCase("alkyne")) {
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); //two after the colon
				int index = Integer.parseInt(loc) - 1; //size minus one
				
				c.addBondNode(index);
				c.setBondSize(3);
			} //if
		} //loop
	} //end setBondNoddes
	
	/*
	 * Name direction from location
	 * int location - location on main chain
	 */
	private DrawDirection nameDir(int location) {
		if (location == 1 || location == 2) { //lower
			return DrawDirection.DOWN_LEFT;
			
		} else if (location == 3) { //right
			return DrawDirection.RIGHT;
			
		} else if (location == 4 || location == 5) { //top
			return DrawDirection.UP_LEFT;
			
		} else if (location == 6) { //left
			return DrawDirection.LEFT;
			
		} else { //other cases 
			return DrawDirection.LEFT;
		} //if
	} //end nameDir
	
}//end class
