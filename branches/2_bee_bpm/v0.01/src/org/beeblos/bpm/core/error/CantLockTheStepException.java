/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class CantLockTheStepException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public CantLockTheStepException() {
		
	}

	/**
	 * @param arg0
	 */
	public CantLockTheStepException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public CantLockTheStepException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CantLockTheStepException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
