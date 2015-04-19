package exceptions;

import javax.swing.JOptionPane;

public class EmptyRequest extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String request;
	
	public EmptyRequest(String request) {
		super();
		this.request = request;
	}
	
	public void execute(){
		JOptionPane.showMessageDialog(null, request+" empty", "ERROR !", JOptionPane.ERROR_MESSAGE);
	}

}
