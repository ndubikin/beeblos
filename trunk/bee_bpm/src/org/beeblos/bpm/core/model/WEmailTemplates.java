package org.beeblos.bpm.core.model;

public class WEmailTemplates implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String type;
	private WEmailTemplateGroups wEmailTemplateGroup;
	private String template;
	private String mobileTemplate;

	public WEmailTemplates() {

	}

	public WEmailTemplates(boolean createEmtpyObjects) {
		super();
		if (createEmtpyObjects) {
			this.wEmailTemplateGroup = new WEmailTemplateGroups();

		}
	}

	public WEmailTemplates(Integer id, String name, String type,
			WEmailTemplateGroups wEmailTemplateGroup, String template,
			String mobileTemplate) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.wEmailTemplateGroup = wEmailTemplateGroup;
		this.template = template;
		this.mobileTemplate = mobileTemplate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WEmailTemplateGroups getwEmailTemplateGroup() {
		return wEmailTemplateGroup;
	}

	public void setwEmailTemplateGroup(
			WEmailTemplateGroups wEmailTemplateGroup) {
		this.wEmailTemplateGroup = wEmailTemplateGroup;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getMobileTemplate() {
		return mobileTemplate;
	}

	public void setMobileTemplate(String mobileTemplate) {
		this.mobileTemplate = mobileTemplate;
	}

	@Override
	public String toString() {
		return "WEmailTemplates [id=" + id + ", name=" + name + ", type="
				+ type + ", wEmailTemplateGroup=" + wEmailTemplateGroup
				+ ", template=" + template + ", mobileTemplate="
				+ mobileTemplate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((mobileTemplate == null) ? 0 : mobileTemplate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((template == null) ? 0 : template.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime
				* result
				+ ((wEmailTemplateGroup == null) ? 0
						: wEmailTemplateGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WEmailTemplates other = (WEmailTemplates) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mobileTemplate == null) {
			if (other.mobileTemplate != null)
				return false;
		} else if (!mobileTemplate.equals(other.mobileTemplate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (wEmailTemplateGroup == null) {
			if (other.wEmailTemplateGroup != null)
				return false;
		} else if (!wEmailTemplateGroup
				.equals(other.wEmailTemplateGroup))
			return false;
		return true;
	}

	public boolean empty() {

		if (id != null && !id.equals(0))
			return false;
		if (name != null && !"".equals(name))
			return false;
		if (type != null && !"".equals(type))
			return false;
		if (wEmailTemplateGroup != null && !wEmailTemplateGroup.empty())
			return false;
		if (template != null && !"".equals(template))
			return false;
		if (mobileTemplate != null && !"".equals(mobileTemplate))
			return false;

		return true;

	}

}