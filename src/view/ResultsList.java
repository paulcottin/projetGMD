package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import model.Search;
import model.SearchHandler;

public class ResultsList extends JScrollPane implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static int DRUG_NAME_COLUMN = 0;
	public static int DISEASE_NAME_COLUMN = 1;
	public static int SYMPTOMS_COLUMNS = 2;
	public static int CAUSES_COLUMNS = 3;
	public static int DRUG_SYNS_COLUMN = 4;
	public static int DISEASE_SYNS_COLUMN = 5;
	public static int ORIGIN_COLUMN = 6;
	public static int SCORE_COLUMN = 7;
	public static Color SQL_COLOR = new Color(23, 151, 255);
	public static Color TXT_COLOR = new Color(0, 186, 13);
	public static Color COUCDB_COLOR = new Color(255, 129, 12);
	public static Color XML_COLOR = new Color(255, 255, 0);
	
	
	private SearchHandler search;
	private Vector<Vector<String>> vec;
	private Vector<String> columnNames;
	private DefaultTableModel model;
	private int nbResults;
	private JTable table;
	
	public ResultsList(SearchHandler search) {
		super();
		init();
		this.search = search;
		this.nbResults = this.search.getEl().size();
		search.addObserver(this);

		if (table.getRowCount() > 0) {

			remove();
		}
		this.updateList();

	}
	
	private void init(){
		columnNames = new Vector<String>();
		columnNames.add(("Drug name"));
		columnNames.add(("Disease name"));
		columnNames.add(("Symptoms"));
		columnNames.add(("Cause"));
		columnNames.add(("Synonyms"));
		columnNames.add(("Disease Synonyms"));
		columnNames.add(("Origin"));
		columnNames.add("Score");
		
		vec = new Vector<Vector<String>>();
		model = new DefaultTableModel(vec, columnNames);
		table = new JTable(model);
		
		table.setEnabled(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
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
				causes += (!string.equals("")) ? string.replaceFirst(".",(string.charAt(0)+"").toUpperCase())+",\n" : "";
			}
			for (String string : search.getEl().get(i).getTreat()) {
				treats += (!string.equals("")) ? string.replaceFirst(".",(string.charAt(0)+"").toUpperCase())+",\n" : "";
			}
			for (String string : search.getEl().get(i).getSymptoms()) {
				symptoms += (!string.equals("")) ? string.replaceFirst(".",(string.charAt(0)+"").toUpperCase())+",\n" : "";
			}
			for (String string : search.getEl().get(i).getSynonyms()) {
				synonyms += (!string.equals("")) ? string.replaceFirst(".",(string.charAt(0)+"").toUpperCase())+",\n" : "";
			}
			for (String string : search.getEl().get(i).getDiseaseSynonyms()) {
				diseaseSynonyms += (!string.equals("")) ? string.replaceFirst(".",(string.charAt(0)+"").toUpperCase())+",\n" : "";
			}
			v.addElement(search.getEl().get(i).getName().toUpperCase());
			v.addElement(treats);
			v.addElement(causes);
			v.addElement(symptoms);
			v.addElement(synonyms);
			v.addElement(diseaseSynonyms);
			v.addElement(search.getEl().get(i).getOrigin().replaceAll("/", "\n"));
			v.addElement(String.valueOf(search.getEl().get(i).getScore()));
			vec.addElement(v);
		}
		model.setDataVector(vec, columnNames);
		table.setModel(model);
		
		if (search.getSortBy() >= 0) {
			FiltreSortModel filtre;
			if (search.getSortBy() == 7) 
				filtre = new FiltreSortModel(model, -1);
			else
				filtre = new FiltreSortModel(model, 1);
			table = new JTable(filtre);
			filtre.sort(search.getSortBy());
		}
		
		
		table.getColumnModel().addColumnModelListener( new WrapColListener( table ) );
		table.setDefaultRenderer( Object.class, new JTPRenderer() );
		
		TableColumnModel cs = table.getColumnModel();
		TableColumn c1 = (TableColumn) cs.getColumn(6);
		((TableColumn) c1).setMinWidth(70);
		((TableColumn) c1).setMaxWidth(70);
		c1 = (TableColumn) cs.getColumn(7);
		((TableColumn) c1).setMinWidth(40);
		((TableColumn) c1).setMaxWidth(40);
		
		
		
	
		this.setViewportView(table);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		nbResults = search.getEl().size();
		updateList();
	}
	
	class JTPRenderer extends JTextPane implements TableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		    setText(value.toString());
		    if (getText().equals("Sider2")) {
		    	setBackground(SQL_COLOR);
			}
		    else if (getText().equals("OMIM")) {
				setBackground(TXT_COLOR);
			}
		    else if (getText().equals("OrphaData")) {
				setBackground(COUCDB_COLOR);
			}
		    else if (getText().equals("DrugBank")) {
				setBackground(XML_COLOR);
			}
		    else {
				setBackground(getBackground());
			}
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
	
	class FiltreSortModel extends AbstractTableModel{
		   TableModel model;
		   Line [] lines;
		   int columnSort, order;
		   FiltreSortModel (TableModel m, int order){
		      model = m;
		      this.order = order;
		      lines = new Line[model.getRowCount()];
		      for( int i = 0; i < lines.length; ++i)
		         lines[i] = new Line(i);
		   }
		   public int getRowCount() {
		      return model.getRowCount();
		   }
		   public int getColumnCount() {
		      return model.getColumnCount();
		   }
		   public Object getValueAt(int rowIndex, int columnIndex) {
		      return model.getValueAt(lines[rowIndex].index,  columnIndex);
		   }
		   public Class<?> getColumnClass( int i){
		      return model.getColumnClass(i);
		   }
		   public String getColumnName(int i){
		      return model.getColumnName(i);
		   }
		   
		   public void sort(int c){
		      columnSort = c; 
		      try{
		          Arrays.sort(lines);
		          fireTableDataChanged();
		      }catch (RuntimeException e){e.printStackTrace();}  // The data are not comparable !
		   }
		   //------- The class Line -------
		   private class Line implements Comparable{
		      int index;
		      public Line (int i){index = i;}
		      public int compareTo(Object o) {
	            Line otherLine = (Line)o;
	            Object cell = model.getValueAt(index, columnSort);
	            Object otherCell = model.getValueAt(otherLine.index, columnSort);
	            if (order > 0) 
	            	return ((String) cell).compareTo((String) otherCell);
				else
					return -((String) cell).compareTo((String) otherCell);
					
		      }
		   }
	}
}
