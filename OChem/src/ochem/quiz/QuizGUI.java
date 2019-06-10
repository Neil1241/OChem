package ochem.quiz;
/* QuizGUI
 * Created by: Jordan Lin
 * June 5, 2019
 * GUI to be used for the quizzing feature
 */

//import packages
import javax.swing.*;
import java.awt.*;
import ochem.*;
import ochem.drawing.*;
import ochem.naming.*;

public class QuizGUI extends JPanel {
	//instance variables
	private OBox name = new OBox(200,100,"Name",true,false);
	private OBox draw = new OBox(200,100,"Draw",true,false);
	private QuizModel model = new QuizModel();
	private JLabel displayName = new JLabel("");
	private JLabel rating = new JLabel("");
	private NamingGUI names = new NamingGUI();
	private DrawingGUI draws = new DrawingGUI((int) (0.5 * OChem.width + 2 * View.PAD),(int) (0.5 * OChem.height + 2 * View.PAD));
	private OBox back = new OBox(120,75,"Back",true,false);
	private Model openModel;
	
	//constructor method
	public QuizGUI() {
		super();
		this.openModel = null;
		this.layoutView();
		this.model.setGUI(this);
		this.registerControllers();
		this.setPreferredSize(new Dimension((int) (0.5 * OChem.width + 2 * View.PAD),(int) (0.5 * OChem.height + 2 * View.PAD)));
	}//end Constructor
	
	public QuizGUI(Model m) {
		super();
		this.openModel = m;
		this.layoutView();
		this.model.setGUI(this);
		this.registerControllers();
		this.setPreferredSize(new Dimension((int) (0.5 * OChem.width + 2 * View.PAD),(int) (0.5 * OChem.height + 2 * View.PAD)));
	}//end Constructor
	
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
		back.addMouseListener(b);
		name.addMouseListener(n);
		draw.addMouseListener(d);
	}//end registerControllers
	
	public void update() {
		this.remove(draws);
		this.remove(names);
		if (this.model.getDraw()) {
			this.add(draws,BorderLayout.CENTER);
			this.displayName.setText(this.model.getCompoundName());
			this.setSize((int) (0.5 * OChem.width + 2 * View.PAD),(int) (0.5 * OChem.height + 2 * View.PAD));
			SwingUtilities.updateComponentTreeUI(this);
		}else {
			this.displayName.setText(null);
			this.add(names,BorderLayout.CENTER);
			SwingUtilities.updateComponentTreeUI(this);
		}//end if
	}//end update
	

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
