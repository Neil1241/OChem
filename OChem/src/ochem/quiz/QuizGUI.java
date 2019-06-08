package ochem.quiz;
/* QuizGUI
 * Created by: Jordan Lin
 * June 5, 2019
 * GUI to be used for the quizzing feature
 */

//import packages
import javax.swing.*;
import ochem.drawing.*;
public class QuizGUI extends JPanel {
	//instance variables
	OBox name = new OBox(100,100,"Name",true,false);
	OBox draw = new OBox(100,100,"Draw",true,false);
	QuizModel model = new QuizModel();
	
	//constructor method
	public QuizGUI() {
		super();
		this.layoutView();
		this.model.setGUI(this);
		this.registerControllers();
	}//end Constructor
	
	private void layoutView() {
		this.add(name);
		this.add(draw);
	}
	
	private void registerControllers() {
		QuizController n = new QuizController(this.model,this.name);
		QuizController d = new QuizController(this.model,this.draw);
		name.addMouseListener(n);
		draw.addMouseListener(d);
	}//end registerControllers

	public static void main(String args[]) {
		JFrame f = new JFrame();
		QuizGUI g = new QuizGUI();
		f.setVisible(true);
		f.setContentPane(g);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500,500);
	}
	
	public void update() {
		
	}
}//end class
