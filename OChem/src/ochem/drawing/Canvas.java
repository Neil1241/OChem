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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import ochem.drawing.CanvasUtil.ActionType;
import ochem.drawing.CanvasUtil.DrawDirection;
import ochem.drawing.CanvasUtil.FuncGroup;
import ochem.organic.Chain;
import ochem.organic.Compound;
import ochem.organic.OrganicUtil;

public class Canvas extends JComponent {
	// Attributes
	private int width; // width of component
	private int height; // height of component
	private Palette palette; // instance of the palette

	private Node mouse; // for hovering effects

	// main
	private Compound compound; // compound being drawn
	private ArrayList<Node> mainNodes; // nodes for the main chain

	// side
	private ArrayList<Chain> sideChains; // side chains
	private ArrayList<Node> sideNodes; // nodes for the side chain
	private ArrayList<DrawDirection> directions; // directions for side chains

	// bond
	private ArrayList<Node> bondNodes; // bonded nodes
	private ArrayList<Integer> bondSizes; // size of bonds
	
	//func groups
	private ArrayList<FuncGroup> groups; //functional groups
	private FuncGroup ghostGroup; //current group
	private ArrayList<Node> groupNodes; //list with all the group nodes
	private ArrayList<DrawDirection> groupDirs; //all the directions for the groups

	private boolean mainOnScreen; // whether a main chain is on the screen

	private ActionType type; // type of action

	// ghost drawing (showing where the final action would be)
	private DrawDirection ghostDir; // direction of the ghost side chain
	private int ghostBondIdx; // index of the main chain node for bond drawing

	private int mainStep; // step for the "main" button
	private int sideStep; // step for the "side" button
	private int bondStep; // step for the "bond" button
	private int funcStep; //step for the "functional group" button

	private int bondNum; // global bond number counter

	/*
	 * Create a canvas with its parent's width and height
	 * int parentWidth - width of the parent panel
	 * int parentHeight - height of the parent panel
	 * Palette palette - instance of the palette
	 */
	public Canvas(int width, int height, Palette palette) {
		super();

		// set attributes
		this.width = width;
		this.height = height;
		this.palette = palette;

		// set the size of the component
		this.setPreferredSize(new Dimension(this.width, this.height));

		// instantiate the nodes list, create the mouse node
		mouse = new Node(20);

		// set the type
		type = ActionType.CLEAR;

		// set the main on screen to false
		mainOnScreen = false;

		// instantiate the compound and the chain
		compound = new Compound(0);
		sideChains = new ArrayList<Chain>();

		// instantiate all node lists
		mainNodes = new ArrayList<Node>();
		sideNodes = new ArrayList<Node>();
		bondNodes = new ArrayList<Node>();
		groupNodes = new ArrayList<Node>();

		// set the step numbers to zero when not being used, increment when needed
		mainStep = 0;
		sideStep = 0;
		bondStep = 0;
		funcStep = 0;

		// default the ghost direction to up right
		ghostDir = DrawDirection.UP_RIGHT;

		// default the ghost bond index to outside of regular range
		ghostBondIdx = -1;

		// initialize the directions and groups lists
		directions = new ArrayList<DrawDirection>();
		groups = new ArrayList<FuncGroup>();
		groupDirs = new ArrayList<DrawDirection>();

		// set the global bond counter to zero
		bondNum = 0;

		// size of all the bonds
		bondSizes = new ArrayList<Integer>();

		// add the controllers to the canvas
		registerControllers();
	} // end constructor

	/*
	 * Draw all nodes, bonds and functional groups to the screen
	 * Graphics g - AWT object responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		// cast the more capable Graphics2D onto g
		Graphics2D g2 = (Graphics2D) g;

		// stroke object for drawing
		BasicStroke bs = new BasicStroke(15.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(bs);

		// background
		g2.setBackground(CanvasUtil.BACKGROUND_COLOR);
		g2.clearRect(0, 0, width, height);

		// update the type to the palette's type
		this.type = palette.getSelectedType();
		
		//set the font type
		g2.setFont(g2.getFont().deriveFont(CanvasUtil.fontSize));

		// handle the actions for each type
		clearAction(g2);
		mainAction(g2);
		sideAction(g2);
		bondAction(g2);
		funcAction(g2);
	} // end paintComponent

	// ACTIONS//

	/*
	 * Handles the actions for the clear flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void clearAction(Graphics2D g2) {
		// only draw the node if the type is CLEAR
		if (type == ActionType.CLEAR) {
			g2.setColor(CanvasUtil.TRANS_GREY); // transparent green
			drawNode(g2, mouse);

			// set the steps to zero
			mainStep = 0;
			sideStep = 0;
			bondStep = 0;
			funcStep = 0;

			// set mainOnScreen to false
			mainOnScreen = false;

			// clear the lists of nodes
			mainNodes.clear();
			sideNodes.clear();
			bondNodes.clear();

			// clear the directions
			directions.clear();
			groupDirs.clear();
			ghostDir = DrawDirection.UP_RIGHT;

			// clear the compound and the chains
			compound = new Compound(0);
			sideChains.clear();

			// clear the list of bonds
			bondSizes.clear();

			// reset the steps
			mainStep = 0;
			sideStep = 0;
		} // if
	} // end clearAction

	/*
	 * Handles the actions for the main flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void mainAction(Graphics2D g2) {
		// change what to draw based on what the value of main step is
		switch (mainStep) {
			// do nothing step
			case 0:
				break;

			// size definition step
			case 1:
				mainOnScreen = false;
				DrawingGUI.showMessage("Enter size of main chain: (ENTER)");
				g2.setColor(CanvasUtil.TRANS_BLUE);
				drawNode(g2, mouse);
				break;

			// determine cyclo step
			case 2:
				mainOnScreen = false;
				DrawingGUI.showMessage("Cyclo? (Y/N)");
				g2.setColor(CanvasUtil.TRANS_GREY);
				drawChain(g2, mouse, DrawDirection.RIGHT, compound.getMainSize(), false);
				break;

			// location selection step
			case 3:
				mainOnScreen = false;
				DrawingGUI.showMessage("Select location for main chain: (CLICK)");
				g2.setColor(CanvasUtil.TRANS_GREY);

				if (compound.getMainChain().isCyclo()) { // cycloidal chain
					drawCyclo(g2, mouse, compound.getMainSize(), false, null);

				} else if (compound.getMainChain().isBenzene()) { // benzene ring
					drawBenzene(g2, mouse, false, null);

				} else { // regular chain
					drawChain(g2, mouse, DrawDirection.RIGHT, compound.getMainSize(), false);
				} // if

				drawSides(g2);
				break;

			// fixed on screen step
			case 4:
				DrawingGUI.clear();
				g2.setColor(CanvasUtil.CHAIN_COLOR);

				if (!mainOnScreen) { // first time called
					if (compound.getMainChain().isCyclo()) { // cycloidal chain
						mainNodes = drawCyclo(g2, mainNodes.get(0), compound.getMainSize(), false, null);

					} else if (compound.getMainChain().isBenzene()) { // benzene ring
						mainNodes = drawBenzene(g2, mainNodes.get(0), false, null);

					} else { // regular chain
						mainNodes = drawChain(g2, mainNodes.get(0), DrawDirection.RIGHT, compound.getMainSize(), false);
					} // if
					mainOnScreen = true; // tell other components and actions there is a main chain on the screen

				} else { // all other times
					if (compound.getMainChain().isCyclo()) { // cycloidal chain
						drawCyclo(g2, mainNodes.get(0), compound.getMainSize(), false, null);

					} else if (compound.getMainChain().isBenzene()) { // benzene ring
						drawBenzene(g2, mainNodes.get(0), false, null);

					} else { // regular chain
						drawChain(g2, mainNodes.get(0), DrawDirection.RIGHT, compound.getMainSize(), false);
					} // if

				} // big if

				break;

		} // switch
	} // end mainAction

	/*
	 * Handles the actions for the side flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void sideAction(Graphics2D g2) {
		// change what to draw based on what the value of main step is
		switch (sideStep) {
			// do nothing step
			case 0:
				break;

			// size definition step
			case 1:
				DrawingGUI.showMessage("Enter size of side chain: (ENTER)");
				g2.setColor(CanvasUtil.TRANS_YELLOW);
				drawNode(g2, mouse);

				drawSides(g2);
				break;

			// determine cyclo step 
			case 2:
				DrawingGUI.showMessage("Cyclo? (Y/N)");
				g2.setColor(CanvasUtil.TRANS_GREY);
				drawChain(g2, mouse, ghostDir, sideChains.get(sideChains.size() - 1).getSize() + 1, true);

				drawSides(g2);
				break;

			// location selection step
			case 3:
				DrawingGUI.showMessage("Select location for side chain: (CLICK)");
				g2.setColor(CanvasUtil.TRANS_GREY); // faint gray

				// create and draw the would-be chain
				Chain ghost = sideChains.get(sideChains.size() - 1);
				if (ghost.isCyclo()) { //cycloidal chain
					drawCyclo(g2, mouse, ghost.getSize(), true, ghostDir);
					
				} else if (ghost.isBenzene()) { //benzene ring
					drawBenzene(g2, mouse, true, ghostDir);
					
				} else { //regular chain
					drawChain(g2, mouse, ghostDir, ghost.getSize() + 1, true);
				} //if

				// draw the side chains already on the compound
				drawSides(g2);

				//start positions
				int start;
				int end;
				if (compound.getMainChain().isCyclo() || compound.getMainChain().isBenzene()) {
					start = 0;
					end = mainNodes.size();
				} else {
					start = 1;
					end = mainNodes.size() - 1;
				} //if

				// show nodes that can be clicked
				for (int i = start; i < end; i++) {
					g2.setColor(mainNodes.get(i).getColor());
					drawNode(g2, mainNodes.get(i));
				} // loop
				break;

			// fixed on screen step
			case 4:
				// draw the side chains
				drawSides(g2);
				break;
		} // switch
	} // end sideAction

	/*
	 * Handles the action for the bond flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void bondAction(Graphics2D g2) {
		switch (bondStep) {
			// do nothing step
			case 0:
				ghostBondIdx = -1; // reset the bond index
				break;

			// enter size step
			case 1:
				DrawingGUI.showMessage("Enter size of the bond (2,3)");
				g2.setColor(CanvasUtil.TRANS_RED);
				drawNode(g2, mouse);

				g2.setColor(CanvasUtil.CHAIN_COLOR);
				drawBonds(g2);
				break;

			// select location step
			case 2:
				DrawingGUI.showMessage("Select node for the bond");

				int end;
				if (compound.getMainChain().isCyclo()) {
					end = mainNodes.size();
				} else {
					end = mainNodes.size() - 1;
				} // if

				// show nodes that can be clicked
				for (int i = 0; i < end; i++) {
					g2.setColor(mainNodes.get(i).getColor());
					drawNode(g2, mainNodes.get(i));
				} // loop

				// show ghost bond
				g2.setColor(CanvasUtil.TRANS_GREY); // set ghost color

				// if a node has been selected
				if (ghostBondIdx != -1) {
					if (!bondSizes.isEmpty()) {
						int bondSize = bondSizes.get(bondSizes.size() - 1); // most recent size

						// temporary nodes for drawing ghost
						Node n1 = mainNodes.get(ghostBondIdx);
						Node n2;

						// set the tag for the temporary nodes
						if (ghostBondIdx % 2 == 0) {
							n1.setTag("1");
						} else {
							n1.setTag("-1");
						} // if

						// index of second bond
						if (ghostBondIdx + 1 == compound.getMainSize()) {
							n2 = mainNodes.get(0);
						} else {
							n2 = mainNodes.get(ghostBondIdx + 1);
						} // if

						// draw the ghost bond
						drawBond(g2, n1, n2, bondSize);
					} // inner if
				} // outer if

				g2.setColor(CanvasUtil.CHAIN_COLOR);
				drawBonds(g2);
				break;

			// fixed on screen step
			case 3:
				g2.setColor(CanvasUtil.CHAIN_COLOR);
				drawBonds(g2);
				break;
		} // switch
	} // end bondAction
	
	/*
	 * Handles the actions for the functional group flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void funcAction(Graphics2D g2) {
		// stroke object for drawing
		BasicStroke bs = new BasicStroke(15.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(bs);
		
		switch (funcStep) {
			//do nothing step
			case 0:
				break;
				
			//select location step
			case 1:
				DrawingGUI.showMessage("Select location for " + ghostGroup.toString());
				
				//draw the functional groups
				g2.setColor(CanvasUtil.CHAIN_COLOR);
				drawGroups(g2);
				
				//draw the ghost group
				g2.setColor(CanvasUtil.TRANS_GREY);
				drawFunc(g2, mouse, ghostGroup, ghostDir);
				
				//different nodes can be clicked depending on group
				switch (ghostGroup) {
					case FLUORINE:
					case CHLORINE:
					case IODINE:
					case BROMINE:
					case ALCOHOL:
						//show all nodes
						for (int i = 0; i < mainNodes.size(); i++) {
							g2.setColor(mainNodes.get(i).getColor());
							drawNode(g2, mainNodes.get(i));
						} // loop
						break;
						
					case ALDEHYDE:
					case CARBOXYLIC_ACID:
						//show first and last nodes
						g2.setColor(mainNodes.get(0).getColor());
						drawNode(g2, mainNodes.get(0));
						
						g2.setColor(mainNodes.get(mainNodes.size()-1).getColor());
						drawNode(g2, mainNodes.get(mainNodes.size()-1));
						break;
						
					case KETONE:
						//show all inner nodes (not first or last)
						for (int i = 1; i < mainNodes.size()-1; i++) {
							 g2.setColor(mainNodes.get(i).getColor());
							 drawNode(g2, mainNodes.get(i));
						} //loop
						break;
						
				} //switch
				break;
				
			//draw out step
			case 2:
				//draw the groups
				g2.setColor(CanvasUtil.CHAIN_COLOR);
				drawGroups(g2);
				break;
		} //switch
	} //end funcAction

	// DRAWING//

	/*
	 * Draw all the side chains to the screen
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void drawSides(Graphics2D g2) {
		// draw side chains
		// g2.setColor(Color.black);
		for (int i = 0; i < sideNodes.size(); i++) {
			g2.setColor(CanvasUtil.CHAIN_COLOR);
			if (sideChains.get(i).isCyclo()) { //cycloidal chain
				drawCyclo(g2, sideNodes.get(i), sideChains.get(i).getSize(), true, directions.get(i));
				
			} else if (sideChains.get(i).isBenzene()) { //benzene ring
				drawBenzene(g2, sideNodes.get(i), true, directions.get(i));
				
			} else { //regular chain
				drawChain(g2, sideNodes.get(i), directions.get(i), sideChains.get(i).getSize() + 1, true);
			} // if
		} // loop
	} // end drawSides

	/*
	 * Draw all the bonded pairs
	 * Graphics2D g2 - object responsible for drawing
	 * int idx - index of main node to draw on
	 */
	private void drawBonds(Graphics2D g2) throws NumberFormatException {
		// if there are nodes in the list
		if (!bondNodes.isEmpty()) {

			// draw lines between each pair
			for (int i = 0; i < bondNodes.size(); i += 2) {
				int bondSize = bondSizes.get(i / 2); // size of bond being drawn

				Node n1 = bondNodes.get(i); // first node
				Node n2 = bondNodes.get(i + 1); // second node

				drawBond(g2, n1, n2, bondSize);
			} // loop
		} // big iff
	} // end drawBonds

	/*
	 * Draws a bond between two nodes
	 * Graphics2D g2 - object responsible for drawing
	 * Node n1 - first node
	 * Node n2 - second node
	 * int size - size of bond
	 */
	private void drawBond(Graphics2D g2, Node n1, Node n2, int bondSize) {
		// thinner lines
		BasicStroke bs = new BasicStroke(8.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(bs);

		int flip = Integer.parseInt(n1.getTag()); // integer flip based on position on main chain

		double ang1 = CanvasUtil.angleBetweenNodes(n1, n2); // angle from first node to second
		double ang2 = CanvasUtil.angleBetweenNodes(n2, n1); // angle from second node to first
		double perp = flip * Math.PI / 2; // perpendicular offset angle (+ or - PI/2 radians, or +/- 90 degrees)

		int arm = (int) (CanvasUtil.CHAIN_ARM * 0.5); // length of bond line
		int r = (int) (CanvasUtil.CHAIN_ARM - arm) / 2; // distance between chain end and bond line end

		// bond points are translated along the chain from their starting node
		// then translated perpendicular from the line out depending on if the node # is even/odd

		// start point coordinates
		int x1 = n1.getX() + (int) (r * (Math.cos(ang1) + Math.cos(ang1 + perp)));
		int y1 = n1.getY() + (int) (r * (Math.sin(ang1) + Math.sin(ang1 + perp)));

		// end point coordinates
		int x2 = n2.getX() + (int) (r * (Math.cos(ang2) + Math.cos(ang2 - perp)));
		int y2 = n2.getY() + (int) (r * (Math.sin(ang2) + Math.sin(ang2 - perp)));

		// draw line
		g2.drawLine(x1, y1, x2, y2);

		// if a triple bond
		if (bondSize == 3) {
			// flip to the opposite side
			flip = -flip;

			// recalculate perpendicular offset
			perp = flip * Math.PI / 2;

			// recalculate start point coordinates
			x1 = n1.getX() + (int) (r * (Math.cos(ang1) + Math.cos(ang1 + perp)));
			y1 = n1.getY() + (int) (r * (Math.sin(ang1) + Math.sin(ang1 + perp))) - flip;

			// recalculate end point coordinates
			x2 = n2.getX() + (int) (r * (Math.cos(ang2) + Math.cos(ang2 - perp)));
			y2 = n2.getY() + (int) (r * (Math.sin(ang2) + Math.sin(ang2 - perp)));

			// draw opposite line
			g2.drawLine(x1, y1, x2, y2);
		} // little if
	} // end drawBond

	/*
	 * Draw a node to the screen
	 * Graphics2D g2 - object responsible for drawing
	 * Node n - node to draw
	 */
	private void drawNode(Graphics2D g2, Node n) {
		g2.fillOval(n.getCenterX(), n.getCenterY(), n.getDia(), n.getDia());
	} // end drawNode

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

		// offset length in pixels
		int r = CanvasUtil.CYCLO_RAD;

		// start point
		int x1 = start.getX();
		int y1 = start.getY();

		// end point
		int x2 = 0;
		int y2 = 0;

		// value to increment angle by each step
		double theta = Math.toRadians(-360.0 / chainSize);

		// offsets based on side and size
		int xOffset;
		int yOffset;
		double angOffset;
		double rotAngle;

		// if a side chain
		if (extend) { // calculate offsets
			angOffset = 2 * Math.toRadians(CanvasUtil.cycloAngle(dir));
			rotAngle = CanvasUtil.cycloAngOffset(chainSize);
			xOffset = (int) (r * Math.cos(angOffset));
			yOffset = (int) (r * Math.sin(angOffset));

		} else { // otherwise no offsets
			angOffset = 0;
			rotAngle = 0;
			xOffset = 0;
			yOffset = 0;
		} // if

		// translate start point
		x1 += xOffset;
		y1 += yOffset;

		// draw the cyclo
		for (int i = 0; i < chainSize; i++) {
			x2 = x1 + (int) (r * Math.cos(theta * i + angOffset + rotAngle));
			y2 = y1 + (int) (r * Math.sin(theta * i + angOffset + rotAngle));

			g2.drawLine(x1, y1, x2, y2);

			// add the node to the list
			nodes.add(new Node(x1, y1, 20, "" + (i + 1)));

			x1 = x2;
			y1 = y2;
		} // loop

		// draw the final bonds
		g2.drawLine(start.getX(), start.getY(), x2, y2);
		if (extend) {
			g2.drawLine(start.getX(), start.getY(), start.getX() + xOffset, start.getY() + yOffset);
		} // if

		// draw the #s for debugging
		/*
		 * g2.setFont(g2.getFont().deriveFont(50.0F));
		 * g2.setColor(Color.RED);
		 * for (Node n : mainNodes) {
		 * g2.drawString(n.getTag(), n.getCenterX(), n.getCenterY());
		 * }
		 * g2.setColor(Color.BLACK);
		 */

		return nodes;
	} // end drawCyclo

	/*
	 * Draws the main chain from a starting point
	 * Node start - starting point
	 * DrawDirection dir - direction to draw in
	 * int chainSize - size of chain to draw
	 * boolean side - whether the chain is on the side or not
	 * return nodes - list of nodes for that chain
	 */
	private ArrayList<Node> drawChain(Graphics2D g2, Node start, DrawDirection dir, int chainSize, boolean side) {

		// starting x and y coordinates
		int x1 = start.getX();
		int y1 = start.getY();

		double[] angles = CanvasUtil.angleFromDirection(dir); // get the angles based on the direction
		int arm = CanvasUtil.CHAIN_ARM; // length of bonds in pixels

		ArrayList<Node> nodes = new ArrayList<Node>(); // list of nodes

		int tagStart;
		if (side) {
			tagStart = 1;
		} else {
			tagStart = 0;
		}

		for (int i = 0; i < chainSize - 1; i++) {
			// current angle to draw (alternate between the 2 angles)
			double ang = angles[i % 2];

			// calculate the end points for the line
			int x2 = (int) (arm * Math.cos(Math.toRadians(ang))) + x1;
			int y2 = (int) (arm * Math.sin(Math.toRadians(ang))) + y1;

			// draw the line
			g2.drawLine(x1, y1, x2, y2);

			if (i >= tagStart) {
				if (side) {
					// set the location, radius, tag and save the node to the list
					nodes.add(new Node(x1, y1, 20, "" + (i)));
				} else {
					// set the location, radius, tag, color and save the node to the list
					nodes.add(new Node(x1, y1, 20, "" + (i + 1), CanvasUtil.LIGHT_YELLOW));
				} // small if
			} // big if

			// change the start point to this end for next loop
			x1 = x2;
			y1 = y2;
		} // loop

		// add the last node
		if (side) {
			nodes.add(new Node(x1, y1, 20, "" + (chainSize - 1)));
		} else {
			nodes.add(new Node(x1, y1, 20, "" + (chainSize), CanvasUtil.LIGHT_YELLOW));
		} // if

		return nodes;
	} // end drawChain

	/*
	 * Draw a benzene chain
	 * Graphics2D g2 - object responsible for drawing
	 * Node start - start position of the benzene
	 * boolean extend - whether chain is side or not
	 * DrawDirection dir - direction of the chain
	 */
	private ArrayList<Node> drawBenzene(Graphics2D g2, Node start, boolean extend, DrawDirection dir) {
		ArrayList<Node> nodes = drawCyclo(g2, start, 6, extend, dir);

		// create arrays for the x and y points
		int[] x = new int[nodes.size()];
		int[] y = new int[nodes.size()];

		// fill those arrays with all the x coordinates
		for (int i = 0; i < nodes.size(); i++) {
			x[i] = nodes.get(i).getX();
			y[i] = nodes.get(i).getY();
		} // loop

		// sort those values from least to greatest
		OrganicUtil.bubbleSort(x);
		OrganicUtil.bubbleSort(y);

		// find the center point
		int centerX = (x[0] + x[x.length - 1]) / 2; // avg of first and last; least and greatest
		int centerY = (y[0] + y[y.length - 1]) / 2;

		// radius of outer circle
		int outRad = (int) (CanvasUtil.CYCLO_RAD * 0.7);

		drawNode(g2, new Node(centerX, centerY, outRad));

		g2.setColor(CanvasUtil.BACKGROUND_COLOR);
		drawNode(g2, new Node(centerX, centerY, (int) (outRad * 0.85)));

		return nodes;
	} // end drawBenzene

	/*
	 * Draw all the functional groups
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void drawGroups(Graphics2D g2) {
		if (!groups.isEmpty() && !groupNodes.isEmpty()) {
			for (int i = 0; i < groupNodes.size(); i++) {
				drawFunc(g2, groupNodes.get(i), groups.get(i), groupDirs.get(i));
			} //loop
		} //if
	} //end drawGroups
	
	/*
	 * Draw a functional group
	 * Graphics2D g2 - object responsible for drawing
	 * Node start - start position of group
	 * FuncGroup group - what group to draw
	 * DrawDirectiond dir - what direction to draw group in
	 */
	private void drawFunc(Graphics2D g2, Node start, FuncGroup group, DrawDirection dir) {
		switch (group) {
			case FLUORINE:
				drawLetterFunc(g2, "F", start, dir);
				break;
				
			case CHLORINE:
				drawLetterFunc(g2, "Cl", start, dir);
				break;
				
			case BROMINE:
				drawLetterFunc(g2, "Br", start, dir);
				break;
				
			case IODINE:
				drawLetterFunc(g2, "I", start, dir);
				break;
				
			case ALDEHYDE:
			case KETONE:
				drawDoubleOxygen(g2, start, dir);
				break;
				
			case ALCOHOL:
				drawLetterFunc(g2, "OH", start, dir);
				break;
				
			case CARBOXYLIC_ACID:
				drawDoubleOxygen(g2, start, dir);
				drawLetterFunc(g2, "OH", start, CanvasUtil.incDirection(dir, 2));
				break;
		} //switch
	} //end drawFunc
	
	/*
	 * Draw a functional group that's a single letter
	 * Graphics2D g2 - object reponsible for drawing
	 * String symbol - chemical symbol of element
	 * Node start - start position of the haloalkane
	 * DrawDirection dir - direction to draw in
	 */
	private void drawLetterFunc(Graphics2D g2, String symbol, Node start, DrawDirection dir) {
		int r = (int) (CanvasUtil.CHAIN_ARM * 0.8);
		double angle = 2 * Math.toRadians(CanvasUtil.cycloAngle(dir));
		
		int x2 = start.getX() + (int) (r * Math.cos(angle));
		int y2 = start.getY() + (int) (r * Math.sin(angle));
		
		g2.drawLine(start.getX(), start.getY(), x2, y2);
		
		g2.setFont(g2.getFont().deriveFont(96.0F));
		FontMetrics fm = g2.getFontMetrics();
		int fontR = 60;
		
		int textX = x2 + (int) (fontR * Math.cos(angle)) - fm.stringWidth(symbol)/2;
		int textY = y2 + (int) (fontR * Math.sin(angle)) + (int) (fm.getAscent() * 0.375);
		
		g2.drawString(symbol, textX, textY);
	} //end drawHaloAlkane
	
	/*
	 * Draw a double bonded oxygen group
	 * Graphics2D g2 - object responsible for drawing
	 * Node start - start position of the group
	 * DrawDirection dir - direction to draw in
	 */
	private void drawDoubleOxygen(Graphics2D g2, Node start, DrawDirection dir) {
		int arm = (int) (CanvasUtil.CHAIN_ARM * 0.8); //length of bond
		int offset = (int) (CanvasUtil.CHAIN_ARM * 0.2); //length to move back from start
		int perpOut = (int) (CanvasUtil.CHAIN_ARM * 0.15); //distance to stick out
		int oExtend = (int) (CanvasUtil.CHAIN_ARM * 0.5); //how much letter sticks out from endpoints
		
		double angle = CanvasUtil.funcAngle(dir); //angle to draw with
		
		//perpendicular offsets for each line
		double aPerp = angle + Math.PI/2;
		double bPerp = angle - Math.PI/2;
		
		//first line
		//start points are offset back behind start node and out perpendicular to angle of travel
		int ax1 = start.getX() - rCos(offset, angle) + rCos(perpOut, aPerp);
		int ay1 = start.getY() - rSin(offset, angle) + rSin(perpOut, aPerp);
		
		//end points are start points translated out at the angle
		int ax2 = ax1 + rCos(arm + offset, angle);
		int ay2 = ay1 + rSin(arm + offset, angle);
		
		g2.drawLine(ax1, ay1, ax2, ay2); // draw the first line
		
		//second line
		//start points are offset back behind start node and out perpendicular to angle of travel
		int bx1 = start.getX() - rCos(offset, angle) + rCos(perpOut, bPerp);
		int by1 = start.getY() - rSin(offset, angle) + rSin(perpOut, bPerp);

		//end points are start points translated out at the angle
		int bx2 = bx1 + rCos(arm + offset, angle);
		int by2 = by1 + rSin(arm + offset, angle);
		
		g2.drawLine(bx1, by1, bx2, by2); //draw the second line
		
		//draw the oxygen
		String o = "O"; //symbol for oxygen
		FontMetrics fm = g2.getFontMetrics(); //object containing details about font
		
		//coordinates to draw String to
		int oX = start.getX() + rCos(arm + oExtend, angle) - fm.stringWidth(o)/2; //string width is width of the letter
		int oY = start.getY() + rSin(arm + oExtend, angle) + (int) (fm.getAscent() * 0.375); //ascent is how high typeface can draw
		
		g2.drawString(o, oX, oY); //draw the symbol
	} //end drawDoubleOxygen
	
	//MATH//
	/*
	 * Calculate the x offset from rotating a line 'arm' long by 'angRad' CCW
	 * int arm - length of line in pixels
	 * angRad - angle to rotate line CCW in radians
	 */
	private int rCos(int arm, double angRad) {
		return (int) (arm * Math.cos(angRad));
	} //end rCos
	
	/*
	 * Calculate the y offset from rotating a line 'arm' long by 'angRad' CCW
	 * int arm - length of line in pixels
	 * angRad - angle to rotate line CCW in radians
	 */
	private int rSin(int arm, double angRad) {
		return (int) (arm * Math.sin(angRad));
	} //end rSin
	
	// COMPONENT//

	/*
	 * Update the screen
	 */
	public void update() {
		repaint();
	} // end update

	/*
	 * Add the CanvasController to this component
	 */
	private void registerControllers() {
		CanvasController cc = new CanvasController(this);
		this.addMouseListener(cc);
		this.addMouseMotionListener(cc);
	} // end registerControllers

	/*
	 * Get the current type for the canvas
	 * return type - action type of the canvas
	 */
	public ActionType getType() {
		return type;
	} // end getType

	/*
	 * Set the x and y of the mouse
	 * int x - mouse x
	 * int y - mouse y
	 */
	public void setMouseXY(int x, int y) {
		mouse.setXY(x, y);
	} // end setMouseXY

	// MAIN//

	/*
	 * Set the size of the main chain
	 * int main - size of the main chain
	 */
	public void setMainSize(int main) {
		compound.setMainSize(main);
	} // end setMainSize

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
		} // inner if
	} // end setMainStart

	/*
	 * Get whether there is a main chain on the screen
	 * return mainOnScreen - whether the main chain is on the screen
	 */
	public boolean getMainOnScreen() {
		return mainOnScreen;
	} // end getMainOnScreen

	/*
	 * Get the nodes on the main chain
	 * return nodes - list of all the nodes of the main chain
	 */
	public ArrayList<Node> getMainNodes() {
		return mainNodes;
	} // end getMainNodes

	/*
	 * Set the nodes on the main chain to the list passed
	 * ArrayList<Node> mainNodes - list to change nodes to
	 */
	public void setMainNodes(ArrayList<Node> mainNodes) {
		this.mainNodes = mainNodes;
	} // end setMainNodes

	/*
	 * Update the type of the canvas from the palette type
	 */
	public void updateActionType() {
		type = palette.getSelectedType();

		// if the type was set to clear, wipe the main chain
		if (type == ActionType.CLEAR) {
			mainOnScreen = false;
		} // if
	} // end updateActionType

	/*
	 * Set the main chain to be cycloidal
	 * boolean val - whether main chain is cyclo or not
	 */
	public void setMainCyclo(boolean val) {
		compound.getMainChain().setCyclo(val);
	} // end setMainCyclo

	/*
	 * Get whether the main chain is a cyclo or not
	 * return - whether main chain is cycloidal or not
	 */
	public boolean getMainCyclo() {
		return compound.getMainChain().isCyclo();
	} // end getMainCyclo

	/*
	 * Set the main chain to be a benzene ring
	 * boolean val - whether main chain is benzene or not
	 */
	public void setMainBenzene(boolean val) {
		compound.getMainChain().setBenzene(val);
	} // end setMainBenzene

	/*
	 * Get whether the main chain is a benzene ring or not
	 * return - whether main chain is benzene ring or not
	 */
	public boolean getMainBenzene() {
		return compound.getMainChain().isBenzene();
	} // end getMainBenzene

	// SIDE//

	/*
	 * Add a side cyclo to the list
	 * boolean val - value to add to the side list
	 */
	public void addSideCyclo(boolean val) {
		sideChains.get(sideChains.size() - 1).setCyclo(val);
	} // end addSideCyclo
	
	/*
	 * Add a side benzene to the list
	 * boolean val - value to add to the side list
	 */
	public void addSideBenzene(boolean val) {
		sideChains.get(sideChains.size() - 1).setBenzene(val);
	} //end addSideBenzene

	/*
	 * Add a direction for a side chain
	 * DrawDirection dir - direction for latest side chain
	 */
	public void addSideDirection(DrawDirection dir) {
		directions.add(dir);
	} // end addSideDirection

	/*
	 * Add a side size to the screen
	 * int size - new size of side chain
	 */
	public void addSideSize(int size) {
		if (sideChains.isEmpty()) {
			sideChains.add(new Chain(size, ""));
		} else {
			sideChains.add(new Chain(size, ""));
		} // if
	} // end addSideSize

	/*
	 * Add side node to the side nodes list
	 * Node n - node to add to the side Nodes
	 */
	public void addSideNode(Node n) {
		sideNodes.add(n);
		Chain lastSide = sideChains.get(sideChains.size() - 1);
		lastSide.setLocation(n.getTag());
		compound.addSideChain(lastSide.getSize(), lastSide.getLocation(), lastSide.isCyclo(), lastSide.isBenzene());
		compound.addFunctionalLocation(lastSide.getLocation());
	} // end addSideNode

	/*
	 * Set the direction for the ghost to be drawn
	 * DrawDirection dir - new direction for ghost chain
	 */
	public void setGhostDirection(DrawDirection dir) {
		ghostDir = dir;
	} // end setGhostDirection
	
	// BONDS

	/*
	 * Set the bond size for main drawing
	 * int bond - size of bond
	 */
	public void setBondSize(int bond) {
		if (compound.getMainChain().getBond() < bond) {
			compound.getMainChain().setBond(bond);
		}

		bondSizes.add(bond);
		// compound.getMainChain().setEnding(bond+1); //was throwing null pointer
	} // end setBondSize

	/*
	 * Add a pair of bonded nodes to the compound
	 * int idx - position of the node in the main chain
	 */
	public void addBondNode(int idx) {
		// nodes to add to the bond nodes list
		Node start = mainNodes.get(idx); // first node of the bond
		Node end; // second and lost node for the bond

		// change second node index depending on index pos
		if (idx + 1 == compound.getMainSize()) { // if position in array exceeds the list capacity
			end = mainNodes.get(0); // roll over to zero
		} else { // regular case
			end = mainNodes.get(idx + 1); // second node is one right after
		} // if

		// change the tag based on the index
		if (idx % 2 == 0) {
			start.setTag("1"); // even, positive
		} else {
			start.setTag("-1"); // odd, negative
		} // if

		// add nodes to list
		bondNodes.add(start);
		bondNodes.add(end);

		// add the location to the main chain
		compound.getMainChain().addFunctionalLocation("" + idx);
	} // end addBondedNode

	/*
	 * Set the index of the node for ghost bonds
	 * int idx - index of the node for the bond on the main chain
	 */
	public void setGhostBondIndex(int idx) {
		ghostBondIdx = idx;
	} // end setGhostBondIndex
	
	//FUNCTIONAL GROUPS//
	/*
	 * Add a group to the list
	 * FuncGroup group - group to add to the list
	 */
	public void addFuncGroup(FuncGroup group) {
		groups.add(group);
		ghostGroup = group;
	} //end addFuncGroup
	
	/*
	 * Get the ghost group
	 * return ghostGroup - group currently being attempted to be drawn
	 */
	public FuncGroup getGhostGroup() {
		return ghostGroup;
	} //end getGhostGroup
	
	/*
	 * Add a node to the functional groups list
	 * Node n - node to add to list
	 */
	public void addFuncNode(Node n) {
		groupNodes.add(n);
	} //end addFuncNode
	
	/*
	 * Add a direction for a functional group
	 * DrawDirection dir - direction for latest side chain
	 */
	public void addGroupDirection(DrawDirection dir) {
		groupDirs.add(dir);
	} // end addGroupDirection
	
	//STEPS//

	/*
	 * Set the step for main drawing
	 * int step - step for main drawing
	 */
	public void setMainStep(int step) {
		// if outside of the range
		if (step < 0 || step > 4) {
			throw new IllegalArgumentException("Too much for me :(");
		} else {
			mainStep = step;
		} // if

		this.update();
	} // end setMainStep

	/*
	 * Get the step for main drawing
	 * return mainStep - step for the main drawing
	 */
	public int getMainStep() {
		return mainStep;
	} // end getMainStep

	/*
	 * Set the step for side drawing
	 * int step - step for side drawing
	 */
	public void setSideStep(int step) {
		if (step < 0 || step > 4) {
			throw new IllegalArgumentException("Too much for me :(");
		} else {
			sideStep = step;
		} // if

		this.update();
	} // end sideStep

	/*
	 * Get the side step for drawing
	 * return sideStep - step for drawing side chains
	 */
	public int getSideStep() {
		return sideStep;
	} // end getSideStep

	/*
	 * Set the step for side drawing
	 * int step - step for side drawing
	 */
	public void setBondStep(int step) {
		if (step < 0 || step > 3) {
			throw new IllegalArgumentException("Too much for me :(");
		} else {
			bondStep = step;
		} // if

		this.update();
	} // end sideStep

	/*
	 * Get the side step for drawing
	 * return sideStep - step for drawing side chains
	 */
	public int getBondStep() {
		return bondStep;
	} // end getSideStep

	/*
	 * Set the step for functional group drawing
	 * int step - step for group drawing
	 */
	public void setFuncStep(int step) {
		if (step < 0 || step > 3) {
			throw new IllegalArgumentException("Too much for me :(");
		} else {
			funcStep = step;
		} // if

		this.update();
	} //end setFuncStep
	
	/*
	 * Get the functional group step for drawing
	 * return funcStep - step for drawing side chains
	 */
	public int getFuncStep() {
		return funcStep;
	} // end getFuncStep
	
	// COMPOUND//

	/*
	 * Get the compound
	 * return compound - compound being created
	 */
	public Compound getCompound() {
		return compound;
	} // end getCompound

	/*
	 * Get the endings String list
	 * return - list of endings for the main chain
	 */
	public ArrayList<String> getEndings() {
		// add the number of groups
		bondNum++;
		// compound.getMainChain().addNumOfGroups(bondNum);

		// set the ending
		compound.getMainChain().setEnding(compound.getMainChain().getBond() - 1); // position in organic util array

		return compound.getMainChain().getEndings();
	} // end getEndings

	/*
	 * String representation of the String used for debugging
	 */
	public String toString() {
		return "Canvas";
	} // end toString
} // end class
