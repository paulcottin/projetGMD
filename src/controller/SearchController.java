package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import vues.Progress;
import vues.Recherche;

import modele.Search;

public class SearchController implements ActionListener{

	private Search s;
	private Recherche r;
	
	public SearchController(Search s, Recherche r) {
		this.s = s;
		this.r = r;
	}

	public void actionPerformed(ActionEvent e) {
		booleanInit();
		s.setMedic(r.getName());
		s.setDisease(r.getDisease());
		r.getMode().setEnabled(false);
		try {
			s.search();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		r.getMode().setEnabled(true);
	}
	
	private void booleanInit(){
		s.setXmlProcBegin(false);
		s.setXmlProcEnd(false);
		s.setCouchDBProcEnd(false);
		s.setCouchDBProcBegin(false);
		s.setSqlProcEnd(false);
		s.setSqlProcBegin(false);
		s.setTxtProcEnd(false);
		s.setTxtProcBegin(false);
		s.setMergeProcEnd(false);
		s.setMergeProcBegin(false);
	}
}
