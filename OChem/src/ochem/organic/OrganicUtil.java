package ochem.organic;

/*
 * OrganicUtil
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * A static class with constants and methods for creating Compounds
 */

public class OrganicUtil {

	// prefixes for all numbers one to ten
	public static final String[] PREFIX = { "di", "tri", "tetra", "penta", "hexa", "hepta", "octa", "nona", "deca",
			"cyclo" };

	// Suffixes
	public static final String[] MAIN_CHAIN_SUFFIX = { "ane", "ene", "yne", "ol", "al", "one", "amine", "amide", "oate",
			"oic acid" };
	public static final String[] SIDE_CHAIN_SUFFIX = { "phenyl", "yl", "bromo", "iodo", "fluro", "chloro", "oxo",
			"hydroxy", "oxy", "amino" };
	//private static final String[] ALKYL = {"methyl","ethyl","propyl","butyl"};

	// public static final String [] HALOALKYLS = {};

	// name of functional groups
	public static final String[] FUNCTIONAL_NAMES = { "Alkane", "Alkene", "Alkyne", "Alcohol", "Aldehyde", "Ketone",
			"Amine", "Amide", "Ester", "Carboxylic Acid", "Benzene", "Ether" };

	// names of the main chains
	public static final String[] CHAIN = { "meth", "eth", "prop", "but", "pent", "hex", "hept", "oct", "non", "dec",
			"benzene" };

	// numbers 1 thru 9 for side chain location identification
	public static final String[] LOCATIONS = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "N" };

	// method used to generate a random compound
	public static Compound generateRandomCompound() {
		// declare variables
		Compound c;
		int mainSize = 0; // size of main chaing
		int bondType = 0; // bond type
		int ending = 0; // ending in reference to the functional names variable
		int prefixBond = 0; // value of the to be prefix of the bond
		int prefixGroup = 0; // value of the to be prefix of the functional group
		int numOfSideChains = 0;
		String[] sideChainType = null;
		int[] bondLocation = null;
		int[] sideLocation = null;
		int[] groupLocation = null;
		boolean benzene = false;
		boolean cyclo = false;

		// generate main chain size and create the compound
		mainSize = random(2, 10);
		c = new Compound(mainSize);

		// generate type of bond and functional group
		bondType = random(1, 3);
		ending = random(0, FUNCTIONAL_NAMES.length - 1);

		// check for benzene and cyclo and set the main chain accordingly
		if (ending == 9)
			benzene = true;
		else if (mainSize > 2)
			cyclo = cyclo();
		c.getMainChain().setBenzene(benzene);
		c.getMainChain().setCyclo(cyclo);

		// set bond type if it turns out to be an alkane set to one
		if (ending == 0)
			bondType = 1;
		else
			prefixBond = random(0, mainSize);
		c.getMainChain().setBond(bondType);
		bondLocation = new int[prefixBond];

		// creates random values but with no duplicate values
		if (bondType != 1) {
			for (int i = 0; i < prefixBond; i++) {
				int hold = random(1, mainSize - 1);
				boolean b = false;
				for (int j = 0; j < i; j++) {
					if (bondLocation[j] == hold) {
						b = true;
						break;
					} // end if
				} // end for

				if (!b)
					bondLocation[i] = hold;
				else
					i--;
			} // end for
		} // end if

		// Check Endings
		if (ending == 9 || ending == 8 || ending == 7 || ending == 4) {
			c.getMainChain().addFunctionalLocation("1");
			prefixGroup = 1;
		} else if (ending == 9) {
			prefixGroup = 1;
			bondLocation = null;
		} else if (ending != 0) {
			prefixGroup = random(1, 10);
			groupLocation = new int[prefixGroup];
			for (int i = 0; i < prefixGroup; i++)
				groupLocation[i] = random(1, mainSize);
			// end for
		} // end if

		// add locations to mainchain
		if (groupLocation != null) {
			bubbleSort(groupLocation);
			for (int i = 0; i < prefixGroup; i++)
				c.getMainChain().addFunctionalLocation(Integer.toString(groupLocation[i]));
		}
		if (bondLocation != null) {
			bubbleSort(bondLocation);
			for (int i = 0; i < prefixBond; i++)
				c.getMainChain().addFunctionalLocation(Integer.toString(bondLocation[i]));
		}
		// determine amount of side chains
		numOfSideChains = random(0, mainSize);
		sideLocation = new int[numOfSideChains];
		sideChainType = new String[numOfSideChains];
		for (int i = 0; i < numOfSideChains; i++) {
			int pre = 0;
			sideLocation[i] = random(1, mainSize);
			sideChainType[i] = SIDE_CHAIN_SUFFIX[random(0, SIDE_CHAIN_SUFFIX.length - 1)];
			if (sideChainType[i].equals("yl")) {
				pre = random(1,4);
				sideChainType[i] = CHAIN[pre] + sideChainType[i];
			}
			c.addSideChain(pre, Integer.toString(sideLocation[i]), false, false);
		}

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
		for (int i=0;i<sideLocation.length;i++) {
			System.out.println(sideChainType[i]+" "+sideLocation[i]);
		}
		System.out.println("\n------------------------------------------");

		// return the compound
		return c;
	}// end generateRandomCompound

	// returns true or false whether the a componenet should be a cyclo or not
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
} // end OrganicUtil
