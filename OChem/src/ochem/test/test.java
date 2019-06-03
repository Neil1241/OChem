package ochem.test;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.net.*;
	  
	public class test extends JFrame {
	   JDesktopPane desktop;
	   int nframes = 0;
	   
	   public test() {
	      modifyColors();
	      desktop = new JDesktopPane();
	      setContentPane(desktop);
	      setJMenuBar(createMenuBar());
	      createInternalFrame();
	      addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent we) {
	            System.exit(0);
	         }
	      });
	   }
	  
	   protected void modifyColors() {
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
	   }
	  
	   protected JMenuBar createMenuBar() {
	      JMenuBar menuBar = new JMenuBar();
	  
	      JMenu createMenu = new JMenu("Create");
	      createMenu.setMnemonic(KeyEvent.VK_C);
	      JMenuItem newMenuItem = new JMenuItem("New");
	      newMenuItem.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent ae) {
	            createInternalFrame();
	         }
	      });
	      newMenuItem.setMnemonic(KeyEvent.VK_N);
	      createMenu.add(newMenuItem);
	      menuBar.add(createMenu);
	  
	      return menuBar;
	   }
	  
	   protected void createInternalFrame() {
	      nframes++;
	      String title = "JInternalFrame #" + nframes;
	      JInternalFrame frame = new JInternalFrame(title,
	         true,    // resizable
	         true,    // closable
	         true,    // maximizable
	         true);   // iconifiable
	      frame.setVisible(true);
	  
	      frame.setBorder(BorderFactory.createLineBorder(Color.black));
	 frame.updateUI();
	      desktop.add(frame);
	      frame.setSize(200, 200);
	      frame.setLocation(30*nframes, 30*nframes);
	      try {
	         frame.setSelected(true);
	      } catch (java.beans.PropertyVetoException e) {}
	   }
	  
	   public static void main(String []args) {
	      test main = new test();
	      main.setSize(500, 300);
	      main.setVisible(true);
	   }
	}

