package ochem.drawing;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import ochem.drawing.Node.NodeType;

/*
 * Palette
 * Created by: Neil Balaskandarajah
 * Last modified: 05/09/2019
 * A palette class that lets the user select what they want to add to the canvas
 */

public class Palette extends JPanel {
	//Attributes
	private int width;
	private int height;
	
	//Buttons
	private JButton singleBnd;
	private JButton doubleBnd;
	private JButton tripleBnd;
	private JButton clear;
	
	//current type
	private NodeType selectedType;
	
	/*
	 * Creates a palette with a width and a height
	 * int width - width of the palette
	 * int height - height of the palette
	 */
	public Palette(int width, int height) {
		this.width = width;
		this.height = height;
		
		this.setPreferredSize(new Dimension(width, height));
		
		selectedType = NodeType.BLANK;
		
		layoutView();
		registerControllers();
	} //end constructor

	/*
	 * Add all the buttons to the palette
	 */
	private void layoutView() {
		//add layout to panel
		GridLayout grid = new GridLayout(2,5, 5,5); //2x5 grid with 5 pixel gap all around
		this.setLayout(grid);
		
		//single button 
		singleBnd = new JButton("S");
		singleBnd.setPreferredSize(new Dimension(width/2, height / 5));
		this.add(singleBnd);
		
		//double button
		doubleBnd = new JButton("D");
		doubleBnd.setPreferredSize(new Dimension(width/2, height / 5));
		this.add(doubleBnd);
		
		//triple button
		tripleBnd = new JButton("T");
		tripleBnd.setPreferredSize(new Dimension(width/2, height / 5));
		this.add(tripleBnd);
		
		//clear button
		clear = new JButton("ERASE");
		clear.setPreferredSize(new Dimension(width/2, height / 5));
		this.add(clear);
	} //end layoutView

	/*
	 * Add controllers to each button
	 */
	private void registerControllers() {
		PaletteButtonController s = new PaletteButtonController(this, singleBnd);
		singleBnd.addActionListener(s);
		
		PaletteButtonController d = new PaletteButtonController(this, doubleBnd);
		doubleBnd.addActionListener(d);
		
		PaletteButtonController t = new PaletteButtonController(this, tripleBnd);
		tripleBnd.addActionListener(t);
		
		PaletteButtonController c = new PaletteButtonController(this, clear);
		clear.addActionListener(c);
	} //end registerControllers
	
	/*
	 * Return the selected type of node
	 * return selectedType - currently selected node type
	 */
	public NodeType getSelectedType() {
		return selectedType;
	} //end getSelectedType
	
	/*
	 * Set the selected type of node
	 * NodeType type - type of node selected
	 */
	public void setSelectedType(NodeType type) {
		selectedType = type;
	} //end setNodeType	
} //end class
