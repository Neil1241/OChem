package ochem.welcome;
/*
 * Welcome GUI
 * Jordan Lin
 * May 6 2019
 * 
 */


//import packages
import ochem.*;
import java.awt.*;
import javax.swing.*;

public class WelcomeGUI extends JPanel
{
	//declare instance variables
	private WelcomeButton quizing= new WelcomeButton("Quiz");
	private WelcomeButton naming= new WelcomeButton("Naming");
	private WelcomeButton drawing = new WelcomeButton("Drawing");
	private JLabel quote= new JLabel("Best Period of The Day!");
	private JLabel title= new JLabel("OCHEM");
	private Model model = new Model();
	
	//constructor
	public WelcomeGUI()
	{
		super();
		this.layoutView();
		this.model.setGUI(this);
		this.registerControllers();
	}
	
	private void registerControllers()
	{
		WelcomeController name = new WelcomeController(this.model);
		WelcomeController draw = new WelcomeController(this.model);
		WelcomeController quiz = new WelcomeController (this.model);
		this.naming.addActionListener(name);
		this.drawing.addActionListener(draw);
		this.quizing.addActionListener(quiz);
	}
	private void layoutView()
	{
		Color hover=Color.CYAN;
		Color press=Color.BLUE;
		
		//create layout managers and JPanels
		JPanel buttons= new JPanel();
		BorderLayout layout= new BorderLayout();
		GridLayout buttonAligment= new GridLayout(3,1);
		
		//change JLabel justification
		title.setFont(new Font("Arial",Font.PLAIN,25));
		title.setHorizontalAlignment(JLabel.CENTER);
		quote.setHorizontalAlignment(JLabel.CENTER);
		
		
		//set the colors of buttons
		quizing.setHoverBackgroundColor(hover);
		quizing.setPressedBackgroundColor(press);
		naming.setHoverBackgroundColor(hover);
		naming.setPressedBackgroundColor(press);
		drawing.setHoverBackgroundColor(hover);
		drawing.setPressedBackgroundColor(press);
		
		//set layouts
		this.setLayout(layout);
		buttons.setLayout(buttonAligment);
		
		//add buttons to button panel
		buttons.add(naming);
		buttons.add(drawing);
		buttons.add(quizing);

		//add components to this panel
		this.add(buttons,BorderLayout.CENTER);
		this.add(quote,BorderLayout.SOUTH);
		this.add(title,BorderLayout.NORTH);
		
		
	}
	
	//main for testing purposes
	public static void main(String args[])
	{
		WelcomeGUI view= new WelcomeGUI();
	}
}
