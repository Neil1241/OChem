package ochem.drawing;

import java.awt.Color;

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
import java.util.HashSet;

import ochem.drawing.DrawingUtil.DrawDirection;

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
		canvas.setGhostDirection(dir); // set it to the canvas

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

				case MAIN: // main type
					mainLeft();
					break;

				case SIDE: // side type
					sideLeft();
					break;

				case BOND: // bond type
					bondLeft();
					break;

				case FUNC_GROUP: // functional group
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
				n.setTag("" + (i + 1)); // set the location for that chain
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
			dir = DrawingUtil.incDirection(dir, 1); // increment the direction
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
		} // if

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

		// different nodes can be clicked depending on group
		switch (canvas.getGhostGroup()) {
			case FLUORINE:
			case CHLORINE:
			case IODINE:
			case BROMINE:
			case ALCOHOL:
			case AMINE:
			case ETHER:
				// show all nodes
				for (int i = 0; i < mainNodes.size(); i++) {
					Node n = mainNodes.get(i);

					// if the click was on a valid node
					if (isWithinBounds(current.getCenterX(), current.getCenterY(), n.getCenterX(), n.getCenterY(),
							n.getDia())) {
						canvas.addFuncNode(n); // add that node to the functional group nodes
						canvas.addGroupDirection(dir); // save that direction into the list
						canvas.setFuncStep(2); // increment the bond step
						break; // exit loop
					} // if
				} // loop
				break;

			case ALDEHYDE:
			case CARBOXYLIC_ACID:
			case AMIDE:
			case ESTER:
				// first node
				if (isWithinBounds(current.getCenterX(), current.getCenterY(), mainNodes.get(0).getCenterX(),
						mainNodes.get(0).getCenterY(), mainNodes.get(0).getDia())) {
					canvas.addFuncNode(mainNodes.get(0)); // add the first node
					canvas.addGroupDirection(dir); // save that direction into the list
					canvas.setFuncStep(2); // increment the bond step

				//last node
				} else if (isWithinBounds(current.getCenterX(), current.getCenterY(),
						mainNodes.get(mainNodes.size() - 1).getCenterX(),
						mainNodes.get(mainNodes.size() - 1).getCenterY(),
						mainNodes.get(mainNodes.size() - 1).getDia())) {
					canvas.addFuncNode(mainNodes.get(mainNodes.size() - 1)); // add the first node
					canvas.addGroupDirection(dir); // save that direction into the list
					canvas.setFuncStep(2); // increment the bond step
				} // if

				break;

			case KETONE:
				// show all inner nodes (not first or last)
				for (int i = 1; i < mainNodes.size() - 1; i++) {
					Node n = mainNodes.get(i);

					// if the click was on a valid node
					if (isWithinBounds(current.getCenterX(), current.getCenterY(), n.getCenterX(), n.getCenterY(),
							n.getDia())) {
						canvas.addFuncNode(n); // add that node to the functional group nodes
						canvas.addGroupDirection(dir); // save that direction into the list
						canvas.setFuncStep(2); // increment the bond step
						break; // exit loop
					} // if
				} // loop
				break;

		} // switch
	} // end funcLeft

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
		current = new Node(m.getX(), m.getY(), 20);

		// if on the side drawing step
		if (canvas.getSideStep() == 3) {
			showSideNodes(m); // show clickable side nodes

			// showing bonding nodes
		} else if (canvas.getBondStep() == 2) {
			showBondNodes(m); // show clickable bond nodes

			// showing functional group nodes
		} else if (canvas.getFuncStep() == 1) {
			showFuncNodes(m);

		} // if

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

		//set of all node tags that can't be updated
		HashSet<String> noUpdate = canvas.getNoUpdate();

		// loop through all main nodes
		for (int i = 0; i < mainNodes.size(); i++) {

			if (!noUpdate.contains(mainNodes.get(i).getTag())) { //if the tag isn't in the no update set, valid
				
				// if mouse is over the node
				if (isWithinBounds(ms.getCenterX(), ms.getCenterY(), mainNodes.get(i).getCenterX(),
						mainNodes.get(i).getCenterY(), mainNodes.get(i).getRad())) {
					
					// make its color darker
					mainNodes.get(i).setColor(Color.GREEN); 
					
					// if not a cycloidal chain
					if (!canvas.getMainCyclo() && !canvas.getMainBenzene() && DrawingUtil.isNumber(mainNodes.get(i).getTag())) {
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
					mainNodes.get(i).setColor(DrawingUtil.LIGHT_YELLOW); 
				} // if
				
			} //big if
		} // loop
	} // end showSideNodes

	/*
	 * Show the bond nodes
	 * MouseEvent m - holds information about the click event
	 */
	private void showBondNodes(MouseEvent m) {

		// change node color based on mouse position
		ArrayList<Node> nodes = canvas.getMainNodes(); // list of main nodes
		Node ms = new Node(m.getX(), m.getY(), nodes.get(0).getRad()); // node for mouse
		boolean wasOver = false; // whether the mouse was over a node

		// change end node based on whether main chain is cyclo
		int end;
		if (canvas.getMainCyclo()) {
			end = nodes.size();
		} else {
			end = nodes.size() - 1;
		} // if

		// loop through selectable nodes to see if mouse is over
		for (int i = 0; i < end; i++) {
			// if mouse is over the node
			if (isWithinBounds(ms.getCenterX(), ms.getCenterY(), nodes.get(i).getCenterX(), nodes.get(i).getCenterY(),
					nodes.get(i).getRad())) {
				// make its color darker
				nodes.get(i).setColor(DrawingUtil.DARK_RED); // dark yellow

				// set the index for the ghost bond to the index of the mouse
				canvas.setGhostBondIndex(i);
				wasOver = true;

			} else { // mouse is not over the node
				// return to the default lighter color
				nodes.get(i).setColor(DrawingUtil.LIGHT_RED); // light yellow
			} // if
		} // loop

		// if the mouse wasn't over a selectable node, don't draw the bond index
		if (!wasOver) {
			canvas.setGhostBondIndex(-1);
		} // if
	} // end showBondNodes

	/*
	 * Show the functional group nodes
	 * MouseEvent m - holds information about the click
	 */
	private void showFuncNodes(MouseEvent m) {

		// change node color based on mouse position
		ArrayList<Node> nodes = canvas.getMainNodes(); // list of main nodes
		Node ms = new Node(m.getX(), m.getY(), nodes.get(0).getRad()); // node for mouse

		// different nodes can be clicked depending on group
		switch (canvas.getGhostGroup()) {
			case FLUORINE:
			case CHLORINE:
			case IODINE:
			case BROMINE:
			case ALCOHOL:
			case AMINE:
			case ETHER:

				// show all nodes
				for (int i = 0; i < nodes.size(); i++) {
					// if mouse is over the node
					if (isWithinBounds(ms.getCenterX(), ms.getCenterY(), nodes.get(i).getCenterX(),
							nodes.get(i).getCenterY(), nodes.get(i).getRad())) {
						// make its color darker
						nodes.get(i).setColor(DrawingUtil.DARK_BLUE); // dark green

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

					} else { // mouse is not over the node
						// return to the default lighter color
						nodes.get(i).setColor(DrawingUtil.LIGHT_BLUE); // light green
					} // if
				} // loop
				break;

			case ALDEHYDE:
			case CARBOXYLIC_ACID:
				// first node
				if (isWithinBounds(current.getCenterX(), current.getCenterY(), nodes.get(0).getCenterX(),
						nodes.get(0).getCenterY(), nodes.get(0).getDia())) {
					nodes.get(0).setColor(DrawingUtil.DARK_BLUE);
				} else {
					nodes.get(0).setColor(DrawingUtil.LIGHT_BLUE);
				}

				// last node
				if (isWithinBounds(current.getCenterX(), current.getCenterY(), nodes.get(nodes.size() - 1).getCenterX(),
						nodes.get(nodes.size() - 1).getCenterY(), nodes.get(nodes.size() - 1).getDia())) {
					nodes.get(nodes.size() - 1).setColor(DrawingUtil.DARK_BLUE);
				} else {
					nodes.get(nodes.size() - 1).setColor(DrawingUtil.LIGHT_BLUE);
				}

				break;
				
			case KETONE:
				// show all inner nodes (not first or last)
				for (int i = 1; i < nodes.size() - 1; i++) {
					// if mouse is over the node
					if (isWithinBounds(ms.getCenterX(), ms.getCenterY(), nodes.get(i).getCenterX(),
							nodes.get(i).getCenterY(), nodes.get(i).getRad())) {
						// make its color darker
						nodes.get(i).setColor(DrawingUtil.DARK_BLUE); // dark green

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

					} else { // mouse is not over the node
						// return to the default lighter color
						nodes.get(i).setColor(DrawingUtil.LIGHT_BLUE); // light green
					} // if
				} // loop
				break;

		} // switch
	} // end showFuncNodes

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
	public void mouseDragged(MouseEvent arg0) {

	}

	/*
	 * Required by interface
	 */
	public void mouseReleased(MouseEvent m) {

	}

	/*
	 * Required by interface
	 */
	public void mouseClicked(MouseEvent m) {

	}

	/*
	 * Required by interface
	 */
	public void mouseEntered(MouseEvent m) {

	}

	/*
	 * Required by interface
	 */
	public void mouseExited(MouseEvent m) {

	}
} // end class
