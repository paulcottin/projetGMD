package vues;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Statistics extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel xml, sql, couchDB, txt, nbresults, tpsTotal, tpsMerge;
	private modele.Statistics s;
	
	public Statistics(modele.Statistics s) {
		this.s = s;
		init();
		this.setBackground(Selection.BACKGROUND_COLOR);
		this.setLayout(new GridLayout(2,4));
		s.addObserver(this);
		this.add(xml);
		this.add(sql);
		this.add(couchDB);
		this.add(txt);
		this.add(nbresults);
		this.add(new JLabel());
		this.add(tpsTotal);
		this.add(tpsMerge);
	}
	
	private void init(){
		this.xml = new JLabel("("+s.getXmlNumber()+") XML : "+s.getXmlTimeTxt());
		this.sql = new JLabel("("+s.getSqlNumber()+") SQL : "+s.getSqlTimeTxt());
		this.couchDB = new JLabel("("+s.getCouchDbNumber()+") CouchDB : "+s.getCouchDbTimeTxt());
		this.txt = new JLabel("("+s.getTxtNumber()+") Texte : "+s.getTxtTimeTxt());
		this.nbresults = new JLabel("Number of results : "+s.getTotalNumber());
		this.tpsTotal = new JLabel("Search Time : "+s.getTotalTimeTxt());
		this.tpsMerge = new JLabel("Merge time : "+s.getMergeTimeTxt());
		
		this.xml.setBackground(Selection.BACKGROUND_COLOR);
		this.sql.setBackground(Selection.BACKGROUND_COLOR);
		this.couchDB.setBackground(Selection.BACKGROUND_COLOR);
		this.txt.setBackground(Selection.BACKGROUND_COLOR);
		this.tpsMerge.setBackground(Selection.BACKGROUND_COLOR);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		this.xml.setText("("+s.getXmlNumber()+") XML : "+s.getXmlTimeTxt());
		this.sql.setText("("+s.getSqlNumber()+") SQL : "+s.getSqlTimeTxt());
		this.couchDB.setText("("+s.getCouchDbNumber()+") CouchDB : "+s.getCouchDbTimeTxt());
		this.txt.setText("("+s.getTxtNumber()+") Texte : "+s.getTxtTimeTxt());
		this.nbresults.setText("Number of results : "+s.getTotalNumber());
		this.tpsTotal.setText("Search Time : "+s.getTotalTimeTxt());
		this.tpsMerge.setText("Merge time : "+s.getMergeTimeTxt());
	}
	
	public modele.Statistics getS() {
		return s;
	}

	public void setS(modele.Statistics s) {
		this.s = s;
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
}
