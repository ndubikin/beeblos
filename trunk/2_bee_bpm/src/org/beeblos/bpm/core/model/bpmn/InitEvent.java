package org.beeblos.bpm.core.model.bpmn;

import org.beeblos.bpm.core.model.WStepTypeDef;


/**
 * BPMN2 init event
 * may be a Begin, Message begin or Timer Begin.
 * 
 * @author nestor
 *
 */
public abstract class InitEvent extends WStepTypeDef {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2271061877575979471L;
	
	private String paramPrueba;
	
	public InitEvent(){
		
	}

	/**
	 * @return the paramPrueba
	 */
	public String getParamPrueba() {
		return paramPrueba;
	}

	/**
	 * @param paramPrueba the paramPrueba to set
	 */
	public void setParamPrueba(String paramPrueba) {
		this.paramPrueba = paramPrueba;
	}

}