package org.richfaces.demo.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;

@KeepAlive
public class ComponentNavigator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SECURITY_CONTEXT = "contextoSeguridad";

	private static final Log logger = LogFactory.getLog(ComponentNavigator.class);
	
	private String lastCompId = null;

    private List<ComponentDescriptor> components = null;
    private HashMap<String, ComponentDescriptor> componentMap = new HashMap<String, ComponentDescriptor>();

    private String currentComponentId;  //rrl 20111006
    private ComponentDescriptor currentComponent;

    private List<ComponentDescriptor> componentGroups = null;
    
    public ComponentNavigator(){
    	logger.debug("ComponentNavigator()");
    }

    public boolean getHasCurrentComponent() {
        return currentComponent != null;
    }

    //rrl 20111006
	public String getCurrentComponentId() {
		return currentComponentId;
	}

	public void setCurrentComponentId(String currentComponentId) {
		this.currentComponentId = currentComponentId;
	}
    
    public ComponentDescriptor getCurrentComponent() {
    	logger.debug("getCurrentComponent()");
    	
        String id = getComponentParam("c");
        if (id != null) {
            setCurrentComponent(findComponentById(id));
            lastCompId = id;
        } else if (lastCompId != null) {
            setCurrentComponent(findComponentById(lastCompId));
        //rrl 20111006    
        } else if (currentComponentId != null) {
            setCurrentComponent(findComponentById(currentComponentId));
            lastCompId = currentComponentId;
        } else {
            String uri = getComponentUri();
            setCurrentComponent(findComponentByUri(uri));
        }

        // set active tab for current component if any
        if (null != currentComponent) {
            String tab = getComponentParam("tab");
            if (null != tab) {
                currentComponent.setActiveTab(tab);
            }
        }

        //logger.debug("--->>> current component:"+currentComponent.toString());
        return currentComponent;
    }

    private String getComponentUri() {
    	logger.debug("getComponentUri()");
    	
        FacesContext fc = FacesContext.getCurrentInstance();
        return ((HttpServletRequest) fc.getExternalContext().getRequest()).getRequestURI();
    }

    private String getComponentParam(String name) {
    	logger.debug("getComponentParam():name = " + name);
    	
        FacesContext fc = FacesContext.getCurrentInstance();
        String param = (String) fc.getExternalContext().getRequestParameterMap().get(name);
        if (param != null && param.trim().length() > 0) {
            return param;
        } else {
            return null;
        }
    }

    private List<ComponentDescriptor> components_() {
        if (components == null) {
            loadComponents();
        }
        return components;
    }

    @SuppressWarnings("unchecked")
	public ComponentDescriptor findComponentByUri(String uri) {
    	logger.debug("findComponentByUri()");
    	
        Iterator it = components_().iterator();
        while (it.hasNext()) {
            ComponentDescriptor component = (ComponentDescriptor) it.next();
            if (uri.endsWith(component.getDemoLocation())) {
                return component;
            }
        }
        return null;
    }

    public ComponentDescriptor findComponentById(String id) {
    	logger.debug("findComponentById()");
    	
        Iterator<ComponentDescriptor> it = components_().iterator();
        while (it.hasNext()) {
            ComponentDescriptor component = (ComponentDescriptor) it.next();
            if (component.getId().equals(id)) {
                return component;
            }
        }
        return null;
    }

    public void setCurrentComponent(ComponentDescriptor currentComponent) {
    	logger.debug("setCurrentComponent()");
    	
        if (currentComponent == null) {
            this.currentComponent = (ComponentDescriptor) components_().get(0);
        }
        this.currentComponent = currentComponent;
    }

    public List<ComponentDescriptor> getComponentGroups() {
        return componentGroups;
    }

    public void setComponentGroups(List<ComponentDescriptor> componentGroups) {
        this.componentGroups = componentGroups;
    }

    private List<ComponentDescriptor> getFilteredComponents(String group) {
    	logger.debug("getFilteredComponents()");
    	
        List<ComponentDescriptor> ret = new ArrayList<ComponentDescriptor>();
        Iterator<ComponentDescriptor> it = getComponents().iterator();
        while (it.hasNext()) {
            ComponentDescriptor desc = (ComponentDescriptor) it.next();
            if (desc.getGroup().equals(group)) {
                ret.add(desc);
            }
        }
        return ret;
    }

    private boolean checkNewComponents(List<ComponentDescriptor> groups){
    	logger.debug("checkNewComponents()");
    	
    	for (ComponentDescriptor component : groups) {
			if (component.isNewComponent()) return true;
		}
    	return false;
    }
    
    public boolean isValidatorsHasNew() {
        return checkNewComponents(getFilteredComponents("richValidators"));
    }

    public boolean isSelectHasNew() {
        return checkNewComponents(getFilteredComponents("richSelect"));
    }

    public boolean isRichDragDropHasNew() {
        return checkNewComponents(getFilteredComponents("richDragDrop"));
    }

    public boolean isRichDataIteratorsHasNew() {
        return checkNewComponents(getFilteredComponents("richDataIterators"));
    }

    public boolean isRichMenuHasNew() {
        return checkNewComponents(getFilteredComponents("richMenu"));
    }

    public boolean isRichTreeHasNew() {
        return checkNewComponents(getFilteredComponents("richTree"));
    }

    public boolean isRichInputsHasNew() {
        return checkNewComponents(getFilteredComponents("richInputs"));
    }

    public boolean isRichOutputsHasNew() {
        return checkNewComponents(getFilteredComponents("richOutputs"));
    }

    public boolean isAjaxSupportHasNew() {
        return checkNewComponents(getFilteredComponents("ajaxSupport"));
    } 

    public boolean isAjaxResourcesHasNew() {
        return checkNewComponents(getFilteredComponents("ajaxResources"));
    }

    public boolean isAjaxOutputHasNew() {
        return checkNewComponents(getFilteredComponents("ajaxOutput"));
    }
 
    public boolean isAjaxMiscHasNew() {
        return checkNewComponents(getFilteredComponents("ajaxMisc"));
    }

    public boolean isRichMiscHasNew() {
        return checkNewComponents(getFilteredComponents("richMisc"));
    }
    
 // ************************************************************************************
    
    public List getValidatorsComponents() {
        return getFilteredComponents("richValidators");
    }

    public List getSelectComponents() {
        return getFilteredComponents("richSelect");
    }

    public List getRichDragDropComponents() {
        return getFilteredComponents("richDragDrop");
    }

    public List getRichDataIterators() {
        return getFilteredComponents("richDataIterators");
    }

    public List getRichMenu() {
        return getFilteredComponents("richMenu");
    }

    public List getRichTree() {
        return getFilteredComponents("richTree");
    }

    public List getRichInputs() {
        return getFilteredComponents("richInputs");
    }

    public List getRichOutputs() {
        return getFilteredComponents("richOutputs");
    }

    public List getAjaxSupport() {
        return getFilteredComponents("ajaxSupport");
    }

    public List getAjaxResources() {
        return getFilteredComponents("ajaxResources");
    }

    public List getAjaxOutput() {
        return getFilteredComponents("ajaxOutput");
    }

    public List getAjaxMisc() {
        return getFilteredComponents("ajaxMisc");
    }

    public List getRichMisc() {
        return getFilteredComponents("richMisc");
    }

    
    public List getGeneral() {
        return getFilteredComponents("general");
    }
    
    public List getAdmin() {
        return getFilteredComponents("admin");
    }

    public List getDefinitions() {
        return getFilteredComponents("definitions");
    }

    public List getMyaccount() {
        return getFilteredComponents("myaccount");
    }
    
    public List getCrud() {
        return getFilteredComponents("crud");
    }
  
    
    public List getTask() {
        return getFilteredComponents("task");
    }


    
    
// ************************************************************************************    
    public List<ComponentDescriptor> getComponents() {
    	logger.debug("getComponents()");
    	
        Iterator<ComponentDescriptor> it = components_().iterator();
        ComponentDescriptor cur = getCurrentComponent();
        while (it.hasNext()) {
            ComponentDescriptor desc = (ComponentDescriptor) it.next();
            if (desc.equals(cur)) {
                desc.setCurrent(true);
            } else {
                desc.setCurrent(false);
            }
        }
        return components;
    }

    public void setComponents(List<ComponentDescriptor> components) {
        this.components = components;
    }

    private void loadComponents() {
    	logger.debug("loadComponents()");

    	// nes 20110711
    	String permiso=null;
    	HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		ContextoSeguridad cs = (ContextoSeguridad) session.getAttribute(SECURITY_CONTEXT);    	

        Properties props = new Properties();
        List<ComponentDescriptor> temp = new ArrayList<ComponentDescriptor>();
        componentMap.clear();
        try {
            InputStream is = this.getClass().getResourceAsStream(
                    "/org/richfaces/demo/common/components.menu");
            props.load(is);
        } catch (Exception e) {
            throw new FacesException(e);
        }
        Set entries = props.entrySet();
        Iterator it = entries.iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            ComponentDescriptor desc = new ComponentDescriptor();
            String id = e.getKey().toString().trim();
            desc.setId(id);
            StringTokenizer toc = new StringTokenizer(e.getValue().toString(), ",");
            // #id=name,captionImage,iconImage,devGuideLocation,tldDocLocation,javaDocLocation
            desc.setGroup(toc.nextToken().trim());
            desc.setName(toc.nextToken().trim());
            desc.setIconImage(toc.nextToken().trim());
            desc.setCaptionImage(toc.nextToken().trim());
            desc.setDevGuideLocation(toc.nextToken().trim());
            desc.setTldDocLocation(toc.nextToken().trim());
            desc.setJavaDocLocation(toc.nextToken().trim());
            desc.setDemoLocation(toc.nextToken().trim());
            
            // nes 20110711
            if (toc.hasMoreElements() ) {
            		desc.setBean(toc.nextToken().trim());
            }else{ 
            	desc.setBean(null);
            }
             
            if( desc.getBean() != null ){
            	desc.setRutaBean(toc.nextToken().trim());
            }
            if (toc.hasMoreElements() ) {
	        		permiso = toc.nextToken().trim();
	        }else{ 
	        		permiso = null;
	        }            

 //       	System.out.println("----------> menu opc ---------->>>"+desc.getName()+"<-----------------");
 
        	// nes - no tengo claro si mostrar lo que venga con el permiso en null o si no mostrarlo ... comentar con Milton ...
        	if ( permiso!=null && cs.permiteCualquiera( permiso ) ||
        			permiso==null ) {
	            componentMap.put(id, desc);
	            temp.add(desc);
        	}
        }
        Collections.sort(temp, new Comparator() {
            public int compare(Object o1, Object o2) {
                ComponentDescriptor d1 = (ComponentDescriptor) o1;
                ComponentDescriptor d2 = (ComponentDescriptor) o2;
                return d1.getName().compareTo(d2.getName());
            }
        });
        setComponents(temp);
        setCurrentComponent((ComponentDescriptor) temp.get(0));
    }
    
    private void recreateBean(String beanName, String rutaBean, String group){
    	
    	FacesContext faces = FacesContext.getCurrentInstance();
    	
        Map map = faces.getExternalContext().getSessionMap();
        CoreManagedBean bean = (CoreManagedBean) map.get(beanName);
        
        try{ 
        	// Wil 20100819
        	Class c = Class.forName(rutaBean);
        	CoreManagedBean newBean = (CoreManagedBean)c.newInstance();    
        	newBean.setGrupo(group);
        	newBean.init();
        	map.put(beanName, newBean);
        	
        }catch(ClassNotFoundException e) {
        	
        }catch(IllegalAccessException e){
        	
        }catch(InstantiationException e){
        	
        }        
    }

    /**
     * Invoked when example tab panel switched
     * 
     * @param event
     *                a ValueChangeEvent object
     */
    public void tabPanelSwitched(ValueChangeEvent event) {
    	logger.debug("tabPanelSwitched()");
    	
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String viewId = facesContext.getViewRoot().getViewId();
        String actionURL = viewHandler.getActionURL(facesContext, viewId);
        actionURL = patchURL(actionURL, "tab", getCurrentComponent().getActiveTab());
        
        logger.debug("--->>> actionURL ---------->>>>>>> :"+actionURL);
        
        try {
            facesContext.getExternalContext().redirect(actionURL);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Adds/replaces a query parameter in a given "GET request URL"-like string.
     *
     * @param param
     *                name of query parameter
     * @param value
     *                value of query parameter
     * @return string representing parched url
     */
    private String patchURL(String url, String param, String value) {
    	logger.debug("patchURL()");
    	
        String queryPair = param + "=" + value;
        url.replaceAll("[\\?]" + param + "=[\\w]*", "?" + queryPair);
        url.replaceAll("[&]" + param + "=[\\w]*", "&" + queryPair);

        if (!url.contains("?" + param + "=") && !url.contains("&" + param + "=")) {
            if (url.contains("?")) {
                url += "&";
            } else {
                url += "?";
            }
            url += queryPair;
        }

        return url;
    }
    
    public void routerMethod(ActionEvent event){
    	logger.debug("routerMethod()");
    	HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
 	   	try {
 		   
 		String id = (String)FacesContext.getCurrentInstance()
 			        .getExternalContext().getRequestParameterMap().get("paramItem"); 			    
 		   
 		if( id != null ) {
 			ComponentDescriptor cD = componentMap.get(id);
 			
 			logger.debug("-- DemoLocation = " + cD.getDemoLocation() + " --");
 			logger.debug("-- Backend Bean(shortName) = " + cD.getBean() + " --");
 			
 			// Recargar el bean
 			if( cD.getBean() != null ){
 				recreateBean(cD.getBean(), cD.getRutaBean(), cD.getGroup());
 			}
 			
 			// Ir a la p�gina 			
	 		String url = cD.getContextRelativeDemoLocation();	 		   
	 		response.sendRedirect(url);
 		}
 		  
// 		   Object obj = ((HtmlCommandLink)event.getComponent()).getValue();
// 		   
// 		   if( obj != null ){
// 			  String idComp = obj.toString();
// 		 		   
//	 		  ComponentDescriptor cD = componentMap.get(idComp);
//	 		  String url = cD.getContextRelativeDemoLocation();
//	 		   
//	 		  response.sendRedirect(url);   
// 		   }
// 		  
 	    	
	 	} catch (IOException e) {
	 		e.printStackTrace();
	 	}

    }

}
