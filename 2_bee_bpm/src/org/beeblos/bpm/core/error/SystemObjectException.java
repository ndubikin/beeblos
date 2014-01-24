package org.beeblos.bpm.core.error;

public class SystemObjectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public SystemObjectException() {
		
	}

	/**
	 * @param arg0
	 */
	public SystemObjectException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public SystemObjectException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public SystemObjectException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
