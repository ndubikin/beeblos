package org.beeblos.bpm.core.graph;

import static com.sp.common.util.ConstantsCommon.EMPTY_OBJECT;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.beeblos.bpm.core.model.WProcessDef;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class MxGraphModel {

	// porque necesito el objeto entero pues hay partes como el name que están en head... nes 20150102 
	private WProcessDef root = new WProcessDef(EMPTY_OBJECT);  
	
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
