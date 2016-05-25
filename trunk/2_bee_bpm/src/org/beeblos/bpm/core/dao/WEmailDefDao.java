package org.beeblos.bpm.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WEmailDefException;
import org.beeblos.bpm.core.model.WEmailDef;
import org.hibernate.HibernateException;

import com.sp.common.util.HibernateUtil;

public class WEmailDefDao implements java.io.Serializable {

	private static final long serialVersionUID = 4568309960100408427L;
	
	private static final Log logger = LogFactory.getLog(WEmailDefDao.class.getName());
	
	public WEmailDefDao(){
		
	}
	
	public Integer add(WEmailDef instance) throws WEmailDefException {
		
		logger.debug("add() WEmailDef");
		
		try {

			return Integer.valueOf(HibernateUtil.save(instance));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add()"
					+ (ex.getMessage()!=null?": " + ex.getMessage():"") 
					+ (ex.getCause()!=null?". " + ex.getCause():"");
			logger.error(mess);
			throw new WEmailDefException(mess);
		} catch (Exception ex) {
			String mess = "Exception: add()"
					+ (ex.getMessage()!=null?": " + ex.getMessage():"") 
					+ (ex.getCause()!=null?". " + ex.getCause():"") + ". " + ex.getClass();
			logger.error(mess);
			throw new WEmailDefException(mess);
		}

	}
	
	
	public void update(WEmailDef instance) throws WEmailDefException {
		
		logger.debug("update() WEmailDef < id = "+instance.getId()+">");
		
		try {

			HibernateUtil.update(instance);


		} catch (HibernateException ex) {
			String mess = "HibernateException: update()"
					+ (ex.getMessage()!=null?": " + ex.getMessage():"") 
					+ (ex.getCause()!=null?". " + ex.getCause():"");
			logger.error(mess);
			throw new WEmailDefException(mess);
		} catch (Exception ex) {
			String mess = "Exception: update()"
					+ (ex.getMessage()!=null?": " + ex.getMessage():"") 
					+ (ex.getCause()!=null?". " + ex.getCause():"") + ". " + ex.getClass();
			logger.error(mess);
			throw new WEmailDefException(mess);
		}
					
	}
	
	
	public void delete(WEmailDef instance) throws WEmailDefException {

		logger.debug("delete() WEmailDef - Id: ["+instance.getId()+"]");
		
		try {

			instance = getByPK(instance.getId());

			HibernateUtil.delete(instance);

		} catch (HibernateException ex) {
			String mess = "HibernateException: update()"
					+ (ex.getMessage()!=null?": " + ex.getMessage():"") 
					+ (ex.getCause()!=null?". " + ex.getCause():"");
			logger.error(mess);
			throw new WEmailDefException(mess);
		} catch (Exception ex) {
			String mess = "Exception: update()"
					+ (ex.getMessage()!=null?": " + ex.getMessage():"") 
					+ (ex.getCause()!=null?". " + ex.getCause():"") + ". " + ex.getClass();
			logger.error(mess);
			throw new WEmailDefException(mess);
		} 

	}

	public WEmailDef getByPK(Integer id) throws WEmailDefException {

		WEmailDef instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			instance = (WEmailDef) session.get(WEmailDef.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null) tx.rollback();
			String mess = "HibernateException: getByPK()"
					+ (ex.getMessage()!=null?": " + ex.getMessage():"") 
					+ (ex.getCause()!=null?". " + ex.getCause():"");
			logger.error(mess);
			throw new WEmailDefException(mess);
		} catch (Exception ex) {
			if (tx != null) tx.rollback();
			String mess = "Exception: getByPK()"
					+ (ex.getMessage()!=null?": " + ex.getMessage():"") 
					+ (ex.getCause()!=null?". " + ex.getCause():"") + ". " + ex.getClass();
			logger.error(mess);
			throw new WEmailDefException(mess);
		}

		return instance;
	}
	
}
