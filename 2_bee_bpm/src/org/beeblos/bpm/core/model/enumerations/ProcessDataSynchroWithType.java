package org.beeblos.bpm.core.model.enumerations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sp.common.util.StringPair;

/**
 * It allows user to synchronize the data field with these three options
 * 
 * @author dmuleiro 20170201
 *
 */
public enum ProcessDataSynchroWithType {

	JDBC(0, "JDBC", ""), 
	APP(1, "App", ""), 
	GIVEN(2, "Given", "");

	private int code;
	private String name;
	private final String description;

	private ProcessDataSynchroWithType(int s, String n,  String c) {
		code = s;
		name = n;
		description = c;
	}

	public String clase() {
		return this.description;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		// Capitaliza solo la 1era letra
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}

	/**
	 * returns ProcessDataFieldJDBCType list as string pair
	 * @return
	 */
	public static List<StringPair> getAsStringPair() {
		List<StringPair> lsp = new ArrayList<StringPair>();
		for (ProcessDataSynchroWithType ct : ProcessDataSynchroWithType.values()) {
			lsp.add(new StringPair(ct.getCode(), ct.getName()));
		}
		return lsp;
	}

	private static final Map<Integer, ProcessDataSynchroWithType> map;
	static {
		map = new HashMap<Integer, ProcessDataSynchroWithType>();
		for (ProcessDataSynchroWithType st : ProcessDataSynchroWithType.values()) {
			map.put(st.getCode(), st);
		}
	}

	/**
	 * locate and returns ProcessDataFieldJDBCType by code
	 * @param code
	 * @return
	 */
	public static ProcessDataSynchroWithType findByKey(int code) {
		return map.get(code);
	}

}