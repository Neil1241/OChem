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

public class CanvasController implements MouseListener, MouseMotionListener {
	//Attributes
	private Canvas canvas; //instance of the Canvas to be updated
	
	/*
	 * Creates a canvas controller
	 * Canvas canvas - instance of the canvas to be updated
	 */
	public CanvasController(Canvas canvas) {
		super();
		
		this.canvas = canvas;
	} //end constructor
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent m) {} //end mouseClicked

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent m) {} //end mouseEntered

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent m) {} //end mouseExited

	/*
	 * Send information to the canvas based on the mouse click
	 * MouseEvent m - object holding data about the click
	 */
	public void mousePressed(MouseEvent m) { 
		Node current = new Node(m.getX(), m.getY(), 20); //create a node from the mouse coordinates
		
		handleClick(m, current); //handle the click based on the canvas action type
		
		canvas.update(); //update the canvas
	} //end mousePressed
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent m) {} //end mouseReleased
	
	/*
	 * Handles a click action on a node
	 * MouseEvent m - the event object created on click
	 * Node current - current node
	 */
	private void handleClick(MouseEvent m, Node current) {
		//left click
		if (m.getButton() == MouseEvent.BUTTON1) { 
			//change action based on the type
			switch (canvas.getType()) {
				//main action type
				case MAIN: 
					
					canvas.setMainStep(3);
					canvas.setMainOnScreen(true);
					break;
					
				//side action type
				case SIDE:
					ArrayList<Node> nodes = canvas.getMainNodes();
					
					for (int i = 0; i < nodes.size(); i++) {
						if (isWithinBounds(current.getCenterX(), current.getCenterY(),
								nodes.get(i).getCenterX(), nodes.get(i).getCenterY(), 20)) {
							canvas.addSideNode(nodes.get(i));
						}	
					} //loop
					canvas.setSideStep(3);
					break;
					
				//clear action type
				case CLEAR:
					break;
					
				//functional group action type
				case FUNC_GROUP:
					break;
			} //switch
		} //if
	} //end handleClick
	 
	/*
	 * Check whether one point is within a radius around a target point 
	 * int x1 - current point x
	 * int y1 - current point y
	 * int x2 - goal point x
	 * int y2 - goal point y
	 * int range - distance to check within
	 */
	private boolean isWithinBounds(int x1, int y1, int x2, int y2, int range) {
		//calculate the differences in x and y
		double xDiff = Math.abs(x2 - x1);
		double yDiff = Math.abs(y2 - y1);
		
		if(xDiff < range && yDiff < range) {
			return true;
		} else {
			return false;
		} //if
	} //end isWithinBounds

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent arg0) {}

	/*
	 * Send the mouse position to the canvas
	 */
	public void mouseMoved(MouseEvent m) {
		canvas.setMouseXY(m.getX(), m.getY());

		if (canvas.getMainOnScreen()) {
			ArrayList<Node> nodes = canvas.getMainNodes();
			Node ms = new Node(m.getX(), m.getY(), 10);
			
			for (int i = 0; i < nodes.size(); i++) {
				if (isWithinBounds(ms.getCenterX(), ms.getCenterY(), nodes.get(i).getCenterX(), 
						nodes.get(i).getCenterY(), nodes.get(i).getRad())) {
//					canvas.getMainNodes().get(i).setColor(Color.BLUE);
				}	
			} //loop
		} //if
		
		canvas.update();
	} //end mouseMoved
} //end class
