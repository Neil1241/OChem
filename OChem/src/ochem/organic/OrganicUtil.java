package ochem.organic;

/*
 * OrganicUtil
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * A static class with constants and methods for creating Compounds
 */

import java.util.*;

public class OrganicUtil {
	// prefixes for all numbers one to ten
	public static final String[] PREFIX = { "di", "tri", "tetra", "penta", "hexa", "hepta", "octa", "nona", "deca",
			"cyclo" };

	// Suffixes
	public static final String[] MAIN_CHAIN_SUFFIX = { "ane", "ene", "yne", "ol", "al", "one", "amine", "amide", "oate",
			"oic acid" };
	public static final String[] SIDE_CHAIN_SUFFIX = { "phenyl", "yl", "bromo", "iodo", "fluro", "chloro", "oxo",
			"hydroxy", "oxy", "amino" };

	// public static final String [] HALOALKYLS = {};

	// name of functional groups
	public static final String[] FUNCTIONAL_NAMES = { "Alkane", "Alkene", "Alkyne", "Alcohol", "Aldehyde", "Ketone",
			"Amine", "Amide", "Ester", "Carboxylic Acid", "Benzene", "Ether" };

	// names of the main chains
	public static final String[] CHAIN = { "meth", "eth", "prop", "but", "pent", "hex", "hept", "oct", "non", "dec",
			"benzene" };

	// numbers 1 thru 9 for side chain location identification
	public static final String[] LOCATIONS = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "N" };
	// private static final String [] LOCATIONO = { "1", "2", "3", "4", "5", "6",
	// "7", "8", "9", "O"};
	// private static final String [] LOCATIONSNO = {"1", "2", "3", "4", "5", "6",
	// "7", "8", "9", "N","O"};

	// method used to generate a random compound
	public static Compound generateRandomCompound() {
		// declare variables
		Compound c;
		int mainSize = 0; // size of main chain
		int bondType = 0; // bond type
		int ending = 0; // ending in reference to the functional names variable
		int prefixBond = 0; // value of the to be prefix of the bond
		int prefixGroup = 0; // value of the to be prefix of the functional group
		int numOfSideChains = 0;
		String[] sideChainType = null;
		int[] bondLocation = null;
		String[] sideLocation = null;
		int[] groupLocation = null;
		boolean benzene = false;
		boolean cyclo = false;

		// generate main chain size
		mainSize = random(2, 10);
		pass();

		// generate type of bond and functional group
		bondType = random(1, 3);
		ending = random(0, FUNCTIONAL_NAMES.length - 1);

		// check for benzene and cyclo and set the main chain accordingly
		if (ending == 10) {
			benzene = true;
			mainSize = 6;
		} else if (mainSize > 2 && mainSize < 7)
			cyclo = cyclo();

		c = new Compound(mainSize);
		c.getMainChain().setBenzene(benzene);
		c.getMainChain().setCyclo(cyclo);

		pass();

		// set bond type if it turns out to be an alkane set to one
		if (ending == 0) {
			bondType = 1;
		} else if (ending == 1) {
			bondType = 2;
		} else if (ending == 2) {
			bondType = 3;
		} // end if
		c.getMainChain().setBond(bondType);

		// generate a prefix if there are double or triple bonds
		if (mainSize == 2) {
			prefixBond = 1;
		}else if (mainSize == 3) {
			prefixBond = random(1, 2);
		}else if (ending != 0) {
			prefixBond = random(1, 3);
		} // end if

		pass();
		// creates random values but with no duplicate values
		if (bondType != 1) {
			bondLocation = new int[prefixBond];
			for (int i = 0; i < prefixBond; i++) {
				System.out.println(mainSize);
				System.out.println(prefixBond);
				int hold = random(1, mainSize - 1);
				for (int j = 0; j < i; j++) {
					while (bondLocation[j] == hold)
						hold = random(1, mainSize - 1);
					j = -1;
				} // end for
				bondLocation[i] = hold;
			} // end for
		} // end if

		pass();

		// if ending position is an amide, acid, ester or aldehyde set the functional
		// location to one
		if (ending == 9 || ending == 8 || ending == 7 || ending == 4) {
			c.getMainChain().addFunctionalLocation("1");
			prefixGroup = 1;
			c.getMainChain().addNumOfGroups(prefixGroup, 1);
			c.getMainChain().setEnding(ending, 1);
		} else if (ending == 10) {
			// set prefix on benzene to be one
			prefixGroup = 1;
			bondLocation = null;
		} else if (ending != 0) {
			// generate a random number between 1 and 3 for a prefix on the functional group
			// and generate a random location for the group
			prefixGroup = random(1, 3);
			groupLocation = new int[prefixGroup];
			for (int i = 0; i < prefixGroup; i++)
				groupLocation[i] = random(1, mainSize);
			// end for
		} // end if

		pass();

		// depending on the bondtype add the locations
		if (bondType > 1) {
			// add locations to mainchain
			if (groupLocation != null && ending > 3) {
				bubbleSort(groupLocation);
				for (int i = 0; i < prefixGroup; i++)
					c.getMainChain().addFunctionalLocation(Integer.toString(groupLocation[i]));
				c.getMainChain().addNumOfGroups(prefixGroup, 1);
				c.getMainChain().setEnding(ending, 1);
			} // end if
			if (bondLocation != null) {
				bubbleSort(bondLocation);
				for (int i = 0; i < prefixBond; i++)
					c.getMainChain().addFunctionalLocation(Integer.toString(bondLocation[i]));
				// end for

				c.getMainChain().addNumOfGroups(prefixBond, 0);
				c.getMainChain().setEnding(bondType - 1, 0);
			}
		} else {
			// add locations to mainchain
			if (groupLocation != null) {
				bubbleSort(groupLocation);
				for (int i = 0; i < prefixGroup; i++)
					c.getMainChain().addFunctionalLocation(Integer.toString(groupLocation[i]));
				c.getMainChain().addNumOfGroups(prefixGroup, 1);
				c.getMainChain().setEnding(ending, 1);
			} // end if

		} // end if

		// add sidechain if needed for esters and ethers
		if (ending == 8 || ending == 11) {
			int length = random(1, 4);
			c.addSideChain(length, "O", cyclo(), false);
		}

		// determine amount of side chains
		numOfSideChains = random(0, 4);
		sideLocation = new String[numOfSideChains];
		sideChainType = new String[numOfSideChains];

		// add the sidechains to the compound
		for (int i = 0; i < numOfSideChains; i++) {
			// temp variables for the loop
			int pre = 0;
			boolean phenyl = false;
			boolean sideCyclo = false;

			sideLocation[i] = location(ending, mainSize);
			sideChainType[i] = SIDE_CHAIN_SUFFIX[random(0, SIDE_CHAIN_SUFFIX.length - 1)];
			if (sideChainType[i].equals("yl")) {
				while (sideLocation[i].equals("1") || sideLocation[i].equals("" + mainSize)) {
					sideLocation[i] = location(ending, mainSize);
				} // end while
				sideCyclo = cyclo();
				if (sideCyclo)
					pre = random(3, mainSize);
				else
					pre = random(1, 4);
				sideChainType[i] = CHAIN[pre] + sideChainType[i];
			} else if (sideChainType[i].equals("phenyl")) {
				pre = 6;
				phenyl = true;
			} else {
				for (int j = 2; j < SIDE_CHAIN_SUFFIX.length; j++) {
					if (sideChainType[i].equals(SIDE_CHAIN_SUFFIX[j])) {
						pre = -j;
						break;
					} // end if
				} // end for
			} // end if
			c.addSideChain(pre, sideLocation[i], sideCyclo, phenyl);
		} // end for

		// output for debugging
		System.out.println(
				"Length " + mainSize + "\nBond " + bondType + "\n" + FUNCTIONAL_NAMES[ending] + "\nBenzene " + benzene);
		System.out.println("Cyclo " + cyclo + "\nPrefixBond " + prefixBond + "\nPrefixGroup " + prefixGroup);

		System.out.println("GroupLocations");
		if (groupLocation != null)
			for (int i = 0; i < groupLocation.length; i++)
				System.out.print(groupLocation[i] + " ");
		System.out.println("\nBondLocations");
		if (bondLocation != null)
			for (int i = 0; i < bondLocation.length; i++)
				System.out.print(bondLocation[i] + " ");
		System.out.println("\nSideChain + SideLocation");
		for (int i = 0; i < sideLocation.length; i++) {
			System.out.println(sideChainType[i] + " " + sideLocation[i]);
		}
		System.out.println("\n------------------------------------------");

		// return the compound
		return c;
	}// end generateRandomCompound

	private static String location(int ending, int mainSize) {
		String sideLocation = "";
		if (ending == 6 || ending == 7) {
			sideLocation = LOCATIONS[random(0, mainSize)];
			if (sideLocation.equals("" + mainSize))
				sideLocation = LOCATIONS[LOCATIONS.length - 1];
		} /*
			 * else if (ending == 8 || ending == 11) { sideLocation =
			 * LOCATIONO[random(0,mainSize)]; if (sideLocation.equals(""+mainSize))
			 * sideLocation = LOCATIONO[LOCATIONO.length-1]; }
			 */else {
			sideLocation = LOCATIONS[random(0, mainSize - 1)];
		}
		return sideLocation;
	}

	// returns true or false whether the a component should be a cyclo or not
	private static boolean cyclo() {
		if (Math.round(Math.random()) == 1)
			return true;
		else
			return false;
		// end if
	}// end cyclo

	// generates a random number within a given range
	private static int random(int lowest, int highest) {
		int range = (highest - lowest) + 1;
		return (int) (Math.random() * range) + lowest;
	}// end random

	private static void pass() {
		System.out.println("PASSED");
	}

	public static void bubbleSort(int[] a) {
		for (int i = a.length - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (a[j] > a[j + 1]) {
					int temp = a[j];
					a[j] = a[j + 1];
					a[j + 1] = temp;
				} // end if
			} // end for
		} // end for
	}// end bubblesort

	// gets the name of the compound from the compound object
	public static String nameFromCompound(Compound c) {
		String name = "";
		name = assignPrefix(c.getSideChains());
		name += mainToName(c.getMainChain());
		return name;
	}// end nameFromCompound

	private static String mainToName(Chain main) {
		String name = "";
		int[] prefixes = main.getNumOfGroups();
		ArrayList<String> endings = main.getEndings();
		ArrayList<String> location = main.getFunctionalLocation();
		String hold = "";

		// add cyclo to the name if the main chain is a cyclo, return benzene if the
		// compound is a benzene
		if (main.isCyclo()) {
			name += "cyclo";
		} else if (main.isBenzene()) {
			return "benzene";
		} // end if

		name += CHAIN[main.getSize() - 1];
		if (prefixes[0] > 1) {
			name += "-";
			for (int i = 0; i < prefixes[0]; i++)
				name += location.get(i + prefixes[1]) + ",";
			name = name.substring(0, name.length() - 1);
			name += "-" + prefixFromNumber(prefixes[0]);
		} else if (prefixes[0] == 1) {
			name += "-" + location.get(location.size() - prefixes[0]) + "-";
		} // end if

		if (main.getBond() == 1)
			name += "an";
		else if (main.getBond() == 2)
			name += "en";
		else
			name += "yn";
		// end if

		if (prefixes[1] > 1) {
			name += "-";
			for (int i = 0; i < prefixes[1]; i++)
				name += location.get(i) + ",";
			name = name.substring(0, name.length() - 1);
			name += "-" + prefixFromNumber(prefixes[1]);
		} else if (prefixes[1] == 1) {
			name += "-" + location.get(0) + "-";
		} // end if

		try {
			hold = endings.get(endings.size() - prefixes[0] - 1);
			hold = hold.substring(0, hold.length() - 4);
			if (hold.equals(FUNCTIONAL_NAMES[0]) || hold.equals(FUNCTIONAL_NAMES[1]) || hold.equals(FUNCTIONAL_NAMES[2])
					|| hold.equals(FUNCTIONAL_NAMES[11]))
				name += "e";
			else {
				for (int i = 3; i < FUNCTIONAL_NAMES.length - 1; i++) {
					if (hold.equalsIgnoreCase(FUNCTIONAL_NAMES[i])) {
						if (hold.equals("Amide") || hold.equals("Ester") || hold.equals("Carboxylic Acid")
								|| hold.equals("Aldehyde"))
							name = name.substring(0, name.length() - 3) + MAIN_CHAIN_SUFFIX[i];
						else
							name += MAIN_CHAIN_SUFFIX[i];
						break;
					} // end if
				} // end for
			} // end if
		} catch (ArrayIndexOutOfBoundsException e) {
			// adds to the name if the compound is an alkane
			name += "e";
		} catch (IndexOutOfBoundsException e) {
		}

		// return the mainchain name
		return name;
	}// end mainToName

	private static String prefixFromNumber(int n) {
		return PREFIX[n - 2];
	}

	private static String assignPrefix(Chain[] side) {
		// declare temporary variables
		String beforeMain = "";
		TreeMap<String, Integer> groups;
		ArrayList<String> toCheck = new ArrayList<String>();
		ArrayList<String> position = new ArrayList<String>();
		ArrayList<Integer> etherSpots = new ArrayList<Integer>();
		groups = initializeMap();

		// loop for the length of the side chain array to get the word and location
		for (int i = 0; i < side.length; i++) {
			toCheck.add(sizeToWord(side[i]));
			position.add(side[i].getLocation());
			if (side[i].getLocation().equalsIgnoreCase("o"))
				etherSpots.add(i);
		} // end for
		
		for (int j=0;j<toCheck.size();j++) {
			if (toCheck.get(j).equalsIgnoreCase("oxy")) {
				for (int i=0;i<position.size();i++) {
					if (position.get(i).equalsIgnoreCase("o")) {
						toCheck.set(j, toCheck.get(i).substring(0,toCheck.get(i).length()-2));
						toCheck.remove(i);
						j--;
					}
				}
			}
				
		}

		// uses the treemap key of the sidechain name to be added to the value which
		// starts at 0
		for (int i = 0; i < toCheck.size(); i++) {
			try {
			int temp = groups.get(toCheck.get(i)) + 1;
			if (!position.get(i).equalsIgnoreCase("o"))
				groups.replace(toCheck.get(i), temp);
			
			}catch(NullPointerException e) {}
		} // end for
		
		for (String key: groups.keySet()){
			int prefix = groups.get(key);
			if (prefix!=0) {
				for (int i=0;i<toCheck.size();i++) {
					if (key.equals(toCheck.get(i))) {
						if (!position.get(i).equalsIgnoreCase("o"))
							beforeMain+=position.get(i)+",";
					}//end if
				}//end for
				beforeMain = beforeMain.substring(0,beforeMain.length()-1) + "-";
				if (prefix>=2)
					beforeMain+=prefixFromNumber(prefix);
				beforeMain += key+"-"; 
			}//end if
		}//end for
		if (side.length>0)
			beforeMain = beforeMain.substring(0,beforeMain.length()-1);
		return beforeMain;
	}// end assignPrefix

	//creates a treemap and sets all the values to 0
	private static TreeMap<String, Integer> initializeMap() {
		TreeMap<String, Integer> t = new TreeMap<String, Integer>();

		// add in the alkyl side chains
		for (int i = 0; i < CHAIN.length - 1; i++)
			t.put(CHAIN[i] + "yl", 0);
		// end for

		// add in the side unique side chains
		for (int i = 0; i < SIDE_CHAIN_SUFFIX.length; i++) {
			if (i != 1)
				t.put(SIDE_CHAIN_SUFFIX[i], 0);
		} // end for

		// add in the cyclo side chains
		for (int i = 2; i < CHAIN.length - 3; i++)
			t.put("cyclo" + CHAIN[i] + "yl", 0);

		return t;
	}

	private static String sizeToWord(Chain s) {
		int size = s.getSize();
		String toReturn = "";
		if (size > 0) {
			if (size == 6 && s.isBenzene())
				toReturn = "phenyl";
			else if (s.isCyclo())
				toReturn = "cyclo";
			toReturn += CHAIN[size - 1] + "yl";
		} else {
			toReturn = SIDE_CHAIN_SUFFIX[size * -1];
		} // end if
		return toReturn;
	}// end sizeToWord

	// main for testing purposes
	public static void main(String[] args) {
		Compound c = generateRandomCompound();
		System.out.println(c.toString());
		System.out.println(nameFromCompound(c));
	}

} // end OrganicUtil