/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class CustomValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public CustomValidationException() {
		
	}

	/**
	 * @param arg0
	 */
	public CustomValidationException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public CustomValidationException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CustomValidationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
