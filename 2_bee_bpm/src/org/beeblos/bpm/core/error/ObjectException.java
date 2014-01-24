/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class ObjectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public ObjectException() {
		
	}

	/**
	 * @param arg0
	 */
	public ObjectException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public ObjectException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ObjectException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
