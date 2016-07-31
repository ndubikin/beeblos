package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WUserDef;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sp.hb4util.core.util.HibernateUtil;
import com.sp.common.util.ListUtils;
import com.sp.common.util.StringPair;



public class WUserDefDao {
	
	private static final Log logger = LogFactory.getLog(WUserDefDao.class.getName());
	
	public WUserDefDao (){
		super();
	}
	
	public Integer add(WUserDef user) throws WUserDefException {
		
		logger.debug("add() WUserDef - Name: ["+user.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(user));

		} catch (HibernateException ex) {
			logger.error("WUserDefDao: add - Can't store user definition record "+ 
					user.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("WUserDefDao: add - Can't store user definition record "+ 
					user.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WUserDef user) throws WUserDefException {
		
		logger.debug("update() WUserDef < id = "+user.getId()+">");
		
		try {

			HibernateUtil.update(user);


		} catch (HibernateException ex) {
			logger.error("WUserDefDao: update - Can't update user definition record "+ 
					user.getName()  +
					" - id = "+user.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WUserDefException("WUserDefDao: update - Can't update user definition record "+ 
					user.getName()  +
					" - id = "+user.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	public void delete(Integer userId) throws WUserDefException {

		logger.debug("delete() WUserDef - Name: ["+userId+"]");
		WUserDef user=null;
		try {

			user = getWUserDefByPK(userId);

			HibernateUtil.delete(user);

		} catch (HibernateException ex) {
			logger.error("WUserDefDao: delete - Can't delete user definition record " +
					" <id = "+userId+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("WUserDefDao:  delete - Can't delete user definition record  " +
					" <id = "+userId+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WUserDefException e) {
			logger.error("WUserDefDao: delete - Exception in deleting user rec "+ 
					" <id = "+userId+ "> no esta almacenada \n"+" - "+e.getMessage()+"\n"+e.getCause() );
			throw new WUserDefException("WUserDefDao: delete - Exception in deleting user rec "+ 
					" <id = "+userId+ "> not stored \n"+" - "+e.getMessage()+"\n"+e.getCause() );

		} 

	}
	
	public void delete(WUserDef user) throws WUserDefException {

		logger.debug("delete() WUserDef - Name: ["+user.getName()+"]");
		
		try {

			user = getWUserDefByPK(user.getId());

			HibernateUtil.delete(user);

		} catch (HibernateException ex) {
			logger.error("WUserDefDao: delete - Can't delete user definition record "+ user.getName() +
					" <id = "+user.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("WUserDefDao:  delete - Can't delete user definition record  "+ user.getName() +
					" <id = "+user.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WUserDefException e) {
			logger.error("WUserDefDao: delete - Exception in deleting user rec "+ user.getName() +
					" <id = "+user.getId()+ "> no esta almacenada \n"+" - "+e.getMessage()+"\n"+e.getCause() );
			throw new WUserDefException("WUserDefDao: delete - Exception in deleting user rec "+ user.getName() +
					" <id = "+user.getId()+ "> not stored \n"+" - "+e.getMessage()+"\n"+e.getCause() );

		} 

	}

	public WUserDef getWUserDefByPK(Integer id) throws WUserDefException {

		WUserDef user = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			user = (WUserDef) session.get(WUserDef.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WUserDefDao: getWUserDefByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("WUserDefDao: getWUserDefByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return user;
	}
	
	
	public WUserDef getWUserDefByName(String name) throws WUserDefException {

		WUserDef  user = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			user = (WUserDef) session.createCriteria(WUserDef.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WUserDefDao: getWUserDefByName - can't obtain user name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("getWUserDefByName;  can't obtain user name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return user;
	}

	
	public List<WUserDef> getWUserDefs() throws WUserDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WUserDef> users = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			users = session.createQuery("From WUserDef order by name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WUserDefDao: getWUserDefs() - can't obtain user list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("WUserDefDao: getWUserDefs() - can't obtain user list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return users;
	}
	
	// dml 20120425
	public List<WUserDef> getWUserDefList(WUserDef wUserDef) throws WUserDefException{
		
		String filter = getSQLFilter(wUserDef);
		
		filter = (( filter != null && !"".equals(filter)) ? " WHERE "+filter:"");

		String query = _buildQuery(filter);

		return getWUserDefList(query);
	}

	/**
	 * Gets the WUserDef list where its ids are into pkList
	 * 
	 * @author dmuleiro 20160525
	 * 
	 * @param pkList
	 * @return
	 * @throws WUserDefException
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<WUserDef> getWUserDefByPkList(List<String> pkList) throws WUserDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WUserDef> luser = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			if (pkList != null && !pkList.isEmpty()){
				
				String pkListParam = ListUtils.getStringListAsString(pkList, ",", true);
				
				luser = session.createQuery("From WUserDef Where id In (" + pkListParam + ") Order By Name ")
						.list();

			}
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException - getWUserDefByPkList() - can't obtain user list"
					+ (ex.getMessage()!=null?". "+ex.getMessage():"")
					+ (ex.getCause()!=null?". "+ex.getCause():"") ;
			logger.error(mess);
			throw new WUserDefException(mess);
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException - getWUserDefByPkList() - can't obtain user list"
					+ (ex.getMessage()!=null?". "+ex.getMessage():"")
					+ (ex.getCause()!=null?". "+ex.getCause():"")
					+ (ex.getClass()!=null?". "+ex.getClass():"") ;
			logger.error(mess);
			throw new WUserDefException(mess);
		}

		return luser;
	}

	// dml 20120425
	private String getSQLFilter (WUserDef wUserDef) {

		String filter="";
		
		if (wUserDef != null) {
			
			if ( wUserDef.getName()!=null && !"".equals(wUserDef.getName())) {
				if ( filter ==null || !"".equals(filter)) {
					filter +=" AND ";
				}
				filter +=" wud.name LIKE '%"+wUserDef.getName()+"%' ";
			}
			
			if ( wUserDef.getLogin()!=null && !"".equals(wUserDef.getLogin())) {
				if ( filter ==null || !"".equals(filter)) {
					filter +=" AND ";
				}
				filter +=" wud.login LIKE '%"+wUserDef.getLogin()+"%' ";
			}
			
			if ( wUserDef.getEmail()!=null && !"".equals(wUserDef.getEmail())) {
				if ( filter ==null || !"".equals(filter)) {
					filter +=" AND ";
				}
				filter +=" wud.email LIKE '%"+wUserDef.getEmail()+"%' ";
			}
			
		}
		
		return filter;
	}

	// dml 20120425
	private String _buildQuery(String filter) {

		String tmpQuery = "SELECT ";
		tmpQuery += " wud.id, ";
		tmpQuery += " wud.name, ";
		tmpQuery += " wud.login, ";
		tmpQuery += " wud.email ";

		tmpQuery += " FROM w_user_def wud ";
		
		tmpQuery += filter;

		tmpQuery += " ORDER BY wud.mod_date ";

		logger.debug("------>> getWUserDefByFinder -> query:" + tmpQuery + "<<-------");

		System.out.println("QUERY:" + tmpQuery);

		return tmpQuery;
	}

	// dml 20120425
	public List<WUserDef> getWUserDefList(String query)
			throws WUserDefException {

		WUserDef wud;
		
		Session session = null;
		Transaction tx = null;

		List<Object[]> result = null;
		List<WUserDef> ret = new ArrayList<WUserDef>();

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			Hibernate.initialize(result);

			result = session.createSQLQuery(query).list();

			tx.commit();

			if (result != null) {

				for (Object irObj : result) {

					Object[] cols = (Object[]) irObj;
					
					wud = new WUserDef();

					wud.setId((cols[0] != null ? new Integer(
							cols[0].toString()) : null));
					wud.setName(cols[1] != null ? cols[1].toString() : "");
					wud.setLogin(cols[2] != null ? cols[2].toString() : "");
					wud.setEmail(cols[3] != null ? cols[3].toString() : "");					
					
					ret.add(wud);
				}

			} else {
				// Si el select devuelve null entonces devuelvo null
				ret = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("Exception WUserDefDao: getWUserDefByFinder() - can't obtain user list - "
					+ ex.getMessage()
					+ "\n"
					+ ex.getLocalizedMessage()
					+ " \n"
					+ ex.getCause());
			throw new WUserDefException(ex);

		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WUserDefException {
		 
			List<WUserDef> lwpd = null;
			List<StringPair> ret = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From WUserDef order by name ")
						.list();
		
				tx.commit();

				if (lwpd!=null) {
					
					// inserta los extras
					if ( firstLineText!=null && !"".equals(firstLineText) ) {
						if ( !firstLineText.equals("WHITESPACE") ) {
							ret.add(new StringPair(null,firstLineText));  // deja la primera línea con lo q venga
						} else {
							ret.add(new StringPair(null," ")); // deja la primera línea en blanco ...
						}
					}
					
					if ( blank!=null && !"".equals(blank) ) {
						if ( !blank.equals("WHITESPACE") ) {
							ret.add(new StringPair(null,blank));  // deja la separación línea con lo q venga
						} else {
							ret.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
						}
					}
					
				
					
					for (WUserDef wpd: lwpd) {
						ret.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					ret=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WUserDefException(
						"Can't obtain WUserDefs combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return ret;


	}

}
	