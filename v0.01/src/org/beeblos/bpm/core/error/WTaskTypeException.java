package org.beeblos.bpm.core.error;

public class WTaskTypeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9041970749138321399L;

	/**
	 * 
	 */
	public WTaskTypeException() {
		
	}

	/**
	 * @param arg0
	 */
	public WTaskTypeException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WTaskTypeException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WTaskTypeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
