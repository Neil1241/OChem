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
import javax.swing.border.*;

public class WelcomeGUI extends JPanel {
	// declare instance variables
	private WelcomeButton quizing = new WelcomeButton("Quiz");
	private WelcomeButton naming = new WelcomeButton("Naming");
	private WelcomeButton drawing = new WelcomeButton("Drawing");
	private JLabel quote = new JLabel("Best Period of The Day!");
	private JLabel title = new JLabel("OChem");
	private Model model = new Model();

	// constructor
	public WelcomeGUI() {
		super();
		this.layoutView();
		this.model.setGUI(this);
		this.registerControllers();
	}

	private void registerControllers() {
		WelcomeController name = new WelcomeController(this.model);
		WelcomeController draw = new WelcomeController(this.model);
		WelcomeController quiz = new WelcomeController(this.model);
		this.naming.addActionListener(name);
		this.drawing.addActionListener(draw);
		this.quizing.addActionListener(quiz);
	}

	private void layoutView() {
		Color hover = Color.CYAN;
		Color press = Color.BLUE;
		JLabel gif = new JLabel("");

		try {
			ImageIcon icon = new ImageIcon("src/resources/Spin.gif");
			gif = new JLabel(icon);
		} catch (Exception e) {
			System.out.println("HAHAHAHAHA");
		}
		
		Border emptyBorder = BorderFactory.createEmptyBorder();
		naming.setBorder(emptyBorder);
		drawing.setBorder(emptyBorder);
		quizing.setBorder(emptyBorder);
		
		naming.setFont(new Font("Arial", Font.PLAIN, 25));
		drawing.setFont(new Font("Arial", Font.PLAIN, 25));
		quizing.setFont(new Font("Arial", Font.PLAIN, 25));
		
		

		// create layout managers and JPanels
		JPanel buttons = new JPanel();
		BorderLayout layout = new BorderLayout();
		GridLayout buttonAligment = new GridLayout(3, 1);

		// change JLabel justification
		title.setFont(new Font("Arial", Font.PLAIN, 45));
		title.setHorizontalAlignment(JLabel.CENTER);
		quote.setHorizontalAlignment(JLabel.CENTER);
		quote.setFont(new Font("Arial", Font.PLAIN, 25));

		// set the colors of buttons
		quizing.setHoverBackgroundColor(hover);
		quizing.setPressedBackgroundColor(press);
		naming.setHoverBackgroundColor(hover);
		naming.setPressedBackgroundColor(press);
		drawing.setHoverBackgroundColor(hover);
		drawing.setPressedBackgroundColor(press);

		// set layouts
		this.setLayout(layout);
		buttons.setLayout(buttonAligment);

		// add buttons to button panel
		buttons.add(naming);
		buttons.add(drawing);
		buttons.add(quizing);

		// add components to this panel
		this.add(buttons, BorderLayout.CENTER);
		this.add(gif, BorderLayout.EAST);
		this.add(quote, BorderLayout.SOUTH);
		this.add(title, BorderLayout.NORTH);
		this.setPreferredSize(new Dimension (OChem.width/2,OChem.height/2));
		this.setBackground(Color.white);
	}

	// main for testing purposes
	public static void main(String args[]) {
		new WelcomeGUI();
	}
}
