package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vues.Recherche;

import modele.Search;

public class ModeController implements ActionListener{

	private Search search;
	private Recherche r;
	
	public ModeController(Search s, Recherche r) {
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
