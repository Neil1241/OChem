package ochem.drawing;

import java.awt.Dimension;

import javax.swing.JPanel;

public class DrawingGUI extends JPanel {
	private int width;
	private int height;
	private Canvas canvas;
	
	public DrawingGUI(int width, int height) {		
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		
		layoutView();
	}
	
	private void layoutView() {
		addCanvas();
	}
	
	private void addCanvas() {
		canvas = new Canvas(width, height);
		this.add(canvas);
	}
}
