package ochem;

/*
 * View
 * Created by: Neil Balaskandarajah
 * Last modified: 05/07/2019
 * View class holding all panels that the program passes through
 */

import java.awt.Dimension;

import javax.swing.JFrame;

import ochem.drawing.DrawingGUI;

public class View extends JFrame {	
	//Attributes
	private int width; //width of frame
	private int height; //hwight of frame
	
	//Panels
	private DrawingGUI draw; 
	
	/*
	 * Create a frame with a view proportional to the 
	 */
	public View() {
		width = (int) (0.625 * OChem.width);
		height = (int) (0.8 * OChem.height);
		
//		this.setPreferredSize(new Dimension(width, height));
		
		layoutView();
		
		this.setContentPane(draw);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void layoutView() {
		draw = new DrawingGUI(width, height);
	}
}
