package org.beeblos.bpm.wc.taglib.util;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.model.SelectItem;

import org.beeblos.bpm.core.model.noper.StringPair;


public class UtilsVs {

	public UtilsVs() {
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
	
	public static String castStringListToString(List<String> listString, String delimiter){
		
		String outputString = "";
		
		if (listString != null){
			
			for (String s : listString) {
				outputString += s + delimiter;
			}
			
			// remove the last delimiter
			if (outputString.endsWith(delimiter)) {
				outputString = outputString.substring(0, outputString.length() - 1);
			}
			
			return outputString;
		
		} else {
			return null;
		}
				
	}
	
	public static List<String> castStringToStringList(String listString, String delimiter){
		
		List<String> outputListString = new ArrayList<String>();
		
		if ( listString!=null && !"".equals(listString)) {
		     StringTokenizer st = new StringTokenizer(listString, delimiter);
		     while (st.hasMoreTokens()) {
		         
		    	 outputListString.add(st.nextToken());
		    	 
		     }
		} else {
			outputListString=null;
		}
		return outputListString;
				
	}
	
	
}