package theSurvivors;

import java.util.ArrayList;
import java.util.Hashtable;

public class Table {
	String name;
	Hashtable<String,String> ColumnNameType;
	Hashtable<String,String> ColumnNameRefs;
	String PrimaryKey;
    ArrayList<Page>  tablesPages;
	ArrayList<BTree> trees;
    
	public Table(String name, Hashtable<String, String> ColumnNameType,
			Hashtable<String, String> ColumnNameRefs, String PrimaryKey) {
		this.name = name;
		this.ColumnNameType=ColumnNameType;
		this.ColumnNameRefs=ColumnNameRefs;
		this.PrimaryKey=PrimaryKey;
		this.tablesPages = new ArrayList<Page>();
	    trees = new ArrayList<BTree>();
	     //initial btree for the primary key of that table
	}
	
	
	


	public ArrayList<BTree> getTrees() {
		return trees;
	}


	public void setTrees(ArrayList<BTree> trees) {
		this.trees = trees;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Hashtable<String, String> getColumnNameType() {
		return ColumnNameType;
	}
	public void setColumnNameType(Hashtable<String, String> columnNameType) {
		ColumnNameType = columnNameType;
	}
	public Hashtable<String, String> getColumnNameRefs() {
		return ColumnNameRefs;
	}
	public void setColumnNameRefs(Hashtable<String, String> columnNameRefs) {
		ColumnNameRefs = columnNameRefs;
	}
	public String getPrimaryKey() {
		return PrimaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		PrimaryKey = primaryKey;
	}
	public ArrayList<Page> getTablesPages() {
		return tablesPages;
	}
	public void setTablesPages(ArrayList<Page> tablesPages) {
		this.tablesPages = tablesPages;
	}
}
