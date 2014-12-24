/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class AlreadyExistsRunningProcessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public AlreadyExistsRunningProcessException() {
		
	}

	/**
	 * @param arg0
	 */
	public AlreadyExistsRunningProcessException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public AlreadyExistsRunningProcessException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public AlreadyExistsRunningProcessException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
