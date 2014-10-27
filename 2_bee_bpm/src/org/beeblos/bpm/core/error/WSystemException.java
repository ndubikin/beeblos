package org.beeblos.bpm.core.error;

public class WSystemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9041970749138321399L;

	/**
	 * 
	 */
	public WSystemException() {

	}

	/**
	 * @param arg0
	 */
	public WSystemException(String arg0) {
		super(arg0);

	}

	/**
	 * @param arg0
	 */
	public WSystemException(Throwable arg0) {
		super(arg0);

	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WSystemException(String arg0, Throwable arg1) {
		super(arg0, arg1);

	}

}
