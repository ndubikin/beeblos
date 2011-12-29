/**
 * 
 */
package org.beeblos.security.st.error;

/**
 * @author Roger
 *
 */
public class UsuarioLoginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public UsuarioLoginException() {
		
	}

	/**
	 * @param arg0
	 */
	public UsuarioLoginException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public UsuarioLoginException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UsuarioLoginException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
