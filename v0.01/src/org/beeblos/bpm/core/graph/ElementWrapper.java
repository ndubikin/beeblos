package org.beeblos.bpm.core.graph;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ElementWrapper")
public class ElementWrapper {

	List<Symbol> sList;
	List<Layer> lList;
	
	public ElementWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ElementWrapper(List<Symbol> sList) {
		this.sList = sList;
	}
	
	public ElementWrapper(List<Symbol> sList, List<Layer> lList) {
		this.sList = sList;
		this.lList = lList;
	}

	/**
	 * @return the sList
	 */
	@XmlElement(name="Symbol")
	public List<Symbol> getsList() {
		return sList;
	}

	/**
	 * @param sList the sList to set
	 */
	public void setsList(List<Symbol> sList) {
		this.sList = sList;
	}

	/**
	 * @return the lList
	 */
	@XmlElement(name="Layer")
	public List<Layer> getlList() {
		return lList;
	}

	/**
	 * @param lList the lList to set
	 */
	public void setlList(List<Layer> lList) {
		this.lList = lList;
	}
}
