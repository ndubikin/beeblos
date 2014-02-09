package org.beeblos.bpm.tm;

import static org.beeblos.bpm.core.util.Constants.ACTIVE_DATA_FIELDS;

import java.util.List;

import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessHeadManagedDataConfiguration;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.util.ListConverters;

import com.sp.common.model.ManagedDataField;

/**
 * Utility methods related with bee-bpm and TableManager
 * 
 * @author nestor
 *
 */
public class TableManagerBeeBpmUtil {

	
	/**
	 * Creates managedData object from current step def datafield list
	 * 
	 * @param stepWork
	 * @return
	 */
	public static ManagedData createManagedDataObject(WStepWork stepWork) {
		
		return createManagedDataObject(
						stepWork.getwProcessWork().getId(),		
						stepWork.getId(),
						stepWork.getwProcessWork().getProcessHeadId(),
						stepWork.getwProcessWork().getManagedTableConfiguration(),
						
						ListConverters.convertWStepDataFieldToList(
								stepWork.getCurrentStep().getStepDataFieldList(),
								null,
								null,
								ACTIVE_DATA_FIELDS) ) ;
	
	}
	/**
	 * Create ManagedData object from process def data field list (all process defined data fields)
	 * 
	 * @param processWork
	 * @return
	 */
	public static ManagedData createManagedDataObject(WProcessWork processWork) {
		return createManagedDataObject(
				processWork.getId()
				,null // step work id (doesn't apply here ...)
				,processWork.getProcessHeadId()
				,processWork.getManagedTableConfiguration()
				,ListConverters.convertWProcessDataFieldToList
				 		(processWork.getProcessDef().getProcessHead().getProcessDataFieldDef()
				 				,processWork.getId()
				 				,null
				 				,ACTIVE_DATA_FIELDS) 
				 );

		
	}
	
	/**
	 * Creates managedData object. From definitions, create a object representing a row
	 * of managed data in configured managedTable
	 * 
	 * Note: call this method from stepWork generate only a "sub-row" with data fields exposed
	 * to the methos. If this method is called from "Process" the object will represent entire
	 * row of managed data.
	 * 
	 * @param processWorkId // 
	 * @param stepWorkId
	 * @param idProcessHeadId
	 * @param managedTableConfiguration >> WProcessHeadManagedDataConfiguration
	 * @param dataFieldList >> List<ManagedDataField>
	 * @return
	 */
	public static ManagedData createManagedDataObject(
			Integer processWorkId, Integer stepWorkId, Integer processHeadId, 
			WProcessHeadManagedDataConfiguration managedTableConfiguration, 
			List<ManagedDataField> dataFieldList) {
		ManagedData md = new ManagedData();
		md.setDataField(dataFieldList);
		md.setChanged(false);				
		md.setCurrentStepWorkId(stepWorkId); // step work id (work)
		md.setCurrentWorkId(processWorkId); // head step work id (work)
		md.setProcessId(processHeadId); // process head id (def)
		md.setManagedTableConfiguration(managedTableConfiguration);
		return md;
	}
}
