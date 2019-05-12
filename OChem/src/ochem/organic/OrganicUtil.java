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
	
	//Suffixes
	public static final String[] MAIN_CHAIN_SUFFIX= {"ane","ene","yne","ol","al","one","amide","amine","oate","oic acid"};
	public static final String[] SIDE_CHAIN_SUFFIX= {"yl","bromo","iodo","fluro","chloro","oxo","hydroxy","oxy","amino","phenyl"};

	
	//names of the main chains
	public static final String[] CHAIN= {"meth","eth","prop","but","pent","hex","hept","oct","non","dec","benzene"};

	
	//numbers 1 thru 9 for side chain location identification
	public static final String[] LOCATIONS = {"1","2","3","4","5","6","7","8","9","N"};
} //end OrganicUtil
