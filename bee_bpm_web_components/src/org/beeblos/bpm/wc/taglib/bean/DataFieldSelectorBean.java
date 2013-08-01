package org.beeblos.bpm.wc.taglib.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WProcessDataFieldBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;

public class DataFieldSelectorBean extends CoreManagedBean {
	
	private static final long serialVersionUID = 1L;
	
	private static final Log logger = 
		LogFactory.getLog(DataFieldSelectorBean.class);
	
	Integer processHeadId;
	private List<String> selectedWStepDataFieldList = new ArrayList<String>();
	private List<SelectItem> wStepDataFieldListCombo = new ArrayList<SelectItem>();
	private String strDataFieldString;
	
	private boolean selectedAllDataFields;
	
	private boolean selectedShowOnlySelectedDataFields;
	
	private List<SelectItem> wStepDataFieldListStatic = null;
	
	
	
	public DataFieldSelectorBean() {
		super();
		this.init();
	}

	public void init() {
		super.init();
		
		processHeadId=null;
		selectedAllDataFields=false;
		selectedShowOnlySelectedDataFields=false;
	}

	public void reset() {
		
		processHeadId=null;
		selectedAllDataFields=false;
		selectedShowOnlySelectedDataFields=false;
	}
	
	public Integer getProcessHeadId() {
		return processHeadId;
	}

	public void setProcessHeadId(Integer processHeadId) {
		this.processHeadId = processHeadId;
	}
	
	public void setSelectedWStepDataFieldList(List<String> selectedWStepDataFieldList) {
		this.selectedWStepDataFieldList = selectedWStepDataFieldList;
	}

	public List<String> getSelectedWStepDataFieldList() {
		return selectedWStepDataFieldList;
	}
	
	public void setwStepDataFieldListCombo(List<SelectItem> wStepDataFieldListCombo) {
		this.wStepDataFieldListCombo = wStepDataFieldListCombo;
	}

	public List<SelectItem> getwStepDataFieldListCombo() {
		return wStepDataFieldListCombo;
	}
	
	public boolean isSelectedShowOnlySelectedDataFields() {
		return selectedShowOnlySelectedDataFields;
	}

	public void setSelectedShowOnlySelectedDataFields(
			boolean selectedShowOnlySelectedDataFields) {
		this.selectedShowOnlySelectedDataFields = selectedShowOnlySelectedDataFields;
	}
	
	public String addWStepDataFieldSelected() {

		strDataFieldString = UtilsVs.castStringListToString(selectedWStepDataFieldList, ",");
		
		return null;
	}
	
	public String cancelAddWStepDataFieldSelected() {
//		System.out.println("### TRAZA.... cancelAddWUserDefSelected()");
		return null;
	}
	
	public String selectWStepDataFieldList() {

		// No eliminar este método, asi hace submit en Ajax y guarda selectedWStepDataFieldList
		logger.debug("Seleccionado item en selectedWStepDataFieldList");
		
		return null;
	}
	
	public String selectAllDataFields() {
		
		if (selectedAllDataFields) {
			selectedWStepDataFieldList.clear();
			for (SelectItem item : wStepDataFieldListCombo) {
				selectedWStepDataFieldList.add(item.getValue().toString());
			}
		} else {
			selectedWStepDataFieldList.clear();
		}
		
		return null;
	}
	
	public boolean isSelectedAllDataFields() {
		return selectedAllDataFields;
	}

	public void setSelectedAllDataFields(boolean selectedAllDataFields) {
		this.selectedAllDataFields = selectedAllDataFields;
	}
	
	public String loadInfoForDataField() {
		
		inicializeStepDataFieldListCombo();
		
		return null;
	}
	
	private void inicializeStepDataFieldListCombo() {

		WProcessDataFieldBL wProcessDataFieldBL = new WProcessDataFieldBL();

		try {
			wStepDataFieldListCombo = 
					UtilsVs
					.castStringPairToSelectitem(
							wProcessDataFieldBL.getComboList("", "", processHeadId));
			
			wStepDataFieldListStatic = new ArrayList<SelectItem>(wStepDataFieldListCombo);
			
		} catch (WProcessDataFieldException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getStrDataFieldString() {
		return strDataFieldString;
	}

	public void setStrDataFieldString(String strDataFieldString) {

		selectedWStepDataFieldList.clear();
		if (strDataFieldString != null && !"".equals(strDataFieldString)) {
			selectedWStepDataFieldList.addAll( Arrays.asList(strDataFieldString.split(",")) );
		}
		
		this.strDataFieldString = strDataFieldString;		
	}

	public String selectShowOnlySelectedDataFields() {

		if (selectedShowOnlySelectedDataFields) {

			Set<String> hsSelectedWStepDataFieldList=new HashSet<String>();
			hsSelectedWStepDataFieldList.addAll(selectedWStepDataFieldList);
			wStepDataFieldListCombo.clear();

			for (SelectItem item : wStepDataFieldListStatic) {
				if (hsSelectedWStepDataFieldList.contains(item.getValue())) {
					wStepDataFieldListCombo.add(item);
				}
			}
			
		} else {
			
			wStepDataFieldListCombo.clear();
			wStepDataFieldListCombo.addAll(wStepDataFieldListStatic);
		}
		
		return null;
	}
	
}
