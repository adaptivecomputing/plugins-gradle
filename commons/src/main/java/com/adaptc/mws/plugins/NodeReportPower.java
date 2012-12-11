package com.adaptc.mws.plugins;

/**
 * Represents the various options for a node's power state.
 */
public enum NodeReportPower {
	/**
	 * No or unknown power state.
	 */
	NONE("NONE"),
	/**
	 * The power is on.
	 */
	ON("On"),
	/**
	 * The power is off.
	 */
	OFF("Off"),
	/**
	 * The power is reset.
	 */
	RESET("Reset"),
	/**
	 * The node is in a hibernate mode.
	 */
	HIBERNATE("Hibernate"),
	/**
	 * The node is in a standby mode.
	 */
	STANDBY("Standby");

	private String moabLabel;

	private NodeReportPower(String moabLabel) {
		this.moabLabel = moabLabel;
	}

	/**
	 * Parses a string and translates it into a NodePower enum value.
	 * 
	 * @param string The string to parse.
	 * @return The corresponding NodePower object or null if none is found.
	 */
	public static NodeReportPower parse(String string) {
		if (string==null || string.isEmpty()) {
			return null;
		}
		//trim any white space
		string = string.trim();
		
		for(NodeReportPower power : values())
			if (power.moabLabel.equalsIgnoreCase(string))
				return power;
		return null;
	}

	/**
	 * Returns the actual label used by Moab, i.e. "Reset" for {@link #RESET}.
	 * @return The actual moab label
	 */
	public String toString() {
		return moabLabel;
	}
}
