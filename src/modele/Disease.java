package modele;

public class Disease {
	
	String name, treatment, cause, symptoms;
	
	public Disease() {
		name = "";
		treatment = "";
		cause = "";
		symptoms = "";
	}
	
	public Disease(String name, String treatment, String cause, String symptoms){
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

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
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

}
