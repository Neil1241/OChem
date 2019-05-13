package ochem;

import java.awt.Toolkit;

public class OChem {
	public static int width;
	public static int height;
	
	/*
	 * Instantiate the View, Model and show the frame
	 * (Currently a test platform for the Interpreter)
	 */
	public static void main(String[] args) {
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		/*
		Scanner in = new Scanner(System.in);
		PrintStream out = System.out;
		
		out.println("Enter name of compound: ");
		String cmpnd = in.next();
		
		Compound compound = Interpreter.compoundFromName(cmpnd);
		
		out.println(compound.toString());
		*/
		
		View view = new View();
	} //end main

} //end OChem
