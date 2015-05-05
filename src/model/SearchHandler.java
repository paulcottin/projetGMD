package model;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Observable;

import com.mysql.jdbc.Util;
import com.sun.swing.internal.plaf.synth.resources.synth;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import exceptions.NotFoundException;

public class SearchHandler extends Observable {

	private ArrayList<Element> el;
	private ArrayList<ArrayList<Element>> resultList;
	private ArrayList<String> drugList, diseaseList;
	private ArrayList<Synonyms> synonymList;
	private String drug, disease;
	private ArrayList<Search> searchList;
	private boolean xml_b, sql_b, txt_b, couchDB_b;
	private boolean useSynonyms;
	private int mode;
	private Statistics stats;
	private Merger merger;
	private ArrayList<Integer> mergeType;
	private int sortBy;
	
	public SearchHandler() {
		init();
	}
	
	private void init(){
		this.el = new ArrayList<Element>();
		this.resultList = new ArrayList<ArrayList<Element>>();
		this.drugList = new ArrayList<String>();
		this.diseaseList = new ArrayList<String>();
		this.synonymList = new ArrayList<Synonyms>();
		this.synonymList.add(new Synonyms());
		this.drug = "";
		this.disease = "";
		this.searchList = new ArrayList<Search>();
		this.stats = new Statistics();
		this.couchDB_b = true;
		this.txt_b = true;
		this.sql_b = true;
		this.xml_b = true;
		this.mode = Search.OR;
		this.merger = new Merger();
		this.mergeType = new ArrayList<Integer>();
		this.sortBy = -1;
		this.useSynonyms = false;
	}
	
	private void initSearch(){
		searchList.clear();
		for (int i = 0; i < drugList.size(); i++) {
			Search search = new Search();
			search.setDrug(drugList.get(i));
			search.setDisease(diseaseList.get(i));
			search.setStats(stats);
			search.setCouch_b(couchDB_b);
			search.setXml_b(xml_b);
			search.setSql_b(sql_b);
			search.setTxt_b(txt_b);
			search.setMode(this.mode);
			searchList.add(search);
		}
	}
	
	public void search() throws InterruptedException, NotFoundException{
		initRequests();
		
		initSearch();
		
		ArrayList<Thread> threadList = initThread();

		stats.setTotalBegin(GregorianCalendar.getInstance());

		for (Thread thread : threadList) {
			thread.start();
		}
		
		for (Thread thread : threadList) {
			thread.join();
		}
		
		stats.setTotalEnd(GregorianCalendar.getInstance());
		stats.execute();
		stats.setMergeBegin(GregorianCalendar.getInstance());
		getResults();
		stats.setMergeEnd(GregorianCalendar.getInstance());
		
		el = merger.getOutDuplicates(el);
		
		executeStats();
		
		update();
		
		if (el.size() == 0) {
			throw new NotFoundException();
		}
	}
	
	private ArrayList<Thread> initThread(){
		ArrayList<Thread> t = new ArrayList<Thread>();
		for (Search s : searchList) {
			t.add(new Thread(s));
		}
		return t;
	}
	
	private void initRequests(){
		el.clear();
		resultList.clear();
		drugList.clear();
		diseaseList.clear();
		searchList.clear();
		mergeType.clear();
		
		// 0 - 0
		if (!drug.contains(" AND ") && !drug.contains(" OR ")&& !disease.contains(" AND ") && !disease.contains(" OR ")) {
			drugList.add(drug);
			diseaseList.add(disease);
		}
		// && - 0
		else if (drug.contains(" AND ") && !disease.contains(" OR ") && !disease.contains(" AND ")) {
			String[] t = drug.split(" AND ");
			for (int i = 0; i < t.length; i++) {
				System.out.println("drug : "+t[i]+", disease : "+disease);
				drugList.add(t[i]);
				diseaseList.add(disease);
				if (i+1 < t.length) {
					mergeType.add(Merger.EXCLUSIVE_MERGE);
				}
			}
		}
		// || - 0
		else if (drug.contains(" OR ") && !disease.contains(" OR ") && !disease.contains(" AND ")) {
			String[] t = drug.split(" OR ");
			for (int i = 0; i < t.length; i++) {
				System.out.println("drug : "+t[i]+", disease : "+disease);
				drugList.add(t[i]);
				diseaseList.add(disease);
				if (i+1 < t.length) {
					mergeType.add(Merger.INCLUSIVE_MERGE);
				}
			}
		}
		// 0 - &&
		else if (!drug.contains(" AND ") && !drug.contains(" OR ")&& disease.contains(" AND ")) {
			String[] t = disease.split(" AND ");
			for (int i = 0; i < t.length; i++) {
				System.out.println("disease : "+t[i]+", drug : "+drug);
				drugList.add(drug);
				diseaseList.add(t[i]);
				if (i+1 < t.length) {
					mergeType.add(Merger.EXCLUSIVE_MERGE);
				}
			}
		}
		// 0 - ||
		else if (!drug.contains(" AND ") && !drug.contains(" OR ") && disease.contains(" OR ")) {
			String[] t = disease.split(" OR ");
			for (int i = 0; i < t.length; i++) {
				System.out.println("disease : "+t[i]+", drug : "+drug);
				drugList.add(drug);
				diseaseList.add(t[i]);
				if (i+1 < t.length) {
					mergeType.add(Merger.INCLUSIVE_MERGE);
				}
			}
		}
		// && - &&
		else if (drug.contains(" AND ") && !drug.contains(" OR ") && disease.contains(" AND ") && !disease.contains(" OR ")) {
			String[] m = drug.split(" AND ");
			String[] d = disease.split(" AND ");
			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < d.length; j++) {
					System.out.println("disease : "+m[i]+", drug : "+d[j]);
					drugList.add(m[i]);
					diseaseList.add(d[j]);
					if (i+1 < d.length) {
						mergeType.add(Merger.EXCLUSIVE_MERGE);
					}
				}
				if (i+1 < m.length) {
					mergeType.add(Merger.EXCLUSIVE_MERGE);
				}
			}
		}
		// || - ||
		else if (!drug.contains(" AND ") && drug.contains(" OR ") && !disease.contains(" AND ") && disease.contains(" OR ")) {
			String[] m = drug.split(" OR ");
			String[] d = disease.split(" OR ");
			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < d.length; j++) {
					System.out.println("disease : "+m[i]+", drug : "+d[j]);
					drugList.add(m[i]);
					diseaseList.add(d[j]);
					if (i+1 < d.length) {
						mergeType.add(Merger.INCLUSIVE_MERGE);
					}
				}
				if (i+1 < m.length) {
					mergeType.add(Merger.INCLUSIVE_MERGE);
				}
			}
		}
		// && - ||
		else if (drug.contains(" AND ") && !drug.contains(" OR ") && !disease.contains(" AND ") && disease.contains(" OR ")) {
			String[] m = drug.split(" AND ");
			String[] d = disease.split(" OR ");
			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < d.length; j++) {
					System.out.println("disease : "+m[i]+", drug : "+d[j]);
					drugList.add(m[i]);
					diseaseList.add(d[j]);
					if (i+1 < d.length) {
						mergeType.add(Merger.INCLUSIVE_MERGE);
					}
				}
				if (i+1 < m.length) {
					mergeType.add(Merger.EXCLUSIVE_MERGE);
				}
			}
		}
		// || - &&
		else if (!drug.contains(" AND ") && drug.contains(" OR ") && disease.contains(" AND ") && !disease.contains(" OR ")) {
			String[] m = drug.split(" OR ");
			String[] d = disease.split(" AND ");
			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < d.length; j++) {
					System.out.println("disease : "+m[i]+", drug : "+d[j]);
					drugList.add(m[i]);
					diseaseList.add(d[j]);
					if (i+1 < d.length) {
						mergeType.add(Merger.EXCLUSIVE_MERGE);
					}
				}
				if (i+1 < m.length) {
					mergeType.add(Merger.INCLUSIVE_MERGE);
				}
			}
		}
		else
			System.out.println("erreur");

		//Si utilisation de synonym
		if (useSynonyms) {
			ArrayList<String> dList = getSynonyms(disease);
			ArrayList<String> mList = getSynonyms(drug);
			
			for (int i = 0; i < dList.size(); i++) {
				diseaseList.add(dList.get(i));
				drugList.add("");
			}
			for (int i = 0; i < mList.size(); i++) {
				diseaseList.add("");
				drugList.add(mList.get(i));
			}
			for (int i = 0; i < dList.size()+mList.size(); i++) {
				mergeType.add(Merger.INCLUSIVE_MERGE);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void getResults() throws InterruptedException{
		for (Search search : searchList) {
			resultList.add(search.getEl());
		}
		ArrayList<Element> l1 = new ArrayList<Element>(), l2 = new ArrayList<Element>();
		for (int i = 0; i < mergeType.size(); i++) {
			l1.clear();
			l2.clear();
			l1 = resultList.get((i*2)%resultList.size());
			l2 = resultList.get((i*2+1)%resultList.size());
				
			Merger m = new Merger();
			m.setList1(l1);
			m.setList2(l2);
			m.setMergeType(mergeType.get(i));
			m.setDisease(diseaseList.get(i));
			m.setDrug(drugList.get(i));
			
			Thread th = new Thread(m);
			th.start();
			th.join();
			
			resultList.set(i%2, (ArrayList<Element>) m.getList2().clone());
		}
		el.addAll(resultList.get(0));
	}
	
	private void executeStats(){
		int xmlNumber = 0, couchDbNumber = 0, sqlNumber = 0, txtNumber = 0;
		int xmlTime = 0, couchDbTime = 0, sqlTime = 0, txtTime = 0;
		
		for (Search search : searchList) {
			xmlNumber += search.getStats().getXmlNumber();
			couchDbNumber += search.getStats().getCouchDbNumber();
			sqlNumber += search.getStats().getSqlNumber();
			txtNumber += search.getStats().getSqlNumber();
			
			xmlTime += search.getStats().getXmlTime();
			couchDbTime += search.getStats().getCouchDbTime();
			sqlTime += search.getStats().getSqlTime();
			txtTime += search.getStats().getTxtTime();
		}
		
		stats.setXmlNumber(xmlNumber);
		stats.setCouchDbNumber(couchDbNumber);
		stats.setSqlNumber(sqlNumber);
		stats.setTxtNumber(txtNumber);
		
		stats.setXmlTime(xmlTime);
		stats.setCouchDbTime(couchDbTime);
		stats.setTxtTime(txtTime);
		stats.setSqlTime(sqlTime);
		
		stats.setTotalNumber(el.size());
		
		stats.execute();
	}
	
	private ArrayList<String> getSynonyms(String s){
		ArrayList<String> list = new ArrayList<String>();
		for (Synonyms string : synonymList) {
			ArrayList<String> l = string.getSynonyms(s);
			
			if (l.size() > 0) {
				list.addAll(l);
			}
		}
		return list;
	}
	
	private void update(){
		setChanged();
		notifyObservers();
	}

	public ArrayList<Element> getEl() {
		return el;
	}

	public void setEl(ArrayList<Element> el) {
		this.el = el;
	}

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
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

	public int getSortBy() {
		return sortBy;
	}

	public void setSortBy(int sortBy) {
		this.sortBy = sortBy;
	}

	public boolean isUseSynonyms() {
		return useSynonyms;
	}

	public void setUseSynonyms(boolean useSynonyms) {
		this.useSynonyms = useSynonyms;
	}
}
