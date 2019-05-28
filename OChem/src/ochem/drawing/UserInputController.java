	package ochem.drawing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class UserInputController implements KeyListener {
	//Attributes
	private Canvas canvas;
	private Palette palette;
	
	//Constants
	private final int ENTER_KEY = 10;
	
	public UserInputController(Canvas canvas, Palette palette) {
		this.canvas = canvas;
		this.palette = palette;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent k) {
		if ((int) k.getKeyChar() == ENTER_KEY ) {
			if (isNumber(DrawingGUI.getUserInput())) {
				int num = Integer.parseInt(DrawingGUI.getUserInput());
				
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

	/*
	 * Checks if a String is a valid number
	 * String text - String to check for number
	 */
	private boolean isNumber(String text) {
		//if parse succeeds, string is a number and true is returned
		try {
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException n) {
			return false;
		} //try-catch
	} //end isNumber
	
	//MAIN//
	
	/*
	 * Handles the main action
	 * int num - num entered by user
	 */
	private void mainAction(int num) {
		//check to see if number entered is within range
		if (num < 2) { //too small
			DrawingGUI.reportError("Size entered too small!");
			
		} else if (num > 10) { //too big
			DrawingGUI.reportError("Size entered too big!");
			
		} else if (num > 8 || num < 3) { //too big or too small for cyclo
			canvas.setMainSize(num);
			canvas.setMainStep(3);
			DrawingGUI.clear();
			
		} else {
			//set the size and step forward
			canvas.setMainSize(num);
			canvas.setMainStep(2);
				
			DrawingGUI.clear();
		} //if
	} //end bondAction
	
	/*
	 * Handles the main action
	 * String key - key entered by user
	 */
	private void mainAction(String in) {
		if (canvas.getMainStep() == 2) {
			if (in.equalsIgnoreCase("Y")) {
				canvas.setMainCyclo(true);
				canvas.setMainStep(3);
				DrawingGUI.clear();
			} else if (in.equalsIgnoreCase("N")) {
				canvas.setMainCyclo(false);
				canvas.setMainStep(3);
				DrawingGUI.clear();
			} //if
		} //big if
	} //end mainAction
	
	//SIDE//
	
	/*
	 * Handles the side action
	 * int num - num entered by user
	 */
	private void sideAction(int num) {
		//add a size and step forward
		
		if (num < 1) { //invalidly small
			DrawingGUI.reportError("Size enter too small!");
			
		} else if (num < 3 || num > 5) { //too small or too big for cyclo
			canvas.addSideSize(num);
			canvas.addSideCyclo(false);
			canvas.setSideStep(3); //skip over cyclo step
			
		} else if (num > 10) { //invalidly big
			DrawingGUI.reportError("Size entered too big");		
			
		} else {
			canvas.addSideSize(num);
			canvas.setSideStep(2);
		} //if
	} //end sideAction
	
	/*
	 * Handles the side action
	 * String in - key entered by user
	 */
	private void sideAction(String in) {
		if (canvas.getSideStep() == 2) {
			if (in.equalsIgnoreCase("Y")) {
				canvas.addSideCyclo(true);
				canvas.setSideStep(3);
			} else if (in.equalsIgnoreCase("N")) {
				canvas.addSideCyclo(false);
				canvas.setSideStep(3);
			} //if
		} //big if
	} //end sideAction
	
	//BOND//
	
	/*
	 * Handles the bond action
	 * int num - num entered by user
	 */
	private void bondAction(int num) {
		if (num < 2) { //too small
			DrawingGUI.reportError("Number entered too small!");
			
		} else if (num > 3) { //too big
			DrawingGUI.reportError("Number entered too big!");
			
		} else { //valid
			canvas.setBondSize(num);
			canvas.setBondStep(2);
		} //if
	} //end bondAction
} //end class
