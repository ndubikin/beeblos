package org.beeblos.bpm.core.graph;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.beeblos.bpm.core.model.WProcessDef;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class MxGraphModel {

	private WProcessDef root;
	
	public MxGraphModel() {
		this.root = new WProcessDef();
	}

	public MxGraphModel(WProcessDef pro) {
		this.root = pro;
	}

	@XmlElement(name="root")
	public WProcessDef getRoot() {
		return root;
	}

	public void setRoot(WProcessDef root) {
		this.root = root;
	}
}
