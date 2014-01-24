/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WStepSequenceDefException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WStepSequenceDefException() {
		
	}

	/**
	 * @param arg0
	 */
	public WStepSequenceDefException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WStepSequenceDefException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WStepSequenceDefException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
