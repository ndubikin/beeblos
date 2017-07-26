/**
 * 
 */
package org.beeblos.bpm.tm.exception;

/**
 * @author Roger
 *
 */
public class TableManagerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public TableManagerException() {
		
	}

	/**
	 * @param arg0
	 */
	public TableManagerException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public TableManagerException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TableManagerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
