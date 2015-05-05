package model;

import java.util.ArrayList;

public class Synonyms {

	private String name;
	private ArrayList<String> synonyms;
	
	public Synonyms() {
		init();
		remplissageTest();
	}
	
	private void init(){
		this.name = "";
		this.synonyms = new ArrayList<String>();
	}
	
	private void remplissageTest(){
		name = "Lepirudin";
		synonyms.add("Dornase alfa");
		synonyms.add("Cetuximab");
	}
	
	public boolean hadSynonyms(String s){
		if (synonyms.contains(s)) {
			return true;
		}
		return false;
	}
	
	public ArrayList<String> getSynonyms(String s){
		if (name.equalsIgnoreCase(s)) {
			return synonyms;
		}
		else if (synonyms.contains(s)) {
			ArrayList<String> l = new ArrayList<String>();
			for (String string : synonyms) {
				if (!string.equals(s)) {
					l.add(s);
				}
			}
			l.add(s);
			return l;
		}
		else
			return new ArrayList<String>();
	}
}
