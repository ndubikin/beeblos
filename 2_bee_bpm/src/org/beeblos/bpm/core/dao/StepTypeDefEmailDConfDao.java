package org.beeblos.bpm.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.StepTypeDefEmailDConfException;
import org.beeblos.bpm.core.model.bpmn.StepTypeDefEmailDConf;
import org.hibernate.HibernateException;

import com.sp.common.util.HibernateUtil;

/**
 * RequestType role dao.
 * 
 * @author dml 20141031
 *
 */
public class StepTypeDefEmailDConfDao {

	private static final Log logger = LogFactory.getLog(StepTypeDefEmailDConfDao.class.getName());

	public StepTypeDefEmailDConfDao() {

	}

	public Integer add(StepTypeDefEmailDConf instance) throws StepTypeDefEmailDConfException {

		logger.debug("add() StepTypeDefEmailDConf - emailDConf/stepTypeDef: [" 
				+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
				+ instance.getStepTypeDefId() + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(instance));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add instance definition "
					+ " id emailDConf/stepTypeDef: " 
					+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
					+ instance.getStepTypeDefId()
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add instance definition "
					+ " id emailDConf/stepTypeDef: " 
					+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
					+ instance.getStepTypeDefId() 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);

		}

	}

	public void delete(StepTypeDefEmailDConf instance) throws StepTypeDefEmailDConfException {

		logger.debug("delete() StepTypeDefEmailDConf - emailDConf/stepTypeDef: [" 
				+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
				+ instance.getStepTypeDefId() + "]");

		try {

			instance = getStepTypeDefEmailDConfByPK(instance.getId());

			HibernateUtil.delete(instance);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete instance definition "
					+ " id emailDConf/stepTypeDef: " 
					+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
					+ instance.getStepTypeDefId() 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete instance definition "
					+ " id emailDConf/stepTypeDef: " 
					+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
					+ instance.getStepTypeDefId() 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);

		}

	}

	public StepTypeDefEmailDConf getStepTypeDefEmailDConfByPK(Integer id) throws StepTypeDefEmailDConfException {

		StepTypeDefEmailDConf instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			instance = (StepTypeDefEmailDConf) session.get(StepTypeDefEmailDConf.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getStepTypeDefEmailDConfByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new StepTypeDefEmailDConfException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getStepTypeDefEmailDConfByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new StepTypeDefEmailDConfException(mess);			

		}

		return instance;
	}

	public boolean existsRelation(Integer idStepTypeDef, Integer idEmailDConf)throws StepTypeDefEmailDConfException {

		Integer id = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			id = (Integer) session
						.createQuery("Select id From StepTypeDefEmailDConf Where emailDConf.id = :idEmailDConf And stepTypeDefId = :idStepTypeDef ")
						.setInteger("idEmailDConf", idEmailDConf)
						.setInteger("idStepTypeDef", idStepTypeDef)
						.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
			    tx.rollback(); 
			String mess = "HibernateException: existsRelation - Can't check if the relation with ids emailDConf/stepTypeDef = " 
					+ ((idEmailDConf != null)?idEmailDConf:"") + ", "
					+ ((idStepTypeDef != null)?idStepTypeDef:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);
		} catch (Exception ex){
			if (tx != null)
			    tx.rollback(); 
			String mess = "Exception: existsRelation - Can't check if the relation with ids emailDConf/stepTypeDef = " 
					+ ((idEmailDConf != null)?idEmailDConf:"") + ", "
					+ ((idStepTypeDef != null)?idStepTypeDef:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"") + " " + ex.getClass();
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);
		}

		if (id == null){
			return false;
		}
		return true;
	}
	

}