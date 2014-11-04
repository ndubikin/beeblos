/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WProcessUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WProcessUserException() {
		
	}

	/**
	 * @param arg0
	 */
	public WProcessUserException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WProcessUserException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WProcessUserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
