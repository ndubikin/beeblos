package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WTrackWorkException;
import org.beeblos.bpm.core.model.WTrackWork;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;



public class WTrackWorkDao {

	private static final Log logger = LogFactory.getLog(WTrackWorkDao.class.getName());

	public WTrackWorkDao() {

	}

	public Integer add(WTrackWork trackw) throws WTrackWorkException {

		logger.debug("add() WTrackWork - Name: [" + trackw.getIdObjectType()
				+ " " + trackw.getIdObject() + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(trackw));

		} catch (HibernateException ex) {
			logger.error("WTrackWorkDao: add - Can't store trackw definition record "
					+ trackw.getIdObjectType()
					+ " "
					+ trackw.getIdObject()
					+ " - " + ex.getMessage() + "\n" + ex.getCause());
			throw new WTrackWorkException(
					"WTrackWorkDao: add - Can't store trackw definition record "
							+ trackw.getIdObjectType() + " "
							+ trackw.getIdObject() + " - " + ex.getMessage()
							+ "\n" + ex.getCause());

		}

	}

	public void update(WTrackWork trackw) throws WTrackWorkException {

		logger.debug("update() WTrackWork < id = " + trackw.getId() + ">");

		try {

			HibernateUtil.update(trackw);

		} catch (HibernateException ex) {
			logger.error("WTrackWorkDao: update - Can't update trackw definition record "
					+ trackw.getIdObjectType()
					+ " "
					+ trackw.getIdObject()
					+ " - id = "
					+ trackw.getId()
					+ "\n - "
					+ ex.getMessage()
					+ "\n" + ex.getCause());
			throw new WTrackWorkException(
					"WTrackWorkDao: update - Can't update trackw definition record "
							+ trackw.getIdObjectType() + " "
							+ trackw.getIdObject() + " - id = "
							+ trackw.getId() + "\n - " + ex.getMessage() + "\n"
							+ ex.getCause());

		}

	}

	public void delete(WTrackWork trackw) throws WTrackWorkException {

		logger.debug("delete() WTrackWork - Name: [" + trackw.getIdObjectType()
				+ " " + trackw.getIdObject() + "]");

		try {

			trackw = getWTrackWorkByPK(trackw.getId());

			HibernateUtil.delete(trackw);

		} catch (HibernateException ex) {
			logger.error("WTrackWorkDao: delete - Can't delete proccess definition record "
					+ trackw.getIdObjectType()
					+ " "
					+ trackw.getIdObject()
					+ " <id = "
					+ trackw.getId()
					+ "> \n"
					+ " - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WTrackWorkException(
					"WTrackWorkDao:  delete - Can't delete proccess definition record  "
							+ trackw.getIdObjectType() + " "
							+ trackw.getIdObject() + " <id = " + trackw.getId()
							+ "> \n" + " - " + ex.getMessage() + "\n"
							+ ex.getCause());

		} catch (WTrackWorkException e) {
			logger.error("WTrackWorkDao: delete - Exception in deleting trackw rec "
					+ trackw.getIdObjectType()
					+ " "
					+ trackw.getIdObject()
					+ " <id = "
					+ trackw.getId()
					+ "> no esta almacenada \n"
					+ " - " + e.getMessage() + "\n" + e.getCause());
			throw new WTrackWorkException(
					"WTrackWorkDao: delete - Exception in deleting trackw rec "
							+ trackw.getIdObjectType() + " "
							+ trackw.getIdObject() + " <id = " + trackw.getId()
							+ "> not stored \n" + " - " + e.getMessage()
							+ "\n" + e.getCause());

		}

	}

	public WTrackWork getWTrackWorkByPK(Integer id) throws WTrackWorkException {

		WTrackWork trackw = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			trackw = (WTrackWork) session.get(WTrackWork.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WTrackWorkDao: getWTrackWorkByPK - we can't obtain the required id = "
					+ id
					+ "]  almacenada - \n"
					+ ex.getMessage()
					+ "\n"
					+ ex.getCause());
			throw new WTrackWorkException(
					"WTrackWorkDao: getWTrackWorkByPK - we can't obtain the required id : "
							+ id + " - " + ex.getMessage() + "\n"
							+ ex.getCause());

		}

		return trackw;
	}

	public WTrackWork getWTrackWorkByName(String name)
			throws WTrackWorkException {

		WTrackWork trackw = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			trackw = (WTrackWork) session.createCriteria(WTrackWork.class)
					.add(Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WTrackWorkDao: getWTrackWorkByName - can't obtain trackw name = "
					+ name
					+ "]  almacenada - \n"
					+ ex.getMessage()
					+ "\n"
					+ ex.getCause());
			throw new WTrackWorkException(
					"getWTrackWorkByName;  can't obtain trackw name: " + name
							+ " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return trackw;
	}

	@SuppressWarnings("unchecked")
	public List<WTrackWork> getWTrackWorks() throws WTrackWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WTrackWork> trackws = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			trackws = session.createQuery("From WTrackWork").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WTrackWorkDao: getWTrackWorks() - can't obtain trackw list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WTrackWorkException(
					"WTrackWorkDao: getWTrackWorks() - can't obtain trackw list: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		return trackws;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(String textoPrimeraLinea,
			String separacion) throws WTrackWorkException {

		List<WTrackWork> ltw = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			ltw = session.createQuery("From WTrackWork order by name ").list();

			tx.commit();

			if (ltw != null) {

				// inserta los extras
				if (textoPrimeraLinea != null && !"".equals(textoPrimeraLinea)) {
					if (!textoPrimeraLinea.equals("WHITESPACE")) {
						retorno.add(new StringPair(null, textoPrimeraLinea)); // deja
																				// la
																				// primera
																				// línea
																				// con
																				// lo
																				// q
																				// venga
					} else {
						retorno.add(new StringPair(null, " ")); // deja la
																// primera línea
																// en blanco ...
					}
				}

				if (separacion != null && !"".equals(separacion)) {
					if (!separacion.equals("WHITESPACE")) {
						retorno.add(new StringPair(null, separacion)); // deja
																		// la
																		// separación
																		// línea
																		// con
																		// lo q
																		// venga
					} else {
						retorno.add(new StringPair(null, " ")); // deja la
																// separacion
																// con linea en
																// blanco ...
					}
				}

				for (WTrackWork sw : ltw) {
					retorno.add(new StringPair(sw.getId(), sw.getIdObject()
							+ "-" + sw.getIdObjectType().trim()));
				}
			} else {
				// nes - si el select devuelve null entonces devuelvo null
				retorno = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new WTrackWorkException(
					"Can't obtain WTrackWorks combo list " + ex.getMessage()
							+ "\n" + ex.getCause());
		} catch (Exception e) {
		}

		return retorno;

	}

	@SuppressWarnings("unchecked")
	public List<WTrackWork> getTrackListByIdObject(Integer idObject,
			String idObjectType, Integer currentUser)
			throws WTrackWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WTrackWork> trackws = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			trackws = session
					.createQuery(
							"From WTrackWork where idObject=? and idObjectType=? order by process.id ")
					.setParameter(0, idObject).setParameter(1, idObjectType)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message = "WTrackWorkDao: 005 getWTrackWorks() - can't obtain trackw list - "
					+ ex.getMessage() + "\n" + ex.getCause();
			logger.warn(message);
			throw new WTrackWorkException(message);

		}

		return trackws;
	}

	@SuppressWarnings("unchecked")
	public List<WTrackWork> getTrackListByProcess ( Integer idProcess ) 
	throws WTrackWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WTrackWork> trackws = null;

		String query="SELECT * FROM w_track_work w ";	
		query +="WHERE  w.id_process = :idProcess";
		query += " ORDER BY w.id_process DESC ";
	
		System.out.println(query);
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			trackws = session
					.createSQLQuery(query)
					.addEntity("WTrackWork", WTrackWork.class)
					.setInteger("idProcess",idProcess)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WTrackWorkDao: 001 getWTrackWorks() - can't obtain trackw list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WTrackWorkException(message);

		}

		return trackws;
	}

	@SuppressWarnings("unchecked")
	public List<WTrackWork> getTrackListByCurrentStep ( Integer idCurrentStep ) 
	throws WTrackWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WTrackWork> trackws = null;

		String query="SELECT * FROM w_track_work w ";	
		query +="WHERE  w.id_current_step = :idCurrentStep";
		query += " ORDER BY w.id_current_step DESC ";
	
		System.out.println(query);
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			trackws = session
					.createSQLQuery(query)
					.addEntity("WTrackWork", WTrackWork.class)
					.setInteger("idCurrentStep",idCurrentStep)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WTrackWorkDao: 001 getWTrackWorks() - can't obtain trackw list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WTrackWorkException(message);

		}

		return trackws;
	}


	@SuppressWarnings("unchecked")
	public List<WTrackWork> getTrackListByInsertUser ( Integer idUser ) 
	throws WTrackWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WTrackWork> trackws = null;

		String query="SELECT * FROM w_track_work w ";	
		query +="WHERE  w.insert_user = :idUser";
		query += " ORDER BY w.insert_user DESC ";
	
		System.out.println(query);
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			trackws = session
					.createSQLQuery(query)
					.addEntity("WTrackWork", WTrackWork.class)
					.setInteger("idUser",idUser)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WTrackWorkDao: 001 getWTrackWorks() - can't obtain trackw list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WTrackWorkException(message);

		}

		return trackws;
	}	
	
	@SuppressWarnings("unchecked")
	public List<WTrackWork> getTrackListByUserNotes ( String userNotes ) 
	throws WTrackWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WTrackWork> trackws = null;

		String query="SELECT * FROM w_track_work w ";	
		query +="WHERE  w.user_notes LIKE '%"+userNotes+"%'";
		query += " ORDER BY w.user_notes DESC ";
	
		System.out.println(query);
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			trackws = session
					.createSQLQuery(query)
					.addEntity("WTrackWork", WTrackWork.class)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WTrackWorkDao: 001 getWTrackWorks() - can't obtain trackw list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WTrackWorkException(message);

		}

		return trackws;
	}	
	
	
}
