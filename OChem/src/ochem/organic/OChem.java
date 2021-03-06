package ochem.organic;

/*
 * OChem
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * An application where users can enter the name of an organic compound
 * 		or draw it on the screen and see the opposite form. Also has a
 * 		random quiz generation feature.
 */

import java.io.PrintStream;
import java.util.Scanner;

public class OChem {
	/*
	 * Instantiate the View, Model and show the frame (Currently a test platform for
	 * the Interpreter)
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		PrintStream out = System.out;

		//for (int i=0;i<3;i++) {
		out.println("Enter name of compound: ");
		String cmpnd = in.nextLine();
		//String cmpnd2 = in.nextLine();

		Compound compound = Interpreter.compoundFromName(cmpnd);
		//Compound c = Interpreter.compoundFromName(cmpnd2);

		out.println("------------------------------------------");
		out.println("OCHEM RUNNING THIS");
		out.println("------------------------------------------");
		out.println(compound.toString());
		out.println("------------------BOND--------------------");
		out.println(compound.getMainChain().getBond());
		
		out.println(OrganicUtil.nameFromCompound(compound));
		//out.println(OrganicUtil.compareCompound(c, compound));
		//}

		in.close();
	} // end main

} // end OChem
