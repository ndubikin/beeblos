/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author rrl
 *
 */
public class WDataTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9041970749138321399L;

	/**
	 * 
	 */
	public WDataTypeException() {
		
	}

	/**
	 * @param arg0
	 */
	public WDataTypeException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WDataTypeException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WDataTypeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}

