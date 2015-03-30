package modele;

import java.util.ArrayList;
/**
 * Name : Name of medic
 * Treat : Name of disease
 * Cause : 
 * Symptoms : Symptoms of the disease
 * Synonyms : Synonyms of the medic
 * @author paul
 *
 */

public class Element {

	String name;
	ArrayList<String> synonyms, treat, cause, symptoms, diseaseSynonyms;
	
	public Element() {
		name = "";
		treat = new ArrayList<String>();
		cause = new ArrayList<String>();
		symptoms = new ArrayList<String>();
		synonyms = new ArrayList<String>();
		diseaseSynonyms = new ArrayList<String>();
	}
	
	public Element(String name, ArrayList<String> treat, ArrayList<String> cause, ArrayList<String> symptoms, ArrayList<String> synonyms, ArrayList<String> diseaseSynonyms){
		this.name = name;
		this.treat = treat;
		this.cause = cause;
		this.symptoms = symptoms;
		this.synonyms = synonyms;
		this.diseaseSynonyms = diseaseSynonyms;
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

	public ArrayList<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(ArrayList<String> synonyms) {
		this.synonyms = synonyms;
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

	public ArrayList<String> getDiseaseSynonyms() {
		return diseaseSynonyms;
	}

	public void setDiseaseSynonyms(ArrayList<String> diseaseSynonyms) {
		this.diseaseSynonyms = diseaseSynonyms;
	}
	
	
}
