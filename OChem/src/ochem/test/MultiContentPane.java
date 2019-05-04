package ochem.test;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * MultiContentPane
 * @author Neil
 * - SwingUtilities.updateComponentTreeUI takes the panel to update
 * - changing panels for a JFrame
 */

public class MultiContentPane {
	static boolean swap;
	
	public static void main(String[] args) {
		swap = false;
		
		JFrame f = new JFrame("Multi-Frame Test");

		JButton button = new JButton("Switch content pane");
		button.setPreferredSize(new Dimension(300,300));
		
		JButton button2 = new JButton("Switch content pane");
		button2.setPreferredSize(new Dimension(300,300));
		
		JPanel horiz = new JPanel();
		horiz.setLayout(new BoxLayout(horiz, BoxLayout.X_AXIS));
		horiz.add(new JLabel("I'm horizontal"));
		
		JPanel vert = new JPanel();
		vert.setLayout(new BoxLayout(vert, BoxLayout.Y_AXIS));
		vert.add(new JLabel("I'm vertical"));
		vert.add(button2);
		horiz.add(button);		
		
		f.setContentPane(horiz);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				swap = !swap;
				System.out.println(swap);
				
				if (swap) { 
					f.setContentPane(horiz);
					SwingUtilities.updateComponentTreeUI(horiz);
				} else {
					f.setContentPane(vert);
					SwingUtilities.updateComponentTreeUI(vert);
				}
				
			}
			
		});
		
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				swap = !swap;
				System.out.println(swap);
				
				if (swap) { 
					f.setContentPane(horiz);
					SwingUtilities.updateComponentTreeUI(horiz);
				} else {
					f.setContentPane(vert);
					SwingUtilities.updateComponentTreeUI(vert);
				}
			
			}
		});
	}
}
