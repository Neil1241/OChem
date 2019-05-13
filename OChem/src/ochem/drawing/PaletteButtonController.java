package ochem.drawing;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ochem.drawing.Canvas.ActionType;

public class PaletteButtonController implements MouseListener {
	//Attributes
	private Palette palette; //instance of the palette
	private OBox button; //instance of the button this listener is attached to
	
	private final Color NORMAL;
	private final Color ENTERED;
	private final Color PRESSED;

	public PaletteButtonController(Palette palette, OBox button) {
		this.palette = palette;
		this.button = button;
		
		NORMAL = new Color(140,255,0);
		ENTERED = new Color(124,226,0);
		PRESSED = new Color(99,181,0);
		
		button.setBackgroundColor(NORMAL);
		button.update();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		//get the text in the button 
		String text = button.getText();

		//set type based on button text
		if (text.equals("Main")) {
			palette.setSelectedType(ActionType.MAIN);			
		} else if (text.equals("Side")) {
			palette.setSelectedType(ActionType.SIDE);
		} else if (text.equals("Function")) {
			palette.setSelectedType(ActionType.FUNC_GROUP);
		} // switch
		
		button.update();
		
		palette.update();
		System.out.println(palette.getSelectedType().toString());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		button.setBackgroundColor(ENTERED);
		button.update();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		button.setBackgroundColor(NORMAL);
		button.update();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		button.setBackgroundColor(PRESSED);
		button.update();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		button.setBackgroundColor(ENTERED);
		button.update();
	}

} // end class
