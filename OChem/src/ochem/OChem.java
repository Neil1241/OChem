package ochem;

import java.awt.Toolkit;

public class OChem {
	public static int width;
	public static int height;
	
	/*
	 * Instantiate the View, Model and show the frame
	 * (Currently a test platform for the Interpreter)
	 */
	public static void main(String[] args) {
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		new View();
	} //end main

} //end OChem
