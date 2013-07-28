package org.beeblos.bpm.core.util;


import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.beeblos.bpm.core.model.noper.StringPair;

/*
 * NOTA IMPORTANTE RAUL: CUANDO MIGREMOS A JSF2 USAR sp_common_jsf.utilsvs
 */
public class UtilesVarios {

	public UtilesVarios() {
    }

	public static List<SelectItem> castStringPairToSelectitem(List<StringPair> inputList) {
		
		List<SelectItem> outList = new ArrayList<SelectItem>();
		
		try {

			for (StringPair inStr: inputList) {
				outList.add(new SelectItem(inStr.getString1(),inStr.getString2()));
			}
			
		} catch (Exception e) {
			System.out.println("Error al intentar convertir StringPair to SelectItem:"
								+e.getMessage()+" "+e.getCause()+" - "+e.getClass());
			outList=null;
		}
		return outList;
	}
	
	
}