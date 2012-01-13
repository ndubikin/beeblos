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
import org.beeblos.bpm.core.model.WRoleDef;
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
	
	//rrl 20120113
	private String searchRoleDefName;	
	private List<SelectItem> wRoleDefListStatic = new ArrayList<SelectItem>();
	
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
			
			// rrl 20120113 not to read several times to the database
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

		System.out.println("### TRAZA.... addWRoleDefSelected()");

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

		System.out.println("### TRAZA.... cancelAddWRoleDefSelected()");

		return null;
	}
	
	public String selectWRoleDefList() {
		
		System.out.println("### TRAZA.... selectWRoleDefList()");
		
//		// recover lost items selected (to the union of elements "selectedWRoleDefList" and "strRoleString") 
//		Set<String> hsUnionWRoleDef=new HashSet<String>();
//		hsUnionWRoleDef.addAll(selectedWRoleDefList);
//		if (strRoleString != null && !"".equals(strRoleString)) {
//			hsUnionWRoleDef.addAll(Arrays.asList(strRoleString.split(",")));
//		}
//
//		selectedWRoleDefList.clear();
//		selectedWRoleDefList.addAll(hsUnionWRoleDef);
//		
//		// refresh "strRoleString"
//		this.strRoleString = "";
//		for (String s : selectedWRoleDefList) {
//			this.strRoleString += s + ",";
//		}
//		
//		// remove the last comma
//		if (this.strRoleString.endsWith(",")) {
//			this.strRoleString = this.strRoleString.substring(0, this.strRoleString.length() - 1);
//		}
		
		for (String item : selectedWRoleDefList) {
			System.out.println("### TRAZA.... selectedWRoleDefList="+item);
		}
		System.out.println("#### TRAZA.... strRoleString="+strRoleString);
		
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
		System.out.println("#### TRAZA: setStrRoleString().strRoleString="+strRoleString);
		
		for (String item : selectedWRoleDefList) {
			System.out.println("### TRAZA.... selectedWRoleDefList="+item);
		}
		System.out.println("#### TRAZA.... strRoleString="+strRoleString);
		
		selectedWRoleDefList.clear();
		if (strRoleString != null && !"".equals(strRoleString)) {
			selectedWRoleDefList.addAll( Arrays.asList(strRoleString.split(",")) );
		}
		
		this.strRoleString = strRoleString;		
		
		
// SOLUCION UNO		
//		Set<String> hsUnionWRoleDef=new HashSet<String>();
//		hsUnionWRoleDef.addAll(selectedWRoleDefList);
//		
//		selectedWRoleDefList.clear();
//		if (strRoleString != null && !"".equals(strRoleString)) {
//			hsUnionWRoleDef.addAll(Arrays.asList(strRoleString.split(",")));
//			selectedWRoleDefList.addAll(hsUnionWRoleDef);
//		}
//		
//		this.strRoleString = "";
//		for (String s : selectedWRoleDefList) {
//			this.strRoleString += s + ",";
//		}
//		
//		// remove the last comma
//		if (this.strRoleString.endsWith(",")) {
//			this.strRoleString = this.strRoleString.substring(0, this.strRoleString.length() - 1);
//		}

		
		// SOLUCION DOS		
		
		
//		this.strRoleString = strRoleString;
//		selectedWRoleDefList=Arrays.asList(this.strRoleString.split(","));		
	}
	
	//rrl 20120113
	public String getStrUserString() {
		return strUserString;
	}

	public void setStrUserString(String strUserString) {
		selectedWUserDefList = new ArrayList<String>();
		if (strUserString != null && !"".equals(strUserString)) {
			selectedWUserDefList=Arrays.asList(strUserString.split(","));
		}
		
		this.strUserString = strUserString;
	}
	
	public String getSearchRoleDefName() {
		return searchRoleDefName;
	}

	public void setSearchRoleDefName(String searchRoleDefName) {
		this.searchRoleDefName = searchRoleDefName;
	}
	
	public List<SelectItem> getwRoleDefListStatic() {
		return wRoleDefListStatic;
	}

	public void setwRoleDefListStatic(List<SelectItem> wRoleDefListStatic) {
		this.wRoleDefListStatic = wRoleDefListStatic;
	}
	
	public ArrayList<SelectItem> autocompleteRoleDefName(Object input) {
		
		System.out.println("#### TRAZA: autocompleteRoleDefName()");
		
		for (String item : selectedWRoleDefList) {
			System.out.println("### TRAZA.... selectedWRoleDefList="+item);
		}
		System.out.println("#### TRAZA.... strRoleString="+strRoleString);
		
		
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		String likeWordsName = (String) input;
		
		if (likeWordsName.trim().equals("")){
			
			// show all  
			wRoleDefListCombo = new ArrayList<SelectItem>(wRoleDefListStatic);
			
		} else {
			
			// The static list has all the elements and also filter the table  
			for ( SelectItem s: wRoleDefListStatic) {
				if (s.getLabel().toLowerCase().indexOf(likeWordsName.toLowerCase()) > -1) {
					result.add(s);
				}
			}
			
			wRoleDefListCombo = result;
		}
		
		recoverLostWRoleDefSelected();		
		
		return result;
	}

	// rrl 20120113 recover lost items selected (to the union of elements "selectedWRoleDefList" and "strRoleString")
	private void recoverLostWRoleDefSelected() {
		
		System.out.println("### TRAZA.... recoverLostItemsSelected()");
		
		Set<String> hsUnionWRoleDef=new HashSet<String>();
		hsUnionWRoleDef.addAll(selectedWRoleDefList);
		if (strRoleString != null && !"".equals(strRoleString)) {
			hsUnionWRoleDef.addAll(Arrays.asList(strRoleString.split(",")));
		}

		selectedWRoleDefList.clear();
		selectedWRoleDefList.addAll(hsUnionWRoleDef);
		
		// refresh "strRoleString"
		this.strRoleString = "";
		for (String s : selectedWRoleDefList) {
			this.strRoleString += s + ",";
		}
		
		// remove the last comma
		if (this.strRoleString.endsWith(",")) {
			this.strRoleString = this.strRoleString.substring(0, this.strRoleString.length() - 1);
		}
		
		for (String item : selectedWRoleDefList) {
			System.out.println("### TRAZA.... selectedWRoleDefList="+item);
		}
		System.out.println("#### TRAZA.... strRoleString="+strRoleString);
	}
	
}
