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

		//set type based on button text
		if (text.equals("Main")) {
			if (!canvas.getMainOnScreen()) {
				palette.setSelectedType(ActionType.MAIN);
				canvas.setMainStep(1);
			} //if
			
		} else if (text.equals("Side")) {
			if (canvas.getMainOnScreen()) {
				palette.setSelectedType(ActionType.SIDE);
				canvas.setSideStep(1);
			} //if
			
		} else if (text.equals("Function")) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
			
		} else if (text.equals("Bond")) {
			if (canvas.getMainOnScreen()) {
				palette.setSelectedType(ActionType.BOND);
				canvas.setBondStep(1);
				
				for (int i = 0; i < canvas.getMainNodes().size(); i++) {
					canvas.getMainNodes().get(i).setColor(CanvasUtil.LIGHT_RED);
				}
			} //if
			
		} else if (text.equals("Clear")) {
			palette.setSelectedType(ActionType.CLEAR);
			DrawingGUI.showMessage("Screen cleared!");
		
		} else if (text.equals("Name")) {
			if (canvas.getMainOnScreen()) {
				System.out.println(canvas.getCompound().toString());
			} //if
		} //big if
		
		//update the button
		button.update();
		canvas.update();
	} //end mouseClicked

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
