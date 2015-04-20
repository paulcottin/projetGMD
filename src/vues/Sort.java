package vues;

import java.awt.Checkbox;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.SortController;

import modele.SearchHandler;

public class Sort extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SearchHandler search;
	private JLabel sort, by;
	private Checkbox sort_c;
	private JComboBox<String> choice;
	private boolean choiceEnabled;
	
	public Sort(SearchHandler search) {
		this.search = search;
		this.search.addObserver(this);
		init();
		construct();
		this.setBackground(Selection.BACKGROUND_COLOR);
	}
	
	private void init(){
		choiceEnabled = false;
		sort = new JLabel("Sorting");
		by = new JLabel(" by ");
		sort_c = new Checkbox();
		sort_c.addItemListener(new SortController(search, this));
		choice = new JComboBox<String>(new String[]{"Drug Name", "Disease Name", "Symptoms", "Cause", "Synonyms", "Diseases Synonyms", "Origin", "Score"});
		choice.setEnabled(choiceEnabled);
	}

	private void construct(){
		this.add(sort);
		this.add(sort_c);
		this.add(by);
		this.add(choice);
	}
	
	private void refreshing(){
		choice.setEnabled(choiceEnabled);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		refreshing();
	}

	public SearchHandler getSearch() {
		return search;
	}

	public void setSearch(SearchHandler search) {
		this.search = search;
	}

	public JLabel getSort() {
		return sort;
	}

	public void setSort(JLabel sort) {
		this.sort = sort;
	}

	public JLabel getBy() {
		return by;
	}

	public void setBy(JLabel by) {
		this.by = by;
	}

	public Checkbox getSort_c() {
		return sort_c;
	}

	public void setSort_c(Checkbox sort_c) {
		this.sort_c = sort_c;
	}

	public JComboBox<String> getChoice() {
		return choice;
	}

	public void setChoice(JComboBox<String> choice) {
		this.choice = choice;
	}

	public boolean isChoiceEnabled() {
		return choiceEnabled;
	}

	public void setChoiceEnabled(boolean choiceEnabled) {
		this.choiceEnabled = choiceEnabled;
	}
}
