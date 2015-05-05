package exceptions;

import javax.swing.JOptionPane;

public class NotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundException() {
		super();
	}
	
	public String getMessage(){
		return "No drug found !"+this.getStackTrace();
	}
	
	public void execute(){
		JOptionPane.showMessageDialog(null, "No results found", "Information", JOptionPane.INFORMATION_MESSAGE);
	}

}
