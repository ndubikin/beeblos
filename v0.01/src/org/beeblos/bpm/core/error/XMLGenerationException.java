/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class XMLGenerationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public XMLGenerationException() {
		
	}

	/**
	 * @param arg0
	 */
	public XMLGenerationException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public XMLGenerationException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public XMLGenerationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
