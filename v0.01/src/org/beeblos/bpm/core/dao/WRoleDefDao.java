package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.error.WUserRoleException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WUserDef;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;



public class WRoleDefDao {
	
	private static final Log logger = LogFactory.getLog(WRoleDefDao.class.getName());
	
	public WRoleDefDao (){
		super();
	}
	
	public Integer add(WRoleDef role) throws WRoleDefException {
		
		logger.debug("add() WRoleDef - Name: ["+role.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(role));

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

			HibernateUtil.update(role);


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

			HibernateUtil.delete(role);

		} catch (HibernateException ex) {
			logger.error("WRoleDefDao: delete - Can't delete role definition record "+ role.getName() +
					" <id = "+role.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WRoleDefException("WRoleDefDao:  delete - Can't delete role definition record  "+ role.getName() +
					" <id = "+role.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WRoleDefException e) {
			logger.error("WRoleDefDao: delete - Exception in deleting role rec "+ role.getName() +
					" <id = "+role.getId()+ "> no esta almacenada \n"+" - "+e.getMessage()+"\n"+e.getCause() );
			throw new WRoleDefException("WRoleDefDao: delete - Exception in deleting role rec "+ role.getName() +
					" <id = "+role.getId()+ "> not stored \n"+" - "+e.getMessage()+"\n"+e.getCause() );

		} 

	}
	
	public void delete(Integer roleId) throws WRoleDefException {
		logger.debug("delete() WRoleDef - Name: ["+roleId+"]");
		
		WRoleDef role =null;
		try {

			role = getWRoleDefByPK(roleId);

			HibernateUtil.delete(role);

		} catch (HibernateException ex) {
			
			String mess = "WRoleDefDao: HibernateException: Error deleting role rec " 
					+ " <id = "+roleId+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause(); 
			logger.error( mess );
			throw new WRoleDefException(mess );

		} catch (WRoleDefException e) {

			String mess = "WRoleDefDao: WRoleDefException: Error deleting role rec " 
					+ " <id = "+roleId+ "> \n"+" - "+e.getMessage()+"\n"+e.getCause();
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
		
				tx.commit();

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
	
	public List<WRoleDef> getWRoleDefListByUser( Integer idUser ) throws WUserDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;

		List<WRoleDef> roles = null;
		
		String query="SELECT * " + getSelectPhrase();
		String filter = getFilter(idUser);
		String order = " ORDER BY r.name ";

		query += filter+order;
		
		System.out.println("QUERY: "+query);
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			q = session
					.createSQLQuery(query)
					.addEntity("WRoleDef", WRoleDef.class);
			
			roles = q.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WUserDefDao: getWUserDefByRole() - can't obtain user list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WUserDefException("WUserDefDao: getWUserDefByRole() - can't obtain user list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return roles;
	}

	private String getSelectPhrase() {
		String query="FROM w_role_def r ";
		query +="left join w_user_role wur on r.id=wur.id_role ";
		return query;
	}
	
	private String getFilter(Integer idUser) {
		
		String filter = " WHERE wur.id_user = "+ idUser;
		
		return filter;
	}

	
}
	