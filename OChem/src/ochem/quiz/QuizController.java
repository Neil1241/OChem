package ochem.quiz;
/* QuizController
 * Jordan Lin
 * June 8 2019
 */
import java.awt.event.*;
import java.awt.*;
import ochem.drawing.*;

public class QuizController implements MouseListener{
	private final Color NORMAL;
	private final Color ENTERED;
	private final Color PRESSED;
	private OBox button;
	private QuizModel model;
	
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

	public void mouseClicked(MouseEvent e) {
		if (this.button.getText().equalsIgnoreCase("name")) {
			this.model.setDraw(false);
			this.model.generateCompound();
		}else if(this.button.getText().equalsIgnoreCase("draw")) {
			this.model.setDraw(true);
			this.model.generateCompound();
		}
	}

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
