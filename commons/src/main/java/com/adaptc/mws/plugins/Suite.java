package com.adaptc.mws.plugins;

/**
 * This enum represents which suite or context Moab Web Services is running in -
 * HPC or Cloud.
 * @author bsaville
 */
public enum Suite {
	HPC("HPC"),
	CLOUD("Cloud");
	
	/**
	 * The default suite for MWS.  This is equivalent to {@link #CLOUD}.
	 */
	public static final Suite DEFAULT_SUITE = Suite.CLOUD;
	
	private String str;
	
	private Suite(String str) {
		this.str = str;
	}

	/**
	 * Returns the suite in a human-readable string, such as "Cloud" for {@link #CLOUD} and "HPC" for
	 * {@link #HPC}.
	 * @return A human-readable string
	 */
	public String toString() {
		return str;
	}
	
	/**
	 * Returns {@link #DEFAULT_SUITE} by default if none is found matching.  This is a
	 * case insensitive match.  Spaces and underscores are also equivalent for parsing.
	 * @param suite The suite as a string
	 * @return The corresponding suite value
	 */
	public static Suite parseString(String suite) {
		for(Suite val : values()) {
			if (val.toString().equalsIgnoreCase(suite) || val.name().equalsIgnoreCase(suite))
				return val;
		}
		return DEFAULT_SUITE;
	}
	
	/**
	 * A helper method to make sure that a true Suite reference may be used.
	 * @param suite An actual suite value
	 * @return The suite parameter
	 */
	public static Suite parseString(Suite suite) {
		return suite;
	}
}
