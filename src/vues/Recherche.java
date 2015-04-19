package vues;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import modele.Search;
import modele.SearchHandler;
import controller.ButtonHelperController;
import controller.ModeController;
import controller.SearchController;
import sun.net.www.content.image.jpeg;

public class Recherche extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel name_l, disease_l;
	private TextField drugName, diseaseName;
	private JButton search_button;
	private JComboBox<String> mode;
	private JButton andName, orName, andDisease, orDisease, eraseDrug, eraseDisease;
	
	SearchHandler search;
	
	public Recherche(SearchHandler s) {
		super();
		this.setBackground(Selection.BACKGROUND_COLOR);
		this.search = s;
		this.setLayout(new BorderLayout());
		initRecherche();
		creerRecherche();
	}
	
	private void initRecherche(){
		drugName = new TextField("");
		drugName.setColumns(20);
		name_l = new JLabel("Drug Name");
		name_l.setBackground(Selection.BACKGROUND_COLOR);
		diseaseName = new TextField("");
		diseaseName.setColumns(20);
		disease_l = new JLabel("Disease Name");
		disease_l.setBackground(Selection.BACKGROUND_COLOR);
		search_button = new JButton("Search");
		search_button.setMnemonic(KeyEvent.VK_ENTER);
		mode = new JComboBox<String>(new String[]{"OR", "AND"});
		mode.addActionListener(new ModeController(search, this));
		mode.setSelectedIndex(0);
		andName = new JButton("AND");
		orName = new JButton("OR");
		andDisease = new JButton("AND");
		orDisease = new JButton("OR");
		eraseDrug = new JButton("C");
		eraseDisease = new JButton("C");
		andName.addActionListener(new ButtonHelperController(this));
		orName.addActionListener(new ButtonHelperController(this));
		andDisease.addActionListener(new ButtonHelperController(this));
		orDisease.addActionListener(new ButtonHelperController(this));
		eraseDrug.addActionListener(new ButtonHelperController(this));
		eraseDisease.addActionListener(new ButtonHelperController(this));
	}
	
	private void creerRecherche(){
		JPanel cases = new JPanel();
		cases.setLayout(new GridLayout(1,3));
		JPanel name_p = new JPanel();
		name_p.setLayout(new GridLayout(3,1));
		name_p.add(name_l);
		name_p.add(drugName);
		JPanel buttons_p = new JPanel();
		buttons_p.add(andName);
		buttons_p.add(orName);
		buttons_p.add(eraseDrug);
		buttons_p.setBackground(Selection.BACKGROUND_COLOR);
		name_p.add(buttons_p);
		name_p.setBackground(Selection.BACKGROUND_COLOR);
		cases.add(name_p);
		JPanel mode_p = new JPanel();
		mode_p.add(mode);
		mode_p.setBackground(Selection.BACKGROUND_COLOR);
		cases.add(mode_p);
		JPanel disease_p = new JPanel();
		disease_p.setLayout(new GridLayout(3,1));
		disease_p.add(disease_l);
		disease_p.add(diseaseName);
		JPanel buttons = new JPanel();
		buttons.add(andDisease);
		buttons.add(orDisease);
		buttons.add(eraseDisease);
		buttons.setBackground(Selection.BACKGROUND_COLOR);
		disease_p.add(buttons);
		disease_p.setBackground(Selection.BACKGROUND_COLOR);
		cases.add(disease_p);
		
		JPanel search_p = new JPanel();
		this.search_button.addActionListener(new SearchController(this.search, this));
		search_p.add(search_button);
		search_p.setBackground(Selection.BACKGROUND_COLOR);
		
		
		this.add(cases, BorderLayout.CENTER);
		this.add(search_p, BorderLayout.SOUTH);
	}

	public JComboBox<String> getMode() {
		return mode;
	}

	public void setMode(JComboBox<String> mode) {
		this.mode = mode;
	}

	public JButton getSearch_button() {
		return search_button;
	}

	public void setSearch_button(JButton search_button) {
		this.search_button = search_button;
	}

	public JButton getAndName() {
		return andName;
	}

	public void setAndName(JButton andName) {
		this.andName = andName;
	}

	public JButton getOrName() {
		return orName;
	}

	public void setOrName(JButton orName) {
		this.orName = orName;
	}

	public JButton getAndDisease() {
		return andDisease;
	}

	public void setAndDisease(JButton andDisease) {
		this.andDisease = andDisease;
	}

	public JButton getOrDisease() {
		return orDisease;
	}

	public void setOrDisease(JButton orDisease) {
		this.orDisease = orDisease;
	}

	public JLabel getName_l() {
		return name_l;
	}

	public void setName_l(JLabel name_l) {
		this.name_l = name_l;
	}

	public JLabel getDisease_l() {
		return disease_l;
	}

	public void setDisease_l(JLabel disease_l) {
		this.disease_l = disease_l;
	}

	public TextField getDrugName() {
		return drugName;
	}

	public void setDrugName(TextField drugName) {
		this.drugName = drugName;
	}

	public TextField getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(TextField diseaseName) {
		this.diseaseName = diseaseName;
	}

	public JButton getEraseDrug() {
		return eraseDrug;
	}

	public void setEraseDrug(JButton eraseDrug) {
		this.eraseDrug = eraseDrug;
	}

	public JButton getEraseDisease() {
		return eraseDisease;
	}

	public void setEraseDisease(JButton eraseDisease) {
		this.eraseDisease = eraseDisease;
	}

}
