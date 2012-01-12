package org.beeblos.bpm.wc.taglib.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WRoleDefBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;


public class UserAndRoleSelectorBean extends CoreManagedBean {
	
	private static final long serialVersionUID = 1L;
	
	private static final Log logger = 
		LogFactory.getLog(UserAndRoleSelectorBean.class);
	
	private List<String> selectedWUserDefList = new ArrayList<String>();
	private List<SelectItem> wUserDefListCombo = new ArrayList<SelectItem>();
	
	private boolean selectedAllRoles;

	// dml 20120109
	private List<String> selectedWRoleDefList = new ArrayList<String>();
	private List<SelectItem> wRoleDefListCombo = new ArrayList<SelectItem>();
	private String strRoleString;
	
	// dml 20120109
	private boolean selectedAllUsers;
	
	public UserAndRoleSelectorBean() {
		super();
		this.init();
	}

	public void init() {
		super.init();
		
		inicializeRoleListCombo();
		inicializeUserListCombo();
		
		//selectedWRoleDefList=Arrays.asList("2", "5", "7");
		
	}
	
	public void reset() {
		
		inicializeRoleListCombo();
		inicializeUserListCombo();
	}
	
	public void setSelectedWUserDefList(List<String> selectedWUserDefList) {
		this.selectedWUserDefList = selectedWUserDefList;
	}

	public List<String> getSelectedWUserDefList() {
		return selectedWUserDefList;
	}

	public void setwUserDefListCombo(List<SelectItem> wUserDefListCombo) {
		this.wUserDefListCombo = wUserDefListCombo;
	}

	public List<SelectItem> getwUserDefListCombo() {
		return wUserDefListCombo;
	}
	
	public boolean isSelectedAllRoles() {
		return selectedAllRoles;
	}

	public void setSelectedAllRoles(boolean selectedAllRoles) {
		this.selectedAllRoles = selectedAllRoles;
	}
	
	public List<String> getSelectedWRoleDefList() {
		return selectedWRoleDefList;
	}

	public void setSelectedWRoleDefList(List<String> selectedWRoleDefList) {
		this.selectedWRoleDefList = selectedWRoleDefList;
	}

	public List<SelectItem> getwRoleDefListCombo() {
		return wRoleDefListCombo;
	}

	public void setwRoleDefListCombo(List<SelectItem> wRoleDefListCombo) {
		this.wRoleDefListCombo = wRoleDefListCombo;
	}

	public boolean isSelectedAllUsers() {
		return selectedAllUsers;
	}

	public void setSelectedAllUsers(boolean selectedAllUsers) {
		this.selectedAllUsers = selectedAllUsers;
	}

	private void inicializeRoleListCombo() {

		WRoleDefBL wRoleDefBL = new WRoleDefBL();

		try {
			wRoleDefListCombo = 
					UtilsVs
					.castStringPairToSelectitem(
							wRoleDefBL.getComboList("", ""));
		} catch (WRoleDefException e) {
			e.printStackTrace();
		}
		
	}

	public String addSelectedWUserDef() {


		String ret = null;

//		loadWProcessDefForm();

		/*
		WUserDef wUserDef;
		WUserDefBL wUserDefBL = new WUserDefBL();

		try {


			
			for (String s : selectedWUserDefList) {
				wUserDef = wUserDefBL.getWUserDefByPK(Integer.parseInt(s));
			}
			
			loadWProcessDefForm();
			
			} catch (NumberFormatException e) {
				String mensaje = e.getMessage() + " - " + e.getCause();
				String params[] = { mensaje + ",",
						".addSelectedWUserDef() WUserDef NumberFormatException ..." };
				agregarMensaje("204", mensaje, params, FGPException.ERROR);
				e.printStackTrace();
			} catch (WUserDefException e) {
				String mensaje = e.getMessage() + " - " + e.getCause();
				String params[] = { mensaje + ",",
						".addSelectedWUserDef() WUserDef WUserDefException ..." };
				agregarMensaje("204", mensaje, params, FGPException.ERROR);
				e.printStackTrace();
			}
			*/
		return ret;
	}
	
	public String cancelAddWUserDefSelected() {

		System.out.println("### TRAZA.... cancelAddWUserDefSelected()");

		return null;
	}
	
	
	public String selectWUserDefList() {

		// No eliminar este método, asi hace submit en Ajax y guarda selectedWUserDefList
		logger.debug("Seleccionado item en selectedWUserDefList");
		
		return null;
	}


	public String selectAllRoles() {
		
		if (selectedAllRoles) {
			selectedWRoleDefList.clear();
			for (SelectItem item : wRoleDefListCombo) {
				selectedWRoleDefList.add(item.getValue().toString());
			}
		} else {
			selectedWRoleDefList.clear();
		}
		
		return null;
	}
	
	private void inicializeUserListCombo() {

		WUserDefBL wUserDefBL = new WUserDefBL();

		try {
			wUserDefListCombo = 
					UtilsVs
					.castStringPairToSelectitem(
							wUserDefBL.getComboList("", ""));
		} catch (WUserDefException e) {
			e.printStackTrace();
		}
		
	}

	//rrl 20120112
	public String addWRoleDefSelected() {

		String ret = null;

		strRoleString = "";
		for (String s : selectedWRoleDefList) {
			strRoleString += s + ",";
		}
		
		// remove the last comma
		if (strRoleString.endsWith(",")) {
			strRoleString = strRoleString.substring(0, strRoleString.length() - 1);
		}
		
		return ret;
	}


	public String cancelAddWRoleDefSelected() {

		System.out.println("### TRAZA.... cancelAddWRoleDefSelected()");

		return null;
	}
	
	
	public String selectWRoleDefList() {

		// No eliminar este método, asi hace submit en Ajax y guarda selectedWRoleDefList
		logger.debug("Item selected from selectedWRoleDefList");
		
		return null;
	}

	public String selectAllUsers() {
		
		if (selectedAllUsers) {
			selectedWUserDefList.clear();
			for (SelectItem item : wUserDefListCombo) {
				selectedWUserDefList.add(item.getValue().toString());
			}
		} else {
			selectedWUserDefList.clear();
		}
		
		return null;
	}

	public String getStrRoleString() {
		return strRoleString;
	}

	//rrl 20120112
	public void setStrRoleString(String strRoleString) {
		selectedWRoleDefList = new ArrayList<String>();
		if (strRoleString != null && !"".equals(strRoleString)) {
			selectedWRoleDefList=Arrays.asList(strRoleString.split(","));
		}
		
		this.strRoleString = strRoleString;
	}
	
}
