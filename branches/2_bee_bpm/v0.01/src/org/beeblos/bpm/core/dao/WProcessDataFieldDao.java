package org.beeblos.bpm.core.dao;

import static org.beeblos.bpm.core.util.Constants.ALL_DATA_FIELDS;

import org.beeblos.bpm.core.model.enumerations.ProcessDataFieldStatus;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessHead;
import org.beeblos.bpm.core.util.ListConverters;
import org.beeblos.bpm.tm.TableManager;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;


public class WProcessDataFieldDao {
	
	private static final String ORG_HIBERNATE_EXCEPTION_CONSTRAINT_VIOLATION_EXCEPTION = "org.hibernate.exception.ConstraintViolationException";
	private static final Log logger = LogFactory.getLog(WProcessDataFieldDao.class.getName());
	
	public WProcessDataFieldDao(){
		
	}
	
	public Integer add(
			WProcessDataField processDataField) throws WProcessDataFieldException {
		
		logger.debug("add() WProcessDataField - Name: ["+processDataField.getName()+"]");

		Integer id;
		
		try {
			
			TableManager tm = new TableManager();
			ManagedData managedData = new ManagedData();

			// returns managedData if there is defined (processHead) and table exists
			// if not there is another instance to create the managed table
			managedData = getAndCheckManagedData(processDataField.getProcessHeadId(), tm, managedData);			

			// add new process data field
			id = Integer.valueOf(HibernateUtil.save(processDataField));
			
			/*
			 *  synchronize managed table
			 *  
			 *  Only syncro if managed table exists and have their datafields
			 *  
			 */
			if ( managedData!=null ) {
				try {
					tm.updateFieldSynchro(managedData,null, processDataField);
				} catch (TableManagerException e) {
					// TODO IMPLEMENTAR!!
					e.printStackTrace();
				}
			}			

		} catch (HibernateException ex) {
			logger.error("WProcessDataFieldDao: add - Can't store process processDataField definition record "+ 
					processDataField.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao: add - Can't store process definition record "+ 
					processDataField.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

		return id;
	}
	
	/**
	 * arriving here implies BL check and detects the changes in existing object processDataField
	 * this process data field is related with managed table and must check implicancies of modify 
	 * an existing data field already defined and will be changed (the definition of the data field...) 
	 * (this operations likes as a RMDB ALTER TABLE operation...)
	 * 
	 */
	public void update(
			WProcessDataField processDataField ) throws WProcessDataFieldException {
		
		logger.debug("update() WProcessDataField < id = "+processDataField.getId()+">");
		
		try {

			WProcessDataField storedDataField = getWProcessDataFieldByPK(processDataField.getId());
			
			if (!storedDataField.getProcessHeadId().equals(processDataField.getProcessHeadId())) {
				throw new WProcessDataFieldException("WProcessDataFieldDao: update - change of process head id is not permitted. You must delete the datafield and create a new one in the other process... ");
			}
			
			TableManager tm = new TableManager();
			ManagedData managedData = new ManagedData();

			// returns managedData if there is defined (processHead) and table exists
			// if not there is another instance to create the managed table
			managedData = 
					getAndCheckManagedData(processDataField.getProcessHeadId(), tm, managedData);

			// update process datafield
			HibernateUtil.update(processDataField);

			/*
			 *  synchronize managed table
			 *  
			 *  Only syncro if managed table exists and have their datafields
			 *  
			 */
			if ( managedData!=null ) {
				try {
					tm.updateFieldSynchro(managedData,storedDataField, processDataField);
				} catch (TableManagerException e) {
					// TODO IMPLEMENTAR!!
					e.printStackTrace();
				}
			}

		} catch (HibernateException ex) {
			logger.error("WProcessDataFieldDao: update - Can't update process definition record "+ 
					processDataField.getName()  +
					" - id = "+processDataField.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessDataFieldException("WProcessDataFieldDao: update - Can't update process definition record "+ 
					processDataField.getName()  +
					" - id = "+processDataField.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
	}

	/**
	 * @author nes
	 * 
	 * Delete a processDataField from Managed Table using TableManager manager
	 * 
	 * @param WProcessDataField processDataField
	 *
	 * @return void
	 * 
	 */
	public void delete(WProcessDataField processDataField) throws WProcessDataFieldException {

		logger.debug("delete() WProcessDataField - Name: ["+processDataField.getName()+"]");
		
		String tableName="", schemaName="", fieldName="";
		
		try {
			
			// @TODO revisar si esto es necesario aqui, traer todo para el final hacer drop de 1 campo
			TableManager tm = new TableManager();
			ManagedData managedData = new ManagedData();
			Integer qtyRecsNotNull=0;
			
			// returns managedData if there is defined (processHead) and table exists
			// if not there is another instance to create the managed table
			managedData = getAndCheckManagedData(processDataField.getProcessHeadId(), tm, managedData);
		
			if (managedData!=null) {

				tableName=managedData.getManagedTableConfiguration().getName();
				schemaName=managedData.getManagedTableConfiguration().getSchema();
				fieldName=processDataField.getColumnName();				
				
				qtyRecsNotNull = 
						tm.countNotNullRecords(	schemaName, tableName, fieldName);

			}

			if(qtyRecsNotNull<0) {
				logger.warn("Warning: table: "+tableName+" does not exist. No action realized.");
			} else if(qtyRecsNotNull>0) {
				String mess = "WProcessDataFieldDao: delete: trying delete managed field "+tableName
						+" wich has "+qtyRecsNotNull
						+" records with information in the table. Options you have are to clean manually the table nullating this field, or deactivate the field in ProcessDef form.";
				throw new WProcessDataFieldException(mess);
			}
			
			HibernateUtil.delete(processDataField);
			
			/*
			 *  synchronize managed table
			 *  
			 *  Only syncro if managed table exists and have their datafields
			 *  
			 */
			if ( managedData!=null && qtyRecsNotNull==0 ) {
				
				try {
					
					tm.deleteFieldSynchro(schemaName, tableName, fieldName, false);

				} catch (TableManagerException e) {
					String mess = "delete: TableManagerException Can't delete process data field:"+(processDataField!=null?processDataField.getName():"null")+" " 
							+e.getMessage()+" - "+e.getCause();
					logger.error(mess);
					throw new WProcessDataFieldException(mess);
				}
			}

		} catch (HibernateException ex) {
			
			// check constraint violation exception
			if (ex.getClass().getName().equals(ORG_HIBERNATE_EXCEPTION_CONSTRAINT_VIOLATION_EXCEPTION) ) {
				String mess = "WProcessDataFieldDao:org.hibernate.exception.ConstraintViolationException deleting a process definition record "+ processDataField.getName() +
						" <id = "+processDataField.getId()+ "> \n  The record will be used by a Step Definition. Delete it first!! "+" - "+ex.getMessage()+"\n"+ex.getCause(); 
				logger.error( mess );
				throw new WProcessDataFieldException(mess);
			}
			logger.error("WProcessDataFieldDao: delete - Can't delete process definition record "+ processDataField.getName() +
					" <id = "+processDataField.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao:  delete - Can't delete process definition record  "+ processDataField.getName() +
					" <id = "+processDataField.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (TableManagerException e) {
			String mess = "delete: TableManagerException Can't check table existence or existing records in table ..."
					+ tableName +" in schema "+schemaName
					+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WProcessDataFieldException(mess);
		} 

	}

	public WProcessDataField getWProcessDataFieldByPK(Integer id) throws WProcessDataFieldException {

		WProcessDataField process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			process = (WProcessDataField) session.get(WProcessDataField.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDataFieldDao: getWProcessDataFieldByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao: getWProcessDataFieldByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}

	
	public WProcessDataField getWProcessDataFieldByName(
			String name ) 
					throws WProcessDataFieldException {

		WProcessDataField process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessDataField) session.createCriteria(WProcessDataField.class)
						.add(Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="WProcessDataFieldDao: getWProcessDataFieldByName - can't obtain process data field name = " +
							name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause();
			logger.warn( mess );
			throw new WProcessDataFieldException( mess );

		}

		return process;
	}

	public WProcessDataField getWProcessDataFieldByNameAndProcessHeadId(
			String name, Integer processHeadId ) 
					throws WProcessDataFieldException {

		WProcessDataField process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessDataField) session.createCriteria(WProcessDataField.class)
						.add(Restrictions.naturalId().set("name", name))
						.add(Restrictions.naturalId().set("processHeadId", processHeadId))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="WProcessDataFieldDao: getWProcessDataFieldByName - can't obtain process data field name = " +
							name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause();
			logger.warn( mess );
			throw new WProcessDataFieldException( mess );

		}

		return process;
	}
	
	public List<WProcessDataField> getWProcessDataFieldList() throws WProcessDataFieldException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessDataField> processs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processs = session.createQuery("From WProcessDataField order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDataFieldDao: getWProcessDataFieldList() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao: getWProcessDataFieldList() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processs;
	}

	//rrl 20130730
	@SuppressWarnings("unchecked")
	public List<WProcessDataField> getWProcessDataFieldList(Integer processHeadId) throws WProcessDataFieldException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessDataField> processs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processs = session
					.createQuery("From WProcessDataField pdfi WHERE pdfi.processHeadId= :processHeadId order by id ")
					.setInteger("processHeadId", processHeadId)
					.list();
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="WProcessDataFieldDao: getWProcessDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn( mess );
			throw new WProcessDataFieldException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess="Exception: WProcessDataFieldDao: getWProcessDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): " +
					ex.getMessage()+"\n"+ex.getCause()+" - "+ex.getClass();
			logger.warn(mess);
			throw new WProcessDataFieldException(mess);
		}

		return processs;
	}
	
	/**
	 * Returns count of process data fields defined for a Process Head.
	 * 
	 * @param processHeadId
	 * @return
	 * @throws WProcessDataFieldException
	 */
	public Integer hasProcessDataFields(Integer processHeadId) throws WProcessDataFieldException {
		return hasProcessDataFields(processHeadId,null);
	}
	
	/**
	 * Returns count of process data fields defined for a Process Head.
	 * If filters applied then filters by this filters:
	 * 	ACTIVE: qty of active process data fields
	 *  REQUIRED: qty of required data fields
	 *  ALL/null: all 
	 *  
	 * @param processHeadId
	 * @param status
	 * @return
	 * @throws WProcessDataFieldException
	 */
	public Integer hasProcessDataFields(
			Integer processHeadId, ProcessDataFieldStatus status) 
		throws WProcessDataFieldException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		Long qtyRows;

		List<WProcessDataField> processs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			Criteria crit = session.createCriteria(WProcessDataField.class);
			crit.setProjection(Projections.rowCount());
			crit.add( Restrictions.eq("processHeadId", processHeadId));
			
			if (status!=null && !status.equals(ProcessDataFieldStatus.ALL) )  {
//				crit.add( Restrictions.isNotNull("birthDate"));
				
				if (status.equals(ProcessDataFieldStatus.ACTIVE)) {
					crit.add( Restrictions.eq("active", true));
				} else if (status.equals(ProcessDataFieldStatus.INACTIVE)) {
					crit.add( Restrictions.eq("active", false));
				} else if (status.equals(ProcessDataFieldStatus.REQUIRED)) {
					crit.add( Restrictions.eq("required", true));
				}  
				
			}
			
			qtyRows = (Long) crit.uniqueResult(); 
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="WProcessDataFieldDao: getWProcessDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn( mess );
			throw new WProcessDataFieldException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess="Exception: WProcessDataFieldDao: getWProcessDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): " +
					ex.getMessage()+"\n"+ex.getCause()+" - "+ex.getClass();
			logger.warn(mess);
			throw new WProcessDataFieldException(mess);
		}

		return (qtyRows!=null?(qtyRows.intValue()):0);
	}
	
	
	//rrl 20130801
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String separationLine, Integer processHeadId )
	throws WProcessDataFieldException {
		 
		List<WProcessDataField> lpdf = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			lpdf = session
					.createQuery("From WProcessDataField pdfi WHERE pdfi.processHeadId= ? order by pdfi.name ")
					.setInteger(0, processHeadId)
					.list();
	
			tx.commit();

			if (lpdf!=null) {
				
				// inserta los extras
				if ( firstLineText!=null && !"".equals(firstLineText) ) {
					if ( !firstLineText.equals("WHITESPACE") ) {
						retorno.add(new StringPair(null,firstLineText));  // deja la primera línea con lo q venga
					} else {
						retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
					}
				}
				
				if ( separationLine!=null && !"".equals(separationLine) ) {
					if ( !separationLine.equals("WHITESPACE") ) {
						retorno.add(new StringPair(null,separationLine));  // deja la separación línea con lo q venga
					} else {
						retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
					}
				}
				
				for (WProcessDataField pdfi: lpdf) {
					retorno.add(new StringPair(pdfi.getId(),pdfi.getName()));
				}
			} else {
				// nes  - si el select devuelve null entonces devuelvo null
				retorno=null;
			}
			
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new WProcessDataFieldException(
					"Can't obtain WProcessDataField combo list "
					+ex.getMessage()+"\n"+ex.getCause());
		} catch (Exception e) {}

		return retorno;

	}

	// if exists managed data definition and exists managed table in schema
	// then returns managedData object loaded, else return null
	private ManagedData getAndCheckManagedData(
			Integer processHeadId, TableManager tm,	ManagedData managedData) 
					throws WProcessDataFieldException {

		String tableName=null, schemaName=null;
		
		try {
			
			WProcessHead processHead = new WProcessHead();
			
			processHead = new WProcessHeadDao()
								.getWProcessHeadByPK(processHeadId);
	
			if (processHead!=null
					&& processHead.getManagedTableConfiguration()!=null
					&& processHead.getManagedTableConfiguration().getName()!=null
					&& !"".equals(processHead.getManagedTableConfiguration().getName())) {
	

				tableName=processHead.getManagedTableConfiguration().getName();
				schemaName=processHead.getManagedTableConfiguration().getSchema();

				Integer reccount = 
						tm.checkTableExists(schemaName,tableName);

				// if table exists creates managedData object ...
				if (reccount>=0) {
					
					managedData
						.setManagedTableConfiguration(
								processHead.getManagedTableConfiguration() );
					
					managedData.setReccount(reccount);
					
					managedData
						.setDataField(
								ListConverters.convertWProcessDataFieldToList
								 (processHead.getProcessDataFieldDef(),null,null,ALL_DATA_FIELDS) );
				} else {
					managedData=null;
				}
	
			} else {
				managedData=null;
			}
		} catch (WProcessHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TableManagerException e) {
			String mess = "getAndCheckManagedData: TableManagerException Can't access managed table:"
					+(tableName!=null?tableName:"null")+" " 
					+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WProcessDataFieldException(mess);
		}
		return managedData;
	}
	
	
}
	
