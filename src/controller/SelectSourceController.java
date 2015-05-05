package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import view.Options;
import model.Search;
import model.SearchHandler;

public class SelectSourceController implements ActionListener{

	private SearchHandler s;
	private String id;
	
	public SelectSourceController(SearchHandler s, String id) {
		this.s = s;
		this.id = id;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (id) {
		case "txt":
			if (((JCheckBox) arg0.getSource()).isSelected()) 
				s.setTxt_b(true);
			else
				s.setTxt_b(false);
			break;
		case "sql":
			if (((JCheckBox) arg0.getSource()).isSelected()) 
				s.setSql_b(true);
			else
				s.setSql_b(false);
			break;
		case "xml":
			if (((JCheckBox) arg0.getSource()).isSelected()) 
				s.setXml_b(true);
			else
				s.setXml_b(false);
			break;
		case "couchDB":
			if (((JCheckBox) arg0.getSource()).isSelected()) 
				s.setCouchDB_b(true);
			else
				s.setCouchDB_b(false);
			break;
		}
	}

}
