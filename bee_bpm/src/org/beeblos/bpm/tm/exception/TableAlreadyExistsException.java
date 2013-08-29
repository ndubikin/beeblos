/**
 * 
 */
package org.beeblos.bpm.tm.exception;

/**
 * @author Roger
 *
 */
public class TableAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public TableAlreadyExistsException() {
		
	}

	/**
	 * @param arg0
	 */
	public TableAlreadyExistsException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public TableAlreadyExistsException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TableAlreadyExistsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
