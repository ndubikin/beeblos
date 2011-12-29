package org.beeblos.bpm.web.util;

import java.io.Serializable;


/**
 * @author nestor
 * 
 */
public abstract class WfManagedBean implements Serializable {

	public static final String SECURITY_CONTEXT = "contextoSeguridad";
	
	private static final long serialVersionUID = -4397579297370416992L;
	
	public WfManagedBean() {
		
	}
	

	public void init(){

	}
	
	public void reset(){
	
	}

	public void loadObject() {
		
	}

}