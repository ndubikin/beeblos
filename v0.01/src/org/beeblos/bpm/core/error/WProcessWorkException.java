/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WProcessWorkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WProcessWorkException() {
		
	}

	/**
	 * @param arg0
	 */
	public WProcessWorkException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WProcessWorkException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WProcessWorkException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
