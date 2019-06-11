package ochem.quiz;
/* NameInputController.java
 * Jordan Lin
 * Jun. 11, 2019
 */
import java.awt.event.*;

public class NameInputController implements ActionListener {
	//instance variables
	QuizModel model;
	
	
	public NameInputController(QuizModel model) {
		this.model = model;
	}//constructor
	
	public void actionPerformed(ActionEvent e) {
		this.model.checkCompound(e.getActionCommand());
	}//end actionPerformed

}
