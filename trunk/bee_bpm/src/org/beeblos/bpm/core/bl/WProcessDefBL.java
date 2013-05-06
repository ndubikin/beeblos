package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.model.WProcess;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessRole;
import org.beeblos.bpm.core.model.WProcessUser;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;



public class WProcessDefBL {
	
	private static final Log logger = LogFactory.getLog(WProcessDefBL.class.getName());
	
	public WProcessDefBL (){
		
	}
	
	public Integer add(WProcessDef process, Integer currentUser) throws WProcessDefException, WProcessException {
		
		logger.debug("add() WProcessDef - Name: ["+process.getName()+"]");
		
		// dml 20130430 - si es un nuevo WProcess se guarda antes de guardar el WProcessDef
		if (process.getProcess() != null
				&& (process.getProcess().getId() == null
				|| process.getProcess().getId().equals(0))){
			
			Integer processId = new WProcessBL().add(process.getProcess(), currentUser);
			process.getProcess().setId(processId);
			
		}
		
		// timestamp & trace info
		process.setInsertDate(new Date());
		process.setModDate( DEFAULT_MOD_DATE );
		process.setInsertUser(currentUser);
		process.setModUser(currentUser);
		return new WProcessDefDao().add(process);

	}
	
	// dml 20130430
	public void createFirstWProcessDef(WProcess process, Integer currentUser) throws WProcessDefException, WProcessException{
		
		WProcessDef wpd = new WProcessDef();
		wpd.setActive(true);

		wpd.setProcess(process);
		wpd.setVersion(1);
		
		this.add(wpd, currentUser);
		
	}
	
	public void update(WProcessDef process, Integer currentUser) throws WProcessDefException {
		
		logger.debug("update() WProcessDef < id = "+process.getId()+">");
		
		if (!process.equals(new WProcessDefDao().getWProcessDefByPK(process.getId())) ) {

			// timestamp & trace info
			process.setModDate(new Date());
			process.setModUser(currentUser);
			new WProcessDefDao().update(process);
			
		} else {
			
			logger.debug("WProcessDefBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcessDef process, Integer currentUser) throws WProcessDefException {

		logger.debug("delete() WProcessDef - Name: ["+process.getName()+"]");
		
		new WProcessDefDao().delete(process);

	}

	public WProcessDef getWProcessDefByPK(Integer id, Integer currentUserId) 
				throws WProcessDefException, WStepSequenceDefException {

		WProcessDef wpd;
		wpd = new WProcessDefDao().getWProcessDefByPK(id);
		
		try {
			wpd.setlSteps(loadStepList(wpd.getId(),currentUserId));
		} catch (WStepDefException e1) {
			String mess="Error: getWProcessDefByPK "+e1.getMessage();
			throw new WStepSequenceDefException(mess);
		}
		
		try {
			wpd.setStepSequenceList(loadStepSequenceList(wpd.getId(),currentUserId));
		} catch (WStepSequenceDefException e) {
			String mess="Error: getWProcessDefByPK "+e.getMessage();
			throw new WStepSequenceDefException(mess);
		}
		
		return wpd;
		
	}
	
	
	public WProcessDef getWProcessDefByName(String name, Integer currentUserId) throws WProcessDefException, WStepSequenceDefException {

		WProcessDef wpd;
		wpd = new WProcessDefDao().getWProcessDefByName(name);
		
		try {
			wpd.setlSteps(loadStepList(wpd.getId(),currentUserId));
		} catch (WStepDefException e1) {
			String mess="Error: getWProcessDefByName "+e1.getMessage();
			throw new WStepSequenceDefException(mess);
		}
		
		try {
			wpd.setStepSequenceList(loadStepSequenceList(wpd.getId(),currentUserId));
		} catch (WStepSequenceDefException e) {
			String mess="Error: getWProcessDefByName "+e.getMessage();
			throw new WStepSequenceDefException(mess);
		}
		
		return wpd;
	}

	
	// DAVID ESTA QUE HAY Q METERLE 1 FOR TE LA DEJO PARA VOS ...
	// HABRIA Q VER PORQUE SI 1 PROCESO PETA Y NO SE CARGA, NO DEBERIAMOS ABORTAR LA LISTA COMPLETA
	// SI NO QUE DEBERIAMOS PASAR POR ALTO ESE PROCESO Y ESCRIBIR LOG
	// Y TIRAMOS UN THROW SOLO SI PETA ALGO GRAVE Q NO DEJE CARGAR NADA, NINGUN PROCESO, OK?
	public List<WProcessDef> getWProcessDefs(Integer currentUser) throws WProcessDefException {

		return new WProcessDefDao().getWProcessDefs();
	
	}
	
	// dml 20130430
	public Integer getLastVersionNumber(Integer processId) throws WProcessDefException {

		return new WProcessDefDao().getLastVersionNumber(processId);
	
	}
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WProcessDefException {
		 
		return new WProcessDefDao().getComboList(textoPrimeraLinea, separacion);
		
	}

	// dml 20120120
	public List<StringPair> getComboActiveProcessList(String firstLineText, String blank )
	throws WProcessDefException {
	
		return new WProcessDefDao().getComboActiveProcessList(firstLineText, blank);
	
	}	
	
	public List<WProcessDef> getProcessListByFinder (Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter,
			Integer userId, boolean isAdmin, String action ) 
	throws WProcessDefException {
		
		return new WProcessDefDao().getProcessListByFinder(initialInsertDateFilter, finalInsertDateFilter, 
				strictInsertDateFilter, nameFilter, commentFilter, listZoneFilter, 
				workZoneFilter, additinalZoneFilter, userId, isAdmin, action);

	}

	public List<WProcessDefLight> getWorkingProcessListFinder(boolean onlyWorkingProcessesFilter, 
			String processNameFilter, Date initialProductionDateFilter, Date finalProductionDateFilter, 
			boolean estrictProductionDateFilter, Integer productionUserFilter, String action)
	throws WProcessDefException {
		
		return new WProcessDefDao().getWorkingProcessListFinder(onlyWorkingProcessesFilter, 
				processNameFilter, initialProductionDateFilter, finalProductionDateFilter,
				estrictProductionDateFilter, productionUserFilter, action);
		
	}

	public String getProcessNameByVersionId(Integer id, Integer currentUserId) 
			throws WProcessDefException, WStepSequenceDefException {

		return new WProcessDefDao().getProcessNameByVersionId(id);
	
	}
	
	
	// clone a process version (w_process_def, related users and roles, related steps and routes ...)
	// returns new process id
	public Integer cloneWProcessDef(Integer processId, Integer userId) 
			throws  WProcessException, WStepSequenceDefException, WProcessDefException  {
		
		Integer clonedId=null,newversion=0;
		WProcessDef newprocver,procver;
		List<WStepSequenceDef> routes = new ArrayList<WStepSequenceDef>();
		try {
			newprocver = this.getWProcessDefByPK(processId,userId);
			procver = this.getWProcessDefByPK(processId,userId);
			newversion=this.getLastVersionNumber(processId)+1;
		} catch (WProcessDefException e) {
			String mess = "Error cloning process version: can't get original process version id:"+processId
							+" - "+e.getMessage()+" - "+e.getCause();
			throw new WProcessDefException(mess);
		} catch (WStepSequenceDefException e) {
			String mess = "Error cloning process version: can't get original process version id:"+processId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepSequenceDefException(mess);
		}
		
		try {
			newprocver.setId(null);
			newprocver.setVersion(newversion); // new version number ....
			newprocver.setRolesRelated(new HashSet<WProcessRole>());
			newprocver.setUsersRelated(new HashSet<WProcessUser>());
			
			if ( procver.getRolesRelated().size()>0) {
				for ( WProcessRole processRole: procver.getRolesRelated() ) {
					processRole.setProcess(null);
					newprocver
						.addRole(
								processRole.getRole(), processRole.isAdmin(), 
								processRole.getIdObject(), processRole.getIdObjectType(), 
								userId);
				}
			}

			if ( procver.getUsersRelated().size()>0) {
				for ( WProcessUser processUser: procver.getUsersRelated() ) {
					processUser.setProcess(null);
					newprocver
						.addUser(
								processUser.getUser(), processUser.isAdmin(), 
								processUser.getIdObject(), processUser.getIdObjectType(), 
								userId);
				}
			}
			
		} catch (Exception e) {
			String mess = "Error cloning process version:"+processId+" can't clone related role or user list ..."
					+" - "+e.getClass()+" -" +e.getMessage()+" - "+e.getCause();
			throw new WProcessDefException(mess);
		}
			
		try {
			clonedId = this.add(newprocver,userId);
		} catch (WProcessDefException e) {
			String mess = "Error cloning process version: can't ADD new clone process version original-id:"+processId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WProcessDefException(mess);
		} catch (WProcessException e) {
			String mess = "Error cloning process version: can't ADD new clone process version original-id:"+processId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WProcessException(mess);
		}			
		
		// clone routes (the workflow map really ...)
		WStepSequenceDefBL seqBL = new WStepSequenceDefBL();
		try {
			routes = new WStepSequenceDefBL().getStepSequenceList(processId, userId);
			if (routes.size()>0){
				for (WStepSequenceDef route: routes) {
					route.setProcess(new WProcessDef());
					route.getProcess().setId(clonedId);
					seqBL.add(route, userId); // insert new cloned route
					logger.debug("inserted new route:"+route.getId()+":"+route.getProcess().getId()+" user:"+userId);
				}
			}			
		} catch (WStepSequenceDefException e) {
			String mess = "Error cloning routes for old process id:"+processId+"  newProcessId:"+newprocver
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepSequenceDefException(mess);
		}

		
		return clonedId;
	}
	
	// dml 20130129
	public boolean userIsProcessAdmin(Integer userId, Integer processId) throws WProcessDefException{
		
		return new WProcessDefDao().userIsProcessAdmin(userId, processId);
		
	}
	
	
	// nes 20130502 - traido desde el backing bean ...
	private List<WStepDef> loadStepList(Integer processId, Integer userId) throws WStepDefException {

		List<WStepDef> lsteps=new ArrayList<WStepDef>();
		try {

			if (processId != null && processId != 0) {

				lsteps = new WStepDefBL().getStepDefs(processId, userId);

			}
		} catch (WStepDefException ex1) {
			
			lsteps=null;
			String mess = "Error loading step list:"+ ex1.getMessage() + " - " + ex1.getCause();
			throw new WStepDefException(mess);

		}
		
		return lsteps;
	}
	
	private List<WStepSequenceDef> loadStepSequenceList(Integer processId, Integer currentUserId) 
			throws WStepSequenceDefException{
		
		List<WStepSequenceDef> routes = new ArrayList<WStepSequenceDef>();
		try {

			if (processId != null && processId != 0) {

				// dml 20120326 NOTA: LA VERSION ESTA HARDCODEADA A 1
				routes = new WStepSequenceDefBL().getStepSequenceList(processId, currentUserId);

//				DAVID: ESTO ESTABA AQUI EN EN BACKING BEAN Y LO DEJÉ COMENTADO PARA Q TE SITUES PERO ENTIENDO Q HAY QUE QUITARLO
//				PORQUE ESTO SI Q ES PROPIO DEL BACKING BEAN NO?				
//				this.stepOutgoings = false;
//				this.stepIncomings = false;
				
			}
			
		} catch (WStepSequenceDefException ex1) {

			routes=null;
			String mess = "Error loading StepSequenceList:"+ ex1.getMessage() + " - " + ex1.getCause();
			throw new WStepSequenceDefException(mess);

		}
		return routes;
	}
}
	