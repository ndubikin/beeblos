package org.beeblos.bpm.core.error;


public class WEmailAccountException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WEmailAccountException() {
		
	}

	/**
	 * @param arg0
	 */
	public WEmailAccountException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WEmailAccountException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WEmailAccountException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
