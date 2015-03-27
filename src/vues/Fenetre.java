package vues;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import modele.ResultsList;
import modele.Search;

public class Fenetre extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Search search;
	private Selection selection;
	private ResultsList resultsList;
	
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
		selection = new Selection(search);
		resultsList = new ResultsList(search);
	}
	
	private void creerFen(){
		this.setJMenuBar(new Menu());
		this.add(selection, BorderLayout.NORTH);
		this.add(resultsList, BorderLayout.CENTER);
	}

	public void update(Observable arg0, Object arg1) {
		System.out.println("update");
		resultsList = new ResultsList(search);
		resultsList.revalidate();
		creerFen();
		this.revalidate();
	}

}
