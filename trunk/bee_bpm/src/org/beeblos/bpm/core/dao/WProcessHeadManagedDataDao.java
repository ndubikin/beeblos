package org.beeblos.bpm.core.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessException;
import org.beeblos.bpm.core.model.WProcessHeadManagedData;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;


public class WProcessHeadManagedDataDao {
	
	private static final Log logger = LogFactory.getLog(WProcessHeadManagedDataDao.class.getName());
	
	public WProcessHeadManagedDataDao (){
		
	}
	
	public Integer add(WProcessHeadManagedData managedTableDef, Integer currentUserId) throws WProcessException {
		
		logger.debug("add() WProcessHeadManagedData - Name: ["+managedTableDef.getName()+"]");
		
		System.out.println("-------------------------------------------------------------------------------------------");
		System.out.println("mtd:"+managedTableDef);
		System.out.println("------------------------------------------------------------------------------------------");
		try {

			return Integer.valueOf(HibernateUtil.guardar(managedTableDef));

		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: add - Can't store process definition record "+ 
					managedTableDef.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessException("WProcessHeadDao: add - Can't store process definition record "+ 
					managedTableDef.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WProcessHeadManagedData managedTableDef, Integer currentUserId) throws WProcessException {
		
		logger.debug("update() WProcessHeadManagedData < id = "+managedTableDef.getHeadId()+">");
		
		try {

			HibernateUtil.actualizar(managedTableDef);


		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: update - Can't update process definition record "+ 
					managedTableDef.getName()  +
					" - id = "+managedTableDef.getHeadId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessException("WProcessHeadDao: update - Can't update process definition record "+ 
					managedTableDef.getName()  +
					" - id = "+managedTableDef.getHeadId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WProcessHeadManagedData managedTableDef, Integer currentUserId) throws WProcessException {

		logger.debug("delete() WProcessHeadManagedData - Name: ["+managedTableDef.getName()+"]");
		
		try {

			//process = getWProcessByPK(process.getId());

			HibernateUtil.borrar(managedTableDef);

		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: delete - Can't delete proccess definition record "+ managedTableDef.getName() +
					" <id = "+managedTableDef.getHeadId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessException("WProcessHeadDao:  delete - Can't delete proccess definition record  "+ managedTableDef.getName() +
					" <id = "+managedTableDef.getHeadId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

//		} catch (WProcessException ex1) {
//			logger.error("WProcessHeadDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
//			throw new WProcessException("WProcessHeadDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}

	public WProcessHeadManagedData getWProcessHeadManagedTableByPK(Integer id, Integer currentUserId) throws WProcessException {

		WProcessHeadManagedData process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			process = (WProcessHeadManagedData) session.get(WProcessHeadManagedData.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: getWProcessByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessException("WProcessHeadDao: getWProcessByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}
	
	
	public WProcessHeadManagedData getWProcessHeadManagedTableByName(String name, Integer currentUserId) throws WProcessException {

		WProcessHeadManagedData  process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessHeadManagedData) session.createCriteria(WProcessHeadManagedData.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: getWProcessByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessException("getWProcessByName;  can't obtain process name: " + 
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
					.createQuery("Select name from WProcessHeadManagedData Where headId = :id)")
						.setInteger("id",id)
						.uniqueResult();


			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadManagedData: getTableName - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDef: getTableName - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return name;
	}
	
	public List<WProcessHeadManagedData> getTableDefList(Integer currentUserId) throws WProcessException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessHeadManagedData> processList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processList = session.createQuery("From WProcessHeadManagedData Order By name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: getWProcesss() - can't obtain tabledef list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessException("WProcessHeadDao: getWProcesss() - can't obtain tabledef list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank, Integer currentUserId)
	throws WProcessException {
		 
			List<WProcessHeadManagedData> lwph = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwph = session
						.createQuery("From WProcessHeadManagedData Order By name ")
						.list();
		
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
					
					for (WProcessHeadManagedData wph: lwph) {
						retorno.add(new StringPair(wph.getHeadId(),wph.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WProcessException(
						"Can't obtain WProcessHeadManagedData combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;
	}

}
	