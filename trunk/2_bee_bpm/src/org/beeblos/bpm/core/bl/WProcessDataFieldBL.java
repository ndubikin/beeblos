package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;
import static org.beeblos.bpm.core.util.Constants.DEFAULT_VARCHAR_LENGHT;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDataFieldDao;
import org.beeblos.bpm.core.error.WDataTypeException;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WStepDataFieldException;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.enumerations.ProcessDataFieldStatus;
import org.beeblos.bpm.tm.TableManagerUtil;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.joda.time.DateTime;

import com.sp.common.model.en.WDataType;
import com.sp.common.util.StringPair;


public class WProcessDataFieldBL {
	
	private static final Log logger = LogFactory.getLog(WProcessDataFieldBL.class.getName());
	
	public WProcessDataFieldBL (){
		
	}
	
	public Integer add( WProcessDataField processDataField, Integer currentUserId ) 
			throws WProcessDataFieldException, WDataTypeException, TableManagerException {
		logger.debug("add() WProcessDataField - Name: ["
			+(processDataField!=null&&processDataField.getName()!=null?processDataField.getName():"null")+"]");
		
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
		WProcessDataField pdf = getWProcessDataFieldByNameAndProcessHeadId(
					processDataField.getName(),processDataField.getProcessHeadId(),currentUserId);
		if (pdf != null && pdf.getName()!=null 
				&& pdf.getName().equalsIgnoreCase(processDataField.getName()) 
				&& pdf.getProcessHeadId().equals(processDataField.getProcessHeadId())) {
			throw new WProcessDataFieldException("Data field name ["+pdf.getName()+"] already exists for this process!! Not permitted operation ... ");
		}
		
		// default checks
		_processDataFieldDefaultADDChecks(processDataField, currentUserId);
		
		_generateColumnName(processDataField);
		
		// timestamp & trace info
		processDataField.setInsertDate(new DateTime());
		processDataField.setModDate( DEFAULT_MOD_DATE_TIME );
		processDataField.setInsertUser(currentUserId);
		processDataField.setModUser(currentUserId);
		
		return new WProcessDataFieldDao().add( processDataField );

	}
	
	/**
	 * @author nes - 20130829
	 * 
	 * generates a column name with lowercase and change space by underscore
	 * and set received processDataField.ColumnName 
	 *
	 * @param  WProcessDataField processDataField
	 * 
	 * @return void
	 * @throws TableManagerException 
	 * 
	 */
	private void _generateColumnName(WProcessDataField processDataField) 
			throws WDataTypeException, TableManagerException {
		
		String colname = TableManagerUtil
				.generateColumnName(processDataField.getName(), processDataField.getClass().getName(), 45);
		
		processDataField.setColumnName(colname);

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
				
				WDataType currentDataType = WDataType.TEXT; // nes 20160521 - cambiado WDataType a Enum...
				
				processDataField.setDataType(currentDataType);
				
				processDataField.setLength((currentDataType.getDefaultLength()!=null 
						&& currentDataType.getDefaultLength()>0
						? currentDataType.getDefaultLength()
								: DEFAULT_VARCHAR_LENGHT));
						
			// dml 20130822 - si tiene nombre y el lenght es null o "0" le ponemos el defaultLength del dato
			} else{
				
				WDataType currentDataType = WDataType.findByKey(processDataField.getDataType().getId()); // nes 20160521 - cambiado WDataType a Enum...
				
				// si en la tabla datatype está en null el defaultLength considero que no debe ir asi que lo anulo
				if (currentDataType.getDefaultLength() == null ) {
					processDataField.setLength(null);
				} else { // si el currentDataType.getDefaultLength() es diferente de nulo entonces veo
					if (processDataField.getLength()==null 
							|| processDataField.getLength()<1) {
						processDataField.setLength(currentDataType.getDefaultLength());
					} else if (processDataField.getLength() > currentDataType.getMaxLength()) {
						processDataField.setLength(currentDataType.getMaxLength());
					} 
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
			processDataField.setModDate(new DateTime());
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
		
		WDataType currentDataType = WDataType.findByKey(
						processDataField.getDataType().getId());// nes 20160521 - cambiado WDataType a Enum...

		// checks if datatype must have length or not (HAY QUE ARREGLALO QUEDA INCOMPLETO)
		// NESTOR: yo lo que hacia aquí es comprobar si se le mete un valor en el campo
		// pero este tiene que ser null que saque error por pantalla...tal como esta ahora
		// si se mete un valor y el dato no lo acepta simplemente se pone a "null" (en el 
		// siguiente if)
		if (currentDataType.getMaxLength() != null
				&& processDataField.getLength() != null
				&& processDataField.getLength() > currentDataType.getMaxLength()){
			throw new WProcessDataFieldException("Process data field has not a valid length!! Not permitted operation ... ");
		} 

		// si el default length está en nulo considero q ese campo no puede tener largo
		// asi que lo anulo para que no reviente el create ...
		// dml 20130829 - NESTOR si se pone un cero también hay que ponerlo a null para que no
		// intente poner ese cero en la query (porque la query si es != null intenta meterlo)
		// el cero también lo pone automáticamente el jsf en el campo de la vista cuando no
		// se completa el formulario...
		if ( currentDataType.getDefaultLength() == null
			|| processDataField.getLength().equals(0) ){
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

	/**
	 * Returns processDataField list by name and processHeadId
	 * 
	 * @param name
	 * @param processHeadId
	 * @param currentUserId
	 * @return
	 * @throws WProcessDataFieldException
	 */
	public WProcessDataField getWProcessDataFieldByNameAndProcessHeadId(
			String name, Integer processHeadId, Integer currentUserId ) 
					throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldByNameAndProcessHeadId(name,processHeadId);
	}
	
	/**
	 * Returns process data field list for a process (without security ...)
	 * 
	 * @param currentUserId
	 * @return
	 * @throws WProcessDataFieldException
	 */
	public List<WProcessDataField> getWProcessDataFieldList(Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldList();
	
	}

	/**
	 * Returns process data field list for a process
	 * @param processHeadId
	 * @param currentUserId
	 * @return
	 * @throws WProcessDataFieldException
	 */
	public List<WProcessDataField> getWProcessDataFieldList(Integer processHeadId, Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldList(processHeadId);
	
	}
	
	//rrl 20130801
	public List<StringPair> getComboList(String firstLineText, String separationLine, Integer processHeadId ) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getComboList(firstLineText, separationLine, processHeadId);
	}
	
	/**
	 * returns qty of process data fields defined for a process (processHead)
	 * 
	 * @param processHeadId
	 * @param currentUserId
	 * @return
	 * @throws WProcessDataFieldException
	 */
	public Integer hasProcessDataFields(Integer processHeadId, Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().hasProcessDataFields(processHeadId);
	
	}
	
	/**
	 * returns qty of process data fields defined for a process (processHead) and a staus (all, active, required, etc)
	 * 
	 * @param processHeadId
	 * @param currentUserId
	 * @return
	 * @throws WProcessDataFieldException
	 */
	public Integer hasProcessDataFields(
			Integer processHeadId, ProcessDataFieldStatus status, Integer currentUserId) 
		throws WProcessDataFieldException {

		return new WProcessDataFieldDao().hasProcessDataFields(processHeadId,status);
	
	}	
}
	