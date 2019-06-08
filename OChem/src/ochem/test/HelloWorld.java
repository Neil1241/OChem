package ochem.test;

public class HelloWorld {
	public static void main(String args[]) {
		StringBuffer b = new StringBuffer("PTENE");
		b.deleteCharAt(1);
		b.insert(1, "ENT");
		System.out.println(b.toString());
	}//end main

}
