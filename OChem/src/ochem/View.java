package ochem;

import java.awt.Dimension;

/*
 * View
 * Created by: Neil Balaskandarajah
 * Last modified: 05/16/2019
 * View of the program that cycles through the different panels
 */

import javax.swing.JFrame;

import ochem.drawing.DrawingGUI;

public class View extends JFrame {	
	//Attributes
	private int width; //width of frame
	private int height; //hwight of frame
	
	//Panels
	private DrawingGUI draw; 
	
	//integer for the padding of the component
	public static final int PAD = 6;
	
	/*
	 * Create a frame with a view proportional to the 
	 */
	public View() {
		//calculate the width and height of the frame
		width = (int) (0.5 * OChem.width + 2 * PAD);
		height = (int) (0.5 * OChem.height + 2 * PAD);
		
		//create and add all components
		layoutView();
		
		//set the view's panel
		this.setContentPane(draw);
		
		//configure the frame
//		this.setSize(new Dimension(width, height));
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(OChem.width/2 - width/2, OChem.height/2 - 3*height/4);
		this.setResizable(false);
		this.dispose();
		this.setUndecorated(true);
		this.setVisible(true);
	} //end constructor
	
	/*
	 * Instantiate all panels
	 */
	private void layoutView() {
		draw = new DrawingGUI(width, height);
	} //end layoutView
} //end class
