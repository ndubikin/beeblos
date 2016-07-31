package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.model.WProcessHeadManagedDataConfiguration;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.hb4util.core.util.HibernateUtil;
import com.sp.common.util.StringPair;


public class WProcessHeadManagedDataDao {
	
	private static final Log logger = LogFactory.getLog(WProcessHeadManagedDataDao.class.getName());
	
	public WProcessHeadManagedDataDao (){
		
	}
	
	public Integer add(WProcessHeadManagedDataConfiguration managedTableDef, Integer currentUserId) throws WProcessHeadException {
		
		logger.debug("add() WProcessHeadManagedDataConfiguration - Name: ["+managedTableDef.getName()+"]");
		
		System.out.println("-------------------------------------------------------------------------------------------");
		System.out.println("mtd:"+managedTableDef);
		System.out.println("------------------------------------------------------------------------------------------");
		try {

			return Integer.valueOf(HibernateUtil.save(managedTableDef));

		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: add - Can't store process definition record "+ 
					managedTableDef.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessHeadException("WProcessHeadDao: add - Can't store process definition record "+ 
					managedTableDef.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WProcessHeadManagedDataConfiguration managedTableDef, Integer currentUserId) throws WProcessHeadException {
		
		logger.debug("update() WProcessHeadManagedDataConfiguration < id = "+managedTableDef.getHeadId()+">");
		
		try {

			HibernateUtil.update(managedTableDef);


		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: update - Can't update process definition record "+ 
					managedTableDef.getName()  +
					" - id = "+managedTableDef.getHeadId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessHeadException("WProcessHeadDao: update - Can't update process definition record "+ 
					managedTableDef.getName()  +
					" - id = "+managedTableDef.getHeadId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WProcessHeadManagedDataConfiguration managedTableDef) throws WProcessHeadException {

		logger.debug("delete() WProcessHeadManagedDataConfiguration - Name: ["+managedTableDef.getName()+"]");
		
		try {

			//process = getWProcessByPK(process.getId());

			HibernateUtil.delete(managedTableDef);

		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: delete - Can't delete proccess definition record "+ managedTableDef.getName() +
					" <id = "+managedTableDef.getHeadId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessHeadException("WProcessHeadDao:  delete - Can't delete proccess definition record  "+ managedTableDef.getName() +
					" <id = "+managedTableDef.getHeadId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

//		} catch (WProcessHeadException e) {
//			logger.error("WProcessHeadDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> no esta almacenada \n"+" - "+e.getMessage()+"\n"+e.getCause() );
//			throw new WProcessHeadException("WProcessHeadDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> not stored \n"+" - "+e.getMessage()+"\n"+e.getCause() );

		} 

	}

	public WProcessHeadManagedDataConfiguration getWProcessHeadManagedTableByPK(Integer id, Integer currentUserId) throws WProcessHeadException {

		WProcessHeadManagedDataConfiguration process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			process = (WProcessHeadManagedDataConfiguration) session.get(WProcessHeadManagedDataConfiguration.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: getWProcessByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessHeadException("WProcessHeadDao: getWProcessByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}
	
	
	public WProcessHeadManagedDataConfiguration getWProcessHeadManagedTableByName(String name, Integer currentUserId) throws WProcessHeadException {

		WProcessHeadManagedDataConfiguration  process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessHeadManagedDataConfiguration) session.createCriteria(WProcessHeadManagedDataConfiguration.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: getWProcessByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessHeadException("getWProcessByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}


	// id=processId
	public String getTableName(Integer id, Integer currentUserId) throws WProcessDefException {

		String name = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			name = (String) session
					.createQuery("Select name from WProcessHeadManagedDataConfiguration Where headId = :id)")
						.setInteger("id",id)
						.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadManagedDataConfiguration: getTableName - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDef: getTableName - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return name;
	}
	
	public List<WProcessHeadManagedDataConfiguration> getTableDefList(Integer currentUserId) throws WProcessHeadException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessHeadManagedDataConfiguration> processList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processList = session.createQuery("From WProcessHeadManagedDataConfiguration Order By name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: getWProcesss() - can't obtain tabledef list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessHeadException("WProcessHeadDao: getWProcesss() - can't obtain tabledef list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank, Integer currentUserId)
	throws WProcessHeadException {
		 
			List<WProcessHeadManagedDataConfiguration> lwph = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwph = session
						.createQuery("From WProcessHeadManagedDataConfiguration Order By name ")
						.list();
		
				tx.commit();

				if (lwph!=null) {
					
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
							retorno.add(new StringPair(null,blank));  // deja la separación línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
						}
					}
					
					for (WProcessHeadManagedDataConfiguration wph: lwph) {
						retorno.add(new StringPair(wph.getHeadId(),wph.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WProcessHeadException(
						"Can't obtain WProcessHeadManagedDataConfiguration combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;
	}

}
	