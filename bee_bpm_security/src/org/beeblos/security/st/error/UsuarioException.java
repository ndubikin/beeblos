/**
 * 
 */
package org.beeblos.security.st.error;

/**
 * @author Roger
 *
 */
public class UsuarioException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public UsuarioException() {
		
	}

	/**
	 * @param arg0
	 */
	public UsuarioException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public UsuarioException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UsuarioException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
