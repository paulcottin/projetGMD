package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.Searcher;
import model.Search;
import model.SearchHandler;

public class ModeController implements ActionListener{

	private SearchHandler search;
	private Searcher r;
	
	public ModeController(SearchHandler s, Searcher r) {
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
