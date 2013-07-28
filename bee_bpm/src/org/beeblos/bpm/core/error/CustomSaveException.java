package org.beeblos.bpm.core.error;

public class CustomSaveException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8324836221655596156L;

	/**
	 * 
	 */
	public CustomSaveException() {
		
	}

	/**
	 * @param arg0
	 */
	public CustomSaveException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public CustomSaveException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CustomSaveException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}

