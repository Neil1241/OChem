package ochem.drawing;

/*
 * CanvasController
 * Created by: Neil Balaskandarajah
 * Last modified: 05/08/2019
 * Controller for the canvas that updates all nodes
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import ochem.drawing.CanvasUtil.DrawDirection;

public class CanvasController implements MouseListener, MouseMotionListener {
	// Attributes
	private Canvas canvas; // instance of the Canvas to be updated

	private DrawDirection dir; // direction for a side chain
	private Node current; // node representing mouse

	// button presses
	private final int LEFT_CLICK = MouseEvent.BUTTON1;
	private final int RIGHT_CLICK = MouseEvent.BUTTON3;

	/*
	 * Creates a canvas controller
	 * Canvas canvas - instance of the canvas to be updated
	 */
	public CanvasController(Canvas canvas) {
		super();

		this.canvas = canvas;

		dir = DrawDirection.UP_RIGHT; // default draw direction
		canvas.setGhostDirection(dir); //set it to the canvas
		
		current = new Node(0, 0, 20); // instantiate a node at origin
	} // end constructor

	/*
	 * Send information to the canvas based on the mouse click
	 * MouseEvent m - object holding data about the press
	 */
	public void mousePressed(MouseEvent m) {
		current = new Node(m.getX(), m.getY(), 20); // create a temporary node from the mouse coordinates

		// different action based on the click and canvas type
		// if left clicked
		if (m.getButton() == LEFT_CLICK) {
			switch (canvas.getType()) {
				
				case MAIN: //main type
					mainLeft(); 
					break;

				case SIDE: //side type
					sideLeft();
					break;

				case BOND: //bond type
					bondLeft();
					break;
					
				case FUNC_GROUP:
					funcLeft();
					break;

			} // switch

		// if right clicked
		} else if (m.getButton() == RIGHT_CLICK) {
			right();
		}

		canvas.update(); // update the canvas
	} // end mousePressed

	// ACTION METHODS//
	/*
	 * Action for when screen is left clicked with the "main" type
	 */
	private void mainLeft() {
		// if the location deciding step
		if (canvas.getMainStep() == 3) {
			canvas.setMainStart(current.getX(), current.getY()); // set the start position for the main chain
			canvas.setMainStep(4); // step forward
		} // if
	} // end mainLeft

	/*
	 * Action for when screen is left clicked with the "side" type
	 */
	private void sideLeft() {
		// create a temporary list with all the side nodes
		ArrayList<Node> sideNodes = canvas.getMainNodes();

		// loop through all nodes to see if click was on a main node
		for (int i = 0; i < sideNodes.size(); i++) {
			Node n = sideNodes.get(i); // current side node

			// if click was on a main node
			if (isWithinBounds(current.getCenterX(), current.getCenterY(), n.getCenterX(), n.getCenterY(),
					n.getDia())) {
				n.setTag("" + i); // set the location for that chain
				canvas.addSideNode(n); // add that node to the side nodes list
				canvas.addSideDirection(dir); // save that direction into the chain
				canvas.setSideStep(4); // step forward
				break; // exit loop (don't need to check other nodes)
			} // if
		} // loop
	} // end sideLeft

	/*
	 * Action for when screen is right clicked with the "side" type
	 */
	private void right() {
		// if on side location deciding step
		if (canvas.getSideStep() == 3 || canvas.getFuncStep() == 1) {
			incDirection(); // increment the direction
			canvas.setGhostDirection(dir); // set the ghost direction for the canvas
		} // if
	} // end sideRight

	/*
	 * Action for when screen is left clicked with the "bond" type
	 */
	private void bondLeft() {
		// temporary list with all the nodes
		ArrayList<Node> mainNodes = canvas.getMainNodes();

		int end;
		if (canvas.getMainCyclo()) {
			end = mainNodes.size();
		} else {
			end = mainNodes.size() - 1;
		} //if
		
		// loop through main nodes (not last one, cannot have bond on last node)
		for (int i = 0; i < end; i++) {
			Node n = mainNodes.get(i); // current node

			// if the click was on a valid node
			if (isWithinBounds(current.getCenterX(), current.getCenterY(), n.getCenterX(), n.getCenterY(),
					n.getDia())) {
				canvas.addBondNode(i); // add that node to the bonded nodes
				canvas.setBondStep(3); // increment the bond step
				break; // exit loop
			} // if
		} // loop
	} // end bondLeft
	
	/*
	 * Action for when screen is left clicked with the "functional group" type
	 */
	private void funcLeft() {
		// temporary list with all the nodes
		ArrayList<Node> mainNodes = canvas.getMainNodes();
		
		// loop through main nodes (not last one, cannot have bond on last node)
		for (int i = 0; i < mainNodes.size(); i++) {
			Node n = mainNodes.get(i); // current node

			// if the click was on a valid node
			if (isWithinBounds(current.getCenterX(), current.getCenterY(), n.getCenterX(), n.getCenterY(),
					n.getDia())) {
				canvas.addFuncNode(n); // add that node to the bonded nodes
				canvas.addGroupDirection(dir); //save that direction into the list
				canvas.setFuncStep(2); // increment the bond step
				break; // exit loop
			} // if
		} // loop
	}

	/*
	 * Increment the draw direction one step forward
	 */
	private void incDirection() {
		int pos = dir.ordinal();
		if (pos == DrawDirection.values().length - 1) { // if last value in enum
			dir = DrawDirection.values()[0]; // set to zero

		} else { // any other
			dir = DrawDirection.values()[pos + 1]; // set to one further ahead
		} // if
	} // end incDirection

	/*
	 * Check whether one point is within a radius around a target point
	 * int x1 - current point x
	 * int y1 - current point y
	 * int x2 - goal point x
	 * int y2 - goal point y
	 * int range - distance to check within
	 */
	private boolean isWithinBounds(int x1, int y1, int x2, int y2, int range) {
		// calculate the differences in x and y
		double xDiff = Math.abs(x2 - x1);
		double yDiff = Math.abs(y2 - y1);

		if (xDiff < range && yDiff < range) {
			return true;
		} else {
			return false;
		} // if
	} // end isWithinBounds

	/*
	 * Send the mouse position to the canvas
	 */
	public void mouseMoved(MouseEvent m) {
		// send the mouse (x,y) to the canvas
		canvas.setMouseXY(m.getX(), m.getY());

		// if on the side drawing step
		if (canvas.getSideStep() == 3) {
			showSideNodes(m); // show clickable side nodes

			// showing bonding nodes
		} else if (canvas.getBondStep() == 2) {
			showBondNodes(m); // show clickable bond nodes
			
			//showing functional group nodes
		} else if (canvas.getFuncStep() == 1) {
			showFuncNodes(m);
			
		} //if

		// update the display
		canvas.update();
	} // end mouseMoved

	// MOTION//

	/*
	 * Show the side nodes able to be clicked
	 * MouseEvent m - holds information about the click event
	 */
	private void showSideNodes(MouseEvent m) {
		// change node color based on mouse position
		ArrayList<Node> mainNodes = canvas.getMainNodes(); // list of nodes
		Node ms = new Node(m.getX(), m.getY(), mainNodes.get(0).getRad()); // mouse node

		int start; // start for loop
		int end; // end of loop

		// if cycloidal chain
		if (canvas.getMainCyclo() || canvas.getMainBenzene()) {
			start = 0;
			end = mainNodes.size();
			// else, regular chain
		} else {
			start = 1;
			end = mainNodes.size() - 1;
		} // if

		// loop through all main nodes
		for (int i = start; i < end; i++) {
			
			// if mouse is over the node
			if (isWithinBounds(ms.getCenterX(), ms.getCenterY(), mainNodes.get(i).getCenterX(),
					mainNodes.get(i).getCenterY(), mainNodes.get(i).getRad())) {
				// make its color darker
				mainNodes.get(i).setColor(CanvasUtil.DARK_YELLOW); // dark yellow

				// if not a cycloidal chain
				if (!canvas.getMainCyclo() && !canvas.getMainBenzene()) {
					// change direction of the ghost chain depending on node position on chain
					if (i % 2 == 0) {
						dir = DrawDirection.DOWN_RIGHT; // even, down
					} else {
						dir = DrawDirection.UP_RIGHT; // odd, up
					} // if

					canvas.setGhostDirection(dir); // set the ghost direction for the canvas
				} // if
			} else {
				// return to the default lighter color
				mainNodes.get(i).setColor(CanvasUtil.LIGHT_YELLOW); // light yellow
			} // if
		} // loop
	} // end showSideNodes

	/*
	 * Show the bond nodes
	 * MouseEvent m - holds information about the click event
	 */
	private void showBondNodes(MouseEvent m) {
		// change node color based on mouse position
		ArrayList<Node> nodes = canvas.getMainNodes(); //list of main nodes
		Node ms = new Node(m.getX(), m.getY(), nodes.get(0).getRad()); //node for mouse
		boolean wasOver = false; //whether the mouse was over a node
		
		//change end node based on whether main chain is cyclo
		int end;
		if (canvas.getMainCyclo()) {
			end = nodes.size();
		} else {
			end = nodes.size() - 1;
		} //if
		
		//loop through selectable nodes	 to see if mouse is over
		for (int i = 0; i < end; i++) {
			// if mouse is over the node
			if (isWithinBounds(ms.getCenterX(), ms.getCenterY(), nodes.get(i).getCenterX(), nodes.get(i).getCenterY(),
					nodes.get(i).getRad())) {
				// make its color darker
				nodes.get(i).setColor(CanvasUtil.DARK_RED); // dark yellow
				
				//set the index for the ghost bond to the index of the mouse
				canvas.setGhostBondIndex(i);
				wasOver = true;

			} else { //mouse is not over the node
				// return to the default lighter color
				nodes.get(i).setColor(CanvasUtil.LIGHT_RED); // light yellow
			} // if
		} //loop
		
		//if the mouse wasn't over a selectable node, don't draw the bond index
		if (!wasOver) {
			canvas.setGhostBondIndex(-1);
		} //if
	} // end showBondNodes

	/*
	 * Show the functional group nodes
	 * MouseEvent m - holds information about the click
	 */
	private void showFuncNodes(MouseEvent m) {
		// change node color based on mouse position
		ArrayList<Node> nodes = canvas.getMainNodes(); //list of main nodes
		Node ms = new Node(m.getX(), m.getY(), nodes.get(0).getRad()); //node for mouse
		
		int end = nodes.size();
		
		//loop through selectable nodes	 to see if mouse is over
		for (int i = 0; i < end; i++) {
			// if mouse is over the node
			if (isWithinBounds(ms.getCenterX(), ms.getCenterY(), nodes.get(i).getCenterX(), nodes.get(i).getCenterY(),
					nodes.get(i).getRad())) {
				// make its color darker
				nodes.get(i).setColor(CanvasUtil.DARK_RED); // dark green

				// if not a cycloidal chain
				if (!canvas.getMainCyclo() && !canvas.getMainBenzene()) {
					// change direction of the ghost chain depending on node position on chain
					if (i % 2 == 0) {
						dir = DrawDirection.DOWN_RIGHT; // even, down
					} else {
						dir = DrawDirection.UP_RIGHT; // odd, up
					} // if

					canvas.setGhostDirection(dir); // set the ghost direction for the canvas
				} // if
				
			} else { //mouse is not over the node
				// return to the default lighter color
				nodes.get(i).setColor(CanvasUtil.LIGHT_RED); // light green
			} // if
		} //loop
	} //end showFuncNodes
	
	/*
	 * String representation of the object used for debugging
	 */
	public String toString() {
		return "CanvasController";
	} // end toString

	// UNUSED INTERFACE METHODS//

	/*
	 * Required by interface
	 */
	public void mouseDragged(MouseEvent arg0) {}

	/*
	 * Required by interface
	 */
	public void mouseReleased(MouseEvent m) {}

	/*
	 * Required by interface
	 */
	public void mouseClicked(MouseEvent m) {}

	/*
	 * Required by interface
	 */
	public void mouseEntered(MouseEvent m) {}

	/*
	 * Required by interface
	 */
	public void mouseExited(MouseEvent m) {}
} // end class
