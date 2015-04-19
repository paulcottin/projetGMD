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
	
	public static int AND = 0;
	public static int OR = 1;

	private String medic, disease, xmlPath, txtPath, csvPath, outPath;
	private boolean xml_b, txt_b, couch_b, sql_b;
	private boolean xmlProcBegin, xmlProcEnd, txtProcBegin, txtProcEnd, couchDBProcBegin, couchDBProcEnd, sqlProcBegin, sqlProcEnd;
	private boolean mergeProcBegin, mergeProcEnd;
	private XMLSearch xml;
	private SQLSearch sql;
	private TextSearch txt;
	private CouchDBSearch couchDB;
	private Merger merger;
	private ArrayList<Element> sql_array, txt_array, xml_array, couchDB_array;
	private ArrayList<Element> el;
	private Statistics stats;
	private int mode;

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
		this.xmlProcBegin = false;
		this.xmlProcEnd = false;
		this.sqlProcBegin = false;
		this.sqlProcEnd = false;
		this.txtProcBegin = false;
		this.txtProcEnd = false;
		this.couchDBProcBegin = false;
		this.couchDBProcEnd = false;
		this.mergeProcBegin = false;
		this.mergeProcEnd = false;
		this.sql_array = new ArrayList<Element>();
		this.txt_array = new ArrayList<Element>();
		this.couchDB_array = new ArrayList<Element>();
		this.xml_array = new ArrayList<Element>();
		this.mode = OR;
	}

	public void search() throws InterruptedException{
		searchClear();

		//On test si c'est une recherche exacte ou un joker (qqchose*)
		nameHelper();

		executeRequests();

		if (mode == OR) {
			doMerge();
		}
		else if (mode == AND) {
			System.out.println("merge special");
		}

		if (el.size() == 0) {
			try {
				throw new NotFoundException();
			} catch (NotFoundException e) {
				e.execute();
			}
		}
		medic = "";
		disease = "";
	}

	private void executeRequests() throws InterruptedException{
		//Déclaration des thread
		Thread couchDB_t = new Thread(couchDB);
		Thread xml_t = new Thread(xml);
		Thread sql_t = new Thread(sql);
		Thread txt_t = new Thread(txt);

		//Lancement des requêtes
		stats.setTotalBegin(GregorianCalendar.getInstance());
		if (xml_b) {
			stats.setXmlBegin(GregorianCalendar.getInstance());
			xmlProcBegin = true;
			update();
			xml_t.start();
		}
		if (couch_b) {
			stats.setCouchDbBegin(GregorianCalendar.getInstance());
			couchDBProcBegin = true;
			update();
			couchDB_t.start();
		}
		if (sql_b) {
			stats.setSqlBegin(GregorianCalendar.getInstance());
			sqlProcBegin = true;
			update();
			sql_t.start();
		}
		if (txt_b) {
			txtProcBegin = true;
			update();
			txt_t.start();
		}

		//Récuperation des resultats

		if (xml_b) {
			xml_t.join();
			for (Element e : xml.getInfos()) {
				xml_array.add(e);
			}
			xmlProcEnd = true;
			update();
		}
		stats.setXmlEnd(GregorianCalendar.getInstance());
		stats.setXmlNumber(xml_array.size());
		stats.execute();
		update();

		if (couch_b) {
			couchDB_t.join();
			for (Element e : couchDB.getReturnList()) {
				couchDB_array.add(e);
			}
			couchDBProcEnd = true;
			update();
		}
		stats.setCouchDbEnd(GregorianCalendar.getInstance());
		stats.setCouchDbNumber(couchDB_array.size());
		//				System.out.println("CouchDB ok, "+couchDB_array.size()+" result(s)");

		if (sql_b) {
			sql_t.join();
			for (Element element : sql.getReturnList()) {
				sql_array.add(element);
			}
			sqlProcEnd = true;
			update();
		}
		stats.setSqlEnd(GregorianCalendar.getInstance());
		stats.setSqlNumber(sql_array.size());
		stats.execute();
		update();
		//				System.out.println("SQL ok, "+sql_array.size()+" result(s)");

		if (txt_b) {
			stats.setTxtBegin(GregorianCalendar.getInstance());
			txt_t.join();
			for (Element e : txt.getReturnList()) {
				txt_array.add(e);
			}
			txtProcEnd = true;
			update();
		}
		stats.setTxtEnd(GregorianCalendar.getInstance());
		stats.setTxtNumber(txt_array.size());
		stats.setTotalEnd(GregorianCalendar.getInstance());
		stats.execute();
		update();
	}

	private void doMerge() throws InterruptedException{
		stats.setMergeBegin(GregorianCalendar.getInstance());
		mergeProcBegin = true;
		update();
		
		Merger xmlMerger = new Merger();
		xmlMerger.setList1(xml_array);
		xmlMerger.setList2(el);
		Thread xmlMergeThread = new Thread(xmlMerger);
		
		Merger sqlMerger = new Merger();
		sqlMerger.setList1(sql_array);
		sqlMerger.setList2(el);
		Thread sqlMergeThread = new Thread(sqlMerger);
		
		Merger txtMerger = new Merger();
		txtMerger.setList1(txt_array);
		txtMerger.setList2(el);
		Thread txtMergeThread = new Thread(txtMerger);
		
		Merger couchDBMerger = new Merger();
		couchDBMerger.setList1(couchDB_array);
		couchDBMerger.setList2(el);
		Thread couchDBMergeThread = new Thread(couchDBMerger);
		
		xmlMergeThread.start();
		sqlMergeThread.start();
		couchDBMergeThread.start();
		txtMergeThread.start();
		
		txtMergeThread.join();
		couchDBMergeThread.join();
		xmlMergeThread.join();
		sqlMergeThread.join();
		synchronized (el) {
			el.addAll(txtMerger.getList2());
			el.addAll(couchDBMerger.getList2());
			el.addAll(xmlMerger.getList2());
			el.addAll(sqlMerger.getList2());
		}
		
		el = merger.getOutDuplicates(el);
		mergeProcEnd = true;
		update();
		
		stats.setTotalNumber(el.size());
		stats.setMergeEnd(GregorianCalendar.getInstance());
		stats.execute();
		update();
	}
	
	private void searchClear(){
		el = new ArrayList<Element>();
		el.clear();
		txt_array.clear();
		couchDB_array.clear();
		xml_array.clear();
		sql_array.clear();
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

	public boolean isXmlProcBegin() {
		return xmlProcBegin;
	}

	public void setXmlProcBegin(boolean xmlProcBegin) {
		this.xmlProcBegin = xmlProcBegin;
	}

	public boolean isXmlProcEnd() {
		return xmlProcEnd;
	}

	public void setXmlProcEnd(boolean xmlProcEnd) {
		this.xmlProcEnd = xmlProcEnd;
	}

	public boolean isTxtProcBegin() {
		return txtProcBegin;
	}

	public void setTxtProcBegin(boolean txtProcBegin) {
		this.txtProcBegin = txtProcBegin;
	}

	public boolean isTxtProcEnd() {
		return txtProcEnd;
	}

	public void setTxtProcEnd(boolean txtProcEnd) {
		this.txtProcEnd = txtProcEnd;
	}

	public boolean isCouchDBProcBegin() {
		return couchDBProcBegin;
	}

	public void setCouchDBProcBegin(boolean couchDBProcBegin) {
		this.couchDBProcBegin = couchDBProcBegin;
	}

	public boolean isCouchDBProcEnd() {
		return couchDBProcEnd;
	}

	public void setCouchDBProcEnd(boolean couchDBProcEnd) {
		this.couchDBProcEnd = couchDBProcEnd;
	}

	public boolean isSqlProcBegin() {
		return sqlProcBegin;
	}

	public void setSqlProcBegin(boolean sqlProcBegin) {
		this.sqlProcBegin = sqlProcBegin;
	}

	public boolean isSqlProcEnd() {
		return sqlProcEnd;
	}

	public void setSqlProcEnd(boolean sqlProcEnd) {
		this.sqlProcEnd = sqlProcEnd;
	}

	public boolean isMergeProcBegin() {
		return mergeProcBegin;
	}

	public void setMergeProcBegin(boolean mergeProcBegin) {
		this.mergeProcBegin = mergeProcBegin;
	}

	public boolean isMergeProcEnd() {
		return mergeProcEnd;
	}

	public void setMergeProcEnd(boolean mergeProcEnd) {
		this.mergeProcEnd = mergeProcEnd;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}
