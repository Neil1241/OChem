package ochem.test;
/* titleBar.java
 * Jordan Lin
 * May 31, 2019
 */

import javax.swing.*;
import javax.swing.plaf.synth.SynthLookAndFeel;

import java.awt.*;

import ochem.OChem;
//import ochem.drawing.*;

public class TitleBar extends JComponent {
	private String title;
	public TitleBar (String title) {
		super();
		this.title =title;
		this.setPreferredSize(new Dimension(OChem.height/10,OChem.width/10));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2= (Graphics2D)(g);
		g2.scale(this.getWidth()/10, this.getHeight()/10);
		g2.setStroke(new BasicStroke(1.0F/this.getWidth()));
		
		//g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) 50));
		g2.drawString(this.title, 1, 1);
		g2.drawRect(2, 0, 2, 2);
	}
	
	public static void main (String[] args) {
		
		SynthLookAndFeel s = new SynthLookAndFeel();
		 try { 
		        UIManager.setLookAndFeel(s); 
		    } catch(Exception ignored){}
		JFrame f = new JFrame();
		JPanel p = new JPanel();
		TitleBar t = new TitleBar("Hello");
		JFrame.setDefaultLookAndFeelDecorated(true);
		p.add(t);
		f.setContentPane(p);
		//f.setUndecorated(true);
		f.setVisible(true);
	    //f.getRootPane().setWindowDecorationStyle( JRootPane. COLOR_CHOOSER_DIALOG );
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
	}

}

