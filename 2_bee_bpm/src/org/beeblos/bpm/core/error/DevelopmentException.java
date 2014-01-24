/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class DevelopmentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public DevelopmentException() {
		
	}

	/**
	 * @param arg0
	 */
	public DevelopmentException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public DevelopmentException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DevelopmentException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
