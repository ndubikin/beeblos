package org.beeblos.bpm.core.model.bpmn;

import org.beeblos.bpm.core.model.WStepTypeDef;


/**
 * BPMN2 Generic StepType Group
 * 
 * NOTE dmuleiro 20150429: This is an abstract class. If we want to assign an 
 * GenericStepTypeGroup we have to assign the correct implementation of this class. 
 * 
 * @author dml 20150414
 *
 */
public abstract class GenericStepTypeGroup extends WStepTypeDef {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2271061877575979471L;
	
	public GenericStepTypeGroup(){
		
	}

	/**
	 * Constructor using object GenericStepTypeGroup
	 */
	public void setObj(GenericStepTypeGroup gstg) {

		super.setObj(gstg);

	}
}