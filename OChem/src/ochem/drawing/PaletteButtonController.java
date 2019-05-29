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

import ochem.drawing.CanvasUtil.ActionType;

public class PaletteButtonController implements MouseListener {
	//Attributes
	private Palette palette; //instance of the palette
	private OBox button; //instance of the button this listener is attached to
	private Canvas canvas; //canvas to update 
	
	//Colors for each button state
	private final Color NORMAL;
	private final Color ENTERED;
	private final Color PRESSED;

	/*
	 * Create a controller with an instance of the palette, canvas and the button to control
	 * Palette palette - instance of the palette
	 * OBox button - button being controlled
	 * Canvas canvas - instance of the canvas
	 */
	public PaletteButtonController(Palette palette, OBox button, Canvas canvas) {
		this.palette = palette;
		this.button = button;
		this.canvas = canvas;
		
		//initialize the colors
		NORMAL = new Color(140,255,0);
		ENTERED = new Color(124,226,0);
		PRESSED = new Color(99,181,0);
		
		//set the background color of the button
		button.setBackgroundColor(NORMAL);
		//redraw with the new color
		button.update();
	} //end constructor

	/*
	 * Update the canvas and the palette depending on the text in the button
	 * MouseEvent m - object holding click data
	 */
	public void mouseClicked(MouseEvent m) {
		//get the text in the button 
		String text = button.getText();
		
		//check button text to see what action to perform
		if (text.equals("Main")) { //main button was pressed
			mainAction();
			
		} else if (text.equals("Side")) { //side button was pressed
			sideAction();
			
		} else if (text.equals("Bond")) { //bond button was pressed
			bondAction();
			
		} else if (text.equals("Function")) { //functional group button was pressed
			funcAction();
			
		} else if (text.equals("Clear")) { //clear button was pressed
			clearAction();
		
		} else if (text.equals("Name")) { //name button was pressed
			nameAction();
		} //big if
		
		//update the button and canvas
		button.update();
		canvas.update();
	} //end mouseClicked

	//BUTTON ACTIONS//
	
	/*
	 * Action for when the "main" button is pressed
	 */
	private void mainAction() {
		//if there is no main chain on the screen
		if (!canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.MAIN); //set type to main
			canvas.setMainStep(1); //step forward
		} //if
	} //end mainAction
	
	/*
	 * Action for when the "side" button is pressed
	 */
	private void sideAction() {
		//if there is a main chain on the screen
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.SIDE); //set type to side
			canvas.setSideStep(1); //step forward
			
			//set the colors for all the main nodes to yellow
			for (int i = 0; i < canvas.getMainNodes().size(); i++) {
				canvas.getMainNodes().get(i).setColor(CanvasUtil.LIGHT_YELLOW);
			} //loop
		} //if
	} //end sideAction
	
	/*
	 * Action for when the "bond" button is pressed
	 */
	private void bondAction() {
		//if there is a main chain on screen
		if (canvas.getMainOnScreen()) {
			palette.setSelectedType(ActionType.BOND); //set type to bond
			canvas.setBondStep(1); //step forward
			
			//set the colors for all the main nodes to red
			for (int i = 0; i < canvas.getMainNodes().size(); i++) {
				canvas.getMainNodes().get(i).setColor(CanvasUtil.LIGHT_RED);
			} //loop
		} //if
	} //end bondAction
	
	/*
	 * Action for when the "functional group" button is pressed
	 */
	private void funcAction() {
		palette.setSelectedType(ActionType.FUNC_GROUP); //set type to functional group
	} //end funcAction
	
	/*
	 * Action for when the "clear" button is pressed
	 */
	private void clearAction() {
		palette.setSelectedType(ActionType.CLEAR); //set the type to clear
		DrawingGUI.showMessage("Screen cleared!"); //notify on the dialog box
	} //end clearAction
	
	/*
	 * Action for when the "name" button is pressed
	 */
	private void nameAction() {
		//if main chain is on the screen
		if (canvas.getMainOnScreen()) {
			//dbg
			System.out.println(canvas.getCompound().toString());
			for (String s : canvas.getEndings()) {
				System.out.println(s);
			}
			System.out.println();
		} //if
	} //end nameAction
	
	//UPDATE VISUALS//
	
	/*
	 * Update the color of the button to "entered"
	 * MouseEvent m - object holding click data
	 */
	public void mouseEntered(MouseEvent m) {
		button.setBackgroundColor(ENTERED);
		button.update();
	} //end mouseEntered

	/*
	 * Update the color of the button to "normal"
	 * MouseEvent m - object holding click data
	 */
	public void mouseExited(MouseEvent m) {
		button.setBackgroundColor(NORMAL);
		button.update();
	} //end mouseExited

	/*
	 * Update the color of the button to "pressed"
	 * MouseEvent m - object holding click data
	 */
	public void mousePressed(MouseEvent m) {
		button.setBackgroundColor(PRESSED);
		button.update();
	} //end mousePressed

	/*
	 * Update the color of the button to "entered"
	 * MouseEvent m - object holding click data
	 */
	public void mouseReleased(MouseEvent m) {
		button.setBackgroundColor(ENTERED);
		button.update();
	} //end mouseReleased
} // end class
