package org.beeblos.bpm.core.model.enumerations;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * to indicate the result of a process start ...
 * @author nestor 20151026
 *
 */
public enum StartProcessResult {

	SUCCESS(0, "Process has sarted successfully"), FAIL(1,
			"Process start has failed"), WARNING(2,
			"Process has started with warnings");

	private int code;
	private final String description;

	private StartProcessResult(int s, String c) {
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
	private static final Map<Integer, StartProcessResult> lookup = new HashMap<Integer, StartProcessResult>();

	// Populate the lookup table on loading time
	static {
		for (StartProcessResult s : EnumSet.allOf(StartProcessResult.class))
			lookup.put(s.getCode(), s);
	}

	// This method can be used for reverse lookup purpose
	public static StartProcessResult get(int code) {
		return (StartProcessResult) lookup.get(code);
	}

	@Override
	public String toString() {
		// Capitaliza solo la 1era letra
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}