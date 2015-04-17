package vues;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import modele.Search;

public class ResultsList extends JScrollPane implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Search search;
	private Vector<Vector<String>> vec;
	private Vector<String> columnNames;
	private DefaultTableModel model;
	private int nbResults;
	private JTable table;
	
	public ResultsList(Search search) {
		super();
		init();
		this.search = search;
		this.nbResults = this.search.getEl().size();
		search.addObserver(this);
//		System.out.println("constructeur, results size : "+nbResults);
//		System.out.println("longueur table : "+table.getRowCount());
		if (table.getRowCount() > 0) {
//			System.out.println("remove");
			remove();
		}
		this.updateList();
//		System.out.println("longueur table : "+table.getRowCount());
	}
	
	private void init(){
		columnNames = new Vector<String>();
		columnNames.addElement(("Medic name"));
		columnNames.addElement(("Disease name"));
		columnNames.addElement(("Symptoms"));
		columnNames.addElement(("Cause"));
		columnNames.addElement(("Synonyms"));
		columnNames.addElement(("Disease Synonyms"));
		columnNames.addElement(("Origin"));
		
		vec = new Vector<Vector<String>>();
		model = new DefaultTableModel(vec, columnNames);
		table = new JTable(model);
		
		table.setEnabled(false);
		table.getColumnModel().addColumnModelListener( new WrapColListener( table ) );
		table.setDefaultRenderer( Object.class, new JTPRenderer() );
		table.getColumn("Origin").setPreferredWidth(0);
		
		this.setViewportView(table);
	}
	
	private void remove(){
		vec = new Vector<Vector<String>>();
		model.setRowCount(0);
		table.setModel(model);
		this.setViewportView(table);
	}
	
	private void updateList(){
		Vector<String> v;
		String synonyms = "", treats = "", causes = "", symptoms = "", diseaseSynonyms= "";
		for (int i = 0; i < nbResults; i++) {
			v = new Vector<String>();
			treats = "";
			causes = "";
			symptoms = "";
			synonyms = "";
			diseaseSynonyms = "";
			for (String string : search.getEl().get(i).getCause()) {
				causes += string+"\n";
			}
			for (String string : search.getEl().get(i).getTreat()) {
				treats += string+"\n";
			}
			for (String string : search.getEl().get(i).getSymptoms()) {
				symptoms += string+"\n";
			}
			for (String string : search.getEl().get(i).getSynonyms()) {
				synonyms += string+"\n";
			}
			for (String string : search.getEl().get(i).getDiseaseSynonyms()) {
				diseaseSynonyms += string+"\n";
			}
			v.addElement(search.getEl().get(i).getName());
			v.addElement(treats);
			v.addElement(causes);
			v.addElement(symptoms);
			v.addElement(synonyms);
			v.addElement(diseaseSynonyms);
			v.addElement(search.getEl().get(i).getOrigin());
			vec.addElement(v);
		}
		model.setDataVector(vec, columnNames);
		table.setModel(model);
		this.setViewportView(table);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		nbResults = search.getEl().size();
//		System.out.println("nbResults : "+nbResults);
		
		
//		updateList();
	}
	
	class JTPRenderer extends JTextPane implements TableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		    setText(value.toString());
//		    if (getText().equals("Sider2")) {
//		    	setBackground(Color.red);
//			}
		    return this;
		  }
		}
	
	class WrapColListener implements TableColumnModelListener {
		  JTable m_table;
		  WrapColListener( JTable table ){
		    m_table = table;
		  }

		  void refresh_row_heights() {
		    int n_rows = m_table.getRowCount();
		    int n_cols = m_table.getColumnCount();
		    int intercell_width = m_table.getIntercellSpacing().width;
		    int intercell_height = m_table.getIntercellSpacing().height;
		    TableColumnModel col_model = m_table.getColumnModel();
		    if( col_model == null ) return;
		    for (int row = 0; row < n_rows; row++) {
		      int pref_row_height = 1;
		      for (int col = 0; col < n_cols; col++) {
		        Object value = m_table.getValueAt(row, col);
		        TableCellRenderer renderer = m_table.getCellRenderer(row, col);
		        if( renderer == null ) return;
		        Component comp = renderer.getTableCellRendererComponent( m_table, value, false, false,
		            row, col);
		        if( comp == null ) return;
		        int col_width = col_model.getColumn(col).getWidth();
		        comp.setBounds(new Rectangle(0, 0, col_width - intercell_width, Integer.MAX_VALUE )); int pref_cell_height = comp.getPreferredSize().height  + intercell_height;
		        if (pref_cell_height > pref_row_height) {
		          pref_row_height = pref_cell_height;
		        }
		      }
		      if (pref_row_height != m_table.getRowHeight(row)) {
		        m_table.setRowHeight(row, pref_row_height);
		      }
		    }
		  }

		  @Override
		  public void columnAdded(TableColumnModelEvent e) {
		    refresh_row_heights();

		  }

		  @Override
		  public void columnRemoved(TableColumnModelEvent e) {
		  }

		  @Override
		  public void columnMoved(TableColumnModelEvent e) {
		  }

		  @Override
		  public void columnMarginChanged(ChangeEvent e) {
		    refresh_row_heights();
		  }

		  @Override
		  public void columnSelectionChanged(ListSelectionEvent e) {
		  }
	}
}
