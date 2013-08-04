package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.model.WProcessDataField;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;


public class WProcessDataFieldDao {
	
	private static final String ORG_HIBERNATE_EXCEPTION_CONSTRAINT_VIOLATION_EXCEPTION = "org.hibernate.exception.ConstraintViolationException";
	private static final Log logger = LogFactory.getLog(WProcessDataFieldDao.class.getName());
	
	public WProcessDataFieldDao(){
		
	}
	
	public Integer add(WProcessDataField processDataField) throws WProcessDataFieldException {
		
		logger.debug("add() WProcessDataField - Name: ["+processDataField.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(processDataField));

		} catch (HibernateException ex) {
			logger.error("WProcessDataFieldDao: add - Can't store process processDataField definition record "+ 
					processDataField.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao: add - Can't store process definition record "+ 
					processDataField.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WProcessDataField processDataField) throws WProcessDataFieldException {
		
		logger.debug("update() WProcessDataField < id = "+processDataField.getId()+">");
		
		try {

			HibernateUtil.actualizar(processDataField);


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

			HibernateUtil.borrar(processDataField);

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
	
	
}
	