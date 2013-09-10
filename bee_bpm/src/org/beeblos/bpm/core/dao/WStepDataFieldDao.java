package org.beeblos.bpm.core.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepDataFieldException;
import org.beeblos.bpm.core.model.WStepDataField;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;


public class WStepDataFieldDao {
	
	private static final Log logger = LogFactory.getLog(WStepDataFieldDao.class.getName());
	
	public WStepDataFieldDao(){
		
	}
	
	public Integer add(WStepDataField stepDataField) throws WStepDataFieldException {
		
		logger.debug("add() WStepDataField - Name: ["+stepDataField.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(stepDataField));

		} catch (HibernateException ex) {
			logger.error("WStepDataFieldDao: add - Can't store process stepDataField definition record "+ 
					stepDataField.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDataFieldException("WStepDataFieldDao: add - Can't store process definition record "+ 
					stepDataField.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WStepDataField stepDataField) throws WStepDataFieldException {
		
		logger.debug("update() WStepDataField < id = "+stepDataField.getId()+">");
		
		try {

			HibernateUtil.update(stepDataField);


		} catch (HibernateException ex) {
			logger.error("WStepDataFieldDao: update - Can't update process definition record "+ 
					stepDataField.getName()  +
					" - id = "+stepDataField.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WStepDataFieldException("WStepDataFieldDao: update - Can't update process definition record "+ 
					stepDataField.getName()  +
					" - id = "+stepDataField.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WStepDataField stepDataField) throws WStepDataFieldException {

		logger.debug("delete() WStepDataField - Name: ["+stepDataField.getName()+"]");
		
		try {

			HibernateUtil.delete(stepDataField);

		} catch (HibernateException ex) {
			logger.error("WStepDataFieldDao: delete - Can't delete process definition record "+ stepDataField.getName() +
					" <id = "+stepDataField.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDataFieldException("WStepDataFieldDao:  delete - Can't delete process definition record  "+ stepDataField.getName() +
					" <id = "+stepDataField.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} 

	}

	public WStepDataField getWStepDataFieldByPK(Integer id) throws WStepDataFieldException {

		WStepDataField process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			process = (WStepDataField) session.get(WStepDataField.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDataFieldDao: getWStepDataFieldByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDataFieldException("WStepDataFieldDao: getWStepDataFieldByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}

	
	public WStepDataField getWStepDataFieldByName(String name) throws WStepDataFieldException {

		WStepDataField process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WStepDataField) session.createCriteria(WStepDataField.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDataFieldDao: getWStepDataFieldByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDataFieldException("getWStepDataFieldByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}

	
	@SuppressWarnings("unchecked")
	public List<WStepDataField> getWStepDataFieldList() throws WStepDataFieldException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepDataField> processList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processList = session.createQuery("From WStepDataField order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDataFieldDao: getWStepDataFieldList() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDataFieldException("WStepDataFieldDao: getWStepDataFieldList() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processList;
	}

	//rrl 20130730
	@SuppressWarnings("unchecked")
	public List<WStepDataField> getWStepDataFieldList(Integer processHeadId, Integer stepHeadId) 
			throws WStepDataFieldException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepDataField> processList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			if (stepHeadId==null) {
				processList = session
						.createQuery("From WStepDataField sdf WHERE processHeadId= ? Order By order, dataField.id ")
						.setInteger(0, processHeadId)
						.list();
			} else if (processHeadId==null) {
				processList = session
						.createQuery("From WStepDataField sdf WHERE stepHeadId= ? Order By order, dataField.id ")
						.setInteger(0, stepHeadId)
						.list();
			} else {
				processList = session
						.createQuery("From WStepDataField sdf WHERE processHeadId= ? AND stepHeadId= ? Order By order, dataField.id ")
						.setInteger(0, processHeadId)
						.setInteger(1, stepHeadId)
						.list();
			}
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDataFieldDao: getWStepDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDataFieldException("WStepDataFieldDao: getWStepDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processList;
	}
	
	public Integer countWStepDataFieldList(Integer processHeadId) 
			throws WStepDataFieldException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Long qty = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			qty = (Long) session
					.createQuery("Select Count (id) as count From WStepDataField pdfi WHERE stepHeadId= ? ")
					.setInteger(0, processHeadId)
					.uniqueResult();
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDataFieldDao: getWStepDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDataFieldException("WStepDataFieldDao: getWStepDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return qty.intValue();
	}
	
	public Set<WStepDataField> getWStepDataFieldStepFilteredSet(Integer stepHeadId) throws WStepDataFieldException {

		Set<WStepDataField> dataFieldSet = new HashSet<WStepDataField>(0);

		List<WStepDataField> listDF = getWStepDataFieldList(null, stepHeadId);
		for (WStepDataField dataField: listDF) {
			dataFieldSet.add(dataField);
		}
	
		return dataFieldSet;
	}
	
}
	
