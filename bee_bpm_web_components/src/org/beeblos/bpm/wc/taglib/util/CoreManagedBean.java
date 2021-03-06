package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.FICHERO_BDC_MENSAJES;
import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static com.sp.common.util.ConstantsCommon.OK_MESSAGE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sp.common.util.Resourceutil;

//import org.apache.myfaces.shared_impl.util.MessageUtils;


/**
 * @author roger
 * 
 */
public class CoreManagedBean implements Serializable {

	private static final Log logger = LogFactory.getLog(CoreManagedBean.class);
	
	public static final String SECURITY_CONTEXT = "contextoSeguridad";
	public String BEAN_RELACIONADO="org.beeblos.bpm.wc.taglib.util.CoreManagedBean";
	
//	public static final long DEFAULT_AREA = -1;
//	public static final long DEFAULT_DEPTO = -1;
	
	private static final long serialVersionUID = -4397579297370416992L;
	
	private ArrayList<FGPException> exceptionList = new ArrayList<FGPException>();
	
	
	private String requestContextPath;
	public static String contextPath;
	
	private boolean showHeaderMessage;
	public String messageStyle;
	
	
	// Wil 20100819
	private String grupo; 
	// Wil 20100819
	public static String GRUPO_FICHAS = "fichas";
	
	// hzam 20110104
	public static final String REPOSITORY_STATUS = "bStatusServidor";

//	public static ResourceBundle getResourceBundle() {
//		return ResourceBundle.getBundle("org.beeblos.bpm.wc.taglib.util.mensajes",
//				FacesContext.getCurrentInstance().getViewRoot().getLocale(),
//				Thread.currentThread().getContextClassLoader());
//	}

	// rrl 20110610 pasa por parametro el fichero de propiedades de mensajes
	public static ResourceBundle getResourceBundle(String ficheroPropiedades) {
		return ResourceBundle.getBundle(FICHERO_BDC_MENSAJES,
					FacesContext.getCurrentInstance().getViewRoot().getLocale(),
					Thread.currentThread().getContextClassLoader());
	}
	
	public CoreManagedBean() {
		this.reset();
		this.init_core();
	}

	public void init_core(){
		this.setRequestContextPath();
		this.setContextPath();
	}
	
	public void reset(){
		showHeaderMessage = false;
	}

	// nes 20111026
	public static String setConfigurationPath() {
		String strConfPath=null;
		try {
		
			strConfPath = contextPath + "/" 
				+FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath()
				+"/WEB-INF/classes";
			
		}catch (Exception e) {
			String mensaje = "ERROR al intentar setear configurationPath: "+e.getClass()+" - "+e.getMessage()+" - "+e.getCause()
			+" \n se setea configurationPath a null";
			
			logger.error(mensaje);
		}
		return strConfPath;
	}
	
	// dml 20130417
	public String getContextPath(){
		
		if (this.contextPath == null){
			this.setContextPath();
		}
		
		return contextPath;
		
	}
	
	// nes 20110503
	public void setContextPath() {
		
		try {
			Resourceutil.getProperties();
			contextPath = FacesContext.getCurrentInstance()
								.getExternalContext()
									.getRealPath("")
										.replaceAll("\\\\", "/");
		
			logger.debug( "------------>>>>>>>>>>>>>>>  contextPath leido:["+contextPath+"]" );

	    	
	    	if ( this.getRequestContextPath() != null 
	    			&& !"".equals(this.getRequestContextPath()) ) {
					contextPath = contextPath.substring(0,contextPath.indexOf( this.getRequestContextPath() ));
	    	} 
			
		} catch (Exception e) {
			String mensaje = "ERROR al intentar setear contextPath: "+e.getClass()+" - "+e.getMessage()+" - "+e.getCause()
								+" \n se setea contextPath a null";

			logger.error(mensaje);

		}
		
		logger.debug( "------------>>>>>>>>>>>>>>>  contextPath ELEGIDO:["+contextPath+"]" );

	}

	// dml 20130417
	public String getRequestContextPath(){
		
		if (this.requestContextPath == null){
			this.setRequestContextPath();
		}
		
		return requestContextPath;
		
	}
	
	// nes 20120316
	public void setRequestContextPath() {
		
		try {

			requestContextPath = FacesContext.getCurrentInstance()
												.getExternalContext()
													.getRequestContextPath();
			
			requestContextPath = requestContextPath.trim().replaceAll("\\\\", "/");
		

			logger.debug( "------------>>>>>>>>>>>>>>> 2 RequestContextPath:["+requestContextPath+"]" );
	    	

		} catch (Exception e) {
			String mensaje = "ERROR al intentar setear requestContextPath: "+e.getClass()+" - "+e.getMessage()+" - "+e.getCause()
								+" \n se setea requestContextPath a null";
			logger.error(mensaje);

		}
		
	}
	
/*	
	public void agregarMensaje(String code,String message,String[] params,int level){
		
		ExceptionBean exceptionBean = (ExceptionBean)HelperUtil.getBeanFromSession("exceptionBean");
		
		if( getContext().getAttributes().get("exceptionList") != null ){
			exceptionList = (ArrayList<FGPException>)getContext().getAttributes().get("exceptionList");			
		}
		
		exceptionList.add(new FGPException(code,message,params,level));			
		getContext().getAttributes().put("exceptionList", exceptionList);				
	}
	
	public void agregarMensaje(FacesMessage.Severity tipo, String idMensaje,
			Object[] params) {
		//MessageUtils.addMessage(tipo, idMensaje, params, getContext());
		
	}

	public void agregarMensaje(String resumen, String detalle) {
		FacesMessage facesMessage = new FacesMessage(resumen, detalle);
		getContext().addMessage(null, facesMessage);
	}
	
*/
	public void addMessage(String resumen) {
		
		FacesMessage facesMessage = new FacesMessage(resumen, null);
		getContext().addMessage(null, facesMessage);		
	}

	public void createWindowMessage(String messageType, String message, Exception e) {

		if (e != null) {

			message += " "+e.getMessage();
			
			e.printStackTrace();
		}

		if (messageType.equals(ERROR_MESSAGE)) {
			this.setMessageStyle(errorMessageStyle());
			logger.error(message);
		} else if (messageType.equals(OK_MESSAGE)) {
			this.setMessageStyle(normalMessageStyle());
			logger.info(message);
		}

		setShowHeaderMessage(true);
		this.addMessage(message);
		

	}

	public String normalMessageStyle() {
		return "font-weight:bold;";
	}
	
	public String errorMessageStyle(){
		return "font-weight:bold; color:red;";
	}

	public FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) getContext().getExternalContext()
				.getRequest();
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) getContext().getExternalContext()
				.getResponse();
	}

	public HttpSession getSession() {
		return (HttpSession) getContext().getExternalContext()
				.getSession(false);
	}

	// rrl 20101202
	// para sustituir ValueBinding deprecated
	public ValueExpression getValueExpression(String el) {
        Application app = getContext().getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = getContext().getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, el, Object.class);
        return valueExp;
	}
	
	// rrl 20101202
	// para sustituir ValueBinding deprecated
	public ELContext getELContext() {
		return getContext().getELContext();
	}

	public MethodExpression getMethodExpression(String methodName, Class[] args ) {
        Application app = getContext().getApplication();
	    return app.getExpressionFactory().createMethodExpression(getELContext(), methodName, null, args);
	}
	
// rrl deprecated
//	
	
	// nes 20101205 - temporal hasta que rrl arregle el tema del serValueBinding
	public ValueBinding getValueBinding(String el) {
		return getContext().getApplication().createValueBinding(el);
	}
	
	public MethodBinding getMethoBinding(String methodName, Class[] args ) {
		return getContext().getApplication().createMethodBinding(methodName, args);
	}

	public boolean isShowHeaderMessage() {
		return showHeaderMessage;
	}

	public void setShowHeaderMessage(boolean showHeaderMessage) {
		this.showHeaderMessage = showHeaderMessage;
	}

	public String getMessageStyle() {
		return messageStyle;
	}

	public void setMessageStyle(String messageStyle) {
		this.messageStyle = messageStyle;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	// nes 20110307
	public ArrayList<FGPException> getExceptionList() {
		return exceptionList;
	}

	public void setExceptionList(ArrayList<FGPException> exceptionList) {
		this.exceptionList = exceptionList;
	}
	
}