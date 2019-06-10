package ochem.test;
import javax.swing.*;
public class HelloWorld {
	public static void main(String args[]) {
		JMenuBar m = new JMenuBar();
		JMenu mm = new JMenu("Buttons?");
		JFrame f = new JFrame();
		JPanel p = new JPanel();
		JButton b = new JButton("Test");
		
		m.add(mm);
		p.add(m);
		
		f.setContentPane(p);
		f.setVisible(true);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}//end main

}
