package ochem.test;
/* titleBar.java
 * Jordan Lin
 * May 31, 2019
 */

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;

import java.awt.*;

import ochem.OChem;
import ochem.drawing.*;

public class TitleBar extends JPanel {
	private String title;
	public TitleBar (String title) {
		super();
		this.title =title;
		this.layoutView();
		//this.setPreferredSize(new Dimension(OChem.height/10,OChem.width/10));
	}
	
	private void layoutView() {
		OBox exit = new OBox(100,100,"X",true,false);
		OBox min = new OBox(100,100,"-",true,false);
		OBox full = new OBox(100,100,"min",true,false);
		
		this.add(min);
		this.add(full);
		this.add(exit);
	}
	
	public static void main (String[] args) {
		
		//JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame f = new JFrame();
		TitleBar t = new TitleBar("Hello");
		JDesktopPane d = new JDesktopPane();
		JInternalFrame ff = new JInternalFrame("test",true,true,true,true);
		/*SynthLookAndFeel s = new SynthLookAndFeel();
		NimbusLookAndFeel n = new NimbusLookAndFeel();
		 try { 
		       // UIManager.setLookAndFeel(s); 
			 n.installColors(t, "Black", "green");
		        UIManager.setLookAndFeel(n);
		    } catch(Exception ignored){}*/
		
		//JPanel p = new JPanel();
		
		try {
	        // String name = UIManager.getSystemLookAndFeelClassName();
	         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	         UIManager.put("InternalFrame.activeTitleBackground", Color.red);
	         UIManager.put("InternalFrame.activeTitleForeground", Color.blue);
	         UIManager.put("InternalFrame.inactiveTitleBackground", Color.black);
	         UIManager.put("InternalFrame.inactiveTitleForeground", Color.yellow);
	      }
	      catch(Exception e) {
	         e.printStackTrace();
	      }
		
		ff.setContentPane(t);
		ff.getContentPane().setBackground(Color.white);
		//f.setUndecorated(true);
		ff.setVisible(true);
	    //f.getRootPane().setWindowDecorationStyle( JRootPane. COLOR_CHOOSER_DIALOG );
		ff.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ff.pack();
		d.add(ff);
		f.add(d);
		f.setSize(500,500);
		f.setVisible(true);
	}

}

