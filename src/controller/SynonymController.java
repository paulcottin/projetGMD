package controller;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import vues.Recherche;

import modele.SearchHandler;

public class SynonymController implements ItemListener{

	private SearchHandler search;
	private Recherche r;
	private boolean ok;
	
	public SynonymController(SearchHandler s, Recherche r) {
		this.search = s;
		this.r = r;
		this.ok = false;
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		if (!ok) {
			ok = true;
			search.setUseSynonyms(true);
		}else {
			ok = false;
			search.setUseSynonyms(false);
		}
	}
}
