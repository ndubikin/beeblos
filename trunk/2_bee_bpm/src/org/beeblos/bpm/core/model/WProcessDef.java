package org.beeblos.bpm.core.model;

import static com.sp.common.util.ConstantsCommon.EMPTY_OBJECT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.beeblos.bpm.core.model.noper.WProcessDefThin;
import org.joda.time.DateTime;

import com.sp.common.core.model.SystemObject;


/**
 * WProcessDef generated by hbm2java
 */
public class WProcessDef implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private WProcessHead process;
	private Integer version;
	
	private boolean active;
	
	/**
	 * Indicate this process allow multiples alive instances running for 1 idObject
	 */
	private boolean allowedMultipleInstances; // indicates this process
	
	/**
	 * If true then indicates the objects must be injected in any step.
	 * If false, only steps with allowedInjection property must be able to accept new works
	 */
	private boolean allowedStartAtAnyPoint; 
	
	
	private DateTime productionDate;
	private Integer productionUser;
	
	private String comments;
	
	private WStepDef beginStep;
	
	private String idListZone; // pag que define la lista de seleccion de steps
	private String idWorkZone; // pag que define la zona de trabajo
	private String idAdditionalZone; // pag que define la zona de ayuda ( info adicional )
	
	private String idProcessorStep; // dml 20120619

	// dml 20120105
	private String adminEmail;

	private Integer totalTime;
	private WTimeUnit totalTimeUnit;
	private String globalDeadlineDate;
	
	/**
	 * email account for administrative tasks related with this process like send notification
	 * emails, etc.
	 *  
	 * dml 20120223
	 * 
	 */
	private WEmailAccount systemEmailAccount;
	
	// dml 20120306
	private WEmailTemplates arrivingAdminNoticeTemplate;
	private WEmailTemplates arrivingUserNoticeTemplate;
		
	private DateTime insertDate;
	private Integer insertUser;
	private DateTime modDate;
	private Integer modUser;
	
	// MANY2MANY
	private Set<WProcessRole> rolesRelated = new HashSet<WProcessRole>();
	private Set<WProcessUser> usersRelated = new HashSet<WProcessUser>();
	
//	// nes 20130502 - traje desde backing bean ...
//	// se carga "a mano" en la BL pues no está mapeado por hibernate
//	private List<WStepDef> lSteps = new ArrayList<WStepDef>();
	/**
	 * Step list (task list) used by this process.
	 * 
	 * nes 20141027: added relation tabla w_process_step_def and loaded step
	 * list directly from the relation  
	 */
	private Set<WStepDef> steps = new HashSet<WStepDef>();
	
	private List<WStepSequenceDef> stepSequenceList; 
	
	private Set<SystemObject> systemObject = new HashSet<SystemObject>(0);

//	private ManagedData managedDataDef;
	
	private String processMap; // dml 20120105

	public WProcessDef() {
		super();
	}

	
	public WProcessDef(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.process = new WProcessHead();
			this.beginStep = new WStepDef( EMPTY_OBJECT );
			this.systemEmailAccount = new WEmailAccount(EMPTY_OBJECT);
			this.arrivingAdminNoticeTemplate = new WEmailTemplates(EMPTY_OBJECT);
			this.arrivingUserNoticeTemplate = new WEmailTemplates(EMPTY_OBJECT);
		}	
	}
	
	public WProcessDef(String comments, WStepDef beginStep,
			DateTime fechaAlta, DateTime fechaModificacion) {

		this.comments = comments;
		this.beginStep = beginStep;
		this.insertDate = fechaAlta;
		this.modDate = fechaModificacion;
	}

	public WProcessDef(String comments, WStepDef beginStep,
			String idListZone, String idWorkZone, String idAdditionalZone,
			DateTime insertDate, Integer insertUser, DateTime modDate,
			Integer modUser, String adminEmail ) {
		this.comments = comments;
		this.beginStep = beginStep;
		this.idListZone = idListZone;
		this.idWorkZone = idWorkZone;
		this.idAdditionalZone = idAdditionalZone;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
		this.adminEmail = adminEmail;
	}
	
	@Deprecated
	public String getName(){
		
		if (this.process != null){
			return this.process.getName();
		}
		
		return null;
		
	}

	@Deprecated
	public void setName(String name){
		
		if (this.process != null){
			 this.process.setName(name);
		}
				
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WProcessHead getProcess() {
		return this.process;
	}

	public void setProcess(WProcessHead process) {
		this.process = process;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the beginStep
	 */
	public WStepDef getBeginStep() {
		return beginStep;
	}

	/**
	 * @param beginStep the beginStep to set
	 */
	public void setBeginStep(WStepDef beginStep) {
		this.beginStep = beginStep;
	}

	public String getIdListZone() {
		return this.idListZone;
	}

	public void setIdListZone(String idListZone) {
		this.idListZone = idListZone;
	}

	public String getIdWorkZone() {
		return this.idWorkZone;
	}

	public void setIdWorkZone(String idWorkZone) {
		this.idWorkZone = idWorkZone;
	}

	public String getIdAdditionalZone() {
		return this.idAdditionalZone;
	}

	public void setIdAdditionalZone(String idAdditionalZone) {
		this.idAdditionalZone = idAdditionalZone;
	}


	
	public String getIdProcessorStep() {
		return idProcessorStep;
	}


	public void setIdProcessorStep(String idProcessorStep) {
		this.idProcessorStep = idProcessorStep;
	}

	public List<WProcessRole> getRolesRelatedAsList() {
		if (this.rolesRelated != null){
			return new ArrayList<WProcessRole>(this.rolesRelated);
		}
		return null;
	}

	public Set<WProcessRole> getRolesRelated() {
		return rolesRelated;
	}

	public void setRolesRelated(Set<WProcessRole> rolesRelated) {
		this.rolesRelated = rolesRelated;
	}

	public List<WProcessUser> getUsersRelatedAsList() {
		if (this.usersRelated != null){
			return new ArrayList<WProcessUser>(this.usersRelated);
		}
		return null;
	}

	public Set<WProcessUser> getUsersRelated() {
		return usersRelated;
	}

	public void setUsersRelated(Set<WProcessUser> usersRelated) {
		this.usersRelated = usersRelated;
	}



	public DateTime getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}

	public Integer getInsertUser() {
		return insertUser;
	}

	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	public DateTime getModDate() {
		return modDate;
	}

	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}

	public Integer getModUser() {
		return modUser;
	}

	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isAllowedMultipleInstances() {
		return allowedMultipleInstances;
	}

	public void setAllowedMultipleInstances(boolean allowsMultipleInstances) {
		this.allowedMultipleInstances = allowsMultipleInstances;
	}

	public boolean isAllowedStartAtAnyPoint() {
		return allowedStartAtAnyPoint;
	}

	public void setAllowedStartAtAnyPoint(boolean allowedStartAtAnyPoint) {
		this.allowedStartAtAnyPoint = allowedStartAtAnyPoint;
	}

	public DateTime getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(DateTime productionDate) {
		this.productionDate = productionDate;
	}


	public Integer getProductionUser() {
		return productionUser;
	}


	public void setProductionUser(Integer productionUser) {
		this.productionUser = productionUser;
	}


	public Integer getTotalTime() {
		return totalTime;
	}


	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}


	public WTimeUnit getTotalTimeUnit() {
		return totalTimeUnit;
	}


	public void setTotalTimeUnit(WTimeUnit totalTimeUnit) {
		this.totalTimeUnit = totalTimeUnit;
	}


	public String getGlobalDeadlineDate() {
		return globalDeadlineDate;
	}


	public void setGlobalDeadlineDate(String globalDeadlineDate) {
		this.globalDeadlineDate = globalDeadlineDate;
	}


	public WEmailAccount getSystemEmailAccount() {
		return systemEmailAccount;
	}


	public void setSystemEmailAccount(WEmailAccount systemEmailAccount) {
		this.systemEmailAccount = systemEmailAccount;
	}


	public WEmailTemplates getArrivingAdminNoticeTemplate() {
		return arrivingAdminNoticeTemplate;
	}


	public void setArrivingAdminNoticeTemplate(
			WEmailTemplates arrivingAdminNoticeTemplate) {
		this.arrivingAdminNoticeTemplate = arrivingAdminNoticeTemplate;
	}


	public WEmailTemplates getArrivingUserNoticeTemplate() {
		return arrivingUserNoticeTemplate;
	}


	public void setArrivingUserNoticeTemplate(
			WEmailTemplates arrivingUserNoticeTemplate) {
		this.arrivingUserNoticeTemplate = arrivingUserNoticeTemplate;
	}

	@Deprecated // nes 20141027 - deprecated by steps
	public List<WStepDef> getlSteps() {
		return getWStepDefAsList();
	}

	public List<WStepDef> getWStepDefAsList() {
		return new ArrayList<WStepDef>(steps);
	}

	/**
	 * @return the steps
	 */
	public Set<WStepDef> getSteps() {
		return steps;
	}


	/**
	 * @param steps the steps to set
	 */
	public void setSteps(Set<WStepDef> steps) {
		this.steps = steps;
	}


	public List<WStepSequenceDef> getStepSequenceList() {
		return stepSequenceList;
	}


	public void setStepSequenceList(List<WStepSequenceDef> stepSequenceList) {
		this.stepSequenceList = stepSequenceList;
	}


	public String getProcessMap() {
		return processMap;
	}

	public void setProcessMap(String processMap) {
		this.processMap = processMap;
	}

//	public ManagedData getManagedDataDef() {
//		return managedDataDef;
//	}

	public Set<SystemObject> getSystemObject() {
		return systemObject;
	}
	
	public List<SystemObject> getSystemObjectList() {

		if (systemObject != null){
			return new ArrayList<SystemObject>(systemObject);
		}
		return null;
	}
	
	public void setSystemObject(Set<SystemObject> systemObject) {
		this.systemObject = systemObject;
	}

//	public void setManagedDataDef(ManagedData managedDataDef) {
//		this.managedDataDef = managedDataDef;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((adminEmail == null) ? 0 : adminEmail.hashCode());
		result = prime
				* result
				+ ((arrivingAdminNoticeTemplate == null) ? 0
						: arrivingAdminNoticeTemplate.hashCode());
		result = prime
				* result
				+ ((arrivingUserNoticeTemplate == null) ? 0
						: arrivingUserNoticeTemplate.hashCode());
		result = prime * result
				+ ((beginStep == null) ? 0 : beginStep.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime
				* result
				+ ((globalDeadlineDate == null) ? 0 : globalDeadlineDate
						.hashCode());
		result = prime * result
				+ ((process == null) ? 0 : process.hashCode());
		result = prime * result
				+ ((version == null) ? 0 : version.hashCode());
		result = prime
				* result
				+ ((idAdditionalZone == null) ? 0 : idAdditionalZone.hashCode());
		result = prime
				* result
				+ ((idProcessorStep == null) ? 0 : idProcessorStep.hashCode());
		result = prime * result
				+ ((idListZone == null) ? 0 : idListZone.hashCode());
		result = prime * result
				+ ((idWorkZone == null) ? 0 : idWorkZone.hashCode());
		result = prime * result
				+ ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result
				+ ((productionUser == null) ? 0 : productionUser.hashCode());
/* dml 20141029 COMENTADO POR ERRORES DE DUPLICIDAD
		result = prime * result
				+ ((rolesRelated == null) ? 0 : rolesRelated.hashCode());
*/
		result = prime * result
				+ ((totalTime == null) ? 0 : totalTime.hashCode());
		result = prime * result
				+ ((totalTimeUnit == null) ? 0 : totalTimeUnit.hashCode());
/* dml 20141029 COMENTADO POR ERRORES DE DUPLICIDAD
		result = prime * result
				+ ((usersRelated == null) ? 0 : usersRelated.hashCode());
*/
		result = prime
				* result
				+ ((systemEmailAccount == null) ? 0 : systemEmailAccount
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WProcessDef other = (WProcessDef) obj;
		if (active != other.active)
			return false;
		if (adminEmail == null) {
			if (other.adminEmail != null)
				return false;
		} else if (!adminEmail.equals(other.adminEmail))
			return false;
		if (arrivingAdminNoticeTemplate == null) {
			if (other.arrivingAdminNoticeTemplate != null)
				return false;
		} else if (!arrivingAdminNoticeTemplate
				.equals(other.arrivingAdminNoticeTemplate))
			return false;
		if (arrivingUserNoticeTemplate == null) {
			if (other.arrivingUserNoticeTemplate != null)
				return false;
		} else if (!arrivingUserNoticeTemplate
				.equals(other.arrivingUserNoticeTemplate))
			return false;
		if (beginStep == null) {
			if (other.beginStep != null)
				return false;
		} else if (!beginStep.equals(other.beginStep))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (globalDeadlineDate == null) {
			if (other.globalDeadlineDate != null)
				return false;
		} else if (!globalDeadlineDate.equals(other.globalDeadlineDate))
			return false;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (idAdditionalZone == null) {
			if (other.idAdditionalZone != null)
				return false;
		} else if (!idAdditionalZone.equals(other.idAdditionalZone))
			return false;
		if (idProcessorStep == null) {
			if (other.idProcessorStep != null)
				return false;
		} else if (!idProcessorStep.equals(other.idProcessorStep))
			return false;
		if (idListZone == null) {
			if (other.idListZone != null)
				return false;
		} else if (!idListZone.equals(other.idListZone))
			return false;
		if (idWorkZone == null) {
			if (other.idWorkZone != null)
				return false;
		} else if (!idWorkZone.equals(other.idWorkZone))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (productionUser == null) {
			if (other.productionUser != null)
				return false;
		} else if (!productionUser.equals(other.productionUser))
			return false;
/* dml 20141029 COMENTADO POR ERRORES DE DUPLICIDAD
		if (rolesRelated == null) {
			if (other.rolesRelated != null)
				return false;
		} else if (!rolesRelated.equals(other.rolesRelated))
			return false;
*/
		if (totalTime == null) {
			if (other.totalTime != null)
				return false;
		} else if (!totalTime.equals(other.totalTime))
			return false;
		if (totalTimeUnit == null) {
			if (other.totalTimeUnit != null)
				return false;
		} else if (!totalTimeUnit.equals(other.totalTimeUnit))
			return false;
/* dml 20141029 COMENTADO POR ERRORES DE DUPLICIDAD
		if (usersRelated == null) {
			if (other.usersRelated != null)
				return false;
		} else if (!usersRelated.equals(other.usersRelated))
			return false;
*/			
		if (systemEmailAccount == null) {
			if (other.systemEmailAccount != null)
				return false;
		} else if (!systemEmailAccount.equals(other.systemEmailAccount))
			return false;
		if (processMap == null) {
			if (other.processMap != null)
				return false;
		} else if (!processMap.equals(other.processMap))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WProcessDef [id=" + id + "process=" + process + ", version=" + version
				+ ", active=" + active + ", productionDate=" + productionDate
				+ ", productionUser=" + productionUser + ", comments=" + comments + ", beginStep="
				+ beginStep + ", idListZone=" + idListZone + ", idWorkZone=" + idWorkZone
				+ ", idAdditionalZone=" + idAdditionalZone + ", idProcessorStep=" + idProcessorStep
				+ ", adminEmail=" + adminEmail + ", totalTime=" + totalTime + ", totalTimeUnit="
				+ totalTimeUnit + ", globalDeadlineDate=" + globalDeadlineDate
				+ ", systemEmailAccount=" + systemEmailAccount + ", arrivingAdminNoticeTemplate="
				+ arrivingAdminNoticeTemplate + ", arrivingUserNoticeTemplate="
				+ arrivingUserNoticeTemplate + ", insertDate=" + insertDate + ", insertUser="
				+ insertUser + ", modDate=" + modDate + ", modUser=" + modUser + ", rolesRelated="
				+ rolesRelated + ", usersRelated=" + usersRelated + "]";
	}
	

	public boolean empty() {

		if (id!=null && !id.equals(0)) return false;

		if (process!=null && !process.empty()) return false;
		if (version!=null && !version.equals(0)) return false;

		if (comments!=null && ! "".equals(comments)) return false;
		
		if (beginStep!=null && ! beginStep.empty()) return false;
		
		if (idListZone!=null && ! "".equals(idListZone)) return false;
		if (idWorkZone!=null && ! "".equals(idWorkZone)) return false;
		if (idAdditionalZone!=null && ! "".equals(idAdditionalZone)) return false;
		if (adminEmail!=null && ! "".equals(adminEmail)) return false;
		
		// dml 20120306
		if (arrivingAdminNoticeTemplate!=null && ! arrivingAdminNoticeTemplate.empty()) return false;
		if (arrivingUserNoticeTemplate!=null && ! arrivingUserNoticeTemplate.empty()) return false;
		
		if (idProcessorStep!=null && ! "".equals(idProcessorStep)) return false; // dml 20120619

		if (processMap != null && ! "".equals(processMap)) return false; // dml 20130628

		return true;
	}
	

	public void addRole( WRoleDef role, boolean admin, Integer idObject, String idObjectType, Integer insertUser ) {
		WProcessRole wpr = new WProcessRole(admin, idObject, idObjectType, insertUser, new DateTime() );
//		wpr.setProcess(this);
		wpr.setRole(role);
		rolesRelated.add(wpr);
	}
	
	public void addUser( WUserDef user, boolean admin, Integer idObject, String idObjectType, Integer insertUser ) {
		WProcessUser wpu = new WProcessUser(admin, idObject, idObjectType, insertUser, new DateTime() );
//		wpu.setProcess(this);
		wpu.setUser(user);
		usersRelated.add(wpu);
	}

	
	public WProcessDefThin getAsProcessDefThin() {
		return new WProcessDefThin(
				id,process,
				active,
				productionDate,
				productionUser,
				comments,
				(beginStep != null)?beginStep.getId():null,
				idListZone, idWorkZone,idAdditionalZone,
				idProcessorStep,
				adminEmail,
				totalTime,
				totalTimeUnit,
				globalDeadlineDate,
				insertDate,  insertUser,  modDate,  modUser,
				systemEmailAccount,
				arrivingAdminNoticeTemplate,
				arrivingUserNoticeTemplate);
	}
	
	/**
	 * @author rrl 20141022
	 * 
	 * nullates empty objects to persist
	 */
	public void nullateEmtpyObjects() {
	   
		if (process!=null && process.empty()) process=null;
		if (beginStep!=null && beginStep.empty()) beginStep=null;
		if (totalTimeUnit!=null && totalTimeUnit.empty()) totalTimeUnit=null;
		if (systemEmailAccount!=null && systemEmailAccount.empty()) systemEmailAccount=null;
		if (arrivingAdminNoticeTemplate!=null && arrivingAdminNoticeTemplate.empty()) arrivingAdminNoticeTemplate=null;
		if (arrivingUserNoticeTemplate!=null && arrivingUserNoticeTemplate.empty()) arrivingUserNoticeTemplate=null;
		if (rolesRelated!=null && rolesRelated.isEmpty()) rolesRelated=null;
		if (usersRelated!=null && usersRelated.isEmpty()) usersRelated=null;
//		if (lSteps!=null && lSteps.isEmpty()) lSteps=null;
		if (steps!=null && steps.isEmpty()) steps=null;
		if (stepSequenceList!=null && stepSequenceList.isEmpty()) stepSequenceList=null;
		if (systemObject!=null && systemObject.isEmpty()) systemObject=null;
	}
	
    /**
     * @author rrl 20141022
     * 
     * recover empty objects to persist
     */
    public void recoverEmtpyObjects() {

		if (process==null) process = new WProcessHead(EMPTY_OBJECT);
		if (beginStep==null) beginStep = new WStepDef(EMPTY_OBJECT);
		if (totalTimeUnit==null) totalTimeUnit = new WTimeUnit(EMPTY_OBJECT);
		if (systemEmailAccount==null) systemEmailAccount = new WEmailAccount(EMPTY_OBJECT);
		if (arrivingAdminNoticeTemplate==null) arrivingAdminNoticeTemplate = new WEmailTemplates(EMPTY_OBJECT);
		if (arrivingUserNoticeTemplate==null) arrivingUserNoticeTemplate = new WEmailTemplates(EMPTY_OBJECT);
		if (rolesRelated==null) rolesRelated = new HashSet<WProcessRole>();
		if (usersRelated==null) usersRelated = new HashSet<WProcessUser>();
		//if (lSteps==null) lSteps = new ArrayList<WStepDef>();
		if (steps==null) steps = new HashSet<WStepDef>();
		if (stepSequenceList==null) stepSequenceList = new ArrayList<WStepSequenceDef>();
		if (systemObject==null) systemObject = new HashSet<SystemObject>();
    }
	
}
