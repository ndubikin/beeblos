package org.beeblos.bpm.core.model.bpmn;

import org.beeblos.bpm.core.model.WStepTypeDef;


/**
 * BPMN2 init event
 * may be a Begin, Message begin or Timer Begin.
 * 
 * NOTE dmuleiro 20150429: This is an abstract class. If we want to assign an 
 * InitEvent we have to assign the correct implementation of this class. 
 * 
 * @author nestor
 *
 */

public abstract class InitEvent extends WStepTypeDef {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2271061877575979471L;
	
	public InitEvent(){
		
	}

	/**
	 * Constructor using object InitEvent
	 */
	public void setObj(InitEvent ie) {

		super.setObj(ie);

	}

}