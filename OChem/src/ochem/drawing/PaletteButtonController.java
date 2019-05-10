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
		String text = button.getText();

		//set type based on button text
		if (text.equals("S")) {
			palette.setSelectedType(NodeType.SINGLE_BOND);
		} else if (text.equals("D")) {
			palette.setSelectedType(NodeType.DOUBLE_BOND);
		} else if (text.equals("T")) {
			palette.setSelectedType(NodeType.TRIPLE_BOND);
		} else if (text.equals("ERASE")) {
			palette.setSelectedType(NodeType.BLANK);
		} // switch
		System.out.println(this.toString() +": "+ palette.getSelectedType().toString()); //dbg
	}

} // end class
