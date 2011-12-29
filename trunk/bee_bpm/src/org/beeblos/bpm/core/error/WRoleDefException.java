/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WRoleDefException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WRoleDefException() {
		
	}

	/**
	 * @param arg0
	 */
	public WRoleDefException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WRoleDefException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WRoleDefException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
