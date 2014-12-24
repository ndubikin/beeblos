/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class InjectorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public InjectorException() {
		
	}

	/**
	 * @param arg0
	 */
	public InjectorException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public InjectorException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public InjectorException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
