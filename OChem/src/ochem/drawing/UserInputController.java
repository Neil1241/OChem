package ochem.drawing;

/*
 * UserInputController
 * Created by: Neil Balaskandarajah
 * Last modified: 05/20/2019
 * The controller for the user interactive text field
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class UserInputController implements KeyListener {
	//Attributes
	private Canvas canvas; //instance of the canvas to communicate with
	private Palette palette; //instance of the palette to communicate with
	
	//Constants
	private final int ENTER_KEY = 10; //int value representing an enter key press
	
	/*
	 * Creates an input controller with instances of canvas and palette
	 * Canvas canvas - instance of the canvas
	 * Palette palette - instance of the palette
	 */
	public UserInputController(Canvas canvas, Palette palette) {
		this.canvas = canvas;
		this.palette = palette;
	} //end constructor

	/*
	 * Responsible for dealing with a key typed into the text field
	 * KeyEvent k - holds information about the key type
	 */
	public void keyTyped(KeyEvent k) {
		//if the enter key is pressed
		if ((int) k.getKeyChar() == ENTER_KEY ) {
			
			//if the text in the text field is a number
			if (DrawingUtil.isNumber(DrawingGUI.getUserInput())) {
				int num = Integer.parseInt(DrawingGUI.getUserInput()); //store the number
				
				//actions depend on the palette type
				switch (palette.getSelectedType()) {
					//main action
					case MAIN:
						mainAction(num);
						break;
						
					//side action
					case SIDE:
						sideAction(num);
						break;
						
					//bond action
					case BOND: 
						bondAction(num);
						break;
				} //switch
			
			//if letter was typed
			} else {				
				String in = DrawingGUI.getUserInput();
				
				switch (palette.getSelectedType()) {
					//main button
					case MAIN:
						mainAction(in);					
						break;
						
					//side button
					case SIDE:
						sideAction(in);
						break;
				} //switch
			
			} //if
		} //outer if
	} //end keyTyped
	
	//MAIN TYPE//
	
	/*
	 * Handles the main action
	 * int num - num entered by user
	 */
	private void mainAction(int num) {
		//check to see if number entered is within range for a main chain
		if (num < 2) { //too small
			DrawingGUI.reportError("Size entered too small!");
			
		} else if (num > 10) { //too big
			DrawingGUI.reportError("Size entered too big!");
			
		} else if (num > 8 || num < 3) { //too big or too small for cyclo
			canvas.setMainSize(num);
			canvas.setMainStep(3);
			DrawingGUI.clear();
			
		} else {
			//set the size of the main chain and step forward
			canvas.setMainSize(num);
			canvas.setMainStep(2);
			
			//clear the dialog box
			DrawingGUI.clear();
		} //if
	} //end mainAction
	
	/*
	 * Handles the main action
	 * String in - text entered by user
	 */
	private void mainAction(String in) {
		if (canvas.getMainStep() == 1) { //if on size deciding step
			//if text entered is B (benzene)
			if (in.equalsIgnoreCase("B")) { 
				canvas.setMainBenzene(true); //set the main chain to be a benzene
				canvas.setMainStep(3); //step forward
				DrawingGUI.clear(); //clear the dialog box
			} //if
			
		} else if (canvas.getMainStep() == 2) { //if on deciding cyclo step
			//if text entered is Y (yes)
			if (in.equalsIgnoreCase("Y")) {
				canvas.setMainCyclo(true); //set the main chain to be a cyclo
				canvas.setMainStep(3); //step forward
				DrawingGUI.clear(); //clear the dialog box
				
			//else if N (no)
			} else if (in.equalsIgnoreCase("N")) {
				canvas.setMainCyclo(false); //set the main chain to be regular
				canvas.setMainStep(3); //step forward
				DrawingGUI.clear(); //clear the dialog box
			} //if
		} //big if
	} //end mainAction
	
	//SIDE TYPE//
	
	/*
	 * Handles the side action
	 * int num - num entered by user
	 */
	private void sideAction(int num) {
		//add a size to the list of chains and step forward
		if (num < 1) { //too small to be a chain
			DrawingGUI.reportError("Size enter too small!");
			
		} else if (num < 3 || (num > 7 && num <= 10)) { //valid size, can't be cyclo
			canvas.addSideSize(num); //add number to the list
			canvas.addSideCyclo(false); //set it to false
			canvas.setSideStep(3); //skip over cyclo step
			
		} else if (num > 10) { //too big to be a side chain
			DrawingGUI.reportError("Size entered too big");		
			
		} else { //valid and can be cyclo
			canvas.addSideSize(num); //add size to list
			canvas.setSideStep(2); //step forward
		} //if
	} //end sideAction
	
	/*
	 * Handles the side action
	 * String in - key entered by user
	 */
	private void sideAction(String in) {
		//cyclo deciding step
		if (canvas.getSideStep() == 1) { //size deciding step
			//benzene ring
			if (in.equalsIgnoreCase("B")) { 
				canvas.addSideSize(6); //6 for benzene
				canvas.addSideBenzene(true); //set the main chain to be a benzene
				canvas.setSideStep(3); //skip over cyclo step
				DrawingGUI.clear(); //clear the dialog box
			} //if
			
		} else if (canvas.getSideStep() == 2) { //cyclo deciding step
			
			//if text entered is Y (yes)
			if (in.equalsIgnoreCase("Y")) {
				canvas.addSideCyclo(true); //set that side chain to be a cyclo
				canvas.setSideStep(3); //step forward
				
			} else if (in.equalsIgnoreCase("N")) {
				canvas.addSideCyclo(false); //set that side chain to be regular
				canvas.setSideStep(3); //step forward
			} //if
		} //big if
	} //end sideAction
	
	//BOND TYPE//
	
	/*
	 * Handles the bond action
	 * int num - num entered by user
	 */
	private void bondAction(int num) {
		if (num < 2) { //too small for a bond 
			DrawingGUI.reportError("Number entered too small!");
			
		} else if (num > 3) { //too big for a bond
			DrawingGUI.reportError("Number entered too big!");
			
		} else { //valid
			canvas.addBondSize(num); //set the size of the bond
			canvas.setBondStep(2); //step forward
		} //if
	} //end bondAction
	
	/*
	 * Required by interface
	 */
	public void keyPressed(KeyEvent arg0) {}

	/*
	 * Required by interface
	 */
	public void keyReleased(KeyEvent arg0) {}
} //end class
