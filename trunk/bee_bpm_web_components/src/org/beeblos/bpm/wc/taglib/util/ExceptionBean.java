package org.beeblos.bpm.wc.taglib.util;

import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExceptionBean extends CoreManagedBean {
	
	private static final Log logger = LogFactory.getLog(ExceptionBean.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<FGPException> exceptionList = new ArrayList<FGPException>();
	private boolean renderMessage;

	public ExceptionBean(){
		logger.debug("ExceptionBean()");
		exceptionList = new ArrayList<FGPException>();
		renderMessage=false;
	}
	
	public ArrayList<FGPException> getExceptionList() {
		logger.debug("getExceptionList()");
		
		exceptionList.clear();
		FGPException exp = null;
						
		ArrayList<FGPException> sessionList = (ArrayList<FGPException>)getContext().getAttributes().get("exceptionList");
		
		if( sessionList != null ){
			Iterator<FGPException> it = sessionList.iterator();		
			if( it.hasNext() ){	
				renderMessage = true;
				exp = it.next();							
				exceptionList.add(exp);
			}			
		}
		getContext().getAttributes().put("exceptionList", null);
		
		return exceptionList;
	}
	
	public FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}	

	public void setExceptionList(ArrayList<FGPException> exceptionList) {
		this.exceptionList = exceptionList;
	}

	public boolean isRenderMessage() {
		
		ArrayList<FGPException> sessionList = (ArrayList<FGPException>)getContext().getAttributes().get("exceptionList");
		
		if(sessionList != null && sessionList.size() > 0 ){
			renderMessage = true;
		}else{
			renderMessage = false;
		}
		
		return renderMessage;
	}

	public void setRenderMessage(boolean renderMessage) {
		this.renderMessage = renderMessage;
	}

}
