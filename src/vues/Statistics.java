package vues;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Statistics extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel label;
	private modele.Statistics s;
	
	public Statistics(modele.Statistics s) {
		this.s = s;
		init();
		this.setBackground(Selection.BACKGROUND_COLOR);
		s.addObserver(this);
		this.add(label);
	}
	
	private void init(){
		this.label = new JLabel("("+s.getXmlNumber()+") XML : "+s.getXmlTimeTxt()+
				" ; ("+s.getSqlNumber()+") SQL : "+s.getSqlTimeTxt()+
				" ; ("+s.getCouchDbNumber()+") CouchDB : "+s.getCouchDbTimeTxt()+
				" ; ("+s.getTxtNumber()+") Texte : "+s.getTxtTimeTxt()
				);
		this.label.setBackground(Selection.BACKGROUND_COLOR);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		label.setText("XML ("+s.getXmlNumber()+") : "+s.getXmlTimeTxt()+
				" ; SQL ("+s.getSqlNumber()+") : "+s.getSqlTimeTxt()+
				" ; CouchDB ("+s.getCouchDbNumber()+") : "+s.getCouchDbTimeTxt()+
				" ; Texte ("+s.getTxtNumber()+") : "+s.getTxtTimeTxt()
				);
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public modele.Statistics getS() {
		return s;
	}

	public void setS(modele.Statistics s) {
		this.s = s;
	}
}
