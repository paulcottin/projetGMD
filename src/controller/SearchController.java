package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;

import javax.swing.JOptionPane;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import model.SearchHandler;
import view.Searcher;
import exceptions.NotFoundException;

public class SearchController implements ActionListener{

	private SearchHandler s;
	private Searcher r;
	
	public SearchController(SearchHandler s, Searcher r) {
		this.s = s;
		this.r = r;
	}

	public void actionPerformed(ActionEvent e) {
		if (checkText(r.getDrugName().getText()) && checkText(r.getDiseaseName().getText())) {
			s.setDrug(r.getDrugName().getText());
			s.setDisease(r.getDiseaseName().getText());
			r.getMode().setEnabled(false);
			r.getSearch_button().setEnabled(false);
			s.getSynonymsThread().interrupt();
			try {
				try {
					try {
						s.search();
					} catch (NotFoundException e1) {
						e1.execute();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (CommunicationsException e1) {
				System.out.println("coucou");
				JOptionPane.showMessageDialog(null, "Connection error, have you enable your VPN ?", "ERROR !", JOptionPane.ERROR_MESSAGE);
			}
			r.getMode().setEnabled(true);
			r.getSearch_button().setEnabled(true);
		}else{
			JOptionPane.showMessageDialog(null, "You have to write *' AND/OR '* !", "ERROR !", JOptionPane.ERROR_MESSAGE);
		}
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
