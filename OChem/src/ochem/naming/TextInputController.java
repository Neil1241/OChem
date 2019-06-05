package ochem.naming;
/* TextInputController.java
 * Jordan Lin
 * May 24, 2019
 */

import java.awt.event.*;
import ochem.organic.*;

import ochem.organic.Interpreter;

public class TextInputController implements ActionListener {
	// declare instance variables
	private NamingModel model;
	private Compound c;

	public TextInputController(NamingModel model) {
		this.model = model;
	}

	public void actionPerformed(ActionEvent e) {
		String in = e.getActionCommand();
		if (in.length() < 6)
			this.model.giveInvalid();
		else {
			//try {
				this.c = Interpreter.compoundFromName(in);
				this.model.giveCompound(c);
			//} catch (Exception error) {
//				this.model.giveInvalid();
			//}
		}

	}
}
