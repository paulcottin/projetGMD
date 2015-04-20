package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import vues.Recherche;

public class ButtonHelperController implements ActionListener{

	private Recherche r;
	
	public ButtonHelperController(Recherche r) {
		this.r = r;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(r.getAndName())) {
			r.getDrugName().setText(r.getDrugName().getText()+" AND ");
//			r.getDrugName().requestFocus();
		}
		else if (e.getSource().equals(r.getOrName())) {
			r.getDrugName().setText(r.getDrugName().getText()+ " OR ");
		}
		else if (e.getSource().equals(r.getAndDisease())) {
			r.getDiseaseName().setText(r.getDiseaseName().getText()+" AND ");
		}
		else if (e.getSource().equals(r.getOrDisease())) {
			r.getDiseaseName().setText(r.getDiseaseName().getText()+" OR ");
		}
		else if (e.getSource().equals(r.getEraseDrug())) {
			r.getDrugName().setText("");
			r.getDrugName().requestFocus();
		}
		else if (e.getSource().equals(r.getEraseDisease())) {
			r.getDiseaseName().setText("");
			r.getDiseaseName().requestFocus();
		}
	}

}
