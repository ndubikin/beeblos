/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WStepWorkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WStepWorkException() {
		
	}

	/**
	 * @param arg0
	 */
	public WStepWorkException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WStepWorkException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WStepWorkException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
