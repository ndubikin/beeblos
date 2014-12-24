/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WStepHeadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WStepHeadException() {
		
	}

	/**
	 * @param arg0
	 */
	public WStepHeadException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WStepHeadException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WStepHeadException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
