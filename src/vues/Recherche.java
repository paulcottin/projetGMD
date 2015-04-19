package vues;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import modele.Search;
import modele.SearchHandler;
import controller.ModeController;
import controller.SearchController;
import sun.net.www.content.image.jpeg;

public class Recherche extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel name_l, disease_l;
	private TextField name, disease;
	private JButton search_button;
	private JComboBox<String> mode;
	
	SearchHandler search;
	
	public Recherche(SearchHandler s) {
		super();
		this.setBackground(Selection.BACKGROUND_COLOR);
		this.search = s;
		this.setLayout(new BorderLayout());
		initRecherche();
		creerRecherche();
	}
	
	private void initRecherche(){
		name = new TextField("");
		name.setColumns(20);
		name_l = new JLabel("Drug Name");
		name_l.setBackground(Selection.BACKGROUND_COLOR);
		disease = new TextField("");
		disease.setColumns(20);
		disease_l = new JLabel("Disease Name");
		disease_l.setBackground(Selection.BACKGROUND_COLOR);
		search_button = new JButton("Search");
		search_button.setMnemonic(KeyEvent.VK_ENTER);
		mode = new JComboBox<String>(new String[]{"OR", "AND"});
		mode.addActionListener(new ModeController(search, this));
		mode.setSelectedIndex(0);
	}
	
	private void creerRecherche(){
		JPanel cases = new JPanel();
		cases.setLayout(new GridLayout(1,3));
		JPanel name_p = new JPanel();
		name_p.setLayout(new GridLayout(2,1));
		name_p.add(name_l);
		name_p.add(name);
		name_p.setBackground(Selection.BACKGROUND_COLOR);
		cases.add(name_p);
		JPanel mode_p = new JPanel();
		mode_p.add(mode);
		mode_p.setBackground(Selection.BACKGROUND_COLOR);
		cases.add(mode_p);
		JPanel disease_p = new JPanel();
		disease_p.setLayout(new GridLayout(2,1));
		disease_p.add(disease_l);
		disease_p.add(disease);
		disease_p.setBackground(Selection.BACKGROUND_COLOR);
		cases.add(disease_p);
		
		JPanel search_p = new JPanel();
		this.search_button.addActionListener(new SearchController(this.search, this));
		search_p.add(search_button);
		search_p.setBackground(Selection.BACKGROUND_COLOR);
		
		
		this.add(cases, BorderLayout.CENTER);
		this.add(search_p, BorderLayout.SOUTH);
	}

	public String getName() {
		return name.getText();
	}

	public void setName(TextField name) {
		this.name = name;
	}

	public String getDisease() {
		return disease.getText();
	}

	public void setDisease(TextField disease) {
		this.disease = disease;
	}

	public JComboBox<String> getMode() {
		return mode;
	}

	public void setMode(JComboBox<String> mode) {
		this.mode = mode;
	}

	public JButton getSearch_button() {
		return search_button;
	}

	public void setSearch_button(JButton search_button) {
		this.search_button = search_button;
	}

}
