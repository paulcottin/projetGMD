package modele;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ResultsList extends JScrollPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Search search;
	private Vector<Vector<String>> vec;
	private int nbResults;
	
	public ResultsList(Search search) {
		super();
		this.search = search;
		this.nbResults = this.search.getEl().size();
		this.updateList();
	}
	
	private void updateList(){
		Vector<String> columnNames = new Vector<String>();
		columnNames.addElement(("Medic name"));
		columnNames.addElement(("Disease name"));
		columnNames.addElement(("Cause"));
		columnNames.addElement(("Symptoms"));
		columnNames.addElement(("Synonyms"));
		
		vec = new Vector<Vector<String>>();
		Vector<String> v;
		for (int i = 0; i < nbResults; i++) {
			v = new Vector<String>();
			v.addElement(search.getEl().get(i).getName());
			v.addElement(search.getEl().get(i).getTreat());
			v.addElement(search.getEl().get(i).getCause());
			v.addElement(search.getEl().get(i).getSymptoms());
			v.addElement(search.getEl().get(i).getName());
			vec.addElement(v);
		}
		JTable test= new JTable(vec, columnNames);
		this.setViewportView(test);
		this.revalidate();
		this.getViewport().revalidate();
		System.out.println("nb row : "+test.getRowCount());
	}

}
