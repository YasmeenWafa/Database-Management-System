package theSurvivors;

public class LeafNode extends Node {

	
	public LeafNode(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}
	public static int minPtrs(int n){
		return (int)Math.floor(((n+1)/2));
	}
	public int minKeys(int n){
		return (n+1)/2;
	}
	
}
