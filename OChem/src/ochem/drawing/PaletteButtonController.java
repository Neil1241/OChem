package ochem.drawing;

/*
 * PaletteButtonController
 * Created by: Neil Balaskandarajah
 * Last modified: 05/16/2019
 * OBox controller for the drawing area
 */

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import ochem.drawing.DrawingUtil.ActionType;
import ochem.drawing.DrawingUtil.FuncGroup;
import ochem.organic.Chain;
import ochem.organic.Compound;
import ochem.organic.OrganicUtil;

public class PaletteButtonController implements MouseListener {
	// Attributes
	private Palette palette; // instance of the palette
	private OBox button; // instance of the button this listener is attached to
	private Canvas canvas; // canvas to update

	// Colors for each button state
	private final Color NORMAL;
	private final Color ENTERED;
	private final Color PRESSED;

	/*
	 * Create a controller with an instance of the palette, canvas and the button to
	 * control Palette palette - instance of the palette OBox button - button being
	 * controlled Canvas canvas - instance of the canvas
	 */
	public PaletteButtonController(Palette palette, OBox button, Canvas canvas) {
		this.palette = palette;
		this.button = button;
		this.canvas = canvas;

		// initialize the colors
		/*
		 * NORMAL = new Color(140,255,0); ENTERED = new Color(124,226,0); PRESSED = new
		 * Color(99,181,0);
		 */

		NORMAL = DrawingGUI.BG_COLOR;
		ENTERED = NORMAL.darker();
		PRESSED = ENTERED.darker();

		// set the background color of the button
		button.setBackgroundColor(NORMAL);
		// redraw with the new color
		button.update();
	} // end constructor

	/*
	 * Update the canvas and the palette depending on the text in the button
	 * MouseEvent m - object holding click data
	 */
	public void mouseClicked(MouseEvent m) {
		// get the text in the button
		String text = button.getText();

		// check button text to see what action to perform
		if (text.equals("Main")) { // main button was pressed
			mainAction();

		} else if (text.equals("Side")) { // side button was pressed
			sideAction();

		} else if (text.equals("Bond")) { // bond button was pressed
			bondAction();

		} else if (text.equals("Clear")) { // clear button was pressed
			clearAction();

		} else if (text.equals("Name")) { // name button was pressed
			nameAction();

			// functional groups
		} else if (text.equals("F")) { // fluorine
			fluoroAction();

		} else if (text.equals("Cl")) { // chlorine
			chloroAction();

		} else if (text.equals("Br")) { // bromine
			bromoAction();

		} else if (text.equals("I")) { // iodine
			iodoAction();

		} else if (text.equals("Al")) { // aldehyde
			aldehydeAction();

		} else if (text.equals("Ke")) { // ketone
			ketoneAction();

		} else if (text.equals("OH")) { // alcohol
			alcoholAction();

		} else if (text.equals("CA")) { // carboxylic acid
			acidAction();

		} else if (text.equals("N")) { // amine
			amineAction();

		} else if (text.equals("ON")) { // amide
			amideAction();

		} else if (text.equals("O")) { // ether
			etherAction();

		} else if (text.equals("OO")) { // ester
			esterAction();

		} // if

		// update the button and canvas
		button.update();
		canvas.updateDisplay();
	} //end mouseClicked


	// BUTTON ACTIONS//

	/*
	 * Set the main nodes to a specific color Color color - color to set the nodes
	 * to
	 */
	private void setMainNodeColor(Color color) {
		// set the colors for all the main nodes
		for (int i = 0; i < canvas.getMainNodes().size(); i++) {
			canvas.getMainNodes().get(i).setColor(color);
		} // loop
	} // end setMainNodeColor

	/*
	 * Action for when the "main" button is pressed
	 */
	private void mainAction() {
		// if there is no main chain on the screen
		if (!canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.MAIN); // set type to main
			canvas.setMainStep(1); // step forward
		} // if
	} // end mainAction

	/*
	 * Action for when the "side" button is pressed
	 */
	private void sideAction() {
		// if there is a main chain on the screen
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.SIDE); // set type to side
			canvas.setSideStep(1); // step forward
			setMainNodeColor(DrawingUtil.LIGHT_YELLOW); // set the node colors
		} // if
	} // end sideAction

	/*
	 * Action for when the "bond" button is pressed
	 */
	private void bondAction() {
		// if there is a main chain on screen
		if (canvas.getMainOnScreen() && !canvas.getMainBenzene()) { // if main chain on screen and its not a benzene
																	// ring
			palette.setSelectedType(ActionType.BOND); // set type to bond
			canvas.setBondStep(1); // step forward
			
			setMainNodeColor(DrawingUtil.LIGHT_RED); // set the node colors
		} // if
	} // end bondAction

	/*
	 * Action for when the "fluorine" button is pressed
	 */
	private void fluoroAction() {
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.FLUORINE);
			canvas.setFuncStep(1);
			canvas.addSideSize(-4);
			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end fluoroAction

	/*
	 * Action for when the "chlorine" button is pressed
	 */
	private void chloroAction() {
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.CHLORINE);
			canvas.setFuncStep(1);
			canvas.addSideSize(-5);

			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end chloroAction

	/*
	 * Action for when the "bromine" button is pressed
	 */
	private void bromoAction() {
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.BROMINE);
			canvas.setFuncStep(1);
			canvas.addSideSize(-2);

			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end bromoAction

	/*
	 * Action for when the "iodine" button is pressed
	 */
	private void iodoAction() {
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.IODINE);
			canvas.setFuncStep(1);
			canvas.addSideSize(-3);
			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end iodoAction

	/*
	 * Action for when the "aldehyde" button is pressed
	 */
	private void aldehydeAction() {
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.ALDEHYDE);
			canvas.setFuncStep(1);
			canvas.addSideSize(-6);
			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end aldehydeAction

	/*
	 * Action for when the "ketone" button is pressed
	 */
	private void ketoneAction() {
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.KETONE);
			canvas.setFuncStep(1);
			canvas.addSideSize(-6);
			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end aldehydeAction

	/*
	 * Action for when the "ketone" button is pressed
	 */
	private void alcoholAction() {
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.ALCOHOL);
			canvas.setFuncStep(1);
			canvas.addSideSize(-7);
			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end ketoneAction

	/*
	 * Action for when the "carboxylic acid" button is pressed
	 */
	private void acidAction() {
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.CARBOXYLIC_ACID);
			canvas.setFuncStep(1);
			canvas.addSideSize(-12);
			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end ketoneAction

	/*
	 * Action for when the "amine" button is pressed
	 */
	private void amineAction() {
		if (canvas.getMainOnScreen() && !canvas.getHasNO()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.AMINE);
			canvas.setFuncStep(1);
			canvas.addSideSize(-9);
			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end amineAction

	/*
	 * Action for when the "amide" button is pressed
	 */
	private void amideAction() {
		if (canvas.getMainOnScreen() && !canvas.getHasNO()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.AMIDE);
			canvas.setFuncStep(1);
			canvas.addSideSize(-10);
			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end amineAction

	/*
	 * Action for when the "carboxylic acid" button is pressed
	 */
	private void etherAction() {
		if (canvas.getMainOnScreen() && !canvas.getHasNO()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.ETHER);
			canvas.setFuncStep(1);
			canvas.addSideSize(-8);
			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end ketoneAction

	/*
	 * Action for when the "amine" button is pressed
	 */
	private void esterAction() {
		if (canvas.getMainOnScreen() && !canvas.getHasNO()) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			canvas.addFuncGroup(FuncGroup.ESTER);
			canvas.setFuncStep(1);
			canvas.addSideSize(-11);
			setMainNodeColor(DrawingUtil.LIGHT_BLUE); // set the node colors
		} // if
	} // end amineAction

	/*
	 * Action for when the "clear" button is pressed
	 */
	private void clearAction() {
		palette.setSelectedType(ActionType.CLEAR); // set the type to clear
		DrawingGUI.showMessage("Screen cleared!"); // notify on the dialog box
	} // end clearAction

	/*
	 * Action for when the "name" button is pressed
	 */
	private void nameAction() {
		// if main chain is on the screen
		if (canvas.getMainOnScreen()) {
			/*
			 * //dbg System.out.println(canvas.getCompound().toString()); for (String s :
			 * canvas.getEndings()) { System.out.println(s); }
			 */
			Compound c = this.adjustEnding();
			System.out.println(c.toString());
			System.out.println(c.getMainChain().getBond());
			System.out.println(OrganicUtil.nameFromCompound(c));
			canvas.nameStep(OrganicUtil.nameFromCompound(c));
		} // if
	} // end nameAction

	private Compound adjustEnding() {
		Compound c = canvas.getCompound();
		Chain sides[] = c.getSideChains();
		int num = 0;
		ArrayList<Integer> spots = new ArrayList<Integer>();
		ArrayList<String> locations = new ArrayList<String>();
		
		if (c.getMainChain().getBond() == 1 && c.getMainChain().getFunctionalLocation().size() >= 1) {
			c.getMainChain().clearFunctionalLocations();
		} else {
			c.getMainChain().addNumOfGroups(c.getMainChain().getFunctionalLocation().size(), 0);
			if (c.getMainChain().getBond() == 2) {
				c.getMainChain().setEnding(1);
			} else if (c.getMainChain().getBond() == 3) {
				c.getMainChain().setEnding(2);
			}//end if
		}//end if
		
		// saves the indexes of the positions with the highest priority
		for (int i = 0; i < sides.length; i++) {
			if (sides[i].getSize() < num) {
				num = sides[i].getSize();
				spots = new ArrayList<Integer>();
				spots.add(i);
			} else if (sides[i].getSize() == num) {
				spots.add(i);
			}//end if
		} // end for

		// add functional locations
		if (num <= -6) {
		for (int i : spots) {
			c.getMainChain().addFunctionalLocation(sides[i].getLocation());
			locations.add(sides[i].getLocation());
		}// end for

		c.getMainChain().addNumOfGroups(spots.size(), 1);
		// remove side chains
		sides = c.removeSideChain(num);

		switch (num) {
		case -12:
			c.getMainChain().setEnding(OrganicUtil.FUNCTIONAL_NAMES.length-3, 1);
			break;
		case -11:
			c.getMainChain().setEnding(OrganicUtil.FUNCTIONAL_NAMES.length-4, 1);
			break;
		case -10:
			c.getMainChain().setEnding(OrganicUtil.FUNCTIONAL_NAMES.length-5, 1);
			break;
		case -9:
			c.getMainChain().setEnding(OrganicUtil.FUNCTIONAL_NAMES.length-6, 1);
			break;
		case -7:
			c.getMainChain().setEnding(3, 1);
			break;
		case -6:
			int al = 0;
			for (String s: locations) {
				if (s.equals("1")||s.equals(""+c.getMainChain().getSize()))
					al++;
			}
			if (al>0) {
				c.getMainChain().clearFunctionalLocations();
				for (String s:locations) {
					if (!s.equals("1")&&!s.equals(""+c.getMainChain().getSize()))
						c.addSideChain(-6, s, false, false);
					else
						c.addFunctionalLocation(s);
					//end if
				}//end for
				c.getMainChain().setEnding(4,1);
			}else
				c.getMainChain().setEnding(5, 1);
			//end if
			break;
		default:
		}//end switch case
		}//end if
		return c;
	}// end adjust ending

	// UPDATE VISUALS//

	/*
	 * Update the color of the button to "entered" MouseEvent m - object holding
	 * click data
	 */
	public void mouseEntered(MouseEvent m) {
		button.setBackgroundColor(ENTERED);
		button.update();
	} // end mouseEntered

	/*
	 * Update the color of the button to "normal" MouseEvent m - object holding
	 * click data
	 */
	public void mouseExited(MouseEvent m) {
		button.setBackgroundColor(NORMAL);
		button.update();
	} // end mouseExited

	/*
	 * Update the color of the button to "pressed" MouseEvent m - object holding
	 * click data
	 */
	public void mousePressed(MouseEvent m) {
		button.setBackgroundColor(PRESSED);
		button.update();
	} // end mousePressed

	/*
	 * Update the color of the button to "entered" MouseEvent m - object holding
	 * click data
	 */
	public void mouseReleased(MouseEvent m) {
		button.setBackgroundColor(ENTERED);
		button.update();
	} // end mouseReleased
} // end class
