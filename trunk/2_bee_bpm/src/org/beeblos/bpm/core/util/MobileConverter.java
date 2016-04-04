package org.beeblos.bpm.core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.mobile.WProcessWorkMobile;
import org.beeblos.bpm.core.model.mobile.WStepResponseDefMobile;
import org.beeblos.bpm.core.model.mobile.WStepWorkMobile;

import com.sp.common.model.ManagedDataField;
import com.sp.common.util.StringPair;

/**
 * @author pab 20160317
 * 
 * Clase para convertir objetos pesados de bee_bpm a objetos ligeros para usar en la app móvil que estamos haciendo.
 * 
 */
public class MobileConverter implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Converts a set of List<WStepWork> to a  /lighter/ List<WStepWorkMobile> one.
	 * 
	 * It uses the method convertToMobile(WStepWork wsw) to convert each response.
	 * 
	 * @param responses - a List<WStepWork> of steps to convert to List<WStepWorkMobile>
	 * @return the converted List<WStepWorkMobile>
	 */
	public static List<WStepWorkMobile> convertListToMobile(List<WStepWork> wswList){
		
		List<WStepWorkMobile> retList = new ArrayList<WStepWorkMobile>();
		
		for(WStepWork wsw : wswList){
			retList.add(convertToMobile(wsw));
		}
		
		return retList;
	}
	
	public static List<WProcessWorkMobile> convertProcessListToMobile(List<WProcessWork> pwmL){
		
		List<WProcessWorkMobile> retList = new ArrayList<WProcessWorkMobile>();
		
		for(WProcessWork wpw : pwmL){
			retList.add(convertToMobile(wpw));
		}
		
		return retList;
	}
	
	public static WProcessWorkMobile convertToMobile(WProcessWork wpw){
		
		WProcessWorkMobile ret = new WProcessWorkMobile();
		
		ret.setId(wpw.getId());
		ret.setName(wpw.getName());
		ret.setRef(wpw.getReference());
		
		ret.setDate(wpw.getStartingTime().toString("dd/MM/yyyy HH:mm"));
		ret.setStatus(wpw.getStatus().getName());
		ret.setQtyTasks(10);
		
		return ret;
	}
	
	private static List<StringPair> _createListStringPair(WStepWork wsw){
		
		if(wsw == null || wsw.empty() ||
				wsw.getManagedData() == null || wsw.getManagedData().empty() || 
				wsw.getManagedData().getDataField() == null || wsw.getManagedData().getDataField().isEmpty())
			return new ArrayList<StringPair>();
		
		List<StringPair> ret = new ArrayList<StringPair>();
		
		for(ManagedDataField d : wsw.getManagedData().getDataField()){
			ret.add(new StringPair(d.getName(), d.getValue()));
		}
		
		return ret;
	}
	
	/**
	 * Converts a single WStepWorkMobile to a  /lighter/ WStepWorkMobileMobile.
	 * 
	 * @param wsw - a WStepWork to convert.
	 * @return the WStepWorkMobile extracted from wsw.
	 */
	public static WStepWorkMobile convertToMobile(WStepWork wsw){
		
		WStepWorkMobile ret = new WStepWorkMobile();
		
		ret.setId(wsw.getId());
		ret.setStepName(wsw.getCurrentStep().getStepHead().getName());
		ret.setArrivingDate(wsw.getArrivingDate().toString("dd/MM/yyyy HH:mm"));
		ret.setUserInstructions(wsw.getUserInstructions());
		ret.setUserNotes(wsw.getUserNotes());
		ret.setSendUserNotesToNextStep(wsw.isSendUserNotesToNextStep());
		ret.setResponses(convertListToMobile(wsw.getCurrentStep().getResponse()));
		ret.setInsertUser(wsw.getInsertUser().getName());
		
		ret.setDataFields(_createListStringPair(wsw));
		
		ret.setProcessReference(wsw.getwProcessWork().getReference());
		ret.setProcessName(wsw.getwProcessWork().getName());
		ret.setProcessComments(wsw.getwProcessWork().getComments());
		
		return ret;
	}
	
	/**
	 * Converts a set of WStepResponseDef to a  /lighter/ WStepResponseDefMobile one.
	 * 
	 * It uses the method convertToMobile(WStepResponseDef wsr) to convert each response.
	 * 
	 * @param responses - a Set<WStepResponseDef> of responses to convert to WStepResponseDefMobile
	 * @return the converted List<WStepResponseDefMobile>
	 */
	public static List<WStepResponseDefMobile> convertListToMobile(Set<WStepResponseDef> responses){
		List<WStepResponseDefMobile> retList = new ArrayList<WStepResponseDefMobile>();
		
		for(WStepResponseDef response : responses){
			retList.add(convertToMobile(response));
		}
		
		return retList;
	}
	
	/**
	 * Converts a single WStepResponseDef to a  /lighter/ WStepResponseDefMobile.
	 *  
	 * @param wsr - the WStepResponseDef to convert
	 * @return the WStepResponseDefMobile extracted from wsr.
	 */
	public static WStepResponseDefMobile convertToMobile(WStepResponseDef wsr){
		
		WStepResponseDefMobile ret = new WStepResponseDefMobile();
		
		ret.setId(wsr.getId());
		ret.setName(wsr.getName());
		ret.setRespOrder(wsr.getRespOrder());
		
		return ret;
	}
}
