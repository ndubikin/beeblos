package org.beeblos.bpm.wc.taglib.util;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class SPDateConverter implements Converter {

	public SPDateConverter() {
    }
 
    public Object getAsObject(FacesContext facesContext, 
                              UIComponent uiComponent, 
                              String param){
        
    	// NOTA: presupone que la fecha viene en formato dd-mm-aaaa
    	
    	
    	String separador="-";
 
    	try {

//    		// determino cual es el separador
//	    	if ( param.contains("/") ) {
//	    		separador="/";
//	    	} else if ( param.contains("-") ) {
//	    		separador="-";
//	    	}

//            String fecha[] = param.split(separador);

            Date sObject = new SimpleDateFormat("dd/MM/yyyy").parse(param);

            return sObject;
            }
       catch (Exception exception) {
            throw new ConverterException(exception);
            }
    }
 
    public String getAsString(FacesContext facesContext, 
                              UIComponent uiComponent, 
                              Object obj) {

    	try {

    		String fecha = new SimpleDateFormat("dd/MM/yyyy").format((Date)obj);
            return fecha;
      
         } 
      catch (Exception exception) {
          throw new ConverterException(exception);
          }
    }
}