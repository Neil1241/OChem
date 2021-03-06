package ochem.quiz;
/* QuizController
 * Jordan Lin
 * June 8 2019
 * Controller used for the quizzing gui actions
 */

//imports
import java.awt.event.*;
import java.awt.*;
import ochem.drawing.*;
import ochem.*;
import javax.swing.*;

public class QuizController implements MouseListener{
	//instance variables
	private final Color NORMAL;
	private final Color ENTERED;
	private final Color PRESSED;
	private OBox button;
	private QuizModel model;
	private Model p;
	
	//constructor
	public QuizController(QuizModel model, OBox button) {
		this.model = model;
		this.button = button;
		NORMAL = DrawingGUI.BG_COLOR;
		ENTERED = NORMAL.darker();
		PRESSED = ENTERED.darker();
		
		this.button.setBackgroundColor(NORMAL);
		// redraw with the new color
		this.button.update();
	}
	
	//constructor
	public QuizController(Model model, OBox button) {
		this.p = model;
		this.button = button;
		NORMAL = DrawingGUI.BG_COLOR;
		ENTERED = NORMAL.darker();
		PRESSED = ENTERED.darker();
		
		this.button.setBackgroundColor(NORMAL);
		// redraw with the new color
		this.button.update();
	}

	public void mouseClicked(MouseEvent e) {
		if (this.button.getText().equalsIgnoreCase("name")) {
			this.model.setDraw(false);
			this.model.generateCompound();
		}else if(this.button.getText().equalsIgnoreCase("draw")) {
			this.model.setDraw(true);
			this.model.generateCompound();
		}else if(this.button.getText().equalsIgnoreCase("check")){
			try {
			this.model.checkDrawn();
			}catch (NullPointerException err) {}
		}else{
			this.p.setGUI(this.p.getWelcome());
		}
	}

	//methods below are just used to darken the button
	public void mouseEntered(MouseEvent e) {
		button.setBackgroundColor(ENTERED);
		this.button.update();
	}

	public void mouseExited(MouseEvent e) {
		this.button.setBackgroundColor(NORMAL);
		this.button.update();
	}

	public void mousePressed(MouseEvent e) {
		button.setBackgroundColor(PRESSED);
		this.button.update();
	}

	public void mouseReleased(MouseEvent e) {
		this.button.setBackgroundColor(NORMAL);
		this.button.update();
	}
	
	

}
