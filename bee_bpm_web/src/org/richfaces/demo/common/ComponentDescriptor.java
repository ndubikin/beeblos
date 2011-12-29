package org.richfaces.demo.common;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ComponentDescriptor implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(ComponentDescriptor.class);

    private String id;

    private String name;

    private String group;

    private String captionImage;

    private String iconImage;

    private String devGuideLocation;

    private String tldDocLocation;

    private String javaDocLocation;

    private String demoLocation;

    private boolean current;

    private String activeTab;
    
    private String bean;
    
	// Wil 20100819
    private String rutaBean;
    
    private boolean newComponent;
    
	// mrico 20110630
    private Boolean permiso;
    
    public boolean isNewComponent() {
		return newComponent;
	}

	public void setNewComponent(boolean newComponent) {
		this.newComponent = newComponent;
	}

	public ComponentDescriptor() {
        this.id = "";
        this.name = "";
        this.captionImage = "";
        this.iconImage = "";
        this.devGuideLocation = "";
        this.tldDocLocation = "";
        this.javaDocLocation = "";
        this.current = false;
        this.activeTab = "caratula";
        this.newComponent=false;
        
        //mrico 20110630
        this.permiso =true;
    }

    public String getCaptionImage() {
        return captionImage;
    }

    public void setCaptionImage(String captionImage) {
        this.captionImage = captionImage;
    }

    public String getDevGuideLocation() {
        return devGuideLocation;
    }

    public void setDevGuideLocation(String devGuideLocation) {
        this.devGuideLocation = devGuideLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJavaDocLocation() {
        return javaDocLocation;
    }

    public void setJavaDocLocation(String javaDocLocation) {
        this.javaDocLocation = javaDocLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTldDocLocation() {
        return tldDocLocation;
    }

    public void setTldDocLocation(String tldDocLocation) {
        this.tldDocLocation = tldDocLocation;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public String getDemoLocation() {
        return demoLocation;
    }

    public void setDemoLocation(String demoLocation) {
        this.demoLocation = demoLocation;
    }

    public String getContextRelativeDemoLocation() {
        FacesContext fc = FacesContext.getCurrentInstance();
        //logger.info("getContextRelativeDemoLocation() = " + fc.getExternalContext().getRequestContextPath() + getDemoLocation());
        return fc.getExternalContext().getRequestContextPath() + getDemoLocation();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTagInfoLocation() {
        int pos = tldDocLocation.indexOf("tlddoc");
        if (pos > 0) {
            return tldDocLocation.substring(pos);
        }
        return "tlddoc/" + tldDocLocation;
    }

    public String getTagCaratulaLocation() {
    	
    	System.out.println(" tag caratula ---->>>>:"+ demoLocation.replaceAll("\\.jsf[\\s]*$", ""));
    	
        return demoLocation.replaceAll("\\.jsf[\\s]*$", "");
    }

    /**
     * Gets value of activeTab field.
     * @return value of activeTab field
     */
    public String getActiveTab() {
        return activeTab;
    }

    /**
     * Set a new value for activeTab field.
     * @param activeTab a new value for activeTab field
     */
    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public String getRutaBean() {
		return rutaBean;
	}

	public void setRutaBean(String rutaBean) {
		this.rutaBean = rutaBean;
	}

	public void setPermiso(Boolean permiso) {
		this.permiso = permiso;
	}

	public Boolean getPermiso() {
		return permiso;
	}

}
