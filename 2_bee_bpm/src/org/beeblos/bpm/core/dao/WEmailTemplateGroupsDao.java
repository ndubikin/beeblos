package org.beeblos.bpm.core.dao;

// Generado 25-nov-2010 12:52:05 con Hibernate Tools 3.3.0.GA

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WEmailTemplateGroupsException;
import org.beeblos.bpm.core.model.WEmailTemplateGroups;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;

/**
 * Dao object for domain model class WEmailTemplateGroups.
 * 
 * @see org.beeblos.WEmailTemplateGroups.st.model.bdc.model.WEmailTemplateGroups
 */

public class WEmailTemplateGroupsDao {

	private static final Log logger = LogFactory
			.getLog(WEmailTemplateGroupsDao.class);

	public WEmailTemplateGroupsDao() {

	}

	public Integer add(WEmailTemplateGroups instance)
			throws WEmailTemplateGroupsException {
		logger.debug("add() name: [" + instance.getName() + "]");
		logger.info("add() name: [" + instance.getName() + "]");
		try {
			return Integer.valueOf(HibernateUtil.save(instance));
		} catch (HibernateException ex) {
			logger.error("WEmailTemplateGroupsDao: add - Cannot save WEmailTemplateGroups "
					+ instance.getName());
			throw new WEmailTemplateGroupsException(ex);
		}
	}

	public void update(WEmailTemplateGroups instance)
			throws WEmailTemplateGroupsException {

		logger.debug("update() WEmailTemplateGroups < id = " + instance.getId()
				+ ">");

		try {

			HibernateUtil.update(instance);

			logger.info("update() WEmailTemplateGroups < id = "
					+ instance.getId() + ">");

		} catch (HibernateException ex) {
			logger.error("WEmailTemplateGroupsDao:update - Cannot update WEmailTemplateGroups "
					+ instance.getName()
					+ " - id = "
					+ instance.getId()
					+ "\n - " + ex.getMessage());
			throw new WEmailTemplateGroupsException(ex);

		}

	}

	public void delete(WEmailTemplateGroups instance)
			throws WEmailTemplateGroupsException {

		logger.debug("delete() name: [" + instance.getName() + "]");

		try {

			instance = getWEmailTemplateGroupsByPK(instance.getId());

			HibernateUtil.delete(instance);

			logger.info("delete() WEmailTemplateGroups < id = "
					+ instance.getId() + ">");

		} catch (HibernateException ex) {
			logger.error("WEmailTemplateGroupsDao: delete - It is not possible to delete the instance "
					+ instance.getName()
					+ " <id = "
					+ instance.getId()
					+ "> \n" + " - " + ex.getMessage());
			throw new WEmailTemplateGroupsException(ex);

		} catch (WEmailTemplateGroupsException e) {
			logger.error("WEmailTemplateGroupsDao: delete - The instance "
					+ instance.getName() + " <id = " + instance.getId()
					+ "> does not exist \n" + " - " + e.getMessage());
			throw new WEmailTemplateGroupsException(e);

		}

	}

	public WEmailTemplateGroups getWEmailTemplateGroupsByPK(Integer i)
			throws WEmailTemplateGroupsException {

		WEmailTemplateGroups instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			instance = (WEmailTemplateGroups) session.get(
					WEmailTemplateGroups.class, i);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailTemplateGroupsDao: getWEmailTemplateGroupsByPK - There are not any WEmailTemplateGroups with id = "
					+ i + "]  almacenado - \n" + ex.getMessage());
			throw new WEmailTemplateGroupsException("The WEmailTemplateGroups: "
					+ i + " - " + ex.getMessage() + "does not exist.");

		}

		return instance;
	}

	public WEmailTemplateGroups getWEmailTemplateGroupsByName(String instanceName)
			throws WEmailTemplateGroupsException {

		WEmailTemplateGroups instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instance = (WEmailTemplateGroups) session
					.createCriteria(WEmailTemplateGroups.class)
					.add(Restrictions.naturalId().set("name", instanceName))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailTemplateGroupsDao: getWEmailTemplateGroupsByName - The WEmailTemplateGroups with name: "
					+ instanceName
					+ " - "
					+ ex.getMessage()
					+ " does not exist");
			throw new WEmailTemplateGroupsException(
					"The WEmailTemplateGroups with name: " + instanceName + " - "
							+ ex.getMessage() + " does not exist");

		}

		return instance;
	}

	public List<WEmailTemplateGroups> getWEmailTemplateGroupsList()
			throws WEmailTemplateGroupsException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WEmailTemplateGroups> instances = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instances = session.createQuery(
					"From WEmailTemplateGroups order by id").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailTemplateGroupsDao: getWEmailTemplateGroupsList() - It was not possible to get the WEmailTemplateGroups list - "
					+ ex.getMessage());

			throw new WEmailTemplateGroupsException(
					"Error getting the WEmailTemplateGroupsList: "
							+ ex.getMessage());

		}

		return instances;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WEmailTemplateGroupsException {
		 
			List<WEmailTemplateGroups> lwpd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From WEmailTemplateGroups order by name ")
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
					
				
					
					for (WEmailTemplateGroups wpd: lwpd) {
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
				throw new WEmailTemplateGroupsException(
						"Can't obtain WEmailTemplateGroups combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

/*
	public List<WEmailTemplateGroups> wEmailAccountsFinder(String nameFilter, String emailFilter)
			throws WEmailTemplateGroupsException {

		List<WEmailTemplateGroups> result = null;
		// List<StringPair> result = new ArrayList<StringPair>(10);

		
		String query="SELECT *  " + getSelectPhrase();
		String order = " ORDER BY wuea.name ";
		String filter = "";
		if (nameFilter != null && !"".equals(nameFilter)) {
			filter = "WHERE wuea.name LIKE '%"+nameFilter+"%' " ;
		}
		if (emailFilter != null && !"".equals(emailFilter)) {
			if ("".equals(filter)) {
				filter += " WHERE ";
			} else {
				filter += " OR ";
			}
			filter += " wuea.email LIKE '%"+emailFilter+"%' " ;
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

			q = session.createSQLQuery(query).addEntity("WEmailTemplateGroups", WEmailTemplateGroups.class);
			
			result = q.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailTemplateGroupsDao: wEmailAccountsFinder - Cannot get the finder list: "
					+ nameFilter
					+ " - "
					+ ex.getMessage()
					+ " does not exist");
			throw new WEmailTemplateGroupsException(
					"The WEmailTemplateGroups with name: " + nameFilter + " - "
							+ ex.getMessage() + " does not exist");

		}

		return result;
	}

	private String getSelectPhrase() {
		String query="FROM w_user_email_accounts wuea ";
		return query;
	}
	*/
	public boolean duplicatedNameVerification(String instanceName, Integer id)
			throws WEmailTemplateGroupsException {

		WEmailTemplateGroups instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instance = (WEmailTemplateGroups) session
					.createQuery(
							"From WEmailTemplateGroups WHERE  name= ? AND id != ?")
					.setString(0, instanceName).setInteger(1, id)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailTemplateGroupsDao: duplicatedNameVerification - Cannot load un WEmailTemplateGroups "
					+ instanceName + " - " + ex.getMessage());
			throw new WEmailTemplateGroupsException(
					"Cannot obtain the WEmailTemplateGroups: " + instanceName
							+ " - " + ex.getMessage() + " ; " + ex.getCause());

		}
		if (instance != null)
			return true;
		else
			return false;
	}

}
