package vues;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;


import modele.Search;

public class Progress extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String NOT_EXECUTED = "Not executed";
	public static String PROCESSING = "Processing...";
	public static String EXECUTED = "Executed";
	
	private Search search;
	private JLabel xml, sql, couchDB, txt, merge;
	
	public Progress(Search s) {
		this.search = s;
		search.addObserver(this);
		init();
		this.setBackground(Selection.BACKGROUND_COLOR);
		this.setLayout(new GridLayout(5,1));
		
		this.setVisible(true);
	}
	
	private void init(){
		xml = new JLabel("DrugBank : "+NOT_EXECUTED);
		sql = new JLabel("Sider 2 : "+NOT_EXECUTED);
		couchDB = new JLabel("OrphaData : "+NOT_EXECUTED);
		txt = new JLabel("OMIM : "+NOT_EXECUTED);
		merge = new JLabel("Merging : "+NOT_EXECUTED);
		
		xml.setBackground(Selection.BACKGROUND_COLOR);
		sql.setBackground(Selection.BACKGROUND_COLOR);
		couchDB.setBackground(Selection.BACKGROUND_COLOR);
		txt.setBackground(Selection.BACKGROUND_COLOR);
		merge.setBackground(Selection.BACKGROUND_COLOR);
		
		this.add(xml);
		this.add(sql);
		this.add(couchDB);
		this.add(txt);
		this.add(merge);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (!search.isXmlProcBegin()) {
			xml.setText("DrugBank : "+NOT_EXECUTED);
		}
		else if (search.isXmlProcBegin() && !search.isXmlProcEnd()) {
			xml.setText("DrugBank : "+PROCESSING);
		}
		else {
			xml.setText("DrugBank : "+EXECUTED);
		}
		
		if (!search.isCouchDBProcBegin()) {
			couchDB.setText("OrphaData : "+NOT_EXECUTED);
		}
		else if (search.isCouchDBProcBegin() && !search.isCouchDBProcEnd()) {
			couchDB.setText("OrphaData : "+PROCESSING);
		}
		else {
			couchDB.setText("OrphaData : "+EXECUTED);
		}
		
		if (!search.isTxtProcBegin()) {
			txt.setText("OMIM : "+NOT_EXECUTED);
		}
		else if (search.isTxtProcBegin() && !search.isTxtProcEnd()) {
			txt.setText("OMIM : "+PROCESSING);
		}
		else {
			txt.setText("OMIM : "+EXECUTED);
		}
		
		if (!search.isSqlProcBegin()) {
			sql.setText("Sider 2 : "+NOT_EXECUTED);
		}
		else if (search.isSqlProcBegin() && !search.isSqlProcEnd()) {
			sql.setText("Sider 2 : "+PROCESSING);
		}
		else {
			sql.setText("Sider 2 : "+EXECUTED);
		}
		
		if (!search.isMergeProcBegin()) {
			merge.setText("Merging : "+NOT_EXECUTED);
		}
		else if (search.isMergeProcBegin() && !search.isSqlProcEnd()) {
			merge.setText("Merging : "+PROCESSING);
		}
		else {
			merge.setText("Merging : "+EXECUTED);
		}
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public JLabel getXml() {
		return xml;
	}

	public void setXml(JLabel xml) {
		this.xml = xml;
	}

	public JLabel getSql() {
		return sql;
	}

	public void setSql(JLabel sql) {
		this.sql = sql;
	}

	public JLabel getCouchDB() {
		return couchDB;
	}

	public void setCouchDB(JLabel couchDB) {
		this.couchDB = couchDB;
	}

	public JLabel getTxt() {
		return txt;
	}

	public void setTxt(JLabel txt) {
		this.txt = txt;
	}

	public JLabel getMerge() {
		return merge;
	}

	public void setMerge(JLabel merge) {
		this.merge = merge;
	}
	
	

}
