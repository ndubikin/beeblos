/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WUserRoleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WUserRoleException() {
		
	}

	/**
	 * @param arg0
	 */
	public WUserRoleException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WUserRoleException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WUserRoleException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
