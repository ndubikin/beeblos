/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WStepLockedByAnotherUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WStepLockedByAnotherUserException() {
		
	}

	/**
	 * @param arg0
	 */
	public WStepLockedByAnotherUserException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WStepLockedByAnotherUserException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WStepLockedByAnotherUserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
