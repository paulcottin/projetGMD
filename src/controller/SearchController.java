package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import exceptions.EmptyRequest;
import exceptions.NotFoundException;

import vues.Progress;
import vues.Recherche;

import modele.Search;
import modele.SearchHandler;

public class SearchController implements ActionListener{

	private SearchHandler s;
	private Recherche r;
	
	public SearchController(SearchHandler s, Recherche r) {
		this.s = s;
		this.r = r;
	}

	public void actionPerformed(ActionEvent e) {
		booleanInit();
		if (checkText(r.getDrugName().getText()) && checkText(r.getDiseaseName().getText())) {
			s.setMedic(r.getDrugName().getText());
			s.setDisease(r.getDiseaseName().getText());
			r.getMode().setEnabled(false);
			r.getSearch_button().setEnabled(false);
			try {
				try {
					s.search();
				} catch (NotFoundException e1) {
					e1.execute();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			r.getMode().setEnabled(true);
			r.getSearch_button().setEnabled(true);
		}else{
			JOptionPane.showMessageDialog(null, "You have to write *' AND/OR '* !", "ERROR !", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void booleanInit(){
//		s.setXmlProcBegin(false);
//		s.setXmlProcEnd(false);
//		s.setCouchDBProcEnd(false);
//		s.setCouchDBProcBegin(false);
//		s.setSqlProcEnd(false);
//		s.setSqlProcBegin(false);
//		s.setTxtProcEnd(false);
//		s.setTxtProcBegin(false);
//		s.setMergeProcEnd(false);
//		s.setMergeProcBegin(false);
	}
	
	private boolean checkText(String s){
		if (s.contains("AND")) {
			if (!s.contains(" AND ")) {
				return false;
			}else
				return true;
		}
		else if (s.contains("OR")) {
			if (!s.contains(" OR ")) {
				return false;
			}else
				return true;
		}else
			return true;
	}
}
