package ochem;

import javax.swing.*;

public class Model extends Object
{
	//private JPanel opening;
	private JFrame frame= new JFrame();
	
	public Model()
	{
		super();
	}
	
	public void setGUI(JPanel p)
	{
		//this.opening=p;
		frame.setContentPane(p);
		frame.pack();
		frame.setVisible(true);
		frame.setLocation(OChem.width/2 - p.getWidth()/2, OChem.height/2 - 3*p.getHeight()/4); //get rid of x offset
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
