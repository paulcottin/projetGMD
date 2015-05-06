package model;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Observable;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import exceptions.NotFoundException;

public class SearchHandler extends Observable {

	private ArrayList<Element> el;
	private ArrayList<ArrayList<Element>> resultList;
	private ArrayList<String> drugList, diseaseList, drugSynonyms, diseaseSynonyms;
	private ArrayList<Synonyms> synonymList;
	private String drug, disease;
	private ArrayList<Search> searchList;
	private boolean xml_b, sql_b, txt_b, couchDB_b;
	private boolean useSynonyms;
	private int mode;
	private Statistics stats;
	private Merger merger;
	private ArrayList<Integer> mergeType;
	private int sortBy, synonymAdvancement, drugSyn, diseaseSyn;
	private Thread synonymsThread;

	public SearchHandler() {
		init();
	}

	private void init(){
		this.el = new ArrayList<Element>();
		this.resultList = new ArrayList<ArrayList<Element>>();
		this.drugList = new ArrayList<String>();
		this.diseaseList = new ArrayList<String>();
		this.synonymList = new ArrayList<Synonyms>();
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
		this.synonymAdvancement = 0;
		this.drugSyn = 0;
		this.diseaseSyn = 0;
		this.drugSynonyms = new ArrayList<String>();
		this.diseaseSynonyms = new ArrayList<String>();
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
			search.setUseSynonyms(useSynonyms);
			searchList.add(search);
		}
	}

	public void search() throws InterruptedException, NotFoundException, CommunicationsException{
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
		drugList.clear();
		diseaseList.clear();

		// 0 - 0
		if (!drug.contains(" AND ") && !drug.contains(" OR ")&& !disease.contains(" AND ") && !disease.contains(" OR ")) {
			drugList.add(drug);
			diseaseList.add(disease);
		}
		// && - 0
		else if (drug.contains(" AND ") && !disease.contains(" OR ") && !disease.contains(" AND ")) {
			String[] t = drug.split(" AND ");
			for (int i = 0; i < t.length; i++) {
//				System.out.println("drug : "+t[i]+", disease : "+disease);
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
//				System.out.println("drug : "+t[i]+", disease : "+disease);
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
//				System.out.println("disease : "+t[i]+", drug : "+drug);
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
//				System.out.println("disease : "+t[i]+", drug : "+drug);
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
//					System.out.println("disease : "+m[i]+", drug : "+d[j]);
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
//					System.out.println("disease : "+m[i]+", drug : "+d[j]);
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
//					System.out.println("disease : "+m[i]+", drug : "+d[j]);
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
//					System.out.println("disease : "+m[i]+", drug : "+d[j]);
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
			System.out.println("error");

		//If using of synonyms
		if (useSynonyms) {
			diseaseSynonyms = getSynonyms(disease);
			drugSynonyms = getSynonyms(drug);

			this.drugSyn = drugSynonyms.size();
			this.diseaseSyn = diseaseSynonyms.size();

			for (int i = 0; i < diseaseSynonyms.size(); i++) {
				diseaseList.add(diseaseSynonyms.get(i));
				drugList.add("");
			}
			for (int i = 0; i < drugSynonyms.size(); i++) {
				diseaseList.add("");
				drugList.add(drugSynonyms.get(i));
			}
			for (int i = 0; i < diseaseSynonyms.size()+drugSynonyms.size()+1; i++) {
				mergeType.add(Merger.INCLUSIVE_MERGE);
			}
		}
	}

	private ArrayList<String> getSynonyms(String s){
		if (s.equals(""))
			return new ArrayList<String>();
		ArrayList<String> list = new ArrayList<String>();
		for (Synonyms string : synonymList) {
			ArrayList<String> l = string.getSyns(s);
			if (l.size() > 0) {
				list.addAll(l);
			}
		}
		return list;
	}

	public void initSynonyms(){
		synonymsThread = new Thread(){
			public void run(){
				Starter xml = new Starter("XML");
				Thread xml_thread = new Thread(xml);
				Starter sql = new Starter("SQL");
				Thread sql_thread = new Thread(sql);
				Starter csv = new Starter("CSV");
				Thread csv_thread = new Thread(csv);
				Starter couchDB = new Starter("CouchDB");
				Thread couchDB_thread = new Thread(couchDB);

				couchDB_thread.start();
				xml_thread.start();
				csv_thread.start();
				sql_thread.start();

				try {
					xml_thread.join();
					synonymAdvancement++;
					update();
					sql_thread.join();
					synonymAdvancement++;
					update();
					csv_thread.join();
					synonymAdvancement++;
					update();
					couchDB_thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synonymList.addAll(xml.getSynsList());
				synonymList.addAll(sql.getSynsList());
				synonymList.addAll(csv.getSynsList());
				synonymList.addAll(couchDB.getSynsList());
				
				synonymAdvancement++;
				update();
			}
		};
		synonymsThread.start();
	}



	@SuppressWarnings("unchecked")
	private void getResults() throws InterruptedException{
		for (Search search : searchList) {
			resultList.add(search.getEl());
//			System.out.println("results : "+search.getEl().size());
		}
		ArrayList<Element> l1 = new ArrayList<Element>(), l2 = new ArrayList<Element>();
		for (int i = 0; i < mergeType.size(); i++) {
			l1.clear();
			l2.clear();
			l1 = resultList.get((i*2)%resultList.size());
			l2 = resultList.get((i*2+1)%resultList.size());
//			System.out.println("l1 : \n\t"+l1.toString()+"\nl2 : \n\t"+l2.toString());

			Merger m = new Merger();
			m.setList1(l1);
			m.setList2(l2);
			m.setMergeType(mergeType.get(i));
			m.setDisease(diseaseList.get(i));
			m.setDrug(drugList.get(i));

			m.run();
//			System.out.println("list2 : "+m.getList2().toString());
			resultList.set((resultList.size() >= 2) ? i%(resultList.size()/(2)) : 0, (ArrayList<Element>) m.getList2().clone());
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

	public void update(){
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

	public Thread getSynonymsThread() {
		return synonymsThread;
	}

	public void setSynonymsThread(Thread synonymsThread) {
		this.synonymsThread = synonymsThread;
	}

	public int getSynonymAdvancement() {
		return synonymAdvancement;
	}

	public void setSynonymAdvancement(int synonymAdvancement) {
		this.synonymAdvancement = synonymAdvancement;
	}

	public int getDrugSyn() {
		return drugSyn;
	}

	public void setDrugSyn(int drugSyn) {
		this.drugSyn = drugSyn;
	}

	public int getDiseaseSyn() {
		return diseaseSyn;
	}

	public void setDiseaseSyn(int diseaseSyn) {
		this.diseaseSyn = diseaseSyn;
	}

	public ArrayList<Synonyms> getSynonymList() {
		return synonymList;
	}

	public void setSynonymList(ArrayList<Synonyms> synonymList) {
		this.synonymList = synonymList;
	}

	public ArrayList<String> getDrugSynonyms() {
		return drugSynonyms;
	}

	public void setDrugSynonyms(ArrayList<String> drugSynonyms) {
		this.drugSynonyms = drugSynonyms;
	}

	public ArrayList<String> getDiseaseSynonyms() {
		return diseaseSynonyms;
	}

	public void setDiseaseSynonyms(ArrayList<String> diseaseSynonyms) {
		this.diseaseSynonyms = diseaseSynonyms;
	}
}
