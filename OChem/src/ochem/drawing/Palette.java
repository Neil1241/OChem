package ochem.drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import ochem.View;
import ochem.drawing.DrawingUtil.ActionType;

/*
 * Palette
 * Created by: Neil Balaskandarajah
 * Last modified: 05/09/2019
 * A palette class that lets the user select what they want to add to the canvas
 */

public class Palette extends JPanel {
	//Attributes
	private int width; //width of palette in pixels
	private int height; //height of palette in pixels
	
	//feature buttons
	private OBox main; //main button
	private OBox side; //side button
	private OBox bond; //bond button
	private OBox clear; //clear button
	private OBox name; //name button
	
	//functional group buttons
	private OBox fluoro;
	private OBox bromo;
	private OBox chloro;
	private OBox iodo;
	private OBox aldehyde;
	private OBox ketone;
	private OBox alcohol;
	private OBox carbox;
	private OBox amine;
	private OBox amide;
	private OBox ether;
	private OBox ester;
	
	//list of buttons so adding controllers is easier
	private ArrayList<OBox> buttons;
	
	private final int NUM_ROWS = 11; //number of buttons on the screen
	
	private Canvas canvas; //instance of the Canvas
	
	private ActionType selectedType; //current type
	
	/*
	 * Creates a palette with a width and a height
	 * int width - width of the palette
	 * int height - height of the palette
	 */
	public Palette(int width, int height) {
		this.width = width;
		this.height = height;
		
		//set the size of the palette
		this.setPreferredSize(new Dimension(width, height));
		
		//set the type of the palette
		selectedType = ActionType.CLEAR;
		
		//create and add all components
		layoutView();
	} //end constructor

	/*
	 * Add all the buttons to the palette
	 */
	private void layoutView() {
		Color btnTxt = DrawingGUI.TEXT_COLOR; //text color
		buttons = new ArrayList<OBox>();
		
		//main chain button
		main = new OBox(width, height/NUM_ROWS - 2*View.PAD, "Main", true, false);
		this.add(main);
		buttons.add(main);
		
		//side chain button
		side = new OBox(width, height/NUM_ROWS - 2*View.PAD, "Side", true, false);
		this.add(side);
		buttons.add(side);
		
		//bond button
		bond = new OBox(width, height/NUM_ROWS - 2*View.PAD, "Bond", true, false);
		bond.setFontSize(90.0F);
		this.add(bond);
		buttons.add(bond);
		
		//functional group button 
		JPanel row1 = new JPanel(); //first row
		
		//fluorine
		fluoro = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "F", true, false);
		buttons.add(fluoro);
		row1.add(fluoro);
		
		//iodine
		iodo = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "I", true, false);
		buttons.add(iodo);
		row1.add(iodo);
		
		//add first row to panel
		this.add(row1);
		row1.setBackground(DrawingGUI.BG_COLOR);
		
		JPanel row2 = new JPanel(); //second row
		
		//chlorine
		chloro = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "Cl", true, false);
		buttons.add(chloro);
		row2.add(chloro);
		
		//bromine
		bromo = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "Br", true, false);
		buttons.add(bromo);
		row2.add(bromo);
		
		this.add(row2);
		row2.setBackground(DrawingGUI.BG_COLOR);
		
		JPanel row3 = new JPanel(); //third row
		
		//aldehyde
		aldehyde = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "Al", true, false);
		buttons.add(aldehyde);
		row3.add(aldehyde);
		
		//ketone
		ketone = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "Ke", true, false);
		buttons.add(ketone);
		row3.add(ketone);	
		
		//add second row to panel
		this.add(row3);
		row3.setBackground(DrawingGUI.BG_COLOR);
		
		JPanel row4 = new JPanel(); //fourth row
		
		//alcohol
		alcohol = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "OH", true, false);
		buttons.add(alcohol);
		row4.add(alcohol);	
		
		//carboxylic acid
		carbox = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "CA", true, false);
		buttons.add(carbox);
		row4.add(carbox);
		
		this.add(row4);
		row4.setBackground(DrawingGUI.BG_COLOR);
	
		JPanel row5 = new JPanel(); //row 5
		
		//amine
		amine = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "N", true, false);
		buttons.add(amine);
		row5.add(amine);
		
		//amide
		amide = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "ON", true, false);
		buttons.add(amide);
		row5.add(amide);
		
		this.add(row5);
		row5.setBackground(DrawingGUI.BG_COLOR);
		
		JPanel row6 = new JPanel(); //row 6
		
		//ether
		ether = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "O", true, false);
		buttons.add(ether);
		row6.add(ether);
		
		//ester
		ester = new OBox(width/2, height/NUM_ROWS - 2*View.PAD, "OO", true, false);
		buttons.add(ester);
		row6.add(ester);
		
		this.add(row6);
		row6.setBackground(DrawingGUI.BG_COLOR);
		
		//clear button
		clear = new OBox(width, height/NUM_ROWS - 2*View.PAD, "Clear", true, false);
		buttons.add(clear);
		this.add(clear);
		
		//name button
		name = new OBox(width, height/NUM_ROWS - 2*View.PAD, "Name", true, false);
		buttons.add(name);
		this.add(name);
		
		//set color to all buttons
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setTextColor(btnTxt);
		} //loop
		
		//set the background color for the panel
		this.setBackground(DrawingGUI.BG_COLOR);
	} //end layoutView

	/*
	 * Add controllers to each button
	 */
	private void registerControllers() {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setFontSize((float) (DrawingUtil.FONT_SIZE * 0.8));
			buttons.get(i).addMouseListener(new PaletteButtonController(this, buttons.get(i), canvas));
		} //loop
	} //end registerControllers
	
	/*
	 * Return the selected type of node
	 * return selectedType - currently selected node type
	 */
	public ActionType getSelectedType() {
		return selectedType;
	} //end getSelectedType
	
	/*
	 * Set the selected type of node
	 * NodeType type - type of node selected
	 */
	public void setSelectedType(ActionType type) {
		selectedType = type;
	} //end setNodeType	
	
	/*
	 * Set the canvas instance of the palette and instantiate all controllers
	 * Canvas canvas - the instance of the Canvas for the Palette
	 */
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
		registerControllers();
	} //end setCanvas
	
	/*
	 * Get the Canvas instance
	 * return canvas - instance of the Canvas
	 */
	public Canvas getCanvas() {
		return canvas;
	} //end getCanvas
} //end class
