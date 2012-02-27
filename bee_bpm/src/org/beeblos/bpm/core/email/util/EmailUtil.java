package org.beeblos.bpm.core.email.util;


import org.apache.commons.validator.EmailValidator;

public class EmailUtil {

	// nes 20110419 - cambiado a usar el commons
	 public static boolean isValidEmailAddress(String email){  
		   //String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
//		 String  expression="^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$"; // nes 20110419
//		   CharSequence inputStr = emailAddress;  
//		   Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
//		   Matcher matcher = pattern.matcher(inputStr); 
//		   return matcher.matches();  
		 // Get an EmailValidator
		 
		 // si tiene una coma o un punto y coma supongo q podrán ser mas de 1 direccion de email concatenadas
		 // asi q las doy por buenas sin mas - en todo caso en un futuro vemos de hacer un split y ver que pasa
		 // pero por ahora vale así pienso ...
		 
		 if ( email.indexOf(";")>0 || email.indexOf(",")>0) {
			 return true;
		 }
		 
	     EmailValidator validator = EmailValidator.getInstance();

	     return validator.isValid(email);

	 }
}
