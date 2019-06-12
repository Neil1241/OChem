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
	private boolean attempted;
	private boolean lastRight;
	
	public QuizModel() {
		super();
		this.attempted = false;
		this.lastRight = false;
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
		this.attempted = true;
		try {
			cmpd2 = Interpreter.compoundFromName(c);
			if (OrganicUtil.compareCompound(this.compound,cmpd2)) {
				this.correct++;
				this.lastRight = true;
			}else
				this.lastRight = false;
		}catch (Exception e) {}
		this.g.update();
	}
	
	public void checkDrawn() {
		Compound cmpd2 = this.g.getCompound();
		
		if (OrganicUtil.compareCompound(this.compound,cmpd2)) {
			this.correct++;
			this.lastRight = true;
		}else
			this.lastRight = false;
		this.compound = null;
		this.attempted = true;
		this.g.update();
	}
	
	public boolean isCorrect() {
		return this.lastRight;
	}
	
	public boolean getAttempted() {
		return this.attempted;
	}
	
	public void setAttempted(boolean b) {
		this.attempted = b;
	}

}
