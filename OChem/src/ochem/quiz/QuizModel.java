package ochem.quiz;
/* QuizModel
 * Jordan Lin
 * June 8 2019
 * The model class for the interactions within the quiz
 */

//import packages
import ochem.organic.*;

public class QuizModel extends Object {
	// instance variables
	private QuizGUI g; // gui
	private Compound compound; // generated compound
	private boolean draw; // set the quiz to draw mode
	private int correct = 0; // num of correctly answered questions
	private int questions = 0;// num of questions given
	private boolean attempted; // if the last compound was attempted
	private boolean lastRight; // if the last answer was right

	// constructor
	public QuizModel() {
		super();
		this.attempted = false;
		this.lastRight = false;
	}

	// set gui
	public void setGUI(QuizGUI g) {
		this.g = g;
	}

	// set if the gui should be displaying the drawing gui or the naming gui
	public void setDraw(Boolean b) {
		this.draw = b;
	}

	// return if the drawing gui is to be displayed
	public boolean getDraw() {
		return this.draw;
	}

	// return the compound name in string form
	public String getCompoundName() {
		return OrganicUtil.nameFromCompound(this.compound);
	}

	// get the compound object
	public Compound getCompound() {
		return this.compound;
	}

	// generates the compound for the quiz
	public void generateCompound() {
		this.compound = OrganicUtil.generateRandomCompound();
		System.out.println(compound.toString());
		this.questions++;
		this.g.update();
	}

	// returns the number of correctly answered questions
	public int getCorrect() {
		return this.correct;
	}

	// returns the number of questions given
	public int getQuestions() {
		return this.questions;
	}

	// checks the named compound with the randomly generated one
	public void checkCompound(String c) {
		Compound cmpd2;
		this.attempted = true;
		try {
			cmpd2 = Interpreter.compoundFromName(c);
			if (OrganicUtil.compareCompound(this.compound, cmpd2)) {
				this.correct++;
				this.lastRight = true;
			} else
				this.lastRight = false;
		} catch (Exception e) {
		}
		this.g.update();
	}

	// checks the drawnCompound with the randomly generated one
	public void checkDrawn() {
		Compound cmpd2 = this.g.getCompound();

		if (OrganicUtil.compareCompound(this.compound, cmpd2)) {
			this.correct++;
			this.lastRight = true;
		} else
			this.lastRight = false;
		this.compound = null;
		this.attempted = true;
		this.g.update();
	}

	// return the amount of correctly answered questions
	public boolean isCorrect() {
		return this.lastRight;
	}

	// return if the last question was attempted
	public boolean getAttempted() {
		return this.attempted;
	}

	// set whether or not the compound was attempted
	public void setAttempted(boolean b) {
		this.attempted = b;
	}

}
