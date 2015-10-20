package org.beeblos.bpm.core.model.enumerations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sp.common.util.StringPair;

/**
 * event type for BPMN activity
 * 
 * @author nes 20151020
 *
 */
public enum EventType {
	
	ACTIVITY(1, "Activity"),
	INITEV(2, "InitEv"),
	INTERMEV(3, "IntermEv"),
	GATEWAY(4, "Gateway"),
	ENDEV(5, "EndEv");

	private Integer code;
	private String title;

	private EventType(Integer code, String title) {
		this.title = title;
		this.code = code;
	}

	public String getTitle() {
		return this.title;
	}

	public Integer getCode() {
		return this.code;
	}

	@Override
	public String toString() {
		// Capitaliza solo la 1era letra
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}

	public List<EventType> getAsList() {
		return new ArrayList<EventType>(
				Arrays.asList(EventType.values()));
	}

	/**
	 * returns EventType list as string pair
	 * @return
	 */
	public static List<StringPair> getAsStringPair() {
		List<StringPair> lsp = new ArrayList<StringPair>();
		for (EventType ct : EventType.values()) {
			lsp.add(new StringPair(ct.getCode(), ct.getTitle()));
		}
		return lsp;
	}

	public StringPair getStringPair() {
		return new StringPair(this.code, this.title);
	}
	
	private static final Map<Integer, EventType> map;
	static {
		map = new HashMap<Integer, EventType>();
		for (EventType st : EventType.values()) {
			map.put(st.getCode(), st);
		}
	}

	/**
	 * locate and returns EventType by code
	 * @param code
	 * @return
	 */
	public static EventType findByKey(int code) {
		return map.get(code);
	}

	public static String getByCode(int code) {
		return map.get(code).getTitle();
	}
}
