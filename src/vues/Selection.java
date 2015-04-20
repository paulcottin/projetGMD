package vues;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import sun.net.www.content.image.jpeg;

import modele.Search;
import modele.SearchHandler;

public class Selection extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Color BACKGROUND_COLOR = Color.lightGray;
	
	private Options options;
	private Recherche recherche;
	private Progress progress;
	private SearchHandler search;
	private Sort sort;
	
	public Selection(SearchHandler s) {
		super();
		this.search = s;
		this.setBackground(Selection.BACKGROUND_COLOR);
		JPanel opt = new JPanel();
		JPanel rech = new JPanel();
		JPanel prog = new JPanel();
		JPanel sort = new JPanel();
		opt.setBackground(Selection.BACKGROUND_COLOR);
		rech.setBackground(Selection.BACKGROUND_COLOR);
		prog.setBackground(Selection.BACKGROUND_COLOR);
		sort.setBackground(Selection.BACKGROUND_COLOR);
		
		options = new Options(this.search);
		recherche = new Recherche(this.search);
		progress = new Progress(this.search);
		this.sort = new Sort(this.search);
		
		opt.add(options);
		rech.add(recherche);
		prog.add(progress);
		sort.add(this.sort);
		
		this.add(opt);
		this.add(rech);
//		this.add(prog);
		this.add(sort);
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

	public Progress getProgress() {
		return progress;
	}

	public void setProgress(Progress progress) {
		this.progress = progress;
	}

}
