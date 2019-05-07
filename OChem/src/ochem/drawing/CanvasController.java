package ochem.drawing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CanvasController implements MouseListener {
	private Canvas canvas;
	
	public CanvasController(Canvas canvas) {
		super();
		
		this.canvas = canvas;
	}
	
	@Override
	public void mouseClicked(MouseEvent m) {
		
	}

	@Override
	public void mouseEntered(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent m) {
		System.out.println("CanvasController: " + m.getX() +" "+ m.getY());
		canvas.update();
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

}
