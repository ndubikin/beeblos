/**
 * 
 */
package org.beeblos.bpm.tm.exception;

/**
 * @author Roger
 *
 */
public class TableHasRecordsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public TableHasRecordsException() {
		
	}

	/**
	 * @param arg0
	 */
	public TableHasRecordsException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public TableHasRecordsException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TableHasRecordsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
