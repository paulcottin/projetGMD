package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import view.Sort;
import model.SearchHandler;

public class SortController implements ItemListener{

	private SearchHandler search;
	private Sort panel;
	private boolean ok;
	
	public SortController(SearchHandler search, Sort panel) {
		this.search = search;
		this.panel = panel;
		this.ok = false;
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		if (!ok) {
			ok = true;
			panel.getChoice().setEnabled(true);
			panel.getSort_c().setState(true);
		}else {
			ok = false;
			panel.getChoice().setEnabled(false);
			panel.getSort_c().setState(false);
			panel.getSort_c().revalidate();
			search.setSortBy(-1);
		}
	}
}
