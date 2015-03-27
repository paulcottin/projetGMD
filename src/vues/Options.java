package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Options extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel selectSources;
	private JCheckBox sql_case, xml_case, txt_case, couch_case;
	
	public Options() {
		super();
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
		
		//Sélection par défaut
		sql_case.setSelected(true);
		xml_case.setSelected(true);
		txt_case.setSelected(true);
		couch_case.setSelected(true);
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
