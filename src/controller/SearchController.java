package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vues.Recherche;

import modele.Search;

public class SearchController implements ActionListener{

	private Search s;
	private Recherche r;
	
	public SearchController(Search s, Recherche r) {
		this.s = s;
		this.r = r;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		s.setMedic(r.getName());
		s.setDisease(r.getDisease());
		s.search();
	}
}
