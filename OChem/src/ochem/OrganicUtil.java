package ochem;

/*
 * OrganicUtil
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * A static class with constants and methods for creating Compounds
 * TO-DO: Add double and triple bonds
 * 	 	  Add functional groups
 */

public class OrganicUtil {
	//"yl" signifies an alkyl side chain
	public static final String ALKYL_SIDE_CHAIN = "yl";
	
	//prefixes for all numbers one to ten
	public static final String TWO_PREFIX = "di";
	public static final String THREE_PREFIX = "tri";
	public static final String FOUR_PREFIX = "tetra";
	public static final String FIVE_PREFIX = "penta";
	public static final String SIX_PREFIX = "hexa";
	public static final String SEVEN_PREFIX = "hepta";
	public static final String EIGHT_PREFIX = "octa";
	public static final String NINE_PREFIX = "nona";
	public static final String TEN_PREFIX = "deca";
	
	//names of the main chains
	public static final String ONE_CHAIN = "methane";
	public static final String TWO_CHAIN = "ethane";
	public static final String THREE_CHAIN = "propane";
	public static final String FOUR_CHAIN = "butane";
	public static final String FIVE_CHAIN = "pentane";
	public static final String SIX_CHAIN = "hexane";
	public static final String SEVEN_CHAIN = "heptane";
	public static final String EIGHT_CHAIN = "octane";
	public static final String NINE_CHAIN = "nonane";
	public static final String TEN_CHAIN = "decane";
	
	//numbers 1 thru 9 for side chain location identification
	public static final String NUMBERS = "123456789";
} //end OrganicUtil
