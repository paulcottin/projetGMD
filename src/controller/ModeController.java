package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vues.Recherche;

import modele.Search;
import modele.SearchHandler;

public class ModeController implements ActionListener{

	private SearchHandler search;
	private Recherche r;
	
	public ModeController(SearchHandler s, Recherche r) {
		this.search = s;
		this.r = r;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (((String) r.getMode().getSelectedItem()).equals("OR")) {
			search.setMode(Search.OR);
		}
		else if (((String) r.getMode().getSelectedItem()).equals("AND")) {
			search.setMode(Search.AND);
		}
	}

}
