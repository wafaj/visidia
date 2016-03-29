package visidia.io.gml.parser;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;


/**
 * Set of pair (key, value). The content of this set can be queried by label.
 */

public class GMLList {

	Hashtable<String, Vector<Object>> hash = new Hashtable<String, Vector<Object>>();

	/**
	 */
	public Enumeration<Object> getValues(String key) {
		Vector<Object> vect = (Vector<Object>) this.hash.get(key);
		if (vect != null) {
			return vect.elements();
		}

		return new EmptyEnumeration();
	}

	public Object getValue(String key) {
		Vector<Object> vect = (Vector<Object>) this.hash.get(key);
		if (vect != null) {
			return vect.get(0);
		}

		return null;
	}

	/**
	 */
	public void add(String key, Object value) {
		if (this.hash.containsKey(key)) {
			((Vector<Object>) this.hash.get(key)).add(value);
		} else {
			Vector<Object> vect = new Vector<Object>(1, 5);
			vect.add(value);
			this.hash.put(key, vect);
		}

	}

	public String toString(){
		String txt ="";
		Set<Entry<String, Vector<Object>>> ens = hash.entrySet();
		Iterator<Entry<String, Vector<Object>>> i = ens.iterator();
		while (i.hasNext()){
			Map.Entry<String, Vector<Object>> entree = (Map.Entry<String, Vector<Object>>) i.next();
			txt += "\""+entree.getKey() +"\",{\n"+ entree.getValue().toString()+"\n}\n";
		}
		return txt;
	}
}


class EmptyEnumeration implements Enumeration<Object> {
	public boolean hasMoreElements() {
		return false;
	}

	public Object nextElement() {
		throw new NoSuchElementException("empty enumeration");
	}
}
