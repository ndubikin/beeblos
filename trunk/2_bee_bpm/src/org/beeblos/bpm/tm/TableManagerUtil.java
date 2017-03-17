package org.beeblos.bpm.tm;

import java.text.Normalizer;
import java.util.Date;
import java.util.Random;

import org.beeblos.bpm.tm.exception.TableManagerException;

/**
 * Utilities for TableManager
 * 
 * 
 * @author nestor
 * @version 1.001
 */
public class TableManagerUtil {

	
	private static final int MINIMUM_ACCEPTED_MAXLEN = 8;
	private static final String UNDERSCORE = "_";

	/**
	 * Generates a valid column name for a new user data field defined
	 * 
	 * @param name
	 * @param className
	 * @param maxlen
	 * @return
	 * @throws TableManagerException
	 */
	public static String generateColumnName(String name, String className, int maxlen) 
			throws TableManagerException {
	
		if (maxlen<MINIMUM_ACCEPTED_MAXLEN) {
			throw new TableManagerException("generateColumnName:Maximum lenght for column must be greater than 8. Current maxlen:"+maxlen+" is not accepted!");
		}
		
		String generatedColumnName;
		
		if (name==null || "".equals(name)) {
			if (className==null || "".equals(className)) {
				className="ag_";
			}
			int val = generateRandomInt(); 
			generatedColumnName=className+val;
		} else {
			
			generatedColumnName=convertStringToValidClassName(name,maxlen);
		
		}
		
		return generatedColumnName;
	}

	// returns random int between 1 and 49
	private static int generateRandomInt() {
		Random rand = new Random(); 
		rand.setSeed(new Date().getTime());
		int val = rand.nextInt(50);
		return val;
	}
	
	public static String convertStringToValidClassName(String name, int maxlen){
		
		String normalizedName = 
				Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

		String lowerCaseBaseName = normalizedName.toLowerCase();
		
		String generatedColName="";
		
		int i=0, currLen=0, tope=normalizedName.length();
		while (i<tope) {
			char ch = normalizedName.charAt(i);
			char lch = lowerCaseBaseName.charAt(i);
			if (ch==lch) {
				if (ch == ' '){
					generatedColName+=UNDERSCORE;
				} else {
					generatedColName+=ch;	
				}
				
			} else {
				if (i>0 && generatedColName.charAt(currLen-1) != UNDERSCORE.charAt(0)){
					generatedColName+=UNDERSCORE+lch;
					currLen++;
				} else {
					generatedColName+=lch;	
				}

			}
			i++;
			currLen++;
		}
		
		// trims enf of string ir result lenght is greater than allowed maxlen
		if (maxlen>0) {
			int genlen=generatedColName.length();
			if (genlen>maxlen) {
				generatedColName = generatedColName.substring(0,genlen-MINIMUM_ACCEPTED_MAXLEN+2);
				generatedColName+=generateRandomInt()+generateRandomInt();
			}
		}
	
		return generatedColName;
	}
}
