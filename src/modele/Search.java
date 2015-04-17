package modele;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import exceptions.NotFoundException;

import requests.CouchDBSearch;
import requests.SQLSearch;
import requests.TextSearch;
import requests.XMLSearch;

public class Search extends Observable{
	
	private String medic, disease, xmlPath, txtPath, csvPath, outPath;
	private boolean xml_b, txt_b, couch_b, sql_b;
	private XMLSearch xml;
	private SQLSearch sql;
	private TextSearch txt;
	private CouchDBSearch couchDB;
	private Merger merger;
	private ArrayList<Element> el;
	private Statistics stats;
	
	public Search() {
		init();
		this.xml = new XMLSearch(medic, disease, xmlPath);
		this.txt = new TextSearch(disease, txtPath, csvPath);
		this.couchDB = new CouchDBSearch(disease);
		this.sql = new SQLSearch(medic, disease);
	}
	
	public Search(String medic, String disease){
		init();
		this.medic = medic;
		this.disease = disease;
		this.xml = new XMLSearch(medic, disease, xmlPath);
		this.txt = new TextSearch(disease, txtPath, csvPath);
		this.couchDB = new CouchDBSearch(disease);
		this.sql = new SQLSearch(medic, disease);
	}
	
	private void init(){
		this.disease = "";
		this.medic = "";
		this.couch_b = true;
		this.txt_b = true;
		this.sql_b = true;
		this.xml_b = true;
		this.xmlPath = "drugbankTest.xml";
		this.txtPath = "_text.txt";
		this.csvPath = "omim_onto.csv";
		this.outPath = "out.txt";
		this.el = new ArrayList<Element>();
		this.merger = new Merger();
		this.stats = new Statistics();
	}
	
	public void search(){
		el = new ArrayList<Element>();
		el.clear();
//		System.out.println("el : "+el.toString());
		ArrayList<Element> xml_array = new ArrayList<Element>();
		xml_array.clear();
		ArrayList<Element> txt_array = new ArrayList<Element>();
		txt_array.clear();
		ArrayList<Element> couchDB_array = new ArrayList<Element>();
		couchDB_array.clear();
		ArrayList<Element> sql_array = new ArrayList<Element>();
		sql_array.clear();
		
		//On test si c'est une recherche exacte ou un joker (qqchose*)
		nameHelper();
		
		stats.setXmlBegin(GregorianCalendar.getInstance());
		if (xml_b) {
			for (Element e : xml.getInfos()) {
				xml_array.add(e);
			}
		}
		stats.setXmlEnd(GregorianCalendar.getInstance());
		stats.setXmlNumber(xml_array.size());
		update();
//		System.out.println("XML ok, "+xml_array.size()+" result(s)");
		stats.setCouchDbBegin(GregorianCalendar.getInstance());
		if (couch_b) {
			for (Element e : couchDB.search()) {
				couchDB_array.add(e);
			}
		}
		stats.setCouchDbEnd(GregorianCalendar.getInstance());
		stats.setCouchDbNumber(couchDB_array.size());
//		System.out.println("CouchDB ok, "+couchDB_array.size()+" result(s)");
		stats.setSqlBegin(GregorianCalendar.getInstance());
		if (sql_b) {
			for (Element element : sql.search()) {
				sql_array.add(element);
			}
		}
		stats.setSqlEnd(GregorianCalendar.getInstance());
		stats.setSqlNumber(sql_array.size());
		update();
//		System.out.println("SQL ok, "+sql_array.size()+" result(s)");
		stats.setTxtBegin(GregorianCalendar.getInstance());
		if (txt_b) {
			for (Element e : txt.getInfos()) {
				txt_array.add(e);
			}
		}
		stats.setTxtEnd(GregorianCalendar.getInstance());
		stats.setTxtNumber(txt_array.size());
		update();
//		System.out.println("TXT ok, "+txt_array.size()+" result(s)");
//		System.out.println("***\n"+txt_array.toString()+"\n***");
//		el.addAll(couchDB_array);
//		el.addAll(sql_array);
//		el.addAll(txt_array);
//		el.addAll(xml_array);
		//Merge des résultats
		ArrayList<Element> t = new ArrayList<Element>();
		t.clear();
		for (Element element : xml_array) {
			t = merger.merge(element, el);
			if (t.size() > 0) {
				el.addAll(t);
			}
		}
//		System.out.println("t.size : "+t.size());
		el.addAll(t);
//		System.out.println("xml merge : "+el.size());
		t.clear();
		for (Element element : couchDB_array) {
			t = merger.merge(element, el);
			if (t.size() > 0) {
				el.addAll(t);
			}
		}
//		System.out.println("t.size : "+t.size());
		el.addAll(t);
//		System.out.println("couchDB merge : "+el.size());
		t.clear();
		for (Element element : txt_array) {
			t = merger.merge(element, el);
			if (t.size() > 0) {
				el.addAll(t);
			}
		}
//		System.out.println("t.size : "+t.size());
		el.addAll(t);
//		System.out.println("txt merge : "+el.size());
		t.clear();
		for (Element element : sql_array) {
			t = merger.merge(element, el);
			if (t.size() > 0) {
				el.addAll(t);
			}
//			else
//				System.out.println("NULL");
//			System.out.println("-------------------");
//			el.addAll(merger.merge(element, el));
		}
		el.addAll(t);
//		System.out.println("sql merge : "+el.size());
//		System.out.println("results : \n"+el.toString());
//		System.out.println("el size : "+el.size());
//		System.out.println("el avant dup : "+el.size());
		el = merger.getOutDuplicates(el);
//		System.out.println("el size : "+el.size());
		stats.setTotalNumber(el.size());
		stats.execute();
//		try {
//			this.writer(el, outPath);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		System.out.println("__ el size : "+el.size());
		if (el.size() == 0) {
			try {
				throw new NotFoundException();
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.execute();
			}
		}
		update();
	}
	
	private void nameHelper(){
		String d = "", m = "";
		if (disease.contains("*")) {
			d = disease.replaceAll("\\*", ".*");
			xml.setDSearch(d);
			txt.setDsearch(d);
			
			d = disease.replaceAll("\\*", "%");
			sql.setDsearch(d);
			
			if (disease.indexOf("*") > 1) {
				d = disease.substring(0, disease.indexOf("*")-1);
				couchDB.setJoker(true);
			}else {
				//On ne recherche pas dans CouchDB
				couch_b = false;
			}
		}else{
			xml.setDSearch(disease);
			txt.setDsearch(disease);
			sql.setDsearch(disease);
			couchDB.setJoker(false);
			couchDB.setdSearch(disease);
		}
		if (medic.contains("*")) {
			m = medic.replaceAll("\\*", ".*");
			xml.setMSearch(m);
			
			m = medic.replaceAll("\\*", "%");
			sql.setMsearch(m);
		}else{
			xml.setMSearch(medic);
			sql.setMsearch(medic);
		}
	}
	
	private void writer(ArrayList<Element> list, String path) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
		for (Element el: list) {
			bw.write("^"+el.getName()+"\n");
			bw.write("&"+el.getTreat()+"\n");
			bw.write("£"+el.getCause()+"\n");
			bw.write("£"+el.getSymptoms()+"\n");
			bw.write("--");
		}
		bw.close();		
	}
	
	private void update(){
		setChanged();
		notifyObservers();
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	public String getTxtPath() {
		return txtPath;
	}

	public void setTxtPath(String txtPath) {
		this.txtPath = txtPath;
	}

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
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

	public boolean isXml_b() {
		return xml_b;
	}

	public void setXml_b(boolean xml_b) {
		this.xml_b = xml_b;
		setChanged();
		notifyObservers();
	}

	public boolean isTxt_b() {
		return txt_b;
	}

	public void setTxt_b(boolean txt_b) {
		this.txt_b = txt_b;
		setChanged();
		notifyObservers();
	}

	public boolean isCouch_b() {
		return couch_b;
	}

	public void setCouch_b(boolean couch_b) {
		this.couch_b = couch_b;
		setChanged();
		notifyObservers();
	}

	public boolean isSql_b() {
		return sql_b;
	}

	public void setSql_b(boolean sql_b) {
		this.sql_b = sql_b;
		setChanged();
		notifyObservers();
	}

	public String getCsvPath() {
		return csvPath;
	}

	public void setCsvPath(String csvPath) {
		this.csvPath = csvPath;
	}

	public Statistics getStats() {
		return stats;
	}

	public void setStats(Statistics stats) {
		this.stats = stats;
	}

}
