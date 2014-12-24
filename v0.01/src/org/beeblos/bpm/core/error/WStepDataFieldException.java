/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author rrl
 *
 */
public class WStepDataFieldException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5942500164649636543L;

	/**
	 * 
	 */
	public WStepDataFieldException() {
		
	}

	/**
	 * @param arg0
	 */
	public WStepDataFieldException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WStepDataFieldException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WStepDataFieldException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
