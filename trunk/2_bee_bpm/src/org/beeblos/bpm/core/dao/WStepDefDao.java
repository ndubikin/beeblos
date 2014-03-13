package org.beeblos.bpm.core.dao;

import static com.sp.common.util.ConstantsCommon.LAST_ADDED;
import static com.sp.common.util.ConstantsCommon.LAST_MODIFIED;
import static org.beeblos.bpm.core.util.Constants.ACTIVE;
import static org.beeblos.bpm.core.util.Constants.INACTIVE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDataFieldException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WStepDataField;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.hibernate.HibernateException;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;



public class WStepDefDao {
	
	private static final Log logger = LogFactory.getLog(WStepDefDao.class.getName());
	
	public WStepDefDao (){
		
	}
	
	public Integer add(WStepDef step) throws WStepDefException {
		
		logger.debug("add() WStepDef - Name: ["+step.getStepHead().getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(step));

		} catch (HibernateException ex) {
			logger.error("WStepDefDao: add - Can't store step definition record "+ 
					step.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("WStepDefDao: add - Can't store step definition record "+ 
					step.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WStepDef step) throws WStepDefException {
		
		logger.debug("update() WStepDef < id = "+step.getId()+">");
		
		try {

			HibernateUtil.update(step);


		} catch (HibernateException ex) {
			logger.error("WStepDefDao: update - Can't update step definition record "+ 
					step.getName()  +
					" - id = "+step.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WStepDefException("WStepDefDao: update - Can't update step definition record "+ 
					step.getName()  +
					" - id = "+step.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	/**
	 * @author dmuleiro - 20130830
	 * 
	 * Updates the step's "logic delete" putting the field "delete" as the user wants. 
	 *
	 * @param  Integer stepId
	 * @param  boolean deleted
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepDefException 
	 * 
	 */
	public void updateStepDeletedField(Integer stepId, boolean deleted, Integer modUserId,
			Date modDate) throws WStepDefException {

		logger.debug("updateStepDeletedField() WStepDef < id = " + stepId + ">");

		try {

			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			session.createQuery(
					"UPDATE WStepDef SET deleted = :deleted, modUser = :modUserId, modDate = :modDate WHERE id = :stepId")
					.setParameter("deleted", deleted)
					.setParameter("stepId", stepId)
					.setParameter("modUserId", modUserId)
					.setParameter("modDate", modDate)
					.executeUpdate();

			tx.commit();

		} catch (HibernateException ex) {
			String message = "SlaDao: update - Can't update deleted field for step " + stepId
					+ " - and deleted = " + deleted + "\n - " + ex.getMessage() + "\n"
					+ ex.getCause();
			logger.error(message);
			throw new WStepDefException(message);

		}

	}
	
	public void delete(WStepDef step) throws WStepDefException {

		logger.debug("delete() WStepDef - Name: ["+step.getName()+"]");
		
		try {

			step = getStepDefByPK(step.getId(), null);// nes 20130808 por el filtro añadido

			HibernateUtil.delete(step);

		} catch (HibernateException ex) {
			logger.error("WStepDefDao: delete - Can't delete proccess definition record "+ step.getName() +
					" <id = "+step.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("WStepDefDao:  delete - Can't delete proccess definition record  "+ step.getName() +
					" <id = "+step.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WStepDefException e) {
			logger.error("WStepDefDao: delete - Exception in deleting step rec "+ step.getName() +
					" <id = "+step.getId()+ "> no esta almacenada \n"+" - "+e.getMessage()+"\n"+e.getCause() );
			throw new WStepDefException("WStepDefDao: delete - Exception in deleting step rec "+ step.getName() +
					" <id = "+step.getId()+ "> not stored \n"+" - "+e.getMessage()+"\n"+e.getCause() );

		} 

	}
	
	public void deleteStepRole(WStepDef step, WStepRole wsr) throws WStepDefException {

		logger.debug("deleteStepRole() WStepDef - Name: ["+step.getName()+"]");
		
		WStepDef wstepdef = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			wstepdef = (WStepDef) session.get(WStepDef.class, step.getId());
			

			for (WStepRole wsteprole: wstepdef.getRolesRelated()) {
				if ( wsteprole.getRole().getId().equals(wsr.getRole().getId()) ) {
			
					wstepdef.getRolesRelated().remove(wsteprole);
					//wsteprole.setStep(null);
					session.update(wstepdef);
					break;
					
				}
			}
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDefDao: getWStepDefByPK - we can't obtain the required id = "+
					step.getId() + "] - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("WStepDefDao: getWStepDefByPK - we can't obtain the required id : " + step.getId() + " - "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

	}

	public WStepDef getStepDefByPK(Integer id, Integer processHeadId) throws WStepDefException {

		WStepDef step = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			
			tx = session.getTransaction();
			tx.begin();
			
			// nes 20130808 - nota: dejo esto como vestigio de que no me funcionó el filter con el mapping
			// colocado en la coleccion del WSTEPDEF para mapear los dataFieldStep
			// Paso a cargarlo a mano mas abajo ...
//			if (processHeadId!=null) {
//				//session.enableFilter("myFilter").setParameter("myFilterParam", "some-value");
////				session.enableFilter("byProcessHeadId").setParameter("filterProcessHeadId", processHeadId);
//		        Filter filter = session.enableFilter("byProcessHeadId");
//		        filter.setParameter("filterProcessHeadId", processHeadId);
//		        System.out.println("--->> filtro:"+processHeadId);					
//			}
			
			step = (WStepDef) session.createQuery("From WStepDef where id="+id).uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.error("WStepDefDao: getWStepDefByPK - we can't obtain the required id = "+
					id + "] - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("WStepDefDao: getWStepDefByPK - HibernateException we can't obtain the required id : " + id + " - "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		try {

			List<WStepDataField> dataFields = 
					new WStepDataFieldDao().getWStepDataFieldList(processHeadId,step.getStepHead().getId());

			step.setDataFieldDef(dataFields);

		} catch (WStepDataFieldException e) {
			String mess = "WStepDefDao: getWStepDefByPK - WStepDataFieldException can't load related step data fields = "+
								id + "] - \n"+e.getMessage()+"\n"+e.getCause();
			logger.error( mess );
			throw new WStepDefException( mess );
		}

		return step;
	}

//	private static <T> Set<T> filterCollection(Set<T> collection, Session s, Integer id) {
//		Query filterQuery = s.createFilter(collection, "where processHeadId ="+id);
//		return new HashSet<T>(filterQuery.list());
//	}
	
/* dml 20130509 metodo pasado a WStepHeadDao	
	public WStepDef getStepDefByName(String name) throws WStepDefException {

		WStepDef  step = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			step = (WStepDef) session.createCriteria(WStepDef.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDefDao: getWStepDefByName - can't obtain step name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("getWStepDefByName;  can't obtain step name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return step;
	}
*/
	
	public List<WStepDef> getWStepDefs() throws WStepDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepDef> steps = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			steps = session.createQuery("From WStepDef Order By stepHead.name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDefDao: getWStepDefs() - can't obtain step list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("WStepDefDao: getWStepDefs() - can't obtain step list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		// TODO HAY QUE CARGAR EL MANAGED DATA PARA CADA STEP-DEF
		
		return steps;
	}
	
	// dml 20130430
	public Integer getLastVersionNumber(Integer stepHeadId) throws WStepDefException {
	
		Integer version = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			version = (Integer) session.createSQLQuery("SELECT MAX(version) FROM w_step_def WHERE head_id = " + stepHeadId)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDefDao: getLastVersionNumber - can't obtain step last version = " +
					stepHeadId + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("getLastVersionNumber;  can't obtain step last version: " + 
					stepHeadId + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		if (version == null){
			return 0;
		}
		
		return version;
	}	

	// dml 20130827
	public boolean existsStep(Integer stepId) throws WStepDefException {
	
		Integer storedId = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			storedId = (Integer) session.createSQLQuery("SELECT id FROM w_step_def WHERE id = " + stepId)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDefDao: existsStep - can't obtain step id = " +
					stepId + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("existsStep;  can't obtain step id: " + 
					stepId + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		if (storedId == null){
			return false;
		} else {
			return true;
		}
		
	}	

	/**
	 * @author nes 20130502 (dml 20130829 - added the deleted where clause)
	 * 
	 * Returns the List<WStepDef> related with a given WProcessDef.
	 * NOTA: ajustado al nuevo formato de campos de la sequence
	 *
	 * @param  Integer processId
	 * @param Boolean deleted
	 * @param  Integer currentUserId
	 * 
	 * @return List<WStepDef>
	 * 
	 * @throws WStepDefException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<WStepDef> getStepDefs(Integer processDefId, Boolean deleted) 
			throws WStepDefException {
	
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepDef> steps = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			// dml 20130829 - se añade para devolver filtrando "deleted" si es necesario
			String deletedWhereClause = "";
			if (deleted != null){
				if (deleted){
					deletedWhereClause = "AND sd.deleted IS TRUE";
				} else {
					deletedWhereClause = "AND sd.deleted IS NOT TRUE";
				}
			}
			
			// NES 20130726 - ajustada query para que traiga los pasos que no tengan rutas "salientes" (que con la anterior no los traía...)
			String query =  "SELECT sd.* FROM w_step_def sd " 
							+ "WHERE sd.id IN ("
							+ "SELECT DISTINCT wsd.id_origin_step FROM  w_step_sequence_def wsd WHERE wsd.process_id = :processId "
							+ "UNION "
							+ "SELECT DISTINCT id_dest_step FROM w_step_sequence_def WHERE process_id = :processId "
							+ ")" 
							+ deletedWhereClause; // dml 20130829 - si es != "", solo devuelve los "deleted" o los no "deleted"
		
			logger.debug("[QUERY]: "+query);
			
			steps = session.createSQLQuery(query)
					.addEntity("WStepDef", WStepDef.class)
					.setParameter("processId", processDefId)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDefDao: getWStepDefs() - can't obtain step list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("WStepDefDao: getWStepDefs() - can't obtain step list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return steps;
	
	}	
	
	// dml 20130507
	/**
	 * Returns a list of process def id which use given step def id
	 * 
	 * @param stepDefId
	 * @return
	 * @throws WStepDefException
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getProcessIdList(Integer stepDefId) throws WStepDefException {
	
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Integer> processIdList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String query =  " SELECT DISTINCT(wssd.process_id) " +
							" FROM w_step_sequence_def wssd " +
							" WHERE (wssd.id_origin_step = :stepId OR wssd.id_dest_step = :stepId) ";
			
			logger.debug("[QUERY]: "+query);
			
			processIdList = (ArrayList<Integer>) session.createSQLQuery(query)
					.setInteger("stepId", stepDefId)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "WStepDefDao: getProcessIdList() - can't obtain process id list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(mess);
			throw new WStepDefException(mess);

		}

		return processIdList;
	
	}	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WStepDefException {
		 
			List<WStepDef> lwpd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From WStepDef Order By stepHead.name ")
						.list();
		
				tx.commit();

				if (lwpd!=null) {
					
					// inserta los extras
					if ( textoPrimeraLinea!=null && !"".equals(textoPrimeraLinea) ) {
						if ( !textoPrimeraLinea.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,textoPrimeraLinea));  // deja la primera línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
						}
					}
					
					if ( separacion!=null && !"".equals(separacion) ) {
						if ( !separacion.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,separacion));  // deja la separación línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
						}
					}
					
				
					
					for (WStepDef wpd: lwpd) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WStepDefException(
						"Can't obtain WStepDefs combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}
	
	// returns a list with step names
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			Integer processDefId, String firstLineText, String blank )
	throws WProcessDefException {
		 
			List<Object> lwsd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwsd = session
							.createQuery("Select Distinct w.id, w.stepHead.name, w.stepComments FROM WStepDef w, WStepSequenceDef ws WHERE ws.process.id=? and w.id=ws.fromStep.id order by w.stepHead.name")
							.setParameter(0, processDefId)
							.list();
				
				tx.commit();

				if (lwsd!=null) {
					
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
					
					String nombrePaso="";
					for (Object wsd: lwsd) {
						Object [] cols= (Object []) wsd;
						
						nombrePaso=(cols[1]!=null ? cols[1].toString().trim():"") +" "+(cols[2]!=null ? cols[2].toString().trim():""); 
						
						retorno.add(new StringPair(
										(cols[0]!=null ? new Integer(cols[0].toString()):null),
										nombrePaso ) );
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				String mess="Can't obtain WProcessDefs combo list " +ex.getMessage()+"\n"+ex.getCause();
				logger.error(mess);
				throw new WProcessDefException();
			} catch (Exception e) {}

			return retorno;


	}

	/**
	 * Deprecated method. versionId does not applies for step-sequence. This param will be ignored
	 * 
	 * @param processDefId
	 * @param versionId
	 * @param userId
	 * @param userIsProcessAdmin
	 * @param allItems
	 * @param firstLineText
	 * @param blank
	 * @return
	 * @throws WProcessDefException
	 */
	@Deprecated
	public List<StringPair> getComboList(
			Integer processDefId, Integer versionId, Integer userId, boolean userIsProcessAdmin, boolean allItems, 
			String firstLineText, String blank )
	throws WProcessDefException {
		return getComboList(processDefId, userId, userIsProcessAdmin, allItems, 
									firstLineText, blank);	
	}
	
	/**
	 * Returns a list or id/step-name for processDefId and other filters indicated
	 * 
	 * @param processDefId
	 * @param userId
	 * @param userIsProcessAdmin
	 * @param allItems
	 * @param firstLineText
	 * @param blank
	 * @return
	 * @throws WProcessDefException
	 */
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			Integer processDefId, Integer userId, boolean userIsProcessAdmin, boolean allItems, 
			String firstLineText, String blank )
	throws WProcessDefException {		
		 
			List<Object> lwsd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();
				
				String query = "SELECT DISTINCT (s.id), wsh.name, s.step_comments ";
				query += " FROM w_step_def s ";
				query += " LEFT OUTER JOIN w_step_head wsh ON s.head_id = wsh.id ";
				query += " LEFT OUTER JOIN w_step_role wsr ON s.id = wsr.id_step ";
				query += " LEFT OUTER JOIN w_step_user wsu ON s.id = wsu.id_step ";
				query += " WHERE s.id IN ";
				query += " (SELECT DISTINCT id_origin_step FROM w_step_sequence_def ws WHERE ws.process_id = " 
							+ processDefId +  " ) "; // nes 20131114 quitado filtro por version q no corresponde ya en la sequence" AND ws.version_id = " + versionId 
													 // la sequenceses propia de la version del workflow (mapa)
				// dml 20130129
				if (userId != null
						&& !userId.equals(0)
						&& (!userIsProcessAdmin 
						|| (userIsProcessAdmin && !allItems))) {
					
					query +=" AND ( wsr.id_role IN (SELECT wur.id_role FROM w_user_role wur WHERE wur.id_user = " + userId + " ) ";
					query +=" OR (wsu.id_user = " + userId + " ) ) ";
					
				}
				
				query += " ORDER BY wsh.name ";
/*
				lwsd = session
						.createSQLQuery("SELECT DISTINCT w.id, w.name, w.stepComments FROM WStepDef w, WStepSequenceDef ws WHERE ws.process.id=? AND ws.version=? AND w.id = ws.fromStep.id ORDER BY w.name")
						.setParameter(0, idProcess)
						.setParameter(1, version)
						.list();
*/
				
				lwsd = session
						.createSQLQuery(query)  
						.list();

				tx.commit();

				if (lwsd!=null) {
					
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
					
					String nombrePaso="";
					for (Object wsd: lwsd) {
						Object [] cols= (Object []) wsd;
						
						nombrePaso=(cols[1]!=null ? cols[1].toString().trim():"") +" "+(cols[2]!=null ? cols[2].toString().trim():""); 
						
						retorno.add(new StringPair(
										(cols[0]!=null ? new Integer(cols[0].toString()):null),
										nombrePaso ) );
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				String mess="Can't obtain WProcessDefs combo list " +ex.getMessage()+"\n"+ex.getCause();
				logger.error(mess);
				throw new WProcessDefException();
			} catch (Exception e) {}

			return retorno;


	}

	public List<WStepDef> finderWStepDef (String nameFilter, String commentFilter, 
			String instructionsFilter, Integer userId, boolean isAdmin, String searchOrder, 
			Integer stepHeadId, String activeFilter ) 
	throws WStepDefException {
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;
					
		List<WStepDef> lprocess = null;
		
		// build filter from user params. String and Integer values will be added to
		// the String directly in the string filter.
		// Date parameters must be added to hibernate query in the try / catch clause below
		String userFilter = getSQLFilter(nameFilter, commentFilter, instructionsFilter,
				stepHeadId, activeFilter);
		
		String requiredFilter = "";//getRequiredFilter(userId, isAdmin);
		
		String filter = userFilter + ( userFilter!=null && !"".equals(userFilter) 
							&& requiredFilter!=null && !"".equals(requiredFilter)?" AND ":"" ) 
							+ requiredFilter ;
		
		filter = (( filter != null && !"".equals(filter)) ? " WHERE ":"") + filter;
		
		logger.debug(" ---->>>>>>>>>> userFilter:["+userFilter+"]");
		logger.debug(" ---->>>>>>>>>> requiredFilter:["+requiredFilter+"]");
		logger.debug(" ---->>>>>>>>>> filter:["+filter+"]");
		
		// load base query phrase
		String query = getBaseQuery( isAdmin );
		
		logger.debug(" ---->>>>>>>>>> base query:["+query+"]");

		// builds full query phrase
		query += filter+getSQLOrder(searchOrder);

		logger.debug(" ---->>>>>>>>>> FULL query:["+query+"]");
		logger.debug(" ---->>>>>>>>>> userId: "+userId);

		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			q = session
					.createSQLQuery(query)
					.addEntity("WStepDef", WStepDef.class);

			// set userId
			//q.setInteger("userId",userId);
			
			// retrieve list
			lprocess = q.list();
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepDefDao: 002 getWStepDefs() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WStepDefException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepDefDao: 002B getWStepDefs() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WStepDefException(message);
		}
		
		return lprocess;
	}

	private String getRequiredFilter ( Integer userId, boolean isAdmin ) {
		
		String reqFilter = " ( ( wsr.id_role in ";
		reqFilter +="(select wur.id_role from w_user_def wud, w_user_role wur where wur.id_user=:userId ) OR  ";
		reqFilter +=" ( wsu.id_user =:userId ) OR  ";
		reqFilter +=" ( wswa.id_role in ";
		reqFilter +="(select wur.id_role from w_user_def wud, w_user_role wur where wur.id_user=:userId )) OR  ";
		reqFilter +=" ( wswa.id_user =:userId )  ) ) ";
	
		return reqFilter;
	
	}
	
	private String getBaseQuery(boolean isAdmin) {
	
		String baseQueryTmp = "SELECT * FROM w_step_def wsd ";
		baseQueryTmp += "LEFT OUTER JOIN w_step_head wsh ON wsd.head_id = wsh.id ";
	
		return baseQueryTmp;
	
	}
/*	
	private String getFilter(String nameFilter, String commentFilter, String instructionsFilter) {

		String filter="";
	
		if ( nameFilter!=null && ! "".equals(nameFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" wpd.name = "+nameFilter;
		}
	
		if ( commentFilter!=null && ! "".equals(commentFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" stepComments = "+commentFilter;
		}
	
		if ( instructionsFilter!=null && ! "".equals(instructionsFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" instructions = "+instructionsFilter;
		}
	
		return filter;
	
	}
*/	
	private String getSQLFilter(String nameFilter, String commentFilter, String instructionsFilter, 
			Integer stepHeadId, String activeFilter ) {
	
		String filter="";
	
	
		if (nameFilter!=null && ! "".equals(nameFilter)){
			if (filter == ""){
				filter+=" wsh.name LIKE '%"+nameFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wsh.name LIKE '%"+nameFilter.trim()+"%' ";
			}
		}
		
		if (commentFilter!=null && ! "".equals(commentFilter)){
			if (filter == ""){
				filter+=" wsd.step_comments LIKE '%"+commentFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wsd.step_comments LIKE '%"+commentFilter.trim()+"%' ";
			}
		}
	
		if (instructionsFilter!=null && ! "".equals(instructionsFilter)){
			if (filter == ""){
				filter+=" wsd.instructions LIKE '%"+instructionsFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wsd.instructions LIKE '%"+instructionsFilter.trim()+"%' ";
			}
		}
	
		// dml 20130508
		if (activeFilter != null
				&& (activeFilter.equals(ACTIVE)
				|| activeFilter.equals(INACTIVE))) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			if (activeFilter.equals(ACTIVE)) {
				filter += " wsd.active IS TRUE ";
			} else if (activeFilter.equals(INACTIVE)){
				filter += " wsd.active IS FALSE ";

			}
		}
		
		// dml 20130508
		if (stepHeadId != null
				&& !stepHeadId.equals(0)) {
			if (!"".equals(filter)) {
				filter += " AND wsd.head_id = " + stepHeadId + " ";
			} else {
				filter += " wsd.head_id = " + stepHeadId + " ";

			}
		}
		
		return filter;

	}	
	
	private String getOrder() {
	
		return "";
		
	}
	
	
	
	private String getSQLOrder(String searchOrder) {
	
		String ret = "";
		
		if (searchOrder != null && !"".equals(searchOrder.trim())){
			if (searchOrder.equals(LAST_ADDED)) {
				ret = " ORDER by wsd.insert_date DESC ";
			} else if (searchOrder.equals(LAST_MODIFIED)) {
				ret = " ORDER by wsd.mod_date DESC ";
			} 
		}
		
		return ret;

	}
	
}
	