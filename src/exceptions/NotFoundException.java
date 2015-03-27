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
		return "No medic found !"+this.getStackTrace();
	}
	
	public void execute(){
		JOptionPane.showMessageDialog(null, "Aucun résutat trouvé", "Information", JOptionPane.INFORMATION_MESSAGE);
	}

}
