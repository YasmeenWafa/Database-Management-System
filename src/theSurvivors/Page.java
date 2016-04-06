package theSurvivors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import theSurvivors.DBApp;

public class Page implements Serializable {
	// 1 d array of array of objects (size 200)
	ArrayList<ArrayList> ar;
	int index;
	String Name;
	ArrayList<Boolean> deleted; //if something is deleted then its index will be true else false

	public Page(String Name) throws FileNotFoundException, IOException {
		this.Name = Name;
		ar = new ArrayList<ArrayList>(200);
		index = 0;
		deleted = new ArrayList<Boolean> (200);
		createPage();
	}

	public ArrayList<Boolean> getDeleted() {
		return deleted;
	}

	public void setDeleted(ArrayList<Boolean> deleted) {
		this.deleted = deleted;
	}

	public void createPage() throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File(Name + ".class")));
		oos.writeObject(this);
		oos.close();
	}

	public static Page readPage(String Name) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				new File(Name + ".class")));
		Page p = (Page) ois.readObject();
		ois.close();
		System.out.println(p.ar);
		return p;

	}

	public void add(ArrayList s) throws IOException, ClassNotFoundException {
		ar.add(index, s);
		deleted.add(index, false);
		index++;

	}
	public void remove(ArrayList s)
	{
		int place = ar.indexOf(s);
		deleted.set(place, true);
		System.out.println(deleted);
	}

	public ArrayList<ArrayList> getAr() {
		return ar;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setAr(ArrayList<ArrayList> ar) {
		this.ar = ar;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String toString() {
		String res = "";
		for (int i = 0; i < index; i++) {
			res += ar.get(i);
			res += '\n';
		}
		return res;
	}

	public static void main(String[] args) throws Exception {

	}

}
