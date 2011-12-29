package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;



public class WRoleDefDao {
	
	private static final Log logger = LogFactory.getLog(WRoleDefDao.class);
	
	public WRoleDefDao (){
		super();
	}
	
	public Integer add(WRoleDef user) throws WRoleDefException {
		
		logger.debug("add() WRoleDef - Name: ["+user.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(user));

		} catch (HibernateException ex) {
			logger.error("WRoleDefDao: add - Can't store user definition record "+ 
					user.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("WRoleDefDao: add - Can't store user definition record "+ 
					user.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WRoleDef user) throws WRoleDefException {
		
		logger.debug("update() WRoleDef < id = "+user.getId()+">");
		
		try {

			HibernateUtil.actualizar(user);


		} catch (HibernateException ex) {
			logger.error("WRoleDefDao: update - Can't update user definition record "+ 
					user.getName()  +
					" - id = "+user.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WRoleDefException("WRoleDefDao: update - Can't update user definition record "+ 
					user.getName()  +
					" - id = "+user.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WRoleDef role) throws WRoleDefException {

		logger.debug("delete() WRoleDef - Name: ["+role.getName()+"]");
		
		try {

			role = getWRoleDefByPK(role.getId());

			HibernateUtil.borrar(role);

		} catch (HibernateException ex) {
			logger.error("WRoleDefDao: delete - Can't delete role definition record "+ role.getName() +
					" <id = "+role.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("WRoleDefDao:  delete - Can't delete role definition record  "+ role.getName() +
					" <id = "+role.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WRoleDefException ex1) {
			logger.error("WRoleDefDao: delete - Exception in deleting role rec "+ role.getName() +
					" <id = "+role.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
			throw new WRoleDefException("WRoleDefDao: delete - Exception in deleting role rec "+ role.getName() +
					" <id = "+role.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}
	
	public void delete(Integer roleId) throws WRoleDefException {

		logger.debug("delete() WRoleDef - Name: ["+roleId+"]");
		WRoleDef role =null;
		try {

			role = getWRoleDefByPK(roleId);

			HibernateUtil.borrar(role);

		} catch (HibernateException ex) {
			logger.error("WRoleDefDao: delete - Can't delete role definition record "+ 
					" <id = "+roleId+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("WRoleDefDao:  delete - Can't delete role definition record  "+ 
					" <id = "+roleId+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WRoleDefException ex1) {
			logger.error("WRoleDefDao: delete - Exception in deleting role rec "+ 
					" <id = "+roleId+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
			throw new WRoleDefException("WRoleDefDao: delete - Exception in deleting role rec "+ 
					" <id = "+roleId+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}

	public WRoleDef getWRoleDefByPK(Integer id) throws WRoleDefException {

		WRoleDef user = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			user = (WRoleDef) session.get(WRoleDef.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WRoleDefDao: getWRoleDefByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("WRoleDefDao: getWRoleDefByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return user;
	}
	
	
	public WRoleDef getWRoleDefByName(String name) throws WRoleDefException {

		WRoleDef  user = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			user = (WRoleDef) session.createCriteria(WRoleDef.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WRoleDefDao: getWRoleDefByName - can't obtain user name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("getWRoleDefByName;  can't obtain user name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return user;
	}

	
	public List<WRoleDef> getWRoleDefs() throws WRoleDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WRoleDef> lrole = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			lrole = session.createQuery("From WRoleDef order by name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WRoleDefDao: getWRoleDefs() - can't obtain user list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("WRoleDefDao: getWRoleDefs() - can't obtain user list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return lrole;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WRoleDefException {
		 
			List<WRoleDef> lrole = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lrole = session
						.createQuery("From WRoleDef order by name ")
						.list();
		
				if (lrole!=null) {
					
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
					
				
					
					for (WRoleDef wpd: lrole) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WRoleDefException(
						"Can't obtain Roles combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;
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
	
}
	