package ochem.drawing;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class DrawingGUI extends JPanel {
	private int width;
	private int height;
	private Canvas canvas;
	private Palette palette;
	
	private final double CANVAS_SCALE = 0.8;
	
	public DrawingGUI(int width, int height) {		
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		
		layoutView();
	}
	
	private void layoutView() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		addPalette();
		addCanvas();
	}
	
	private void addPalette() {
		palette = new Palette((int) ((1.0 - CANVAS_SCALE) * width), height);
		this.add(palette);
	}
	
	private void addCanvas() {
		canvas = new Canvas((int) (width * CANVAS_SCALE), height, palette);
		this.add(canvas);
	}
} //end class
