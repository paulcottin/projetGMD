package modele;

import java.util.ArrayList;

public class Disease {
	
	String name;
	ArrayList<String> treatment, cause, symptoms;
	
	public Disease() {
		name = "";
		treatment = new ArrayList<String>();
		cause = new ArrayList<String>();
		symptoms = new ArrayList<String>();
	}
	
	public Disease(String name, ArrayList<String> treatment, ArrayList<String> cause, ArrayList<String> symptoms){
		this.name = name;
		this.treatment = treatment;
		this.cause = cause;
		this.symptoms = symptoms;
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

}
