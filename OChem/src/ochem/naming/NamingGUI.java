package ochem.naming;
/* NamingGUI.java
 * Jordan Lin
 * May 24, 2019
 */

//import packages
import javax.swing.*;

import ochem.OChem;
import ochem.View;
import ochem.drawing.*;
import ochem.organic.OrganicUtil;

public class NamingGUI extends JPanel {
	//declare instance variables
	private JTextField input;
	private NamingModel model = new NamingModel();
	private JLabel test= new JLabel("");
	private JTextArea a= new JTextArea();
	private Canvas c;
	private int width = (int) (0.5 * OChem.width + 2 * View.PAD);
	private int height = (int) (0.5 * OChem.height + 2 * View.PAD);

	public NamingGUI() {
		super();
		this.layoutView();
		this.model.setGUI(this);
		this.registerControllers();
	}
	
	private void layoutView()
	{
		input= new JTextField(20);
		c = new Canvas(this.width,this.height);
		this.add(c);
		this.add(input);
		this.add(test);
		this.add(a);
		
	}
	
	private void registerControllers() {
		TextInputController t = new TextInputController(this.model);
		input.addActionListener(t);
	}
	
	public void update() {
		this.test.setText("IT WORKED");
	}
	
	//main for testing purposes
	public static void main(String[] args) {
		JFrame f= new JFrame();
		NamingGUI g= new NamingGUI();
		
		
		f.setLocation(OChem.width/2 - g.width/2, OChem.height/2 - 3*g.height/4);
		f.setContentPane(g);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(g.width,g.height);		
	}
	
}//end class
