/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WExternalMethodException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WExternalMethodException() {
		
	}

	/**
	 * @param arg0
	 */
	public WExternalMethodException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WExternalMethodException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WExternalMethodException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
