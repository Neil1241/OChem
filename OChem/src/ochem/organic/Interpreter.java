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
	//Attributes
	private static StringTokenizer compoundName; //name of the compound
	private static Compound compound; //compound to be created
	private static String suffix=""; //compound suffix made more general for future use
	private static int error=0; //counter to use for checking suffix
	
	private static ArrayList<String> chainNames; //all the side chains
	private static ArrayList<Integer> chainLocations; //locations of the side chains
	
	private static String originalName; //original name of String
	
	/*
	 * Create a Compound object from a text name
	 * String name - name of the compound as text
	 * return compound - compound object from Compound
	 */
	public static Compound compoundFromName(String name) {
		originalName = name;
		
		splitChains(); //remove all the hyphens in the String
		addLocations(); //add the locations of the chains to the locations list
		
		//print out the elements in the array
		/*for(int i = 0; i < chainNames.size(); i++) {
			System.out.println(chainNames.get(i)); //dbg
		}*/
		
		compound=new Compound(mainChainToNumber(chainNames.get(chainNames.size()-1),suffix));
		addChains();
		
		
		return compound;
	} //end compoundFromName
	
	/*
	 * Split the text form of the name into the side chains and locations
	 */
	private static void splitChains() {		
		compoundName = new StringTokenizer(originalName,"-,"); //Create StringTokenizer for ease of manipulation
		
		chainNames = new ArrayList<String>();
		
		while (compoundName.hasMoreTokens()) { //delimit by the hyphen and add it to the list
			chainNames.add(compoundName.nextToken());
		} //loop
		
		splitLastChain(); //split the last chain and add it to the list
	} //end splitChains()
	
	/*
	 * Split the last chain into its elements
	 */
	private static void splitLastChain() {
		String last=chainNames.get(chainNames.size() - 1);
		int length=0;
		chainNames.remove(chainNames.get(chainNames.size() - 1)); //remove last element
		length=last.indexOf(OrganicUtil.ALKYL_SIDE_CHAIN);
		
		
		//try to find attached sidechain, if it fails just add the entire compound
		if (length != -1) {
			length += 2;
			chainNames.add(last.substring(0, length));
			chainNames.add(last.substring(length));
		} 
		else {
			chainNames.add(last);
		}
		
		//create the last chain to remove the yl
		String mainChain = chainNames.get(chainNames.size() - 1);
		
		suffix=ending(mainChain);
		chainNames.remove(chainNames.size() - 1); //remove last element
		
		chainNames.add(mainChain);
	} //end splitLastChain
	
	private static String ending(String mainChain)
	{
		//string to hold the suffix and to be returned at the end
		String ending="";
		try
		{
			//based on the error counter at the beginning, determine the type of suffix
			if (error==0)
				ending=mainChain.substring(mainChain.indexOf(OrganicUtil.ALKANE));
			else if (error==1)
				ending=mainChain.substring(mainChain.indexOf(OrganicUtil.ALKENE));
			else if (error==2)
				ending=mainChain.substring(mainChain.indexOf(OrganicUtil.ALKYNE));
			//end if
			return ending;
		}
		catch (StringIndexOutOfBoundsException e)
		{ 
			//if error is thrown, add one to the error counter and then run itself again
			error++;
			ending=ending(mainChain);
			return ending;
		}
	}//end ending
	
	/*
	 * Remove the locations from the chain names list and add it to locations list
	 */
	private static void addLocations() {
		chainLocations = new ArrayList<Integer>();
		
		for (int i = 0; i < chainNames.size(); i++) {
			if (isStringNumber(chainNames.get(i))) {
				chainLocations.add(Integer.parseInt(chainNames.get(i))); //add it to the locations list
				chainNames.remove(i);
			} //if
		} //loop
		
	} //end addLocation
	

	//Add the side chains to the compound
	private static void addChains() {
		//loop through the list of the text forms of the chains, convert them and add them
		for (int i = 0; i < chainLocations.size(); i++) {
			compound.addSideChain(chainToNumber(chainNames.get(i)),chainLocations.get(i));
		} //loop
		
	} //end addChains
	
	/*
	 * Convert the text form of the chain to a numerical value representing its length
	 * String chain - the text form of the chain to be converted
	 * return size - numerical form of the chain
	 */
	private static int chainToNumber(String chain) {
		int size = 0;
		
		//compare the String to each of the main chain Strings and set the size accordingly
		if (chain.equals(OrganicUtil.ONE_CHAIN+OrganicUtil.ALKYL_SIDE_CHAIN)) { //meth
			size = 1;
			
		}  else if (chain.equals(OrganicUtil.TWO_CHAIN+OrganicUtil.ALKYL_SIDE_CHAIN)) { //eth
			size = 2;
			
		} else if (chain.equals(OrganicUtil.THREE_CHAIN+OrganicUtil.ALKYL_SIDE_CHAIN)) { //prop
			size = 3;
			
		} else if (chain.equals(OrganicUtil.FOUR_CHAIN+OrganicUtil.ALKYL_SIDE_CHAIN)) { //but
			size = 4;
			
		} else if (chain.equals(OrganicUtil.FIVE_CHAIN+OrganicUtil.ALKYL_SIDE_CHAIN)) { //pent
			size = 5;
				
		} else if (chain.equals(OrganicUtil.SIX_CHAIN+OrganicUtil.ALKYL_SIDE_CHAIN)) { //hex
			size = 6;
			
		} else if (chain.equals(OrganicUtil.SEVEN_CHAIN+OrganicUtil.ALKYL_SIDE_CHAIN)) { //hept
			size = 7;
			
		} else if (chain.equals(OrganicUtil.EIGHT_CHAIN+OrganicUtil.ALKYL_SIDE_CHAIN)) { //oct
			size = 8;
			
		} else if (chain.equals(OrganicUtil.NINE_CHAIN+OrganicUtil.ALKYL_SIDE_CHAIN)) { //non
			size = 9;
			
		} else if (chain.equals(OrganicUtil.TEN_CHAIN+OrganicUtil.ALKYL_SIDE_CHAIN)) { //dec
			size = 10;
		} //(gigantic) if
		
		return size;
	} //end chainToNumber
	
	
	private static int mainChainToNumber(String chain, String suffix) {
		int size = 0;

		// compare the String to each of the main chain Strings and set the size
		// accordingly
		if (chain.equals(OrganicUtil.ONE_CHAIN + suffix)) { // methane
			size = 1;

		} else if (chain.equals(OrganicUtil.TWO_CHAIN + suffix)) { // ethane
			size = 2;

		} else if (chain.equals(OrganicUtil.THREE_CHAIN + suffix)) { // propane
			size = 3;

		} else if (chain.equals(OrganicUtil.FOUR_CHAIN + suffix)) { // butane
			size = 4;

		} else if (chain.equals(OrganicUtil.FIVE_CHAIN + suffix)) { // pentane
			size = 5;

		} else if (chain.equals(OrganicUtil.SIX_CHAIN + suffix)) { // hexane
			size = 6;

		} else if (chain.equals(OrganicUtil.SEVEN_CHAIN + suffix)) { // heptane
			size = 7;

		} else if (chain.equals(OrganicUtil.EIGHT_CHAIN + suffix)) { // octane
			size = 8;

		} else if (chain.equals(OrganicUtil.NINE_CHAIN + suffix)) { // nonane
			size = 9;

		} else if (chain.equals(OrganicUtil.TEN_CHAIN+suffix)) { // decane
			size = 10;
		} // (gigantic) if

		return size;
	}
	/*
	 * Checks whether a String is a number
	 */
	private static boolean isStringNumber(String str) {
		try {
			Integer.parseInt(str);
			
			return true;
			
		} catch (NumberFormatException n) {
			return false;
		}
	} //end stringIsNumber
	
} //end Interpreter