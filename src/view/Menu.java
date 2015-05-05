package view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import model.Search;
import model.SearchHandler;
import controller.SearchController;
import controller.menu.ExitListener;

public class Menu extends JMenuBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenu file, searcher;
	private JMenuItem exit, search;
	private Searcher r;
	private SearchHandler s;
	
	public Menu(SearchHandler s, Searcher r) {
		super();
		this.s = s;
		this.r = r;
		initMenu();
		createMenu();
	}
	
	private void initMenu(){
		file = new JMenu("File");
			exit = new JMenuItem("Exit");
			exit.addActionListener(new ExitListener());
			exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
			searcher = new JMenu("Searcher");
			search = new JMenuItem("Search");
			search.addActionListener(new SearchController(s, r));
			search.setMnemonic(KeyEvent.VK_ENTER);
	}
	
	private void createMenu(){
		file.add(exit);
		searcher.add(search);
		
		this.add(file);
		this.add(searcher);
	}

}
