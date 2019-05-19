	package ochem.drawing;

import java.awt.Color;
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
		if ((int) k.getKeyChar() == ENTER_KEY && isNumber(DrawingGUI.getUserInput())) {
			int num = Integer.parseInt(DrawingGUI.getUserInput());
			
			switch (palette.getSelectedType()) {
				//main button
				case MAIN:
					//set the size and step forward
					canvas.setMainSize(num);
					canvas.setMainStep(2);
					break;
					
				//side button
				case SIDE:
					//add a size and step forward
					canvas.addSideSize(num);
					
					canvas.setSideStep(2);
					break;
					
				//clear button
				case CLEAR:
					break;
					
				//functional group button
				case FUNC_GROUP:
					break;
				default:
					break;
			}
		}
		
	} //end keyTyped

	/*
	 * Checks if a String is a valid number
	 */
	private boolean isNumber(String text) {
		try {
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException n) {
			
			return false;
		}
	} //end isNumber
}
