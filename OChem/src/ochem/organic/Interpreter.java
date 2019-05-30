package ochem.organic;

/*
 * Interpreter
 * Created by: Neil Balaskandarajah
 * Everything else: Jordan
 * Last modified: 05/07/2019
 * Converts text names of compounds into compound objects
 * TODO: fix the way locations are added
 */

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Interpreter {

	// Attributes
	private static StringTokenizer compoundName; // name of the compound
	private static Compound compound; // compound to be created
	private static String suffix = ""; // compound suffix made more general for future use
	private static String delimit; // holds the delimited version of the orignal name
	private static int mainBond; // holds the type of bond
	private static ArrayList<Integer> endingPosition = new ArrayList<Integer>();
	private static int [] numOfGroups = new int[2];
	private static String front = ""; // the prefix of the main chain
	private static ArrayList<String> chainNames; // all the side chains
	private static ArrayList<String> chainLocations; // locations of the side chains
	private static String originalName; // original name of String
	private static int additionalGroups = 0; // counter to see how many groups are on the main chain
	private static boolean ester; // boolean to determine if the compound is an ester
	private static boolean benzene;

	/*
	 * Create a Compound object from a text name String name - name of the compound
	 * as text return compound - compound object from Compound
	 */
	public static Compound compoundFromName(String name) {
		originalName = name;

		splitChains(); // remove all the hyphens in the String

		System.out.println("----------------ELEMENTS-----------------");
		// print out the elements in the array
		for (int i = 0; i < chainNames.size(); i++)
			System.out.println(chainNames.get(i)); // dbg
		System.out.println("-----------------------------------------");

		// create compound and set if the main chain is a cyclo
		compound = new Compound(chainToNumber(chainNames.get(chainNames.size() - 1), suffix, front));
		if (front.equalsIgnoreCase(OrganicUtil.PREFIX[9]))
			compound.getMainChain().setCyclo(true);
		// end if

		if (benzene)
			compound.getMainChain().setBenzene(true);
		// end if

		// for (String n : chainLocations)
		// System.out.println(n);// dbg
		// System.out.println("-----------------" + additionalGroups +
		// "-----------------------");

		// set the mainchain bond type and get their locations
		compound.getMainChain().setBond(mainBond);
		for (int i = chainLocations.size() - additionalGroups; i < chainLocations.size(); i++) {
			compound.addFunctionalLocation(chainLocations.get(i));
		}

		// add sidechains
		addChains();

		// System.out.println("--------------------");
		// for (Integer n : numOfGroups)
		// System.out.println(n);//dbg
		compound.getMainChain().setNumOfGroups(numOfGroups);

		// add to the main chains arraylist
		for (Integer i : endingPosition) {
			compound.getMainChain().setEnding(i);
		}

		return compound;
	} // end compoundFromName

	// Split the text form of the name into the side chains and locations
	private static void splitChains() {
		// delimit by the hyphen, commas and spaces. and add it to the list
		compoundName = new StringTokenizer(originalName, " -,"); // Create StringTokenizer for ease of manipulation
		chainNames = new ArrayList<String>();

		// temporary variable
		String temp = ""; // holds the next token
		// int tokens = 0;
		// loop while there are more tokens
		while (compoundName.hasMoreTokens()) {
			temp = compoundName.nextToken();
			// tokens++;
			delimit += temp;

			// if the token says acid append it with the last token
			if (temp.equalsIgnoreCase("acid")) {
				temp = chainNames.get(chainNames.size() - 1) + " " + temp;
				chainNames.remove(chainNames.remove(chainNames.size() - 1));
				chainNames.add(temp);
			} /*
				 * else if (!compoundName.hasMoreTokens() && tokens>2) { boolean leave = false;
				 * for (int i = 0; i < OrganicUtil.MAIN_CHAIN_SUFFIX.length - 1; i++) { for (int
				 * j = 0; j < OrganicUtil.PREFIX.length; j++) { if
				 * (temp.equalsIgnoreCase(OrganicUtil.PREFIX[j] +
				 * OrganicUtil.MAIN_CHAIN_SUFFIX[i])) { int backwards = 1; while
				 * (Character.isDigit(chainNames.get(chainNames.size() - backwards).charAt(0)))
				 * backwards++; // end while
				 * 
				 * temp = chainNames.get(chainNames.size() - backwards) + temp;
				 * chainNames.remove(chainNames.size() - backwards); chainNames.add(temp); leave
				 * = true; break; } // end if } // end for if (leave) break; } // end for
				 * 
				 * }
				 */ else {
				chainNames.add(temp);
			}
		} // loop
		delimit = delimit.substring(4);
		splitLastChain(); // split the last chain and add it to the list
	} // end splitChains()

	// Split the last chain into its elements
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
		if (last.length() >= 7)
			if (last.substring(0, 7).equalsIgnoreCase(OrganicUtil.CHAIN[10]))
				benzene = true;

		addLocations(); // add the locations of the chains to the locations list
		reListChain(); // reformat the list

		// use last chain to determine suffix
		suffix = mainChainEnding(last);

		if (!suffix.equalsIgnoreCase("ane"))
			preSuffix(last);

		System.out.println(suffix);// dbg

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
		} // end for
	}// end relistchain

	// check if any of the suffixes have prefixes
	private static void preSuffix(String mainChain) {
		// check on the last occurrence of this method if the suffix is an single,
		// double or triple bond
		String test = "";
		String mid = "";
		String hold = "";
		int chain = 0;

		// determine chain of mainchain but not including the ending
		for (int i = 0; i < OrganicUtil.CHAIN.length; i++) {
			try {
				if (front.equalsIgnoreCase(OrganicUtil.PREFIX[9])) {
					if (mainChain.substring(0, OrganicUtil.CHAIN[i].length() + 5)
							.equalsIgnoreCase("cyclo" + OrganicUtil.CHAIN[i])) {
						chain = OrganicUtil.CHAIN[i].length() + 5;
					}
				} else if (mainChain.substring(0, OrganicUtil.CHAIN[i].length())
						.equalsIgnoreCase(OrganicUtil.CHAIN[i])) {
					chain = OrganicUtil.CHAIN[i].length();
				}
			} catch (StringIndexOutOfBoundsException e) {
			}
		} // end for

		// substring the variable mid to hold the mainChain without the prefix
		mid = mainChain.substring(chain);
		System.out.println(mid); // dbg
		suffix = mid;

		// check if mid has a prefix on the suffix and if it does set it to itself
		// without the prefix
		mid = checkPrefix(mid,0);

		// gets digits if there are any
		try {
			test = delimit.substring(0, delimit.length() - mainChain.length());
			int idx = test.length() - 1;
			/*while (!Character.isDigit(test.charAt(idx)))
				idx--;*/

			while (Character.isDigit(test.charAt(idx))) {
				hold = test.charAt(idx) + hold;
				idx--;
			} // end while
		} catch (StringIndexOutOfBoundsException e) {
		} // end try catch

		// used to add any missing locations on the first suffix
		while (additionalGroups > hold.length()) {
			hold = "1" + hold;
			chainLocations.add(0, "1");
		}

		System.out.println("---" + test + "---" + hold);
		try {
			// check bond type and add locations if needed
			if (mid.substring(0, 2).equalsIgnoreCase("an")) {
				mainBond = 1;
				endingPosition.add(0,0);

			} else if (mid.substring(0, 2).equalsIgnoreCase("en")) {
				mainBond = 2;
				endingPosition.add(0, 1);
			} else if (mid.substring(0, 2).equalsIgnoreCase("yn")) {
				mainBond = 3;
				endingPosition.add(0, 2);
			} else
				System.out.println("BREAK");// dbg
			// end if

		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("DIE");
		} // end try catch

		// check to see if the second part of the suffix has a prefix
		try {
			mid = mid.substring(2);
			System.out.println("--------------MID--------------\n" + mid); // dbg
			mid = checkPrefix(mid,1);
			System.out.println(mid); // dbg

		} catch (StringIndexOutOfBoundsException e) {

		} // end try catch

		// used to add any missing locations on the second suffix
		while (additionalGroups > hold.length()) {
			hold += "1";
			chainLocations.add("1");
		}
	}// end preSuffix

	// checks for the prefix of a given name
	private static String checkPrefix(String mid, int timeRan) {
		for (int i = 0; i < OrganicUtil.PREFIX.length; i++) {
			try {
				if (OrganicUtil.PREFIX[i].equalsIgnoreCase(mid.substring(0, OrganicUtil.PREFIX[i].length()))) {
					additionalGroups += i + 2;
					numOfGroups[timeRan] = (i + 2);
					mid = mid.substring(OrganicUtil.PREFIX[i].length());
					return mid;
				} // end if
			} catch (StringIndexOutOfBoundsException e) {
				// System.out.println("CAUGHT"); //dbg
			}
		} // end for
		System.out.println("CHECK PREFIX : " + mid);
		if (mid.length() >= 2)
			if (!mid.substring(0, 2).equalsIgnoreCase("an")) {
				additionalGroups++;
				numOfGroups[timeRan] = 1;
			}
		return mid;
	}

	// method tries to obtain the ending suffix of the mainChain and returns it
	// found
	private static String mainChainEnding(String mainChain) {
		// string to hold the suffix and to be returned at the end
		String ending = "";
		for (int i = 0; i < OrganicUtil.MAIN_CHAIN_SUFFIX.length; i++) {
			try {
				// based on the error counter at the beginning, determine the type of suffix
				ending = mainChain.substring(mainChain.indexOf(OrganicUtil.MAIN_CHAIN_SUFFIX[i]));
				endingPosition.add(i);
				if (ending.equalsIgnoreCase("oate"))
					ester = true;

				if (i < 3)
					mainBond = i + 1;
				return ending;
			} catch (StringIndexOutOfBoundsException e) {
			} // end try catch
		} // end for

		// return the result
		return ending;
	}// end mainChainEnding

	// Remove the locations from the chain names list and add it to locations list
	private static void addLocations() {
		chainLocations = new ArrayList<String>();

		for (int i = 0; i < chainNames.size(); i++) {
			if (isStringNumber(chainNames.get(i))) {
				chainLocations.add(chainNames.get(i)); // add it to the locations list
				chainNames.remove(i);
				i--;
			} else if (chainNames.get(i).length() == 1) {
				throw new IllegalArgumentException("Not a valid location");
			} // if
		} // loop
	} // end addLocation

	// Add the side chains to the compound
	private static void addChains() {
		// temp variable
		int idx = 0;

		// loop through the list of the text forms of the chains, convert them and add
		// them
		for (int i = 0; i < chainNames.size() - 1; i++) {
			// temporary variables
			String temp = chainNames.get(i);
			String prefix = "";
			int spot = -8;
			boolean cyclo = false;
			boolean benzene = false;
			int cycloLength = OrganicUtil.PREFIX[9].length();
			int phenylLength = 6;

			// check if compound has a cyclo
			if (temp.length() > cycloLength) {
				if (temp.substring(0, cycloLength).equalsIgnoreCase(OrganicUtil.PREFIX[9])) {
					cyclo = true;
					prefix = OrganicUtil.PREFIX[9];
				} /// end if
			} // end if

			if (temp.length() >= phenylLength)
				if (temp.substring(0, phenylLength).equalsIgnoreCase(OrganicUtil.SIDE_CHAIN_SUFFIX[0]))
					benzene = true;

			// determine the suffix of the side chain
			for (int j = 0; j < OrganicUtil.SIDE_CHAIN_SUFFIX.length; j++) {
				if (temp.indexOf(OrganicUtil.SIDE_CHAIN_SUFFIX[j]) != -1) {
					spot = j;
					break;
				} // end if
			} // end for

			// Check if the compound is an ester or an ether or none to add side chains
			// accordingly
			if (OrganicUtil.SIDE_CHAIN_SUFFIX[spot].equalsIgnoreCase("oxy")) {
				if (!Character.isDigit(delimit.charAt(0))) {
					ether(chainNames.get(i), prefix, cyclo, "1");
				} else {
					ether(chainNames.get(i), prefix, cyclo, chainLocations.get(idx));
					idx++;
				}
				endingPosition.add(11);
			} else if (ester && i == 0) {
				compound.addSideChain(chainToNumber(chainNames.get(i), OrganicUtil.SIDE_CHAIN_SUFFIX[spot], prefix),
						"O", cyclo, benzene);
			} else {
				try {
					// add side chains and any additional sidechains
					compound.addSideChain(chainToNumber(chainNames.get(i), OrganicUtil.SIDE_CHAIN_SUFFIX[spot], prefix),
							chainLocations.get(idx), cyclo, benzene);
					idx++;
				} catch (IndexOutOfBoundsException e) {
					System.out.println("CATCH");
					idx++;
				} // end try catch
			} // end if
		} // loop
	} // end addChains

	// adds splits up ethers to be an oxy group and an alkyl group
	private static void ether(String chainName, String prefix, boolean cyclo, String location) {
		String chain = chainName.substring(0, chainName.length() - 3);
		compound.addSideChain(chainToNumber(chain + "yl", "yl", prefix), "O", cyclo, false);
		compound.addSideChain(-8, location, false, false);
	}

	/*
	 * Convert the text form of the chain to a numerical value representing its
	 * length String chain - the text form of the chain to be converted return size
	 * - numerical form of the chain
	 */
	private static int chainToNumber(String chain, String suffix, String prefix) {
		// temporary variable
		int size = 0;

		// compare the String to each of the main chain Strings and set the size
		// accordingly
		System.out.println(chain);
		for (int i = 0; i < 10; i++) {
			if (chain.equalsIgnoreCase(prefix + OrganicUtil.CHAIN[i] + suffix)) {
				size = i + 1;
				break;
			} else if (chain.equalsIgnoreCase(OrganicUtil.CHAIN[10])) {
				size = 6;
				endingPosition.add(10);
				break;
			} // end if
		} // end for

		// checks if the suffix is phenyl to determine the size, otherwise set the size
		// to 1
		for (int i = 0; i < OrganicUtil.SIDE_CHAIN_SUFFIX.length; i++) {
			if (chain.equalsIgnoreCase(prefix + "phenyl")) {
				size = 6;
				break;
			} else if (chain.equalsIgnoreCase(prefix + OrganicUtil.SIDE_CHAIN_SUFFIX[i])) {
				size = -i;
				break;
			} // end if
		}

		System.out.println("CHAIN TO NUMBER : " + prefix + suffix);// dbg
		return size;
	}// end chainToNumber

	// Checks whether a String is a valid number/position
	private static boolean isStringNumber(String str) {
		for (int i = 0; i < OrganicUtil.LOCATIONS.length; i++) {
			if (str.equalsIgnoreCase(OrganicUtil.LOCATIONS[i]))
				return true;
		} // end for
		return false;
	} // end stringIsNumber

} // end Interpreter