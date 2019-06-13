package ochem.quiz;
/* NameInputController.java
 * Jordan Lin
 * Jun. 11, 2019
 * Controller for the textfield for the quizzing GUI
 */
import java.awt.event.*;

public class NameInputController implements ActionListener {
	//instance variables
	QuizModel model;
	
	//constructor
	public NameInputController(QuizModel model) {
		this.model = model;
	}//constructor
	
	//gives the string input to the model
	public void actionPerformed(ActionEvent e) {
		this.model.checkCompound(e.getActionCommand());
	}//end actionPerformed

}
