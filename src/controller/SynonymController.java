package controller;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import view.Searcher;
import model.SearchHandler;

public class SynonymController implements ItemListener{

	private SearchHandler search;
	private Searcher r;
	private boolean ok;
	
	public SynonymController(SearchHandler s, Searcher r) {
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
