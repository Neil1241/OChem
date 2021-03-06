package ochem.naming;
/*
 * NamingGUI.java
 * Jordan Lin
 * May 24, 2019
 */

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.ArrayList;

// import packages
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ochem.OChem;
import ochem.View;
import ochem.drawing.Canvas;
import ochem.drawing.DrawingGUI;
import ochem.drawing.DrawingUtil;
import ochem.drawing.DrawingUtil.DrawDirection;
import ochem.drawing.DrawingUtil.FuncGroup;
import ochem.drawing.Node;
import ochem.organic.Chain;
import ochem.organic.Compound;
import ochem.organic.OrganicUtil;
import ochem.quiz.*;

public class NamingGUI extends JPanel {
	// declare instance variables
	private static JTextField input;
	private NamingModel model = new NamingModel();
	private JLabel test = new JLabel("Enter the Compound To Be Drawn Below");
	private Canvas c;
	private int width = (int) (0.5 * OChem.width + 2 * View.PAD);
	private int height = (int) (0.5 * OChem.height + 2 * View.PAD);
	private boolean quiz;

	private Compound compound; // local copy of the compound to be drawn

	public NamingGUI() {
		super();
		this.layoutView();
		this.model.setGUI(this);
		this.registerControllers();
		this.quiz = false;
	}

	public NamingGUI(NameInputController n) {
		super();
		this.layoutView();
		this.model.setGUI(this);
		input.addActionListener(n);
		this.quiz = true;
	}

	private void layoutView() {
		// create and set layout
		BoxLayout shell = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(shell);

		// configure the heights and widths
		input = new JTextField(20);
		input.setPreferredSize(new Dimension(OChem.width / 10, OChem.height / 20));
		input.setFont(DrawingUtil.getFileFont(DrawingUtil.OXYGEN_LOCATION));
		
		test.setFont(DrawingUtil.getFileFont(DrawingUtil.OXYGEN_LOCATION));
		test.setOpaque(true);
		test.setForeground(DrawingGUI.BG_COLOR);
		
		c = new Canvas(this.width, this.height);

		// add components to panel
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
		} else {
			// set the compound to the one from the model
			this.compound = model.getCompound();

			// set the text to what is being drawn
			if (!quiz)
				this.test.setText(OrganicUtil.nameFromCompound(this.compound));

			input.setText(null); // clear the textbox

			this.setUpCanvas(); // set the canvas up

			this.c.updateDisplay(); // update the canvas to show the compound
		} // if
	} // end update

	public void setCompound(Compound c) {
		this.model.giveCompound(c);
	}

	/*
	 * Request the focus of the input field
	 */
	public static void requestFieldFocus() {
		input.requestFocus();
	} // end getFieldFocus

	// main for testing purposes
	public static void main(String[] args) {
		JFrame f = new JFrame();
		NamingGUI g = new NamingGUI();

		f.setLocation(OChem.width / 2 - g.width / 2, OChem.height / 2 - 3 * g.height / 4);
		f.setContentPane(g);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
	}

	// SET UP CANVAS//

	/**
	 * Set up the canvas for drawing the compound
	 */
	private void setUpCanvas() {
		this.c.reset(); // reset the canvas

		this.c.setCompound(compound); // set the compound to the canvas

		// set up the drawing
		this.setUpMain(this.calcMainStart()); // set up main drawing
		this.setUpFuncGroups(); // set up functional groups
		this.setUpSides(); // set up side drawing
		this.setUpBonds(); // set up bond drawing
	} // end setUpCanvas

	/*
	 * Set the canvas main start position by centering the main chain based on its size
	 * return - starting node for the main chain
	 */
	private Node calcMainStart() {
		double angle = Math.toRadians(DrawingUtil.angleFromDirection(DrawDirection.RIGHT)[0]);

		// width of the chain
		int xOffset;
		int yOffset;

		// if a regular chain
		if (!compound.getMainChain().isBenzene() && !compound.getMainChain().isCyclo()) {
			// width of chain
			xOffset = (compound.getMainSize() - 1) * DrawingUtil.rCos(DrawingUtil.CHAIN_ARM, angle);

			// height of chain
			yOffset = -DrawingUtil.rSin(DrawingUtil.CHAIN_ARM, angle);

		} else { // benzene or cycloidal chain
			// offsets equal to the radius of the circumscribed circle
			xOffset = (int) (DrawingUtil.CYCLO_RAD);
			yOffset = (int) (DrawingUtil.CYCLO_RAD);
		} // if

		// center the chain on the screen
		int x = (width - xOffset) / 2;
		int y = (height + yOffset) / 2;

		return new Node(x, y, DrawingUtil.NODE_RAD);
	} // end setMainStart

	/*
	 * Set all the main nodes to the canvas by calculating their positions
	 * Node start - starting node for the chain
	 */
	private void setUpMain(Node start) {
		ArrayList<Node> nodes = new ArrayList<Node>(); // list of nodes

		if (!compound.getMainChain().isBenzene() && !compound.getMainChain().isCyclo()) {
			double[] angles = DrawingUtil.angleFromDirection(DrawDirection.RIGHT); // get the angles based on the direction
			int arm = DrawingUtil.CHAIN_ARM;

			int x1 = start.getX();
			int y1 = start.getY();

			for (int i = 0; i < compound.getMainSize() - 1; i++) {
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

			// add the last node
			nodes.add(new Node(x1, y1, DrawingUtil.NODE_RAD, "" + (compound.getMainSize())));

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
			double theta = Math.toRadians(-360.0 / compound.getMainSize());

			// draw the cyclo
			for (int i = 0; i < compound.getMainSize(); i++) {
				x2 = x1 + (int) (r * Math.cos(theta * i));
				y2 = y1 + (int) (r * Math.sin(theta * i));

				// add the node to the list
				nodes.add(new Node(x1, y1, 20, "" + (i + 1)));

				x1 = x2;
				y1 = y2;
			} // loop
		} // if

		c.setMainNodes(nodes);
		
		c.updateDisplay();
	} // end setMainNodes

	/*
	 * Set the start positions for all the side chains
	 */
	private void setUpSides() {
		// all the chains from the compound
		Chain[] chains = compound.getSideChains();

		// loop through all the chains adding them to the list
		for (int i = 0; i < chains.length; i++) {
			// (+) size for alkyl chain
			if (chains[i].getSize() > 0 && DrawingUtil.stringToNum(chains[i].getLocation()) > 0) {
				int location = DrawingUtil.stringToNum(chains[i].getLocation());

				Node side = c.getMainNodes().get(location - 1);

				c.addSideSize(chains[i].getSize());

				// add a direction for the side chain
				boolean isCyclo = compound.getMainChain().isBenzene() || compound.getMainChain().isCyclo();
				c.addSideDirection(namingDirection(isCyclo, compound.getMainSize(), location));

				// set the chain to cyclo/benzene
				c.addSideCyclo(chains[i].isCyclo());
				c.addSideBenzene(chains[i].isBenzene());

				// add the side node to the list
				c.addSideNode(side);

			} else if (!DrawingUtil.isNumber(chains[i].getLocation())) { // chain is on symbol

				Node side = c.getMainNodes().get(c.getMainNodes().size() - 1); // most recent node
				side.setTag(chains[i].getLocation());

				c.addSideSize(chains[i].getSize());

				// add a direction for the side chain
				boolean isCyclo = compound.getMainChain().isBenzene() || compound.getMainChain().isCyclo();
				c.addSideDirection(namingDirection(isCyclo, compound.getMainSize(), 1));
				
				// set the chain to cyclo/benzene
				c.addSideCyclo(chains[i].isCyclo());
				c.addSideBenzene(chains[i].isBenzene());

				// add the side node to the list
				c.addSideNode(side);
			} // if

		} // loop
	} // end setSideNodes

	/*
	 * Set the bond nodes to the canvas by searching through the main chain's endings
	 */
	private void setUpBonds() {
		ArrayList<String> endings = compound.getMainChain().getEndings();

		for (String s : endings) {

			if (s.substring(0, 6).equalsIgnoreCase("alkene")) {
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); // two after the colon
				int index = Integer.parseInt(loc) - 1; // size minus one

				c.addBondNode(index);
				c.addBondSize(2);

			} else if (s.substring(0, 6).equalsIgnoreCase("alkyne")) {
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); // two after the colon
				int index = Integer.parseInt(loc) - 1; // size minus one

				c.addBondNode(index);
				c.addBondSize(3);
			} // if
		} // loop
	} // end setBondNoddes

	/*
	 * Set up the functional groups on the compound
	 */
	private void setUpFuncGroups() {
		this.setUpNOFuncGroups();
		this.setUpHaloAlkanes();
		this.setUpOxygens();
	} // end setUpFuncGroups

	/*
	 * Add the haloalkanes to the canvas by searching through the chains locations
	 */
	private void setUpHaloAlkanes() {
		Chain[] sideChains = compound.getSideChains();

		for (int i = 0; i < sideChains.length; i++) {
			if (DrawingUtil.isNumber(sideChains[i].getLocation())) { // if group is on a node
				int size = sideChains[i].getSize();

				// add the group based on the index
				switch (size) {
					case -2:

						c.addSideSize(size); // add chain to attach the func group too
						c.addFuncGroup(FuncGroup.BROMINE);
						break;
					case -3:
						c.addFuncGroup(FuncGroup.IODINE);
						break;
					case -4:
						c.addFuncGroup(FuncGroup.FLUORINE);
						break;
					case -5:
						c.addFuncGroup(FuncGroup.CHLORINE);
				} // switch

				if (size < -2 && size > -5) {
					c.addSideSize(size); // add chain to attach the func group too
					// get the location of the haloalkane on the main chain
					int location = Integer.parseInt(sideChains[i].getLocation());

					// add the node
					c.addFuncNode(c.getMainNodes().get(location - 1));

					// add a direction for the chain
					boolean isCycloidal = compound.getMainChain().isCyclo() || compound.getMainChain().isBenzene();
					c.addFuncDirection(namingDirection(isCycloidal, 0, location));
				}
			} // if
		} // loop
	} // end setUpHaloAlkanes

	/*
	 * Add miscellaneous functional groups by searching through the main chain's endings
	 */
	private void setUpOxygens() {
		ArrayList<String> endings = compound.getMainChain().getEndings();

		for (String s : endings) {
			if (s.substring(0, 7).equalsIgnoreCase("alcohol")) { // alcohol
				// add the group
				c.addFuncGroup(FuncGroup.ALCOHOL);
				c.addSideSize(0); // add side chain for drawing

				// add the node
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); // two after the colon
				int index = Integer.parseInt(loc) - 1; // size minus one
				c.addFuncNode(c.getMainNodes().get(index));

				c.addFuncDirection(namingDirection(false, 0, index + 1));

			} else if (s.substring(0, 6).equalsIgnoreCase("ketone")) { // ketone
				// add the group
				c.addFuncGroup(FuncGroup.KETONE);
				c.addSideSize(0); // add side chain for drawing

				// add the node
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); // two after the colon
				int index = Integer.parseInt(loc) - 1; // size minus one
				c.addFuncNode(c.getMainNodes().get(index));

				// different direction case for ketones
				DrawDirection dir;
				c.addFuncDirection(DrawingUtil.oxyDirection(index + 1));

			} else if (s.substring(0, 8).equalsIgnoreCase("aldehyde")) { // aldehyde
				// add the group
				c.addFuncGroup(FuncGroup.ALDEHYDE);
				c.addSideSize(0); // add side chain for drawing

				// add the node
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); // two after the colon
				int index = Integer.parseInt(loc) - 1; // size minus one
				c.addFuncNode(c.getMainNodes().get(index));

				// different direction case for aldehydes
				c.addFuncDirection(DrawingUtil.oxyDirection(index + 1));

			} else if (stringContainsString(s, "carboxylic acid")) { // carboxylic acid
				// add the group
				c.addFuncGroup(FuncGroup.CARBOXYLIC_ACID);
				c.addSideSize(0); // add side chain for drawing

				// add the node
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); // two after the colon
				int index = Integer.parseInt(loc) - 1; // size minus one
				c.addFuncNode(c.getMainNodes().get(index));

				c.addFuncDirection(DrawingUtil.oxyDirection(index + 1));
			} // else if
		} // loop
	} // end setUpMiscFuncGroups

	/*
	 * Set up the nitrogen (N) and oxygen (O) functional groups by searching through the side chains
	 */
	private void setUpNOFuncGroups() {
		// loop through all the endings to add the main chain nitrogen/oxygen
		ArrayList<String> endings = compound.getMainChain().getEndings();

		for (String s : endings) {

			if (stringContainsString(s, "amine")) { // amine ending
				c.addFuncGroup(FuncGroup.AMINE); // add amine group
				c.addSideSize(-9); // position in functional groups array

				// add the node
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); // two after the colon
				int index = Integer.parseInt(loc) - 1; // size minus one
				c.addFuncNode(c.getMainNodes().get(index));

				// add the direction
				c.addFuncDirection(namingDirection(false, 0, index + 1));
				
				addNO(c.getMainNodes().get(index), namingDirection(false, 0, index + 1));

			} else if (stringContainsString(s, "amide")) { // amide ending
				c.addFuncGroup(FuncGroup.AMIDE);
				c.addSideSize(0); // position in functional groups array

				// add the node
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); // two after the colon
				int index = Integer.parseInt(loc) - 1; // size minus one
				c.addFuncNode(c.getMainNodes().get(index));

				// add the direction
				// if the location is equal to the last number on the chain, begin is false
				c.addFuncDirection(doubleDirection((index + 1) == compound.getMainSize()));
				
				addNO(c.getMainNodes().get(index), namingDirection(false, 0, index + 1));

			} else if (stringContainsString(s, "ester")) { // ester ending
				c.addFuncGroup(FuncGroup.ESTER);
				c.addSideSize(-11); // position in functional groups array

				// add the node
				String loc = Character.toString(s.charAt(s.indexOf(":") + 2)); // two after the colon
				int index = Integer.parseInt(loc) - 1; // size minus one
				c.addFuncNode(c.getMainNodes().get(index));

				// add the direction
				// if the location is equal to the last number on the chain, begin is false
				c.addFuncDirection(doubleDirection((index + 1) == compound.getMainSize()));
				
				addNO(c.getMainNodes().get(index), namingDirection(false, 0, index + 1));
			} // if
		} // loop

		// loop through the side chains to add any ethers
		Chain[] sideChains = compound.getSideChains();

		for (int i = 0; i < sideChains.length; i++) {
			if (sideChains[i].getSize() == -7) { // index for ether
				c.addFuncGroup(FuncGroup.ETHER);
				c.addSideSize(-7);

				// add the node
				int index = Integer.parseInt(sideChains[i].getLocation()) - 1; // size minus one
				c.addFuncNode(c.getMainNodes().get(index));

				c.addFuncDirection(DrawingUtil.regDirection(index + 1));
				
				addNO(c.getMainNodes().get(index), namingDirection(false, 0, index + 1));
			} // if
		} // loop

		c.updateDisplay();
	} // end setUpNOFuncGroups

	/*
	 * Add the node for the nitrogen to the main nodes list
	 * Node start - starting point for the nitrogen
	 * DrawDirection dir - direction to draw the chains in
	 */
	private void addNO(Node start, DrawDirection dir) {
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
		
		int fontR = (int) (DrawingUtil.CHAIN_ARM * 0.5);

		int textX = x2 + (int) (fontR * Math.cos(angle)) - DrawingUtil.STRING_WIDTH / 2;
		int textY = y2 + (int) (fontR * Math.sin(angle)) + (int) (DrawingUtil.ASCENT * 0.375);

		Node text = new Node(textX + DrawingUtil.STRING_WIDTH / 2, textY - (int) (DrawingUtil.ASCENT * 0.375),
				DrawingUtil.STRING_WIDTH / 2);
		
		c.getMainNodes().add(text);
	} //end addNO

	/*
	 * Checks whether one string contains another substring
	 * String s - original string to check
	 * String key - String to check for inside of String s
	 * return contains - whether the key was found in String s
	 */
	private boolean stringContainsString(String s, String key) {
		boolean contains = false; // default false

		try {
			// loop through entire string checking if substrings are equal
			for (int i = 0; i < s.length() - key.length(); i++) {
				if (s.substring(i, i + key.length()).equalsIgnoreCase(key)) {
					contains = true;
					break; // no need to continue loop, break out
				} // if
			}
		} catch (StringIndexOutOfBoundsException e) { // if key is longer than s
			e.printStackTrace();
		} // try-catch

		return contains;
	} // end stringContainsString

	/*
	 * Get the correct direction for a chain
	 * boolean isCycloidal - whether the chain is cycloidal (cyclo or benzene)
	 * int size - size of the chain
	 * int pos - position on the chain
	 */
	private DrawDirection namingDirection(boolean isCycloidal, int size, int pos) {
		if (isCycloidal) {
			return DrawingUtil.cycloDir(size, pos);
		} else {
			return DrawingUtil.regDirection(pos);
		} // if
	} // end namingDirection

	/*
	 * Direction for an amide
	 * boolean atEnd - whether the amide is at the end of the group or not
	 */
	private DrawDirection doubleDirection(boolean atEnd) {
		if (atEnd) {
			return DrawDirection.DOWN_RIGHT;
		} else {
			return DrawDirection.UP_LEFT;
		} // if
	} // end amideDirection
}// end class
