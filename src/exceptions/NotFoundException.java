package exceptions;

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

}
