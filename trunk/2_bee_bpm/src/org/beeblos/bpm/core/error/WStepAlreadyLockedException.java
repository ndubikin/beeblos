/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WStepAlreadyLockedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WStepAlreadyLockedException() {
		
	}

	/**
	 * @param arg0
	 */
	public WStepAlreadyLockedException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WStepAlreadyLockedException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WStepAlreadyLockedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
