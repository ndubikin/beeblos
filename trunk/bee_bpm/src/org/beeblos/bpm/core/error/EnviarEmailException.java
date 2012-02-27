package org.beeblos.bpm.core.error;

public class EnviarEmailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 884828375248702357L;

	/**
	 * 
	 */
	public EnviarEmailException() {
	}

	/**
	 * @param arg0
	 */
	public EnviarEmailException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public EnviarEmailException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public EnviarEmailException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}

