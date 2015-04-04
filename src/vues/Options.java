package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import modele.Search;

import controller.SelectSourceController;

public class Options extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Search search;
	private JLabel selectSources;
	private JCheckBox sql_case, xml_case, txt_case, couch_case;
	
	public Options(Search s) {
		super();
		this.search = s;
		this.setLayout(new BorderLayout());
		initOptions();
		creerOptions();
	}
	
	public void initOptions(){
		selectSources = new JLabel("Sélection des sources");
		
		//Initialisation
		sql_case = new JCheckBox("SQL");
		xml_case = new JCheckBox("XML");
		txt_case = new JCheckBox("TXT");
		couch_case = new JCheckBox("CouchDB");
		
		//Listeners
		sql_case.addActionListener(new SelectSourceController(search, "sql"));
		xml_case.addActionListener(new SelectSourceController(search, "xml"));
		txt_case.addActionListener(new SelectSourceController(search, "txt"));
		couch_case.addActionListener(new SelectSourceController(search, "couchDB"));
		
		//Sélection par défaut
//		sql_case.setSelected(true);
//		xml_case.setSelected(true);
		txt_case.setSelected(true);
//		couch_case.setSelected(true);
	}
	
	public void creerOptions(){
		this.add(selectSources, BorderLayout.NORTH);
		
		JPanel cases = new JPanel();
		cases.setLayout(new GridLayout(2,2));
		cases.add(sql_case);
		cases.add(txt_case);
		cases.add(xml_case);
		cases.add(couch_case);
		
		this.add(cases, BorderLayout.CENTER);
	}
}
