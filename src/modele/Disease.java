package modele;

import java.util.ArrayList;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class Disease {
	
	String name;
	ArrayList<String> treatment, cause, symptoms, synonyms;
	
	public Disease() {
		name = "";
		treatment = new ArrayList<String>();
		cause = new ArrayList<String>();
		symptoms = new ArrayList<String>();
		synonyms = new ArrayList<String>();
	}
	
	public Disease(String name, ArrayList<String> treatment, ArrayList<String> cause, ArrayList<String> symptoms, ArrayList<String> synonyms){
		this.name = name;
		this.treatment = treatment;
		this.cause = cause;
		this.symptoms = symptoms;
		this.synonyms = synonyms;
	}
	
	public String toString(){
		return "name : "+name+"\n\ttreatment : "+treatment+"\n\tcause : "+cause+"\n\tsymptoms : "+symptoms+"\n";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getTreatment() {
		return treatment;
	}

	public void setTreatment(ArrayList<String> treatment) {
		this.treatment = treatment;
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

	public ArrayList<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(ArrayList<String> synonyms) {
		this.synonyms = synonyms;
	}

}
