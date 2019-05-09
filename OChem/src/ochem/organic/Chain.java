package ochem;

/*
 * Chain
 * Created by: Neil Balaskandarajah
 * Last modified: 05/04/2019
 * A chain for an organic compound holding its length and location on the main chain
 */

public class Chain {
	//Attributes
	private int size; //side of the side chain
	private int location; //location on the main chain
	private boolean cyclo=false;
	
	/*
	 * Creates a new chain with a size and location
	 * int size - size of the new chain
	 * int location - location of the new chain on the main chain
	 * 		if this chain is to be the main chain, location should be set as "-1"
	 */
	public Chain(int size, int location) {
		this.size = size;
		this.location = location;
	} //end constructor
	
	/*
	 * Set the size of the chain
	 * int size - size of the chain
	 */
	public void setSize(int size) {
		this.size = size;
	} //end setSize
	
	/*
	 * Get the size of the chain
	 * return size - size of the chain
	 */
	public int getSize() {
		return size;
	} //end getSize
	
	/*
	 * Set the location of the chain on the main chain
	 * int location - location of the chain on the main chain
	 */
	public void setLocation(int location) {
		this.location = location;
	} //end setLocation
	
	/*
	 * Get the location of the chain 
	 * return location - location of the chain on the main chain
	 */
	public int getLocation() {
		return location;
	} //end getLocation
	
	public void setCyclo(boolean b) {
		this.cyclo=b;
	}
	
	public boolean getCyclo() {
		return this.cyclo;
	}
} //end Chain
