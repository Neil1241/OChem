package ochem.drawing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ochem.drawing.Node.NodeType;

public class PaletteButtonController implements ActionListener {
	//Attributes
	private Palette palette; //instance of the palette
	private JButton button; //instance of the button this listener is attached to

	public PaletteButtonController(Palette palette, JButton button) {
		this.palette = palette;
		this.button = button;
	}

	@Override
	public void actionPerformed(ActionEvent b) {
		//get the text in the button 
		char text = button.getText().charAt(0);

		//set type based on button text
		switch (text) {
			case 'S':
				palette.setSelectedType(NodeType.SINGLE_BOND);
				break;
			
			case 'D':
				palette.setSelectedType(NodeType.DOUBLE_BOND);
				break;
			
			case 'T':
				palette.setSelectedType(NodeType.TRIPLE_BOND);
				break;
		} // switch
		System.out.println(this.toString() +": "+ palette.getSelectedType().toString()); //dbg
	}

} // end class
