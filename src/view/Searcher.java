package view;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import model.Search;
import model.SearchHandler;
import controller.ButtonHelperController;
import controller.ModeController;
import controller.SearchController;
import controller.SynonymController;
import sun.net.www.content.image.jpeg;

public class Searcher extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel name_l, disease_l, useSyns, synAdvancement;
	private TextField drugName, diseaseName;
	private JButton search_button;
	private JComboBox<String> mode, numberOfDrugSyn, numberOfDiseaseSyn;
	private Checkbox synonyms;
	private JButton andName, orName, andDisease, orDisease, eraseDrug, eraseDisease;
	
	SearchHandler search;
	
	public Searcher(SearchHandler s) {
		super();
		this.setBackground(Selection.BACKGROUND_COLOR);
		this.search = s;
		this.search.addObserver(this);
		this.setLayout(new BorderLayout());
		initSearcher();
		createSearcher();
	}
	
	private void initSearcher(){
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
		useSyns = new JLabel("Use synonyms");
		synonyms = new Checkbox();
		synonyms.addItemListener(new SynonymController(search, this));
		synAdvancement = new JLabel("(0/4)");
		numberOfDrugSyn = new JComboBox<String>();
		numberOfDiseaseSyn = new JComboBox<String>();
		numberOfDrugSyn.setVisible(false);
		numberOfDiseaseSyn.setVisible(true);
		numberOfDrugSyn.setMaximumSize(new Dimension(150, 15));
		numberOfDiseaseSyn.setMaximumSize(new Dimension(150, 15));
	}
	
	private void createSearcher(){
		JPanel cases = new JPanel();
		cases.setLayout(new GridLayout(1,3));
		JPanel name_p = new JPanel();
		name_p.setLayout(new GridLayout(4,1));
		name_p.add(name_l);
		name_p.add(drugName);
		JPanel buttons_p = new JPanel();
		buttons_p.add(andName);
		buttons_p.add(orName);
		buttons_p.add(eraseDrug);
		buttons_p.setBackground(Selection.BACKGROUND_COLOR);
		name_p.add(buttons_p);
		name_p.setBackground(Selection.BACKGROUND_COLOR);
		JPanel drugSyn = new JPanel();
		drugSyn.setBackground(getBackground());
		drugSyn.add(numberOfDrugSyn);
		name_p.add(drugSyn);
		cases.add(name_p);
		JPanel mode_p = new JPanel();
		mode_p.setLayout(new GridLayout(3,1));
		JPanel mod = new JPanel();
		mod.setBackground(getBackground());
		mod.add(mode);
		mode_p.add(mod);
		mode_p.setBackground(Selection.BACKGROUND_COLOR);
		JPanel synCount = new JPanel();
		synCount.setBackground(getBackground());
		synCount.add(synAdvancement);
		mode_p.add(synCount);
		JPanel syns = new JPanel();
		syns.setBackground(getBackground());
		syns.add(useSyns);
		syns.add(synonyms);
		mode_p.add(syns);
		cases.add(mode_p);
		JPanel disease_p = new JPanel();
		disease_p.setLayout(new GridLayout(4,1));
		disease_p.add(disease_l);
		disease_p.add(diseaseName);
		JPanel buttons = new JPanel();
		buttons.add(andDisease);
		buttons.add(orDisease);
		buttons.add(eraseDisease);
		buttons.setBackground(Selection.BACKGROUND_COLOR);
		disease_p.add(buttons);
		disease_p.setBackground(Selection.BACKGROUND_COLOR);
		JPanel diseaseSyn = new JPanel();
		diseaseSyn.setBackground(getBackground());
		diseaseSyn.add(numberOfDiseaseSyn);
		disease_p.add(diseaseSyn);
		cases.add(disease_p);
		
		JPanel search_p = new JPanel();
		this.search_button.addActionListener(new SearchController(this.search, this));
		search_p.add(search_button);
		search_p.setBackground(Selection.BACKGROUND_COLOR);
		
		
		this.add(cases, BorderLayout.CENTER);
		this.add(search_p, BorderLayout.SOUTH);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		numberOfDrugSyn.setMaximumSize(new Dimension(150, 15));
		numberOfDiseaseSyn.setMaximumSize(new Dimension(150, 15));
		if (search.getSynonymAdvancement() < 4)
			synAdvancement.setText("("+search.getSynonymAdvancement()+"/4) running...");
		else
			synAdvancement.setText("("+search.getSynonymAdvancement()+"/4) ok");
		
		if (!search.getDrug().equals("") && search.isUseSynonyms()){
			numberOfDrugSyn.setVisible(true);
			numberOfDrugSyn.addItem(search.getDrugSyn()+" synonyms");
			for (String s : search.getDrugSynonyms()) {
				numberOfDrugSyn.addItem(s);
			}
		}
		else 
			numberOfDrugSyn.setVisible(false);
		if (!search.getDisease().equals("") && search.isUseSynonyms()){
			numberOfDiseaseSyn.setVisible(true);
			numberOfDiseaseSyn.addItem(search.getDiseaseSyn()+" synonyms");
			for (String s : search.getDiseaseSynonyms()) {
				numberOfDiseaseSyn.addItem(s);
			}
		}
		else
			numberOfDiseaseSyn.setVisible(false);
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

	public JLabel getUseSyns() {
		return useSyns;
	}

	public void setUseSyns(JLabel useSyns) {
		this.useSyns = useSyns;
	}

	public Checkbox getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(Checkbox synonyms) {
		this.synonyms = synonyms;
	}

	public JLabel getSynAdvancement() {
		return synAdvancement;
	}

	public void setSynAdvancement(JLabel synAdvancement) {
		this.synAdvancement = synAdvancement;
	}
}
