package org.beeblos.bpm.core.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepRoleException;
import org.beeblos.bpm.core.error.WUserRoleException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WUserRole;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.sp.common.util.HibernateUtil;



public class WUserRoleDao {
	
	private static final Log logger = LogFactory.getLog(WUserRoleDao.class.getName());
	
	public WUserRoleDao (){
		super();
	}
	
	public Integer add(WUserRole userRole) throws WUserRoleException {

		logger.debug("add() WUserRole - user/role: [" 
				+ ((userRole!=null&&userRole.getUser()!=null)?userRole.getUser().getId():"null") + ", " 
				+ ((userRole!=null&&userRole.getRole()!=null)?userRole.getRole().getId():"null") + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(userRole));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add userRole definition "
					+ " id user/role: " 
					+ ((userRole!=null&&userRole.getUser()!=null)?userRole.getUser().getId():"null") + ", " 
					+ ((userRole!=null&&userRole.getRole()!=null)?userRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserRoleException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add userRole definition "
					+ " id user/role: " 
					+ ((userRole!=null&&userRole.getUser()!=null)?userRole.getUser().getId():"null") + ", " 
					+ ((userRole!=null&&userRole.getRole()!=null)?userRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserRoleException(mess);

		}

	}

	public void update(WUserRole userRole) throws WUserRoleException {

		logger.debug("update() WUserRole - user/role: [" 
				+ ((userRole!=null&&userRole.getUser()!=null)?userRole.getUser().getId():"null") + ", " 
				+ ((userRole!=null&&userRole.getRole()!=null)?userRole.getRole().getId():"null") + "]");

		try {

			HibernateUtil.update(userRole);

		} catch (HibernateException ex) {
			String mess = "HibernateException: update - Can't update userRole definition "
					+ " id user/role: " 
					+ ((userRole!=null&&userRole.getUser()!=null)?userRole.getUser().getId():"null") + ", " 
					+ ((userRole!=null&&userRole.getRole()!=null)?userRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserRoleException(mess);

		} catch (Exception ex) {
			String mess = "Exception: update - Can't update userRole definition "
					+ " id user/role: " 
					+ ((userRole!=null&&userRole.getUser()!=null)?userRole.getUser().getId():"null") + ", " 
					+ ((userRole!=null&&userRole.getRole()!=null)?userRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserRoleException(mess);

		}

	}

	public void delete(WUserRole userRole) throws WUserRoleException {

		logger.debug("delete() WUserRole - user/role: [" 
				+ ((userRole!=null&&userRole.getUser()!=null)?userRole.getUser().getId():"null") + ", " 
				+ ((userRole!=null&&userRole.getRole()!=null)?userRole.getRole().getId():"null") + "]");

		try {

			userRole = getUserRoleByPK(userRole.getId());

			HibernateUtil.delete(userRole);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete userRole definition "
					+ " id user/role: " 
					+ ((userRole!=null&&userRole.getUser()!=null)?userRole.getUser().getId():"null") + ", " 
					+ ((userRole!=null&&userRole.getRole()!=null)?userRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserRoleException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete userRole definition "
					+ " id user/role: " 
					+ ((userRole!=null&&userRole.getUser()!=null)?userRole.getUser().getId():"null") + ", " 
					+ ((userRole!=null&&userRole.getRole()!=null)?userRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserRoleException(mess);

		}

	}

	public WUserRole getUserRoleByPK(Integer id) throws WUserRoleException {

		WUserRole userRole = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			userRole = (WUserRole) session.get(WUserRole.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getUserRoleByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WUserRoleException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getUserRoleByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WUserRoleException(mess);			

		}

		return userRole;
	}

	/**
	   * Returns the user list for a given Role id
	   * orderBy: id (user) or name
	   */
	@SuppressWarnings("unchecked")
	public List<WUserDef> getUserDefListByRole( Integer idRole, String orderBy ) throws WUserRoleException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WUserDef> users = null;
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String orderFilter = "user.name";
			if (orderBy != null && orderBy.equals("id")){
				orderFilter = "user.id";
			}

			users = session.createCriteria(WUserDef.class, "user")
					.createAlias("user.rolesRelated", "userRole", JoinType.LEFT_OUTER_JOIN)
					.createAlias("userRole.role", "role", JoinType.LEFT_OUTER_JOIN)
					.add( Restrictions.eq("role.id", idRole))
					.addOrder(Order.desc(orderFilter))
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
					.list();

			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWUserDefByRole - we can't obtain the users by role id = " + idRole
					+ ". " + e.getMessage() + " " + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WUserRoleException(mess);
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWUserDefByRole - we can't obtain the users by role id = " + idRole
					+ ". " + e.getMessage() + " " + (e.getCause()!=null?e.getCause():"")+ " "
					+ e.getClass();
			logger.error(mess);
			throw new WUserRoleException(mess);			
		}

		return users;
	}

	 /**
	   * Returns the ID user list for a given Role id
	   * orderBy: id (user) or name
	   */
	@SuppressWarnings("unchecked")
	public List<Integer> getUserDefIdsByRole( Integer idRole ) throws WUserRoleException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Integer> userIds = null;
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			userIds = session.createCriteria(WUserRole.class, "userRole")
					.createAlias("userRole.user", "user", JoinType.LEFT_OUTER_JOIN)
					.createAlias("userRole.role", "role", JoinType.LEFT_OUTER_JOIN)
					.add( Restrictions.eq("role.id", idRole))
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
					.setProjection(Property.forName("user.id"))
					.list();
					
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getUserDefIdsByRole - we can't obtain the users by role id = " + idRole
					+ ". " + e.getMessage() + " " + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WUserRoleException(mess);
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getUserDefIdsByRole - we can't obtain the users by role id = " + idRole
					+ ". " + e.getMessage() + " " + (e.getCause()!=null?e.getCause():"")+ " "
					+ e.getClass();
			logger.error(mess);
			throw new WUserRoleException(mess);			
		}

		return userIds;
	}

	 /**
	   * Returns the role list for a given user id
	   * orderBy: id (role) or name
	   */
	@SuppressWarnings("unchecked")
	public List<WRoleDef> getRoleDefListByUser( Integer idUser, String orderBy ) throws WUserRoleException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WRoleDef> roles = null;
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String orderFilter = "role.name";
			if (orderBy != null && orderBy.equals("id")){
				orderFilter = "role.id";
			}

			roles = session.createCriteria(WRoleDef.class, "role")
					.createAlias("role.usersRelated", "userRole", JoinType.LEFT_OUTER_JOIN)
					.createAlias("userRole.user", "user", JoinType.LEFT_OUTER_JOIN)
					.add( Restrictions.eq("user.id", idUser))
					.addOrder(Order.desc(orderFilter))
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
					.list();

			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getRoleDefListByUser - we can't obtain the roles by user id = " + idUser
					+ ". " + e.getMessage() + " " + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WUserRoleException(mess);
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getRoleDefListByUser - we can't obtain the roles by user id = " + idUser
					+ ". " + e.getMessage() + " " + (e.getCause()!=null?e.getCause():"")+ " "
					+ e.getClass();
			logger.error(mess);
			throw new WUserRoleException(mess);			
		}

		return roles;
	}

	 /**
	   * Returns the ID user list for a given Role id
	   * orderBy: id (user) or name
	   */
	@SuppressWarnings("unchecked")
	public List<Integer> getRoleDefIdsByUser( Integer idUser ) throws WUserRoleException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Integer> roleIds = null;
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			roleIds = session.createCriteria(WUserRole.class, "userRole")
					.createAlias("userRole.user", "user", JoinType.LEFT_OUTER_JOIN)
					.createAlias("userRole.role", "role", JoinType.LEFT_OUTER_JOIN)
					.add( Restrictions.eq("user.id", idUser))
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
					.setProjection(Property.forName("role.id"))
					.list();
					
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getRoleDefIdsByUser - we can't obtain the roles by user id = " + idUser
					+ ". " + e.getMessage() + " " + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WUserRoleException(mess);
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getRoleDefIdsByUser - we can't obtain the roles by user id = " + idUser
					+ ". " + e.getMessage() + " " + (e.getCause()!=null?e.getCause():"")+ " "
					+ e.getClass();
			logger.error(mess);
			throw new WUserRoleException(mess);			
		}

		return roleIds;
	}

	public boolean existsUserRole(Integer idUser, Integer idRole)throws WUserRoleException {

		Integer id = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			id = (Integer) session
						.createQuery("Select id From WUserRole Where user.id = :idUser And role.id = :idRole ")
						.setInteger("idUser", idUser)
						.setInteger("idRole", idRole)
						.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
			    tx.rollback(); 
			String mess = "HibernateException: existsUserRole - Can't check if the user role with ids user/role = " 
					+ ((idUser != null)?idUser:"") + ", "
					+ ((idRole != null)?idRole:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new WUserRoleException(mess);
		} catch (Exception ex){
			if (tx != null)
			    tx.rollback(); 
			String mess = "Exception: existsUserRole - Can't check if the user role with ids user/role = " 
					+ ((idUser != null)?idUser:"") + ", "
					+ ((idRole != null)?idRole:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"") + " " + ex.getClass();
			logger.error(mess);
			throw new WUserRoleException(mess);
		}

		if (id == null){
			return false;
		}
		return true;
	}

}
	