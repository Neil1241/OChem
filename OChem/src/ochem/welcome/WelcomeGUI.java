package ochem.welcome;
/*
 * Welcome GUI
 * Jordan Lin
 * May 6 2019
 * 
 */

//import packages
import ochem.*;
import ochem.drawing.DrawingGUI;
import ochem.drawing.DrawingUtil;

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
	private Model model;

	// constructor
	public WelcomeGUI(Model model) {
		super();
		this.model = model;
		this.layoutView();
		this.model.setGUI(this);
		this.model.setWelcome(this);
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
		Color hover = DrawingGUI.BG_COLOR.darker();
		Color press = DrawingGUI.BG_COLOR.darker().darker();
		JLabel gif = new JLabel("");

		try {
			ImageIcon icon = new ImageIcon(getClass().getResource("/resources/Spin.gif"));
			gif = new JLabel(icon);
		} catch (Exception e) {
			System.out.println("HAHAHAHAHA");
		}
		
		Border emptyBorder = BorderFactory.createEmptyBorder();
		naming.setBorder(emptyBorder);
		drawing.setBorder(emptyBorder);
		quizing.setBorder(emptyBorder);
		
		naming.setFont(DrawingUtil.getFileFont(DrawingUtil.OXYGEN_LOCATION));
		drawing.setFont(DrawingUtil.getFileFont(DrawingUtil.OXYGEN_LOCATION));
		quizing.setFont(DrawingUtil.getFileFont(DrawingUtil.OXYGEN_LOCATION));

		// create layout managers and JPanels
		JPanel buttons = new JPanel();
		BorderLayout layout = new BorderLayout();
		GridLayout buttonAligment = new GridLayout(3, 1);

		// change JLabel justification
		title.setFont(DrawingUtil.getFileFont(DrawingUtil.VOICE_ACTIVATED_LOCATION));
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setOpaque(true);
		title.setBackground(DrawingGUI.BG_COLOR);
		title.setForeground(DrawingGUI.TEXT_COLOR);
		
		quote.setHorizontalAlignment(JLabel.CENTER);
		quote.setFont(new Font("Arial", Font.PLAIN, 25));
		quote.setOpaque(true);
		quote.setBackground(DrawingGUI.BG_COLOR);
		quote.setForeground(DrawingGUI.TEXT_COLOR);

		// set the colors of buttons and their texts
		quizing.setHoverBackgroundColor(hover);
		quizing.setPressedBackgroundColor(press);
		quizing.setForeground(DrawingGUI.TEXT_COLOR);
		
		naming.setHoverBackgroundColor(hover);
		naming.setPressedBackgroundColor(press);
		naming.setForeground(DrawingGUI.TEXT_COLOR);
		
		drawing.setHoverBackgroundColor(hover);
		drawing.setPressedBackgroundColor(press);
		drawing.setForeground(DrawingGUI.TEXT_COLOR);

		// set layouts
		this.setLayout(layout);
		buttons.setLayout(buttonAligment);

		// add buttons to button panel
		buttons.add(drawing);
		buttons.add(naming);
		buttons.add(quizing);

		// add components to this panel
		this.add(buttons, BorderLayout.CENTER);
//		this.add(gif, BorderLayout.EAST);
		this.add(quote, BorderLayout.SOUTH);
		this.add(title, BorderLayout.NORTH);
		this.setPreferredSize(new Dimension (OChem.width/2,OChem.height/2));
		this.setBackground(Color.white);
	}
}
