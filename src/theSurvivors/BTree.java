package theSurvivors;
import java.util.ArrayList;
import java.util.Collections;



/**
 * A B+ tree
 * Since the structures and behaviors between internal node and external node are different, 
 * so there are two different classes for each kind of node.
 * @param <TKey> the data type of the key
 * @param <TValue> the data type of the value
 */
public class BTree<TKey extends Comparable<TKey>, TValue> {
	private BTreeNode<TKey> root;
	String Name;
	public BTree(String Name) {
		this.root = new BTreeLeafNode<TKey, TValue>();
	this.Name=Name;
	}

	/**
	 * Insert a new key and its associated value into the B+ tree.
	 */
	public void insert(TKey key, TValue value) {
		BTreeLeafNode<TKey, TValue> leaf = this.findLeafNodeShouldContainKey(key);
		leaf.insertKey(key, value);
		
		if (leaf.isOverflow()) {
			BTreeNode<TKey> n = leaf.dealOverflow();
			if (n != null)
				this.root = n; 
		}
	}
	
	/**
	 * Search a key value on the tree and return its associated value.
	 */
	public TValue search(TKey key) {
		BTreeLeafNode<TKey, TValue> leaf = this.findLeafNodeShouldContainKey(key);
		
		int index = leaf.search(key);
		return (index == -1) ? null : leaf.getValue(index);
	}
	
	/**
	 * Delete a key and its associated value from the tree.
	 */
	public void delete(TKey key) {
		BTreeLeafNode<TKey, TValue> leaf = this.findLeafNodeShouldContainKey(key);
		
		if (leaf.delete(key) && leaf.isUnderflow()) {
			BTreeNode<TKey> n = leaf.dealUnderflow();
			if (n != null)
				this.root = n; 
		}
	}
	
	/**
	 * Search the leaf node which should contain the specified key
	 */
	@SuppressWarnings("unchecked")
	private BTreeLeafNode<TKey, TValue> findLeafNodeShouldContainKey(TKey key) {
		BTreeNode<TKey> node = this.root;
		while (node.getNodeType() == TreeNodeType.InnerNode) {
			node = ((BTreeInnerNode<TKey>)node).getChild( node.search(key) );
		}
		
		return (BTreeLeafNode<TKey, TValue>)node;
	}
	public static void main(String []args){
		BTree t=new BTree("one");
		t.insert("ID",30);
		//System.out.println(t.search("ID"));
		t.insert("ID", 20);
		
		
	}
}

/*public class Btree {
	    Node root;
		int n;
		int height;
		int index;
		boolean flag=false;
		Node previous=null;
		Node above=null;
		Node temp1=null;
		Node temp2=null;
		public Btree (LeafNode node,int n) {
		//root = null;
		node=new LeafNode(n);
		this.n=n;
		height=0;
		}
		public void search(int x,Node current,boolean toInsert){
			searchInNode(x,current,current.keys,toInsert);
			}
		public void searchInNode(int x,Node node,ArrayList<Integer> current,boolean toInsert){
			int length=current.size();
			int currentCase = 0;
			int i=0;
			///12,16,29  insert 14
				while(i<length){
					int v=current.get(i).intValue();
					if(v>x)
					{  
					   if(node instanceof LeafNode){
						   if(node.keys.size()<node.maxKeys(n)){
							   if(toInsert ==true)
							   insert(x,node,previous,current,i,0);
							   else delete(x,node,previous,current,i);
						   }
						   else{
							   if(toInsert ==true)
							   insert(x,node,previous,current,i,1);
							   else delete(x,node,previous,current,i);

						   }
						
					   }
					   else{
						   if(node instanceof NonLeafNode){
							   previous=node;
								  if(node.keys.size()==node.maxKeys(n)){
									   Node currentP=node.pointers.get(i);
										  search(x,currentP,toInsert);
								   }
								   else{
									   node.keys.add(x);
									   Collections.sort(node.keys);
									   if(flag==true){
										   node.pointers.add(temp1);
										   node.pointers.add(temp2);
									   }
									   
									 
								   }
							  }
						   else{
							   if(node instanceof Root){
								   previous=node;
								   if(node.keys.size()==node.maxKeys(n)){
									   Node currentP=node.pointers.get(i);
										  search(x,currentP,toInsert);
								   }
								   else{
									   node.keys.add(x);
									   Collections.sort(node.keys);
									   if(flag==true){
										   node.pointers.add(temp1);
										   node.pointers.add(temp2);
									   }
									   
									 
								   }
								  
							   }
						   }
					  }
						  
					}	
					else
					{
						if(v<x){
							i++;
						}
							
					}
				}
		}
		
		public void insert(int Addkey,Node current,Node previous,ArrayList<Integer>c,int q,int cases){
			//case fi makan &leaf
			if(cases==0){
				int length=current.keys.size()+1;
				ArrayList<Integer>temp = null;
				int index=q;
			   for(int i=q;i<length;i++){
				   temp.add(current.keys.get(i));
			   }
			   c.add(Addkey);
			   for(int i=0;i<temp.size();i++){
				   c.add(temp.get(i));
			   }
			}
			//case mfish makan w leaf
			//new leaf and split
			else{
				if(cases==1){
					LeafNode newLeaf=new LeafNode(n); 
					int size=current.keys.size();
					ArrayList<Integer>temp=new ArrayList();
					temp.add(Addkey);
					for(int i=0;i<current.keys.size();i++){
						temp.add(current.keys.get(i));
						Collections.sort(temp);
					}
					for(int i=0;i<temp.size()/2;i++)
					{
						newLeaf.keys.add(temp.get(i));
					}
					for(int i=0;i<size;i++)
						current.keys.remove(i);
					for(int i=temp.size()/2;i<temp.size();i++){
						current.keys.add(temp.get(i));
					}
					if(previous.equals(null)){
					Root r=new Root(n);
					r.keys.add(current.keys.indexOf(0));
				
					}
					else{
						if(previous.keys.size()==previous.maxKeys(n)){
							if(previous instanceof NonLeafNode){
								ArrayList<Integer>temp1=new ArrayList();
								NonLeafNode nn=new NonLeafNode(n);
								temp1.add(Addkey);
								for(int i=0;i<previous.keys.size();i++){
									temp1.add(previous.keys.get(i));
								}
								Collections.sort(temp1);
								int length=(int)(Math.floor(n+2)/2);
								for(int i=0;i<length;i++)
								{
									nn.keys.add(temp1.get(i));
								}
								for(int i=0;i<size;i++)
									previous.keys.remove(i);
								for(int i=length+1;i<temp1.size();i++){
									previous.keys.add(temp1.get(i));
								}
								int size1=nn.keys.size();
								for(int i=size1+1;i<nn.pointers.size();i++){
									nn.pointers.remove(i);
								}
								flag=true;
								search(temp1.get(length),root,true);
								
							}
							else{
								if(previous instanceof Root){
									ArrayList<Integer>temp1=new ArrayList();
									NonLeafNode nn=new NonLeafNode(n);
									temp1.add(Addkey);
									for(int i=0;i<previous.keys.size();i++){
										temp1.add(previous.keys.get(i));
										Collections.sort(temp1);
									}
									int length=(int)(Math.floor(n+2)/2);
									for(int i=0;i<length;i++)
									{
										nn.keys.add(temp1.get(i));
									}
									for(int i=0;i<size;i++)
										previous.keys.remove(i);
									for(int i=length+1;i<temp1.size();i++){
										previous.keys.add(temp1.get(i));
									}
									int size1=nn.keys.size();
									for(int i=size1+1;i<nn.pointers.size();i++){
										nn.pointers.remove(i);
									}
									Root r=new Root(n);
									r.keys.add(temp1.get(length));
									r.pointers.add(nn);
									r.pointers.add(previous);
								}
								}
							}
						else{
								previous.keys.add(Addkey);
							    Collections.sort(previous.keys);
							    previous.pointers.add(newLeaf);
							    previous.pointers.add(current);
						
						}
					}
					
				}
			
			}
			
			
		    
			
		}
		public void delete(int value, Node current,Node previous,ArrayList<Integer>c,int q){
			
			if(current instanceof LeafNode){
			//leaf node and can remove directly
				if(current.keys.contains(value)&&((current.keys.size()-1)>=LeafNode.minPtrs(n)))
			{
				search(value,current,false);
				if(previous.keys.contains(value) && ((previous.keys.size()-1)>=NonLeafNode.minPtrs(n)))
					search(value,previous,false);

			}
			
			}
		}
	
}
		*/
			
		
		
					
			

		
		

