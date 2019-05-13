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
	private static OBox dialog;
	
	private final double CANVAS_SCALE = 0.8;
	private final double ERROR_SCALE = 0.1;
	
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
		addDialogBox();
	}

	private void addPalette() {
		palette = new Palette((int) ((1.0 - CANVAS_SCALE) * width), (int) ((1.0 - ERROR_SCALE) * height));
		this.add(palette, BorderLayout.WEST);
	}
	
	private void addCanvas() {
		canvas = new Canvas((int) (width * CANVAS_SCALE), (int) ((1.0 - ERROR_SCALE) * height), palette);
		this.add(canvas, BorderLayout.CENTER);
	}
	
	private void addDialogBox() {
		dialog = new OBox(width, (int) (height * ERROR_SCALE), "No errors detected");
		dialog.setBackgroundColor(Color.BLACK);
		dialog.setTextColor(Color.GREEN);
		dialog.setCornerRadius(20);
		dialog.setFontSize(80.0F);
		
		this.add(dialog, BorderLayout.SOUTH);
	}	
	
	public static void reportError(String message) {
		dialog.setTextColor(Color.RED);
		dialog.setText(message);
		dialog.update();
	}
	
	public static void showMessage(String message) {
		dialog.setTextColor(Color.GREEN);
		dialog.setText(message);
		dialog.update();
	}
} //end class
