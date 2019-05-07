package ochem;

import java.awt.Dimension;

import javax.swing.JFrame;

import ochem.drawing.DrawingGUI;

public class View extends JFrame {	
	private int width;
	private int height;
	
	private DrawingGUI draw;
	
	public View() {
		width = (int) (0.75 * OChem.width);
		height = (int) (0.5 * OChem.height);
		
		this.setPreferredSize(new Dimension(width, height));
		
		layoutView();
		
		this.setContentPane(draw);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void layoutView() {
		draw = new DrawingGUI(width, height);
	}
}
