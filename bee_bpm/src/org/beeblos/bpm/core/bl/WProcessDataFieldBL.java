package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.TEXT_W_DATA_TYPE_ID;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDataFieldDao;
import org.beeblos.bpm.core.error.WDataTypeException;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WStepDataFieldException;
import org.beeblos.bpm.core.model.WProcessDataField;

import com.sp.common.model.WDataType;
import com.sp.common.util.StringPair;


public class WProcessDataFieldBL {
	
	private static final Log logger = LogFactory.getLog(WProcessDataFieldBL.class.getName());
	
	public WProcessDataFieldBL (){
		
	}
	
	public Integer add( WProcessDataField processDataField, Integer currentUserId ) 
			throws WProcessDataFieldException, WDataTypeException {
		logger.debug("add() WProcessDataField - Name: ["+(processDataField!=null&&processDataField.getName()!=null?processDataField.getName():"null")+"]");
		
		// dml 20130820
		if (processDataField == null) {
			throw new WProcessDataFieldException("Can't add a NULL datafield ...");
		}
		if (processDataField.getName()==null || "".equals(processDataField.getName())) {
			throw new WProcessDataFieldException("Can't add a new datafield with no name or empty name field ...");
		}
		if (processDataField.getId()!=null && !processDataField.getId().equals(0)) {
			throw new WProcessDataFieldException("Trying to add an existing datafield as new one. Not permitted operation ... id:["+processDataField.getId()+"]");
		}
		
		// check for duplicated names (not allowed)
		WProcessDataField pdf = getWProcessDataFieldByName(processDataField.getName(),currentUserId);
		if (pdf != null && pdf.getName()!=null && pdf.getName().equalsIgnoreCase(processDataField.getName()) ) {
			throw new WProcessDataFieldException("Data field name ["+pdf.getName()+"] already exists for this process!! Not permitted operation ... ");
		}
		
		// default checks
		_processDataFieldDefaultADDChecks(processDataField, currentUserId);
		
		// timestamp & trace info
		processDataField.setInsertDate(new Date());
		processDataField.setModDate( DEFAULT_MOD_DATE );
		processDataField.setInsertUser(currentUserId);
		processDataField.setModUser(currentUserId);
		
		return new WProcessDataFieldDao().add( processDataField );

	}
	
	/**
	 * @author dmuleiro - 20130822
	 * 
	 * Checks if the processDataField to be added has correct params and format it if it has not
	 *
	 * @param  WProcessDataField processDataField
	 * @param  Integer currentUserId
	 * @return void
	 * 
	 */
	private void _processDataFieldDefaultADDChecks(WProcessDataField processDataField, Integer currentUserId) throws WDataTypeException {
		
		// chequear el tema de que si viene el tipo de dato en null le ponés text
		// si el largo viene en null le ponés el default de la tabla de DATATYPE ok?
		if (processDataField != null){
			
			// dml 20130821 - si no tiene nombre le ponemos "Text" y defaultLenght del text
			if (processDataField.getDataType() == null
					|| processDataField.getDataType().getName() == null){
				
				WDataType currentDataType = new WDataTypeBL().getWDataTypeByPK(
						TEXT_W_DATA_TYPE_ID, currentUserId);
				
				processDataField.setDataType(currentDataType);
				processDataField.setLength(currentDataType.getDefaultLength());
						
			// dml 20130822 - si tiene nombre y el lenght es null o "0" le ponemos el defaultLength del dato
			} else{
				
				WDataType currentDataType = new WDataTypeBL().getWDataTypeByName(
						processDataField.getDataType().getName(), currentUserId);
				
				if (currentDataType.getDefaultLength() == null
						|| processDataField.getLength() == null
						|| processDataField.getLength().equals(0)){
					
					processDataField.setLength(currentDataType.getDefaultLength());
					
				}

			}
			
		}
		
	}
	
	
	public void update(
			WProcessDataField processDataField, Integer currentUserId) throws WProcessDataFieldException, WDataTypeException {
		
		logger.debug("update() WProcessDataField < id = "+(processDataField.getId()!=null?processDataField.getId():"null")+">");

		if (processDataField.getName()==null || "".equals(processDataField.getName())) {
			throw new WProcessDataFieldException("Can't add or update a datafield with no name or empty name field ...");
		}

		if (processDataField.getId()==null || processDataField.getId().equals(0)) {
			throw new WProcessDataFieldException("Trying to update a datafield but id has an illegal value :["+(processDataField.getId()==null?"null":processDataField.getId())+"]");
		}
		
		if (!processDataField.equals(
				new WProcessDataFieldDao()
						.getWProcessDataFieldByPK( processDataField.getId() )) ) {
			
			_processDataFieldDefaultUPDATEChecks(processDataField, currentUserId);

			// timestamp & trace info
			processDataField.setModDate(new Date());
			processDataField.setModUser(currentUserId);
			
			new WProcessDataFieldDao().update(processDataField);
			
		} else {
			
			logger.debug("WProcessDataFieldBL.update - no changed object has arrived: nothing to do ...");
		}
			
	}
	
	/**
	 * @author dmuleiro - 20130822
	 * 
	 * Checks if the processDataField to be updated has correct params and throws an exception if it has not
	 * 
	 * @param  WProcessDataField processDataField
	 * @param  Integer currentUserId
	 * @return void
	 * 
	 */
	private void _processDataFieldDefaultUPDATEChecks(WProcessDataField processDataField, Integer currentUserId) throws WProcessDataFieldException, WDataTypeException {
		
		// check for duplicated names (not allowed)
		WProcessDataField pdf = getWProcessDataFieldByName(processDataField.getName(),currentUserId);
		if (pdf != null
				&& pdf.getName()!=null
				&& pdf.getId()!=processDataField.getId()
				&& pdf.getName().equals(processDataField.getName()) ) {
			throw new WProcessDataFieldException("Data field name ["+pdf.getName()+"] already exists!! Not permitted operation ... ");
		}
		
		// dml 20130822 - empty name is not allowed
		if (processDataField.getDataType().getName() == null){
			throw new WProcessDataFieldException("Process data field has not a valid name!! Not permitted operation ... ");
		}
		
		WDataType currentDataType = new WDataTypeBL().getWDataTypeByName(
				processDataField.getDataType().getName(), currentUserId);

		// dml 20130822 - empty length is not allowed
		if (currentDataType.getDefaultLength() == null
				&& processDataField.getLength() != null
				&& !processDataField.getLength().equals(0)){
			throw new WProcessDataFieldException("Process data field has not a valid length!! Not permitted operation ... ");
		} else if (processDataField.getLength().equals(0)){
			processDataField.setLength(null);
		}
		
	}
	
	public void delete(WProcessDataField processDataField, Integer currentUserId) 
			throws WProcessDataFieldException {

		logger.debug("delete() WProcessDataField - id/name: ["+(processDataField.getId()!=null?processDataField.getId():"null")
						+"/"+processDataField.getName()+"]");
		// checks if this process data field is being used in some step-data-fields
		// 
		WStepDataFieldBL stepDFBL = new WStepDataFieldBL();
		try {
			
			Integer qty = stepDFBL
								.countWStepDataFieldList(
										processDataField.getId(), currentUserId);
			if (qty != null && qty > 0) {
				throw new WProcessDataFieldException("Error trying delete process-data-field :"+processDataField.getName()
						+" This process has steps wich are referencing or using this Process Step. Unlink it before try delete !");
			}
		
		} catch (WStepDataFieldException e) {
			throw new WProcessDataFieldException("WStepDataFieldException trying count # step-data-field related with this process-data-field");
		}
		
		
		new WProcessDataFieldDao().delete(processDataField);

	}

	public WProcessDataField getWProcessDataFieldByPK(Integer id, Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldByPK(id);
	}
	
	
	public WProcessDataField getWProcessDataFieldByName(String name, Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldByName(name);
	}

	
	public List<WProcessDataField> getWProcessDataFieldList(Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldList();
	
	}

	public List<WProcessDataField> getWProcessDataFieldList(Integer processHeadId, Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldList(processHeadId);
	
	}
	
	//rrl 20130801
	public List<StringPair> getComboList(String firstLineText, String separationLine, Integer processHeadId ) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getComboList(firstLineText, separationLine, processHeadId);
	}
	
	
}
	