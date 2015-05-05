package view;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Search;
import model.SearchHandler;

public class Window extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SearchHandler search;
	private Selection selection;
	private ResultsList resultsList;
	private Statistics stats;
	
	public Window(SearchHandler search) {
		super("Drug Search");
		this.search = search;
		this.search.addObserver(this);
		initWin();
		createWin();
		
		this.setVisible(true);
	}
	
	private void initWin(){
		this.setSize(1200, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.resultsList = new ResultsList(search);
		this.selection = new Selection(search);
		this.stats = new Statistics(search.getStats());
	}
	
	private void createWin(){
		this.setJMenuBar(new Menu(search, selection.getSearcher()));
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

	public ResultsList getResultsList() {
		return resultsList;
	}

	public void setResultsList(ResultsList resultsList) {
		this.resultsList = resultsList;
	}

}
