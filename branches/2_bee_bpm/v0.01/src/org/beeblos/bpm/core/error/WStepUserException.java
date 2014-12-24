/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WStepUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WStepUserException() {
		
	}

	/**
	 * @param arg0
	 */
	public WStepUserException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WStepUserException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WStepUserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
