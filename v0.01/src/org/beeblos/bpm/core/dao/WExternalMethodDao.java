package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WExternalMethodException;
import org.beeblos.bpm.core.model.WExternalMethod;
import org.hibernate.HibernateException;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;

/**
 * External method dao.
 * 
 * last mod: nes 20141016 - agregados catch exception que faltaban y ordenados mensajes a la nueva
 * modalida.
 * 
 * @author nestor
 *
 */
public class WExternalMethodDao {

	private static final Log logger = LogFactory.getLog(WExternalMethodDao.class.getName());

	public WExternalMethodDao() {

	}

	public Integer add(WExternalMethod externalMethod) throws WExternalMethodException {

		logger.debug("add() WExternalMethod - Name: [" + externalMethod.getClassname() + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(externalMethod));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add external method definition "
					+ (externalMethod!=null && externalMethod.getClass()!=null?externalMethod.getClassname():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WExternalMethodException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add external method definition "
					+ (externalMethod!=null && externalMethod.getClass()!=null?externalMethod.getClassname():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WExternalMethodException(mess);

		}

	}

	public void update(WExternalMethod externalMethod) throws WExternalMethodException {

		logger.debug("update() WExternalMethod < id = " + externalMethod.getId() + ">");

		try {

			HibernateUtil.update(externalMethod);

		} catch (HibernateException ex) {
			String mess = "HibernateException: update - Can't update external method definition "
					+ (externalMethod!=null && externalMethod.getClass()!=null?externalMethod.getClassname():"null") 
					+ " id:" + (externalMethod!=null && externalMethod.getId()!=null? externalMethod.getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WExternalMethodException(mess);

		} catch (Exception ex) {
			String mess = "Exception: update - Can't update external method definition "
					+ (externalMethod!=null && externalMethod.getClass()!=null?externalMethod.getClassname():"null") 
					+ " id:" + (externalMethod!=null && externalMethod.getId()!=null? externalMethod.getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WExternalMethodException(mess);

		}

	}

	public void delete(WExternalMethod externalMethod) throws WExternalMethodException {

		logger.debug("delete() WExternalMethod - Name: [" + externalMethod.getClassname() + "]");

		try {

			externalMethod = getExternalMethodByPK(externalMethod.getId());

			HibernateUtil.delete(externalMethod);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete external method definition "
					+ (externalMethod!=null && externalMethod.getClass()!=null?externalMethod.getClassname():"null") 
					+ " id:" + (externalMethod!=null && externalMethod.getId()!=null? externalMethod.getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WExternalMethodException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete external method definition "
					+ (externalMethod!=null && externalMethod.getClass()!=null?externalMethod.getClassname():"null") 
					+ " id:" + (externalMethod!=null && externalMethod.getId()!=null? externalMethod.getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WExternalMethodException(mess);

		}

	}

	public WExternalMethod getExternalMethodByPK(Integer id) throws WExternalMethodException {

		WExternalMethod externalMethod = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			externalMethod = (WExternalMethod) session.get(WExternalMethod.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWExternalMethodByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WExternalMethodException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWExternalMethodByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WExternalMethodException(mess);			

		}

		return externalMethod;
	}

	public List<WExternalMethod> getWExternalMethods() throws WExternalMethodException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WExternalMethod> externalMethods = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			externalMethods = session.createQuery("From WExternalMethod Order By classname ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWExternalMethodByPK - can't obtain externalMethod list - " 
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WExternalMethodException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWExternalMethodByPK - can't obtain externalMethod list - " 
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WExternalMethodException(mess);	


		}

		return externalMethods;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(String textoPrimeraLinea, String separacion)
			throws WExternalMethodException {

		List<WExternalMethod> externalMethodList = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			externalMethodList = session.createQuery("From WExternalMethod Order By classname ").list();

			tx.commit();

			if (externalMethodList != null) {

				// inserta los extras
				if (textoPrimeraLinea != null && !"".equals(textoPrimeraLinea)) {
					if (!textoPrimeraLinea.equals("WHITESPACE")) {
						retorno.add(new StringPair(null, textoPrimeraLinea)); 
					} else {
						retorno.add(new StringPair(null, " ")); 
					}
				}

				if (separacion != null && !"".equals(separacion)) {
					if (!separacion.equals("WHITESPACE")) {
						retorno.add(new StringPair(null, separacion)); 
					} else {
						retorno.add(new StringPair(null, " ")); 
					}
				}

				for (WExternalMethod wphm : externalMethodList) {
					retorno.add(new StringPair(wphm.getId(), wphm.getClassname() + "-" + wphm.getMethodname()));
				}
			} else {
				// nes - si el select devuelve null entonces devuelvo null
				retorno = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWExternalMethodByPK - can't obtain externalMethod combo list - " 
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WExternalMethodException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWExternalMethodByPK - can't obtain externalMethod combo list - " 
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WExternalMethodException(mess);
		}

		return retorno;

	}

}