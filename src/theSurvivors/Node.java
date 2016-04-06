package theSurvivors;

import java.util.ArrayList;

/*public class Node {
public ArrayList<Integer> keys;
public ArrayList<Integer> getKeys() {
	return keys;
}

public void setKeys(ArrayList<Integer> keys) {
	this.keys = keys;
}

public ArrayList<Node> getPointers() {
	return pointers;
}

public void setPointers(ArrayList<Node> pointers) {
	this.pointers = pointers;
}
public ArrayList<Node> pointers;

public Node(int n){
	keys=new ArrayList(maxKeys(n));
	pointers=new ArrayList<Node>(maxPointers(n));
}

public int maxPointers(int n){
	return n+1;
}
public int maxKeys(int n){
	return n;
}

}*/

//generalize the node to work for types other than just int
public class Node<T extends Comparable<? super T> >
{
  private T value; //get; set;
  private Node<T> left; //get; set;
  private Node<T> right; //get; set;

  /**
   * construct a Node with value
   *
   * @param val value for this node
   */
  public Node(T val)
  {
      value = val;
      left = null;
      right = null;
  }

  /**
   * copy constructor
   * 
   * @param n node to copy from
   */
  public Node(Node<T> n)
  {
      value = n.value;
      left = n.left;
      right = n.right;
  }

  /**
   * @return true if this node has no children
   */
  public boolean isLeaf()
  {
      return (left == null && right == null);
  }

  public Node<T> getLeft() { return left; }
  public Node<T> getRight() { return right; }
  public T getValue() { return value; }
  public void setLeft(Node<T> n) { left = n; }
  public void setRight(Node<T> n) { right = n; }
  public void setValue(T v) { value = v; }
}
