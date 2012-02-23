package org.beeblos.bpm.wc.taglib.bean;

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
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.bl.WUserEmailAccountsBL;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.error.WUserEmailAccountsException;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WUserEmailAccounts;
import org.beeblos.bpm.core.util.DesEncrypter;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.Constantes;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;

public class WUserEmailAccountsBean extends CoreManagedBean {

	private static final long serialVersionUID = -4021524365949197101L;

	private static final Log logger = LogFactory.getLog(WUserEmailAccountsBean.class);
	

	
//	private Usuario usuario;
	
	private Integer id; // el tipo de sancion q estoy editando en caso de hacerlo 
	private String wueaName; 
	private String wueaEmail; 
	private WUserEmailAccounts currentWUEA;
	
	private ArrayList<WUserEmailAccounts> wueaList = new ArrayList<WUserEmailAccounts>();

	//dml 20120223
	private ArrayList<WUserEmailAccounts> wueaListByUser = new ArrayList<WUserEmailAccounts>();
		
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
	
	
	public WUserEmailAccountsBean() {

		super();
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		
		this.idUser = cs.getUsuario().getIdUsuario();
		
		
		_init();
		
		getwUserDefList();

	}
	
	private void _init() {
		
		this.currentWUEA = new WUserEmailAccounts(EMPTY_OBJECT);
		this.currentWUEA.setFormat("Text");  // rrl 20110727 //TODO: OJO !! En la BBDD este campo está como IS NOT NULL si se va ha quitar
		this.currentWUEA.setwUserDef(getCurrentUser());
		
		this.id=0;
		this.wueaName=null;
		this.wueaEmail=null;
		
		this.disableDeleteButton=true;
		this.disableSaveButton = true;

		//rrl 20120201
		outputPasswordEditable = false;
		outputPasswordChanged = false;
		
		loadWUserEmailAccountsLists();
		
		
		
	}
	
	
	private void _reset() {
		
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		
		this.idUser = cs.getUsuario().getIdUsuario();
		
		this.currentWUEA = new WUserEmailAccounts(EMPTY_OBJECT);
		this.currentWUEA.setFormat("Text");  // rrl 20110727 //TODO: OJO !! En la BBDD este campo está como IS NOT NULL si se va ha quitar
		this.currentWUEA.setwUserDef(getCurrentUser());
		this.id=0;
		
		this.wueaName=null;
		this.wueaEmail=null;
		
/*		try {
			this.wueaList = (ArrayList<WUserEmailAccounts>) new WUserEmailAccountsBL()
				.getWUserEmailAccountsListByUser(this.idUser);

				 		
		} catch (WUserEmailAccountsException e) {
			logger.error("Ocurrio Un Error: No debe " 
					+ e.getMessage() + " : " + e.getCause());
		
			String params[] = {this.currentWUEA.getId().toString(), "Ocurrio Un Error al tratar de get la lista de usuarios:" 
				+ e.getMessage() + " : " + e.getCause() };
			agregarMensaje("211",e.getMessage(),params,FGPException.ERROR);	
		}
	*/
		// dml 20120223
		loadWUserEmailAccountsLists();
		
		this.disableDeleteButton=true;
		this.disableSaveButton = true;
		
		//rrl 20120201
		outputPasswordEditable = false;
		outputPasswordChanged = false;
		
	}
	
	// dml 20120223
	public void loadWUserEmailAccountsLists(){
		
		try {
			
			this.wueaListByUser = (ArrayList<WUserEmailAccounts>) new WUserEmailAccountsBL()
				.getWUserEmailAccountsListByUser(this.idUser);

			this.wueaList = (ArrayList<WUserEmailAccounts>) new WUserEmailAccountsBL()
					.getWUserEmailAccountsList();				
				 		
		} catch (WUserEmailAccountsException e) {
			logger.error("Ocurrio Un Error: No debe " 
					+ e.getMessage() + " : " + e.getCause());
		
			String params[] = {this.currentWUEA.getId().toString(), "Ocurrio Un Error al tratar de get la lista de usuarios:" 
				+ e.getMessage() + " : " + e.getCause() };
			agregarMensaje("211",e.getMessage(),params,FGPException.ERROR);	
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
			wueaList=null;
			
		}
		_reset();
	}
	
	// nes - esto está en estudio ...
	public void loadCurrentWUserEmailAccounts(){
		
		System.out.println("------------->>>>>>>>< cargar current tipo de sancion:"+id);
		if (id!=null && id!=0) {
			try {
				currentWUEA = new WUserEmailAccountsBL().getWUserEmailAccountsByPK(id);
				
				//rrl 20120201
				staticOutputPassword = currentWUEA.getOutputPassword();
				if (staticOutputPassword!=null && !"".equals(staticOutputPassword.trim())) {
					outputPasswordChanged = false;
					outputPasswordEditable = false;
				} else {
					outputPasswordChanged = true;
					outputPasswordEditable = true;
				}
				
			} catch (WUserEmailAccountsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	// si tiene id es actualización, si no lo tiene es alta ...
	// nes 20101013 - cambié la firma - debe devolver String ...
	public String save() {
		
		if (idUser==null || idUser==0) {
			//martin - 20100930
			logger.error("Ocurrio Un Error: La Cuenta de Correo a almacenar debe estar asociado a un usuario [ Nombre: "
					+ this.currentWUEA.getName());
			
			String params[] = {this.currentWUEA.getName() + ",", "Error: La Cuenta de Correo a almacenar debe estar asociado a un usuario." }; // nes 20101013				
			agregarMensaje("211",null,params,FGPException.ERROR);	
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
			WUserEmailAccounts savedUCE = new WUserEmailAccountsBL()
							.getWUserEmailAccountsByPK(id);
			
			
			savedUCE.setName(currentWUEA.getName());
			savedUCE.setEmail(currentWUEA.getEmail());
			savedUCE.setReplyTo(currentWUEA.getReplyTo());
//			savedUCE.setUceTextoDeLaFirma(currentWUEA.getUceTextoDeLaFirma());    //rrl 20110727 Campo no se utiliza
			
//			//HZC:11012011, cifrar la contrasenia
//		    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
//			savedUCE.setContraseniaSalida(encrypter.encrypt(currentWUEA.getContraseniaSalida()));
			
			//rrl 20120102
			if (outputPasswordChanged) {
				
				if (currentWUEA.getOutputPassword()!=null && !"".equals(currentWUEA.getOutputPassword())) {
				    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
					savedUCE.setOutputPassword(encrypter.encrypt(currentWUEA.getOutputPassword()));
				} else {
					savedUCE.setOutputPassword(null);
				}
			} else {
				savedUCE.setOutputPassword(staticOutputPassword);
			}
			
			savedUCE.setFormat(currentWUEA.getFormat());   //rrl 20110727 Campo no se utiliza  //TODO: OJO !! En la BBDD este campo está como IS NOT NULL si se va ha quitar
			
			//rrl 20110727 Campo no se utiliza
			// Rellena el texto de la firma con el tipo Texto/Html
//			if (currentWUEA.getFormato().equals("Texto")) {
//				savedUCE.setUceFirmaAdjuntaHtml(null);
//				savedUCE.setUceFirmaAdjuntaTxt(currentWUEA.getUceFirmaAdjuntaTxt());
//			} else {
//				savedUCE.setUceFirmaAdjuntaHtml(currentWUEA.getUceFirmaAdjuntaHtml());
//				savedUCE.setUceFirmaAdjuntaTxt(null);
//			}
			
			savedUCE.setSignatureTxt(currentWUEA.getSignatureTxt());
			savedUCE.setSignatureHtml(currentWUEA.getSignatureHtml());
				
			savedUCE.setIdentificationMethod(currentWUEA.getIdentificationMethod());
			savedUCE.setOutputIdentificationMethod(currentWUEA.getOutputIdentificationMethod());
			savedUCE.setInputServerName(currentWUEA.getInputServerName());
			savedUCE.setOutputServerName(currentWUEA.getOutputServerName());
			savedUCE.setOutputUserName(currentWUEA.getOutputUserName());
			savedUCE.setUserDefaultAccount(currentWUEA.isUserDefaultAccount());
			savedUCE.setInputPort(currentWUEA.getInputPort());
			savedUCE.setOutputPort(currentWUEA.getOutputPort());
			savedUCE.setConnectionSecurity(currentWUEA.getConnectionSecurity());
			savedUCE.setOutputConnectionSecurity(currentWUEA.getOutputConnectionSecurity());
			savedUCE.setInputServerType(currentWUEA.getInputServerType());
		
			
			new WUserEmailAccountsBL()
							.update(savedUCE, this.idUser);
					
			retorno=Constantes.SUCCESS_UCE;

			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla
		
		} catch (WUserEmailAccountsException e) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Actualizar el WUserEmailAccounts: [ id = " 
					+ this.currentWUEA.getId() + " ; Nombre: "
					+ this.currentWUEA.getName()
					+"] "	+ e.getMessage() + " : " + e.getCause());
			
			String params[] = {this.currentWUEA.getId() + ",", "Error al actualizar WUserEmailAccounts "+e.getMessage()+"\n"+e.getCause() }; // nes 20101013				
			agregarMensaje("211",e.getMessage(),params,FGPException.ERROR);		
			
		}

		return retorno;
	}

	// nes 20100927
	public String agregar() {

		String retorno = FAIL;

		WUserEmailAccountsBL uceBL = new WUserEmailAccountsBL();

		try {

			// if it is adding the new email account from the CRUD...
/*			if (currentWUEA.getwUserDef() != null) {
				
				// nes 20101013
				currentWUEA.setwUserDef(new WUserDefBL().getWUserDefByPK(this.idUser));

			}
*/			
			// rrl 20110727 Campo no se utiliza
			// Rellena el texto de la firma con el tipo Texto/Html
//			if (currentWUEA.getFormato().equals("Texto")) {
//				currentWUEA.setUceFirmaAdjuntaHtml(null);
//			} else {
//				currentWUEA.setUceFirmaAdjuntaTxt(null);
//			}

			//rrl 20120201
			if (currentWUEA.getOutputPassword()!=null && !"".equals(currentWUEA.getOutputPassword())) {
			    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
				String contraseniaSalida = encrypter.encrypt(currentWUEA.getOutputPassword());
				currentWUEA.setOutputPassword(contraseniaSalida);
			} else {
				currentWUEA.setOutputPassword(null);
			}
			
			uceBL.add(currentWUEA, idUser);
			
			retorno=Constantes.SUCCESS_UCE;
			
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla

			
		} catch (WUserEmailAccountsException ex2) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Agregar el WUserEmailAccounts: [Nombre: " 
					+ this.currentWUEA.getName()
					+"] "	+ ex2.getMessage() + " : " + ex2.getCause());
			
			String params[] = {this.currentWUEA.getName().toString(), "Error al Agregar WUserEmailAccounts "
						+ex2.getMessage()+"\n"+ex2.getCause() };	// nes 20101013			
			agregarMensaje("211",ex2.getMessage(),params,FGPException.ERROR);	
		
			
/*		} catch (WUserDefException ex3) {

			logger.error("Ocurrio Un Error al tratar de cargar el WUserDef: [Nombre: " 
					+ this.currentWUEA.getName()
					+"] "	+ ex3.getMessage() + " : " + ex3.getCause());
			
			String params[] = {this.currentWUEA.getName().toString(), "Error al Agregar WUserEmailAccounts "
						+ex3.getMessage()+"\n"+ex3.getCause() };	// nes 20101013			
			agregarMensaje("211",ex3.getMessage(),params,FGPException.ERROR);	
*/		} 

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

		WUserEmailAccountsBL uceBL = new WUserEmailAccountsBL();
		
		try {

			currentWUEA = uceBL.getWUserEmailAccountsByPK(id);
			
			uceBL.delete(currentWUEA, this.idUser);
			retorno = Constantes.SUCCESS_UCE;
			_reset();
			recargaListaUsuario(); 
			
		} catch (WUserEmailAccountsException e) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de delete el WUserEmailAccounts: [ id = " 
					+ this.currentWUEA.getId() + " ; Nombre: "
					+ this.currentWUEA.getName()
					+"] "	+ e.getMessage() + " : " + e.getCause());
			
			String params[] = {this.currentWUEA.getId() + ",", "Error al delete WUserEmailAccounts "
					+e.getMessage()+"\n"+e.getCause() }; // nes 20101013				
			agregarMensaje("211",e.getMessage(),params,FGPException.ERROR);			
		}

		return retorno;

	}

	
	/**
	 * @return the currentWUEA
	 */
	public WUserEmailAccounts getCurrentWUEA() {
		if (currentWUEA == null) {
			currentWUEA = new WUserEmailAccounts(EMPTY_OBJECT);
		}
		return currentWUEA;
	}


	/**
	 * @param currentWUEA the currentWUEA to set
	 */
	public void setCurrentWUEA(WUserEmailAccounts currentWUEA) {
		this.currentWUEA = currentWUEA;
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
//		return getWUserEmailAccounts().getUsuario().getIdUser();
//	}
//	
//	public void setIdUser(Integer id) {
//		getWUserEmailAccounts().getUsuario().setIdUser(id);
//	}
//
//	public Integer getId() {
//		return getWUserEmailAccounts().getId();
//	}
//	
//	public void setIdWUserEmailAccounts(Integer id) {
//		getWUserEmailAccounts().setIdWUserEmailAccounts(id);
//	}

	public void changeUsuario() {

//		try {
//			this.wueaList = (ArrayList<WUserEmailAccounts>) new WUserEmailAccountsBL()
//				.getWUserEmailAccountsListByUser(this.idUser);

		//dml 20120223
		loadWUserEmailAccountsLists();
			
/*		} catch (WUserEmailAccountsException e) {
			logger.error("Ocurrio Un Error: No debe " 
					+ e.getMessage() + " : " + e.getCause());
		
			String params[] = {this.currentWUEA.getId().toString(), "Ocurrio Un Error al tratar de get la lista de usuarios:" 
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
		
		logger.debug("isDisableDeleteButton:IdWUserEmailAccounts = " + this.currentWUEA.getName());
		
		if( isEmpty(this.currentWUEA.getId()) || this.currentWUEA.getId() == 0){ // nes 20100927
			setDisableDeleteButton(true);
		}else{
			setDisableDeleteButton(false);
		}
		
		return disableDeleteButton;
	}
	
	public boolean isDisableSaveButton() {
		
		logger.debug("isDisableSaveButton:IdWUserEmailAccounts = " + this.currentWUEA.getName());
		
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
				
				logger.error("Ocurrio Un Error al tratar de get la lista de usuarios:" 
							+ e.getMessage() + " : " + e.getCause());
				
				String params[] = {this.currentWUEA.getId().toString(), "Ocurrio Un Error al tratar de get la lista de usuarios:" 
						+ e.getMessage() + " : " + e.getCause() };		
				agregarMensaje("211",e.getMessage(),params,FGPException.ERROR);	
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
	 * @return the wueaList
	 */
	public ArrayList<WUserEmailAccounts> getWueaList() {
		
		//recargaListaUsuario();
		return wueaList;
	}


	/**
	 * @param wueaList the wueaList to set
	 */
	public void setWueaList(ArrayList<WUserEmailAccounts> wueaList) {
		this.wueaList = wueaList;
	}


	public ArrayList<WUserEmailAccounts> getWueaListByUser() {
		return wueaListByUser;
	}

	public void setWueaListByUser(ArrayList<WUserEmailAccounts> wueaListByUser) {
		this.wueaListByUser = wueaListByUser;
	}

	/**
	 * @return the wueaName
	 */
	public String getName() {
		return wueaName;
	}


	/**
	 * @param wueaName the wueaName to set
	 */
	public void setWueaName(String wueaName) {
		this.wueaName = wueaName;
	}


	public void setWueaEmail(String wueaEmail) {
		this.wueaEmail = wueaEmail;
	}


	public String getWueaEmail() {
		return wueaEmail;
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
	
	public String changePassword() {
		
		outputPasswordEditable = true;
		outputPasswordChanged = true;
		currentWUEA.setOutputPassword(null);
		
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


}
