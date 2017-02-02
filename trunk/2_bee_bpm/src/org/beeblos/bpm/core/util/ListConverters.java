package org.beeblos.bpm.core.util;

import static org.beeblos.bpm.core.util.Constants.ACTIVE_DATA_FIELDS;
import static org.beeblos.bpm.core.util.Constants.ALL_DATA_FIELDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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


	/**
	 * converts a WProcessDataField hashset to a list for manage data input and persist it
	 * 
	 *   mode: 1 - only active process data field / null || 0 - all process data field
	 *   
	 * @param convSet
	 * @param currentWorkId
	 * @param currentStepWorkId
	 * @param mode
	 * @return
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
//						"", // value
						null, // value - dml 20161031 - ITS: 1995 - cambiado porque ahora va a ser un "Object" no un String
						processDataField.getOrder(), // display order
						processDataField.getLength(),
						false, // modified (flag to indicate modification of this datafield
						processDataField.isRequired(), // required
						false,  // readonly
						false, // force modification
						processDataField.getResultType() // dml 20170201
						); 
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
						
//						"", // value
						null, // value - dml 20161102 - ITS: 1995 - cambiado porque ahora va a ser un "Object" no un String

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
						stepDatField.isForceModification(),  // force modification
						stepDatField.getDataField()!=null?stepDatField.getDataField().getResultType():null // dml 20170201
						);
				mdfList.add(mdf);
			}
		}
		
		/**
		 * sort field list by order...
		 */
		Collections.sort(mdfList, sortMDFByName());
		
		return mdfList;
	}


	/**
	 * Sorts an mdf list by order
	 * @return
	 */
	private static Comparator<ManagedDataField> sortMDFByName() {
		return new java.util.Comparator<ManagedDataField>() {

			public int compare(ManagedDataField mf1, ManagedDataField mf2) {

				if (mf1.getOrder() != null && mf2.getOrder() != null ){
					
					return mf1.getOrder().compareTo(mf2.getOrder());
				}
				
				return 999999;
			}
		};
	}
	
}