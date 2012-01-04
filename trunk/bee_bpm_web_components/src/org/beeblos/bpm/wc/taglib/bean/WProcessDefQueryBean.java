package org.beeblos.bpm.wc.taglib.bean;


import static org.beeblos.bpm.wc.taglib.util.Constantes.WPROCESSDEF_QUERY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;

public class WProcessDefQueryBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger
			.getLogger(WProcessDefQueryBean.class);

	private Date initialInsertDateFilter;
	private Date finalInsertDateFilter;
	private boolean strictInsertDateFilter;
	
	private String nameFilter;
	private String commentsFilter;
	private String listZoneFilter;
	private String workZoneFilter;
	private String additionalZoneFilter;
	
	private Integer currentUserId;
	

	private List<WProcessDef> wProcessDefList = new ArrayList<WProcessDef>();

	private Integer nResults = 0;

	private String action; // la action de consulta p.e: altas mas recientes o
							// ultimas modificaciones

	private Integer id;

	private TimeZone timeZone;

	public WProcessDefQueryBean() {
		super();
		PropertyConfigurator.configure("log4j.properties");
		_init();

	}

	private void _init() {
		logger.debug("ConsultaFacturaBean._init()");

		wProcessDefList = new ArrayList<WProcessDef>();

		nResults = 0;
		
		initialInsertDateFilter = null;
		finalInsertDateFilter = null;
		strictInsertDateFilter = false;

		nameFilter = "";
		commentsFilter = "";
		listZoneFilter = "";
		workZoneFilter = "";
		additionalZoneFilter = "";
		
		id = 0;

	}

	public List<WProcessDef> getwProcessDefList() {

		if (wProcessDefList == null || wProcessDefList.isEmpty()) {
			nResults = 0;
		}
		return wProcessDefList;
	}

	public void setwProcessDefList(List<WProcessDef> wProcessDefList) {
		this.wProcessDefList = wProcessDefList;
	}


	public Integer getnResults() {
		return nResults;
	}

	public void setnResults(Integer nResults) {
		this.nResults = nResults;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getId() {

		return id;

	}

	public void setId(Integer id) {

		this.id = id;
	}

	public TimeZone getTimeZone() {
		// Si se pone GMT+1 pone mal el dia
		return java.util.TimeZone.getDefault();
	}

	public String searchWProcessDefs() {

		logger.debug("searchWProcessDefs() - action: " + action);

		try {

			wProcessDefList = (ArrayList<WProcessDef>) new WProcessDefBL()
					.getProcessListByFinder(initialInsertDateFilter, finalInsertDateFilter, strictInsertDateFilter, 
							nameFilter, commentsFilter, listZoneFilter, workZoneFilter, additionalZoneFilter, 
							getCurrentUserId(), true);

			nResults = wProcessDefList.size();

		} catch (WProcessDefException e) {
			e.printStackTrace();
		}

		return WPROCESSDEF_QUERY;
	}
/*
	// rrl 20110927 Sin cambiar de pagina inicializa la Factura del proveedor
	public String inicializaFacturaProveedor() {

		logger.debug("inicializaFacturaProveedor(): idFacprov=" + idFacprov);

		ValueExpression valueBinding = super
				.getValueExpression("#{fichaFacturaBean}");

		if (valueBinding != null) {

			FichaFacturaBean ffb = (FichaFacturaBean) valueBinding
					.getValue(super.getELContext());
			ffb.init();
			ffb.setIdFacprov(this.idFacprov);
			ffb.consultarFicha(this.idFacprov);

			// NOTA NESTOR: NO VA NO SE DEBEN CREAR VARIABLES PARA MANEJAR ESTAS
			// COSAS A MENOS Q NO HAYA OTRO REMEDIO
			// ffb.setFacprovVisadaComentario(
			// ffb.getFacturaProveedor().getFacprovVisadaComentario() ); // rrl
			// 20110928 parar mostrar en el popup el comentario

			ffb.setDepartamentoDfagEnabled(false); // rrl 20111107 Al hacer clic
													// en el boton cambiar Dpto
													// le pones enabled el combo
													// Departamento

		}

		return null;
	}

	// rrl 20110831
	public String editarFactura() {

		String retorno = "FAIL";

		ValueExpression valueBinding = super
				.getValueExpression("#{fichaFacturaBean}");

		if (valueBinding != null) {

			FichaFacturaBean ffb = (FichaFacturaBean) valueBinding
					.getValue(super.getELContext());
			ffb.init();
			ffb.setIdFacprov(this.idFacprov);
			ffb.consultarFicha(this.idFacprov);

			retorno = EDITAR_FACTURA;
		}

		return retorno;
	}

	// rrl 20111128
	public String imprimirFacturasProveedor() {

		if (listaFacturas != null && !listaFacturas.isEmpty()) {
			ImpresorBean imp = new ImpresorBean();
			imp.imprimirFacturasProveedor(listaFacturas);
		}

		return null;
	}
		*/

	public Date getInitialInsertDateFilter() {
		return initialInsertDateFilter;
	}

	public void setInitialInsertDateFilter(Date initialInsertDateFilter) {
		this.initialInsertDateFilter = initialInsertDateFilter;
	}

	public Date getFinalInsertDateFilter() {
		return finalInsertDateFilter;
	}

	public void setFinalInsertDateFilter(Date finalInsertDateFilter) {
		this.finalInsertDateFilter = finalInsertDateFilter;
	}

	public boolean isStrictInsertDateFilter() {
		return strictInsertDateFilter;
	}

	public void setStrictInsertDateFilter(boolean strictInsertDateFilter) {
		this.strictInsertDateFilter = strictInsertDateFilter;
	}

	public String getNameFilter() {
		return nameFilter;
	}

	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}

	public String getCommentsFilter() {
		return commentsFilter;
	}

	public void setCommentsFilter(String commentsFilter) {
		this.commentsFilter = commentsFilter;
	}

	public String getListZoneFilter() {
		return listZoneFilter;
	}

	public void setListZoneFilter(String listZoneFilter) {
		this.listZoneFilter = listZoneFilter;
	}

	public String getWorkZoneFilter() {
		return workZoneFilter;
	}

	public void setWorkZoneFilter(String workZoneFilter) {
		this.workZoneFilter = workZoneFilter;
	}

	public String getAdditionalZoneFilter() {
		return additionalZoneFilter;
	}

	public void setAdditionalZoneFilter(String additionalZoneFilter) {
		this.additionalZoneFilter = additionalZoneFilter;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	

	public Integer getCurrentUserId() {
		if ( currentUserId== null ) {
			ContextoSeguridad cs = (ContextoSeguridad)
						getSession().getAttribute(SECURITY_CONTEXT);
			if (cs!=null) currentUserId=cs.getIdUsuario();
		}
		return currentUserId;
	}	

}
