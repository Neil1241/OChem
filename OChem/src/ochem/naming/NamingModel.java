package ochem.naming;
/* NamingModel.java
 * Jordan Lin
 * May 24, 2019
 */

//import packages
import ochem.organic.*;

public class NamingModel extends Object {
	private NamingGUI g;
	private Compound c;
	private Chain mainChain;
	private Chain[] sideChains;
	private boolean valid = true;
	
	public NamingModel() {
		super();
	}
	
	public void setGUI(NamingGUI g) {
		this.g=g;
	}
	
	public void giveCompound(Compound c) {
		this.c=c;
		this.mainChain=c.getMainChain();
		this.sideChains=c.getSideChains();
		this.g.update();
	}
	
	public Compound getCompound() {
		return this.c;
	}
	
	public void giveInvalid() {
		this.valid = false;
		this.g.update();
	}
	
	public boolean isValid() {
		return this.valid;
	}
	
	public void setValid(boolean b) {
		this.valid=b;
	}
	
}//end class
