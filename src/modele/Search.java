package modele;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JOptionPane;

import exceptions.NotFoundException;

import requests.SQLSearch;
import requests.TextSearch;
import requests.XMLSearch;

public class Search extends Observable{
	
	private String medic, disease, xmlPath, txtPath, csvPath, outPath;
	private boolean xml_b, txt_b, couch_b, sql_b;
	private XMLSearch xml;
	private SQLSearch sql;
	private TextSearch txt;
	private Merger merger;
	private ArrayList<Element> el;
	
	public Search() {
		init();
		this.xml = new XMLSearch(medic, disease, xmlPath);
		this.txt = new TextSearch(disease, txtPath, csvPath);
//		this.sql = new SQLSearch();
	}
	
	public Search(String medic, String disease){
		init();
		this.medic = medic;
		this.disease = disease;
		this.xml = new XMLSearch(medic, disease, xmlPath);
		this.txt = new TextSearch(disease, txtPath, csvPath);
	}
	
	private void init(){
		this.disease = "";
		this.medic = "";
		this.couch_b = false;
		this.txt_b = true;
		this.sql_b = false;
		this.xml_b = false;
		this.xmlPath = "drugbank.xml";
		this.txtPath = "_text.txt";
		this.csvPath = "omim_onto.csv";
		this.outPath = "out.txt";
		this.el = new ArrayList<Element>();
		this.merger = new Merger();
	}
	
	public void search(){
		el = new ArrayList<Element>();
		xml.setDSearch(disease);
		xml.setMSearch(medic);
		if (xml_b) {
			try {
				for (Element e : xml.getInfos()) {
					el.add(e);
				}
			} catch (NotFoundException e) {
				e.execute();
			}
		}
		if (couch_b) {
			//ajout des résultats de couchDB
		}
		if (sql_b) {
			//Ajout des résultats de SQL
		}
		txt.setDsearch(disease);
		if (txt_b) {
			for (Element e : txt.getInfos()) {
				el.add(e);
			}
		}
		//Merge des résultats
		try {
			this.writer(el, outPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setChanged();
		notifyObservers();
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

}
