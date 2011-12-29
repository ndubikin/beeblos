package org.beeblos.bpm.web.bean;

import static org.beeblos.bpm.core.util.Constants.ID_ROLE_DEF_DIRECTORES_DEPTO;
import static org.beeblos.bpm.core.util.Constants.ID_ROLE_DEF_SUBDIRECTORES;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;


/**
 * @author rrl
 * 
 * @version 1.0: Permite seleccionar emails destinatarios por Director Depto y Subdirector 
 * 
 */

public class DestinatariosDeptoBean extends CoreManagedBean {
	
	private static final long serialVersionUID = 1L;
	
	private static final Log logger = 
		LogFactory.getLog(DestinatariosDeptoBean.class);
	
	private String selectedWUserDefText;
	private List<String> selectedWUserDefList = new ArrayList<String>();
	private List<String> selectedWUserDefDirectoresDeptoList = new ArrayList<String>();
	private List<String> selectedWUserDefSubdirectoresList = new ArrayList<String>();
	private List<SelectItem> listaWUserDefCombo = new ArrayList<SelectItem>();
	
	private boolean selectedDirectoresDeptoRoles;
	private boolean selectedSubdirectoresRoles;
	private boolean selectedTodosRoles;

	
	public DestinatariosDeptoBean() {
		super();
		this.init();
	}

	public void init() {
		super.init();
		
		selectedWUserDefText = "";
		inicializaListaRolesCombo();
		
	}
	
	public void reset() {
		
		selectedWUserDefText = "";
		inicializaListaRolesCombo();
	}
	
	public void setSelectedWUserDefText(String selectedWUserDefText) {
		this.selectedWUserDefText = selectedWUserDefText;
	}

	public String getSelectedWUserDefText() {
		return selectedWUserDefText;
	}

	public void setSelectedWUserDefList(List<String> selectedWUserDefList) {
		this.selectedWUserDefList = selectedWUserDefList;
	}

	public List<String> getSelectedWUserDefList() {
		return selectedWUserDefList;
	}

	public void setSelectedWUserDefDirectoresDeptoList(List<String> selectedWUserDefDirectoresDeptoList) {
		this.selectedWUserDefDirectoresDeptoList = selectedWUserDefDirectoresDeptoList;
	}

	public List<String> getSelectedWUserDefDirectoresDeptoList() {
		return selectedWUserDefDirectoresDeptoList;
	}

	public void setSelectedWUserDefSubdirectoresList(List<String> selectedWUserDefSubdirectoresList) {
		this.selectedWUserDefSubdirectoresList = selectedWUserDefSubdirectoresList;
	}

	public List<String> getSelectedWUserDefSubdirectoresList() {
		return selectedWUserDefSubdirectoresList;
	}
	
	public void setListaWUserDefCombo(List<SelectItem> listaWUserDefCombo) {
		this.listaWUserDefCombo = listaWUserDefCombo;
	}

	public List<SelectItem> getListaWUserDefCombo() {
		return listaWUserDefCombo;
	}
	
	public boolean isSelectedDirectoresDeptoRoles() {
		return selectedDirectoresDeptoRoles;
	}

	public void setSelectedDirectoresDeptoRoles(boolean selectedDirectoresDeptoRoles) {
		this.selectedDirectoresDeptoRoles = selectedDirectoresDeptoRoles;
	}

	public boolean isSelectedSubdirectoresRoles() {
		return selectedSubdirectoresRoles;
	}

	public void setSelectedSubdirectoresRoles(boolean selectedSubdirectoresRoles) {
		this.selectedSubdirectoresRoles = selectedSubdirectoresRoles;
	}

	public boolean isSelectedTodosRoles() {
		return selectedTodosRoles;
	}

	public void setSelectedTodosRoles(boolean selectedTodosRoles) {
		this.selectedTodosRoles = selectedTodosRoles;
	}
	
	private void inicializaListaRolesCombo() {

		WUserDefBL wUserDefBL = new WUserDefBL();

		try {
			listaWUserDefCombo = 
					UtilsVs
					.castStringPairToSelectitem(
							wUserDefBL.getComboList("", ""));
		} catch (WUserDefException e) {
			e.printStackTrace();
		}

		// Lista de seleccion de Directores Depto
		try {
			List<Integer> listaIdDirectoresDepto = wUserDefBL
					.getWUserDefIdByRole(ID_ROLE_DEF_DIRECTORES_DEPTO);
			
			selectedWUserDefDirectoresDeptoList.clear();
			for (Integer idDirectoresDepto : listaIdDirectoresDepto) {
				selectedWUserDefDirectoresDeptoList.add( idDirectoresDepto.toString() );
			}
			
		} catch (WUserDefException e) {
			e.printStackTrace();
		}
		
		// Lista de seleccion de Subdirectores
		try {
			List<Integer> listaIdSubdirectores = wUserDefBL
					.getWUserDefIdByRole(ID_ROLE_DEF_SUBDIRECTORES);

			selectedWUserDefSubdirectoresList.clear();
			for (Integer idSubdirectores : listaIdSubdirectores) {
				selectedWUserDefSubdirectoresList.add( idSubdirectores.toString() );
			}

		} catch (WUserDefException e) {
			e.printStackTrace();
		}
		
	}

	// rrl 20111130
	public String agregaWUserDefSeleccionados() {

		String ret = null;

		selectedWUserDefText = "";
		WUserDef wUserDef;
		WUserDefBL wUserDefBL = new WUserDefBL();

		try {

			for (String s : selectedWUserDefList) {
				wUserDef = wUserDefBL.getWUserDefByPK(Integer.parseInt(s));
				selectedWUserDefText += wUserDef.getEmail() + "; "; // rrl 20111221 
			}
			if (!selectedWUserDefText.isEmpty()) {
				selectedWUserDefText = selectedWUserDefText.substring(0,
						selectedWUserDefText.length() - 2);
			}

		} catch (NumberFormatException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".Error en recuperacion de WUserDef NumberFormatException ..." };
			agregarMensaje("1020", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
			selectedWUserDefText += " error en recuperacion de WUserDef NumberFormatException";
		} catch (WUserDefException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".Error en recuperacion de WUserDef WUserDefException ..." };
			agregarMensaje("1020", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
			selectedWUserDefText += " error en recuperacion de WUserDefExceptio WUserDefException";
		}

		return ret;
	}

	// rrl 20111214
	public String cancelaAgregarWUserDefSeleccionados() {

		System.out.println("### TRAZA.... cancelaAgregarSeleccionados()");

		return null;
	}
	
	
	public String seleccionarWUserDefList() {

		// No eliminar este método, asi hace submit en Ajax y guarda selectedWUserDefList
		logger.debug("Seleccionado item en selectedWUserDefList");
		
		return null;
	}

	public String seleccionarDirectoresDeptoRoles() {

		Set<String> selectedActual = new HashSet<String>(selectedWUserDefList);
		selectedWUserDefList.clear();
		
		if (selectedDirectoresDeptoRoles) {
			selectedActual.addAll(selectedWUserDefDirectoresDeptoList);
		} else {
			selectedActual.removeAll(selectedWUserDefDirectoresDeptoList);
		}
		
		selectedWUserDefList.addAll(selectedActual);
		
		return null;
	}

	public String seleccionarSubdirectoresRoles() {

		Set<String> selectedActual = new HashSet<String>(selectedWUserDefList);
		selectedWUserDefList.clear();
		
		if (selectedSubdirectoresRoles) {
			selectedActual.addAll(selectedWUserDefSubdirectoresList);
		} else {
			selectedActual.removeAll(selectedWUserDefSubdirectoresList);
		}
		
		selectedWUserDefList.addAll(selectedActual);
		
		return null;
	}

	public String seleccionarTodosRoles() {
		
		if (selectedTodosRoles) {
			selectedWUserDefList.clear();
			for (SelectItem item : listaWUserDefCombo) {
				selectedWUserDefList.add(item.getValue().toString());
			}
		} else {
			selectedWUserDefList.clear();
		}
		
		selectedDirectoresDeptoRoles=false;
		selectedSubdirectoresRoles=false;
		
		return null;
	}
	
	
}
