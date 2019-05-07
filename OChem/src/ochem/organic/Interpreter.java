package ochem.organic;

/*
 * Interpreter
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * Converts text names of compounds into compound objects
 * TO-DO: Add side chain location functionality
 */

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Interpreter {
	//Attributes
	private static StringTokenizer compoundName; //name of the compound
	private static Compound compound; //compound to be created
	
	private static int main; //size of the main chain
	
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
		
		return new Compound(0);
	} //end compoundFromName
	
	/*
	 * Split the text form of the name into the side chains and locations
	 */
	private static void splitChains() {		
		compoundName = new StringTokenizer(originalName); //Create StringTokenizer for ease of manipulation
		
		chainNames = new ArrayList<String>();
		
		while (compoundName.hasMoreTokens()) { //delimit by the hyphen and add it to the list
			chainNames.add(compoundName.nextToken("-"));
		} //loop
		
		splitLastChain(); //split the last chain and add it to the list
	} //end splitChains()
	
	/*
	 * Split the last chain into its elements
	 */
	private static void splitLastChain() {
		StringTokenizer lastChain = new StringTokenizer(chainNames.get(chainNames.size() - 1)); //last element in list
		chainNames.remove(chainNames.get(chainNames.size() - 1)); //remove last element
		
		while(lastChain.hasMoreTokens()) {
			chainNames.add(lastChain.nextToken(OrganicUtil.ALKYL_SIDE_CHAIN) + OrganicUtil.ALKYL_SIDE_CHAIN); //delimit by side chain signifier (will be changed later)
		} //loop
		
		//create the last chain to remove the yl
		String mainChain = chainNames.get(chainNames.size() - 1);
		chainNames.remove(chainNames.size() - 1); //remove last element
		
		chainNames.add(mainChain.substring(0, mainChain.length() - OrganicUtil.ALKYL_SIDE_CHAIN.length()));
	} //end splitLastChain
	
	/*
	 * Remove the locations from the chain names list and add it to locations list
	 */
	private static void addLocations() {
		ArrayList<Integer> numIndices = new ArrayList<Integer>(); //indices for all the numbers to remove later
		chainLocations = new ArrayList<Integer>();
		
		for (int i = 0; i < chainNames.size(); i++) {
			if (isStringNumber(chainNames.get(i))) {
				chainLocations.add(Integer.parseInt(chainNames.get(i))); //add it to the locations list
				numIndices.add(i); //remove it from the list
				System.out.println("i " + i +" "+ chainNames.get(i));
			} //if
		} //loop
		
		for (int j = 0; j < numIndices.size(); j++) {
			System.out.println(j +" "+ chainNames.get(numIndices.get(j)));
			chainNames.remove(chainNames.get(numIndices.get(j)));
		}
		
	} //end addLocation
	
	/*
	 * Add the side chains to the compound
	 * TO-DO: add the proper location
	 */
	private static void addChains() {
		//loop through the list of the text forms of the chains, convert them and add them
		for (int i = 0; i < chainNames.size(); i++) {
			compound.addSideChain(chainToNumber(chainNames.get(i)), 0);
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
		if (chain.equals(OrganicUtil.ONE_CHAIN)) { //methane
			size = 1;
			
		}  else if (chain.equals(OrganicUtil.TWO_CHAIN)) { //ethane
			size = 2;
			
		} else if (chain.equals(OrganicUtil.THREE_CHAIN)) { //propane
			size = 3;
			
		} else if (chain.equals(OrganicUtil.FOUR_CHAIN)) { //butane
			size = 4;
			
		} else if (chain.equals(OrganicUtil.FIVE_CHAIN)) { //pentane
			size = 5;
				
		} else if (chain.equals(OrganicUtil.SIX_CHAIN)) { //hexane
			size = 6;
			
		} else if (chain.equals(OrganicUtil.SEVEN_CHAIN)) { //heptane
			size = 7;
			
		} else if (chain.equals(OrganicUtil.EIGHT_CHAIN)) { //octane
			size = 8;
			
		} else if (chain.equals(OrganicUtil.NINE_CHAIN)) { //nonane
			size = 9;
			
		} else if (chain.equals(OrganicUtil.TEN_CHAIN)) { //decane
			size = 10;
		} //(gigantic) if
		
		return size;
	} //end chainToNumber
	
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