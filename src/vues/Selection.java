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
	
	public static Color BACKGROUND_COLOR = Color.lightGray;
	
	private Options options;
	private Recherche recherche;
	private Search search;
	
	public Selection(Search s) {
		super();
		this.search = s;
		this.setBackground(Selection.BACKGROUND_COLOR);
		JPanel opt = new JPanel();
		JPanel rech = new JPanel();
		opt.setBackground(Selection.BACKGROUND_COLOR);
		rech.setBackground(Selection.BACKGROUND_COLOR);
		
		options = new Options(this.search);
		recherche = new Recherche(this.search);
		
		opt.add(options);
		rech.add(recherche);
		
		this.add(opt);
		this.add(rech);
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
