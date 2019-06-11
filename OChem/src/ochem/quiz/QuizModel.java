package ochem.quiz;
/* QuizModel
 * Jordan Lin
 * June 8 2019
 * The model class for the interactions within the quiz
 */

//import packages
import ochem.organic.*;
public class QuizModel extends Object{
	private QuizGUI g;
	private Compound compound;
	private boolean draw;
	private int correct = 0;
	private int questions = 0;
	
	public QuizModel() {
		super();
	}
	
	public void setGUI(QuizGUI g) {
		this.g = g;
	}
	
	public void setDraw(Boolean b) {
		this.draw = b;
	}
	
	public boolean getDraw() {
		return this.draw;
	}
	
	public String getCompoundName() {
		return OrganicUtil.nameFromCompound(this.compound);
	}
	
	public Compound getCompound() {
		return this.compound;
	}
	
	public void generateCompound() {
		this.compound = OrganicUtil.generateRandomCompound();
		this.questions++;
		this.g.update();
	}
	
	//returns the number of correctly answered questions
	public int getCorrect() {
		return this.correct;
	}
	
	//returns the number of questions given
	public int getQuestions() {
		return this.questions;
	}
	
	public void checkCompound(String c) {
		Compound cmpd2;
		try {
			cmpd2 = Interpreter.compoundFromName(c);
			if (OrganicUtil.compareCompound(this.compound,cmpd2))
				this.correct++;
		}catch (Exception e) {}
		this.g.update();
	}

}
