/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WProcessRoleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WProcessRoleException() {
		
	}

	/**
	 * @param arg0
	 */
	public WProcessRoleException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WProcessRoleException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WProcessRoleException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
