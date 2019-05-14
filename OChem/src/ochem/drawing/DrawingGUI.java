package ochem.drawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DrawingGUI extends JPanel {
	private int width;
	private int height;
	private Canvas canvas;
	private Palette palette;
	
	private static OBox dialog;
	private static JTextField userInput;
	
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
		JPanel dialogPanel = new JPanel();
		double DIALOG_SCALE = 0.9;
		
		dialog = new OBox((int) (DIALOG_SCALE * width), (int) (height * ERROR_SCALE), "No errors detected");
		dialog.setBackgroundColor(Color.BLACK);
		dialog.setTextColor(Color.GREEN);
		dialog.setCornerRadius(20);
		dialog.setFontSize(80.0F);
		dialogPanel.add(dialog);
		
		userInput = new JTextField("", SwingConstants.CENTER);
		userInput.setPreferredSize(new Dimension((int) ((1 - DIALOG_SCALE) * width), (int) (height * ERROR_SCALE))); 
		userInput.setFont(userInput.getFont().deriveFont(80.0F));
		
		UserInputController uic = new UserInputController(canvas);
		userInput.addKeyListener(uic);
		
		dialogPanel.add(userInput);
		
		this.add(dialogPanel, BorderLayout.SOUTH);
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
	
	public static String getUserInput() {
		return userInput.getText();
	}
} //end class
