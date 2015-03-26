package modele;

import java.util.ArrayList;

public class Element {

	String name, treat, cause, symptoms;
	ArrayList<String> synonyms;
	
	public Element() {
		name = "";
		treat = "";
		cause = "";
		symptoms = "";
		synonyms = new ArrayList<>();
	}
	
	public Element(String name, String treat, String cause, String symptoms, ArrayList<String> synonyms){
		this.name = name;
		this.treat = treat;
		this.cause = cause;
		this.symptoms = symptoms;
		this.synonyms = synonyms;
	}
	
	public String toString(){
		String syn = "\tsynonyms : \n";
		for (String s : synonyms) {
			syn += "\t\t"+s+"\n";
		}
		return "name : "+name+"\n\ttreat: "+treat+"\n\tcause : "+cause+"\n\tsymptoms : "+symptoms+"\n"+syn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTreat() {
		return treat;
	}

	public void setTreat(String treat) {
		this.treat = treat;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public ArrayList<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(ArrayList<String> synonyms) {
		this.synonyms = synonyms;
	}
	
	
}
