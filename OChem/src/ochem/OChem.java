package ochem;

import java.awt.Toolkit;

import ochem.welcome.WelcomeGUI;

public class OChem {
	public static int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();;
	public static int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	/*
	 * Instantiate the View, Model and show the frame
	 */
	public static void main(String[] args) {
		new WelcomeGUI();
	} //end main

} //end OChem
