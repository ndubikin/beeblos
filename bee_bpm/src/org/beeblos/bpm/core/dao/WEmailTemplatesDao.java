package org.beeblos.bpm.core.dao;

// Generado 25-nov-2010 12:52:05 con Hibernate Tools 3.3.0.GA

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.error.WEmailTemplatesException;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WEmailTemplates;
import com.sp.common.util.StringPair;
import com.sp.common.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 * Dao object for domain model class WEmailTemplates.
 * 
 * @see org.beeblos.WEmailTemplates.st.model.bdc.model.WEmailTemplates
 */

public class WEmailTemplatesDao {

	private static final Log logger = LogFactory
			.getLog(WEmailTemplatesDao.class);

	public WEmailTemplatesDao() {

	}

	public Integer add(WEmailTemplates instance)
			throws WEmailTemplatesException {
		logger.debug("add() name: [" + instance.getName() + "]");
		logger.info("add() name: [" + instance.getName() + "]");
		try {
			return Integer.valueOf(HibernateUtil.save(instance));
		} catch (HibernateException ex) {
			logger.error("WEmailTemplatesDao: add - Cannot save WEmailTemplates "
					+ instance.getName());
			throw new WEmailTemplatesException(ex);
		}
	}

	public void update(WEmailTemplates instance)
			throws WEmailTemplatesException {

		logger.debug("update() WEmailTemplates < id = " + instance.getId()
				+ ">");

		try {

			HibernateUtil.update(instance);

			logger.info("update() WEmailTemplates < id = "
					+ instance.getId() + ">");

		} catch (HibernateException ex) {
			logger.error("WEmailTemplatesDao:update - Cannot update WEmailTemplates "
					+ instance.getName()
					+ " - id = "
					+ instance.getId()
					+ "\n - " + ex.getMessage());
			throw new WEmailTemplatesException(ex);

		}

	}

	public void delete(WEmailTemplates instance)
			throws WEmailTemplatesException {

		logger.debug("delete() name: [" + instance.getName() + "]");

		try {

			instance = getWEmailTemplatesByPK(instance.getId());

			HibernateUtil.delete(instance);

			logger.info("delete() WEmailTemplates < id = "
					+ instance.getId() + ">");

		} catch (HibernateException ex) {
			logger.error("WEmailTemplatesDao: delete - It is not possible to delete the instance "
					+ instance.getName()
					+ " <id = "
					+ instance.getId()
					+ "> \n" + " - " + ex.getMessage());
			throw new WEmailTemplatesException(ex);

		} catch (WEmailTemplatesException e) {
			logger.error("WEmailTemplatesDao: delete - The instance "
					+ instance.getName() + " <id = " + instance.getId()
					+ "> does not exist \n" + " - " + e.getMessage());
			throw new WEmailTemplatesException(e);

		}

	}

	public WEmailTemplates getWEmailTemplatesByPK(Integer i)
			throws WEmailTemplatesException {

		WEmailTemplates instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			instance = (WEmailTemplates) session.get(
					WEmailTemplates.class, i);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailTemplatesDao: getWEmailTemplatesByPK - There are not any WEmailTemplates with id = "
					+ i + "]  almacenado - \n" + ex.getMessage());
			throw new WEmailTemplatesException("The WEmailTemplates: "
					+ i + " - " + ex.getMessage() + "does not exist.");

		}

		return instance;
	}

	public WEmailTemplates getWEmailTemplatesByName(String instanceName)
			throws WEmailTemplatesException {

		WEmailTemplates instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instance = (WEmailTemplates) session
					.createCriteria(WEmailTemplates.class)
					.add(Restrictions.naturalId().set("name", instanceName))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailTemplatesDao: getWEmailTemplatesByName - The WEmailTemplates with name: "
					+ instanceName
					+ " - "
					+ ex.getMessage()
					+ " does not exist");
			throw new WEmailTemplatesException(
					"The WEmailTemplates with name: " + instanceName + " - "
							+ ex.getMessage() + " does not exist");

		}

		return instance;
	}

	public List<WEmailTemplates> getWEmailTemplatesList()
			throws WEmailTemplatesException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WEmailTemplates> instances = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instances = session.createQuery(
					"From WEmailTemplates order by id").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailTemplatesDao: getWEmailTemplatesList() - It was not possible to get the WEmailTemplates list - "
					+ ex.getMessage());

			throw new WEmailTemplatesException(
					"Error getting the WEmailTemplatesList: "
							+ ex.getMessage());

		}

		return instances;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WEmailTemplatesException {
		 
			List<WEmailTemplates> lwpd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From WEmailTemplates order by name ")
						.list();
		
				if (lwpd!=null) {
					
					// inserta los extras
					if ( firstLineText!=null && !"".equals(firstLineText) ) {
						if ( !firstLineText.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,firstLineText));  // deja la primera línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
						}
					}
					
					if ( blank!=null && !"".equals(blank) ) {
						if ( !blank.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,blank));  // deja la separación línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
						}
					}
					
				
					
					for (WEmailTemplates wpd: lwpd) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				tx.commit();

			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WEmailTemplatesException(
						"Can't obtain WEmailTemplates combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}


	public List<WEmailTemplates> wEmailTemplatesFinder(String nameFilter, String typeFilter)
			throws WEmailTemplatesException {

		List<WEmailTemplates> result = null;
		
		String query="SELECT *  " + getSelectPhrase();
		String order = " ORDER BY wuet.name ";
		String filter = "";
		if (nameFilter != null && !"".equals(nameFilter)) {
			filter = "WHERE wuet.name LIKE '%"+nameFilter+"%' " ;
		}
		if (typeFilter != null && !"".equals(typeFilter)) {
			if ("".equals(filter)){
				filter += " WHERE ";
			} else {
				filter += " OR ";
			}
			filter += "wuet.type = '"+typeFilter+"' " ;
		}

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;
		
		query += filter + order;
		
		System.out.println("QUERY: "+query);

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			q = session.createSQLQuery(query).addEntity("WEmailTemplates", WEmailTemplates.class);
			
			result = q.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailTemplatesDao: wEmailTempaltesFinder - Cannot get the finder list: "
					+ nameFilter
					+ " - "
					+ ex.getMessage()
					+ " does not exist");
			throw new WEmailTemplatesException(
					"The WEmailTemplates with name: " + nameFilter + " - "
							+ ex.getMessage() + " does not exist");

		}

		return result;
	}

	private String getSelectPhrase() {
		String query="FROM w_email_templates wuet ";
		return query;
	}
	
	public boolean duplicatedNameVerification(String instanceName, Integer id)
			throws WEmailTemplatesException {

		WEmailTemplates instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instance = (WEmailTemplates) session
					.createQuery(
							"From WEmailTemplates WHERE  name= ? AND id != ?")
					.setString(0, instanceName).setInteger(1, id)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailTemplatesDao: duplicatedNameVerification - Cannot load un WEmailTemplates "
					+ instanceName + " - " + ex.getMessage());
			throw new WEmailTemplatesException(
					"Cannot obtain the WEmailTemplates: " + instanceName
							+ " - " + ex.getMessage() + " ; " + ex.getCause());

		}
		if (instance != null)
			return true;
		else
			return false;
	}

}
