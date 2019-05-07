package ochem.organic;

/*
 * Compound
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * Creates a compound with chains on it
 */

import java.util.ArrayList;

public class Compound {
	//Attributes
	private Chain mainChain; //the main
	private ArrayList<Chain> sideChains; //the side chains
	
	/*
	 * Create a compound with a main chain
	 * int mainSize - size of the main chain
	 */
	public Compound(int mainSize) {
		mainChain = new Chain(mainSize, -1);
		
		sideChains = new ArrayList<Chain>(); //initialize sideChains list
	} //end constructor
	
	/*
	 * Set the size of the main chain
	 * int main - size of the main chain
	 */
	public void setMainSize(int main) {
		mainChain.setSize(main);
	} //end setMainSize
	
	/*
	 * Get the size of the main chain
	 * return - size of the main chain
	 */
	public int getMainSize() {
		return mainChain.getSize();
	} //end getMainSize
	
	/*
	 * Add a side chain to the compound
	 * int size - size of the side chain
	 * int location - location of the side chain on the main chain
	 */
	public void addSideChain(int size, int location) {
		if (location < 0) { //cannot have negative location
			throw new IllegalArgumentException("Negative numbers not valid as location of side chain");
		} else { //zero or greater integer
			sideChains.add(new Chain(size, location));
		} //if
	} //end addSideChain
	
	/*
	 * Get an array of all the side chains
	 * return side - array containing all the side chains
	 */
	public Chain[] getSideChains() {
		Chain[] sides = new Chain[sideChains.size()];
		
		//fill contents of list into array
		for (int i = 0; i < sideChains.size(); i++) {
			sides[i] = sideChains.get(i);
		} //loop
		
		return sides;
	} //end getSideChains
	
	/*
	 * Return a string representation of the compound; size of mainChain and size,location of side chains
	 */
	public String toString() { //OVERRIDEN
		String s = "";
		
		//main chain
		s = s.concat("Main Chain of: " + mainChain.getSize() + "\n");
		
		//loop through all side chains
		for (int i = 0; i < sideChains.size(); i++) {
			s.concat("Side chain " + i + " of: " + sideChains.get(i) + "\n");
		} //loop
		
		return s;
	} //end toString
	
} //end Compound
