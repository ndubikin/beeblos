package org.beeblos.bpm.core.model;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Tools 3.3.0.GA

import static com.sp.common.util.ConstantsCommon.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.W_SYSROLE_ORIGINATOR_ID;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.beeblos.bpm.core.graph.MxCell;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * WStepDef generated by hbm2java
 * 
 * DEFINE EL PASO EN SI, LAS PÁGINAS RELACIONADAS, LAS RESPUESTAS POSIBLES
 */
@XmlAccessorType(XmlAccessType.NONE)
public class WStepDef implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	
	/**
	 * This variables are used in the conversion from and to xml with jaxb
	 * 
	 * pab 04122014
	 */
	private String mxCellString;
	private MxCell mxCellObject;
	
	private String xmlHref;
	private String xmlId;
	private String xmlLabel;
	private String xmlRules;
	
	private String responsesString;
		
	/**
	 * stepHead is intended to version control of a step definition
	 * 
	 */
	private WStepHead stepHead;
	private Integer version;
	
	/**
	 * indicates this stepDef is active the environment where it is used
	 * Active only intends to create o relate it with new process, but does not
	 * change existing situations of existing processes (wProcessDef) nor 
	 * current works (wProcessWork/wStepWork)
	 */
	private boolean active;
	/**
	 * Indicates this stepDef is used by many process definitions (wProcessDef)
	 */
	private boolean shared;
	/**
	 * Indicates this wStepDef is deleted but there is necessary 
	 */
	private boolean deleted; // dml 20130830
	
	/**
	 * obsolete field
	 */
	@Deprecated
	private Integer idDept;

	/**
	 * obsolete field
	 */
	@Deprecated
	private Integer idPhase;
	private String instructions;
	
	private String stepComments;
	/**
	 * JSF link field
	 */
	private String idListZone;
	/**
	 * JSF link field
	 */
	private String idWorkZone;
	/**
	 * JSF link field
	 */
	private String idAdditionalZone;
	
	/**
	 * id / may be URL for a web app for the default processor for this step.
	 * If bee-bpm is used in a web application the url for default processor must be
	 * built with a root url + this field + instance specific parameters.
	 * Any of this 3 parts may be blank ...
	 * If this field is blank the BL will be use the WProcessDef.idProcessorStep
	 * nes 20141127
	 */
	private String idDefaultProcessor; // dml 20120619
	
	/**
	 * NOTA NES: VER PARA QUE SIRVE O QUE USO TENIAMOS PREVISTO PARA ESTE CAMPO ...
	 */
	private String submitForm; // submit instructions
	
	/**
	 * Time unit used to set the assignedTime
	 */
	private WTimeUnit timeUnit;
	
	/**
	 * Assigned time in timeUnit units
	 */
	private Integer assignedTime;
	
	private LocalDate deadlineDate;

	private LocalTime deadlineTime;

	/**
	 * Time unit used to set the reminderTime
	 */	
	private WTimeUnit reminderTimeUnit;

	/**
	 * Assigned time in timeUnit units
	 */
	private Integer reminderTime; // en unidades de tiempo indicadas en reminderTimeUnit

	/**
	 * if true then the deadlines would be modified at runtime
	 */
	boolean runtimeModifiable; // si se pueden modificar los deadline y eso en runtime ...
	
	/**
	 * indicates mandatory notifications ( not user configurable )
	 * All 'false' flags indicates optional ( or user configurable ) notifications
	 */
	private boolean sentAdminNotice;
	private boolean arrivingAdminNotice;
	private boolean deadlineAdminNotice;
	private boolean reminderAdminNotice;
	private boolean expiredAdminNotice;
	private boolean sentUserNotice;
	private boolean arrivingUserNotice;
	private boolean deadlineUserNotice;
	private boolean reminderUserNotice;
	private boolean expiredUserNotice;
	
	/**
	 * 
	 */
	private boolean emailNotification;
	/**
	 * Indicates engine must notificate state changes 
	 */
	private boolean engineNotification;
	
	/**
	 * indicates how the analysis of outgoing routes of a task must be evaluated:
	 * 
	 * A - all true condition > will be start each route with has a true condition
	 * F - first true condition > will start only the first evaluated route with true condition
	 * 
	 */
	private Character routeEvalOrder;
	
	/**
	 * Valid responses list
	 */
	private Set<WStepResponseDef> response = new HashSet<WStepResponseDef>();
//	private Set<WStepAssignedDef> assigned = new HashSet<WStepAssignedDef>();
	
	// at design time the designer may be associate some roles or users to the step
	// MANY2MANY
	/**
	 * roles related with this step ( roles that have permissions to work / admin with 
	 * the step)
	 */
	Set<WStepRole> rolesRelated=new HashSet<WStepRole>();
	/**
	 * users related with this step ( users that have permissions to work / admin with 
	 * the step)
	 */
	Set<WStepUser> usersRelated=new HashSet<WStepUser>();
	
	/**
	 * indicates the instance (wStepWork) belongs this wStepDef must obtain allowed users
	 * (workers) at runtime from an external source (executing WExternalMethod indicated 
	 * by this id) 
	 * 
	 * The external method must be defined or declared as WExternalMethod 
	 * at WProcessDef/WProcessHead level.
	 * About the meaning of this scenario, at runtime, the external method must be 
	 * executed next to the creation of the new wStepWork related with 
	 * this wStepDef ...
	 * 
	 * required: external method must return an int[] with valid user ids 
	 * 
	 * nes 20140705
	 * 
	 */
	private Integer idUserAssignmentMethod;
	
	// dml 20120217
	private boolean customValidation;
	private String customValidationRefClass;
	private String customValidationMethod;
	private boolean backingBean;

	private String customSaveMethod;
	private String customSaveRefClass;
	
	// dml 20130727
	private String rules;
	private String preconditions;
	private String postconditions;

	/**
	 * data fields exposed for this step (must be showed, updated, loaded...)
	 * 
	 * The data fields exposed at step level must be defined at process level...
	 * 
	 * dml 20130821
	 */
	List<WStepDataField> dataFieldDef = new ArrayList<WStepDataField>();

	// dml 20120113
	private DateTime insertDate;
	private Integer insertUser;
	private DateTime modDate;
	private Integer modUser;

	public WStepDef() {
	}

	public WStepDef(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.stepHead = new WStepHead();
			this.timeUnit = new WTimeUnit( EMPTY_OBJECT );
			this.reminderTimeUnit = new WTimeUnit( EMPTY_OBJECT );
			
		}	
	}

	public WStepDef(Integer id){
		super();
		this.id = id;
	}

	/**
	 * nes 20141206 - added version as parameter (mandatory field ...)
	 * @param id
	 * @param version
	 * @param idDept
	 * @param idPhase
	 * @param instructions
	 * @param stepComments
	 * @param idListZone
	 * @param idWorkZone
	 * @param idAdditionalZone
	 */
	public WStepDef(Integer id, Integer version, Integer idDept, Integer idPhase,
			String instructions, String stepComments, String idListZone,
			String idWorkZone, String idAdditionalZone/*,
			Set<WStepResponseDef> response*/) {
		super();
		this.id = id;
		this.version=version;
		this.idDept = idDept;
		this.idPhase = idPhase;
		this.instructions = instructions;
		this.stepComments = stepComments;
		this.idListZone = idListZone;
		this.idWorkZone = idWorkZone;
		this.idAdditionalZone = idAdditionalZone;
//		this.response = response;
	}


	@XmlAttribute(name="spId")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Deprecated
	public String getName(){
		
		if (this.stepHead != null){
			return this.stepHead.getName();
		}
		
		return null;
		
	}

	@Deprecated
	public void setName(String name){
		
		if (this.stepHead != null){
			 this.stepHead.setName(name);
		}
				
	}

	public Integer getIdDept() {
		return this.idDept;
	}

	public WStepHead getStepHead() {
		return stepHead;
	}

	public void setStepHead(WStepHead stepHead) {
		this.stepHead = stepHead;
	}

	@XmlAttribute(name="version")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setIdDept(Integer idDept) {
		this.idDept = idDept;
	}

	public Integer getIdPhase() {
		return this.idPhase;
	}

	public void setIdPhase(Integer idPhase) {
		this.idPhase = idPhase;
	}

	@XmlAttribute(name="description")
	public String getStepComments() {
		return this.stepComments;
	}

	public void setStepComments(String stepComments) {
		this.stepComments = stepComments;
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

	/**
	 * id / may be URL for a web app for the default processor for this step.
	 * If bee-bpm is used in a web application the url for default processor must be
	 * built with a root url + this field + instance specific parameters.
	 * Any of this 3 parts may be blank ...
	 * If this field is blank the BL will be use the WProcessDef.idProcessorStep
	 * nes 20141127
	 */
	public String getIdDefaultProcessor() {
		return idDefaultProcessor;
	}

	/**
	 * id / may be URL for a web app for the default processor for this step.
	 * If bee-bpm is used in a web application the url for default processor must be
	 * built with a root url + this field + instance specific parameters.
	 * Any of this 3 parts may be blank ...
	 * If this field is blank the BL will be use the WProcessDef.idProcessorStep
	 * nes 20141127
	 */
	public void setIdDefaultProcessor(String idDefaultProcessor) {
		this.idDefaultProcessor = idDefaultProcessor;
	}

	public String getSubmitForm() {
		return submitForm;
	}

	public void setSubmitForm(String submitForm) {
		this.submitForm = submitForm;
	}

	public List<WStepResponseDef> getResponseAsList() {
		if (this.response != null){
			return new ArrayList<WStepResponseDef>(this.response);
		}
		return null;
	}

	public Set<WStepResponseDef> getResponse() {
		return response;
	}

	public void setResponse(Set<WStepResponseDef> response) {
		this.response = response;
	}

	/**
	 * @return the instructions
	 */
	@XmlAttribute(name="instructions")
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}



	/**
	 * @return the assignedTime
	 */
	public Integer getAssignedTime() {
		return assignedTime;
	}



	/**
	 * @param assignedTime the assignedTime to set
	 */
	public void setAssignedTime(Integer assignedTime) {
		this.assignedTime = assignedTime;
	}



	/**
	 * @return the deadlineDate
	 */
	public LocalDate getDeadlineDate() {
		return deadlineDate;
	}



	/**
	 * @param deadlineDate the deadlineDate to set
	 */
	public void setDeadlineDate(LocalDate deadlineDate) {
		this.deadlineDate = deadlineDate;
	}



	/**
	 * @return the deadlineTime
	 */
	public LocalTime getDeadlineTime() {
		return deadlineTime;
	}



	/**
	 * @param deadlineTime the deadlineTime to set
	 */
	public void setDeadlineTime(LocalTime deadlineTime) {
		this.deadlineTime = deadlineTime;
	}



	/**
	 * @return the reminderTimeUnit
	 */
	public WTimeUnit getReminderTimeUnit() {
		return reminderTimeUnit;
	}



	/**
	 * @param reminderTimeUnit the reminderTimeUnit to set
	 */
	public void setReminderTimeUnit(WTimeUnit reminderTimeUnit) {
		this.reminderTimeUnit = reminderTimeUnit;
	}



	/**
	 * @return the reminderTime
	 */
	public Integer getReminderTime() {
		return reminderTime;
	}



	/**
	 * @param reminderTime the reminderTime to set
	 */
	public void setReminderTime(Integer reminderTime) {
		this.reminderTime = reminderTime;
	}



	/**
	 * @return the runtimeModifiable
	 */
	public boolean isRuntimeModifiable() {
		return runtimeModifiable;
	}



	/**
	 * @param runtimeModifiable the runtimeModifiable to set
	 */
	public void setRuntimeModifiable(boolean runtimeModifiable) {
		this.runtimeModifiable = runtimeModifiable;
	}



	/**
	 * @return the timeUnit
	 */
	public WTimeUnit getTimeUnit() {
		return timeUnit;
	}



	/**
	 * @param timeUnit the timeUnit to set
	 */
	public void setTimeUnit(WTimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		if (idDept!=null && ! idDept.equals(0)) return false;
		if (idPhase!=null && ! idPhase.equals(0)) return false;
		if (assignedTime!=null && ! assignedTime.equals(0)) return false;
		if (reminderTime!=null && ! reminderTime.equals(0)) return false;
	
		if (version!=null && ! version.equals(0)) return false;
		if (stepHead!=null && ! stepHead.empty()) return false;
		if (instructions!=null && ! "".equals(instructions)) return false;
		if (stepComments!=null && ! "".equals(stepComments)) return false;
		
		if (idListZone!=null && ! "".equals(idListZone)) return false;
		if (idWorkZone!=null && ! "".equals(idWorkZone)) return false;
		if (idAdditionalZone!=null && ! "".equals(idAdditionalZone)) return false;

		if (idDefaultProcessor!=null && ! "".equals(idDefaultProcessor)) return false;

		if (timeUnit!=null && ! timeUnit.empty()) return false;
		if (reminderTimeUnit!=null && ! reminderTimeUnit.empty()) return false;

		if (deadlineDate!=null ) return false;
		if (deadlineTime!=null ) return false;
		
		if (customValidationRefClass!=null && ! "".equals(customValidationRefClass)) return false;
		if (customValidationMethod!=null && ! "".equals(customValidationMethod)) return false;

		if (customSaveMethod!=null && ! "".equals(customSaveMethod)) return false;
		if (customSaveRefClass!=null && ! "".equals(customSaveRefClass)) return false;

		return true;
	}

	public List<WStepRole> getRolesRelatedAsList() {
		if (this.rolesRelated != null){
			return new ArrayList<WStepRole>(this.rolesRelated);
		}
		return null;
	}

	public Set<WStepRole> getRolesRelated() {
		return rolesRelated;
	}

	public void setRolesRelated(Set<WStepRole> rolesRelated) {
		this.rolesRelated = rolesRelated;
	}

	public List<WStepUser> getUsersRelatedAsList() {
		if (this.usersRelated != null){
			return new ArrayList<WStepUser>(this.usersRelated);
		}
		return null;
	}

	public Set<WStepUser> getUsersRelated() {
		return usersRelated;
	}

	public void setUsersRelated(Set<WStepUser> usersRelated) {
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
	
	

	public boolean isSentAdminNotice() {
		return sentAdminNotice;
	}

	public void setSentAdminNotice(boolean sentAdminNotice) {
		this.sentAdminNotice = sentAdminNotice;
	}

	public boolean isArrivingAdminNotice() {
		return arrivingAdminNotice;
	}

	public void setArrivingAdminNotice(boolean arrivingAdminNotice) {
		this.arrivingAdminNotice = arrivingAdminNotice;
	}

	public boolean isDeadlineAdminNotice() {
		return deadlineAdminNotice;
	}

	public void setDeadlineAdminNotice(boolean deadlineAdminNotice) {
		this.deadlineAdminNotice = deadlineAdminNotice;
	}

	public boolean isReminderAdminNotice() {
		return reminderAdminNotice;
	}

	public void setReminderAdminNotice(boolean reminderAdminNotice) {
		this.reminderAdminNotice = reminderAdminNotice;
	}

	public boolean isExpiredAdminNotice() {
		return expiredAdminNotice;
	}

	public void setExpiredAdminNotice(boolean expiredAdminNotice) {
		this.expiredAdminNotice = expiredAdminNotice;
	}

	public boolean isSentUserNotice() {
		return sentUserNotice;
	}

	public void setSentUserNotice(boolean sentUserNotice) {
		this.sentUserNotice = sentUserNotice;
	}

	public boolean isArrivingUserNotice() {
		return arrivingUserNotice;
	}

	public void setArrivingUserNotice(boolean arrivingUserNotice) {
		this.arrivingUserNotice = arrivingUserNotice;
	}

	public boolean isDeadlineUserNotice() {
		return deadlineUserNotice;
	}

	public void setDeadlineUserNotice(boolean deadlineUserNotice) {
		this.deadlineUserNotice = deadlineUserNotice;
	}

	public boolean isReminderUserNotice() {
		return reminderUserNotice;
	}

	public void setReminderUserNotice(boolean reminderUserNotice) {
		this.reminderUserNotice = reminderUserNotice;
	}

	public boolean isExpiredUserNotice() {
		return expiredUserNotice;
	}

	public void setExpiredUserNotice(boolean expiredUserNotice) {
		this.expiredUserNotice = expiredUserNotice;
	}

	public boolean isEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public boolean isEngineNotification() {
		return engineNotification;
	}

	public void setEngineNotification(boolean engineNotification) {
		this.engineNotification = engineNotification;
	}

	public Character getRouteEvalOrder() {
		return routeEvalOrder;
	}

	public void setRouteEvalOrder(Character routeEvalOrder) {
		this.routeEvalOrder = routeEvalOrder;
	}
/* dml 20141031 - AHORA SE USA SU PROPIA BL
	// nes 20111209
	public void addRole( WRoleDef role, boolean admin, Integer idObject, String idObjectType, Integer insertUser ) {
		WStepRole wsr = new WStepRole(admin, idObject, idObjectType, insertUser, new DateTime() );
		wsr.setStep(this);
		wsr.setRole(role);
		rolesRelated.add(wsr);
	}
	
	// nes 20111209
	public void addUser( WUserDef user, boolean admin, Integer idObject, String idObjectType, Integer insertUser ) {
		WStepUser wsu = new WStepUser(admin, idObject, idObjectType, insertUser, new DateTime() );
		wsu.setStep(this);
		wsu.setUser(user);
		usersRelated.add(wsu);
	}
*/
	// dml 20120110
	public void addResponse( WStepResponseDef wStepResponseDef ) {
		response.add(wStepResponseDef);
	}

	/**
	 * indicates the instance (wStepWork) belongs this wStepDef must obtain allowed users
	 * to method the step at runtime
	 * 
	 *  
	 * link to method to assign users. The method must be defined or declared as 
	 * externalMethod list at WProcessDef/WProcessHead level.
	 * About the meaning of this scenario, at runtime the external method must be 
	 * performed at the moment of the creation of the new wStepWork related with 
	 * this wStepDef ...
	 * 
	 * required: external method must return an int[] with valid user ids
	 * 
	 * 	 * nes 20140705
	 * 
	 * @return the idUserAssignmentMethod
	 */
	public Integer getIdUserAssignmentMethod() {
		return idUserAssignmentMethod;
	}

	/**
	 * @param idUserAssignmentMethod the idUserAssignmentMethod to set
	 */
	public void setIdUserAssignmentMethod(Integer idUserAssignmentMethod) {
		this.idUserAssignmentMethod = idUserAssignmentMethod;
	}
	/**
	 * returns true if users to work with this step must be assigned at runtime...
	 * nes 20140705
	 * @return
	 */
	public boolean isRuntimeAssignedUsers() {
		return idUserAssignmentMethod!=null && idUserAssignmentMethod!=0
				|| roleContainsSysroleOriginator();
	}
	
	/**
	 * checks if current roles for the step contains sysrole originator...
	 * nes 20141014
	 * @return
	 */
	private boolean roleContainsSysroleOriginator() {
		for (WStepRole wsr: this.getRolesRelatedAsList()){
			if (wsr.getRole().getId().equals(W_SYSROLE_ORIGINATOR_ID)){
				return true;
			}
		}
		return false;
	}

	public boolean isCustomValidation() {
		return customValidation;
	}
	
	public void setCustomValidation(boolean customValidation) {
		this.customValidation = customValidation;
	}

	public String getCustomValidationRefClass() {
		return customValidationRefClass;
	}

	public void setCustomValidationRefClass(String customValidationRefClass) {
		this.customValidationRefClass = customValidationRefClass;
	}

	public String getCustomValidationMethod() {
		return customValidationMethod;
	}

	public void setCustomValidationMethod(String customValidationMethod) {
		this.customValidationMethod = customValidationMethod;
	}

	public String getCustomSaveMethod() {
		return customSaveMethod;
	}

	public void setCustomSaveMethod(String customSaveMethod) {
		this.customSaveMethod = customSaveMethod;
	}

	public String getCustomSaveRefClass() {
		return customSaveRefClass;
	}

	public void setCustomSaveRefClass(String customSaveRefClass) {
		this.customSaveRefClass = customSaveRefClass;
	}

	public boolean isBackingBean() {
		return backingBean;
	}

	public void setBackingBean(boolean backingBean) {
		this.backingBean = backingBean;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public String getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(String preconditions) {
		this.preconditions = preconditions;
	}

	public String getPostconditions() {
		return postconditions;
	}

	public void setPostconditions(String postconditions) {
		this.postconditions = postconditions;
	}

	public List<WStepDataField> getDataFieldDef() {
		return dataFieldDef;
	}

	public void setDataFieldDef(List<WStepDataField> dataFieldDef) {
		this.dataFieldDef = dataFieldDef;
	}
	
	public List<WStepDataField> getStepDataFieldList() {

		if (dataFieldDef != null
				&& dataFieldDef.size() > 0) {

			List<WStepDataField> dfl = 
					new ArrayList<WStepDataField>(dataFieldDef.size()+1);
			
			dfl = new ArrayList<WStepDataField>(dataFieldDef);

			return dfl;
		}

		return null;

	}
	
	/**
	 * @author rrl 20141022
	 * 
	 * nullates empty objects to persist
	 */
	public void nullateEmtpyObjects() {

		if (stepHead!=null && stepHead.empty()) stepHead=null;
		if (timeUnit!=null && timeUnit.empty()) timeUnit=null;
		if (reminderTimeUnit!=null && reminderTimeUnit.empty()) reminderTimeUnit=null;
		if (response!=null && response.isEmpty()) response=null;
		if (rolesRelated!=null && rolesRelated.isEmpty()) rolesRelated=null;
		if (usersRelated!=null && usersRelated.isEmpty()) usersRelated=null;
		if (dataFieldDef!=null && dataFieldDef.isEmpty()) dataFieldDef=null;
	}
	
    /**
     * @author rrl 20141022
     * 
     * recover empty objects to persist
     */
    public void recoverEmtpyObjects() {

		if (stepHead==null) stepHead = new WStepHead();
		if (timeUnit==null) timeUnit = new WTimeUnit(EMPTY_OBJECT);
		if (reminderTimeUnit==null) reminderTimeUnit = new WTimeUnit(EMPTY_OBJECT);
		if (response==null) response = new HashSet<WStepResponseDef>();
		if (rolesRelated==null) rolesRelated = new HashSet<WStepRole>();
		if (usersRelated==null) usersRelated = new HashSet<WStepUser>();
		if (dataFieldDef==null) dataFieldDef = new ArrayList<WStepDataField>();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + (arrivingAdminNotice ? 1231 : 1237);
		result = prime * result + (arrivingUserNotice ? 1231 : 1237);
		result = prime * result
				+ ((assignedTime == null) ? 0 : assignedTime.hashCode());
		result = prime * result + (backingBean ? 1231 : 1237);
		result = prime
				* result
				+ ((customSaveMethod == null) ? 0 : customSaveMethod.hashCode());
		result = prime
				* result
				+ ((customSaveRefClass == null) ? 0 : customSaveRefClass
						.hashCode());
		result = prime * result + (customValidation ? 1231 : 1237);
		result = prime
				* result
				+ ((customValidationMethod == null) ? 0
						: customValidationMethod.hashCode());
		result = prime
				* result
				+ ((customValidationRefClass == null) ? 0
						: customValidationRefClass.hashCode());
		result = prime * result
				+ ((dataFieldDef == null) ? 0 : dataFieldDef.hashCode());
		result = prime * result + (deadlineAdminNotice ? 1231 : 1237);
		result = prime * result
				+ ((deadlineDate == null) ? 0 : deadlineDate.hashCode());
		result = prime * result
				+ ((deadlineTime == null) ? 0 : deadlineTime.hashCode());
		result = prime * result + (deadlineUserNotice ? 1231 : 1237);
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + (emailNotification ? 1231 : 1237);
		result = prime * result + (engineNotification ? 1231 : 1237);
		result = prime * result + (expiredAdminNotice ? 1231 : 1237);
		result = prime * result + (expiredUserNotice ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((idAdditionalZone == null) ? 0 : idAdditionalZone.hashCode());
		result = prime
				* result
				+ ((idDefaultProcessor == null) ? 0 : idDefaultProcessor
						.hashCode());
		result = prime * result + ((idDept == null) ? 0 : idDept.hashCode());
		result = prime * result
				+ ((idListZone == null) ? 0 : idListZone.hashCode());
		result = prime * result + ((idPhase == null) ? 0 : idPhase.hashCode());
		result = prime
				* result
				+ ((idUserAssignmentMethod == null) ? 0
						: idUserAssignmentMethod.hashCode());
		result = prime * result
				+ ((idWorkZone == null) ? 0 : idWorkZone.hashCode());
		result = prime * result
				+ ((instructions == null) ? 0 : instructions.hashCode());
		result = prime * result
				+ ((postconditions == null) ? 0 : postconditions.hashCode());
		result = prime * result
				+ ((preconditions == null) ? 0 : preconditions.hashCode());
		result = prime * result + (reminderAdminNotice ? 1231 : 1237);
		result = prime * result
				+ ((reminderTime == null) ? 0 : reminderTime.hashCode());
		result = prime
				* result
				+ ((reminderTimeUnit == null) ? 0 : reminderTimeUnit.hashCode());
		result = prime * result + (reminderUserNotice ? 1231 : 1237);
		result = prime * result
				+ ((response == null) ? 0 : response.hashCode());
		result = prime * result
				+ ((rolesRelated == null) ? 0 : rolesRelated.hashCode());
		result = prime * result
				+ ((routeEvalOrder == null) ? 0 : routeEvalOrder.hashCode());
		result = prime * result + ((rules == null) ? 0 : rules.hashCode());
		result = prime * result + (runtimeModifiable ? 1231 : 1237);
		result = prime * result + (sentAdminNotice ? 1231 : 1237);
		result = prime * result + (sentUserNotice ? 1231 : 1237);
		result = prime * result + (shared ? 1231 : 1237);
		result = prime * result
				+ ((stepComments == null) ? 0 : stepComments.hashCode());
		result = prime * result
				+ ((stepHead == null) ? 0 : stepHead.hashCode());
		result = prime * result
				+ ((submitForm == null) ? 0 : submitForm.hashCode());
		result = prime * result
				+ ((timeUnit == null) ? 0 : timeUnit.hashCode());
		result = prime * result
				+ ((usersRelated == null) ? 0 : usersRelated.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		WStepDef other = (WStepDef) obj;
		if (active != other.active)
			return false;
		if (arrivingAdminNotice != other.arrivingAdminNotice)
			return false;
		if (arrivingUserNotice != other.arrivingUserNotice)
			return false;
		if (assignedTime == null) {
			if (other.assignedTime != null)
				return false;
		} else if (!assignedTime.equals(other.assignedTime))
			return false;
		if (backingBean != other.backingBean)
			return false;
		if (customSaveMethod == null) {
			if (other.customSaveMethod != null)
				return false;
		} else if (!customSaveMethod.equals(other.customSaveMethod))
			return false;
		if (customSaveRefClass == null) {
			if (other.customSaveRefClass != null)
				return false;
		} else if (!customSaveRefClass.equals(other.customSaveRefClass))
			return false;
		if (customValidation != other.customValidation)
			return false;
		if (customValidationMethod == null) {
			if (other.customValidationMethod != null)
				return false;
		} else if (!customValidationMethod.equals(other.customValidationMethod))
			return false;
		if (customValidationRefClass == null) {
			if (other.customValidationRefClass != null)
				return false;
		} else if (!customValidationRefClass
				.equals(other.customValidationRefClass))
			return false;
		if (dataFieldDef == null) {
			if (other.dataFieldDef != null)
				return false;
		} else if (!dataFieldDef.equals(other.dataFieldDef))
			return false;
		if (deadlineAdminNotice != other.deadlineAdminNotice)
			return false;
		if (deadlineDate == null) {
			if (other.deadlineDate != null)
				return false;
		} else if (!deadlineDate.equals(other.deadlineDate))
			return false;
		if (deadlineTime == null) {
			if (other.deadlineTime != null)
				return false;
		} else if (!deadlineTime.equals(other.deadlineTime))
			return false;
		if (deadlineUserNotice != other.deadlineUserNotice)
			return false;
		if (deleted != other.deleted)
			return false;
		if (emailNotification != other.emailNotification)
			return false;
		if (engineNotification != other.engineNotification)
			return false;
		if (expiredAdminNotice != other.expiredAdminNotice)
			return false;
		if (expiredUserNotice != other.expiredUserNotice)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idAdditionalZone == null) {
			if (other.idAdditionalZone != null)
				return false;
		} else if (!idAdditionalZone.equals(other.idAdditionalZone))
			return false;
		if (idDefaultProcessor == null) {
			if (other.idDefaultProcessor != null)
				return false;
		} else if (!idDefaultProcessor.equals(other.idDefaultProcessor))
			return false;
		if (idDept == null) {
			if (other.idDept != null)
				return false;
		} else if (!idDept.equals(other.idDept))
			return false;
		if (idListZone == null) {
			if (other.idListZone != null)
				return false;
		} else if (!idListZone.equals(other.idListZone))
			return false;
		if (idPhase == null) {
			if (other.idPhase != null)
				return false;
		} else if (!idPhase.equals(other.idPhase))
			return false;
		if (idUserAssignmentMethod == null) {
			if (other.idUserAssignmentMethod != null)
				return false;
		} else if (!idUserAssignmentMethod.equals(other.idUserAssignmentMethod))
			return false;
		if (idWorkZone == null) {
			if (other.idWorkZone != null)
				return false;
		} else if (!idWorkZone.equals(other.idWorkZone))
			return false;
		if (instructions == null) {
			if (other.instructions != null)
				return false;
		} else if (!instructions.equals(other.instructions))
			return false;
		if (postconditions == null) {
			if (other.postconditions != null)
				return false;
		} else if (!postconditions.equals(other.postconditions))
			return false;
		if (preconditions == null) {
			if (other.preconditions != null)
				return false;
		} else if (!preconditions.equals(other.preconditions))
			return false;
		if (reminderAdminNotice != other.reminderAdminNotice)
			return false;
		if (reminderTime == null) {
			if (other.reminderTime != null)
				return false;
		} else if (!reminderTime.equals(other.reminderTime))
			return false;
		if (reminderTimeUnit == null) {
			if (other.reminderTimeUnit != null)
				return false;
		} else if (!reminderTimeUnit.equals(other.reminderTimeUnit))
			return false;
		if (reminderUserNotice != other.reminderUserNotice)
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (rolesRelated == null) {
			if (other.rolesRelated != null)
				return false;
		} else if (!rolesRelated.equals(other.rolesRelated))
			return false;
		if (routeEvalOrder == null) {
			if (other.routeEvalOrder != null)
				return false;
		} else if (!routeEvalOrder.equals(other.routeEvalOrder))
			return false;
		if (rules == null) {
			if (other.rules != null)
				return false;
		} else if (!rules.equals(other.rules))
			return false;
		if (runtimeModifiable != other.runtimeModifiable)
			return false;
		if (sentAdminNotice != other.sentAdminNotice)
			return false;
		if (sentUserNotice != other.sentUserNotice)
			return false;
		if (shared != other.shared)
			return false;
		if (stepComments == null) {
			if (other.stepComments != null)
				return false;
		} else if (!stepComments.equals(other.stepComments))
			return false;
		if (stepHead == null) {
			if (other.stepHead != null)
				return false;
		} else if (!stepHead.equals(other.stepHead))
			return false;
		if (submitForm == null) {
			if (other.submitForm != null)
				return false;
		} else if (!submitForm.equals(other.submitForm))
			return false;
		if (timeUnit == null) {
			if (other.timeUnit != null)
				return false;
		} else if (!timeUnit.equals(other.timeUnit))
			return false;
		if (usersRelated == null) {
			if (other.usersRelated != null)
				return false;
		} else if (!usersRelated.equals(other.usersRelated))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WStepDef [id=" + id + ", stepHead=" + stepHead + ", version="
				+ version + ", active=" + active + ", shared=" + shared
				+ ", deleted=" + deleted + ", idDept=" + idDept + ", idPhase="
				+ idPhase + ", instructions=" + instructions
				+ ", stepComments=" + stepComments + ", idListZone="
				+ idListZone + ", idWorkZone=" + idWorkZone
				+ ", idAdditionalZone=" + idAdditionalZone
				+ ", idDefaultProcessor=" + idDefaultProcessor
				+ ", submitForm=" + submitForm + ", timeUnit=" + timeUnit
				+ ", assignedTime=" + assignedTime + ", deadlineDate="
				+ deadlineDate + ", deadlineTime=" + deadlineTime
				+ ", reminderTimeUnit=" + reminderTimeUnit + ", reminderTime="
				+ reminderTime + ", runtimeModifiable=" + runtimeModifiable
				+ ", sentAdminNotice=" + sentAdminNotice
				+ ", arrivingAdminNotice=" + arrivingAdminNotice
				+ ", deadlineAdminNotice=" + deadlineAdminNotice
				+ ", reminderAdminNotice=" + reminderAdminNotice
				+ ", expiredAdminNotice=" + expiredAdminNotice
				+ ", sentUserNotice=" + sentUserNotice
				+ ", arrivingUserNotice=" + arrivingUserNotice
				+ ", deadlineUserNotice=" + deadlineUserNotice
				+ ", reminderUserNotice=" + reminderUserNotice
				+ ", expiredUserNotice=" + expiredUserNotice
				+ ", emailNotification=" + emailNotification
				+ ", engineNotification=" + engineNotification
				+ ", routeEvalOrder=" + routeEvalOrder + ", response="
				+ response + ", rolesRelated=" + rolesRelated
				+ ", usersRelated=" + usersRelated
				+ ", idUserAssignmentMethod=" + idUserAssignmentMethod
				+ ", customValidation=" + customValidation
				+ ", customValidationRefClass=" + customValidationRefClass
				+ ", customValidationMethod=" + customValidationMethod
				+ ", backingBean=" + backingBean + ", customSaveMethod="
				+ customSaveMethod + ", customSaveRefClass="
				+ customSaveRefClass + ", rules=" + rules + ", preconditions="
				+ preconditions + ", postconditions=" + postconditions
				+ ", dataFieldDef=" + dataFieldDef + ", insertDate="
				+ insertDate + ", insertUser=" + insertUser + ", modDate="
				+ modDate + ", modUser=" + modUser + "]";
	}

	/**
	 * @return the mxCellObject
	 */
	@XmlElement(name="mxCell")
	public MxCell getMxCellObject() {
		return mxCellObject;
	}

	/**
	 * @param mxCellObject the mxCellObject to set
	 */
	public void setMxCellObject(MxCell mxCellObject) {
		this.mxCellObject = mxCellObject;
	}

	/**
	 * @return the mxCellString
	 */
	public String getMxCellString() {
		return mxCellString;
	}

	/**
	 * @param mxCellString the mxCellString to set
	 */
	public void setMxCellString(String mxCell) {
		this.mxCellString = mxCell;
	}

	/**
	 * @return the xmlHref
	 */
	@XmlAttribute(name="href")
	public String getXmlHref() {
		return xmlHref == null ? "" : xmlHref;
	}

	/**
	 * @param xmlHref the xmlHref to set
	 */
	public void setXmlHref(String xmlHref) {
		this.xmlHref = xmlHref;
	}

	/**
	 * @return the xmlId
	 */
	@XmlAttribute(name="id")
	public String getXmlId() {
		return xmlId;
	}

	/**
	 * @param xmlId the xmlId to set
	 */
	public void setXmlId(String xmlId) {
		this.xmlId = xmlId;
	}

	/**
	 * @return the xmlLabel
	 */
	@XmlAttribute(name="label")
	public String getXmlLabel() {
		xmlLabel = this.getStepHead().getName() != null ? this.getStepHead().getName() : "";
		return xmlLabel;
	}

	/**
	 * @param xmlLabel the xmlLabel to set
	 */
	public void setXmlLabel(String xmlLabel) {
		this.xmlLabel = xmlLabel;
	}

	/**
	 * @return the xmlRules
	 */
	@XmlAttribute(name="rules")
	public String getXmlRules() {
		return xmlRules == null ? "" : xmlRules;
	}

	/**
	 * @param xmlRules the xmlRules to set
	 */
	public void setXmlRules(String xmlRules) {
		this.xmlRules = xmlRules;
	}

	/**
	 * @return the responsesString
	 */
	@XmlAttribute(name="responses")
	public String getResponsesString() {
//		return responsesString;
		String out ="";
		Iterator<WStepResponseDef> i = this.getResponseAsList().iterator();
		while(i.hasNext()){
			out += i.next().getName() + (i.hasNext()?"|":"") ;
		}
		responsesString = out;
		return responsesString ;
	}

	/**
	 * @param responsesString the responsesString to set
	 */
	public void setResponsesString(String responsesString) {
		this.responsesString = responsesString;
	}
}
