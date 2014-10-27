package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WTaskTypeException;
import org.beeblos.bpm.core.model.WTaskType;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;

public class WTaskTypeDao {

	private static final Log logger = LogFactory.getLog(WTaskTypeDao.class.getName());
	
	public WTaskTypeDao (){
		
	}
	
	public Integer add(WTaskType taskType) throws WTaskTypeException {
		
		logger.debug("add() WTaskType - Name: ["+taskType.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(taskType));

		} catch (HibernateException ex) {
			logger.error("WTaskTypeDao: add - Can't store process definition record "+ 
					taskType.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WTaskTypeException("WTaskTypeDao: add - Can't store process definition record "+ 
					taskType.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WTaskType taskType) throws WTaskTypeException {
		
		logger.debug("update() WTaskType < id = "+taskType.getId()+">");
		
		try {

			HibernateUtil.update(taskType);


		} catch (HibernateException ex) {
			logger.error("WTaskTypeDao: update - Can't update process definition record "+ 
					taskType.getName()  +
					" - id = "+taskType.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WTaskTypeException("WTaskTypeDao: update - Can't update process definition record "+ 
					taskType.getName()  +
					" - id = "+taskType.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WTaskType taskType) throws WTaskTypeException {

		logger.debug("delete() WTaskType - Name: ["+taskType.getName()+"]");
		
		try {

			HibernateUtil.delete(taskType);

		} catch (HibernateException ex) {
			logger.error("WTaskTypeDao: delete - Can't delete proccess definition record "+ taskType.getName() +
					" <id = "+taskType.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WTaskTypeException("WTaskTypeDao:  delete - Can't delete proccess definition record  "+ taskType.getName() +
					" <id = "+taskType.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} 

	}

	public WTaskType getWTaskTypeByPK(Integer id) throws WTaskTypeException {

		WTaskType taskType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			taskType = (WTaskType) session.get(WTaskType.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WTaskTypeDao: getWTaskTypeByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WTaskTypeException("WTaskTypeDao: getWTaskTypeByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return taskType;
	}
	
	
	public WTaskType getWTaskTypeByName(String name) throws WTaskTypeException {

		WTaskType taskType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			taskType = (WTaskType) session.createCriteria(WTaskType.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WTaskTypeDao: getWTaskTypeByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WTaskTypeException("getWTaskTypeByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return taskType;
	}

	
	public List<WTaskType> getWTaskTypeList() throws WTaskTypeException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WTaskType> taskType = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			taskType = session.createQuery("From WTaskType order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WTaskTypeDao: getWTaskTypeList() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WTaskTypeException("WTaskTypeDao: getWTaskTypeList() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return taskType;
	}
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String separationLine )
	throws WTaskTypeException {
		 
			List<WTaskType> ldt = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				ldt = session
						.createQuery("From WTaskType Order By name ")
						.list();
		
				if (ldt!=null) {
					
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
					
				
					
					for (WTaskType dt: ldt) {
						retorno.add(new StringPair(dt.getId(),dt.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				tx.commit();

			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WTaskTypeException(
						"Can't obtain WTaskType combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}
}
