package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;



public class WUserDefDao {
	
	private static final Log logger = LogFactory.getLog(WUserDefDao.class);
	
	public WUserDefDao (){
		super();
	}
	
	public Integer add(WUserDef user) throws WUserDefException {
		
		logger.debug("add() WUserDef - Name: ["+user.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(user));

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

			HibernateUtil.actualizar(user);


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

			HibernateUtil.borrar(user);

		} catch (HibernateException ex) {
			logger.error("WUserDefDao: delete - Can't delete user definition record " +
					" <id = "+userId+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("WUserDefDao:  delete - Can't delete user definition record  " +
					" <id = "+userId+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WUserDefException ex1) {
			logger.error("WUserDefDao: delete - Exception in deleting user rec "+ 
					" <id = "+userId+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
			throw new WUserDefException("WUserDefDao: delete - Exception in deleting user rec "+ 
					" <id = "+userId+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}
	
	public void delete(WUserDef user) throws WUserDefException {

		logger.debug("delete() WUserDef - Name: ["+user.getName()+"]");
		
		try {

			user = getWUserDefByPK(user.getId());

			HibernateUtil.borrar(user);

		} catch (HibernateException ex) {
			logger.error("WUserDefDao: delete - Can't delete user definition record "+ user.getName() +
					" <id = "+user.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("WUserDefDao:  delete - Can't delete user definition record  "+ user.getName() +
					" <id = "+user.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WUserDefException ex1) {
			logger.error("WUserDefDao: delete - Exception in deleting user rec "+ user.getName() +
					" <id = "+user.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
			throw new WUserDefException("WUserDefDao: delete - Exception in deleting user rec "+ user.getName() +
					" <id = "+user.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

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

	 /**
	   * returns the user list for a given Role
	   * orderBy: id (user) or name
	   */
	public List<WUserDef> getWUserDefByRole( Integer idRole, String orderBy ) throws WUserDefException {
		
		return new WUserRoleDao().getWUserDefByRole(idRole, orderBy);

	}

	 /**
	   * returns the ID user list for a given Role
	   * orderBy: id (user) or name
	   */
	public List<Integer> getWUserDefIdByRole( Integer idRole ) throws WUserDefException {

		return new WUserRoleDao().getWUserDefIdByRole(idRole);
	}
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WUserDefException {
		 
			List<WUserDef> lwpd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From WUserDef order by name ")
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
					
				
					
					for (WUserDef wpd: lwpd) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WUserDefException(
						"Can't obtain WUserDefs combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

	
	
	private String getSQLOrder(String order) {

		if ( order==null || "".equals(order) ) {
			return " ORDER BY name ";	
		} else {
			return " ORDER BY "+order+" ";
		}
		
		
	}
}
	