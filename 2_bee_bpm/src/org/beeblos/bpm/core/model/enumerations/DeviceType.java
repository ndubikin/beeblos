package org.beeblos.bpm.core.model.enumerations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sp.common.util.String3;

public enum DeviceType {
	DEVICE_TYPE_TABLET(0,"noProcessInfo","there was no information about processes"), 
	DEVICE_TYPE_MOBILE(1,"noProcessExists","there i no process related with object id and object type id");

	private Integer code;
	private String title;
	private String comments;

	private DeviceType(Integer code, String title, String comments) {
		this.title = title;
		this.code = code;
		this.comments = comments;
	}

	public String getTitle() {
		return this.title;
	}

	public Integer getCode() {
		return this.code;
	}
	
	public String getComments(){
		return this.comments;
	}

	@Override
	public String toString() {
		// Capitaliza solo la 1era letra
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}

	public List<DeviceType> getAsList() {
		return new ArrayList<DeviceType>(
				Arrays.asList(DeviceType.values()));
	}

	/**
	 * returns DeviceType list as string pair
	 * @return
	 */
	public static List<String3> getAsString3() {
		List<String3> lsp = new ArrayList<String3>();
		for (DeviceType ct : DeviceType.values()) {
			lsp.add(new String3(ct.getCode().toString(), ct.getTitle(), ct.getComments()));
		}
		return lsp;
	}

	public String3 getString3() {
		return new String3(this.code.toString(), this.title, this.comments);
	}
	
	private static final Map<Integer, DeviceType> map;
	static {
		map = new HashMap<Integer, DeviceType>();
		for (DeviceType st : DeviceType.values()) {
			map.put(st.getCode(), st);
		}
	}

	/**
	 * locate and returns DeviceType by code
	 * @param code
	 * @return
	 */
	public static DeviceType findByKey(int code) {
		return map.get(code);
	}

	public static String getByCode(int code) {
		return map.get(code).getTitle();
	}
}
