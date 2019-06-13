package ochem;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/*
 * Model
 * Jordan Lin
 * May 25 2019
 * Model for the welcome screen
 */
import javax.swing.JFrame;
import javax.swing.JPanel;

import ochem.drawing.DrawingUtil;
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
		frame.setLocation(OChem.width/2 - p.getWidth()/2, OChem.height/2 - 3*p.getHeight()/4); //get rid of x offset
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			frame.setIconImage(ImageIO.read(new File(DrawingUtil.ICON_LOCATION)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end setGUI
}//end class
