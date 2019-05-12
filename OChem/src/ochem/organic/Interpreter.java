package ochem;

/*
 * Interpreter
 * Created by: Neil Balaskandarajah
 * Last modified: 05/07/2019
 * Converts text names of compounds into compound objects
 */

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Interpreter {

	// Attributes
	private static StringTokenizer compoundName; // name of the compound
	private static Compound compound; // compound to be created
	private static String suffix = ""; // compound suffix made more general for future use
	private static int error = 0; // counter to use for checking suffix
	private static int mainBond;
	private static String front = "";
	private static ArrayList<String> chainNames; // all the side chains
	private static ArrayList<Integer> chainLocations; // locations of the side chains
	private static String originalName; // original name of String
	private static int additionalGroups;

	/*
	 * Create a Compound object from a text name String name - name of the compound
	 * as text return compound - compound object from Compound
	 */
	public static Compound compoundFromName(String name) {
		originalName = name;

		splitChains(); // remove all the hyphens in the String

		// print out the elements in the array
		for (int i = 0; i < chainNames.size(); i++)
			System.out.println(chainNames.get(i)); // dbg

		// create compound and set if the main chain is a cyclo
		compound = new Compound(chainToNumber(chainNames.get(chainNames.size() - 1), suffix, front));
		if (front.equalsIgnoreCase(OrganicUtil.PREFIX[9]))
			compound.getMainChain().setCyclo(true);

		//set the mainchain bond type and get their locations
		compound.getMainChain().setBond(mainBond);
		for (int i = 1; i <= additionalGroups; i++)
			compound.addFunctionalLocation(chainLocations.get(chainLocations.size() - i));

		//add sidechains
		addChains();

		return compound;
	} // end compoundFromName

	
	// Split the text form of the name into the side chains and locations
	private static void splitChains() {
		// delimit by the hyphen, commas and spaces. and add it to the list
		compoundName = new StringTokenizer(originalName, "-, "); // Create StringTokenizer for ease of manipulation
		chainNames = new ArrayList<String>();
 
		//temporary variable
		String temp = ""; //holds the next token
		
		//loop while there are more tokens
		while (compoundName.hasMoreTokens()) {
			temp = compoundName.nextToken();
			
			//if the token says acid append it with the last token
			if (temp.equalsIgnoreCase("acid")) {
				temp = chainNames.get(chainNames.size() - 1) + " " + temp;
				chainNames.remove(chainNames.remove(chainNames.size() - 1));
				chainNames.add(temp);
			} else
				chainNames.add(temp);
		} // loop

		splitLastChain(); // split the last chain and add it to the list
	} // end splitChains()

	/*
	 * Split the last chain into its elements
	 */
	private static void splitLastChain() {
		// local variables
		String last = chainNames.get(chainNames.size() - 1);
		int length = 0;

		// try to find attached sidechain and if found, remove the last element within
		// the arraylist and change it to be two elements
		for (int i = 0; i < OrganicUtil.SIDE_CHAIN_SUFFIX.length; i++) {
			length = last.indexOf(OrganicUtil.SIDE_CHAIN_SUFFIX[i]);
			if (length != -1) {
				length += OrganicUtil.SIDE_CHAIN_SUFFIX[i].length();
				chainNames.remove(chainNames.get(chainNames.size() - 1)); // remove last element
				chainNames.add(last.substring(0, length));
				chainNames.add(last.substring(length));
				break;
			} // end if
		} // end for

		// get the last element within the array list and see if it is a cyclo
		last = chainNames.get(chainNames.size() - 1);
		if (last.length() >= 5)
			if (last.substring(0, 5).equalsIgnoreCase(OrganicUtil.PREFIX[9]))
				front = OrganicUtil.PREFIX[9];

		addLocations(); // add the locations of the chains to the locations list
		reListChain();
		// use last chain to determine suffix
		suffix = mainChainEnding(last);

		if (!suffix.equalsIgnoreCase("ane"))
			preSuffix(last);

		// System.out.println(suffix);
		// use last chain to determine if multiple suffixes

	} // end splitLastChain

	private static void reListChain() {
		for (int i = 0; i < chainNames.size() - 1; i++) {
			// loop for the length of the size of the organicUtil array
			for (int j = 0; j < OrganicUtil.PREFIX.length; j++) {
				// temporary variables
				String temp = chainNames.get(i);
				int prefixLength = OrganicUtil.PREFIX[j].length();

				if (temp.length() >= prefixLength) {
					if (temp.substring(0, prefixLength).equalsIgnoreCase(OrganicUtil.PREFIX[j])) {
						if (j != 9) {
							chainNames.remove(i);
							for (int k = 0; k < j + 2; k++)
								chainNames.add(i, temp.substring(prefixLength));
						} // end if

						break;
					} // end if
				} // end if
			} // end for loop
		}
	}

	private static void preSuffix(String mainChain) {
		// check on the last occurrence of this method if the suffix is an single,
		// double or triple bond
		String temp = "";
		for (int i = 0; i < OrganicUtil.CHAIN.length; i++) {
			// temp variables
			int length = OrganicUtil.CHAIN[i].length() - 1;
			int ending;
			String mid;
			boolean found = false;
			boolean firstFound = false;
			int same = -2;

			// if the length of the main chaing is larger than the length of the chain then
			// see if the functional groups have prefixes
			if (mainChain.length() > length) {
				try {
					// setting variables for ease of use
					ending = mainChain.length() - temp.length();
					if (front.equalsIgnoreCase(OrganicUtil.PREFIX[9])) {
						mid = mainChain.substring(length + OrganicUtil.PREFIX[9].length(), ending);
					} else {
						mid = mainChain.substring(length, ending);
					}

					// check for first occurrence of prefix
					if (!firstFound) {
						for (int j = 0; j < OrganicUtil.PREFIX.length; j++) {
							if (mid.length() > OrganicUtil.PREFIX[j].length()) {
								if (mid.indexOf(OrganicUtil.PREFIX[j]) != -1 && i != 9) {
									same = mid.indexOf(OrganicUtil.PREFIX[j]);
									temp = OrganicUtil.PREFIX[j];
									additionalGroups = j + 2;
									// System.out.println(temp);
									firstFound = true;
									break;
								} // end if
							} // end if
						} // end for
					}

					if (mid.length() > 2) {

						// add to the temp depending on what bond type if found
						if (mid.substring(0, 2).equalsIgnoreCase("an")) {
							temp = "an" + temp;
							;
							mainBond = 1;
						} else if (mid.substring(0, 2).equalsIgnoreCase("en")) {
							temp = "en" + temp;
							additionalGroups++;
							chainLocations.add(1);
							mainBond = 2;
						} else if (mid.substring(0, 2).equalsIgnoreCase("yn")) {
							temp = "yn" + temp;
							additionalGroups++;
							chainLocations.add(1);
							mainBond = 3;
						} // end if
						else if (mid.substring(temp.length(), temp.length() + 2).equalsIgnoreCase("an")) {
							temp += "an";
							mainBond = 1;
						} else if (mid.substring(temp.length(), temp.length() + 2).equalsIgnoreCase("en")) {
							temp += "en";
							mainBond = 2;
						} else if (mid.substring(temp.length(), temp.length() + 2).equalsIgnoreCase("yn")) {
							temp += "yn";
							mainBond = 3;
						} // end if
						else
							System.out.println("DIED");
					} // end if
						// System.out.println("AFTER IF");
						// System.out.println(temp); dbg

					// check if the ending has a prefix else return the suffix without any prefixes
					if (!found && mid.length() > 2) {
						for (int j = 0; j < OrganicUtil.PREFIX.length; j++) {

							// System.out.println("---------");
							// System.out.println(mid);
							// System.out.println(suffix); dbg

							// search for secondary prefix on a suffix
							if (mid.length() > OrganicUtil.PREFIX[j].length()) {
								if (mid.lastIndexOf(OrganicUtil.PREFIX[j]) > -1 && i != 9) {
									// if same suffix is found set this loop equal to true and set suffix equal to
									// temp + suffix
									if (mid.lastIndexOf(OrganicUtil.PREFIX[j]) == same) {
										suffix = temp + suffix;
										found = true;
										break;
									} else {
										suffix = temp + OrganicUtil.PREFIX[j] + suffix;
										additionalGroups += j + 2;
										found = true;
										System.out.println("SUFFIX - " + suffix);
										break;
									} // end if
								} // end if
							} // end if
						} // end for

						// break if a prefix is found
						if (found)
							break;
					} else {
						suffix = temp + suffix;
						break;
					} // end if
				} catch (StringIndexOutOfBoundsException e) {
					// System.out.println(suffix);
					if (suffix.equalsIgnoreCase("ane")) {
						additionalGroups = 0;
					} else if (chainLocations.size() - chainNames.size() < 0) {
						chainLocations.add(1);
						additionalGroups = 1;
						// System.out.println(additionalGroups);
					} else if (chainLocations.size() - chainNames.size() == 0) {
						additionalGroups = 1;
					}
					// System.out.println("------------------------------");
					System.out.println("ERROR");
				} // end try catch
			} // end if
		} // end for
	}// end preSuffix

	// method tries to obtain the ending suffix of the mainChain and returns it
	// found
	private static String mainChainEnding(String mainChain) {
		// string to hold the suffix and to be returned at the end
		String ending = "";
		try {
			// based on the error counter at the beginning, determine the type of suffix
			ending = mainChain.substring(mainChain.indexOf(OrganicUtil.MAIN_CHAIN_SUFFIX[error]));
			if (error < 3)
				mainBond = error + 1;

			return ending;
		} catch (StringIndexOutOfBoundsException e) {
			// if error is thrown, add one to the error counter and then run itself again
			error++;
			ending = mainChainEnding(mainChain);
		} // end try catch

		// return the result
		return ending;
	}// end mainChainEnding

	
	// Remove the locations from the chain names list and add it to locations list 
	private static void addLocations() {
		chainLocations = new ArrayList<Integer>();

		for (int i = 0; i < chainNames.size(); i++) {
			if (isStringNumber(chainNames.get(i))) {
				chainLocations.add(Integer.parseInt(chainNames.get(i))); // add it to the locations list
				chainNames.remove(i);
				i--;
			} // if
		} // loop
	} // end addLocation

	// Add the side chains to the compound
	private static void addChains() {
		// loop through the list of the text forms of the chains, convert them and add
		// them
		for (int i = 0; i < chainNames.size() - 1; i++) {
			// temporary variables
			String temp = chainNames.get(i);
			String prefix = "";
			boolean cyclo = false;
			int cycloLength = OrganicUtil.PREFIX[9].length();

			// check if compound has a cyclo
			if (temp.length() > cycloLength) {
				if (temp.substring(0, cycloLength).equalsIgnoreCase(OrganicUtil.PREFIX[9])) {
					cyclo = true;
					prefix = OrganicUtil.PREFIX[9];
				} /// end if
			} // end if

			try {
				// add side chains and any additional sidechains
				compound.addSideChain(chainToNumber(chainNames.get(i), OrganicUtil.ALKYL_SIDE_CHAIN, prefix),
						chainLocations.get(i), cyclo);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("NOT ENOUGH LOCATIONS");
			} // end try catcc
		} // loop
	} // end addChains

	/*
	 * Convert the text form of the chain to a numerical value representing its
	 * length String chain - the text form of the chain to be converted return size
	 * - numerical form of the chain
	 */
	private static int chainToNumber(String chain, String suffix, String prefix) {
		int size = 0;
		// compare the String to each of the main chain Strings and set the size
		// accordingly
		for (int i = 0; i < 10; i++) {
			if (chain.equalsIgnoreCase(prefix + OrganicUtil.CHAIN[i] + suffix)) {
				size = i + 1;
				break;
			} // end if
		} // end for
			// System.out.println("CHAIN TO NUMBER : " + prefix + OrganicUtil.CHAIN[size -
			// 1] + suffix);// dbg
		return size;
	}//end chainToNumber

	// Checks whether a String is a valid number/position
	private static boolean isStringNumber(String str) {
		for (int i = 0; i < OrganicUtil.LOCATIONS.length; i++) {
			if (str.equalsIgnoreCase(OrganicUtil.LOCATIONS[i]))
				return true;
		} // end for
		return false;
	} // end stringIsNumber

} // end Interpreter