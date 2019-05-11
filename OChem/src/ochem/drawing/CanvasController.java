package ochem.drawing;

/*
 * CanvasController
 * Created by: Neil Balaskandarajah
 * Last modified: 05/08/2019
 * Controller for the canvas that updates all nodes
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import ochem.drawing.Node.NodeType;

public class CanvasController implements MouseListener {
	//Attributes
	private Canvas canvas; //instance of the Canvas to be updated
	
	private Node last; //used for validating clicks
	
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
		Node[][] nodes = canvas.getNodes();
		
		//get mouse coordinates
		int x = m.getX();
		int y = m.getY();
		
		//loop through all nodes to check if the click was on one of them
		boolean breakOut = false; //break out of both loops is this is true
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				Node current = nodes[i][j];
				
				//if the click is on a node
				if (isWithinBounds(x, y, current.getX(), current.getY(), 75)) { //change back to node radius
					if (last == null) {
						last = current;
					}
					
					handleClick(m, current);
					
					//break out of the loop
					breakOut = true;
					last = current;
					break;
				} //if
			} //inner
			
			if (breakOut) {
				break;
			} //if
 		} //outer 
		
		canvas.update();
	}
	
	public void mouseReleased(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

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
	
	/*
	 * Handles a click action on a node
	 * MouseEvent m - the event object created on click
	 * Node current - current node
	 */
	private void handleClick(MouseEvent m, Node current) {
		if (m.getButton() == MouseEvent.BUTTON1) { //left click
			System.out.println(canvas.getSelectedType().toString());
			
			if (canvas.getSelectedType() != NodeType.BLANK) {
				if (isNodeClose(current)) {
					current.setType(canvas.getSelectedType());
					canvas.getBonds(canvas.getSelectedType()).add(current);
				}
			} else {
				System.out.println("CLEAR CLICK");
				canvas.clearNode(current);
				current.setType(NodeType.BLANK);
			} //if
			
		} //big if
		//button 3 for right click
	} //end handleClick
	
	private boolean isNodeClose(Node current) {
		//compare tag difference of current and last
		//if not +-1, +-6, report error
		//else, add
		int diff = current.getTag() - last.getTag();
		if (diff == 6 || diff == -6 || diff == 1 || diff == -1) {
			DrawingGUI.reportError("");
			return true;
		} else {
			DrawingGUI.reportError("Nodes too far!");
			return false;
		}
	}
} //end class
