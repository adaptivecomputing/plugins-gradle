package com.adaptc.mws.plugins;

/**
 * Signifies that the plugin specified is invalid and does not exist.
 * @author bsaville
 */
public class InvalidPluginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5471717572752385451L;

	public InvalidPluginException() {
		super();
	}
	
	public InvalidPluginException(String message) {
		super(message);
	}

	public InvalidPluginException(String message, Exception ex) {
		super(message, ex);
	}
}
