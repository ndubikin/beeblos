package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepDataFieldException;
import org.beeblos.bpm.core.model.WStepDataField;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.hb4util.core.util.HibernateUtil;
import com.sp.common.util.StringPair;


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
		logger.debug("getWStepDataFieldList: loading step data fields...  processHeadId:["
						+(processHeadId!=null?processHeadId:"null")+"]"
						+"stepHeadId:["+(stepHeadId!=null?stepHeadId:"null")+"]");
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepDataField> stepDataFieldList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			// nes 20140208 - cambiado a criteria ...
			
	        Criteria criteria = session.createCriteria(WStepDataField.class.getName());
	        
	        if (processHeadId != null && !processHeadId.equals(0)){
	        	criteria.add(Restrictions.eq("processHeadId", processHeadId));
	        }
	        if (stepHeadId != null && !stepHeadId.equals(0)){
	        	criteria.add(Restrictions.eq("stepHeadId", stepHeadId));
	        }

	        stepDataFieldList = criteria.list();	
			
//			if (stepHeadId==null) {
//				processList = session
//						.createQuery("From  sdf WHERE processHeadId= :phid ? Order By order, dataField.id ")
//						.setInteger("phid", processHeadId)
//						.list();
//			} else if (processHeadId==null) {
//				processList = session
//						.createQuery("From WStepDataField sdf WHERE stepHeadId= :shid Order By order, dataField.id ")
//						.setInteger("shid", stepHeadId)
//						.list();
//			} else {
//				processList = session
//						.createQuery("From WStepDataField sdf WHERE processHeadId= :phid AND stepHeadId= :shid Order By order, dataField.id ")
//						.setInteger("phid", processHeadId)
//						.setInteger("shid", stepHeadId)
//						.list();
//			}
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDataFieldDao: getWStepDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDataFieldException("WStepDataFieldDao: getWStepDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return stepDataFieldList;
	}
	
	public Integer countWStepDataFieldList(Integer processHeadId) 
			throws WStepDataFieldException {
		logger.debug("countWStepDataFieldList:   processHeadId:["
				+(processHeadId!=null?processHeadId:"null")+"]");
		
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
			String mess="WStepDataFieldDao: getWStepDataFieldList(processHeadId) - can't obtain process list for the value (processHeadId:" + processHeadId + "): " +
								ex.getMessage()+"\n"+ex.getCause() ;
			logger.warn(mess);
			throw new WStepDataFieldException(mess);

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
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String separationLine, Integer stepHeadId )
	throws WStepDataFieldException {
		 
		List<WStepDataField> lpdf = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			lpdf = session
					.createQuery("From WStepDataField sdfi WHERE sdfi.stepHeadId= :stepHeadId Order By sdfi.name ")
					.setInteger("stepHeadId", stepHeadId)
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
				
				for (WStepDataField sdfi: lpdf) {
					retorno.add(new StringPair(sdfi.getId(),sdfi.getName()));
				}
			} else {
				// nes  - si el select devuelve null entonces devuelvo null
				retorno=null;
			}
			
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new WStepDataFieldException(
					"Can't obtain WStepDataField combo list "
					+ex.getMessage()+"\n"+ex.getCause());
		} catch (Exception e) {}

		return retorno;

	}

}
	
