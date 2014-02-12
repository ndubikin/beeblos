package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.PASS_PHRASE;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WEmailAccountBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WEmailAccountException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WEmailAccount;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.Constantes;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;

import com.sp.common.jsf.util.UtilsVs;
import com.sp.common.util.DesEncrypter;

public class WEmailAccountBean extends CoreManagedBean {

	private static final long serialVersionUID = -4021524365949197101L;

	private static final Log logger = LogFactory.getLog(WEmailAccountBean.class);
	

	
//	private Usuario usuario;
	
	private Integer id; // el tipo de sancion q estoy editando en caso de hacerlo 
	private WEmailAccount currEmailAccount;
	
	private List<WEmailAccount> emailAccountList = new ArrayList<WEmailAccount>();

	//dml 20120223
	private List<WEmailAccount> emailAccountListByUser = new ArrayList<WEmailAccount>();
		
	private Integer idUser;
	private List<SelectItem> wUserDefList = new ArrayList<SelectItem>();
	
	
	
	private String mensajes;
	
	private HtmlCommandButton deleteButton;
	private HtmlCommandButton saveButton;
	private boolean disableDeleteButton;
	private boolean disableSaveButton;
	
	
	private String currentSession;
	
	//rrl 20120201
	private String staticOutputPassword;
	private boolean outputPasswordEditable;
	private boolean outputPasswordChanged;
	
	//dml 20120305
	private String emailFilter;
	private String nameFilter;
	
	private String valueBtn;
	
	//dml 20120206
	private boolean addNewEmailMode;
	
	
	public WEmailAccountBean() {

		super();
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		
		this.idUser = cs.getUsuario().getIdUsuario();
		
		
		_init();
		
		getwUserDefList();

	}
	
	private void _init() {
		
		this.currEmailAccount = new WEmailAccount(EMPTY_OBJECT);
		this.currEmailAccount.setFormat("Text");  // rrl 20110727 //TODO: OJO !! En la BBDD este campo está como IS NOT NULL si se va ha quitar
		this.currEmailAccount.setwUserDef(getCurrentUser());
		
		this.id=0;

		this.disableDeleteButton=true;
		this.disableSaveButton = true;

		//rrl 20120201
		outputPasswordEditable = false;
		outputPasswordChanged = false;
		
		loadWEmailAccountLists();
		
		this.valueBtn="Add";
		this.addNewEmailMode=false;
		
		
	}
	
	
	private void _reset() {
		
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		
		this.idUser = cs.getUsuario().getIdUsuario();
		
		this.currEmailAccount = new WEmailAccount(EMPTY_OBJECT);
		this.currEmailAccount.setFormat("Text");  // rrl 20110727 //TODO: OJO !! En la BBDD este campo está como IS NOT NULL si se va ha quitar
		this.currEmailAccount.setwUserDef(getCurrentUser());
		this.id=0;
		
/*		try {
			this.emailAccountList = (ArrayList<WEmailAccount>) new WEmailAccountBL()
				.getWEmailAccountListByUser(this.idUser);

				 		
		} catch (WEmailAccountException e) {
			logger.error("Ocurrio Un Error: No debe " 
					+ e.getMessage() + " : " + e.getCause());
		
			String params[] = {this.currEmailAccount.getId().toString(), "Ocurrio Un Error al tratar de get la lista de usuarios:" 
				+ e.getMessage() + " : " + e.getCause() };
			agregarMensaje("211",e.getMessage(),params,FGPException.ERROR);	
		}
	*/
		// dml 20120223
		loadWEmailAccountLists();
		
		this.disableDeleteButton=true;
		this.disableSaveButton = true;
		
		//rrl 20120201
		outputPasswordEditable = false;
		outputPasswordChanged = false;
		
		// dml 20120305
		this.emailFilter = "";
		this.nameFilter = "";
		this.valueBtn="Add";

		//dml 20120306
		this.addNewEmailMode=false;

	}
	
	// dml 20120223
	public void loadWEmailAccountLists(){
		
		try {
			
			this.emailAccountListByUser = (ArrayList<WEmailAccount>) new WEmailAccountBL()
				.getWEmailAccountListByUser(this.idUser);

			this.emailAccountList = (ArrayList<WEmailAccount>) new WEmailAccountBL()
					.getWEmailAccountList();				
				 		
		} catch (WEmailAccountException e) {
			String message = "WEmailAccountBean.loadWEmailAccountLists() WEmailAccountException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		
	}

	public void recargaListaUsuario() {
		// recarga la lista para reflejar la actualización realizada ...
		if (this.idUser!=null && this.idUser!=0) {
			
			changeUsuario(); // recarga la lista de cuentas de correo para el usuario corriente
			this.disableSaveButton=false;
			this.disableDeleteButton=true;
		} else {
			//this.disableSaveButton=true;
			emailAccountList=null;
			
		}
		_reset();
	}
	
	// nes - esto está en estudio ...
	public void loadCurrentWEmailAccount(){
		
		System.out.println("------------->>>>>>>>< cargar current tipo de sancion:"+id);
		if (id!=null && id!=0) {
			try {
				currEmailAccount = new WEmailAccountBL().getWEmailAccountByPK(id);
				
				this.valueBtn="Update";
				
				//rrl 20120201
				staticOutputPassword = currEmailAccount.getOutputPassword();
				if (staticOutputPassword!=null && !"".equals(staticOutputPassword.trim())) {
					outputPasswordChanged = false;
					outputPasswordEditable = false;
				} else {
					outputPasswordChanged = true;
					outputPasswordEditable = true;
				}
				
			} catch (WEmailAccountException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	public void cancel(){
		
		_reset();
		
	}
	
	// si tiene id es actualización, si no lo tiene es alta ...
	// nes 20101013 - cambié la firma - debe devolver String ...
	public String save() {
		
		if (idUser==null || idUser==0) {
			String message = "Ocurrio Un Error: La Cuenta de Correo a almacenar debe estar asociado a un usuario [ Nombre: "
					+ this.currEmailAccount.getName();
			super.createWindowMessage(ERROR_MESSAGE, message, null);
		} else { 
			
			if (id!=null && id!=0) {
				actualizar(); //rrl 20120201
			} else {
				agregar();  //rrl 20120201
			}
			_reset();
			recargaListaUsuario();
		}
		
		return null;
	}
		
	// nes 20101013 - cambié la firma - debe devolver String ...
	public String actualizar() {
		
		String retorno = FAIL;

		try {

			// uce.setUsuario(usuario); COMENTARIO: no es necesario hacer el set porque no se cambia el usuario

			// como no lo tengo lo leo
			WEmailAccount savedUCE = new WEmailAccountBL()
							.getWEmailAccountByPK(id);
			
			
			savedUCE.setName(currEmailAccount.getName());
			savedUCE.setEmail(currEmailAccount.getEmail());
			savedUCE.setReplyTo(currEmailAccount.getReplyTo());
//			savedUCE.setUceTextoDeLaFirma(currEmailAccount.getUceTextoDeLaFirma());    //rrl 20110727 Campo no se utiliza
			
//			//HZC:11012011, cifrar la contrasenia
//		    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
//			savedUCE.setContraseniaSalida(encrypter.encrypt(currEmailAccount.getContraseniaSalida()));
			
			//rrl 20120102
			if (outputPasswordChanged) {
				
				if (currEmailAccount.getOutputPassword()!=null && !"".equals(currEmailAccount.getOutputPassword())) {
				    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
					savedUCE.setOutputPassword(encrypter.encrypt(currEmailAccount.getOutputPassword()));
				} else {
					savedUCE.setOutputPassword(null);
				}
			} else {
				savedUCE.setOutputPassword(staticOutputPassword);
			}
			
			savedUCE.setFormat(currEmailAccount.getFormat());   //rrl 20110727 Campo no se utiliza  //TODO: OJO !! En la BBDD este campo está como IS NOT NULL si se va ha quitar
			
			//rrl 20110727 Campo no se utiliza
			// Rellena el texto de la firma con el tipo Texto/Html
//			if (currEmailAccount.getFormato().equals("Texto")) {
//				savedUCE.setUceFirmaAdjuntaHtml(null);
//				savedUCE.setUceFirmaAdjuntaTxt(currEmailAccount.getUceFirmaAdjuntaTxt());
//			} else {
//				savedUCE.setUceFirmaAdjuntaHtml(currEmailAccount.getUceFirmaAdjuntaHtml());
//				savedUCE.setUceFirmaAdjuntaTxt(null);
//			}
			
			savedUCE.setSignatureTxt(currEmailAccount.getSignatureTxt());
			savedUCE.setSignatureHtml(currEmailAccount.getSignatureHtml());
				
			savedUCE.setIdentificationMethod(currEmailAccount.getIdentificationMethod());
			savedUCE.setOutputIdentificationMethod(currEmailAccount.getOutputIdentificationMethod());
			savedUCE.setInputServerName(currEmailAccount.getInputServerName());
			savedUCE.setOutputServerName(currEmailAccount.getOutputServerName());
			savedUCE.setOutputUserName(currEmailAccount.getOutputUserName());
			savedUCE.setUserDefaultAccount(currEmailAccount.isUserDefaultAccount());
			savedUCE.setInputPort(currEmailAccount.getInputPort());
			savedUCE.setOutputPort(currEmailAccount.getOutputPort());
			savedUCE.setConnectionSecurity(currEmailAccount.getConnectionSecurity());
			savedUCE.setOutputConnectionSecurity(currEmailAccount.getOutputConnectionSecurity());
			savedUCE.setInputServerType(currEmailAccount.getInputServerType());
		
			
			new WEmailAccountBL()
							.update(savedUCE, this.idUser);
					
			retorno=Constantes.SUCCESS_UCE;

			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla
		
		} catch (WEmailAccountException e) {
			String message = "WEmailAccountBean.actualizar() WEmailAccountException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

		return retorno;
	}

	// nes 20100927
	public String agregar() {

		String retorno = FAIL;

		WEmailAccountBL uceBL = new WEmailAccountBL();

		try {

			// if it is adding the new email account from the CRUD...
/*			if (currEmailAccount.getwUserDef() != null) {
				
				// nes 20101013
				currEmailAccount.setwUserDef(new WUserDefBL().getWUserDefByPK(this.idUser));

			}
*/			
			// rrl 20110727 Campo no se utiliza
			// Rellena el texto de la firma con el tipo Texto/Html
//			if (currEmailAccount.getFormato().equals("Texto")) {
//				currEmailAccount.setUceFirmaAdjuntaHtml(null);
//			} else {
//				currEmailAccount.setUceFirmaAdjuntaTxt(null);
//			}

			//rrl 20120201
			if (currEmailAccount.getOutputPassword()!=null && !"".equals(currEmailAccount.getOutputPassword())) {
			    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
				String contraseniaSalida = encrypter.encrypt(currEmailAccount.getOutputPassword());
				currEmailAccount.setOutputPassword(contraseniaSalida);
			} else {
				currEmailAccount.setOutputPassword(null);
			}
			
			uceBL.add(currEmailAccount, idUser);
			
			retorno=Constantes.SUCCESS_UCE;
			
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla

			
		} catch (WEmailAccountException e) {
			String message = "WEmailAccountBean.agregar() WEmailAccountException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} 

		return retorno;

	}
	
	public void enableDeleteButton(){
		
		if(deleteButton != null){
			deleteButton.setDisabled(false);	
		}			
		
		setDisableDeleteButton(false);
		
		//return null;
	}
	
	public void enableSaveButton(){
		
		if(saveButton != null){
			saveButton.setDisabled(false);	
		}			
		
		setDisableDeleteButton(false);
		
		//return null;
	}

	public String delete(ActionEvent event) {
		return this.delete();
	}
	
	// nes 20100927
	public String delete() {

		String retorno = FAIL;

		WEmailAccountBL uceBL = new WEmailAccountBL();
		
		try {

			currEmailAccount = uceBL.getWEmailAccountByPK(id);
			
			uceBL.delete(currEmailAccount, this.idUser);
			retorno = Constantes.SUCCESS_UCE;
			_reset();
			recargaListaUsuario(); 
			
		} catch (WEmailAccountException e) {
			String message = "WEmailAccountBean.delete() WEmailAccountException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

		return retorno;

	}

	
	/**
	 * @return the currEmailAccount
	 */
	public WEmailAccount getCurrEmailAccount() {
		if (currEmailAccount == null) {
			currEmailAccount = new WEmailAccount(EMPTY_OBJECT);
		}
		return currEmailAccount;
	}


	/**
	 * @param currEmailAccount the currEmailAccount to set
	 */
	public void setCurrEmailAccount(WEmailAccount currEmailAccount) {
		this.currEmailAccount = currEmailAccount;
	}


//	public Usuario getUsuario() {
//		if (usuario == null) {
//
//			usuario = new Usuario();
//
//		}
//		return usuario;
//	}
//	
//	public void setUsuario(Usuario usuario) {
//		this.usuario = usuario;
//	}

	// COMENTARIO : ESTO NO SE PARA Q SERIA PERO EN PPIO NO VA ...
//	
//	public Integer getIdUser() {
//		return getWEmailAccount().getUsuario().getIdUser();
//	}
//	
//	public void setIdUser(Integer id) {
//		getWEmailAccount().getUsuario().setIdUser(id);
//	}
//
//	public Integer getId() {
//		return getWEmailAccount().getId();
//	}
//	
//	public void setIdWEmailAccount(Integer id) {
//		getWEmailAccount().setIdWEmailAccount(id);
//	}

	public void changeUsuario() {

//		try {
//			this.emailAccountList = (ArrayList<WEmailAccount>) new WEmailAccountBL()
//				.getWEmailAccountListByUser(this.idUser);

		//dml 20120223
		loadWEmailAccountLists();
			
/*		} catch (WEmailAccountException e) {
			logger.error("Ocurrio Un Error: No debe " 
					+ e.getMessage() + " : " + e.getCause());
		
			String params[] = {this.currEmailAccount.getId().toString(), "Ocurrio Un Error al tratar de get la lista de usuarios:" 
				+ e.getMessage() + " : " + e.getCause() };
			agregarMensaje("211",e.getMessage(),params,FGPException.ERROR);	
		}
*/
	}
	
	public String getMensajes() {
		return mensajes;
	}


	public void setMensajes(String mensajes) {
		this.mensajes = mensajes;
	}

	public boolean isDisableDeleteButton() {
		
		logger.debug("isDisableDeleteButton:IdWEmailAccount = " + this.currEmailAccount.getName());
		
		if( isEmpty(this.currEmailAccount.getId()) || this.currEmailAccount.getId() == 0){ // nes 20100927
			setDisableDeleteButton(true);
		}else{
			setDisableDeleteButton(false);
		}
		
		return disableDeleteButton;
	}
	
	public boolean isDisableSaveButton() {
		
		logger.debug("isDisableSaveButton:IdWEmailAccount = " + this.currEmailAccount.getName());
		
		if( isEmpty(this.id) || this.id == 0){ // nes 20101119
			this.disableSaveButton = true;
		}else{
			this.disableSaveButton = false;
		}
		
		return disableDeleteButton;
	}
	
	
	public boolean isEmpty(Object obj){
		
		return ( obj == null || obj.toString().trim().equals("") );				
		
	}

	public void setDisableDeleteButton(boolean disableDeleteButton) {
		this.disableDeleteButton = disableDeleteButton;
	}
	
	public void setDisableSaveButton (boolean disableSaveButton) {
		this.disableSaveButton = disableSaveButton;
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

	
	/**
	 * @param idUser the idUser to set
	 */
//	public void setIdUser(Integer idUser) {
//		this.idUser = idUser;
//	}

	/**
	 * @return the idUser
	 */
	public Integer getIdUser() {
		return idUser;
	}


	


	/**
	 * @return the wUserDefList
	 * 
	 * Nota: hago que el get cargue el combo si no está cargado
	 *       El set no lo necesito porque no voy a setear nada en la lista de usuarios
	 */
	public List<SelectItem> getwUserDefList() {
		
		if (this.wUserDefList==null || this.wUserDefList.size()==0) {
			
			try {
				this.wUserDefList = UtilsVs.castStringPairToSelectitem(new WUserDefBL()
												.getComboList(null, null));
			} catch (WUserDefException e) {
				String message = "WEmailAccountBean.getwUserDefList() WUserDefException: ";
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			}
		}
		
		return wUserDefList;
	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * @return the emailAccountList
	 */
	public List<WEmailAccount> getEmailAccountList() {
		
		//recargaListaUsuario();
		return emailAccountList;
	}


	/**
	 * @param emailAccountList the emailAccountList to set
	 */
	public void setEmailAccountList(List<WEmailAccount> emailAccountList) {
		this.emailAccountList = emailAccountList;
	}


	public List<WEmailAccount> getEmailAccountListByUser() {
		return emailAccountListByUser;
	}

	public void setEmailAccountListByUser(List<WEmailAccount> emailAccountListByUser) {
		this.emailAccountListByUser = emailAccountListByUser;
	}
	
	public List<String> getFormatList() {
		
		List<String> formatList = new ArrayList<String>();
		
		formatList.add("Text");
		formatList.add("HTML");
		
		return formatList;
	}
	
	//rrl 20120201
	public List<SelectItem> getSecurityList() {
		
		List<SelectItem> securityList = new ArrayList<SelectItem>();
		
		securityList.add(new SelectItem("none", "NONE"));
		securityList.add(new SelectItem("STARTTLS", "STARTTLS"));
		securityList.add(new SelectItem("SSL/TLS", "SSL/TLS"));
		
		return securityList;
	}
	
	//rrl 20120201
	public String getStaticOutputPassword() {
		return staticOutputPassword;
	}

	public void setStaticOutputPassword(String staticOutputPassword) {
		this.staticOutputPassword = staticOutputPassword;
	}

	public boolean isOutputPasswordEditable() {
		return outputPasswordEditable;
	}

	public void setOutputPasswordEditable(boolean outputPasswordEditable) {
		this.outputPasswordEditable = outputPasswordEditable;
	}

	public boolean isOutputPasswordChanged() {
		return outputPasswordChanged;
	}

	public void setOutputPasswordChanged(boolean outputPasswordChanged) {
		this.outputPasswordChanged = outputPasswordChanged;
	}
	
	public String getEmailFilter() {
		return emailFilter;
	}

	public void setEmailFilter(String emailFilter) {
		this.emailFilter = emailFilter;
	}

	public String getNameFilter() {
		return nameFilter;
	}

	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
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

	public String changePassword() {
		
		outputPasswordEditable = true;
		outputPasswordChanged = true;
		currEmailAccount.setOutputPassword(null);
		
		return null;
	}
	
	public String changeOutputPassword() {
		
		outputPasswordChanged = true;
		
		return null;
	}


	// dml 20120223
	public WUserDef getCurrentUser() {
		
		ContextoSeguridad cs = (ContextoSeguridad)
					getSession().getAttribute(SECURITY_CONTEXT);
		
		try {
			
			if (cs != null) {
					return new WUserDefBL().getWUserDefByPK(cs.getIdUsuario());
			}
			
		} catch (WUserDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}	
	
	public void searchWEmailAccount(){
		
		try {
			
			this.emailAccountList = new WEmailAccountBL()
					.wEmailAccountFinder(nameFilter, emailFilter);		
			
			addNewEmailMode = false;
			this.id = 0;
				 		
		} catch (WEmailAccountException e) {
			String message = "WEmailAccountBean.searchWEmailAccount() WEmailAccountException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

	}
	
	public Integer getEmailAccountListSize(){
		
		if (this.emailAccountList != null) {
			
			return this.emailAccountList.size();
			
		}
		
		return null;
		
	}
	
	//dml 20120306
	public void changeFormMode(){
		
		this.addNewEmailMode=true;
		
	}


}
