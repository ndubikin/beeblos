/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class ManagedDataSynchronizerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public ManagedDataSynchronizerException() {
		
	}

	/**
	 * @param arg0
	 */
	public ManagedDataSynchronizerException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public ManagedDataSynchronizerException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ManagedDataSynchronizerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
