package vues;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import modele.Search;

import controller.SearchController;

import sun.net.www.content.image.jpeg;

public class Recherche extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JLabel name_l, disease_l;
	TextField name, disease;
	JButton search_button;
	Search search;
	
	public Recherche(Search s) {
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
		name_l = new JLabel("Drug Name : ");
		name_l.setBackground(Selection.BACKGROUND_COLOR);
		disease = new TextField("");
		disease.setColumns(20);
		disease_l = new JLabel("Disease Name: ");
		disease_l.setBackground(Selection.BACKGROUND_COLOR);
		search_button = new JButton("Search");
	}
	
	private void creerRecherche(){
		JPanel cases = new JPanel();
		cases.setLayout(new GridLayout(1,2));
		JPanel name_p = new JPanel();
		name_p.add(name_l);
		name_p.add(name);
		name_p.setBackground(Selection.BACKGROUND_COLOR);
		cases.add(name_p);
		JPanel disease_p = new JPanel();
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

}
