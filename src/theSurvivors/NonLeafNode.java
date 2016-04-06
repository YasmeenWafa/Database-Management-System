package theSurvivors;

public class NonLeafNode extends Node{

	
	
	
	public NonLeafNode(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	public static int minPtrs(int n){
		return (int)Math.ceil(((n+1)/2));
	}

	public int minKeys(int n){
		int r;
		if(n%2==0)
			r=(n+1)/2;
		else
			r=((n+1)/2)-1;
		return r;
	}
}
