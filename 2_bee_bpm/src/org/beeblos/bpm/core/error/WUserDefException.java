/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WUserDefException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WUserDefException() {
		
	}

	/**
	 * @param arg0
	 */
	public WUserDefException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WUserDefException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WUserDefException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
