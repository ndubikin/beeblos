package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.FAIL;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WEmailTemplateGroupsBL;
import org.beeblos.bpm.core.bl.WEmailTemplatesBL;
import org.beeblos.bpm.core.error.WEmailTemplateGroupsException;
import org.beeblos.bpm.core.error.WEmailTemplatesException;
import org.beeblos.bpm.core.model.WEmailTemplateGroups;
import org.beeblos.bpm.core.model.WEmailTemplates;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.Constantes;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;

import com.sp.common.jsf.util.UtilsVs;

public class WEmailTemplatesBean extends CoreManagedBean {

	private static final long serialVersionUID = -4021524365949197101L;

	private static final Log logger = LogFactory.getLog(WEmailTemplatesBean.class);
		
	private Integer id; // el tipo de sancion q estoy editando en caso de hacerlo 
	private WEmailTemplates currentWUET;
	
	private List<WEmailTemplates> wuetList = new ArrayList<WEmailTemplates>();

	private Integer idUser;
	private List<SelectItem> wEmailTemplateGroupsList = new ArrayList<SelectItem>();
	
	private String mensajes;
	
	private String currentSession;
	
	//dml 20120305
	private String nameFilter;
	private String typeFilter;
	
	private String valueBtn;
	
	//dml 20120206
	private boolean addNewEmailMode;
	
	
	public WEmailTemplatesBean() {

		super();
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		
		this.idUser = cs.getUsuario().getIdUsuario();
		
		
		_init();
		
		getwEmailTemplateGroupList();

	}
	
	private void _init() {
		
		this.currentWUET = new WEmailTemplates(EMPTY_OBJECT);
		
		this.id=0;
		this.currentWUET.setType("TXT");
		
		loadWEmailTemplatesLists();
		
		this.valueBtn="Add";
		this.addNewEmailMode=false;
		
		this.typeFilter = "";
		this.nameFilter = "";
		
		
	}
	
	
	private void _reset() {
		
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		
		this.idUser = cs.getUsuario().getIdUsuario();
		
		this.currentWUET = new WEmailTemplates(EMPTY_OBJECT);

		this.id=0;
		this.currentWUET.setType("TXT");
		
		// dml 20120223
		loadWEmailTemplatesLists();
		
		// dml 20120305
		this.typeFilter = "";
		this.nameFilter = "";
		
		this.valueBtn="Add";

		//dml 20120306
		this.addNewEmailMode=false;

	}
	
	// SET EMTPY OBJECTS OF CURRENT OBJECT TO NULL TO AVOID PROBLEMS
	// WITH HIBERNATE RELATIONS AND CASCADES
	private void setModel() {
		
		if(currentWUET != null){
			
			if ( currentWUET.getwEmailTemplateGroup() != null && 
					currentWUET.getwEmailTemplateGroup().getId() == 0 ) {
				currentWUET.setwEmailTemplateGroup( null );
			}
			
		}
	}
	
	// CREATE NULL PROPERTIES OF OBJECT TYPE TO AVOID PROBLEMS
	// WITH VIEW AND ITS REFERENES TO THESE OBJECTS ...
	private void recoverNullObjects(){
		
		if(currentWUET != null){
			
			if ( currentWUET.getwEmailTemplateGroup() == null ) {
				currentWUET.setwEmailTemplateGroup( new WEmailTemplateGroups() );
			}
		}

	}
	// dml 20120223
	public void loadWEmailTemplatesLists(){
		
		try {
			
			this.wuetList = (ArrayList<WEmailTemplates>) new WEmailTemplatesBL()
					.getWEmailTemplatesList();				
				 		
			recoverNullObjects();

		} catch (WEmailTemplatesException e) {
			String message = "WEmailAccountBean. Exception: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		
	}
	
	public void cancel(){
		
		_reset();
		
	}
	
	// si tiene id es actualización, si no lo tiene es alta ...
	// nes 20101013 - cambié la firma - debe devolver String ...
	public String save() {
		
		if (idUser==null || idUser==0) {
			//martin - 20100930
			
			String message = "WEmailAccountBean. Exception: ";
			super.createWindowMessage(ERROR_MESSAGE, message, null);
		} else { 
			
			if (id!=null && id!=0) {
				actualizar(); //rrl 20120201
			} else {
				agregar();  //rrl 20120201
				recoverNullObjects();
			}
			_reset();
		}
		
		return null;
	}
		
	// nes 20101013 - cambié la firma - debe devolver String ...
	public String actualizar() {
		
		String retorno = FAIL;

		try {

			// uce.setUsuario(usuario); COMENTARIO: no es necesario hacer el set porque no se cambia el usuario

			// como no lo tengo lo leo
			WEmailTemplates savedUCE = new WEmailTemplatesBL()
							.getWEmailTemplatesByPK(id);
			
			
			savedUCE.setName(currentWUET.getName());
			savedUCE.setType(currentWUET.getType());
			try {
				if (currentWUET.getwEmailTemplateGroup() != null 
						&& currentWUET.getwEmailTemplateGroup().getId() != null
						&& !currentWUET.getwEmailTemplateGroup().getId().equals(0)) {
				
					savedUCE.setwEmailTemplateGroup(new WEmailTemplateGroupsBL().getWEmailTemplateGroupsByPK(currentWUET.getwEmailTemplateGroup().getId()));
			
				}
				
			} catch (WEmailTemplateGroupsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			savedUCE.setTemplate(currentWUET.getTemplate());		
			savedUCE.setMobileTemplate(currentWUET.getMobileTemplate());
			
			setModel();

			new WEmailTemplatesBL()
							.update(savedUCE, this.idUser);
					
			recoverNullObjects();

			retorno=Constantes.SUCCESS_UCE;

			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla
		
		} catch (WEmailTemplatesException e) {
			String message = "WEmailAccountBean. Exception: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

		return retorno;
	}

	// nes 20100927
	public String agregar() {

		String retorno = FAIL;

		WEmailTemplatesBL uceBL = new WEmailTemplatesBL();

		try {
			
			setModel();

			uceBL.add(currentWUET, idUser);
			
			recoverNullObjects();
			
			retorno=Constantes.SUCCESS_UCE;
			
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla

			
		} catch (WEmailTemplatesException e) {
			String message = "WEmailAccountBean. Exception: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		return retorno;

	}
	
	public String delete(ActionEvent event) {
		return this.delete();
	}
	
	// nes 20100927
	public String delete() {

		String retorno = FAIL;

		WEmailTemplatesBL uceBL = new WEmailTemplatesBL();
		
		try {

			currentWUET = uceBL.getWEmailTemplatesByPK(id);
			
			uceBL.delete(currentWUET, this.idUser);
			_reset();
			
		} catch (WEmailTemplatesException e) {
			String message = "WEmailAccountBean. Exception: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

		return retorno;

	}

	
	/**
	 * @return the currentWUET
	 */
	public WEmailTemplates getCurrentWUET() {
		if (currentWUET == null) {
			currentWUET = new WEmailTemplates(EMPTY_OBJECT);
		}
		return currentWUET;
	}


	/**
	 * @param currentWUET the currentWUET to set
	 */
	public void setCurrentWUET(WEmailTemplates currentWUET) {
		this.currentWUET = currentWUET;
	}

	
	public String getMensajes() {
		return mensajes;
	}


	public void setMensajes(String mensajes) {
		this.mensajes = mensajes;
	}

	public boolean isEmpty(Object obj){
		
		return ( obj == null || obj.toString().trim().equals("") );				
		
	}

	public HttpSession getSession() {
		return (HttpSession) getContext().getExternalContext()
				.getSession(false);
	}

	public String getCurrentSession() {
	
		HttpSession session = getSession();		
		currentSession = session.getId();
		
		return currentSession;
	}

	public void setCurrentSession(String currentSession) {
		this.currentSession = currentSession;
	}

	public List<SelectItem> getwEmailTemplateGroupList() {
		
		if (this.wEmailTemplateGroupsList==null || this.wEmailTemplateGroupsList.size()==0) {
			
			try {
				this.wEmailTemplateGroupsList = UtilsVs.castStringPairToSelectitem(new WEmailTemplateGroupsBL()
												.getComboList(null, null));
			} catch (WEmailTemplateGroupsException e) {
				String message = "WEmailAccountBean. Exception: ";
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			}
		}
		
		return wEmailTemplateGroupsList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<WEmailTemplates> getWuetList() {
		
		//recargaListaUsuario();
		return wuetList;
	}

	public void setWuetList(List<WEmailTemplates> wuetList) {
		this.wuetList = wuetList;
	}

	public String getNameFilter() {
		return nameFilter;
	}

	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}

	public String getTypeFilter() {
		return typeFilter;
	}

	public void setTypeFilter(String typeFilter) {
		this.typeFilter = typeFilter;
	}

	public String getValueBtn() {
		return valueBtn;
	}

	public void setValueBtn(String valueBtn) {
		this.valueBtn = valueBtn;
	}

	public boolean isAddNewEmailMode() {
		return addNewEmailMode;
	}

	public void setAddNewEmailMode(boolean addNewEmailMode) {
		this.addNewEmailMode = addNewEmailMode;
	}

	public void searchWEmailTemplates(){
		
		try {
			
			this.wuetList = new WEmailTemplatesBL()
					.wEmailTemplatesFinder(nameFilter, typeFilter);		

			addNewEmailMode = false;
			this.id = 0;
				 		
		} catch (WEmailTemplatesException e) {
			String message = "WEmailAccountBean. Exception: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

	}
	
	public Integer getWuetListSize(){
		
		if (this.wuetList != null) {
			
			return this.wuetList.size();
			
		}
		
		return null;
		
	}
	
	//dml 20120306
	public void changeFormMode(){
		
		this.addNewEmailMode=true;
		
	}
	
	// dml 20120306
	public List<SelectItem> getTypeFilterList() {
		
		List<SelectItem> types = new ArrayList<SelectItem>();
		
		types.add(new SelectItem("", "Select type ..."));
		types.add(new SelectItem("TXT", "TXT"));
		types.add(new SelectItem("HTML", "HTML"));
		
		return types;
		
	}

	// nes - esto está en estudio ...
	public void loadCurrentWEmailTemplate(){
		
		System.out.println("------------->>>>>>>>< cargar current tipo de sancion:"+id);
		if (id!=null && id!=0) {
			try {
				currentWUET = new WEmailTemplatesBL().getWEmailTemplatesByPK(id);
				
				recoverNullObjects();
				
				this.valueBtn="Update";
				
			} catch (WEmailTemplatesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	

}
