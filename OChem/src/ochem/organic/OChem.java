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

		out.println("Enter name of compound: ");
		String cmpnd = in.nextLine();

		Compound compound = Interpreter.compoundFromName(cmpnd);
		Chain[] chains = compound.getSideChains();

		out.println("------------------------------------------");
		out.println("OCHEM RUNNING THIS");
		out.println("------------------------------------------");
		out.println(compound.toString());
		out.println("------------------CYCLO-------------------");
		out.println(compound.getMainChain().getCyclo());
		for (int i = 0; i < compound.getSideChains().length; i++)
			out.println(chains[i].getCyclo());
		out.println("------------------BOND--------------------");
		out.println(compound.getMainChain().getBond());
		in.close();
	} // end main

} // end OChem
