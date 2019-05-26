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

import ochem.drawing.CanvasUtil.DrawDirection;

public class CanvasController implements MouseListener, MouseMotionListener {
	// Attributes
	private Canvas canvas; // instance of the Canvas to be updated
	private DrawDirection dir; // direction for a side chain

	/*
	 * Creates a canvas controller Canvas canvas - instance of the canvas to be
	 * updated
	 */
	public CanvasController(Canvas canvas) {
		super();

		this.canvas = canvas;
		dir = DrawDirection.UP_RIGHT;
	} // end constructor

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent m) {
	} // end mouseClicked

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent m) {
	} // end mouseEntered

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent m) {
	} // end mouseExited

	/*
	 * Send information to the canvas based on the mouse click MouseEvent m - object
	 * holding data about the click
	 */
	public void mousePressed(MouseEvent m) {
		Node current = new Node(m.getX(), m.getY(), 20); // create a node from the mouse coordinates

		handleClick(m, current); // handle the click based on the canvas action type

		canvas.update(); // update the canvas
	} // end mousePressed

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent m) {
	} // end mouseReleased

	/*
	 * Handles a click action on a node MouseEvent m - the event object created on
	 * click Node current - current node
	 */
	private void handleClick(MouseEvent m, Node current) {
		// left click
		if (m.getButton() == MouseEvent.BUTTON1) {
			// change action based on the type
			switch (canvas.getType()) {
			// main action type
			case MAIN:
				if (canvas.getMainStep() == 3) { //only on the cyclo step
					canvas.setMainStart(current.getX(), current.getY());
					canvas.setMainStep(4);
				}
				break;

			// side action type
			case SIDE:
				ArrayList<Node> nodes = canvas.getMainNodes();

				// check to see if the click was on a main chain node
				for (int i = 0; i < nodes.size(); i++) {
					Node n = nodes.get(i);
					
					if (isWithinBounds(current.getCenterX(), current.getCenterY(), n.getCenterX(), n.getCenterY(), n.getDia())) {
						n.setTag("" + i); //set the location for that chain
						canvas.addSideNode(n); // add that node to the side nodes list
						canvas.addSideDirection(dir); // add a direction
						canvas.setSideStep(4); // step forward
						break;
					} // if
				} // loop
				break;

			// clear action type
			case CLEAR:
				break;

			// functional group action type
			case FUNC_GROUP:
				break;
			} // switch

			// right click
		} else if (m.getButton() == MouseEvent.BUTTON3) {
			if (canvas.getSideStep() == 3) {
				incDirection();
				canvas.setGhostDirection(dir);
			}
		} // if
	} // end handleClick

	/*
	 * Check whether one point is within a radius around a target point int x1 -
	 * current point x int y1 - current point y int x2 - goal point x int y2 - goal
	 * point y int range - distance to check within
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
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent arg0) {
	}

	/*
	 * Send the mouse position to the canvas
	 */
	public void mouseMoved(MouseEvent m) {
		// send the mouse (x,y) to the canvas
		canvas.setMouseXY(m.getX(), m.getY());

		// if there is a main chain on the screen
		if (canvas.getMainOnScreen()) {
			// change node color based on mouse position
			ArrayList<Node> nodes = canvas.getMainNodes();
			Node ms = new Node(m.getX(), m.getY(), 10);

			int start;
			if (canvas.getMainCyclo()) {
				start = 0;
			} else {
				start = 1;
			}

			for (int i = start; i < nodes.size() - 1; i++) {

				// if over the node
				if (isWithinBounds(ms.getCenterX(), ms.getCenterY(), nodes.get(i).getCenterX(),
						nodes.get(i).getCenterY(), nodes.get(i).getRad())) {
					// make its color darker
					nodes.get(i).setColor(new Color(219, 194, 52)); // dark yellow

					if (!canvas.getMainCyclo()) {
						// change direction of the ghost depending on the position of the node on the
						// chain
						if (i % 2 == 0) {
							dir = DrawDirection.DOWN_RIGHT;
						} else {
							dir = DrawDirection.UP_RIGHT;
						} // if

						canvas.setGhostDirection(dir);
					}
				} else {
					// return to the defauly lighter color
					nodes.get(i).setColor(new Color(244, 217, 66)); // light yellow
				} // if
			} // loop
		} // if

		// update the display
		canvas.update();
	} // end mouseMoved

	/*
	 * Increment the draw direction one step forward
	 */
	private void incDirection() {
		int pos = dir.ordinal();
		if (pos == DrawDirection.values().length - 1) { //if last value in enum
			dir = DrawDirection.values()[0];
			
		} else { //any other
			dir = DrawDirection.values()[pos + 1]; 
		} // if
	} // end incDirection

	public String toString() {
		return "CanvasController";
	}
} // end class
