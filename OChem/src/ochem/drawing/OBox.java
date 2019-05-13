package ochem.drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;

import javax.swing.JComponent;

public class OBox extends JComponent {
	private String text;
	
	private Color backgroundColor;
	private Color textColor;
	private Font f;
	private float fontSize;
	private int cornerRad;
	
	public OBox(int width, int height, String text) {
		super();
		this.text = text;	
		
		this.setPreferredSize(new Dimension(width, height));
		
		fontSize = 96.0F;		
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/HelveticaNeue.ttf"));
		} catch (Exception e) {
			e.printStackTrace();
			f = new Font(Font.SANS_SERIF, Font.PLAIN, (int) fontSize);
		}
		
		cornerRad = 60;
		
		backgroundColor = Color.BLACK;
		textColor = Color.GREEN;
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), cornerRad, cornerRad);
		
		g.setColor(textColor);
		
		g.setFont(f.deriveFont(fontSize));
		FontMetrics fm = g.getFontMetrics();
		
		g.drawString(text, (this.getWidth() / 2) - (fm.stringWidth(text) / 2), 
							(this.getHeight() / 2) + (fm.getAscent() / 2) );
	}
	
	public void setBackgroundColor(Color c) {
		backgroundColor = c;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void update() {
		repaint();
	}
	
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	
	public void setCornerRadius(int cornerRad) {
		this.cornerRad = cornerRad;
	}

	public void setTextColor(Color color) {
		this.textColor = color;
	}
}
