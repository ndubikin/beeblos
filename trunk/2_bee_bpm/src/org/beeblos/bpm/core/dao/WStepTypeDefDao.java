package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepTypeDefException;
import org.beeblos.bpm.core.model.WStepTypeDef;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;

public class WStepTypeDefDao {

	private static final Log logger = LogFactory.getLog(WStepTypeDefDao.class.getName());

	public WStepTypeDefDao (){
		super();
	}

	public Integer add(WStepTypeDef wStepTypeDef) throws WStepTypeDefException {

		logger.debug("add() WStepTypeDef - Name: ["+wStepTypeDef.getName()+"]");

		try {

			return Integer.valueOf(HibernateUtil.save(wStepTypeDef));

		} catch (HibernateException ex) {
			logger.error("WStepTypeDefDao: add - Can't store wStepTypeDef definition record "+ 
					wStepTypeDef.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepTypeDefException("WStepTypeDefDao: add - Can't store wStepTypeDef definition record "+ 
					wStepTypeDef.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}


	public void update(WStepTypeDef wStepTypeDef) throws WStepTypeDefException {

		logger.debug("update() WStepTypeDef < id = "+wStepTypeDef.getId()+">");

		try {

			HibernateUtil.update(wStepTypeDef);


		} catch (HibernateException ex) {
			logger.error("WStepTypeDefDao: update - Can't update wStepTypeDef definition record "+ 
					wStepTypeDef.getName()  +
					" - id = "+wStepTypeDef.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WStepTypeDefException("WStepTypeDefDao: update - Can't update wStepTypeDef definition record "+ 
					wStepTypeDef.getName()  +
					" - id = "+wStepTypeDef.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}


	public void delete(WStepTypeDef wStepTypeDef) throws WStepTypeDefException {

		logger.debug("delete() WStepTypeDef - Name: ["+wStepTypeDef.getName()+"]");

		try {

			wStepTypeDef = getWStepTypeDefByPK(wStepTypeDef.getId());

			HibernateUtil.delete(wStepTypeDef);

		} catch (HibernateException ex) {
			logger.error("WStepTypeDefDao: delete - Can't delete wStepTypeDef definition record "+ wStepTypeDef.getName() +
					" <id = "+wStepTypeDef.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepTypeDefException("WStepTypeDefDao:  delete - Can't delete wStepTypeDef definition record  "+ wStepTypeDef.getName() +
					" <id = "+wStepTypeDef.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WStepTypeDefException e) {
			logger.error("WStepTypeDefDao: delete - Exception in deleting wStepTypeDef rec "+ wStepTypeDef.getName() +
					" <id = "+wStepTypeDef.getId()+ "> no esta almacenada \n"+" - "+e.getMessage()+"\n"+e.getCause() );
			throw new WStepTypeDefException("WStepTypeDefDao: delete - Exception in deleting wStepTypeDef rec "+ wStepTypeDef.getName() +
					" <id = "+wStepTypeDef.getId()+ "> not stored \n"+" - "+e.getMessage()+"\n"+e.getCause() );

		} 

	}

	public void delete(Integer wStepTypeDefId) throws WStepTypeDefException {
		logger.debug("delete() WStepTypeDef - Name: ["+wStepTypeDefId+"]");

		WStepTypeDef wStepTypeDef =null;
		try {

			wStepTypeDef = getWStepTypeDefByPK(wStepTypeDefId);

			HibernateUtil.delete(wStepTypeDef);

		} catch (HibernateException ex) {

			String mess = "WStepTypeDefDao: HibernateException: Error deleting wStepTypeDef rec " 
					+ " <id = "+wStepTypeDefId+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause(); 
			logger.error( mess );
			throw new WStepTypeDefException(mess );

		} catch (WStepTypeDefException e) {

			String mess = "WStepTypeDefDao: WStepTypeDefException: Error deleting wStepTypeDef rec " 
					+ " <id = "+wStepTypeDefId+ "> \n"+" - "+e.getMessage()+"\n"+e.getCause();
			logger.error( mess );
			throw new WStepTypeDefException(mess );

		} catch (Exception e) {

			String mess = "WStepTypeDefDao: Exception: Error deleting wStepTypeDef rec " 
					+ " <id = "+wStepTypeDefId+ "> \n"+" - "+e.getMessage()+"\n"+e.getCause()
					+ " - "+ e.getClass(); 
			logger.error( mess );
			throw new WStepTypeDefException(mess );
		}
	}

	public WStepTypeDef getWStepTypeDefByPK(Integer id) throws WStepTypeDefException {

		WStepTypeDef wStepTypeDef = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			wStepTypeDef = (WStepTypeDef) session.get(WStepTypeDef.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepTypeDefDao: getWStepTypeDefByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepTypeDefException("WStepTypeDefDao: getWStepTypeDefByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return wStepTypeDef;
	}


	public WStepTypeDef getWStepTypeDefByName(String name) throws WStepTypeDefException {

		WStepTypeDef  wStepTypeDef = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			wStepTypeDef = (WStepTypeDef) session.createCriteria(WStepTypeDef.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepTypeDefDao: getWStepTypeDefByName - can't obtain wStepTypeDef name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepTypeDefException("getWStepTypeDefByName;  can't obtain wStepTypeDef name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return wStepTypeDef;
	}


	public List<WStepTypeDef> getWStepTypeDefs() throws WStepTypeDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepTypeDef> lwStepTypeDef = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			lwStepTypeDef = session.createQuery("From WStepTypeDef").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
			    tx.rollback(); 
			String mess = "HibernateException: getWStepTypeDefs - Can't get types "
					+ ex.getMessage() + (ex.getCause()!=null?". " +ex.getCause():"");
			logger.error(mess);
			throw new WStepTypeDefException(mess);
		} catch (Exception ex) {
			if (tx != null)
			    tx.rollback(); 
			String mess = "Exception: getWStepTypeDefs - Can't get types "
					+ ex.getMessage() + (ex.getCause()!=null?". " +ex.getCause():"")
					+ (ex.getClass()!=null?". " +ex.getClass():"");
			logger.error(mess);
			throw new WStepTypeDefException(mess);
		}

		return lwStepTypeDef;
	}


	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank )
					throws WStepTypeDefException {

		List<WStepTypeDef> lwStepTypeDef = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			lwStepTypeDef = session
					.createQuery("From WStepTypeDef order by name ")
					.list();

			tx.commit();

			if (lwStepTypeDef!=null) {

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



				for (WStepTypeDef wpd: lwStepTypeDef) {
					retorno.add(new StringPair(wpd.getId(),wpd.getName()));
				}
			} else {
				// nes  -if select returns null then I return null
				retorno=null;
			}


		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new WStepTypeDefException(
					"Can't obtain Roles combo list "
							+ex.getMessage()+"\n"+ex.getCause());
		} catch (Exception e) {}

		return retorno;
	}
	

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboListWithType(
			String firstLineText, String blank ,
			String type)
					throws WStepTypeDefException {

		List<WStepTypeDef> lwStepTypeDef = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		String query="From WStepTypeDef wtd ";
		String filter = _getFilter("type", type);
		String order = " ORDER BY wtd.name ";

		query += filter+order;

		logger.debug("QUERY: "+query);
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			lwStepTypeDef = session
					.createQuery(query)
					.list();

			tx.commit();

			if (lwStepTypeDef!=null) {

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



				for (WStepTypeDef wpd: lwStepTypeDef) {
					retorno.add(new StringPair(wpd.getId(),wpd.getName()));
				}
			} else {
				// nes  -if select returns null then I return null
				retorno=null;
			}


		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new WStepTypeDefException(
					"Can't obtain Roles combo list "
							+ex.getMessage()+"\n"+ex.getCause());
		} catch (Exception e) {}

		return retorno;
	}

	private String _getFilter(String propertyName, String idUser) {

		String filter = " WHERE wtd." + propertyName + "='"+ idUser + "'";

		return filter;
	}

}
