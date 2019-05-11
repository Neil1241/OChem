package ochem;

import ochem.welcome.WelcomeGUI;

public class Model extends Object
{
	WelcomeGUI opening;
	
	public Model()
	{
		super();
	}
	
	public void setGUI(WelcomeGUI p)
	{
		this.opening=p;
	}
}
