/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author rrl
 *
 */
public class WProcessDataFieldException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5942500164649636543L;

	/**
	 * 
	 */
	public WProcessDataFieldException() {
		
	}

	/**
	 * @param arg0
	 */
	public WProcessDataFieldException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WProcessDataFieldException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WProcessDataFieldException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
