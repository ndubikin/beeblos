package org.beeblos.bpm.web.bean;

import java.util.HashSet;
import java.util.Set;

import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.UIRepeat;

public class UpdateBean {

	HtmlInputTextarea observaciones;
	private UIRepeat repeater;
	private Set<Integer> keys = null;
	
	/**
	 * @return the keys
	 */
	public Set getKeys() {
		return keys;
	}

	/**
	 * @param keys the keys to set
	 */
	public void setKeys(Set keys) {
		this.keys = keys;
	}

	public void setRepeater(UIRepeat repeater) {
		this.repeater = repeater;
	}

	public UIRepeat getRepeater() {
		return null;
	}

	public HtmlInputTextarea getObservaciones() {
		return null;
	}

	public void setObservaciones(HtmlInputTextarea observaciones) {
		this.observaciones = observaciones;
	}
	
	public String change(){
		
		HashSet keys = new HashSet<Integer>();
		int rowKey = repeater.getRowIndex();
		keys.add(rowKey);
		setKeys(keys);
		observaciones.processValidators(FacesContext.getCurrentInstance());
		observaciones.processUpdates(FacesContext.getCurrentInstance());
		return null;
	}
}
