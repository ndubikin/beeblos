/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WProcessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WProcessException() {
		
	}

	/**
	 * @param arg0
	 */
	public WProcessException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WProcessException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WProcessException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
