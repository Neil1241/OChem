package ochem.drawing;

/*
 * OBox
 * Created by: Neil Balaskandarajah
 * Last modified: 05/12/2019
 * A custom and more aesthetically pleasing button/text box
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;

import javax.swing.JComponent;

import ochem.View;

public class OBox extends JComponent {
	//Attributes
	private String text; //text for the box
	
	private Color backgroundColor; //box background color
	private Color textColor; //text color
	private Font f; //Font for text
	private float fontSize; //font size in pixels
	private int cornerRad; //radius of the corners in pixels
	
	/*
	 * Create an OBox with a width, height and text inside
	 * int width - width of the box
	 * int height - height of the box
	 * String text - text in the box
	 */
	public OBox(int width, int height, String text) {
		super();
		
		this.text = text;	
		
		//set the size of the box
		this.setPreferredSize(new Dimension(width, height));
		
		//set the font size
		fontSize = 96.0F;	
		
		//try to create and set the font if the file is present
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/HelveticaNeue.ttf"));
		} catch (Exception e) {
			e.printStackTrace();
			f = new Font(Font.SANS_SERIF, Font.PLAIN, (int) fontSize);
		} //try-catch
		
		//set the corner radius and colors
		cornerRad = 60;		
		backgroundColor = Color.BLACK;
		textColor = Color.GREEN;
	} //end constructor
	
	/*
	 * Draw the box to the screen
	 * Graphics g - object responsible for drawing 
	 */
	public void paintComponent(Graphics g) {
		//draw border
		g.setColor(backgroundColor.darker());
		g.fillRoundRect(View.PAD, 0, this.getWidth()-View.PAD*2, this.getHeight(), cornerRad, cornerRad);
		
		//draw the inside
		int wall = 10;
		g.setColor(backgroundColor);
		g.fillRoundRect(View.PAD + wall, wall, this.getWidth()-View.PAD*2 - 2*wall, this.getHeight() - 2*wall, cornerRad, cornerRad);
		
		//set the text color
		g.setColor(textColor);

		//set the font
		g.setFont(f.deriveFont(fontSize));
		FontMetrics fm = g.getFontMetrics();
		
		//draw the text centered on the button
		g.drawString(text, (this.getWidth() / 2) - (fm.stringWidth(text) / 2), 
							(this.getHeight() / 2) + (fm.getAscent() / 2) - 10);
	} //end paintComponent
	
	/*
	 * Set the background color of the box
	 * Color c - background color for the box
	 */
	public void setBackgroundColor(Color c) {
		backgroundColor = c;
	} //end setBackgroundColor

	/*
	 * Get the text to the box
	 * return text - text of the box
	 */
	public String getText() {
		return text;
	} //end getText
	
	/*
	 * Set the text to the box
	 * String text - text to set the box to
	 */
	public void setText(String text) {
		this.text = text;
	} //end setText
	
	/*
	 * Update the box
	 */
	public void update() {
		repaint();
	} //end update
	
	/*
	 * Set the font size of the box
	 * float fontSize - new font size
	 */
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	} //end setFontSize
	
	/*
	 * Set the corner radius of the box
	 * int cornerRad - corner radius
	 */
	public void setCornerRadius(int cornerRad) {
		this.cornerRad = cornerRad;
	} //end setCornerRadius

	/*
	 * Set the text color of the box
	 * Color color - new text color
	 */
	public void setTextColor(Color color) {
		this.textColor = color;
	} //end setTextColor
} //end class