package org.beeblos.bpm.wc.taglib.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private String strUserString;  //rrl 20120113
	
	private boolean selectedAllRoles;

	// dml 20120109
	private List<String> selectedWRoleDefList = new ArrayList<String>();
	private List<SelectItem> wRoleDefListCombo = new ArrayList<SelectItem>();
	private String strRoleString;
	
	// dml 20120109
	private boolean selectedAllUsers;
	
	//rrl 20120116
	private String searchRoleDefName;
	private Integer idWRoleDefSelected;
	private String searchUserDefName;
	private Integer idWUserDefSelected;
	private boolean selectedShowOnlySelectedRoles;
	private List<SelectItem> wRoleDefListStatic = null;
	private boolean selectedShowOnlySelectedUsers;
	private List<SelectItem> wUserDefListStatic = null;
	
	
	public UserAndRoleSelectorBean() {
		super();
		this.init();
	}

	public void init() {
		super.init();
		
		inicializeRoleListCombo();
		inicializeUserListCombo();
		//selectedWRoleDefList=Arrays.asList("2", "5", "7");
		
		//rrl 20120116
		selectedAllRoles=false;
		searchRoleDefName="";
		idWRoleDefSelected=0;
		selectedShowOnlySelectedRoles=false;
		
		selectedAllUsers=false;
		searchUserDefName="";
		idWUserDefSelected=0;
		selectedShowOnlySelectedUsers=false;
	}
	
	public void reset() {
		
		inicializeRoleListCombo();
		inicializeUserListCombo();
		
		//rrl 20120116
		selectedAllRoles=false;
		searchRoleDefName="";
		idWRoleDefSelected=0;
		selectedShowOnlySelectedRoles=false;
		
		selectedAllUsers=false;
		searchUserDefName="";
		idWUserDefSelected=0;
		selectedShowOnlySelectedUsers=false;
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
			
			wRoleDefListStatic = new ArrayList<SelectItem>(wRoleDefListCombo);
			
		} catch (WRoleDefException e) {
			e.printStackTrace();
		}
		
	}

	//rrl 20120113
	public String addWUserDefSelected() {

		strUserString = "";
		for (String s : selectedWUserDefList) {
			strUserString += s + ",";
		}
		
		// remove the last comma
		if (strUserString.endsWith(",")) {
			strUserString = strUserString.substring(0, strUserString.length() - 1);
		}
		
		return null;
	}
	
	public String cancelAddWUserDefSelected() {
//		System.out.println("### TRAZA.... cancelAddWUserDefSelected()");
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
			
			wUserDefListStatic = new ArrayList<SelectItem>(wUserDefListCombo);
			
		} catch (WUserDefException e) {
			e.printStackTrace();
		}
		
	}

	//rrl 20120112
	public String addWRoleDefSelected() {

		strRoleString = "";
		for (String s : selectedWRoleDefList) {
			strRoleString += s + ",";
		}
		
		// remove the last comma
		if (strRoleString.endsWith(",")) {
			strRoleString = strRoleString.substring(0, strRoleString.length() - 1);
		}
		
		return null;
	}


	public String cancelAddWRoleDefSelected() {
//		System.out.println("### TRAZA.... cancelAddWRoleDefSelected()");
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
		
		selectedWRoleDefList.clear();
		if (strRoleString != null && !"".equals(strRoleString)) {
			selectedWRoleDefList.addAll( Arrays.asList(strRoleString.split(",")) );
		}
		
		this.strRoleString = strRoleString;		
	}
	
	//rrl 20120113
	public String getStrUserString() {
		return strUserString;
	}

	public void setStrUserString(String strUserString) {

		selectedWUserDefList.clear();
		if (strUserString != null && !"".equals(strUserString)) {
			selectedWUserDefList.addAll( Arrays.asList(strUserString.split(",")) );
		}
		
		this.strUserString = strUserString;		
	}
	
	public String getSearchRoleDefName() {
		return searchRoleDefName;
	}

	public void setSearchRoleDefName(String searchRoleDefName) {
		this.searchRoleDefName = searchRoleDefName;
	}

	public Integer getIdWRoleDefSelected() {
		return idWRoleDefSelected;
	}

	public void setIdWRoleDefSelected(Integer idWRoleDefSelected) {
		this.idWRoleDefSelected = idWRoleDefSelected;
	}

	public String getSearchUserDefName() {
		return searchUserDefName;
	}

	public void setSearchUserDefName(String searchUserDefName) {
		this.searchUserDefName = searchUserDefName;
	}

	public Integer getIdWUserDefSelected() {
		return idWUserDefSelected;
	}

	public void setIdWUserDefSelected(Integer idWUserDefSelected) {
		this.idWUserDefSelected = idWUserDefSelected;
	}
	
	public boolean isSelectedShowOnlySelectedRoles() {
		return selectedShowOnlySelectedRoles;
	}

	public void setSelectedShowOnlySelectedRoles(
			boolean selectedShowOnlySelectedRoles) {
		this.selectedShowOnlySelectedRoles = selectedShowOnlySelectedRoles;
	}
	
	public boolean isSelectedShowOnlySelectedUsers() {
		return selectedShowOnlySelectedUsers;
	}

	public void setSelectedShowOnlySelectedUsers(
			boolean selectedShowOnlySelectedUsers) {
		this.selectedShowOnlySelectedUsers = selectedShowOnlySelectedUsers;
	}
	
	public ArrayList<SelectItem> autocompleteRoleDefName(Object input) {
		
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		String likeWordsName = (String) input;
		
		if (!"".equals(likeWordsName.trim())){
			// The static list has all the elements and also filter the table  
			for ( SelectItem s: wRoleDefListCombo) {
				if (s.getLabel().toLowerCase().indexOf(likeWordsName.toLowerCase()) > -1) {
					result.add(s);
				}
			}
		}
		
		return result;
	}

	public ArrayList<SelectItem> autocompleteUserDefName(Object input) {
		
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		String likeWordsName = (String) input;
		
		if (!"".equals(likeWordsName.trim())){
			// The static list has all the elements and also filter the table  
			for ( SelectItem s: wUserDefListCombo) {
				if (s.getLabel().toLowerCase().indexOf(likeWordsName.toLowerCase()) > -1) {
					result.add(s);
				}
			}
		}
		
		return result;
	}
	
	public String selectedAutocompleteWRoleDef() {
		
		String findItem=null; 

		for (String s : selectedWRoleDefList) {
			if (Integer.parseInt(s) == idWRoleDefSelected.intValue()) {
				findItem = s;
			}
		}

		// check or uncheck
		if (findItem!=null) {
			selectedWRoleDefList.remove(findItem);
		} else {
			selectedWRoleDefList.add(idWRoleDefSelected.toString());
		}
		
		return null;
	}
	
	public String selectedAutocompleteWUserDef() {
		
		String findItem=null; 

		for (String s : selectedWUserDefList) {
			if (Integer.parseInt(s) == idWUserDefSelected.intValue()) {
				findItem = s;
			}
		}

		// check or uncheck
		if (findItem!=null) {
			selectedWUserDefList.remove(findItem);
		} else {
			selectedWUserDefList.add(idWUserDefSelected.toString());
		}
		
		return null;
	}
	

	public String inicializeSearchRole() {
		reset();
		return null;
	}
	
	public String inicializeSearchUser() {
		reset();
		return null;
	}

	public String selectShowOnlySelectedRoles() {

		if (selectedShowOnlySelectedRoles) {

			Set<String> hsSelectedWRoleDefList=new HashSet<String>();
			hsSelectedWRoleDefList.addAll(selectedWRoleDefList);
			wRoleDefListCombo.clear();

			for (SelectItem item : wRoleDefListStatic) {
				if (hsSelectedWRoleDefList.contains(item.getValue())) {
					wRoleDefListCombo.add(item);
				}
			}
			
		} else {
			
			wRoleDefListCombo.clear();
			wRoleDefListCombo.addAll(wRoleDefListStatic);
		}
		
		return null;
	}
	
	public String selectShowOnlySelectedUsers() {

		if (selectedShowOnlySelectedUsers) {

			Set<String> hsSelectedWUserDefList=new HashSet<String>();
			hsSelectedWUserDefList.addAll(selectedWUserDefList);
			wUserDefListCombo.clear();

			for (SelectItem item : wUserDefListStatic) {
				if (hsSelectedWUserDefList.contains(item.getValue())) {
					wUserDefListCombo.add(item);
				}
			}
			
		} else {
			
			wUserDefListCombo.clear();
			wUserDefListCombo.addAll(wUserDefListStatic);
		}
		
		return null;
	}
	

	
}
