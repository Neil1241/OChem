package ochem.drawing;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import ochem.View;
import ochem.drawing.CanvasUtil.ActionType;

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
	
	//feature buttons
	private OBox main;
	private OBox side;
	private OBox func;
	private OBox bond;
	private OBox clear;
	private OBox name;
	
	//number of buttons on the screen
	private final int NUM_BUTTONS = 5;
	
	//instance of the Canvas
	private Canvas canvas;
	
	//current type
	private ActionType selectedType;
	
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
		//main chain button
		main = new OBox(width, height/NUM_BUTTONS - 2*View.PAD, "Main");
		main.setTextColor(Color.BLACK);
		this.add(main);
		
		//side chain button
		side = new OBox(width, height/NUM_BUTTONS - 2*View.PAD, "Side");
		side.setTextColor(Color.BLACK);
		this.add(side);
		
		//functional group button 
		func = new OBox(width, height/NUM_BUTTONS - 2*View.PAD, "Function");
		func.setTextColor(Color.BLACK);
		func.setFontSize(90.0F);
//		this.add(func);
		
		//bond button
		bond = new OBox(width, height/NUM_BUTTONS - 2*View.PAD, "Bond");
		bond.setTextColor(Color.BLACK);
		bond.setFontSize(90.0F);
		this.add(bond);
		
		//clear button
		clear = new OBox(width, height/NUM_BUTTONS - 2*View.PAD, "Clear");
		clear.setTextColor(Color.BLACK);
		clear.setFontSize(90.0F);
		this.add(clear);
		
		//name button
		name = new OBox(width, height/NUM_BUTTONS - 2*View.PAD, "Name");
		name.setTextColor(Color.BLACK);
		name.setFontSize(90.0F);
		this.add(name);
		
		//set the background color for the panel
		this.setBackground(Color.WHITE);
	} //end layoutView

	/*
	 * Add controllers to each button
	 */
	private void registerControllers() {
		//main chain button controller
		PaletteButtonController mainC = new PaletteButtonController(this, main, canvas);
		main.addMouseListener(mainC);

		//side chain button controller
		PaletteButtonController sideC = new PaletteButtonController(this, side, canvas);
		side.addMouseListener(sideC);

		//functional group button controller
		PaletteButtonController funcC = new PaletteButtonController(this, func, canvas);
		func.addMouseListener(funcC);

		//bondbutton controller
		PaletteButtonController bondC = new PaletteButtonController(this, bond, canvas);
		bond.addMouseListener(bondC);
		
		//clear button controller
		PaletteButtonController clearC = new PaletteButtonController(this, clear, canvas);
		clear.addMouseListener(clearC);
		
		//name button controller
		PaletteButtonController nameC = new PaletteButtonController(this, name, canvas);
		name.addMouseListener(nameC);
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
