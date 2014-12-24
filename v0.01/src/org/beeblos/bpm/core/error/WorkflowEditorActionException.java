/**
 * 
 */
package org.beeblos.bpm.core.error;

/**
 * @author Roger
 *
 */
public class WorkflowEditorActionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24652353247973549L;

	/**
	 * 
	 */
	public WorkflowEditorActionException() {
		
	}

	/**
	 * @param arg0
	 */
	public WorkflowEditorActionException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WorkflowEditorActionException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WorkflowEditorActionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

}
