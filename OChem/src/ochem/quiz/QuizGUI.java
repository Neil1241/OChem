package ochem.quiz;
/* QuizGUI
 * Created by: Jordan Lin
 * June 5, 2019
 * GUI to be used for the quizzing feature
 */

import java.awt.BorderLayout;
import java.awt.Dimension;

//import packages
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ochem.Model;
import ochem.OChem;
import ochem.View;
import ochem.drawing.DrawingGUI;
import ochem.drawing.DrawingUtil;
import ochem.drawing.OBox;
import ochem.naming.NamingGUI;
import ochem.organic.Compound;

public class QuizGUI extends JPanel {
	//instance variables
	private OBox name = new OBox(200,100,"Name",true,false);
	private OBox draw = new OBox(200,100,"Draw",true,false);
	private OBox back = new OBox(120,75,"Back",true,false);
	private OBox check = new OBox(120,75,"Check",true,false);
	private QuizModel model = new QuizModel();
	private JLabel displayName = new JLabel("");
	private JLabel rating = new JLabel("");
	private NamingGUI names = new NamingGUI(new NameInputController(this.model));
	private DrawingGUI draws = new DrawingGUI(500,500);
	private Model openModel;
	
	//constructor method
	public QuizGUI() {
		super();
		this.openModel = null;
		this.layoutView();
		this.model.setGUI(this);
		this.registerControllers();
		this.setPreferredSize(
				new Dimension((int) (0.7 * OChem.width + 2 * View.PAD), (int) (0.68 * OChem.height + 2 * View.PAD)));
	}// end Constructor

	public QuizGUI(Model m) {
		this();
		this.openModel = m;
	}// end Constructor
	
	private void layoutView() {
		JPanel buttons = new JPanel();
		JPanel rightSide = new JPanel();
		JPanel top = new JPanel();
		
		//set layouts for panels
		BorderLayout b = new BorderLayout();
		BorderLayout bar = new BorderLayout();
		BoxLayout right = new BoxLayout(rightSide,BoxLayout.Y_AXIS);
		this.setLayout(b);
		top.setLayout(bar);
		rightSide.setLayout(right);
		
		//set text sizing
		this.rating.setHorizontalAlignment(JLabel.CENTER);
		this.rating.setText(this.model.getCorrect()+"/"+this.model.getQuestions());
		this.rating.setFont(DrawingUtil.getFileFont(DrawingUtil.OXYGEN_LOCATION));
		this.displayName.setHorizontalAlignment(JLabel.CENTER);
		this.displayName.setFont(DrawingUtil.getFileFont(DrawingUtil.OXYGEN_LOCATION));
		
		//add components to panel
		buttons.add(name);
		buttons.add(draw);
		rightSide.add(buttons);
		rightSide.add(check);
		rightSide.add(rating);
		top.add(back,BorderLayout.WEST);
		top.add(displayName,BorderLayout.CENTER);
		this.add(rightSide,BorderLayout.EAST);
		this.add(top,BorderLayout.NORTH);
		
	}//end layoutView
	
	private void registerControllers() {
		QuizController n = new QuizController(this.model,this.name);
		QuizController d = new QuizController(this.model,this.draw);
		QuizController b = new QuizController(this.openModel,this.back);
		QuizController c = new QuizController(this.model,this.check);
		this.check.addMouseListener(c);
		this.back.addMouseListener(b);
		this.name.addMouseListener(n);
		this.draw.addMouseListener(d);
	}//end registerControllers
	
	public void update() {
		this.remove(draws);
		this.remove(names);
		if(this.model.getAttempted()) {
			this.displayName.setText(null);
			this.model.setAttempted(false);
			if (this.model.isCorrect()) {
				this.displayName.setText("CORRECT");
			}
		}else if (this.model.getDraw()) {
			this.add(draws,BorderLayout.CENTER);
			this.displayName.setText(this.model.getCompoundName());
		}else {
			this.add(names,BorderLayout.CENTER);
			this.names.setCompound(this.model.getCompound());
			this.names.update();
			this.displayName.setText(null);
		}//end if
		
		SwingUtilities.updateComponentTreeUI(this);
		
		this.rating.setText(this.model.getCorrect()+"/"+this.model.getQuestions());
	}//end update
	
	public Compound getCompound() {
		return this.draws.getCompound();
	}
	

	//main for testing purposes
	public static void main(String args[]) {
		JFrame f = new JFrame();
		QuizGUI g = new QuizGUI();
		f.setVisible(true);
		f.setContentPane(g);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
	}
}//end class
