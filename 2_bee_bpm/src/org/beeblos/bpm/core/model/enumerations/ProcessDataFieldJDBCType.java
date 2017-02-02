package org.beeblos.bpm.core.model.enumerations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sp.common.util.StringPair;

/**
 * If the process data field will synchronize its value with a JDBC action, user can choose 
 * between these three options
 * 
 * @author dmuleiro 20170201
 *
 */
public enum ProcessDataFieldJDBCType {

	TABLE_FIELD(0, "Table field", "It allows user to select one table field to synchronize the value"), 
	QUERY(1, "Query", "It allows user to create a query to synchronize the value"), 
	NAMED_QUERY(2, "Named query", "It allows user to invoke a named query to synchronize the value"), 
	STORED_PROCEDURE(3, "Stored procedure", "It allows user to invoke a procedure to synchronize the value");

	private int code;
	private String name;
	private final String description;

	private ProcessDataFieldJDBCType(int s, String n,  String c) {
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
		for (ProcessDataFieldJDBCType ct : ProcessDataFieldJDBCType.values()) {
			lsp.add(new StringPair(ct.getCode(), ct.getName()));
		}
		return lsp;
	}

	private static final Map<Integer, ProcessDataFieldJDBCType> map;
	static {
		map = new HashMap<Integer, ProcessDataFieldJDBCType>();
		for (ProcessDataFieldJDBCType st : ProcessDataFieldJDBCType.values()) {
			map.put(st.getCode(), st);
		}
	}

	/**
	 * locate and returns ProcessDataFieldJDBCType by code
	 * @param code
	 * @return
	 */
	public static ProcessDataFieldJDBCType findByKey(int code) {
		return map.get(code);
	}

}