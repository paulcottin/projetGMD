package modele;

import java.util.ArrayList;
import java.util.Observable;

public class SearchHandler extends Observable {

	private ArrayList<Element> el;
	private String medic, disease;
	private Search search;
	private boolean xml_b, sql_b, txt_b, couchDB_b;
	private int mode;
	private Statistics stats;
	
	public SearchHandler() {
		init();
	}
	
	private void init(){
		el = new ArrayList<Element>();
		medic = "";
		disease = "";
		search = new Search();
		this.stats = new Statistics();
		this.couchDB_b = true;
		this.txt_b = true;
		this.sql_b = true;
		this.xml_b = true;
		this.mode = Search.OR;
	}
	
	
	public void search(){
		System.out.println("recherche");
	}

	public ArrayList<Element> getEl() {
		return el;
	}

	public void setEl(ArrayList<Element> el) {
		this.el = el;
	}

	public String getMedic() {
		return medic;
	}

	public void setMedic(String medic) {
		this.medic = medic;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public boolean isXml_b() {
		return xml_b;
	}

	public void setXml_b(boolean xml_b) {
		this.xml_b = xml_b;
	}

	public boolean isSql_b() {
		return sql_b;
	}

	public void setSql_b(boolean sql_b) {
		this.sql_b = sql_b;
	}

	public boolean isTxt_b() {
		return txt_b;
	}

	public void setTxt_b(boolean txt_b) {
		this.txt_b = txt_b;
	}

	public boolean isCouchDB_b() {
		return couchDB_b;
	}

	public void setCouchDB_b(boolean couchDB_b) {
		this.couchDB_b = couchDB_b;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public Statistics getStats() {
		return stats;
	}

	public void setStats(Statistics stats) {
		this.stats = stats;
	}
}
