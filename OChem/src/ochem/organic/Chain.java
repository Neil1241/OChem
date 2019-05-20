package ochem.organic;

import java.util.*;

/*
 * Chain
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * A chain for an organic compound holding its length and location on the main chain
 */

public class Chain {
	// Attributes
	private int size; // side of the side chain
	private String location; // location on the main chain
	private boolean cyclo;
	private ArrayList<String> functionalLocation;
	private ArrayList<String> functionalGroup;
	private ArrayList<Integer> numOfGroups;
	private int bond = 1;
	private int idx2 = 0;
	private int idx = 0;
	private boolean main;

	/*
	 * Creates a new chain with a size and location int size - size of the new chain
	 * int location - location of the new chain on the main chain if this chain is
	 * to be the main chain, location should be set as "-1"
	 */
	public Chain(int size, String location) {
		this.size = size;
		this.location = location;
		this.cyclo = false;
		this.bond = 1;
	} // end constructor

	public Chain(int size, String location, boolean cyclo) {
		this.size = size;
		this.location = location;
		this.cyclo = cyclo;
		this.bond = 1;
	} // end constructor

	public void setEnding(int position) {
		try {
			for (int i = 0; i < this.numOfGroups.get(this.idx); i++) {
				this.functionalGroup
						.add(OrganicUtil.FUNCTIONAL_NAMES[position] + " : " + this.functionalLocation.get(this.idx2++));
			}
			this.idx++;
		} catch (IndexOutOfBoundsException e) {

		}
	}

	public ArrayList<String> getEndings() {
		return this.functionalGroup;
	}

	public void setMain() {
		this.main = true;
		this.functionalLocation = new ArrayList<String>();
		this.functionalGroup = new ArrayList<String>();
	}

	public void setNumOfGroups(ArrayList<Integer> n) {
		this.numOfGroups = n;
	}

	public boolean isMain() {
		return this.main;
	}

	public void addFunctionalLocation(String l) {

		this.functionalLocation.add(l);
	}

	public ArrayList<String> getFunctionalLocation() {
		return functionalLocation;
	}

	/*
	 * Set the size of the chain int size - size of the chain
	 */
	public void setSize(int size) {
		this.size = size;
	} // end setSize

	/*
	 * Get the size of the chain return size - size of the chain
	 */
	public int getSize() {
		return size;
	} // end getSize

	/*
	 * Set the location of the chain on the main chain int location - location of
	 * the chain on the main chain
	 */
	public void setLocation(String location) {
		this.location = location;
	} // end setLocation

	/*
	 * Get the location of the chain return location - location of the chain on the
	 * main chain
	 */
	public String getLocation() {
		return this.location;
	} // end getLocation

	public void setCyclo(boolean b) {
		this.cyclo = b;
	}

	public boolean getCyclo() {
		return this.cyclo;
	}

	public int getBond() {
		return this.bond;
	}

	public void setBond(int b) {
		this.bond = b;
	}
} // end Chain
