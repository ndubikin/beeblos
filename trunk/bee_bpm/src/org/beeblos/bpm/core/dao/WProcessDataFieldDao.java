package org.beeblos.bpm.core.dao;

import static org.beeblos.bpm.core.util.Constants.ALL_DATA_FIELDS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessHead;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.beeblos.bpm.core.util.ListConverters;
import org.beeblos.bpm.tm.TableManager;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

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
			id = Integer.valueOf(HibernateUtil.guardar(processDataField));
			
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
	
	/*
	 * arriving here implies bl check and detects change in existing object processDataField
	 * 
	 * this process data field is related with managed table and must check implicancies
	 * 
	 */
	public void update(
			WProcessDataField processDataField ) throws WProcessDataFieldException {
		
		logger.debug("update() WProcessDataField < id = "+processDataField.getId()+">");
		
		try {

			WProcessDataField storedDataField = getWProcessDataFieldByPK(processDataField.getId());
			
			if (storedDataField.getProcessHeadId()!=processDataField.getProcessHeadId()) {
				throw new WProcessDataFieldException("WProcessDataFieldDao: update - change of process head id is not permitted. You must delete the datafield and create a new one in the other process... ");
			}
			
			TableManager tm = new TableManager();
			ManagedData managedData = new ManagedData();

			// returns managedData if there is defined (processHead) and table exists
			// if not there is another instance to create the managed table
			managedData = 
					getAndCheckManagedData(processDataField.getProcessHeadId(), tm, managedData);

			// update process datafield
			HibernateUtil.actualizar(processDataField);

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

	public void delete(WProcessDataField processDataField) throws WProcessDataFieldException {

		logger.debug("delete() WProcessDataField - Name: ["+processDataField.getName()+"]");
		
		try {
			
			TableManager tm = new TableManager();
			ManagedData managedData = new ManagedData();

			// returns managedData if there is defined (processHead) and table exists
			// if not there is another instance to create the managed table
			managedData = getAndCheckManagedData(processDataField.getProcessHeadId(), tm, managedData);	
			
			Integer qtyRecsNotNull = 
					tm.countNotNullRecords(
							managedData.getManagedTableConfiguration().getSchema(),
							managedData.getManagedTableConfiguration().getName(),
							processDataField.getName());

			if(qtyRecsNotNull<0) {
				logger.warn("Warning: table "+managedData.getManagedTableConfiguration().getName()+" does not exist. No action realized.");
			} else if(qtyRecsNotNull>0) {
				String mess = "WProcessDataFieldDao: delete: trying delete managed field name wich has "+qtyRecsNotNull+" records with information in the table. Options you have are to clean manually the table nullating this field, or deactivate the field in ProcessDef form.";
				throw new WProcessDataFieldException(mess);
			}
			
			HibernateUtil.borrar(processDataField);
			
			/*
			 *  synchronize managed table
			 *  
			 *  Only syncro if managed table exists and have their datafields
			 *  
			 */
			if ( managedData!=null && qtyRecsNotNull==0 ) {
				try {
					tm.deleteFieldSynchro(managedData, processDataField, false);
				} catch (TableManagerException e) {
					// TODO IMPLEMENTAR!!
					e.printStackTrace();
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

		} catch (ClassNotFoundException e) {
			logger.error("WProcessDataFieldDao:ClassNotFoundException delete - Can't delete process definition record "+ processDataField.getName() +
					" <id = "+processDataField.getId()+ "> \n"+" - "+e.getMessage()+"\n"+e.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao:ClassNotFoundException  delete - Can't delete process definition record  "+ processDataField.getName() +
					" <id = "+processDataField.getId()+ "> \n"+" - "+e.getMessage()+"\n"+e.getCause() );
		} catch (SQLException e) {
			logger.error("SQLException delete - Can't delete process definition record "+ processDataField.getName() +
					" <id = "+processDataField.getId()+ "> \n"+" - "+e.getMessage()+"\n"+e.getCause() );
			throw new WProcessDataFieldException("SQLException  delete - Can't delete process definition record  "+ processDataField.getName() +
					" <id = "+processDataField.getId()+ "> \n"+" - "+e.getMessage()+"\n"+e.getCause() );
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

	
	public WProcessDataField getWProcessDataFieldByName(String name) throws WProcessDataFieldException {

		WProcessDataField process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessDataField) session.createCriteria(WProcessDataField.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDataFieldDao: getWProcessDataFieldByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("getWProcessDataFieldByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

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
	public List<WProcessDataField> getWProcessDataFieldList(Integer processHeadId) throws WProcessDataFieldException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessDataField> processs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processs = session
					.createQuery("From WProcessDataField pdfi WHERE pdfi.processHeadId= ? order by id ")
					.setInteger(0, processHeadId)
					.list();
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDataFieldDao: getWProcessDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao: getWProcessDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processs;
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
			Integer processHeadId, TableManager tm,	ManagedData managedData) {
		try {
			WProcessHead processHead = new WProcessHead();
			
			processHead = new WProcessHeadDao()
								.getWProcessHeadByPK(processHeadId);
	
			if (processHead!=null
					&& processHead.getManagedTableConfiguration()!=null
					&& processHead.getManagedTableConfiguration().getName()!=null
					&& !"".equals(processHead.getManagedTableConfiguration().getName())) {
	
				Integer reccount = 
						tm.checkTableExists(
								processHead.getManagedTableConfiguration().getSchema(),
								processHead.getManagedTableConfiguration().getName() );
				if (reccount>=0) {
					managedData
						.setManagedTableConfiguration(
								processHead.getManagedTableConfiguration() );
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
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return managedData;
	}
	
	
}
	
