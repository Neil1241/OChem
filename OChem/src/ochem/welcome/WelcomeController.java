package ochem.welcome;
/*
 *WelcomeButton Controller 
 * 
 */

//import packages
import java.awt.event.*;
import ochem.drawing.*;
import ochem.naming.*;
import ochem.*;

public class WelcomeController implements ActionListener
{
	private int width = (int) (0.5 * OChem.width + 2 * View.PAD);
	private int height = (int) (0.5 * OChem.height + 2 * View.PAD);
	private NamingGUI name = new NamingGUI();
	private DrawingGUI draw = new DrawingGUI(this.width,this.height);
	private Model model;
	
	public WelcomeController(Model model)
	{
		this.model=model;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Naming"))
			this.model.setGUI(name);
		else if (e.getActionCommand().equals("Drawing"))
			this.model.setGUI(draw);
		else
			this.model.setGUI(null);
	}
}
