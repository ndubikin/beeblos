package org.beeblos.bpm.core.util;

import static org.beeblos.bpm.core.util.Constants.ACTIVE_DATA_FIELDS;
import static org.beeblos.bpm.core.util.Constants.ALL_DATA_FIELDS;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WStepDataField;

import com.sp.common.model.ManagedDataField;

/*
 * NOTA IMPORTANTE RAUL: CUANDO MIGREMOS A JSF2 USAR sp_common_jsf.utilsvs
 */
public class ListConverters {

	public ListConverters() {
    }

	/*
	 * converts a WProcessDataField hashset to a list for manage data input and persist it
	 * 
	 * mode: 1 - only active process data field / null || 0 - all process data field
	 */
	public static List<ManagedDataField> convertWProcessDataFieldToList(
			Set<WProcessDataField> convSet, Integer currentWorkId, Integer currentStepWorkId, int mode) {
		
		if (convSet==null) return null;
		if (convSet.size()==0) return null;
		
		List<ManagedDataField> mdfList = new ArrayList<ManagedDataField>(convSet.size());
		for(WProcessDataField processDataField: convSet) {
			if ( mode==ALL_DATA_FIELDS 
					|| mode==ACTIVE_DATA_FIELDS && processDataField.isActive() ) {
				ManagedDataField mdf = new ManagedDataField(
						currentWorkId, 
						currentStepWorkId, 
						processDataField.getDataType(), 
						processDataField.getName(),
						processDataField.getColumnName(),
						"", // value
						processDataField.getOrder(), // display order
						processDataField.getLength(),
						false, // modified (flag to indicate modification of this datafield
						processDataField.isRequired(), // required
						false,  // readonly
						false); // force modification
				mdfList.add(mdf);
			}
		}
		
		return mdfList;
	}

	public static List<ManagedDataField> convertWStepDataFieldToList(
			List<WStepDataField> stepDataFieldDef, Integer currentWorkId, Integer currentStepWorkId, int mode) {
		
		if (stepDataFieldDef==null) return null;
		if (stepDataFieldDef.size()==0) return null;
		
		List<ManagedDataField> mdfList = new ArrayList<ManagedDataField>(stepDataFieldDef.size());
		for(WStepDataField stepDatField: stepDataFieldDef) {
			if ( mode==ALL_DATA_FIELDS 
					|| mode==ACTIVE_DATA_FIELDS && stepDatField.getDataField().isActive() ) {
				ManagedDataField mdf = new ManagedDataField(
						currentWorkId, 
						currentStepWorkId, 
						stepDatField.getDataField().getDataType(), 
						
						// if step has his own name, use it, then use processHead defined name
						(stepDatField.getName()!=null 
							&& !"".equals(stepDatField.getName())
								?stepDatField.getName()
								:stepDatField.getDataField().getName()), 
						
						stepDatField.getDataField().getColumnName(), // column name must not be null ...
						
						"", // value

						// if step has his own order, use it, then use processHead defined order
						(stepDatField.getOrder()!=null
								?stepDatField.getOrder()
								:stepDatField.getDataField().getOrder()), // display order

						// if step has his own length, use it, then use processHead defined length
						(stepDatField.getLength()!=null
						?stepDatField.getLength()
						:stepDatField.getDataField().getLength()), // display order
						
						false, // modified (flag to indicate modification of this datafield
						stepDatField.isRequired(), // required
						stepDatField.isReadOnly(),  // readonly
						stepDatField.isForceModification()); // force modification
				mdfList.add(mdf);
			}
		}
		
		return mdfList;
	}
	
}