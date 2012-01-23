package org.beeblos.bpm.core.dao;

import static org.beeblos.bpm.core.util.Constants.LAST_W_STEP_DEF_ADDED;
import static org.beeblos.bpm.core.util.Constants.LAST_W_STEP_DEF_MODIFIED;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;



public class WStepDefDao {
	
	private static final Log logger = LogFactory.getLog(WStepDefDao.class.getName());
	
	public WStepDefDao (){
		
	}
	
	public Integer add(WStepDef step) throws WStepDefException {
		
		logger.debug("add() WStepDef - Name: ["+step.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(step));

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

			HibernateUtil.actualizar(step);


		} catch (HibernateException ex) {
			logger.error("WStepDefDao: update - Can't update step definition record "+ 
					step.getName()  +
					" - id = "+step.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WStepDefException("WStepDefDao: update - Can't update step definition record "+ 
					step.getName()  +
					" - id = "+step.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WStepDef step) throws WStepDefException {

		logger.debug("delete() WStepDef - Name: ["+step.getName()+"]");
		
		try {

			step = getWStepDefByPK(step.getId());

			HibernateUtil.borrar(step);

		} catch (HibernateException ex) {
			logger.error("WStepDefDao: delete - Can't delete proccess definition record "+ step.getName() +
					" <id = "+step.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("WStepDefDao:  delete - Can't delete proccess definition record  "+ step.getName() +
					" <id = "+step.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WStepDefException ex1) {
			logger.error("WStepDefDao: delete - Exception in deleting step rec "+ step.getName() +
					" <id = "+step.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
			throw new WStepDefException("WStepDefDao: delete - Exception in deleting step rec "+ step.getName() +
					" <id = "+step.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

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

	public WStepDef getWStepDefByPK(Integer id) throws WStepDefException {

		WStepDef step = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			step = (WStepDef) session.get(WStepDef.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepDefDao: getWStepDefByPK - we can't obtain the required id = "+
					id + "] - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepDefException("WStepDefDao: getWStepDefByPK - we can't obtain the required id : " + id + " - "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return step;
	}
	
	
	public WStepDef getWStepDefByName(String name) throws WStepDefException {

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

	
	public List<WStepDef> getWStepDefs() throws WStepDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepDef> steps = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			steps = session.createQuery("From WStepDef order by name ").list();

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
	
	@SuppressWarnings("unchecked")
	public List<WStepDef> getWStepDefs(Integer currentWProcessDefId, Integer version) throws WStepDefException {
	
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepDef> steps = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String query =  "SELECT * FROM w_step_def ws " +
							"WHERE ws.id IN (SELECT DISTINCT wsd.id_origin_step " +
							"FROM  w_step_sequence_def wsd " +
							"WHERE wsd.id_process = :idStep AND wsd.version = :version)";
			
			System.out.println("[QUERY]: "+query);
			
			steps = session.createSQLQuery(query)
					.addEntity("WStepDef", WStepDef.class)
					.setParameter("idStep", currentWProcessDefId)
					.setParameter("version", version)
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
						.createQuery("From WStepDef order by name ")
						.list();
		
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
			Integer idProcess, Integer version,
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

				lwsd = session
							.createQuery("select distinct w.id, w.name, w.stepComments from WStepDef w, WStepSequenceDef ws WHERE ws.process.id=? and ws.version=? and w.id=ws.fromStep.id order by w.name")
							.setParameter(0, idProcess)
							.setParameter(1, version)
							.list();

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

	public List<WStepDef> getStepListByFinder (String nameFilter, String commentFilter, 
			String instructionsFilter, Integer userId, boolean isAdmin, String action ) 
	throws WStepDefException {
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;
					
		List<WStepDef> lprocess = null;
		
		// build filter from user params. String and Integer values will be added to
		// the String directly in the string filter.
		// Date parameters must be added to hibernate query in the try / catch clause below
		String userFilter = getSQLFilter(nameFilter, commentFilter, instructionsFilter);
		
		String requiredFilter = "";//getRequiredFilter(userId, isAdmin);
		
		String filter = userFilter + ( userFilter!=null && !"".equals(userFilter) 
							&& requiredFilter!=null && !"".equals(requiredFilter)?" AND ":"" ) 
							+ requiredFilter ;
		
		filter = (( filter != null && !"".equals(filter)) ? " WHERE ":"") + filter;
		
		System.out.println(" ---->>>>>>>>>> userFilter:["+userFilter+"]");
		System.out.println(" ---->>>>>>>>>> requiredFilter:["+requiredFilter+"]");
		System.out.println(" ---->>>>>>>>>> filter:["+filter+"]");
		
		// load base query phrase
		String query = getBaseQuery( isAdmin );
		
		System.out.println(" ---->>>>>>>>>> base query:["+query+"]");

		// builds full query phrase
		query += filter+getSQLOrder(action);

		System.out.println(" ---->>>>>>>>>> FULL query:["+query+"]");
		System.out.println(" ---->>>>>>>>>> userId: "+userId);

		
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
	
		String baseQueryTmp="SELECT * FROM w_step_def wsd ";
	
		return baseQueryTmp;
	
	}
	
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
	
	private String getSQLFilter(String nameFilter, String commentFilter, String instructionsFilter) {
	
		String filter="";
	
	
		if (nameFilter!=null && ! "".equals(nameFilter)){
			if (filter == ""){
				filter+=" wsd.name LIKE '%"+nameFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wsd.name LIKE '%"+nameFilter.trim()+"%' ";
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
	
		return filter;
	}
	
	
	
	private String getOrder() {
	
		return "";
		
	}
	
	
	
	private String getSQLOrder(String action) {
	
		String ret = "";
		
		if (action==null || action.equals("")) {
			
			ret = "";
			
		} else if (action.equals(LAST_W_STEP_DEF_ADDED)) {
			
			ret = " ORDER by wsd.insert_date DESC ";
			
		} else if (action.equals(LAST_W_STEP_DEF_MODIFIED)) {
			
			ret = " ORDER by wsd.mod_date DESC ";
			
		}
		
		return ret;
		
	}
	
}
	