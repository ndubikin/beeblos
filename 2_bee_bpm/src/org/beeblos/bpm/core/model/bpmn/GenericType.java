package org.beeblos.bpm.core.model.bpmn;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

/**
 * Defines the Generic Type
 * 
 * @author dml 20150414
 *
 */
@XmlRootElement
public class GenericType extends InitEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4915439836593940872L;
	
	public GenericType(){
		
	}
	
	public GenericType(Integer id, String name, String type, boolean active,
			boolean engineReq, boolean deleted, String comments,
			DateTime insertDate, Integer insertUser, DateTime modDate,
			Integer modUser) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.active = active;
		this.engineReq = engineReq;
		this.deleted = deleted;
		this.comments = comments;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
	}

	@Override
	public String toString() {
		return "GenericType [getType()=" + getType() + ", isActive()="
				+ isActive() + ", isEngineReq()=" + isEngineReq() + ", isDeleted()=" + isDeleted() + ", getComments()="
				+ getComments() + ", getInsertDate()=" + getInsertDate() + ", getInsertUser()=" + getInsertUser()
				+ ", getModDate()=" + getModDate() + ", getModUser()=" + getModUser() + ", getId()=" + getId()
				+ ", getName()=" + getName() + ", getAllowedResponses()=" + getAllowedResponses() + ", toString()="
				+ super.toString() + ", getClass()=" + getClass() + "]";
	}

	/**
	 * Returns this object into XML format (into a String)
	 * 
	 * @return
	 */
	public String marshal()  {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(GenericType.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter stringWriter = new StringWriter();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(this, stringWriter);
			return stringWriter.toString();			
		} catch (JAXBException e) {
			String mess = "Error marshalling GenericType "
					+e.getMessage()+(e.getCause()!=null?". "+e.getCause():" ");
			System.out.println(mess);
		}

		return null;
		
	}

	/**
	 * Unmarshals an XML String and fills the GenericType hierarchy
	 * 
	 * @param str
	 * 
	 */
	public void unmarshal(String str)  {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(GenericType.class);
			Unmarshaller unmarshaler = context.createUnmarshaller();
			StringReader stringReader = new StringReader(str);
			this.setObj((GenericType)unmarshaler.unmarshal(stringReader));			
		} catch (JAXBException e) {
			String mess = "Error unmarshalling GenericType "
					+ e.getMessage()+(e.getCause()!=null?". "+e.getCause():" ");
			System.out.println(mess);
		}

	}

	/**
	 * Constructor using object GenericType
	 */
	public void setObj(GenericType mb) {

		super.setObj(mb);

	}

}
