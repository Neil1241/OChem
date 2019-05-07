package ochem.drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class Canvas extends JComponent {
	private int width;
	private int height;

	private Node[][] nodes;
	private final int NODE_RADIUS = 20;
	
	public Canvas(int parentWidth, int parentHeight) {
		super();
		this.width = (int) (parentWidth * 0.8);
		this.height = (int) (parentHeight);
		
		this.setPreferredSize(new Dimension(width, height));
		
		createNodes();
		
		registerControllers();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, width, height);
		
		drawNodes(g2);
	} 
	
	private void createNodes() {
		nodes = new Node[10][6];
		int widthChunk = (width - 100) / (nodes.length+1);
		int heightChunk = (height - 50) / (nodes[0].length+1);
		
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				nodes[i][j] = new Node(widthChunk * (i+1), heightChunk * (j+1), NODE_RADIUS);
			} //inner
		} //outer
	} //end createNodes
	
	private void drawNode(Graphics2D g2, Node n) {
		g2.setColor(Color.YELLOW);
		
		g2.fillOval(n.getX() - n.getRad(), n.getY() - n.getRad(), 2 * n.getRad(), 2 * n.getRad());
//		System.out.println("drawNode(): " + (x-rad) +" "+ (y-rad));
	}

	private void drawNodes(Graphics2D g2) {
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				drawNode(g2, nodes[i][j]);
			} //inner
		} //outer
	}
	
	public void update() {
		repaint();
	}
	
	private void registerControllers() {
		CanvasController cc = new CanvasController(this);
		this.addMouseListener(cc);
	}
}
