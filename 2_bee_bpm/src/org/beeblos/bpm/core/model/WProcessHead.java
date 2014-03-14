package org.beeblos.bpm.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

public class WProcessHead implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String name;	
	private String comments;
	
	/**
	 * If a Process manage data fields for process interactions or for external data syncrhonization,
	 * a managed table is defined and created. This object has the definition of this table
	 */
	private WProcessHeadManagedDataConfiguration managedTableConfiguration; // data fields table
	
	/**
	 * Each managed table can have a set of WProcessDataField to manage their information ...
	 * This set defines the list of managed fields for this process definition ...
	 */
	private Set<WProcessDataField> processDataFieldDef = new HashSet<WProcessDataField>(0);

	/**
	 *  
	 * External methods allowed to be executed or invoked by this process
	 * Designer/Programmer responsibility to allow context class and method reachable
	 * at execution time 
	 * Invoking external method must be linked with sequence (routes), step (before
	 * load step, after executing step), process (at start time or at end process time), etc. 
	 *
	 * nes 20140207 
	 */
	private Set<WExternalMethod> externalMethod = new HashSet<WExternalMethod>(0);
	
	private DateTime insertDate;
	private Integer insertUser;
	private DateTime modDate;
	private Integer modUser;
	
	public WProcessHead() {
		super();
	}

	public WProcessHead(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			managedTableConfiguration = new WProcessHeadManagedDataConfiguration();
		}	
	}
	
	public WProcessHead(Integer id) {
		this.id = id;
	}

	public WProcessHead(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public WProcessHead(String name, String comments, 
			DateTime fechaAlta, DateTime fechaModificacion) {
		this.name = name;
		this.comments = comments;
		this.insertDate = fechaAlta;
		this.modDate = fechaModificacion;
	}

	public WProcessHead(String name, String comments,
			DateTime insertDate, Integer insertUser, DateTime modDate,
			Integer modUser) {
		this.name = name;
		this.comments = comments;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}	

	public DateTime getInsertDate() {
		return insertDate;
	}


	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}


	public Integer getInsertUser() {
		return insertUser;
	}


	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}


	public DateTime getModDate() {
		return modDate;
	}


	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}


	public Integer getModUser() {
		return modUser;
	}


	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	public WProcessHeadManagedDataConfiguration getManagedTableConfiguration() {
		return managedTableConfiguration;
	}

	public void setManagedTableConfiguration(
			WProcessHeadManagedDataConfiguration managedTableConfiguration) {
		this.managedTableConfiguration = managedTableConfiguration;
	}

	public Set<WProcessDataField> getProcessDataFieldDef() {
		return processDataFieldDef;
	}

	// dml 20130822
	public List<WProcessDataField> getProcessDataFieldDefAsList() {
		if (processDataFieldDef != null){
			return new ArrayList<WProcessDataField>(processDataFieldDef);
		}
		return null;
	}


	public void setProcessDataFieldDef(
			Set<WProcessDataField> processDataFieldDef) {
		this.processDataFieldDef = processDataFieldDef;
	}


	public Set<WExternalMethod> getExternalMethod() {
		return externalMethod;
	}

	public List<WExternalMethod> getExternalMethodAsList() {
		if (externalMethod != null){
			return new ArrayList<WExternalMethod>(externalMethod);
		}
		return null;
	}


	public void setExternalMethod(Set<WExternalMethod> externalMethod) {
		this.externalMethod = externalMethod;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime
				* result
				+ ((managedTableConfiguration == null) ? 0
						: managedTableConfiguration.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((processDataFieldDef == null) ? 0 : processDataFieldDef
						.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WProcessHead))
			return false;
		WProcessHead other = (WProcessHead) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (insertDate == null) {
			if (other.insertDate != null)
				return false;
		} else if (!insertDate.equals(other.insertDate))
			return false;
		if (insertUser == null) {
			if (other.insertUser != null)
				return false;
		} else if (!insertUser.equals(other.insertUser))
			return false;
		if (managedTableConfiguration == null) {
			if (other.managedTableConfiguration != null)
				return false;
		} else if (!managedTableConfiguration
				.equals(other.managedTableConfiguration))
			return false;
		if (modDate == null) {
			if (other.modDate != null)
				return false;
		} else if (!modDate.equals(other.modDate))
			return false;
		if (modUser == null) {
			if (other.modUser != null)
				return false;
		} else if (!modUser.equals(other.modUser))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (processDataFieldDef == null) {
			if (other.processDataFieldDef != null)
				return false;
		} else if (!processDataFieldDef.equals(other.processDataFieldDef))
			return false;
		return true;
	}


	@Override
	public String toString() {
		final int maxLen = 2;
		return "WProcessHead ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (comments != null ? "comments=" + comments + ", " : "")
				+ (managedTableConfiguration != null ? "managedTableConfiguration="
						+ managedTableConfiguration + ", "
						: "")
				+ (processDataFieldDef != null ? "processDataFieldDef="
						+ toString(processDataFieldDef, maxLen) + ", " : "")
				+ (externalMethod != null ? "externalMethod="
						+ toString(externalMethod, maxLen) + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser : "") + "]";
	}


	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}


	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		if (name!=null && ! "".equals(name)) return false;
		if (comments!=null && ! "".equals(comments)) return false;
		
		return true;
	}
	
}
