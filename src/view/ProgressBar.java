package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressBar extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL);
	
	public ProgressBar() {
		super();
		this.setSize(300, 50);
		this.setTitle("Processing...");
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.add(bar, BorderLayout.CENTER);
		this.setVisible(true);
		bar.setIndeterminate(true);
	}
}
