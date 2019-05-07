package ochem;
/*
 * WelcomeButton
 * Jordan Lin
 * May 6 2019
 * Extend the JButton object ot be able to change the background
 */

//import packages
import javax.swing.*;
import java.awt.*;

public class WelcomeButton extends JButton 
{
	//declare instance variables
	private Color hoverBackgroundColor;
	private Color pressedBackgroundColor;

	//constructors
	public WelcomeButton() {
		this(null);
	}

	public WelcomeButton(String text) {
		super(text);
		super.setContentAreaFilled(false);
	}

	//paint the button colors
	protected void paintComponent(Graphics g) {
		if (getModel().isPressed()) {
			g.setColor(pressedBackgroundColor);
		} else if (getModel().isRollover()) {
			g.setColor(hoverBackgroundColor);
		} else {
			g.setColor(getBackground());
		}
		
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	//mutator/accessor methods
	public Color getHoverBackgroundColor() {
		return hoverBackgroundColor;
	}

	public void setHoverBackgroundColor(Color hoverBackgroundColor) {
		this.hoverBackgroundColor = hoverBackgroundColor;
	}

	public Color getPressedBackgroundColor() {
		return pressedBackgroundColor;
	}

	public void setPressedBackgroundColor(Color pressedBackgroundColor) {
		this.pressedBackgroundColor = pressedBackgroundColor;
	}
}
