package com.adaptc.mws.plugins;

/**
 * This enumeration represents the current state of a node.
 */
public enum NodeReportState {
	/**
	 * There is no node state associated.
	 */
	NONE("None"),

	/**
	 * The node is not available for workload.
	 */
	DOWN("Down"),

	/**
	 * The node is available for workload but is not running anything.
	 */
	IDLE("Idle"),

	/**
	 * The node is running workload and cannot accept more.
	 */
	BUSY("Busy"),

	/**
	 * The node is running workload and can accept more.
	 */
	RUNNING("Running"),

	/**
	 * The node has been sent the drain request and has no workload on it.
	 */
	DRAINED("Drained"),

	/**
	 * The node has been sent the drain request, but still has workload on it.
	 */
	DRAINING("Draining"),

	/**
	 * The node is being reprovisioned.
	 */
	FLUSH("Flush"),

	/**
	 * The node is being reserved. This is an internal Moab state.
	 */
	RESERVED("Reserved"),

	/**
	 * The state of the node is unknown.
	 */
	UNKNOWN("Unknown"),

	/**
	 * The node is up, but the usage is being determined.
	 */
	UP("Up");

	private String str;

	private NodeReportState(String str) {
		this.str = str;
	}

	/**
	 * Parses a string and translates it into a respective NodeReportState enum value.
	 * Will return null when the string cannot be parsed.
	 * 
	 * @param string The string to parse.
	 * @return The corresponding NodeReportState object or null if not found.
	 */
	public static NodeReportState parse(String string) {
		for(NodeReportState state : values())
			if (state.name().equalsIgnoreCase(string))
				return state;
		return null;
	}

	/**
	 * Returns a human-readable name such as "Unknown" for {@link #UNKNOWN}.
	 * @return Value in a human readable form.
	 */
	public String toString() {
		return str;
	}
}
