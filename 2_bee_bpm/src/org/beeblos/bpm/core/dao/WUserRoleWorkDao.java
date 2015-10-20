package org.beeblos.bpm.core.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WUserRoleWorkException;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WUserRoleWork;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.sp.common.util.HibernateUtil;


/**
 * DAO for runtime roles mgmt
 * 
 * Nota: esta clase utiliza Named Queries ...
 * 
 * 
 * @author nes 20141215
 *
 */
public class WUserRoleWorkDao {
	
	private static final Log logger = LogFactory.getLog(WUserRoleWorkDao.class.getName());
	
	public WUserRoleWorkDao (){
		super();
	}
	
	public Integer add(WUserRoleWork userRoleWork) throws WUserRoleWorkException {
		logger.debug("add() WUserRoleWork - user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(userRoleWork));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add userRoleWork definition "
					+ " id user/role/processWork: " 
					+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
					+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
					+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add userRoleWork definition "
					+ " id user/role/processWork: " 
					+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
					+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
					+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		}

	}

	/**
	 * no tendría mucho sentido hacer un update sobre un WURPW ... solo altas o bajas ...
	 * @param userRoleWork
	 * @throws WUserRoleWorkException
	 */
	@Deprecated
	public void update(WUserRoleWork userRoleWork) throws WUserRoleWorkException {
		// por eso dejo este logger info ....
		logger.info("update() WUserRoleWork - user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") + "]");

		try {

			HibernateUtil.update(userRoleWork);

		} catch (HibernateException ex) {
			String mess = "HibernateException: update - Can't update userRoleWork definition "
					+ " user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		} catch (Exception ex) {
			String mess = "Exception: update - Can't update userRoleWork definition "
					+ " user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null")
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		}

	}

	/**
	 * delete a given runtime wUserRoleWork
	 * @param userRoleWork
	 * @throws WUserRoleWorkException
	 */
	public void delete(WUserRoleWork userRoleWork) throws WUserRoleWorkException {

		logger.debug("delete() WUserRoleWork - user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") + "]");

		try {

			userRoleWork = getUserRoleWorkByPK(userRoleWork.getId());

			HibernateUtil.delete(userRoleWork);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete userRoleWork definition "
					+ " user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null")
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete userRoleWork definition "
					+ " user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		}

	}
	
	/**
	 * delete runtime WUserRoleWork related with given idProcessWork
	 * nes 20150430
	 * 
	 * @param idProcessWork
	 * @return qtyDeletedItems
	 * @throws WUserRoleWorkException 
	 */
	public Integer deleteByProcessWork(Integer idProcessWork) 
			throws WUserRoleWorkException{
		logger.debug(">>>getRuntimeUserRoleByProcessWork...");
		
		if (idProcessWork==null) throw new WUserRoleWorkException("deleteRuntimeUserRoleByProcessWork: id process work arrives with null value ..."); 
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Integer qtyDeletedItems=null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			Query query = session
					.createQuery("Delete WUserRoleWork where idProcessWork= :idProcessWork")
					.setParameter("idProcessWork", idProcessWork);

			qtyDeletedItems = (Integer) query.executeUpdate();


			tx.commit();
			


		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: error removing WUserRoleWork related with given process work id: "
					+" "+(idProcessWork!=null?idProcessWork:"null")+" "
					+ex.getMessage()+(ex.getCause()!=null?". "+ex.getCause():""); 
			logger.error( mess );
			throw new WUserRoleWorkException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: error removing WUserRoleWork related with given process work id: "
					+" "+(idProcessWork!=null?idProcessWork:"null")+" "
					+ex.getMessage()+(ex.getCause()!=null?". "+ex.getCause():"")
					+ex.getClass(); 
			logger.error( mess );
			throw new WUserRoleWorkException( mess );			

		}
		
		return qtyDeletedItems;		

	}	

	/**
	 * returns a w-user-role-processWork
	 * 
	 * @param id
	 * @return
	 * @throws WUserRoleWorkException
	 */
	public WUserRoleWork getUserRoleWorkByPK(Integer id) throws WUserRoleWorkException {

		WUserRoleWork userRoleWork = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			userRoleWork = (WUserRoleWork) session.get(WUserRoleWork.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getUserRoleWorkByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WUserRoleWorkException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getUserRoleWorkByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WUserRoleWorkException(mess);			

		}

		return userRoleWork;
	}


	/**
	 * returns a list of wUserDef related with a runtime role for given idRole and idProcessWork
	 * nes 20141224
	 * 
	 * @param idRole
	 * @param idProcessWork
	 * @return
	 * @throws WUserRoleWorkException 
	 */
	public List<WUserDef> getUserDefListByRole(Integer idRole, Integer idProcessWork) 
			throws WUserRoleWorkException{
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WUserDef> retorno=null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			Query query = session.getNamedQuery("findUsersForRuntimeRole")
					.setInteger("idRole", idRole)
					.setInteger("idProcessWork", idProcessWork);
			
			retorno = query.list();

			tx.commit();
			


		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: error recovering user def related with a runtime role and process work... " +
					ex.getMessage()+" "+(ex.getCause()!=null?"."+ex.getCause():""); 
			logger.error( mess );
			throw new WUserRoleWorkException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: error recovering user def related with a runtime role and process work... " +
					ex.getMessage()+" "+(ex.getCause()!=null?"."+ex.getCause():"")+" "
					+ex.getClass(); 
			logger.error( mess );
			throw new WUserRoleWorkException( mess );			

		}
		
		return retorno;		

	}
	
	/**
	 * returns a list of WUserRoleWork related with given idProcessWork
	 * nes 20150430
	 * 
	 * @param idProcessWork
	 * @return
	 * @throws WUserRoleWorkException 
	 */
	public List<WUserRoleWork> getByProcessWork(Integer idProcessWork) 
			throws WUserRoleWorkException{
		logger.debug(">>>getRuntimeUserRoleByProcessWork...");
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WUserRoleWork> retorno=null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			retorno = session
					.createQuery("From WUserRoleWork where idProcessWork= :idProcessWork")
					.setParameter("idProcessWork", idProcessWork)
					.list();


			tx.commit();
			


		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: error recovering WUserRoleWork related with given process work id: "
					+" "+(idProcessWork!=null?idProcessWork:"null")+" "
					+ex.getMessage()+(ex.getCause()!=null?". "+ex.getCause():""); 
			logger.error( mess );
			throw new WUserRoleWorkException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: error recovering WUserRoleWork related with given process work id: "
					+" "+(idProcessWork!=null?idProcessWork:"null")+" "
					+ex.getMessage()+(ex.getCause()!=null?". "+ex.getCause():"")
					+ex.getClass(); 
			logger.error( mess );
			throw new WUserRoleWorkException( mess );			

		}
		
		return retorno;		

	}
	
	/**
	 * returns yes or not if given process work has defined runtime users...
	 * nes 20150430
	 * 
	 * @param idProcessWork
	 * @return
	 * @throws WUserRoleWorkException 
	 */
	public boolean hasRuntimeUsers(Integer idProcessWork) 
			throws WUserRoleWorkException{
		logger.debug(">>> hasRuntimeUsers...");
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Integer retorno=null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			retorno = (Integer) session
					.createQuery("select count() From WUserRoleWork where idProcessWork= :idProcessWork")
					.setParameter("idProcessWork", idProcessWork)
					.uniqueResult();


			tx.commit();
			


		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: error recovering WUserRoleWork related with given process work id: "
					+" "+(idProcessWork!=null?idProcessWork:"null")+" "
					+ex.getMessage()+(ex.getCause()!=null?". "+ex.getCause():""); 
			logger.error( mess );
			throw new WUserRoleWorkException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: error recovering WUserRoleWork related with given process work id: "
					+" "+(idProcessWork!=null?idProcessWork:"null")+" "
					+ex.getMessage()+(ex.getCause()!=null?". "+ex.getCause():"")
					+ex.getClass(); 
			logger.error( mess );
			throw new WUserRoleWorkException( mess );			

		}
		
		return (retorno!=null && retorno>0?true:false);		

	}	

}
	