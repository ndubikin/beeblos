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
	
	private static final Log logger = LogFactory.getLog(WRoleDefDao.class.getName());
	
	public WRoleDefDao (){
		super();
	}
	
	public Integer add(WRoleDef role) throws WRoleDefException {
		
		logger.debug("add() WRoleDef - Name: ["+role.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(role));

		} catch (HibernateException ex) {
			logger.error("WRoleDefDao: add - Can't store role definition record "+ 
					role.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("WRoleDefDao: add - Can't store role definition record "+ 
					role.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WRoleDef role) throws WRoleDefException {
		
		logger.debug("update() WRoleDef < id = "+role.getId()+">");
		
		try {

			HibernateUtil.actualizar(role);


		} catch (HibernateException ex) {
			logger.error("WRoleDefDao: update - Can't update role definition record "+ 
					role.getName()  +
					" - id = "+role.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WRoleDefException("WRoleDefDao: update - Can't update role definition record "+ 
					role.getName()  +
					" - id = "+role.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

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
			
			String mess = "WRoleDefDao: HibernateException: Error deleting role rec " 
					+ " <id = "+roleId+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause(); 
			logger.error( mess );
			throw new WRoleDefException(mess );

		} catch (WRoleDefException ex1) {

			String mess = "WRoleDefDao: WRoleDefException: Error deleting role rec " 
					+ " <id = "+roleId+ "> \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause();
			logger.error( mess );
			throw new WRoleDefException(mess );

		} catch (Exception e) {
		
			String mess = "WRoleDefDao: Exception: Error deleting role rec " 
							+ " <id = "+roleId+ "> \n"+" - "+e.getMessage()+"\n"+e.getCause()
							+ " - "+ e.getClass(); 
			logger.error( mess );
			throw new WRoleDefException(mess );
		}
	}

	public WRoleDef getWRoleDefByPK(Integer id) throws WRoleDefException {

		WRoleDef role = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			role = (WRoleDef) session.get(WRoleDef.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WRoleDefDao: getWRoleDefByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("WRoleDefDao: getWRoleDefByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return role;
	}
	
	
	public WRoleDef getWRoleDefByName(String name) throws WRoleDefException {

		WRoleDef  role = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			role = (WRoleDef) session.createCriteria(WRoleDef.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WRoleDefDao: getWRoleDefByName - can't obtain role name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("getWRoleDefByName;  can't obtain role name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return role;
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
			logger.warn("WRoleDefDao: getWRoleDefs() - can't obtain role list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("WRoleDefDao: getWRoleDefs() - can't obtain role list: "
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
							retorno.add(new StringPair(null,blank));  // white line (separation)
						} else {
							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
						}
					}
					
				
					
					for (WRoleDef wpd: lrole) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  -if select returns null then I return null
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
	   * returns the role list for a given Role
	   * orderBy: id (role) or name
	   */
	public List<WUserDef> getWUserDefByRole( Integer idRole, String orderBy ) throws WUserDefException {
		
		return new WUserRoleDao().getWUserDefByRole(idRole, orderBy);

	}

	 /**
	   * returns the ID role list for a given Role
	   * orderBy: id (role) or name
	   */
	public List<Integer> getWUserDefIdByRole( Integer idRole ) throws WUserDefException {

		return new WUserRoleDao().getWUserDefIdByRole(idRole);
	}
	
}
	