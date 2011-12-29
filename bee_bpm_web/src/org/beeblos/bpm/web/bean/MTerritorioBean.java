package org.beeblos.bpm.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;
import org.beeblos.bpm.web.util.ConstantsWeb;
import org.beeblos.security.auxiliar.bl.PaisBL;
import org.beeblos.security.auxiliar.bl.TerritorioBL;
import org.beeblos.security.auxiliar.bl.TipoTerritorioBL;
import org.beeblos.security.auxiliar.error.PaisException;
import org.beeblos.security.auxiliar.error.TerritorioException;
import org.beeblos.security.auxiliar.error.TipoTerritorioException;
import org.beeblos.security.auxiliar.model.Pais;
import org.beeblos.security.auxiliar.model.Territorio;
import org.beeblos.security.auxiliar.model.TipoTerritorio;

public class MTerritorioBean extends CoreManagedBean {

	private static final long serialVersionUID = -4021524365949197101L;

	private static final Log logger = LogFactory.getLog(MTerritorioBean.class);
		
	// martin - 20101025
	
	private Integer idTerritorio;
	private String territorioNombre;
	private String territorioComentario;
	private Territorio currentTerritorio;
	
	private String idPais;
	private Pais currentPais;
	
	private Integer idTipoTerritorio;
	private TipoTerritorio currentTipoTerritorio;
	
	private String nombreTipoTerritorioPadre;
	//private Integer idTerritorioPadre;
	private Territorio currentTerritorioPadre;
	
		
	private List<Territorio> listaTerritorios = new ArrayList<Territorio>();
	private List<SelectItem> listaTerritoriosPadre = new ArrayList<SelectItem>();
	private List<SelectItem> listaTiposTerritorio = new ArrayList<SelectItem>();
	private List<SelectItem> listaPais = new ArrayList<SelectItem>();
	
	private List<SelectItem> listaCCAA = new ArrayList<SelectItem>();
	private List<SelectItem> listaProvincia = new ArrayList<SelectItem>();
	private Integer idTerritorioCCAA;
	private Integer idTerritorioProvincia;
		
//	@SuppressWarnings("unused")
//	private List<SelectItem> listadoTerritorios;
	
	private String mensajes;
	
	private HtmlCommandButton btnBorrar;

	private String currentSession;

	
	private boolean disableTerritorio;
	private boolean renderPadre;
	private boolean disableBtnBorrar;
	
	//mrico 20110427
	private boolean disableBtnGuardar;
	// martin - 20101022 
	private String valueBtn;

	private Integer scrollerPage;

		

	public MTerritorioBean() {

		_init();
		
		//getListaTerritorios();
		disableBtnBorrar = true;
		disableBtnGuardar = true;

	}
	
	// martin - 20101117
	private void _init() {
		
		this.idTipoTerritorio=0;
		this.currentTerritorio = new Territorio();
		
		this.currentTipoTerritorio = null;
		this.idPais="ES";
		this.currentPais = null;
		
		this.idTerritorio = 0;
		//this.idTerritorioPadre=null;
		this.currentTerritorioPadre = null;
		
		// recarga la lista para reflejar la actualización realizada ...
		this.listaTerritorios=null;
		//this.getListaTerritorios();
			
		nombreTipoTerritorioPadre=null;
		disableTerritorio = false;
		
		this.valueBtn = "Guardar" ;
		renderPadre = false;
		disableBtnBorrar = true;
		disableBtnGuardar = true;
		//btnBorrar.setDisabled(true);
		
		this.idTerritorioCCAA=null;
		this.idTerritorioProvincia=null;
		this.currentTerritorioPadre=null;
		
		//Obtengo la lista de 
		try {
			this.listaCCAA= UtilsVs
					.castStringPairToSelectitem(
							new TerritorioBL().obtenerListaTerritoriosParaCombo("ES", 1) );
		} catch (TerritorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //
		
		_reset();
		
//		HelperUtil.recreateBean("territorioBean", "org.beeblos.bpm.web.bean.TerritorioBean");
		
	}
	
	
	// nes 20100927
	private void _reset() {
						
		this.currentTerritorio = new Territorio();
								
		this.idTerritorio = 0;

		
		// recarga la lista para reflejar la actualización realizada ...
		this.listaTerritorios=null;
		

					 
		
		this.valueBtn = "Guardar";
		disableBtnBorrar = true;
		disableBtnGuardar = true;
		//btnBorrar.setDisabled(true);
		
		//HelperUtil.recreateBean("territorioBean", "org.beeblos.bpm.web.bean.TerritorioBean");
		
	}
	
	
	public String cargarCurrentTerritorioPadre(){
		
//		TerritorioPadreBean territorioBean = (TerritorioPadreBean)HelperUtil.getBeanFromSession("territorioPadreBean");
//		
//		if(territorioBean.getIdPais()!= null){
//			
//			//Si va a cambiar el territorio padre se eliminar lo que se hubiese seleccionado antes
//			_reset();
//			
//			this.idPais = territorioBean.getIdPais();
//			try {
//				this.currentPais = new PaisBL().obtenerPaisPorPK(idPais);
//			} catch (PaisException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//			this.currentTerritorioPadre = territorioBean.getTerritorioSeleccionado();
//			recargaListaTerritorios();
//			
//			
//		}
		
			if(this.idPais != null && !this.idPais.equals("")){
				try {
					this.currentPais = new PaisBL().obtenerPaisPorPK(this.idPais);
				} catch (PaisException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		if(this.currentPais != null){
			
			TerritorioBL tBL = new TerritorioBL();
			try {
				
				if(this.idTerritorioProvincia != null && this.idTerritorioProvincia!=0){
					
					this.currentTerritorioPadre = tBL.obtenerTerritorioPorPK(idTerritorioProvincia);
							
				} else {
					this.currentTerritorioPadre = null;
				}
				
				if(this.currentTerritorioPadre!=null){
					this.listaTerritorios = tBL.obtenerTerritoriosHijos(this.currentTerritorioPadre);
				} else {
						this.listaTerritorios = null; 
				}
			
				
				
			} catch (TerritorioException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
		
	}	
	
	public void recargaListaProvincias(){
		
		if (this.idTerritorioCCAA != null && this.idTerritorioCCAA!=0){
			
			//this.idTerritorioPadre = currentTerritorioPadre.getIdTerritorio();
			try {
				this.listaProvincia= 
						UtilsVs
						.castStringPairToSelectitem(
								new TerritorioBL().obtenerTerritoriosHijosParaCombo(this.idTerritorioCCAA) );
			
			} catch (TerritorioException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			this.listaProvincia= null;
		}
		
		this.currentTerritorioPadre =null; 
		this.listaTerritorios = null;
		
		disableBtnGuardar = true;
	}
	
	
	public void recargaListaTerritorios(){
		
		if (currentTerritorioPadre != null){
			
			//this.idTerritorioPadre = currentTerritorioPadre.getIdTerritorio();
			try {
				this.listaTerritorios = new TerritorioBL().obtenerTerritoriosHijos(currentTerritorioPadre);
				this.currentTipoTerritorio = 
					new TipoTerritorioBL()
						.obtenerTipoTerritorioHijo(currentTerritorioPadre.getTipoTerritorio());
				
			disableBtnGuardar = false;
			} catch (TerritorioException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TipoTerritorioException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			//this.idTerritorioPadre = null;
			try {
				this.listaTerritorios = new TerritorioBL().obtenerTerritoriosSupremos(this.idPais);
				this.currentTipoTerritorio = new TipoTerritorioBL().obtenerTipoTerritorioNivelMaximo(this.idPais);
			} catch (TerritorioException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TipoTerritorioException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

	
	
	// nes 20101021 - setea currentTerritorio cuando el usuario pincha un elemento de la lista
	public String modificarTerritorio() {
		
		this.idTerritorio=getIdTerritorio();
		this.idTipoTerritorio=getIdTipoTerritorio(); 
		
		try {
			this.currentTerritorio = new TerritorioBL().obtenerTerritorioPorPK(this.idTerritorio);
			
			// martin - 20101025 - Si al actualizar TipoTerritorio = null la vista no funciona
			if (currentTerritorio.getTipoTerritorio() == null){
				this.setTipoTerritorio(new TipoTerritorio());
				this.currentTerritorio.setTipoTerritorio(new TipoTerritorio());
			}
			
		} catch (TerritorioException e) {
			logger.warn("no se puede obtener la territorio id: "+this.idTerritorio);
			
			String params[] = {this.currentTerritorio.getIdTerritorio() 
								+ ",", "Error al intentar recuperar la Territorio "+e.getMessage() 
								+ "\n"+e.getCause() };				
			agregarMensaje("50",e.getMessage(),params,FGPException.ERROR);		
			
			
		}
		disableBtnBorrar = false;
		modificarValueBtn();
		
		System.out.println("----------_>>>>>< tc:"+this.idTipoTerritorio+" idC:"+this.idTerritorio);
		
		return ConstantsWeb.SUCCESS_TERRITORIO;
	}
	

	
	// nes 20100927
	// si tiene id es actualización, si no lo tiene es alta ...
	public void guardar() {
		
		if (idTerritorio!=null && idTerritorio!=0) {
			actualizar();
		} else {
			agregar();
		}
		_reset();
		recargaListaTerritorios();
			
	}
		
	// nes 20100927
	public void actualizar() {
		
		String retorno = ConstantsWeb.FAIL;

		try {
		
			TerritorioBL territorioBL = new TerritorioBL();
			
//			this.currentTerritorio.setTipoTerritorio(this.currentTipoTerritorio);
//			this.currentTerritorio.setTerritorioPadre(this.currentTerritorioPadre);
//			this.currentTerritorio.setTerritorioPais(this.currentPais);
			
			territorioBL.actualizar(currentTerritorio);
			
			retorno=ConstantsWeb.SUCCESS_TERRITORIO;
			
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla
		
		} catch (TerritorioException e) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Actualizar el Territorio: [ id = " 
					+ this.currentTerritorio.getIdTerritorio() + " ; Nombre: "
					+ this.currentTerritorio.getTerritorioNombre()
					+"] "	+ e.getMessage() + " : " + e.getCause());
			
			String params[] = {this.currentTerritorio.getIdTerritorio() + ",", "Error al actualizar Territorio "+e.getMessage() };				
			agregarMensaje("50",e.getMessage(),params,FGPException.ERROR);		
			
		} 

	}

	// nes 20100927
	public String agregar() {

		String retorno = ConstantsWeb.FAIL;

		TerritorioBL territorioBL = new TerritorioBL();

		try {

			this.currentTerritorio.setTipoTerritorio(this.currentTipoTerritorio);
			this.currentTerritorio.setTerritorioPadre(this.currentTerritorioPadre);
			this.currentTerritorio.setTerritorioPais(this.currentPais);
			
			territorioBL.agregar(currentTerritorio);
			retorno=ConstantsWeb.SUCCESS_TERRITORIO;
			
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla

		} catch (TerritorioException ex2) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Agregar el Territorio: [Nombre: " 
					+ this.currentTerritorio.getTerritorioNombre()
					+"] "	+ ex2.getMessage() + " : " + ex2.getCause());
			
			String params[] = {"Error al Agregar Territorio "+ex2.getMessage()+ " : " + ex2.getCause() };					
			agregarMensaje("50",ex2.getMessage(),params,FGPException.ERROR);	
			
		} 
		
		// nes 20101021 - quite esto porque ahora sincronizamos currentTerritorio directamente contra la vista ...
//		catch (TipoTerritorioException e) {
//			//martin - 20100930
//			logger.error("Ocurrio Un Error al tratar de Agregar el TipoTerritorio: [Nombre: " 
//					+ this.currentTerritorio.getTerritorioNombre()
//					+"] "	+ e.getMessage() + " : " + e.getCause());
//			
//			String params[] = {"Error al Agregar Territorio "+e.getMessage()+ " : " + e.getCause() };					
//			agregarMensaje("50",e.getMessage(),params,FGPException.ERROR);	
//
//		}
		

		return retorno;

	}
	
	public String enableBtnBorrar(){
		
		if(btnBorrar != null){
			btnBorrar.setDisabled(false);	
		}			
		
		setDisableBtnBorrar(false);
		
		return null;
	}

	public String borra(ActionEvent event) {
		return this.borra();
	}
	
	// nes 20100927
	public String borra() {

		String retorno = ConstantsWeb.FAIL;

		TerritorioBL territorioBL = new TerritorioBL();

		try {
			currentTerritorio = territorioBL.obtenerTerritorioPorPK(idTerritorio);
			
			territorioBL.borrar(currentTerritorio);
			retorno = ConstantsWeb.SUCCESS_TERRITORIO;
			_reset();
			recargaListaTerritorios();
			
		} catch (TerritorioException e) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Borrar el Territorio: [ id = " 
					+ this.currentTerritorio.getIdTerritorio() + " ; Nombre: "
					+ this.currentTerritorio.getTerritorioNombre()
					+"] "	+ e.getMessage() + " : " + e.getCause());
			
			String params[] = {this.currentTerritorio.getIdTerritorio() + ",", "Error al borrar Territorio "+e.getMessage() };				
			agregarMensaje("50",e.getMessage(),params,FGPException.ERROR);			
		}

		return retorno;

	}

	// nes 20100927
	public Territorio getTerritorio() {
		if (currentTerritorio == null) {
			currentTerritorio = new Territorio();
		}
		return currentTerritorio;
	}



	
	public void changePaisListener(ActionEvent event) {

		try {
			this.currentPais = new PaisBL().obtenerPaisPorPK(this.idPais);
		} catch (PaisException e) {
			logger.error("Ocurrio Un Error al tratar de cargar el pais seleccionado: [ id = " 
					+ this.idPais
					+"] "	+ e.getMessage() + " : " + e.getCause());
			
			String params[] = {this.idPais + ",", "Error al cargar el pais seleccionado"+e.getMessage() };				
			agregarMensaje("50",e.getMessage(),params,FGPException.ERROR);	
		}
		
	}
	
	

	public String getMensajes() {
		return mensajes;
	}


	public void setMensajes(String mensajes) {
		this.mensajes = mensajes;
	}

	public boolean isDisableBtnBorrar() {
		//nes 20100927
		
		if( this.currentTerritorio ==null || isEmpty(this.currentTerritorio.getIdTerritorio()) || this.currentTerritorio.getIdTerritorio() == 0){ // nes 20100927
			setDisableBtnBorrar(true);
		}else{
			setDisableBtnBorrar(false);
		}
		
		return disableBtnBorrar;
	}
	
	public boolean isEmpty(Object obj){
		
		return ( obj == null || obj.toString().trim().equals("") );				
		
	}

	public void setDisableBtnBorrar(boolean disableBtnBorrar) {
		this.disableBtnBorrar = disableBtnBorrar;
	}
	
	
	public void setIdTerritorio(Integer idTerritorio) {
		this.idTerritorio = idTerritorio;
	}


	public Integer getIdTipoTerritorio() {
		return this.idTipoTerritorio;
	}
	
	public void setIdTipoTerritorio(Integer idTipoTerritorio) {
		this.idTipoTerritorio = idTipoTerritorio;
	}


	public Integer getIdTerritorio() {
		return idTerritorio;
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
	 * @return the currentTerritorio
	 */
	public Territorio getCurrentTerritorio() {
		return this.currentTerritorio;
	}


	/**
	 * @param currentTerritorio the currentTerritorio to set
	 */
	public void setCurrentTerritorio(Territorio currentTerritorio) {
		this.currentTerritorio = currentTerritorio;
	}

	// martin - 20101022 
	public void modificarValueBtn() {
		if (idTerritorio != 0){
			this.valueBtn = "Actualizar" ;
			setDisableBtnBorrar(false);
		} else {
			this.valueBtn = "Guardar";
			setDisableBtnBorrar(true);
		}
	}
	
	public String getValueBtn() {
		return this.valueBtn;
	}
	
	public void setValueBtn(String valueBtn) {
		this.valueBtn = valueBtn;
	}


	public void setScrollerPage(Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}


	public Integer getScrollerPage() {
		return this.scrollerPage;
	}



	public List<SelectItem> getListaPais() {
		
		if (this.listaPais==null || this.listaPais.size()==0) {
			try {
				this.listaPais = 
						UtilsVs
						.castStringPairToSelectitem(
								new PaisBL().obtenerPaisesParaCombo("Seleccionar...", "BLANCO") );
			} catch (PaisException e) {
					
				logger.error("Ocurrio Un Error al recuperar la de paises "+ e.getMessage() + " : " + e.getCause());
				
				String params[] = {"Error al recuperar la de paises "+e.getMessage()+ " : " + e.getCause() };					
				agregarMensaje("50",e.getMessage(),params,FGPException.ERROR);	
				
				}
			}
		
		return listaPais;
	}
	
	
	
	public List<Territorio> cargaListaTerritorios() {
		if (this.idPais != null && this.currentTipoTerritorio!=null){
			try {
				//Obtengo la lista de territorios de este tipo que ya existen
				this.listaTerritorios = new TerritorioBL()
								.obtenerTerritorioPorPaisYTipo(this.idPais, this.currentTipoTerritorio);
			
										
			} catch (TerritorioException e) {
				logger.error("Ocurrio Un Error al recuperar la lista de territorios Tipo: [id: " 
						+ this.idTipoTerritorio
						+"] "	+ e.getMessage() + " : " + e.getCause());
					
				String params[] = {"Error al recuperar la lista de territorios "+e.getMessage()+ " : " + e.getCause() };					
				agregarMensaje("50",e.getMessage(),params,FGPException.ERROR);	
				
			}
		}
		
		return listaTerritorios;
	}
	
	
	public void setTerritorioNombre(String territorioNombre) {
		this.territorioNombre = territorioNombre;
	}


	public String getTerritorioNombre() {
		return this.territorioNombre;
	}


	public void setTerritorioComentario(String territorioComentario) {
		this.territorioComentario = territorioComentario;
	}


	public String getTerritorioComentario() {
		return this.territorioComentario;
	}

	public void setIdPais(String territorioComentario) {
		this.idPais = territorioComentario;
	}


	public String getIdPais() {
		return this.idPais;
	}


	public void setTipoTerritorio(TipoTerritorio tipoTerritorio) {
		this.currentTipoTerritorio = tipoTerritorio;
	}


	public TipoTerritorio getTipoTerritorio() {
		return currentTipoTerritorio;
	}

	public void setCurrentPais(Pais currentPais) {
		this.currentPais = currentPais;
	}

	public Pais getCurrentPais() {
		return currentPais;
	}

	
	public boolean isDisableTerritorio() {
		return (this.idTipoTerritorio != null && this.idTipoTerritorio!=0);
	}

	
	public String getNombreTipoTerritorioPadre() {
		return nombreTipoTerritorioPadre;
	}

	public void setRenderPadre(boolean renderPadre) {
		this.renderPadre = renderPadre;
	}

	public boolean isRenderPadre() {
		return renderPadre;
	}

	
	public List<Territorio> getListaTerritorios() {
		return this.listaTerritorios;
	}

	public void setListaTerritoriosPadre(List<SelectItem> listaTerritoriosPadre) {
		this.listaTerritoriosPadre = listaTerritoriosPadre;
	}

	public List<SelectItem> getListaTerritoriosPadre() {
		return listaTerritoriosPadre;
	}

	public void setListaTiposTerritorio(List<SelectItem> listaTiposTerritorio) {
		this.listaTiposTerritorio = listaTiposTerritorio;
	}

	public List<SelectItem> getListaTiposTerritorio() {
		return listaTiposTerritorio;
	}

	public void setListaCCAA(List<SelectItem> listaCCAA) {
		this.listaCCAA = listaCCAA;
	}

	public List<SelectItem> getListaCCAA() {
		return listaCCAA;
	}

	public void setListaProvincia(List<SelectItem> listaProvincia) {
		this.listaProvincia = listaProvincia;
	}

	public List<SelectItem> getListaProvincia() {
		return listaProvincia;
	}

	public void setIdTerritorioCCAA(Integer idTerritorioCCAA) {
		this.idTerritorioCCAA = idTerritorioCCAA;
	}

	public Integer getIdTerritorioCCAA() {
		return idTerritorioCCAA;
	}

	public void setIdTerritorioProvincia(Integer idTerritorioProvincia) {
		this.idTerritorioProvincia = idTerritorioProvincia;
	}

	public Integer getIdTerritorioProvincia() {
		return idTerritorioProvincia;
	}
	public void setDisableBtnGuardar(boolean disableBtnGuardar) {
		this.disableBtnGuardar = disableBtnGuardar;
	}

	public boolean isDisableBtnGuardar() {
	
		if( this.currentTerritorioPadre ==null || isEmpty(currentTerritorioPadre.getIdTerritorio()) || currentTerritorioPadre.getIdTerritorio() == 0){ // nes 20100927
			setDisableBtnGuardar(true);
		}else{
			setDisableBtnGuardar(false);
		}
		return disableBtnGuardar;
	}
	
	
	
	
}
