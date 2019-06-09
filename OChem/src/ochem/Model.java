package ochem;
/*
 * Model
 * Jordan Lin
 * May 25 2019
 * Model for the welcome screen
 */
import javax.swing.*;

import ochem.welcome.WelcomeGUI;

public class Model extends Object
{
	//instance variables
	private JFrame frame= new JFrame();
	private WelcomeGUI g;
	
	//constructor method
	public Model()
	{
		super();
	}
	
	public void setWelcome(WelcomeGUI g) {
		this.g = g;
	}
	
	public WelcomeGUI getWelcome() {
		return this.g;
	}
	
	public void setGUI(JPanel p)
	{
		frame.setContentPane(p);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}//end setGUI
}//end class
