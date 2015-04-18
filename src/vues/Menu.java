package vues;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import modele.Search;
import controller.SearchController;
import controller.menu.QuitterListener;

public class Menu extends JMenuBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenu fichier, recherche;
	private JMenuItem quitter, rechercher;
	private Recherche r;
	private Search s;
	
	public Menu(Search s, Recherche r) {
		super();
		this.s = s;
		this.r = r;
		initMenu();
		creerMenu();
	}
	
	private void initMenu(){
		fichier = new JMenu("Fichier");
			quitter = new JMenuItem("Quitter");
			quitter.addActionListener(new QuitterListener());
			quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		recherche = new JMenu("Recherche");
			rechercher = new JMenuItem("Rechercher");
			rechercher.addActionListener(new SearchController(s, r));
			rechercher.setMnemonic(KeyEvent.VK_ENTER);
	}
	
	private void creerMenu(){
		fichier.add(quitter);
		recherche.add(rechercher);
		
		this.add(fichier);
		this.add(recherche);
	}

}
