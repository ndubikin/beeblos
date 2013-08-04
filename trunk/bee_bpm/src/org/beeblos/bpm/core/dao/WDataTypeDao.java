package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WDataTypeException;
import org.beeblos.bpm.core.error.WStepDefException;
import com.sp.common.model.WDataType;
import org.beeblos.bpm.core.model.WStepDef;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;


public class WDataTypeDao {
	
	private static final Log logger = LogFactory.getLog(WDataTypeDao.class.getName());
	
	public WDataTypeDao (){
		
	}
	
	public Integer add(WDataType dataType) throws WDataTypeException {
		
		logger.debug("add() WDataType - Name: ["+dataType.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(dataType));

		} catch (HibernateException ex) {
			logger.error("WDataTypeDao: add - Can't store process definition record "+ 
					dataType.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WDataTypeException("WDataTypeDao: add - Can't store process definition record "+ 
					dataType.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WDataType dataType) throws WDataTypeException {
		
		logger.debug("update() WDataType < id = "+dataType.getId()+">");
		
		try {

			HibernateUtil.actualizar(dataType);


		} catch (HibernateException ex) {
			logger.error("WDataTypeDao: update - Can't update process definition record "+ 
					dataType.getName()  +
					" - id = "+dataType.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WDataTypeException("WDataTypeDao: update - Can't update process definition record "+ 
					dataType.getName()  +
					" - id = "+dataType.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WDataType dataType) throws WDataTypeException {

		logger.debug("delete() WDataType - Name: ["+dataType.getName()+"]");
		
		try {

			HibernateUtil.borrar(dataType);

		} catch (HibernateException ex) {
			logger.error("WDataTypeDao: delete - Can't delete proccess definition record "+ dataType.getName() +
					" <id = "+dataType.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WDataTypeException("WDataTypeDao:  delete - Can't delete proccess definition record  "+ dataType.getName() +
					" <id = "+dataType.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} 

	}

	public WDataType getWDataTypeByPK(Integer id) throws WDataTypeException {

		WDataType dataType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			dataType = (WDataType) session.get(WDataType.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WDataTypeDao: getWDataTypeByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WDataTypeException("WDataTypeDao: getWDataTypeByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return dataType;
	}
	
	
	public WDataType getWDataTypeByName(String name) throws WDataTypeException {

		WDataType dataType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			dataType = (WDataType) session.createCriteria(WDataType.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WDataTypeDao: getWDataTypeByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WDataTypeException("getWDataTypeByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return dataType;
	}

	
	public List<WDataType> getWDataTypeList() throws WDataTypeException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WDataType> dataType = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			dataType = session.createQuery("From WDataType order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WDataTypeDao: getWDataTypeList() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WDataTypeException("WDataTypeDao: getWDataTypeList() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return dataType;
	}
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String separationLine )
	throws WDataTypeException {
		 
			List<WDataType> ldt = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				ldt = session
						.createQuery("From WDataType Order By name ")
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
					
				
					
					for (WDataType dt: ldt) {
						retorno.add(new StringPair(dt.getId(),dt.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WDataTypeException(
						"Can't obtain WDataType combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

}
	