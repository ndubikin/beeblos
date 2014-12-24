package org.beeblos.bpm.core.model.enumerations;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ProcessWorkStatus {

	ALL(0, "All process works"), ALIVE(1,
			"Active or not finished process works"), PROCESSED(2,
			"Processed process works");

	private int code;
	private final String description;

	private ProcessWorkStatus(int s, String c) {
		code = s;
		description = c;
	}

	public String clase() {
		return this.description;
	}

	public int getCode() {
		return code;
	}

	public String getClase(int code) {
		return this.description;
	}

	// Lookup table
	private static final Map<Integer, ProcessWorkStatus> lookup = new HashMap<Integer, ProcessWorkStatus>();

	// Populate the lookup table on loading time
	static {
		for (ProcessWorkStatus s : EnumSet.allOf(ProcessWorkStatus.class))
			lookup.put(s.getCode(), s);
	}

	// This method can be used for reverse lookup purpose
	public static ProcessWorkStatus get(int code) {
		return (ProcessWorkStatus) lookup.get(code);
	}

	@Override
	public String toString() {
		// Capitaliza solo la 1era letra
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}