package org.beeblos.bpm.core.error;

public class SendEmailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 884828375248702357L;

	/**
	 * 
	 */
	public SendEmailException() {
	}

	/**
	 * @param arg0
	 */
	public SendEmailException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public SendEmailException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public SendEmailException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}

