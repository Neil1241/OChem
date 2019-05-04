package ochem;

/*
 * Interpreter
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * Converts text names of compounds into compound objects
 * TO-DO: Add side chain location functionality
 */

import java.util.ArrayList;

public class Interpreter {
	//Attributes
	private static StringBuffer compoundName; //name of the compound
	private static Compound compound; //compound to be created
	
	private static int main; //size of the main chain
	private static ArrayList<String> chains; //all the side chains
	
	/*
	 * Create a Compound object from a text name
	 * String name - name of the compound as text
	 * return compound - compound object from Compound
	 */
	public static Compound compoundFromName(String name) {
		compoundName = new StringBuffer(name); //Create StringBuffer for ease of manipulation
		
		removeHyphens(); //remove all the hyphens in the String
		splitChains(); //split up into all of the chains
		
		compound = new Compound(main); //create the compound with a main chain size
		addChains(); //add the side chains to the compound
		
		return compound;
	} //end compoundFromName
	
	/*
	 * Remove all hyphens from the compound name
	 */
	private static void removeHyphens() {
		
		//loop through the name String deleted all hyphens
		for (int i = 0; i < compoundName.length(); i++) {
			
			if (compoundName.charAt(i) == '-') {
				compoundName.deleteCharAt(i);
			} //if
			
		} //loop
		
		//dbg
		System.out.println("----------");
		System.out.println("dehyphenated: ");
		System.out.println(compoundName.toString());
		System.out.println("----------");
	} //end removeHyphens
	
	/*
	 * Split the de-hyphenated name into multiple Strings representing the side chains
	 * TO-DO: Find and store the locations of each side chain 
	 * 		- currently the first char in each chain besides the final main chain is an int with its location
	 */
	private static void splitChains() {
		ArrayList<Integer> numIndices = new ArrayList<Integer>(); //list of all the positions numbers are found
		chains = new ArrayList<String>(); //initialize list for all the chain names
		
		System.out.println("number locations in string and numbers"); //dbg
		
		//loop through the entire String finding the index of each number in the String and saving it to a list
		for (int i = 0; i < compoundName.length(); i++) {
			char num = compoundName.charAt(i);
			
			for (int j = 0; j < OrganicUtil.NUMBERS.length(); j++) { //loop through the String of numbers to see if a number matches
				if (num == OrganicUtil.NUMBERS.charAt(j)) {
					numIndices.add(i);
					System.out.println(i +" "+ OrganicUtil.NUMBERS.charAt(j)); //dbg
				} //if
				
			} //inner loop
			
		} //outer loop
		
		//dbg
		System.out.println("----------");
		System.out.println("split chains: ");
		
		//get the chain names and add them to the list
		for (int k = 0; k < numIndices.size()-1; k++) {
			//split up the original dehyphenated name into separate chains
			String chainName = compoundName.substring(numIndices.get(k), numIndices.get(k+1)); 
			chains.add(chainName);
			
			System.out.println(k +" "+ chains.get(k)); //dbg
		}
		//add the leftovers to the list
		chains.add(compoundName.substring(numIndices.get(numIndices.size()-1), compoundName.length()));
		
		//leftovers is a side chain and main chain, split them up
		String lastChain = chains.get(chains.size()-1);
		System.out.println("last chain: " + lastChain); //dbg
		
		//loop through the string to split it using the side chain signifier
		for (int l = 0; l < lastChain.length() - OrganicUtil.ALKYL_SIDE_CHAIN.length() - 1; l++) {
			
			//if the substring found is the side chain signifier ("yl")
			if (lastChain.substring(l, l + OrganicUtil.ALKYL_SIDE_CHAIN.length()).equals(OrganicUtil.ALKYL_SIDE_CHAIN)) {
				//remove the leftovers
				chains.remove(chains.size() - 1);
				
				//and add the side and main chains
				chains.add(lastChain.substring(0, l + OrganicUtil.ALKYL_SIDE_CHAIN.length()));
				chains.add(lastChain.substring(l + OrganicUtil.ALKYL_SIDE_CHAIN.length(), lastChain.length()));
			} //if
			
		} //loop
		
		//dbg
		System.out.println("----------");
		System.out.println("all chains: ");
		
		//print out all the chains
		for (int i = 0; i < chains.size(); i++) {
			System.out.println(chains.get(i)); //dbg
		}
		
		//set the main chain size
		main = chainToNumber(chains.get(chains.size()-1));
		
		//dbg
		System.out.println("----------");
		
	} //end splitChains
	
	/*
	 * Add the side chains to the compound
	 * TO-DO: add the proper location
	 */
	private static void addChains() {
		//loop through the list of the text forms of the chains, convert them and add them
		for (int i = 0; i < chains.size(); i++) {
			compound.addSideChain(chainToNumber(chains.get(i)), 0);
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
	
} //end Interpreter