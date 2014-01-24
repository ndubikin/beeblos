/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WStepDefException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WStepDefException() {
		
	}

	/**
	 * @param arg0
	 */
	public WStepDefException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WStepDefException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WStepDefException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
