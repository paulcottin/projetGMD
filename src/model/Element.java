package model;

import java.util.ArrayList;
/**
 * Name : Name of drug
 * Origin : from which source of data does the element come
 * Treat : Name of disease
 * Cause : Secondary Effect of the drug
 * Symptoms : Symptoms of the disease
 * Synonyms : Synonyms of the drug
 * diseaseSynonysms : Synonyms of the disease
 * @author paul
 *
 */

public class Element {

	String name, origin;
	ArrayList<String> synonyms, treat, cause, symptoms, diseaseSynonyms;
	int score;
	
	public Element() {
		name = "";
		treat = new ArrayList<String>();
		cause = new ArrayList<String>();
		symptoms = new ArrayList<String>();
		synonyms = new ArrayList<String>();
		diseaseSynonyms = new ArrayList<String>();
		origin = "";
		score = 0;
	}
	
	public Element(String name, ArrayList<String> treat, ArrayList<String> cause, ArrayList<String> symptoms, ArrayList<String> synonyms, ArrayList<String> diseaseSynonyms, String origin, int score){
		this.name = name;
		this.treat = treat;
		this.cause = cause;
		this.symptoms = symptoms;
		this.synonyms = synonyms;
		this.diseaseSynonyms = diseaseSynonyms;
		this.origin = origin;
		this.score = score;
	}
	
	public String toString(){
		String syn = "";
		String treat = this.treat.toString();
		String cause = "";
		String symptoms = "\n";
		String diseaseSynonyms = "";
		for (String s : synonyms) {
			syn += "\t\t"+s+"\n";
		}
		for (String s : this.cause) {
			treat += "\t\t"+s+"\n";
		}
		for (String s : this.symptoms) {
			symptoms += "\t\t"+s+"\n";
		}
		for (String s : this.diseaseSynonyms) {
			diseaseSynonyms += "\t\t"+s+"\n";
		}
		return "name : "+name+"\n\ttreatment: "+treat+"\n\tcause : "+cause+"\n\tsymptoms : "+symptoms+"\n\t"+syn+"\n\t"+diseaseSynonyms+"\n\torigin : "+origin+"\n score : "+score;
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

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
}
