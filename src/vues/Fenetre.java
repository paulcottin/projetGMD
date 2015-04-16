package vues;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import modele.Search;

public class Fenetre extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Search search;
	private Selection selection;
	private ResultsList resultsList;
	private Statistics stats;
	
	public Fenetre(Search search) {
		super("Drug Search");
		this.search = search;
		this.search.addObserver(this);
		initFen();
		creerFen();
		
		this.setVisible(true);
	}
	
	private void initFen(){
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.selection = new Selection(search);
		this.resultsList = new ResultsList(search);
		this.stats = new Statistics(search.getStats());
	}
	
	private void creerFen(){
		this.setJMenuBar(new Menu(search, selection.getRecherche()));
		this.add(selection, BorderLayout.NORTH);
		this.add(resultsList, BorderLayout.CENTER);
		this.add(stats, BorderLayout.SOUTH);
	}

	public void update(Observable arg0, Object arg1) {
		this.remove(resultsList);
		resultsList = new ResultsList(search);
		this.add(resultsList, BorderLayout.CENTER);
		this.revalidate();
	}

}
