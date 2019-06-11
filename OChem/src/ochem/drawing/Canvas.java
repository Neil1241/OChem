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
import java.util.HashSet;

import javax.swing.JComponent;

import ochem.drawing.DrawingUtil.ActionType;
import ochem.drawing.DrawingUtil.DrawDirection;
import ochem.drawing.DrawingUtil.FuncGroup;
import ochem.organic.Chain;
import ochem.organic.Compound;
import ochem.organic.OrganicUtil;

public class Canvas extends JComponent {
	// Attributes
	private int width; // width of component
	private int height; // height of component
	private Palette palette; // instance of the palette

	private Node mouse; // for hovering effects
	private Chain chain;

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

	// func groups
	private ArrayList<FuncGroup> groups; // functional groups
	private FuncGroup ghostGroup; // current group
	private ArrayList<Node> groupNodes; // list with all the group nodes
	private ArrayList<DrawDirection> groupDirs; // all the directions for the groups

	private boolean mainOnScreen; // whether a main chain is on the screen
	private boolean hasNO; // whether the compound has a nitrogen or oxygen

	private ActionType type; // type of action

	// ghost drawing (showing where the final action would be)
	private DrawDirection ghostDir; // direction of the ghost side chain
	private int ghostBondIdx; // index of the main chain node for bond drawing

	private int mainStep; // step for the "main" button
	private int sideStep; // step for the "side" button
	private int bondStep; // step for the "bond" button
	private int funcStep; // step for the "functional group" button

	private boolean draw; // whether the canvas is in the DrawingGUI or not

	private HashSet<String> noUpdate; // the list of Strings to not update with
	private String name;
	private int nameStep;

	/*
	 * Create a canvas with its parent's width and height int parentWidth - width of
	 * the parent panel int parentHeight - height of the parent panel Palette
	 * palette - instance of the palette
	 */
	public Canvas(int width, int height, Palette palette) {
		super();

		// set attributes
		this.width = width;
		this.height = height;
		this.palette = palette;
		this.draw = true;

		// set the size of the component
		this.setPreferredSize(new Dimension(this.width, this.height));

		// instantiate the nodes list, create the mouse node
		mouse = new Node(DrawingUtil.NODE_RAD);

		// set the type
		type = ActionType.CLEAR;

		// set the main on screen and nitrogen/oxygen check to false
		mainOnScreen = false;
		hasNO = false;

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

		// size of all the bonds
		bondSizes = new ArrayList<Integer>();

		// initialize the no update set
		noUpdate = new HashSet<String>();

		// set the last index for a nitrogen or oxygen to zero
		// lastNOIndex = 0;

		// add the controllers to the canvas
		registerControllers();
	} // end constructor

	public Canvas(int width, int height) {
		this(width, height, null);
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(this.width, this.height));
		this.draw = false;
	}

	/*
	 * Draw all nodes, bonds and functional groups to the screen Graphics g - AWT
	 * object responsible for drawing
	 */
	public void paintComponent(Graphics g) {

		// cast the more capable Graphics2D onto g
		Graphics2D g2 = (Graphics2D) g;

		// stroke object for drawing
		BasicStroke bs = new BasicStroke(DrawingUtil.CHAIN_STROKE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(bs);

		// background
		g2.setBackground(DrawingUtil.BACKGROUND_COLOR);
		g2.clearRect(0, 0, width, height);

		if (draw) {
			if (this.palette != null)
				// update the type to the palette's type
				this.type = palette.getSelectedType();

			// set the font type
			g2.setFont(g2.getFont().deriveFont(DrawingUtil.FONT_SIZE));

			// handle the actions for each type
			clearAction(g2);
			mainAction(g2);
			funcAction(g2);
			sideAction(g2);
			bondAction(g2);
			nameAction(g2);

			/*
			 * // debug by printing mouse node to screen g2.setColor(Color.GREEN);
			 * g2.setFont(g2.getFont().deriveFont(DrawingUtil.FONT_SIZE));
			 * mouse.setTag("M");
			 */
		} // for drawing GUI
	} // end paintComponent

	// ACTIONS//

	/*
	 * Handles the actions for the clear flow
	 * Graphics2D g2 - object responsible for drawing
	 */
	private void clearAction(Graphics2D g2) {

		// only draw the node if the type is CLEAR
		if (type == ActionType.CLEAR) {
			if (g2 != null) {
				g2.setColor(DrawingUtil.TRANS_GREY); // transparent green
				drawNode(g2, mouse);
			} // if

			// set the steps to zero
			mainStep = 0;
			sideStep = 0;
			bondStep = 0;
			funcStep = 0;
			nameStep = 0;

			// set compound checks to false
			mainOnScreen = false;
			hasNO = false;

			// clear the lists of nodes
			mainNodes.clear();
			sideNodes.clear();
			bondNodes.clear();
			groupNodes.clear();

			// clear the directions
			directions.clear();
			groupDirs.clear();
			ghostDir = DrawDirection.UP_RIGHT;

			// clear the compound and the chains
			compound = new Compound(0);
			sideChains.clear();

			// clear the list of bonds
			bondSizes.clear();

			// clear the list of groups
			groups.clear();

			// clear the set of tags to not update
			noUpdate.clear();

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
				g2.setColor(DrawingUtil.TRANS_BLUE);
				drawNode(g2, mouse);
				break;

			// determine cyclo step
			case 2:
				mainOnScreen = false;
				DrawingGUI.showMessage("Cyclo? (Y/N)");
				g2.setColor(DrawingUtil.TRANS_GREY);
				drawChain(g2, mouse, DrawDirection.RIGHT, compound.getMainSize(), false);
				break;

			// location selection step
			case 3:
				mainOnScreen = false;
				DrawingGUI.showMessage("Select location for main chain: (CLICK)");
				g2.setColor(DrawingUtil.TRANS_GREY);
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
				g2.setColor(DrawingUtil.CHAIN_COLOR);

				if (this.palette != null)
					DrawingGUI.clear();
				g2.setColor(DrawingUtil.CHAIN_COLOR);

				if (!mainOnScreen) { // first time called
					if (compound.getMainChain().isCyclo()) { // cycloidal chain
						mainNodes = drawCyclo(g2, mainNodes.get(0), compound.getMainSize(), false, null);

					} else if (compound.getMainChain().isBenzene()) { // benzene ring
						mainNodes = drawBenzene(g2, mainNodes.get(0), false, null);

					} else { // regular chain
						this.mainNodes = drawChain(g2, mainNodes.get(0), DrawDirection.RIGHT, compound.getMainSize(),
								false);
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
	 * @param Graphics2D g2 - object responsible for
	 * drawing
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
				g2.setColor(DrawingUtil.TRANS_YELLOW);
				drawNode(g2, mouse);

				drawSides(g2);
				break;

			// determine cyclo step
			case 2:
				DrawingGUI.showMessage("Cyclo? (Y/N)");
				g2.setColor(DrawingUtil.TRANS_GREY);
				drawChain(g2, mouse, ghostDir, sideChains.get(sideChains.size() - 1).getSize() + 1, true);

				drawSides(g2);
				break;

			// location selection step
			case 3:
				DrawingGUI.showMessage("Select location for side chain: (CLICK)");
				g2.setColor(DrawingUtil.TRANS_GREY); // faint gray

				// create and draw the would-be chain
				Chain ghost = sideChains.get(sideChains.size() - 1);
				if (ghost.isCyclo()) { // cycloidal chain
					drawCyclo(g2, mouse, ghost.getSize(), true, ghostDir);

				} else if (ghost.isBenzene()) { // benzene ring
					drawBenzene(g2, mouse, true, ghostDir);

				} else { // regular chain
					drawChain(g2, mouse, ghostDir, ghost.getSize() + 1, true);
				} // if

				// draw the side chains already on the compound
				drawSides(g2);

				noUpdate.clear();
				if (!compound.getMainChain().isBenzene() && !compound.getMainChain().isCyclo()) {
					noUpdate.add("1"); // can't add side chain to first node
					noUpdate.add(Integer.toString(compound.getMainSize())); // or last node
				} // if

				// draw all selectable nodes
				drawSelectableNodes(g2);
				break;

			// fixed on screen step
			case 4:
				// draw the side chains
				drawSides(g2);
				break;
		} // switch
	} // end sideAction

	/*
	 * Handles the action for the bond flow Graphics2D g2 - object responsible for
	 * drawing
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
				g2.setColor(DrawingUtil.TRANS_RED);
				drawNode(g2, mouse);

				g2.setColor(DrawingUtil.CHAIN_COLOR);
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
				g2.setColor(DrawingUtil.TRANS_GREY); // set ghost color

				// if a node has been selected
				if (ghostBondIdx != -1) {
					if (!bondSizes.isEmpty()) {
						int bondSize = bondSizes.get(bondSizes.size() - 1); // most recent size

						// temporary nodes for drawing ghost
						Node n1 = mainNodes.get(ghostBondIdx);
						Node n2;

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

				g2.setColor(DrawingUtil.CHAIN_COLOR);
				drawBonds(g2);
				break;

			// fixed on screen step
			case 3:
				g2.setColor(DrawingUtil.CHAIN_COLOR);
				drawBonds(g2);
				break;
		} // switch
	} // end bondAction

	/*
	 * Handles the actions for the functional group flow Graphics2D g2 - object
	 * responsible for drawing
	 */
	private void funcAction(Graphics2D g2) {

		// stroke object for drawing
		BasicStroke bs = new BasicStroke(DrawingUtil.CHAIN_STROKE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(bs);

		switch (funcStep) {
			// do nothing step
			case 0:
				break;

			// select location step
			case 1:
				DrawingGUI.showMessage("Select location for " + ghostGroup.toString());

				// draw the functional groups
				g2.setColor(DrawingUtil.CHAIN_COLOR);
				drawGroups(g2);

				// draw the ghost group
				g2.setColor(DrawingUtil.TRANS_GREY);
				drawFunc(g2, mouse, ghostGroup, ghostDir);

				// clear the set of tags to not update if detected
				noUpdate.clear();

				// different nodes can be clicked depending on group
				switch (ghostGroup) {
					case FLUORINE:
					case CHLORINE:
					case IODINE:
					case BROMINE:
					case ALCOHOL:
						// all nodes allowed
						noUpdate.clear();
						break;

					case AMINE:
					case ETHER:
						// show all non-lettered nodes
						noUpdate.add("N");
						noUpdate.add("O");
						break;

					case ALDEHYDE:
					case CARBOXYLIC_ACID:
					case AMIDE:
					case ESTER:
						// only first or last node
						for (int i = 2; i < mainNodes.size(); i++) {
							if (DrawingUtil.isNumber(mainNodes.get(i).getTag()) && i != compound.getMainSize()) { // if number
								noUpdate.add(Integer.toString(i));
							} // if
						} // loop
						break;

					case KETONE:
						// show all inner nodes (not first or last)
						noUpdate.add("1");
						noUpdate.add("" + compound.getMainSize());
						break;

				} // switch

				drawSelectableNodes(g2); // draw the nodes
				break;

			// draw out step
			case 2:
				// draw the groups
				g2.setColor(DrawingUtil.CHAIN_COLOR);
				drawGroups(g2);
				break;
		} // switch
	} // end funcAction

	private void nameAction(Graphics2D g2) {
		switch (nameStep) {
			case 0:
				break;
			default:
				DrawingGUI.showMessage(this.getName());
		}
	}

	// DRAWING//

	/*
	 * Draw all the side chains to the screen Graphics2D g2 - object responsible for
	 * drawing
	 */
	private void drawSides(Graphics2D g2) {
		// draw side chains
		// g2.setColor(Color.black);
		for (int i = 0; i < sideNodes.size(); i++) {
			g2.setColor(DrawingUtil.CHAIN_COLOR);

			int sizeIdx = i; // index for the chain to get the size from
			int cycloIdx = i; //index for the chain to get the cyclo from
			if (sideChains.get(i).getSize() < 0) { // skip over negative sizes (used for naming, not drawing)
				sizeIdx++;
				cycloIdx++;
			} // if

			if (sideChains.get(cycloIdx).isCyclo()) { // cycloidal chain
				drawCyclo(g2, sideNodes.get(i), sideChains.get(sizeIdx).getSize(), true, directions.get(i));

			} else if (sideChains.get(cycloIdx).isBenzene()) { // benzene ring
				drawBenzene(g2, sideNodes.get(i), true, directions.get(sizeIdx));

			} else { // regular chain
				drawChain(g2, sideNodes.get(i), directions.get(i), sideChains.get(sizeIdx).getSize() + 1, true);
			} // if
		} // loop
	} // end drawSides

	/*
	 * Draw all the bonded pairs Graphics2D g2 - object responsible for drawing int
	 * idx - index of main node to draw on
	 */
	private void drawBonds(Graphics2D g2) throws NumberFormatException {

		// if there are nodes in the list
		if (!bondNodes.isEmpty()) {

			// draw lines between each pair
			for (int i = 0; i < bondNodes.size(); i += 2) {
				int bondSize = bondSizes.get(i / 2); // size of bond being drawn

				drawBond(g2, bondNodes.get(i), bondNodes.get(i + 1), bondSize);
			} // loop
		} // big iff
	} // end drawBonds

	/*
	 * Draws a bond between two nodes Graphics2D g2 - object responsible for drawing
	 * Node n1 - first node Node n2 - second node int size - size of bond
	 */
	private void drawBond(Graphics2D g2, Node n1, Node n2, int bondSize) {

		// thinner lines
		BasicStroke bs = new BasicStroke(DrawingUtil.BOND_STROKE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(bs);

		// determine up or down based on whether the tag is even or odd
		int flip = Integer.parseInt(n1.getTag());
		if (flip % 2 == 0) {
			flip = -1;
		} else {
			flip = 1;
		} // if

		double ang1 = DrawingUtil.angleBetweenNodes(n1, n2); // angle from first node to second
		double ang2 = DrawingUtil.angleBetweenNodes(n2, n1); // angle from second node to first
		double perp = flip * Math.PI / 2; // perpendicular offset angle (+ or - PI/2 radians, or +/- 90 degrees)

		int arm = (int) (DrawingUtil.CHAIN_ARM * 0.5); // length of bond line
		int r = (int) (DrawingUtil.CHAIN_ARM - arm) / 2; // distance between chain end and bond line end

		// bond points are translated along the chain from their starting node
		// then translated perpendicular from the line out depending on if the node # is
		// even/odd

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
	 * Draw all the selectable nodes to the screen Graphics2D g2 - object
	 * responsible for drawing
	 */
	private void drawSelectableNodes(Graphics2D g2) {

		// draw all selectable nodes
		for (Node n : mainNodes) {
			if (!noUpdate.contains(n.getTag())) { // if the tag passes the no update test
				try {
					if (n.getColor().equals(DrawingUtil.DARK_YELLOW)) {
						DrawingUtil.printCM();
						System.out.println(n.getTag() + "\n");
					}
				} catch (NullPointerException e) {
				}

				// draw node or symbol depending on tag
				if (DrawingUtil.isNumber(n.getTag())) { // numeric tag
					g2.setColor(n.getColor());
					drawNode(g2, n);

				} else { // letter tag for nitrogen and oxygen
					g2.setColor(n.getColor());
					drawSymbol(g2, n.getTag(), n);
				} // if
			} // big if
		} // loop
	} // end drawSelectableNodes

	/*
	 * Draw a node to the screen 
	 * Graphics2D g2 - object responsible for drawing 
	 * Node n - node to draw
	 */
	private void drawNode(Graphics2D g2, Node n) {
		g2.fillOval(n.getCenterX(), n.getCenterY(), n.getDia(), n.getDia());
	} // end drawNode

	/*
	 * Draw a symbol to the screen centered around a node Graphics2D g2 - object
	 * responsible for drawing String symbol - symbol to draw Node n - node to draw
	 * at
	 */
	private void drawSymbol(Graphics2D g2, String symbol, Node n) {
		g2.setFont(g2.getFont().deriveFont((DrawingUtil.FONT_SIZE)));
		FontMetrics fm = g2.getFontMetrics();

		int textX = n.getX() - fm.stringWidth(symbol) / 2;
		int textY = n.getY() + (int) (fm.getAscent() * 0.375);

		g2.drawString(symbol, textX, textY);
	} // end drawSymbol

	/*
	 * Draw a cycloidal chain Graphics2D g2 - object responsible for drawing Node
	 * start - starting node int chainSize - size of chain boolean extend - whether
	 * the cyclo is a side chain or not DrawDirection dir - direction to draw in
	 * return nodes - nodes for that chain
	 */
	private ArrayList<Node> drawCyclo(Graphics2D g2, Node start, int chainSize, boolean extend, DrawDirection dir) {
		ArrayList<Node> nodes = new ArrayList<Node>();

		// offset length in pixels
		int r = DrawingUtil.CYCLO_RAD;
		
		// offsets based on side and size
		int xOffset;
		int yOffset;
		double angOffset;
		double rotAngle;

		// if a side chain
		if (extend) { // calculate offsets
			angOffset = 2 * Math.toRadians(DrawingUtil.cycloAngle(dir));
			rotAngle = DrawingUtil.cycloAngOffset(chainSize);
			xOffset = (int) (r * Math.cos(angOffset));
			yOffset = (int) (r * Math.sin(angOffset));

		} else { // otherwise no offsets
			angOffset = 0;
			rotAngle = 0;
			xOffset = 0;
			yOffset = 0;
		} // if
		
		//initial position
		int x0 = start.getX();
		int y0 = start.getY();
		
		if (start.getTag().equals("N") || start.getTag().equals("O")) { // if starting on a symbol
			// move up further
			x0 += DrawingUtil.rCos(DrawingUtil.CHAIN_ARM / 2, angOffset);
			y0 += DrawingUtil.rSin(DrawingUtil.CHAIN_ARM / 2, angOffset);
		} // if
		
		// start point
		int x1 = x0;
		int y1 = y0;
		
		// end point
		int x2 = 0;
		int y2 = 0;

		// value to increment angle by each step
		double theta = Math.toRadians(-360.0 / chainSize);

		// translate start point
		x1 += xOffset;
		y1 += yOffset;

		// draw the cyclo
		for (int i = 0; i < chainSize; i++) {
			x2 = x1 + (int) (r * Math.cos(theta * i + angOffset + rotAngle));
			y2 = y1 + (int) (r * Math.sin(theta * i + angOffset + rotAngle));

			g2.drawLine(x1, y1, x2, y2);

			// add the node to the list
			nodes.add(new Node(x1, y1, DrawingUtil.NODE_RAD, "" + (i + 1)));

			x1 = x2;
			y1 = y2;
		} // loop
		
		// draw the final bonds
		g2.drawLine(x0, y0, x2, y2);
		if (extend) {
			g2.drawLine(x0, y0, x0 + xOffset, y0 + yOffset);
		} // if

		// draw the #s for debugging
		g2.setFont(g2.getFont().deriveFont(DrawingUtil.FONT_SIZE / 2));
		Color oldClr = g2.getColor();
		g2.setColor(Color.RED);
		for (Node n : nodes) {
			g2.drawString(n.getTag(), n.getCenterX(), n.getCenterY());
		}
		g2.setColor(oldClr);

		return nodes;
	} // end drawCyclo

	/*
	 * Draws the main chain from a starting point Node start - starting point
	 * DrawDirection dir - direction to draw in int chainSize - size of chain to
	 * draw boolean side - whether the chain is on the side or not return nodes -
	 * list of nodes for that chain
	 */
	private ArrayList<Node> drawChain(Graphics2D g2, Node start, DrawDirection dir, int chainSize, boolean side) {
		double[] angles = DrawingUtil.angleFromDirection(dir); // get the angles based on the direction
		int arm = DrawingUtil.CHAIN_ARM; // length of bonds in pixels

		// starting x and y coordinates
		int x1 = start.getX();
		int y1 = start.getY();

		if (start.getTag().equals("N") || start.getTag().equals("O")) { // if starting on a symbol
			// move up further
			x1 += DrawingUtil.rCos(DrawingUtil.CHAIN_ARM / 2, Math.toRadians(angles[0]));
			y1 += DrawingUtil.rSin(DrawingUtil.CHAIN_ARM / 2, Math.toRadians(angles[0]));
		} // if

		ArrayList<Node> nodes = new ArrayList<Node>(); // list of nodes

		// order to add nodes in
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
					nodes.add(new Node(x1, y1, DrawingUtil.NODE_RAD, "" + (i)));
				} else {
					// set the location, radius, tag, color and save the node to the list
					nodes.add(new Node(x1, y1, DrawingUtil.NODE_RAD, "" + (i + 1), DrawingUtil.LIGHT_YELLOW));
				} // small if
			} // big if

			// change the start point to this end for next loop
			x1 = x2;
			y1 = y2;
		} // loop

		// add the last node
		if (side) {
			nodes.add(new Node(x1, y1, DrawingUtil.NODE_RAD, "" + (chainSize - 1)));
		} else {
			nodes.add(new Node(x1, y1, DrawingUtil.NODE_RAD, "" + (chainSize), DrawingUtil.LIGHT_YELLOW));
		} // if

		// draw the #s for debugging
		g2.setFont(g2.getFont().deriveFont(DrawingUtil.FONT_SIZE / 2));
		g2.setColor(Color.RED);
		for (Node n : nodes) {
			g2.drawString(n.getTag(), n.getCenterX(), n.getCenterY());
		}
		g2.setColor(DrawingUtil.CHAIN_COLOR);

		return nodes;
	} // end drawChain

	/*
	 * Draw a benzene chain Graphics2D g2 - object responsible for drawing Node
	 * start - start position of the benzene boolean extend - whether chain is side
	 * or not DrawDirection dir - direction of the chain
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
		int outRad = (int) (DrawingUtil.CYCLO_RAD * 0.7);

		drawNode(g2, new Node(centerX, centerY, outRad));

		g2.setColor(DrawingUtil.BACKGROUND_COLOR);
		drawNode(g2, new Node(centerX, centerY, (int) (outRad * 0.85)));

		return nodes;
	} // end drawBenzene

	/*
	 * Draw all the functional groups Graphics2D g2 - object responsible for drawing
	 */
	private void drawGroups(Graphics2D g2) {
		//run only if the lists aren't empty
		if (!groups.isEmpty() && !groupNodes.isEmpty() && !groupDirs.isEmpty()) { 
			for (int i = 0; i < groupNodes.size(); i++) {
				drawFunc(g2, groupNodes.get(i), 
						groups.get(i), 
						groupDirs.get(i)); 
			} // loop
		} else {
//			DrawingUtil.printCM();
//			System.out.println("GND " + groups.size() +" "+ groupNodes.size() +" "+ groupDirs.size());
		} //if
	} // end drawGroups

	/*
	 * Draw a functional group Graphics2D g2 - object responsible for drawing Node
	 * start - start position of group FuncGroup group - what group to draw
	 * DrawDirectiond dir - what direction to draw group in
	 */
	private void drawFunc(Graphics2D g2, Node start, FuncGroup group, DrawDirection dir) {

		switch (group) {
			case FLUORINE: // different symbol
				drawLetterFunc(g2, "F", start, dir);
				break;

			case CHLORINE: // different symbol
				drawLetterFunc(g2, "Cl", start, dir);
				break;

			case BROMINE: // different symbol
				drawLetterFunc(g2, "Br", start, dir);
				break;

			case IODINE: // different symbol
				drawLetterFunc(g2, "I", start, dir);
				break;

			case KETONE: 
			case ALDEHYDE:
				drawDoubleOxygen(g2, start, dir);
				break;

			case ALCOHOL: // different symbol
				drawLetterFunc(g2, "OH", start, dir);
				break;

			case CARBOXYLIC_ACID: // combination
				drawDoubleOxygen(g2, start, dir);
				drawLetterFunc(g2, "OH", start, DrawingUtil.incDirection(dir, 2));
				break;

			case AMIDE:
				drawDoubleOxygen(g2, start, DrawingUtil.incDirection(dir, 4));

			case AMINE: // save location to node
				if (!hasNO && funcStep == 2) { // first time being called when set
					mainNodes.add(drawLetterFunc(g2, "N", start, dir));
					hasNO = true;
				} else {
					drawLetterFunc(g2, "N", start, dir);
				} // if
				break;

			case ESTER:
				drawDoubleOxygen(g2, start, DrawingUtil.incDirection(dir, 4));

			case ETHER: // save location to node
				if (!hasNO && funcStep == 2) {
					mainNodes.add(drawLetterFunc(g2, "O", start, dir));
					hasNO = true;
				} else {
					drawLetterFunc(g2, "O", start, dir);
				} // if
				break;

		} // switch
	} // end drawFunc

	/*
	 * Draw a functional group that's a single letter Graphics2D g2 - object
	 * reponsible for drawing String symbol - chemical symbol of element Node start
	 * - start position of the haloalkane DrawDirection dir - direction to draw in
	 * return text - node with the center coordinate of the symbol
	 */
	private Node drawLetterFunc(Graphics2D g2, String symbol, Node start, DrawDirection dir) {
		int r = (int) (DrawingUtil.CHAIN_ARM * 0.8);
		double angle = 2 * Math.toRadians(DrawingUtil.cycloAngle(dir));

		int x1 = start.getX();
		int y1 = start.getY();

		if (start.getTag().equals("N") || start.getTag().equals("O")) { // if starting on a symbol
			// move up further
			x1 += DrawingUtil.rCos(DrawingUtil.CHAIN_ARM / 2, angle);
			y1 += DrawingUtil.rSin(DrawingUtil.CHAIN_ARM / 2, angle);
		} // if

		int x2 = x1 + (int) (r * Math.cos(angle));
		int y2 = y1 + (int) (r * Math.sin(angle));

		g2.drawLine(x1, y1, x2, y2);

		g2.setFont(g2.getFont().deriveFont(DrawingUtil.FONT_SIZE));
		FontMetrics fm = g2.getFontMetrics();
		int fontR = (int) (DrawingUtil.CHAIN_ARM * 0.5);

		int textX = x2 + (int) (fontR * Math.cos(angle)) - fm.stringWidth(symbol) / 2;
		int textY = y2 + (int) (fontR * Math.sin(angle)) + (int) (fm.getAscent() * 0.375);

		g2.drawString(symbol, textX, textY);

		Node text = new Node(textX + fm.stringWidth(symbol) / 2, textY - (int) (fm.getAscent() * 0.375),
				fm.stringWidth(symbol) / 2);
		text.setTag(symbol);
		// text.setColor(mainNodes.get(0).getColor());

		return text;
	} // end drawHaloAlkane

	/*
	 * Draw a double bonded oxygen group Graphics2D g2 - object responsible for
	 * drawing Node start - start position of the group DrawDirection dir -
	 * direction to draw in
	 */
	private void drawDoubleOxygen(Graphics2D g2, Node start, DrawDirection dir) {

		int arm = (int) (DrawingUtil.CHAIN_ARM * 0.8); // length of bond
		int offset = (int) (DrawingUtil.CHAIN_ARM * 0.085); // length to move back from start
		int perpOut = (int) (DrawingUtil.CHAIN_ARM * 0.15); // distance to stick out
		int oExtend = (int) (DrawingUtil.CHAIN_ARM * 0.5); // how much letter sticks out from endpoints
		
		double angle = DrawingUtil.funcAngle(dir); // angle to draw with
		
		DrawingUtil.printCM();
		System.out.println(ghostGroup.toString() +" "+ angle);
		System.out.println();

		// perpendicular offsets for each line
		double aPerp = angle + Math.PI / 2;
		double bPerp = angle - Math.PI / 2;

		// first line
		// start points are offset back behind start node and out perpendicular to angle
		// of travel
		int ax1 = start.getX() - DrawingUtil.rCos(offset, angle) + DrawingUtil.rCos(perpOut, aPerp);
		int ay1 = start.getY() - DrawingUtil.rSin(offset, angle) + DrawingUtil.rSin(perpOut, aPerp);

		// end points are start points translated out at the angle
		int ax2 = ax1 + DrawingUtil.rCos(arm + offset, angle);
		int ay2 = ay1 + DrawingUtil.rSin(arm + offset, angle);

		g2.drawLine(ax1, ay1, ax2, ay2); // draw the first line

		// second line
		// start points are offset back behind start node and out perpendicular to angle
		// of travel
		int bx1 = start.getX() - DrawingUtil.rCos(offset, angle) + DrawingUtil.rCos(perpOut, bPerp);
		int by1 = start.getY() - DrawingUtil.rSin(offset, angle) + DrawingUtil.rSin(perpOut, bPerp);

		// end points are start points translated out at the angle
		int bx2 = bx1 + DrawingUtil.rCos(arm + offset, angle);
		int by2 = by1 + DrawingUtil.rSin(arm + offset, angle);

		g2.drawLine(bx1, by1, bx2, by2); // draw the second line

		// draw the oxygen
		String o = "O"; // symbol for oxygen
		FontMetrics fm = g2.getFontMetrics(); // object containing details about font
		g2.setFont(g2.getFont().deriveFont(DrawingUtil.FONT_SIZE)); // set the font size

		// coordinates to draw String to
		// string width is width of the symbol
		int oX = start.getX() + DrawingUtil.rCos(arm + oExtend, angle) - fm.stringWidth(o) / 2; 
		//ascent is how high the typeface draws
		int oY = start.getY() + DrawingUtil.rSin(arm + oExtend, angle) + (int) (fm.getAscent() * 0.375); 

		g2.drawString(o, oX, oY); // draw the symbol
	} // end drawDoubleOxygen

	// COMPONENT//

	/*
	 * Update the screen
	 */
	public void updateDisplay() {
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
	 * Get the current type for the canvas return type - action type of the canvas
	 */
	public ActionType getType() {

		return type;
	} // end getType

	/*
	 * Set the x and y of the mouse int x - mouse x int y - mouse y
	 */
	public void setMouseXY(int x, int y) {

		mouse.setXY(x, y);
	} // end setMouseXY

	// MAIN//

	/*
	 * Set the size of the main chain int main - size of the main chain
	 */
	public void setMainSize(int main) {

		compound.setMainSize(main);
	} // end setMainSize

	/*
	 * Set the node for the start position of the main chain int x - starting x int
	 * y - starting y
	 */
	public void setMainStart(int x, int y) {
		if (mainNodes.isEmpty()) {
			mainNodes.add(new Node(x, y, DrawingUtil.NODE_RAD));
		} else {
			mainNodes.set(0, new Node(x, y, DrawingUtil.NODE_RAD));
		} // inner if
	} // end setMainStart

	/*
	 * Get the nodes on the main chain return nodes - list of all the nodes of the
	 * main chain
	 */
	public ArrayList<Node> getMainNodes() {
		return mainNodes;
	} // end getMainNodes

	/*
	 * Set the main nodes to the screen
	 */
	public void setMainNodes(ArrayList<Node> nodes) {
		mainNodes = nodes;
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
	 * Set the main chain to be cycloidal boolean val - whether main chain is cyclo
	 * or not
	 */
	public void setMainCyclo(boolean val) {
		compound.getMainChain().setCyclo(val);
	} // end setMainCyclo

	/*
	 * Get whether the main chain is a cyclo or not return - whether main chain is
	 * cycloidal or not
	 */
	public boolean getMainCyclo() {
		return compound.getMainChain().isCyclo();
	} // end getMainCyclo

	/*
	 * Set the main chain to be a benzene ring boolean val - whether main chain is
	 * benzene or not
	 */
	public void setMainBenzene(boolean val) {
		compound.getMainChain().setBenzene(val);
	} // end setMainBenzene

	/*
	 * Get whether the main chain is a benzene ring or not return - whether main
	 * chain is benzene ring or not
	 */
	public boolean getMainBenzene() {
		return compound.getMainChain().isBenzene();
	} // end getMainBenzene

	// SIDE//

	/*
	 * Add a side cyclo to the list boolean val - value to add to the side list
	 */
	public void addSideCyclo(boolean val) {
		sideChains.get(sideChains.size() - 1).setCyclo(val);
	} // end addSideCyclo

	/*
	 * Add a side benzene to the list boolean val - value to add to the side list
	 */
	public void addSideBenzene(boolean val) {
		sideChains.get(sideChains.size() - 1).setBenzene(val);
	} // end addSideBenzene

	/*
	 * Add a direction for a side chain DrawDirection dir - direction for latest
	 * side chain
	 */
	public void addSideDirection(DrawDirection dir) {
		directions.add(dir);
	} // end addSideDirection

	/*
	 * Add a side size to the screen int size - new size of side chain
	 */
	public void addSideSize(int size) {
		if (sideChains.isEmpty()) {
			sideChains.add(new Chain(size, ""));
		} else {
			sideChains.add(new Chain(size, ""));
		} // if
	} // end addSideSize

	/*
	 * Add side node to the side nodes list Node n - node to add to the side Nodes
	 */
	public void addSideNode(Node n) {
		sideNodes.add(n);
		addSideChain(n);
	} // end addSideNode

	public void addSideChain(Node n) {
		Chain lastSide = sideChains.get(sideChains.size() - 1);
		lastSide.setLocation(n.getTag());
		compound.addSideChain(lastSide.getSize(), lastSide.getLocation(), lastSide.isCyclo(), lastSide.isBenzene());
		if (lastSide.getSize() < -5 && lastSide.getSize() != -8)
			compound.addFunctionalLocation(lastSide.getLocation());
		else
			System.out.println("MUHAHAHAHAHAHAHA");
	}

	/*
	 * Set the direction for the ghost to be drawn DrawDirection dir - new direction
	 * for ghost chain
	 */
	public void setGhostDirection(DrawDirection dir) {

		ghostDir = dir;
	} // end setGhostDirection

	// BONDS

	/**
	 * Add a bond to the compound
	 * 
	 * @param bond
	 *            - bond size to add
	 */
	public void addBondSize(int bond) {
		if (compound.getMainChain().getBond() < bond) {
			compound.getMainChain().setBond(bond);
		}
		bondSizes.add(bond);

		if (bond == 2)
			chain = new Chain(-14, "");
		else if (bond == 3) {
			chain = new Chain(-15, "");
		}

		bondSizes.add(bond);
	} // end setBondSize

	/*
	 * Add a pair of bonded nodes to the compound int idx - position of the node in
	 * the main chain
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
		compound.getMainChain().addFunctionalLocation("" + (idx + 1));
		compound.getMainChain().addFunctionalLocation(idx + 1 + "");
		compound.getMainChain().addFunctionalLocation("" + (idx + 1));

	} // end addBondedNode

	/*
	 * Set the index of the node for ghost bonds int idx - index of the node for the
	 * bond on the main chain
	 */
	public void setGhostBondIndex(int idx) {
		ghostBondIdx = idx;
	} // end setGhostBondIndex

	// FUNCTIONAL GROUPS//
	/*
	 * Add a group to the list FuncGroup group - group to add to the list
	 */
	public void addFuncGroup(FuncGroup group) {
		groups.add(group);
		ghostGroup = group;
	} // end addFuncGroup

	/*
	 * Get the ghost group return ghostGroup - group currently being attempted to be
	 * drawn
	 */
	public FuncGroup getGhostGroup() {
		return ghostGroup;
	} // end getGhostGroup

	/*
	 * Add a node to the functional groups list Node n - node to add to list
	 */
	public void addFuncNode(Node n) {
		groupNodes.add(n);
		addSideChain(n);
	} // end addFuncNode

	/*
	 * Add a direction for a functional group DrawDirection dir - direction for
	 * latest side chain
	 */
	public void addFuncDirection(DrawDirection dir) {
		groupDirs.add(dir);
	} // end addGroupDirection

	// STEPS//

	/*
	 * Set the step for main drawing int step - step for main drawing
	 */
	public void setMainStep(int step) {
		// if outside of the range
		if (step < 0 || step > 4) {
			throw new IllegalArgumentException("Too much for me :(");
		} else {
			mainStep = step;
		} // if

		this.updateDisplay();
	} // end setMainStep

	/*
	 * Get the step for main drawing return mainStep - step for the main drawing
	 */
	public int getMainStep() {

		return mainStep;
	} // end getMainStep

	/*
	 * Set the step for side drawing int step - step for side drawing
	 */
	public void setSideStep(int step) {

		if (step < 0 || step > 4) {
			throw new IllegalArgumentException("Too much for me :(");
		} else {
			sideStep = step;
		} // if

		this.updateDisplay();
	} // end sideStep

	/*
	 * Get the side step for drawing return sideStep - step for drawing side chains
	 */
	public int getSideStep() {
		return sideStep;
	} // end getSideStep

	/*
	 * Set the step for side drawing int step - step for side drawing
	 */
	public void setBondStep(int step) {
		if (step < 0 || step > 3) {
			throw new IllegalArgumentException("Too much for me :(");
		} else {
			bondStep = step;
		} // if

		this.updateDisplay();
	} // end sideStep

	/*
	 * Get the side step for drawing return sideStep - step for drawing side chains
	 */
	public int getBondStep() {

		return bondStep;
	} // end getSideStep

	/*
	 * Set the step for functional group drawing int step - step for group drawing
	 */
	public void setFuncStep(int step) {
		if (step < 0 || step > 3) {
			throw new IllegalArgumentException("Too much for me :(");
		} else {
			funcStep = step;
		} // if

		this.updateDisplay();
	} // end setFuncStep

	/*
	 * Get the functional group step for drawing return funcStep - step for drawing
	 * side chains
	 */
	public int getFuncStep() {

		return funcStep;
	} // end getFuncStep

	public void nameStep(String name) {
		this.name = name;
		this.nameStep++;
	}

	public String getName() {
		if (this.name == null)
			return "";
		else
			return this.name;
	}

	/*
	 * Get the no updates set
	 * return noUpdate - set of Strings containing what to not update
	 */
	public HashSet<String> getNoUpdate() {
		return noUpdate;
	} // end getNoUpdate

	/*
	 * Get whether there is a main chain on the screen return mainOnScreen - whether
	 * the main chain is on the screen
	 */
	public boolean getMainOnScreen() {
		return mainOnScreen;
	} // end getMainOnScreen

	/*
	 * Get whether or not the compound has a nitrogen or an oxygen
	 * return hasNO - whether the compound has a Nitrogen/Oxygen or not
	 */
	public boolean getHasNO() {
		return hasNO;
	} // end getHasNO

	// NAMING GUI//

	/*
	 * Get the compound return compound - compound being created
	 */
	public Compound getCompound() {
		return compound;
	} // end getCompound

	/*
	 * Get the endings String list return - list of endings for the main chain
	 */
	public ArrayList<String> getEndings() {
		// set the ending
		compound.getMainChain().setEnding(compound.getMainChain().getBond() - 1); // position in organic util array

		return compound.getMainChain().getEndings();
	} // end getEndings

	/*
	 * String representation of the String used for debugging
	 */
	public String toString() {
		return "Canvas:";
	} // end toString

	/*
	 * Sets the Compound Object to the given compound object. Changes all the
	 * stepValues to be within the nodes create nodes for drawing
	 */
	public void setCompound(Compound c) {
		/// set the compound
		this.compound = c;

		// set all the steps to the final drawing steps
		this.mainStep = 4;
		this.sideStep = 4;
		this.bondStep = 3;
		this.funcStep = 2;

		// set it to draw
		this.draw = true;

		// give it a type
		this.type = ActionType.MAIN;
	}// end setCompound

	/*
	 * Reset the compounds and all associated lists and sets
	 */
	public void reset() {
		type = ActionType.CLEAR;
		clearAction(null);
		type = ActionType.MAIN;
	} // end reset

} // end class
