package ochem.naming;
/* NamingGUI.java
 * Jordan Lin
 * May 24, 2019
 */

//import packages
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;

import ochem.OChem;
import ochem.View;
import ochem.drawing.*;
import ochem.organic.OrganicUtil;

public class NamingGUI extends JPanel {
	//declare instance variables
	private JTextField input;
	private NamingModel model = new NamingModel();
	private JLabel test= new JLabel("");
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
		//create and set layout
		BoxLayout shell = new BoxLayout(this,BoxLayout.Y_AXIS);
		this.setLayout(shell);
	
		//configure the heights and widths
		input= new JTextField(20);
		input.setPreferredSize(new Dimension(OChem.width/10,OChem.height/20));
		input.setFont(new Font("Arial", Font.PLAIN, 30));
		test.setFont(new Font("Arial", Font.PLAIN, 30));
		c = new Canvas(this.width,this.height);
		
		//add components to panel
		this.add(c);
		this.add(test);
		this.add(input);
		
	}
	
	private void registerControllers() {
		TextInputController t = new TextInputController(this.model);
		input.addActionListener(t);
	}
	
	public void update() {
		if (!this.model.isValid()) {
			this.test.setText("Not A Valid Compound");
			this.model.setValid(true);
		}
		else {
			this.test.setText(OrganicUtil.nameFromCompound(this.model.getCompound()));
			this.input.setText(null);
		}
	}
	
	//main for testing purposes
	public static void main(String[] args) {
		JFrame f= new JFrame();
		NamingGUI g= new NamingGUI();
		
		
		f.setLocation(OChem.width/2 - g.width/2, OChem.height/2 - 3*g.height/4);
		f.setContentPane(g);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();		
	}
	
}//end class
