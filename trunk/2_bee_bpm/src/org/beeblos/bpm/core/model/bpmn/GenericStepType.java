package org.beeblos.bpm.core.model.bpmn;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.beeblos.bpm.core.model.enumerations.EventType;
import org.joda.time.DateTime;

/**
 * Defines the Generic Step Type
 * 
 * @author dml 20150414
 *
 */
@XmlRootElement
public class GenericStepType extends GenericStepTypeGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4915439836593940872L;
	
	public GenericStepType(){
		
	}
	
	public GenericStepType(Integer id, String name, EventType eventType, boolean active,
			boolean engineReq, boolean deleted, String comments,
			DateTime insertDate, Integer insertUser, DateTime modDate,
			Integer modUser) {
		this.id = id;
		this.name = name;
		this.eventType = eventType; // nes 20151020
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
		return "GenericStepType [getEventType()=" + getEventType() + ", isActive()="
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
			context = JAXBContext.newInstance(GenericStepType.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter stringWriter = new StringWriter();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(this, stringWriter);
			return stringWriter.toString();			
		} catch (JAXBException e) {
			String mess = "Error marshalling GenericStepType "
					+e.getMessage()+(e.getCause()!=null?". "+e.getCause():" ");
			System.out.println(mess);
		}

		return null;
		
	}

	/**
	 * Unmarshals an XML String and fills the GenericStepType hierarchy
	 * 
	 * @param str
	 * 
	 */
	public void unmarshal(String str)  {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(GenericStepType.class);
			Unmarshaller unmarshaler = context.createUnmarshaller();
			StringReader stringReader = new StringReader(str);
			this.setObj((GenericStepType)unmarshaler.unmarshal(stringReader));			
		} catch (JAXBException e) {
			String mess = "Error unmarshalling GenericStepType "
					+ e.getMessage()+(e.getCause()!=null?". "+e.getCause():" ");
			System.out.println(mess);
		}

	}

	/**
	 * Constructor using object GenericStepType
	 */
	public void setObj(GenericStepType mb) {

		super.setObj(mb);

	}

}
