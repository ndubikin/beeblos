package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.FICHERO_BDC_MENSAJES;
import static org.beeblos.bpm.core.util.Constants.FICHERO_BDC_MENSAJES_ERROR;

import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class HelperUtil {
	
    public static CoreManagedBean recreateBean(String beanNameFacesConfig, String rutaBean){
    	
    	CoreManagedBean newBean = null;
    	
    	FacesContext faces = FacesContext.getCurrentInstance();
    	
        Map map = faces.getExternalContext().getSessionMap();
        CoreManagedBean bean = (CoreManagedBean) map.get(beanNameFacesConfig);
        
        try{ 
        	// Wil 20100819
        	Class c = Class.forName(rutaBean);
        	//System.out.println("---------------------->>>>>>>>>>> rutaBean:"+rutaBean);
        	newBean = (CoreManagedBean)c.newInstance();
        	map.put(beanNameFacesConfig, newBean);
        	
        }catch(ClassNotFoundException e) {
        	
        }catch(IllegalAccessException e){
        	
        }catch(InstantiationException e){
        	
        }   
        
        return newBean;
    }	
	
	
	public static Object getBeanFromSession(String beanName){
		
		Map map = getContext().getExternalContext().getSessionMap();
        		
		return map.get(beanName);
	}
	
//	public static ResourceBundle getErrorResourceBundle() {
//		return ResourceBundle.getBundle("com.softpoint.fgper.util.mensajes_error",
//				FacesContext.getCurrentInstance().getViewRoot().getLocale(),
//				Thread.currentThread().getContextClassLoader());
//	}
//	
//	public static ResourceBundle getResourceBundle() {
//		return ResourceBundle.getBundle("com.softpoint.fgper.util.mensajes",
//				FacesContext.getCurrentInstance().getViewRoot().getLocale(),
//				Thread.currentThread().getContextClassLoader());
//	}

	// rrl 20110610
	public static ResourceBundle getErrorResourceBundle() {
		return ResourceBundle.getBundle(FICHERO_BDC_MENSAJES_ERROR,
					FacesContext.getCurrentInstance().getViewRoot().getLocale(),
					Thread.currentThread().getContextClassLoader());
	}
	
	// rrl 20110610
	public static ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle(FICHERO_BDC_MENSAJES,
					FacesContext.getCurrentInstance().getViewRoot().getLocale(),
					Thread.currentThread().getContextClassLoader());
	}
	
	public static String getContextParameter(String paramName){		
		return getContext().getExternalContext().getInitParameter(paramName);
	}
	
	public static FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}	
}
