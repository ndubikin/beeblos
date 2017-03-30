package org.beeblos.bpm.core.util;

import static com.sp.common.util.ConstantsCommon.DATE_FORMAT;
import static com.sp.common.util.ConstantsCommon.DATE_HOUR_FORMAT;
import static com.sp.common.util.ConstantsCommon.FALSE_STRING;
import static com.sp.common.util.ConstantsCommon.HOUR_FORMAT;
import static com.sp.common.util.ConstantsCommon.NOW_STRING;
import static com.sp.common.util.ConstantsCommon.TRUE_STRING;
import static org.beeblos.bpm.core.util.Constants.ACTIVE_DATA_FIELDS;
import static org.beeblos.bpm.core.util.Constants.ALL_DATA_FIELDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WStepDataField;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import com.sp.common.model.ManagedDataField;
import com.sp.common.model.en.WDataType;
import com.sp.common.util.StringUtil;

public class ListConverters {

	private static final Log logger = LogFactory.getLog(ListConverters.class);

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
			
				// dml 20170329 - Gets the default value converted into the correct object
				Object defaultValue = _setDefaultValueAsCorrectObject(
						processDataField.getName(), 
						processDataField.getDefaultValue(), 
						processDataField.getDataType());
				
				ManagedDataField mdf = new ManagedDataField(
						currentWorkId, 
						currentStepWorkId, 
						processDataField.getDataType(), 
						processDataField.getName(),
						processDataField.getColumnName(),
//						"", // value
						defaultValue, // value - dml 20161031 - ITS: 1995 - cambiado porque ahora va a ser un "Object" no un String
						processDataField.getOrder(), // display order
						processDataField.getLength(),
						false, // modified (flag to indicate modification of this datafield
						processDataField.isRequired(), // required
						false,  // readonly
						false, // force modification
						processDataField.getResultType(), // dml 20170201
						processDataField.getComments()// comments - dml 20170329
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
			
				// dml 20170329 - Gets the default value converted into the correct object
				Object defaultValue = _setDefaultValueAsCorrectObject(
						stepDatField.getDataField().getName(), 
						stepDatField.getDefaultValue(), 
						stepDatField.getDataField().getDataType());
				
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
						defaultValue, // value - dml 20161102 - ITS: 1995 - cambiado porque ahora va a ser un "Object" no un String

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
						stepDatField.getDataField()!=null?stepDatField.getDataField().getResultType():null, // dml 20170201
						stepDatField.getComments()// comments - dml 20170329
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
	 * Checks the data type of the field and returns the correct Object type to set to 
	 * the "Object value" as default
	 *
	 * @author dmuleiro 20170329
	 *
	 * @param datafieldName
	 * @param defaultValue
	 * @param dataType
	 * @return
	 * Object
	 */
	private static Object _setDefaultValueAsCorrectObject(String datafieldName, String defaultValue, WDataType dataType) {
		
		if (defaultValue == null || "".equals(defaultValue)){
			return null;
		}
		
		if (dataType != null){
			
			try {
				
				if (dataType.equals(WDataType.DATE)){
					
					// if the WDataType is DATE it will be a LocalDate

					if (defaultValue.equals(NOW_STRING)){
						return new LocalDate();
					}
					return DateTimeFormat.forPattern(DATE_FORMAT)
							.parseLocalDate(defaultValue);
					
				} else if (dataType.equals(WDataType.DATETIME)){
					
					// if the WDataType is DATETIME it will be a DateTime

					if (defaultValue.equals(NOW_STRING)){
						return new DateTime();
					}
					return DateTimeFormat.forPattern(DATE_HOUR_FORMAT)
							.parseDateTime(defaultValue);

				} else if (dataType.equals(WDataType.TIME)){
					
					// if the WDataType is TIME it will be a LocalTime

					if (defaultValue.equals(NOW_STRING)){
						return new LocalTime();
					}
					return DateTimeFormat.forPattern(HOUR_FORMAT)
							.parseDateTime(defaultValue).toLocalTime();

				} else if (dataType.equals(WDataType.BOOLEAN)){

					// if the WDataType is BOOLEAN it will be a boolean

					if (defaultValue.equals(TRUE_STRING)){
						return true;
					} else if (defaultValue.equals(FALSE_STRING)){
						return false;
					} else {
						logger.error("Error trying to recover default value to set into attribute '"
								+ datafieldName + "'" + " where default value should be boolean and it is '" + defaultValue + "'" );
						return null;
					}
					
				} else if (dataType.equals(WDataType.NUMBER)){
					
					// if the WDataType is NUMBER it will be a Integer
					
					if (StringUtil.isInteger(defaultValue)){
						return StringUtil.convertParamStringToInteger(defaultValue);
					} else {
						logger.error("Error trying to recover default value to set into attribute '"
								+ datafieldName + "'" + " where default value should be integer and it is '" + defaultValue + "'" );
						return null;
					}
					
				} else {
					
					// if the WDataType is other it will be a String
					
					return defaultValue;
				}
				
			} catch (Exception e){
				String mess = "Error trying to recover default value to set into attribute '"
						+ datafieldName + "'" + " where default value is '" + defaultValue + "'" 
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"")
						+ (e.getClass()!=null?". "+e.getClass():"");
				logger.error(mess);
				return null;
			}
			
		}
		
		return null;
		
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