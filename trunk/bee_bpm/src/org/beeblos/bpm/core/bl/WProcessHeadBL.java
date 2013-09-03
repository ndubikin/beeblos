package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessHeadDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessHead;
import org.beeblos.bpm.tm.TableManager;
import org.beeblos.bpm.tm.exception.TableManagerException;

import com.sp.common.util.StringPair;



public class WProcessHeadBL {
	
	private static final Log logger = LogFactory.getLog(WProcessHeadBL.class.getName());
	
	public WProcessHeadBL (){
		
	}
	
	public Integer add(WProcessHead processHead, Integer currentUserId) throws WProcessHeadException, WProcessDefException {
		
		logger.debug("add() WProcessHead - Name: ["+processHead.getName()+"]");
		
		// timestamp & trace info
		processHead.setInsertDate(new Date());
		processHead.setModDate( DEFAULT_MOD_DATE );
		processHead.setInsertUser(currentUserId);
		processHead.setModUser(currentUserId);

		return new WProcessHeadDao().add(processHead);

	}
		
	// dml 20130506
	public Integer addProcessAndFirstWProcessDef(WProcessHead processHead, Integer currentUserId) throws WProcessHeadException, WProcessDefException, WStepSequenceDefException {
		
		logger.debug("addProcessAndFirstWProcessDef() WProcessHead - Name: ["+processHead.getName()+"]");
		
		Integer processHeadId = this.add(processHead, currentUserId);
		processHead.setId(processHeadId);
		
		new WProcessDefBL().createFirstWProcessDef(processHeadId, currentUserId);
		
		return processHeadId;

	}
		
	public void update(WProcessHead processHead, Integer currentUserId) throws WProcessHeadException {
		
		logger.debug("update() WProcessHead < id = "+processHead.getId()+">");
		
		if (!processHead.equals(new WProcessHeadDao().getWProcessHeadByPK(processHead.getId())) ) {

			// timestamp & trace info
			processHead.setModDate(new Date());
			processHead.setModUser(currentUserId);
			new WProcessHeadDao().update(processHead);
			
		} else {
			
			logger.debug("WProcessHeadBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcessHead processHead, Integer currentUserId) throws WProcessHeadException {

		if (processHead==null) {
			throw new WProcessHeadException("Error: can't delete null process head...");			
		} 
		if (processHead.getId()==null) {
			throw new WProcessHeadException("Error: can't delete process head with null id...");
		}		
		
		logger.debug("delete() WProcessHead - Name: ["+processHead.getId()+"]");
		
		// check existence of alive wprocesdef for this process head
		if (hasVersions(processHead.getId(), currentUserId)){
			throw new WProcessHeadException("Error: can't delete process head:"+processHead.getId()
					+" This process head has active versions. Must delete it first...");
		}
		
		// check for existence of managed table and loaded data for this process head
		// if there is no data then drop it!
		if (processHead.getManagedTableConfiguration()!=null) {

			if (processHead.getManagedTableConfiguration().getName()!=null) {
				
				TableManager tm = new TableManager();
				String tableName = processHead.getManagedTableConfiguration().getName();
				String schemaName = processHead.getManagedTableConfiguration().getSchema();
				Integer qty;
				try {

					qty = tm.countNotNullRecords(schemaName,tableName,null);

					try{

						if (qty<=0) {
							tm.removeTable(schemaName,tableName);
						} else {
							throw new WProcessHeadException("Error: can't delete process head:"
													+processHead.getId()+" Managed table "+processHead.getManagedTableConfiguration().getName()
													+" has records with data!");
						}
					
					} catch (TableManagerException e) {
						String mess = "TableManagerException: error trying delete managed table of processHeadId:"
										+processHead.getId()+" managed table:"+processHead.getManagedTableConfiguration().getName()
										+" ERROR:"+e.getMessage()+" - "+e.getCause();
						logger.error(mess);
						//throw new WProcessHeadException(mess);
						// dejo seguir para que pueda borrar el proceso y si queda la tabla colgada q la borren por fuera
						// igual queda el log
					}

					
				} catch (TableManagerException e) {
					String mess="TableManagerException: error trying count # records of managed table...processHeadId:"
									+processHead.getId()+" managed table:"+processHead.getManagedTableConfiguration().getName()
									+" ERROR:"+e.getMessage()+" - "+e.getCause();
					logger.error(mess);
					//throw new WProcessHeadException(mess);
					// dejo seguir para que pueda borrar el proceso y si queda la tabla colgada q la borren por fuera
					// igual queda el log
					
				}
			}
		}

		// TODO no tengo claro que chekear aqui, si hay que chekear algo ...
		if (processHead.getProcessDataFieldDef()!=null) {
			
		}

		new WProcessHeadDao().delete(processHead);

	}

	public WProcessHead getProcessHeadByPK(Integer id, Integer currentUserId) throws WProcessHeadException {

		return new WProcessHeadDao().getWProcessHeadByPK(id);
	}
	
	
	public WProcessHead getProcessHeadByName(String name, Integer currentUserId) throws WProcessHeadException {

		return new WProcessHeadDao().getWProcessHeadByName(name);
	}

	public String getProcessName(Integer id, Integer currentUserId) throws WProcessDefException {
		return new WProcessHeadDao().getProcessName(id);
	}
	
	
	public List<WProcessHead> getProcessHeadList(Integer currentUserId) throws WProcessHeadException {

		return new WProcessHeadDao().getWProcessHeadList(currentUserId);
	
	}
	
	public List<WProcessDef> getProcessDefList(Integer processHeadId, Integer currentUserId) 
			throws WProcessHeadException {

//		return new WProcessDefBL().getW
	return null;
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion, Integer currentUserId )
	throws WProcessHeadException {
		 
		return new WProcessHeadDao().getComboList(textoPrimeraLinea, separacion, currentUserId);
		
	}

	/**
	 * returns true if there is 1 or more process versions for given processHeadId
	 * or false if doesn't have any version
	 *
	 * @param  Integer processHeadId, Integer currentUserId
	 * @return boolean
	 */
	public boolean hasVersions(Integer processHeadId, Integer currentUserId) 
			throws WProcessHeadException{
		
		return new WProcessHeadDao().hasVersions(processHeadId);
		
	}
}