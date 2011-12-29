/**
 * 
 */
package org.beeblos.security.auxiliar.error;

/**
 * @author Roger
 *
 */
public class MonedaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public MonedaException() {
		
	}

	/**
	 * @param arg0
	 */
	public MonedaException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public MonedaException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MonedaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
