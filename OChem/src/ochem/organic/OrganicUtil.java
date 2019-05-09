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
	public static final String[] PREFIX= {"di","tri","tetra","penta","hexa","hepta","octa","nona","deca","cyclo"};
	/*public static final String TWO_PREFIX = "di";
	public static final String THREE_PREFIX = "tri";
	public static final String FOUR_PREFIX = "tetra";
	public static final String FIVE_PREFIX = "penta";
	public static final String SIX_PREFIX = "hexa";
	public static final String SEVEN_PREFIX = "hepta";
	public static final String EIGHT_PREFIX = "octa";
	public static final String NINE_PREFIX = "nona";
	public static final String TEN_PREFIX = "deca"; */
	
	//Suffixes
	public static final String[] SUFFIX= {"ane","ene","yne","al","one","ol","amide","amine","oxy","oate","oic acid"};
	public static final String ALKANE="ane";
	public static final String ALKENE="ene";
	public static final String ALKYNE="yne";
	public static final String[] HALOGENS= {"bromo","iodo","fluro","chloro","oxo"};
	public static final String ALDEHYDE="al";
	public static final String KETONE="one";
	public static final String ALCOHOL="ol";
	public static final String AMIDE="amide";
	public static final String AMINE="amine";
	public static final String ESTER="oate";
	public static final String ETHER="oxy";
	public static final String CARBOXYLIC_ACID="oic acid";
	
	//names of the main chains
	public static final String[] CHAIN= {"meth","eth","prop","but","pent","hex","hepta","oct","non","dec"};
	/*public static final String ONE_CHAIN = "meth";
	public static final String TWO_CHAIN = "eth";
	public static final String THREE_CHAIN = "prop";
	public static final String FOUR_CHAIN = "but";
	public static final String FIVE_CHAIN = "pent";
	public static final String SIX_CHAIN = "hex";
	public static final String SEVEN_CHAIN = "hept";
	public static final String EIGHT_CHAIN = "oct";
	public static final String NINE_CHAIN = "non";
	public static final String TEN_CHAIN = "dec"; */
	
	//numbers 1 thru 9 for side chain location identification
	public static final String NUMBERS = "123456789";
} //end OrganicUtil
