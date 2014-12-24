/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WProcessHeadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WProcessHeadException() {
		
	}

	/**
	 * @param arg0
	 */
	public WProcessHeadException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WProcessHeadException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WProcessHeadException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
