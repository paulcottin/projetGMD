package modele;

import java.util.ArrayList;

public class Medic {
	String name, origin;
	ArrayList<String> synonyms, treat, cause, symptoms;
	
	public Medic() {
		name = "";
		treat = new ArrayList<String>();
		cause = new ArrayList<String>();
		symptoms = new ArrayList<String>();
		synonyms = new ArrayList<String>();
	}
	
	public Medic(String name, ArrayList<String> treat, ArrayList<String> cause, ArrayList<String> synonyms, String origin){
		this.name = name;
		this.treat = treat;
		this.cause = cause;
		this.synonyms = synonyms;
		this.origin = origin;
	}
	
	public String toString(){
		String syn = "\tsynonyms : \n";
		for (String s : synonyms) {
			syn += "\t\t"+s+"\n";
		}
		return "name : "+name+"\n\ttreat: "+treat+"\n\tcause : "+cause+"\n"+syn;
	}

	public ArrayList<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(ArrayList<String> synonyms) {
		this.synonyms = synonyms;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getTreat() {
		return treat;
	}

	public void setTreat(ArrayList<String> treat) {
		this.treat = treat;
	}

	public ArrayList<String> getCause() {
		return cause;
	}

	public void setCause(ArrayList<String> cause) {
		this.cause = cause;
	}

	public ArrayList<String> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(ArrayList<String> symptoms) {
		this.symptoms = symptoms;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
}
