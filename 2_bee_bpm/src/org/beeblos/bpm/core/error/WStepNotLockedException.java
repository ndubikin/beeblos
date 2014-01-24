/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WStepNotLockedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WStepNotLockedException() {
		
	}

	/**
	 * @param arg0
	 */
	public WStepNotLockedException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WStepNotLockedException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WStepNotLockedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
