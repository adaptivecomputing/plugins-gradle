package com.adaptc.mws.plugins;

import java.util.ArrayList;
import java.util.List;

/**
 * An exception signifying that a misconfiguration error has occurred.  Contains
 * a list of errors that can be retrieved using {@link #getErrors()} and is set in the
 * constructor.
 * @author bsaville
 */
public class InvalidPluginConfigurationException extends Throwable {
	private List<String> errors;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5471717572752385452L;

	/**
	 * Sets the errors to the list provided.
	 * @param errors The list of errors
	 */
	public InvalidPluginConfigurationException(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * Creates an exception with a list of errors consisting of a single element.
	 * @param error The single error
	 */
	public InvalidPluginConfigurationException(String error) {
		this.errors = new ArrayList<String>();
		this.errors.add(error);
	}

	/**
	 * Retrieves the list of error messages set in the constructor.  This will never
	 * be null.
	 * @return List of error message or empty list.
	 */
	public List<String> getErrors() {
		if (errors==null)
			return new ArrayList<String>();
		return errors;
	}
}
