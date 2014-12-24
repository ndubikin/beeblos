package org.beeblos.bpm.core.model.enumerations;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum StepWorkStatus {

	ALL(0, "All step works"), PENDING(1, "Step work process pending"), PROCESSED(
			2, "Processed step works");

	private int code;
	private final String description;

	private StepWorkStatus(int s, String c) {
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
	private static final Map<Integer, StepWorkStatus> lookup = new HashMap<Integer, StepWorkStatus>();

	// Populate the lookup table on loading time
	static {
		for (StepWorkStatus s : EnumSet.allOf(StepWorkStatus.class))
			lookup.put(s.getCode(), s);
	}

	// This method can be used for reverse lookup purpose
	public static StepWorkStatus get(int code) {
		return (StepWorkStatus) lookup.get(code);
	}

	@Override
	public String toString() {
		// Capitaliza solo la 1era letra
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}