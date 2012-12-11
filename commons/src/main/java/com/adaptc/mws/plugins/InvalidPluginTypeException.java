package com.adaptc.mws.plugins;

/**
 * Signifies that the plugin type specified is invalid and does not exist.
 * @author bsaville
 */
public class InvalidPluginTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5471717572752385451L;

	public InvalidPluginTypeException() {
		super();
	}
	
	public InvalidPluginTypeException(String message) {
		super(message);
	}

	public InvalidPluginTypeException(String message, Exception ex) {
		super(message, ex);
	}
}
