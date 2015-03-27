package vues;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import modele.Search;

public class Selection extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Options options;
	private Recherche recherche;
	private Search search;
	
	public Selection(Search s) {
		super();
		this.search = s;
		this.setBackground(Color.gray);
		this.setLayout(new GridLayout(1,2));
		
		options = new Options(this.search);
		recherche = new Recherche(this.search);
		
		this.add(options);
		this.add(recherche);
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public Recherche getRecherche() {
		return recherche;
	}

	public void setRecherche(Recherche recherche) {
		this.recherche = recherche;
	}

}
