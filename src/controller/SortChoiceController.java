package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modele.SearchHandler;
import vues.Sort;

public class SortChoiceController implements ActionListener{

	private SearchHandler search;
	private Sort panel;
	private boolean ok;
	
	public SortChoiceController(SearchHandler searchHandler, Sort panel) {
		this.search = searchHandler;
		this.panel = panel;
		this.ok = false;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (!ok) {
			search.setSortBy(panel.getChoice().getSelectedIndex());
		}
	}

}
