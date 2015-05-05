package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import model.Search;
import model.SearchHandler;
import controller.SelectSourceController;

public class Options extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SearchHandler search;
	private JLabel selectSources;
	private JCheckBox sql_case, xml_case, txt_case, couch_case;
	
	public Options(SearchHandler s) {
		super();
		this.search = s;
		this.setBackground(Selection.BACKGROUND_COLOR);
		this.setLayout(new BorderLayout());
		initOptions();
		createOptions();
		colored();
	}
	
	public void initOptions(){
		selectSources = new JLabel("source selection");
		
		//Initialisation
		sql_case = new JCheckBox("SQL");
		xml_case = new JCheckBox("XML");
		txt_case = new JCheckBox("TXT");
		couch_case = new JCheckBox("CouchDB");
		
		//Couleur
		sql_case.setBackground(Selection.BACKGROUND_COLOR);
		xml_case.setBackground(Selection.BACKGROUND_COLOR);
		txt_case.setBackground(Selection.BACKGROUND_COLOR);
		couch_case.setBackground(Selection.BACKGROUND_COLOR);
		
		//Listeners
		sql_case.addActionListener(new SelectSourceController(search, "sql"));
		xml_case.addActionListener(new SelectSourceController(search, "xml"));
		txt_case.addActionListener(new SelectSourceController(search, "txt"));
		couch_case.addActionListener(new SelectSourceController(search, "couchDB"));
		
		//Sélection par défaut
		sql_case.setSelected(true);
		xml_case.setSelected(true);
		txt_case.setSelected(true);
		couch_case.setSelected(true);
	}
	
	public void createOptions(){
		this.add(selectSources, BorderLayout.NORTH);
		
		JPanel cases = new JPanel();
		cases.setLayout(new GridLayout(2,2));
		cases.add(sql_case);
		cases.add(txt_case);
		cases.add(xml_case);
		cases.add(couch_case);
		
		cases.setBackground(Selection.BACKGROUND_COLOR);
		
		this.add(cases, BorderLayout.CENTER);
	}
	
	private void colored(){
		for (Component c : this.getComponents()) {
			c.setBackground(Selection.BACKGROUND_COLOR);
		}
	}
	
	private void caseProcessing(){
		if (search.isCouchDB_b()) {
			couch_case.setSelected(true);
		}else {
			couch_case.setSelected(false);
		}
		if (search.isSql_b()) {
			sql_case.setSelected(true);
		}else {
			sql_case.setSelected(false);
		}
		if (search.isTxt_b()) {
			txt_case.setSelected(true);
		}else {
			txt_case.setSelected(false);
		}
		if (search.isXml_b()) {
			xml_case.setSelected(true);
		}else {
			xml_case.setSelected(false);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		caseProcessing();
	}
}
