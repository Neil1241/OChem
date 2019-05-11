package ochem.drawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawingGUI extends JPanel {
	private int width;
	private int height;
	private Canvas canvas;
	private Palette palette;
	private static JLabel error;
	
	private final double CANVAS_SCALE = 0.8;
	private final double ERROR_SCALE = 0.05;
	
	public DrawingGUI(int width, int height) {		
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		
		layoutView();
	}
	
	private void layoutView() {
		this.setLayout(new BorderLayout());
		
		addPalette();
		addCanvas();
		addErrorPanel();
	}

	private void addPalette() {
		palette = new Palette((int) ((1.0 - CANVAS_SCALE) * width), (int) ((1.0 - ERROR_SCALE) * height));
		this.add(palette, BorderLayout.WEST);
	}
	
	private void addCanvas() {
		canvas = new Canvas((int) (width * CANVAS_SCALE), (int) ((1.0 - ERROR_SCALE) * height), palette);
		this.add(canvas, BorderLayout.CENTER);
	}
	
	private void addErrorPanel() {
		error = new JLabel("No errors detected");
		error.setFont(error.getFont().deriveFont(50.0F));
		error.setPreferredSize(new Dimension(width, (int) (height * ERROR_SCALE)));
		this.add(error, BorderLayout.SOUTH);
	}	
	
	public static void reportError(String message) {
		error.setForeground(Color.red);
		error.setText(message);
	}
} //end class
