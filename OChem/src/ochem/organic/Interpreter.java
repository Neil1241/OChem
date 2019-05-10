package ochem.organic;

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
	private static String front="";
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
		
		compound=new Compound(chainToNumber(chainNames.get(chainNames.size()-1),suffix,front));
		if (front.equalsIgnoreCase(OrganicUtil.PREFIX[9]))
			compound.getMainChain().setCyclo(true);
		addChains();
		
		
		return compound;
	} //end compoundFromName
	
	/*
	 * Split the text form of the name into the side chains and locations
	 */
	private static void splitChains() {		
		compoundName = new StringTokenizer(originalName,"-, "); //Create StringTokenizer for ease of manipulation
		
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
		//local variables
		String last=chainNames.get(chainNames.size() - 1);
		int length=0;
		
		//try to find attached sidechain and if found, remove the last element within the arraylist and change it to be two elements
		length=last.indexOf(OrganicUtil.ALKYL_SIDE_CHAIN);
		if (length != -1) {
			chainNames.remove(chainNames.get(chainNames.size() - 1)); //remove last element
			chainNames.add(last.substring(0, length+2));
			chainNames.add(last.substring(length+2));
		}//end if
		last=chainNames.get(chainNames.size() - 1);
		
		
		
		if (last.length()>=5)
			if (last.substring(0,5).equalsIgnoreCase(OrganicUtil.PREFIX[9]))
				front=OrganicUtil.PREFIX[9];
		
		//create the last chain to determine suffix
		suffix = mainChainEnding(chainNames.get(chainNames.size() - 1));
		
	} //end splitLastChain
	
	
	//method tries to obtain the ending suffix of the mainChain and returns it found
	private static String mainChainEnding(String mainChain)
	{
		//string to hold the suffix and to be returned at the end
		String ending="";
		try
		{
			//based on the error counter at the beginning, determine the type of suffix
			ending = mainChain.substring(mainChain.indexOf(OrganicUtil.SUFFIX[error]));
			
			return ending;
		}
		catch (StringIndexOutOfBoundsException e)
		{
			//if error is thrown, add one to the error counter and then run itself again
			error++;
			ending=mainChainEnding(mainChain);
			return ending;
		}
	}//end mainChainEnding
	
	/*
	 * Remove the locations from the chain names list and add it to locations list
	 */
	private static void addLocations() {
		chainLocations = new ArrayList<Integer>();
		
		for (int i = 0; i < chainNames.size(); i++) {
			if (isStringNumber(chainNames.get(i))) {
				chainLocations.add(Integer.parseInt(chainNames.get(i))); //add it to the locations list
				
				chainNames.remove(i);
				i--;
			} //if
		} //loop
		

		
	} //end addLocation
	

	//Add the side chains to the compound
	private static void addChains() {
		int location=0;
		for(int i = 0; i < chainNames.size(); i++)
			System.out.println(chainNames.get(i));
		
		//loop through the list of the text forms of the chains, convert them and add them
		for (int i = 0; i < chainNames.size()-1; i++) {
			//temp variables
			String prefix="";
			int additional=0;
			boolean cyclo=false;
			
			//loop for the length of the size of the organicUtil array
			for (int j = 0; j <OrganicUtil.PREFIX.length; j++) {
				String temp = chainNames.get(i);
				//check if 
				if (temp.length() >= OrganicUtil.PREFIX[j].length()) 
				{
					if (temp.substring(0, OrganicUtil.PREFIX[j].length()).equalsIgnoreCase(OrganicUtil.PREFIX[j]))
					{
						if (temp.length()>=OrganicUtil.PREFIX[j].length()+OrganicUtil.PREFIX[9].length())
						{
							if (temp.substring(0, OrganicUtil.PREFIX[j].length()+OrganicUtil.PREFIX[9].length()).equalsIgnoreCase(OrganicUtil.PREFIX[j]+OrganicUtil.PREFIX[9]))
							{
								prefix=OrganicUtil.PREFIX[j]+OrganicUtil.PREFIX[9];
								cyclo=true;
							}
							else
							{
								prefix=OrganicUtil.PREFIX[j];
							}
						}
						else	
							prefix = OrganicUtil.PREFIX[j];
						
						if (j!=9)
							additional=j+1;
						
						break;
					}//end if
					
				}//end if
			}//end for loop
			System.out.println("--------------------------------------");
			System.out.println(chainNames.get(i));
			System.out.println(prefix);
			System.out.println(chainToNumber(chainNames.get(i),OrganicUtil.ALKYL_SIDE_CHAIN,prefix));
			compound.addSideChain(chainToNumber(chainNames.get(i),OrganicUtil.ALKYL_SIDE_CHAIN,prefix),chainLocations.get(location),cyclo);
			
			for (int j=0;j<additional;j++)
			{
				location++;
				compound.addSideChain(chainToNumber(chainNames.get(i),OrganicUtil.ALKYL_SIDE_CHAIN,prefix),chainLocations.get(location),cyclo);
			}
		} //loop
		
	} //end addChains
	
	/*
	 * Convert the text form of the chain to a numerical value representing its length
	 * String chain - the text form of the chain to be converted
	 * return size - numerical form of the chain
	 */
	/*private static int chainToNumber(String chain, String suffix) {
		int size = 0;

		// compare the String to each of the main chain Strings and set the size
		// accordingly
		for (int i=0;i<10;i++)
		{
			if (chain.equalsIgnoreCase(OrganicUtil.CHAIN[i] + suffix)) { // methane
				size = i+1;
				break;
			}//end if
		}//end for
		return size;
	}*/
	
	//PROB DON'T NEED THIS
	/*private static int prefixToNumber(String prefix)
	{
		int size=0;
		for (int j = 0; j < 9; j++) {
			if (prefix.length() >= OrganicUtil.PREFIX[j].length()) {
				if (prefix.substring(0, OrganicUtil.PREFIX[j].length()).equalsIgnoreCase(OrganicUtil.PREFIX[j]))
				{
					size=j+2;
					break;
				}//end if
			}//end if
		}//end for loop
		return size;
	}*/
	
	private static int chainToNumber(String chain, String suffix, String prefix) {
		int size = 0;

		// compare the String to each of the main chain Strings and set the size
		// accordingly
		for (int i=0;i<10;i++)
		{
			if (chain.equalsIgnoreCase(prefix + OrganicUtil.CHAIN[i] + suffix)) { // methane
				size = i+1;
				break;
			}//end if
		}//end for

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