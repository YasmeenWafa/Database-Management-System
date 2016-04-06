package theSurvivors;

import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;

class DBAppException extends Exception {

}

class DBEngineException extends Exception {

}

public class DBApp {
	private static String fileHeader = "Table Name, Column Name, Column Type, Key, Indexed, References";
	private static final String comma = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private FileWriter fileWriter;
	static ArrayList<Table> tables;
	boolean tree=false;

	public void init() {
		try {
			fileWriter = new FileWriter("metadata.csv", true);
			fileWriter.append(fileHeader.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		tables = new ArrayList<Table>();
	}

	public void createTable(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws DBAppException {

		for (int i = 0; i < tables.size(); i++) {
			if (tables.get(i).getName().equals(strTableName)) {
				System.out
						.println("This table already exists, please choose another name");
				// throw new DBAppException();
			}
		}

		try {

			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append(strTableName);
			fileWriter.append(comma);
			fileWriter.append(strKeyColName);
			fileWriter.append(comma);
			String type = htblColNameType.get(strKeyColName);

			if (htblColNameType.get(strKeyColName).equals("Integer")) {

				fileWriter.append("java.lang.Integer");
				fileWriter.append(comma);
				fileWriter.append("True");
				fileWriter.append(comma);
				fileWriter.append("True");
				fileWriter.append(comma);
			} else {
				fileWriter.append(type);
				fileWriter.append(comma);
				fileWriter.append("True");
				fileWriter.append(comma);
				fileWriter.append("False");
				fileWriter.append(comma);
			}

			if (htblColNameRefs.containsKey(strKeyColName)) {
				fileWriter.append(htblColNameRefs.get(strKeyColName));
			} else {
				fileWriter.append("null");
			}

			fileWriter.append(NEW_LINE_SEPARATOR);

			Enumeration e = htblColNameType.keys();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				if (!key.equals(strKeyColName)) {

					fileWriter.append(strTableName);
					fileWriter.append(comma);
					fileWriter.append(key);
					fileWriter.append(comma);
					String value = htblColNameType.get(key);
					if (value.equals("Double")) {
						fileWriter.append("java.lang.Double");
						fileWriter.append(comma);
						fileWriter.append("False");
						fileWriter.append(comma);
						fileWriter.append("False");
						fileWriter.append(comma);
					} else {
						if (value.equals("String")) {
							fileWriter.append("java.lang.String");
							fileWriter.append(comma);
							fileWriter.append("False");
							fileWriter.append(comma);
							fileWriter.append("False");
							fileWriter.append(comma);
						} else {
							if (value.equals("Integer")) {
								fileWriter.append("java.lang.Integer");
								fileWriter.append(comma);
								fileWriter.append("False");
								fileWriter.append(comma);
								fileWriter.append("False");
								fileWriter.append(comma);
							} else {
								fileWriter.append(value);
								fileWriter.append(comma);
								fileWriter.append("False");
								fileWriter.append(comma);
								fileWriter.append("False");
								fileWriter.append(comma);
							}
						}
					}

					if (htblColNameRefs.containsKey(key))
						fileWriter.append(htblColNameRefs.get(key));
					else
						fileWriter.append("null");

					fileWriter.append(NEW_LINE_SEPARATOR);
				}
			}
			Table t = new Table(strTableName, htblColNameType, htblColNameRefs,
					strKeyColName);
			tables.add(t);
			createIndex(strTableName,t.getPrimaryKey());
		}

		catch (Exception e) {

			System.out.println("Error in CsvFileWriter !!!");

			e.printStackTrace();

		} finally {

			try {

				fileWriter.flush();
				// fileWriter.close();
			} catch (IOException e) {

				System.out
						.println("Error while flushing/closing fileWriter !!!");

				e.printStackTrace();

			}
		}

	}

	public void createIndex(String strTableName, String strColName)
			throws DBAppException {
		
		Table t = null;
		for(int i = 0; i<tables.size();i++)
		{
			if(tables.get(i).getName()==strTableName){
				t = tables.get(i);
			break;
			}
		}
		String type = t.getColumnNameType().get(strColName);
		//System.out.println(type);
		switch(type)
		
		{
		case "Integer": IntegerBTree x = new IntegerBTree(strColName);
		t.getTrees().add(x); break;
		case"String": StringBTree y = new StringBTree(strColName);
		t.getTrees().add(y); break;
		case"Boolean":BooleanBTree z = new BooleanBTree(strColName);
		t.getTrees().add(z); break;
		case "Double": DoubleBTree a = new DoubleBTree(strColName);
		t.getTrees().add(a); break;
		}
		
		

	
	
	}

	public void insertIntoTable(String strTableName,
			Hashtable<String, Object> htblColNameValue) throws DBAppException,
			Exception {
		boolean flag = false;
		ArrayList<Object> entries = new ArrayList<Object>();
        
		Set keySet = htblColNameValue.keySet();
		Iterator it = keySet.iterator();

		
		
		Object key = null;
		Integer value;
		String value1 = null;
		Double value2;
		Date value3;
		Boolean value4;
		Calendar c = Calendar.getInstance();
		while (it.hasNext()) {
			key = (Object) it.next();
			if (htblColNameValue.get(key) instanceof Integer) {
				value = (Integer) htblColNameValue.get(key);
				entries.add(key);
				entries.add(value);

			} else {
				if (htblColNameValue.get(key) instanceof Double) {
					value2 = (Double) htblColNameValue.get(key);
					entries.add(key);
					entries.add(value2);
				} else {
					if (htblColNameValue.get(key) instanceof String) {
						value1 = (String) htblColNameValue.get(key);
						entries.add(key);
						entries.add(value1);
					} else {
						if (htblColNameValue.get(key) instanceof Date) {
							value3 = (Date) htblColNameValue.get(key);
							entries.add(key);
							entries.add(value3);
						} else {
							if (htblColNameValue.get(key) instanceof Boolean) {
								value4 = (Boolean) htblColNameValue.get(key);
								entries.add(key);
								entries.add(value4);
							}
							// else
							// {
							// throw new DBAppException();
							// }
						}
					}
				}
			}

		}
		entries.add(c.getTime());
		// System.out.println(entries);

		Table t = null;
		try {
			FileReader fr = new FileReader("metadata.csv");
			BufferedReader br = new BufferedReader(fr);
			String line;

			while ((line = br.readLine()) != null) {

				String[] x = line.split(",");
				if (x[0].equalsIgnoreCase(strTableName)) {
					flag = true;
					br.close();
					break;
				}
			}
			if (flag) {

				for (int i = 0; i < tables.size(); i++) {
					if (tables.get(i).getName().equals(strTableName)) {
						t = tables.get(i);
						// System.out.println(t.getName());
						break;
					}
				}

				// System.out.println("eh?");
				if (t.getTablesPages().isEmpty()) {
					// System.out.println("hello");
					Page z = new Page(strTableName + "" + 1);
					z.add(entries);
					z.createPage();
					t.getTablesPages().add(z);

					
					if(t.getTrees().size()==1){//we have only one tree(primary)
					String colType = t.getColumnNameType().get(entries.get(0));
				    t.getTrees().get(0).insert(colType, entries.get(1));
				    System.out.println("value in tree if first page"+entries.get(1)+strTableName);
					}
					else
					{
						//we have several b+trees for same table (several columns)
						for(int i = 0; i<t.getTrees().size();i++ )
						{
							BTree x = t.getTrees().get(i);
							String colName = x.Name;
							String colType = t.getColumnNameType().get(colName);
							int index = entries.indexOf(colName);
							Comparable valCol = (Comparable) entries.get(index+1);
							x.insert(colType,valCol);
						}
					}
					//inserted in the primary b+tree
				}
			

				else {
					boolean f = false;
					Page ppp = null;
					
					for (int i = 0; i < t.getTablesPages().size(); i++) {

						ppp = t.getTablesPages().get(i);
						
						if (ppp.getIndex() < 200) {
							ppp.add(entries);
							ppp.createPage();
							if(t.getTrees().size()==1){//we have only one tree(primary)
								String colType = t.getColumnNameType().get(entries.get(0));
							t.getTrees().get(0).insert(colType, entries.get(1));
						    System.out.println("value in tree if same page"+entries.get(1));

								}
								else
								{
									//we have several b+trees for same table (several columns)
									for(int j = 0; j<t.getTrees().size();j++ )
									{
										BTree x = t.getTrees().get(j);
										String colName = x.Name;
										String colType = t.getColumnNameType().get(colName);
										int index = entries.indexOf(colName);
										Comparable valCol = (Comparable) entries.get(index+1);
										x.insert(colType,valCol);
									}
								};
							
						

							f = true;
							break;
						}
					}
				
					if (f == false) {
						

						int ss = t.getTablesPages().size();
						Page y = new Page(strTableName + "" + (ss + 1));
						

						y.add(entries);
						y.createPage();
						t.getTablesPages().add(y);
						
								
					
						
						
						if(t.getTrees().size()==1){//we have only one tree(primary)
							String colType = t.getColumnNameType().get(entries.get(0));
						t.getTrees().get(0).insert(colType, entries.get(1));
					    System.out.println("value in tree if new page"+entries.get(1));

							}
							else
							{
								//we have several b+trees for same table (several columns)
								for(int i = 0; i<t.getTrees().size();i++ )
								{
									BTree x = t.getTrees().get(i);
									String colName = x.Name;
									String colType = t.getColumnNameType().get(colName);
									int index = entries.indexOf(colName);
									Comparable valCol = (Comparable) entries.get(index+1);
									x.insert(colType,valCol);//inserted in the primary b+tree
								}
							}

					}
				

			}
		//tree=false;

		}} catch (FileNotFoundException e) {
			System.out.println("file not found");
		}

	}

	public void updateTable(String strTableName, Object strKey,
	    Hashtable<String, Object> htblColNameValue) throws DBAppException, IOException {
		ArrayList<Object> entries = new ArrayList<Object>();

		Set keySet = htblColNameValue.keySet();
		Iterator it = keySet.iterator();
		ArrayList<Object> ar = new ArrayList();
		Object key;
		Integer value;
		String value1;
		Double value2;
		Date value3;
		Boolean value4;
		while (it.hasNext()) {
			key = (Object) it.next();
			if (htblColNameValue.get(key) instanceof Integer) {
				value = (Integer) htblColNameValue.get(key);
				entries.add(key);
				entries.add(value);
				

			} else {
				if (htblColNameValue.get(key) instanceof Double) {
					value2 = (Double) htblColNameValue.get(key);
					entries.add(key);
					entries.add(value2);
				} else {
					if (htblColNameValue.get(key) instanceof String) {
						value1 = (String) htblColNameValue.get(key);
						entries.add(key);
						entries.add(value1);
					} else {
						if (htblColNameValue.get(key) instanceof Date) {
							value3 = (Date) htblColNameValue.get(key);
							entries.add(key);
							entries.add(value3);
						} else {
							if (htblColNameValue.get(key) instanceof Boolean) {
								value4 = (Boolean) htblColNameValue.get(key);
								entries.add(key);
								entries.add(value4);
							}
							
						}
					}
				}
			}

		}
		Table t = null;
		Page p = null;
		Page a=null;
		 

		try {
			boolean flag = false;

			boolean f = false;

			FileReader fr = new FileReader("metadata.csv");
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {

				String[] x = line.split(",");
				if (x[0].equalsIgnoreCase(strTableName)) {
					flag = true;
					break;
				}
			}
			ArrayList<Object> temp = new ArrayList<Object>();
			if (flag) {
				// System.out.println(tables.size());
				for (int i = 0; i < tables.size(); i++) {
					if (tables.get(i).getName().equals(strTableName)) {
						t = tables.get(i);
						break;
					}
				}
				if (t != null) {
					if (t.getTablesPages().isEmpty())
						return ;
                     
					for (int i = 0; i < t.getTablesPages().size(); i++) {
						ArrayList<ArrayList> currentPage = t.getTablesPages()
								.get(i).getAr();
						a=t.getTablesPages().get(i);
						String o=t.getPrimaryKey();
						int d=(currentPage.get(0).indexOf(o))+1;
						
						for (int j = 0; j < currentPage.size(); j++) {
						
							ArrayList<Object> ss = currentPage.get(j);
							boolean w = false;
								for (int x = 0; x < entries.size(); x = x + 1) {
									for (int z = 0; z < ss.size(); z = z + 2) {
										
						
							if(strKey.toString().equals(currentPage.get(j).get(d).toString())){
								
									    	f = true;
									    	if (ss.get(z).equals(entries.get(x))) {
											
											if (!((x + 1) >= entries.size())
													&& !((z + 1) >= currentPage
															.size())) {	
										        							
												//System.out.println("v1:"+ss.get(z+1)+",v2:"+entries.get(x+1));
										       Object[] xa=new Object[1];
										       xa[0]=entries.get(x+1);
										       Object xq=ss.get(z+1);
												xq=xa[0];
												
												currentPage.get(j).remove(z+1);
												
												
												currentPage.get(j).add((z+1),xq);
												
												
												a.createPage();
										       
											}
										}
									    	}
										 
								
										 
						
									}
								}
							
							
						}
					}
				}
			}
		}
			catch (FileNotFoundException e) {
				System.out.println("File not found");
			}
		
	}

	public void deleteFromTable(String strTableName,
			Hashtable<String, Object> htblColNameValue, String strOperator)
			throws DBEngineException, ClassNotFoundException, IOException,
			DBAppException {

		ArrayList<Object> selected = new ArrayList<Object>();
		Iterator it = this.selectFromTable(strTableName, htblColNameValue,
				strOperator);
		while (it.hasNext()) {
			selected.add(it.next());
		}
		//System.out.println(selected);
		
		Table t = null;
		Page p = null;
		for(int i = 0; i<tables.size(); i++)
		{
		if(tables.get(i).getName().equals(strTableName))
		{
			t = tables.get(i);
			break;
		}
		}
		for(int i = 0; i<t.getTablesPages().size(); i++)
		{
			p = t.getTablesPages().get(i);
			if(selected.size()!=0){
				
			for(int j = 0; j<selected.size();j++)
			{
				ArrayList tuple = (ArrayList) selected.get(j);
				if(p.getAr().contains(tuple))
				{
					p.remove(tuple);
					/*String x=t.getPrimaryKey();
					createIndex(strTableName,x);
					if(tree){
						Integer index=(Integer)tuple.get(1);
						//t.getTree().delete(index.intValue()); 
						tree=false;
					}
					//deleting from tree;
					//break;*/
					
					if(t.getTrees().size()==1){//we have only one tree(primary)
						String colType = t.getColumnNameType().get(selected.get(0));
					    t.getTrees().get(0).insert(colType, selected.get(1));
						}
						else
						{
							//we have several b+trees for same table (several columns)
							for(int w = 0; w<t.getTrees().size();w++ )
							{
								BTree x = t.getTrees().get(w);
								String colName = x.Name; //column name
								String colType = t.getColumnNameType().get(colName); //column type
								int index = selected.indexOf(colName);
								Comparable valCol = (Comparable) selected.get(index+1); //value of that column in that tuple
								x.delete(valCol);
							}
						}
				}
			
			}
			}
		}

	}

	public Iterator selectFromTable(String strTable,
			Hashtable<String, Object> htblColNameValue, String strOperator)
			throws DBEngineException, IOException, ClassNotFoundException,
			DBAppException {

		ArrayList<Object> entries = new ArrayList<Object>();
		Iterator<Object> itr = null;
		Set keySet = htblColNameValue.keySet();
		Iterator it = keySet.iterator();
		ArrayList<Object> ar = new ArrayList();
		Object key;
		Integer value;
		String value1;
		Double value2;
		Date value3;
		Boolean value4;
		while (it.hasNext()) {
			key = (Object) it.next();
			if (htblColNameValue.get(key) instanceof Integer) {
				value = (Integer) htblColNameValue.get(key);
				entries.add(key);
				entries.add(value);
				

			} else {
				if (htblColNameValue.get(key) instanceof Double) {
					value2 = (Double) htblColNameValue.get(key);
					entries.add(key);
					entries.add(value2);
				} else {
					if (htblColNameValue.get(key) instanceof String) {
						value1 = (String) htblColNameValue.get(key);
						entries.add(key);
						entries.add(value1);
					} else {
						if (htblColNameValue.get(key) instanceof Date) {
							value3 = (Date) htblColNameValue.get(key);
							entries.add(key);
							entries.add(value3);
						} else {
							if (htblColNameValue.get(key) instanceof Boolean) {
								value4 = (Boolean) htblColNameValue.get(key);
								entries.add(key);
								entries.add(value4);
							}
							
						}
					}
				}
			}

		}
		Table t = null;
		Page p = null;
	System.out.println("In select:"+entries);

		try {
			boolean flag = false;

			boolean f = false;

			FileReader fr = new FileReader("metadata.csv");
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {

				String[] x = line.split(",");
				if (x[0].equalsIgnoreCase(strTable)) {
					flag = true;
					break;
				}
			}
			for (int i = 0; i < tables.size(); i++) {
				if (tables.get(i).getName().equals(strTable)) {
					t = tables.get(i);

					break;
				}
			}
			boolean indexed=false;
			if(t.getTrees().size()>=1){
				for(int j=0;j<t.getTrees().size();j++)
				{
					BTree x = t.getTrees().get(j);
					String colName = x.Name;
					for(int index=0;index+1<entries.size();index=index+2){
						if(colName.equals(entries.get(index)))
						{
							indexed=true;
							break;
						}
					}
					System.out.println("IN Tree"+t.getTrees().get(j));
				}
			}
			if(indexed==true){
					ArrayList <Object>total=new ArrayList<Object>();
					Comparable result;
					int y;
					//we have several b+trees for same table (several columns)
					for(int v = 0; v<t.getTrees().size();v++ )
					{
						BTree x = t.getTrees().get(v);
						String colName = x.Name;
					   // System.out.println("fl index"+colName);
						for(int index=0;index+1<entries.size();index=index+2){
					    if(colName.equals(entries.get(index))){
						Comparable colType = (Comparable)t.getColumnNameType().get(colName);
						//System.out.println("ColType"+colType);
						Comparable valCol = (Comparable) entries.get(index+1); 
						//System.out.println("value"+valCol);
						result=(Comparable) x.search(colType);
						Integer val=(Integer)result;
						 y=val.intValue();
						//System.out.println("Result:"+result);
						ArrayList<ArrayList> currentPage = t.getTablesPages()
								.get(y).getAr();
						System.out.println("current:"+currentPage);
						total.add(currentPage);
					    }
						}
							if(strOperator.equalsIgnoreCase("and")){
								for(int j=0;j<total.size();j++){
									for(int i=0;i<entries.size();i++){
										String o=total.get(j).toString();
										String o1=entries.get(i).toString();
										if(!o.contains(o1)){
											total.remove(j);
											break;
										}
										
										
									}	
								}
								
							}
							for (int q = 0; q < total.size(); q++)
								ar.add(total.get(q));
							
							
						
							
						
						
					}
				
			}
			
			else{
				ArrayList<Object> temp = new ArrayList<Object>();
				if (flag) {
					// System.out.println(tables.size());
					
					// System.out.println(t.getName());
					if (t != null) {
						if (t.getTablesPages().isEmpty())
							return null;

						for (int i = 0; i < t.getTablesPages().size(); i++) {
							ArrayList<ArrayList> currentPage = t.getTablesPages()
									.get(i).getAr();
							// System.out.println(t.getTablesPages().get(i).getAr().get(0));
							for (int j = 0; j < currentPage.size(); j++) {
								ArrayList<Object> ss = currentPage.get(j);
								boolean w = false;
								if (strOperator.equalsIgnoreCase("and")) {
					
												for (int z = 0; z < currentPage.size(); z++) {
													for (int x = 0; x < entries.size(); x++) {

														if (currentPage.get(z).contains(
																entries.get(x))) {

															w = true;

														} else {
															w = false;
															// System.out.println(w);
															break;
														} // x++;

													}
													if (w) {
														int sssss = t.getTablesPages().get(i).getAr().indexOf(currentPage.get(z));
														if(t.getTablesPages().get(i).getDeleted().get(sssss).equals(false))
														{
														if (temp.isEmpty())
															temp.add(currentPage.get(z));
														for (int u = 0; u < temp.size(); u++) {
															if (currentPage.get(z).equals(
																	temp.get(u))) {

															} 
															else{
																temp.add(currentPage.get(z));
																
															}
																
														
																
															
																
														}	
														}
														

													}
												}
											
											 
										
									
								}  else {
												for (int x = 0; x < entries.size(); x = x + 1) {
													for (int z = 0; z < ss.size(); z = z + 1) {

														if (ss.get(z).equals(entries.get(x))) {
															// System.out.println("in"+ss.get(z)+",x"+entries.get(x));
															if (!((x + 1) >= entries.size())
																	&& !((z + 1) >= currentPage
																			.size())) {// System.out.println("in1:"+currentPage.get(j).get(z+1)+" "+entries.get(x+1));
																if (currentPage
																		.get(j)
																		.get(z + 1)
																		.equals(entries
																				.get(x + 1))) {
																	// System.out.println("z"+currentPage.get(j).get(z+1)+",x"+entries.get(x+1));
																	f = true;

																	if (strOperator
																			.equalsIgnoreCase("or")) {
																		int sssss = t.getTablesPages().get(i).getAr().indexOf(currentPage.get(j));
																		if(t.getTablesPages().get(i).getDeleted().get(sssss).equals(false)){
																		ar.add(currentPage
																				.get(j));	
																		
																		}
																		

																		// System.out.println(currentPage.get(j));

																		// return (Iterator)
																		// currentPage.get(j).iterator();
																	}
																}
															}
														}
													}
												}	
											
										
										
									
								}
								
							}
						}
					}
					if (strOperator.equalsIgnoreCase("or")) {
						for (int i = 0; i < ar.size(); i++) {
							for (int j=i+1; j < ar.size(); j++) {
								if (ar.get(i).equals(ar.get(j))){
									ar.remove(i);
								}
									
							}
						}
					}

					for (int q = 0; q < temp.size(); q++)
						ar.add(temp.get(q));
			}
			

				
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
return ar.iterator();
		
	}

	public static void main(String[] args) throws Exception {
		// creat a new DBApp
		DBApp myDB = new DBApp();

		// initialize it
		myDB.init();

		// creating table "Faculty"

		
		  Hashtable<String, String> fTblColNameType = new Hashtable<String,
		  String>(); fTblColNameType.put("ID", "Integer");
		  fTblColNameType.put("Name", "String");
		  
		  Hashtable<String, String> fTblColNameRefs = new Hashtable<String,
		  String>();
		  
		  myDB.createTable("Faculty", fTblColNameType, fTblColNameRefs, "ID");
		  
		  // creating table "Major"
		  
		  Hashtable<String, String> mTblColNameType = new Hashtable<String,
		  String>(); mTblColNameType.put("ID", "Integer");
		  mTblColNameType.put("Name", "String");
		  mTblColNameType.put("Faculty_ID", "Integer");
		  
		  Hashtable<String, String> mTblColNameRefs = new Hashtable<String,
		  String>(); mTblColNameRefs.put("Faculty_ID", "Faculty.ID");
		  
		  myDB.createTable("Major", mTblColNameType, mTblColNameRefs, "ID");
		  
		  // creating table "Course"
		  
		  Hashtable<String, String> coTblColNameType = new Hashtable<String,
		  String>(); coTblColNameType.put("ID", "Integer");
		  coTblColNameType.put("Name", "String"); coTblColNameType.put("Code",
		  "String"); coTblColNameType.put("Hours", "Integer");
		  coTblColNameType.put("Semester", "Integer");
		  coTblColNameType.put("Major_ID", "Integer");
		 
		  Hashtable<String, String> coTblColNameRefs = new Hashtable<String,
		  String>(); coTblColNameRefs.put("Major_ID", "Major.ID");
		  
		  myDB.createTable("Course", coTblColNameType, coTblColNameRefs, "ID");
		  
		  // creating table "Student"
		  
		  Hashtable<String, String> stTblColNameType = new Hashtable<String,
		  String>(); stTblColNameType.put("ID", "Integer");
		  stTblColNameType.put("First_Name", "String");
		  stTblColNameType.put("Last_Name", "String");
		  stTblColNameType.put("GPA", "Double"); stTblColNameType.put("Age",
		  "Integer");
		  
		  Hashtable<String, String> stTblColNameRefs = new Hashtable<String,
		  String>();
		  
		  myDB.createTable("Student", stTblColNameType, stTblColNameRefs,
		  "ID");
		  
		  // creating table "Student in Course" 
		  Hashtable<String, String>
		  scTblColNameType = new Hashtable<String, String>();
		 scTblColNameType.put("ID", "Integer");
		  scTblColNameType.put("Student_ID", "Integer");
		  scTblColNameType.put("Course_ID", "Integer");
		  
		  Hashtable<String, String> scTblColNameRefs = new Hashtable<String,
		  String>(); scTblColNameRefs.put("Student_ID", "Student.ID");
		  scTblColNameRefs.put("Course_ID", "Course.ID");
		  
		  myDB.createTable("Student_in_Course", scTblColNameType,
		  scTblColNameRefs, "ID");
		  
		  // insert in table "Faculty"
		 
		  Hashtable<String, Object> ftblColNameValue1 = new Hashtable<String,
		  Object>(); ftblColNameValue1.put("ID", Integer.valueOf("1"));
		  ftblColNameValue1.put("Name", "Media Engineering and Technology");
		  myDB.insertIntoTable("Faculty", ftblColNameValue1);
		  
		  Hashtable<String, Object> ftblColNameValue2 = new Hashtable<String,
		  Object>(); ftblColNameValue2.put("ID", Integer.valueOf("2"));
		  ftblColNameValue2.put("Name", "Management Technology");
		  myDB.insertIntoTable("Faculty", ftblColNameValue2);
		  
		  for (int i = 0; i < 198; i++) { Hashtable<String, Object>
		  ftblColNameValueI = new Hashtable<String, Object>();
		  ftblColNameValueI.put("ID", Integer.valueOf(("" + (i + 2))));
		  ftblColNameValueI.put("Name", "f" + (i + 2));
		  myDB.insertIntoTable("Faculty", ftblColNameValueI); }
		  
		  Hashtable<String, Object> entries = new Hashtable<String, Object>();
		  entries.put("ID", Integer.valueOf(("" + 200))); entries.put("Name",
		  "f" + 200); myDB.insertIntoTable("Faculty", entries);
		  
		  // insert in table "Major"
		  
		  Hashtable<String, Object> mtblColNameValue1 = new Hashtable<String,
		  Object>(); mtblColNameValue1.put("ID", Integer.valueOf("1"));
		  mtblColNameValue1.put("Name", "Computer Science & Engineering");
		  mtblColNameValue1.put("Faculty_ID", Integer.valueOf("1"));
		  myDB.insertIntoTable("Major", mtblColNameValue1);
		  
		  Hashtable<String, Object> mtblColNameValue2 = new Hashtable<String,
		  Object>(); mtblColNameValue2.put("ID", Integer.valueOf("2"));
		  mtblColNameValue2.put("Name", "Business Informatics");
		  mtblColNameValue2.put("Faculty_ID", Integer.valueOf("2"));
		  myDB.insertIntoTable("Major", mtblColNameValue2);
		  
		  for (int i = 0; i < 300; i++) { Hashtable<String, Object>
		  mtblColNameValueI = new Hashtable<String, Object>();
		  mtblColNameValueI.put("ID", Integer.valueOf(("" + (i + 2))));
		  mtblColNameValueI.put("Name", "m" + (i + 2)); mtblColNameValueI
		  .put("Faculty_ID", Integer.valueOf(("" + (i + 2))));
		  myDB.insertIntoTable("Major", mtblColNameValueI); } 
		  // insert intable "Course"
		  
		  Hashtable<String, Object> ctblColNameValue1 = new Hashtable<String,
		  Object>(); ctblColNameValue1.put("ID", Integer.valueOf("1"));
		  ctblColNameValue1.put("Name", "Data Bases II");
		  ctblColNameValue1.put("Code", "CSEN 604");
		  ctblColNameValue1.put("Hours", Integer.valueOf("4"));
		  ctblColNameValue1.put("Semester", Integer.valueOf("6"));
		  ctblColNameValue1.put("Major_ID", Integer.valueOf("1"));
		  myDB.insertIntoTable("Course", mtblColNameValue1);
		  
		  /*Hashtable<String, Object> ctblColNameValue2 = new Hashtable<String,
		  Object>(); ctblColNameValue2.put("ID", Integer.valueOf("1"));
		  ctblColNameValue2.put("Name", "Data Bases II");
		  ctblColNameValue2.put("Code", "CSEN 604");
		  ctblColNameValue2.put("Hours", Integer.valueOf("4"));
		  ctblColNameValue2.put("Semester", Integer.valueOf("6"));
		  ctblColNameValue2.put("Major_ID", Integer.valueOf("2"));
		  myDB.insertIntoTable("Course", mtblColNameValue2);
		  
		  for (int i = 0; i < 300; i++) { Hashtable<String, Object>
		  ctblColNameValueI = new Hashtable<String, Object>();
		  ctblColNameValueI.put("ID", Integer.valueOf(("" + (i + 2))));
		  ctblColNameValueI.put("Name", "c" + (i + 2));
		  ctblColNameValueI.put("Code", "co " + (i + 2));
		  ctblColNameValueI.put("Hours", Integer.valueOf("4"));
		  ctblColNameValueI.put("Semester", Integer.valueOf("6"));
		  ctblColNameValueI.put("Major_ID", Integer.valueOf(("" + (i + 2))));
		  myDB.insertIntoTable("Course", ctblColNameValueI); }
		  
		  // insert in table "Student"
		  
		  for (int i = 0; i < 300; i++) { Hashtable<String, Object>
		  sttblColNameValueI = new Hashtable<String, Object>();
		  sttblColNameValueI.put("ID", Integer.valueOf(("" + i)));
		  sttblColNameValueI.put("First_Name", "FN" + i);
		  sttblColNameValueI.put("Last_Name", "LN" + i);
		  sttblColNameValueI.put("GPA", Double.valueOf("0.7"));
		  sttblColNameValueI.put("Age", Integer.valueOf("20"));
		  myDB.insertIntoTable("Student", sttblColNameValueI); } 
		  // changed it  to // student instead of course
		 
		  // selecting
		  
		 /* Hashtable<String, Object> stblColNameValue = new Hashtable<String,
		  Object>(); stblColNameValue.put("ID", Integer.valueOf("1"));
		  stblColNameValue.put("Age", Integer.valueOf("20"));
		  stblColNameValue.put("GPA", Double.valueOf("0.7")); long startTime =
		  System.currentTimeMillis(); Iterator myIt = myDB
		  .selectFromTable("Student", stblColNameValue, "AND"); long endTime =
		  System.currentTimeMillis(); long totalTime = endTime - startTime;
		  System.out.println(totalTime); System.out.println("first select");
		  while (myIt.hasNext()) { System.out.println(myIt.next()); }*/
		  
		  // feel free to add more tests
		  Hashtable<String, Object>
		  stblColNameValue3 = new Hashtable<String, Object>();
		  stblColNameValue3.put("Name", "m7");
		  stblColNameValue3.put("Faculty_ID", Integer.valueOf("7"));
		  
		  long startTime2 = System.currentTimeMillis(); Iterator myIt2 = myDB
		  .selectFromTable("Major", stblColNameValue3, "AND"); long endTime2 =
		  System.currentTimeMillis(); long totalTime2 = endTime2 - startTime2;
		  System.out.println(totalTime2); System.out.println("second select");
		  while (myIt2.hasNext()) { System.out.println(myIt2.next()); }
		  
	 for (int i = 0; i < 300; i++) { Hashtable<String, Object>
		  sttblColNameValueI = new Hashtable<String, Object>();
		  sttblColNameValueI.put("ID", Integer.valueOf(("" + i)));
		  sttblColNameValueI.put("First_Name", "FN " + i);
		  sttblColNameValueI.put("Last_Name", "LN " + i);
		  sttblColNameValueI.put("GPA", Double.valueOf("0.7"));
		  sttblColNameValueI.put("Age", Integer.valueOf("20"));
		  myDB.insertIntoTable("Student", sttblColNameValueI); 
		  // changed it tostudent instead of course 
		  }
		 

		// selecting

		// Page.readPage("Student1");
		  
		  
			Hashtable<String, Object> tt = new Hashtable<String,
					  Object>(); 
					  tt.put("ID", Integer.valueOf(("1")));
					  tt.put("First_Name", "FNaaa" + 1);
					 tt.put("Last_Name", "LN" + 1);
					 tt.put("GPA", Double.valueOf("0.7"));
					 tt.put("Age", Integer.valueOf("20"));
					  myDB.insertIntoTable("Student", tt);
		  
		  
		  /*Hashtable<String, Object> stblColNameValuez = new Hashtable<String, Object>();
			stblColNameValuez.put("ID", Integer.valueOf("1"));
			stblColNameValuez.put("Age", Integer.valueOf("20"));
		  myDB.deleteFromTable("Student", stblColNameValuez, "And");*/
		
		Hashtable<String, Object> stblColNameValue5 = new Hashtable<String, Object>();
		stblColNameValue5.put("ID", Integer.valueOf("1"));
		stblColNameValue5.put("Age", Integer.valueOf("20"));
		// stblColNameValue5.put("GPA", Double.valueOf("0.08"));
		// stblColNameValue5.put("First_Name", "FN"+i);
		long startTime1 = System.currentTimeMillis();
		Iterator myIt1 = myDB.selectFromTable("Student", stblColNameValue5,
				"Or");
		long endTime1 = System.currentTimeMillis();
		long totalTime1 = endTime1 - startTime1;
		System.out.println(totalTime1);
		System.out.println("third select");
		//System.out.println(myIt1.next());
		while (myIt1.hasNext()) {
			System.out.print(myIt1.next() + ",");

		}
		System.out.println();
		Hashtable<String, Object> stblColNameValue0 = new Hashtable<String, Object>();
		//	stblColNameValuez.put("ID", Integer.valueOf("1"));
			stblColNameValue0.put("Age", Integer.valueOf("25"));
			stblColNameValue0.put("First_Name", "FN"+4);
			stblColNameValue0.put("GPA", Double.valueOf("0.9"));
		myDB.updateTable("Student", "200", stblColNameValue0);
		// Page.readPage("Student2");

		//Page.readPage("Faculty1");
		
		
		// myDB.createIndex("Faculty", "ID");
		

	}

}