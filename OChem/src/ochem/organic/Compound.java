package ochem.organic;

/*
 * Compound
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * Creates a compound with chains on it
 */

import java.util.ArrayList;

public class Compound {
	// Attributes
	private Chain mainChain; // the main
	private ArrayList<Chain> sideChains; // the side chains

	/*
	 * Create a compound with a main chain int mainSize - size of the main chain
	 */
	public Compound(int mainSize) {
		mainChain = new Chain(mainSize, "-1");
		mainChain.setMain();
		sideChains = new ArrayList<Chain>(); // initialize sideChains list
	} // end constructor

	public void addFunctionalLocation(String location) {
		mainChain.addFunctionalLocation(location);
	}

	public Chain getMainChain() {
		return this.mainChain;
	}

	/*
	 * Set the size of the main chain int main - size of the main chain
	 */
	public void setMainSize(int main) {
		mainChain.setSize(main);
	} // end setMainSize

	/*
	 * Get the size of the main chain return - size of the main chain
	 */
	public int getMainSize() {
		return mainChain.getSize();
	} // end getMainSize

	/*
	 * Add a side chain to the compound int size - size of the side chain int
	 * location - location of the side chain on the main chain
	 */
	public void addSideChain(int size, String location, boolean cyclo, boolean benzene) {
		for (int i = 0; i < OrganicUtil.LOCATIONS.length; i++) {
			if (location.equalsIgnoreCase(OrganicUtil.LOCATIONS[i]) || location.equalsIgnoreCase("o")) {
				sideChains.add(new Chain(size, location, cyclo, benzene));
				break;
			}
		}
	} // end addSideChain

	/*
	 * Get an array of all the side chains return side - array containing all the
	 * side chains
	 */
	public Chain[] getSideChains() {
		Chain[] sides = new Chain[sideChains.size()];

		// fill contents of list into array
		for (int i = 0; i < sideChains.size(); i++) {
			sides[i] = sideChains.get(i);
		} // loop

		return sides;
	} // end getSideChains

	/*
	 * Return a string representation of the compound; size of mainChain and
	 * size,location of side chains
	 */
	public String toString() { // OVERRIDEN
		String s = "";
		ArrayList<String> locations = mainChain.getFunctionalLocation();
		ArrayList<String> e = mainChain.getEndings();

		// main chain
		s = s.concat("Main Chain of: " + mainChain.getSize() + "\n");
		s = s.concat("Main isCyclo: " + mainChain.isCyclo() + "\n");
		s = s.concat("Main isBenzene: " + mainChain.isBenzene() + "\n");
		if (locations != null)
			for (int i = 0; i < locations.size(); i++)
				s = s.concat("Main Chain Functional Groups at " + locations.get(i) + "\n");
		// loop through all side chains
		int num = 1;
		for (Chain side : sideChains) {
			s = s.concat("Side chain " + num + " of size: " + side.getSize() + " and location: " + side.getLocation()
					+ " | cyclo: " + side.isCyclo() + " and benzene: " + side.isBenzene() + "\n");
			num++;
		} // loop
		s = s.concat("------------------GROUP-------------------\n");
		for (String k : e)
			s = s.concat(k+"\n");

		return s;
	} // end toString

} // end Compound
