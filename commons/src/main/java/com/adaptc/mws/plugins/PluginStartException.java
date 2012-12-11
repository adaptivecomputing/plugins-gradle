package com.adaptc.mws.plugins;

/**
 * Signifies that a plugin was not able to be started without errors.
 * @author bsaville
 */
public class PluginStartException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8142427609642291717L;

	public PluginStartException() {
		super();
	}

	public PluginStartException(String message) {
		super(message);
	}

	public PluginStartException(String message, Exception ex) {
		super(message, ex);
	}
}
