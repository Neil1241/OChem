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
	
	@Override
	public void mouseClicked(MouseEvent m) {
		
	}

	@Override
	public void mouseEntered(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	/*
	 *	Updates nodes based on when its clicked
	 */
	public void mousePressed(MouseEvent m) {		
		Node current = new Node(m.getX(), m.getY(), 20);
		
		handleClick(m, current);
		
		canvas.update();
	}
	
	public void mouseReleased(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Handles a click action on a node
	 * MouseEvent m - the event object created on click
	 * Node current - current node
	 */
	private void handleClick(MouseEvent m, Node current) {
		if (m.getButton() == MouseEvent.BUTTON1) { //left click
			canvas.addNode(current);
			DrawingGUI.reportError(canvas.getWidth() - m.getX() +" "+ (canvas.getHeight() - m.getY()));
		}
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
		double xDiff = Math.abs(x2 - x1);
		double yDiff = Math.abs(y2 - y1);
		
		if(xDiff < range && yDiff < range) {
			return true;
		} else {
			return false;
		} //if
	} //end isWithinBounds

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		canvas.setMouseXY(m.getX(), m.getY());
		canvas.update();
	}
} //end class
