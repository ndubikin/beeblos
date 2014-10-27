package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WSystemException;
import org.beeblos.bpm.core.model.WSystem;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;

public class WSystemDao {
	private static final Log logger = LogFactory.getLog(WSystemDao.class.getName());

	public WSystemDao (){

	}

	public Integer add(WSystem system) throws WSystemException {

		logger.debug("add() WSystem - Name: ["+system.getName()+"]");

		try {

			return Integer.valueOf(HibernateUtil.save(system));

		} catch (HibernateException ex) {
			logger.error("WSystemDao: add - Can't store process definition record "+ 
					system.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WSystemException("WSystemDao: add - Can't store process definition record "+ 
					system.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}


	public void update(WSystem system) throws WSystemException {

		logger.debug("update() WSystem < id = "+system.getId()+">");

		try {

			HibernateUtil.update(system);


		} catch (HibernateException ex) {
			logger.error("WSystemDao: update - Can't update process definition record "+ 
					system.getName()  +
					" - id = "+system.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WSystemException("WSystemDao: update - Can't update process definition record "+ 
					system.getName()  +
					" - id = "+system.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}


	public void delete(WSystem system) throws WSystemException {

		logger.debug("delete() WSystem - Name: ["+system.getName()+"]");

		try {

			HibernateUtil.delete(system);

		} catch (HibernateException ex) {
			logger.error("WSystemDao: delete - Can't delete proccess definition record "+ system.getName() +
					" <id = "+system.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WSystemException("WSystemDao:  delete - Can't delete proccess definition record  "+ system.getName() +
					" <id = "+system.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} 

	}

	public WSystem getWSystemByPK(Integer id) throws WSystemException {

		WSystem system = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			system = (WSystem) session.get(WSystem.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WSystemDao: getWSystemByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WSystemException("WSystemDao: getWSystemByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return system;
	}


	public WSystem getWSystemByName(String name) throws WSystemException {

		WSystem system = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			system = (WSystem) session.createCriteria(WSystem.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WSystemDao: getWSystemByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WSystemException("getWSystemByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return system;
	}


	public List<WSystem> getWSystemList() throws WSystemException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WSystem> system = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			system = session.createQuery("From WSystem order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WSystemDao: getWSystemList() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WSystemException("WSystemDao: getWSystemList() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return system;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String separationLine )
					throws WSystemException {

		List<WSystem> ldt = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			ldt = session
					.createQuery("From WSystem Order By name ")
					.list();

			if (ldt!=null) {

				// inserta los extras
				if ( firstLineText!=null && !"".equals(firstLineText) ) {
					if ( !firstLineText.equals("WHITESPACE") ) {
						retorno.add(new StringPair(null,firstLineText));  // deja la primera línea con lo q venga
					} else {
						retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
					}
				}

				if ( separationLine!=null && !"".equals(separationLine) ) {
					if ( !separationLine.equals("WHITESPACE") ) {
						retorno.add(new StringPair(null,separationLine));  // deja la separación línea con lo q venga
					} else {
						retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
					}
				}



				for (WSystem dt: ldt) {
					retorno.add(new StringPair(dt.getId(),dt.getName()));
				}
			} else {
				// nes  - si el select devuelve null entonces devuelvo null
				retorno=null;
			}

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new WSystemException(
					"Can't obtain WSystem combo list "
							+ex.getMessage()+"\n"+ex.getCause());
		} catch (Exception e) {}

		return retorno;


	}

}
