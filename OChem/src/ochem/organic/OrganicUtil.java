package ochem.organic;

/*
 * OrganicUtil
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * A static class with constants and methods for creating Compounds
 * TO-DO: Add double and triple bonds
 * 	 	  Add functional groups
 */

public class OrganicUtil {

	// prefixes for all numbers one to ten
	public static final String[] PREFIX = { "di", "tri", "tetra", "penta", "hexa", "hepta", "octa", "nona", "deca",
			"cyclo" };

	// Suffixes
	public static final String[] MAIN_CHAIN_SUFFIX = { "ane", "ene", "yne", "ol", "al", "one", "amide", "amine", "oate",
			"oic acid" };
	public static final String[] SIDE_CHAIN_SUFFIX = { "phenyl", "yl", "bromo", "iodo", "fluro", "chloro", "oxo",
			"hydroxy", "oxy", "amino" };

	//name of functional groups
	public static final String[] FUNCTIONAL_NAMES = {"Alkene","Alkyne","Alcohol","Amine","Ether","Ester","Aldehyde","ketone","Carboxylic Acid","Benzene"};
	
	// names of the main chains
	public static final String[] CHAIN = { "meth", "eth", "prop", "but", "pent", "hex", "hept", "oct", "non", "dec",
			"benzene" };

	// numbers 1 thru 9 for side chain location identification
	public static final String[] LOCATIONS = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "N" };
} // end OrganicUtil
