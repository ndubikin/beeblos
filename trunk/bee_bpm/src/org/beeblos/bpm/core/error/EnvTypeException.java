/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class EnvTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public EnvTypeException() {
		
	}

	/**
	 * @param arg0
	 */
	public EnvTypeException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public EnvTypeException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public EnvTypeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
