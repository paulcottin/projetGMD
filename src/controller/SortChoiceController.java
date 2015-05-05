package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.SearchHandler;
import view.Sort;

public class SortChoiceController implements ActionListener{

	private SearchHandler search;
	private Sort panel;
	
	public SortChoiceController(SearchHandler searchHandler, Sort panel) {
		this.search = searchHandler;
		this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		search.setSortBy(panel.getChoice().getSelectedIndex());
		search.update();
	}

}
