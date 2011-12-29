/**
 * 
 */
package org.beeblos.security.st.error;

/**
 * @author Roger
 *
 */
public class InyectorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public InyectorException() {
		
	}

	/**
	 * @param arg0
	 */
	public InyectorException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public InyectorException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public InyectorException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
