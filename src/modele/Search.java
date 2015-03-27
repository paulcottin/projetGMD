package modele;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import exceptions.NotFoundException;

import requests.XMLSearch;

public class Search extends Observable{
	
	private String medic, disease, xmlPath, txtPath, outPath;
	private XMLSearch xml;
	private Merger merger;
	private ArrayList<Element> el;
	
	public Search() {
		this.disease = "";
		this.medic = "";
		this.xmlPath = "drugbankTest.xml";
		this.txtPath = "";
		this.outPath = "out.txt";
		this.xml = new XMLSearch(medic, disease, xmlPath);
		this.el = new ArrayList<Element>();
		this.merger = new Merger();
	}
	
	public Search(String medic, String disease){
		this.xmlPath = "drugbankTest.xml";
		this.txtPath = "";
		this.outPath = "out.txt";
		this.medic = medic;
		this.disease = disease;
		this.xml = new XMLSearch(medic, disease, xmlPath);
		this.el = new ArrayList<Element>();
		this.merger = new Merger();
	}
	
	public void search(){
		el = new ArrayList<Element>();
		xml.setDSearch(disease);
		xml.setMSearch(medic);
		System.out.println(medic+", "+ disease);
		try {
			for (Element e : xml.getInfos()) {
				el.add(e);
			}
		} catch (NotFoundException e) {
			/**
			 * Gérer avec une fenetre le fait qu'il n'y ai aucun résultat.
			 */
			System.out.println(e.getMessage());
		}
		//exécution des différentes requêtes sur les sources txt, SQL, CouchDB
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

}
