package vues;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controller.menu.QuitterListener;

public class Menu extends JMenuBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenu fichier;
	private JMenuItem quitter;
	
	public Menu() {
		super();		
		initMenu();
		creerMenu();
	}
	
	private void initMenu(){
		fichier = new JMenu("Fichier");
			quitter = new JMenuItem("Quitter");
			quitter.addActionListener(new QuitterListener());
	}
	
	private void creerMenu(){
		fichier.add(quitter);
		
		this.add(fichier);
		
	}

}
