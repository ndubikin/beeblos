package org.beeblos.bpm.core.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;



public class WUserRoleDao {
	
	private static final Log logger = LogFactory.getLog(WUserRoleDao.class.getName());
	
	public WUserRoleDao (){
		super();
	}
	
	 /**
	   * returns the user list for a given Role
	   * orderBy: id (user) or name
	   */
	public List<WUserDef> getWUserDefByRole( Integer idRole, String orderBy ) throws WUserDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;

		List<WUserDef> users = null;
		
		String query="SELECT *  " + getSelectPhrase();
		String filter = getFilter();
		String order = " ORDER BY "+(orderBy==null || orderBy.equals("id")?"u.id ":"u.name ");

		query += filter+order;
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			q = session
					.createSQLQuery(query)
					.addEntity("WUserDef", WUserDef.class)
					.setInteger("idRole", idRole);
			
			users = q.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WUserDefDao: getWUserDefByRole() - can't obtain user list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("WUserDefDao: getWUserDefByRole() - can't obtain user list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return users;
	}

	private String getFilter() {
		String filter = " WHERE wur.id_role=:idRole ";
		return filter;
	}

	 /**
	   * returns the ID user list for a given Role
	   * orderBy: id (user) or name
	   */
	public List<Integer> getWUserDefIdByRole( Integer idRole ) throws WUserDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;

		List<Integer> userIds = null;
		
		String query = "SELECT u.id " + getSelectPhrase();
		String filter = getFilter();
		String order = " ORDER BY u.id ";

		query += filter+order;
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			q = session
					.createSQLQuery(query)
					.setInteger("idRole", idRole);;
					
			
			userIds = q.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WUserDefDao: getWUserDefIdByRole() - can't obtain user list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("WUserDefDao: getWUserDefIdByRole() - can't obtain user list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return userIds;
	}

	private String getSelectPhrase() {
		String query="FROM w_user_def u ";
		query +="left join w_user_role wur on u.id=wur.id_user ";
		return query;
	}
	
}
	