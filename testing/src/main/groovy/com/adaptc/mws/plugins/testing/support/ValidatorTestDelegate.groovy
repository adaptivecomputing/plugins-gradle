package com.adaptc.mws.plugins.testing.support

import org.apache.commons.lang.StringUtils

/**
 * @author bsaville
 */
class ValidatorTestDelegate {
	private final String propertyName;
	private final Object pluginInstance;

	public ValidatorTestDelegate(String propertyName, Object pluginInstance) {
		this.propertyName = propertyName
		this.pluginInstance = pluginInstance
	}

	@SuppressWarnings("unused")
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Equivalent to getService("applicationContext")
	 */
	@SuppressWarnings("unused")
	public Object getApplicationContext() {
		return getService("applicationContext");
	}

	/**
	 * Retrieves a service from the plugin instance directly.  This requires the plugin instance to have
	 * the bean injected onto it, but this is a reasonable assumption for testing.
	 */
	@SuppressWarnings("unused")
	public Object getService(String name) {
		if (name==null)
			return null;
		try {
			return pluginInstance."get${StringUtils.capitalize(name)}"()
		} catch(MissingMethodException e) {
			throw new Exception("The service ${name} must exist on the plugin instance in order to retrieve it "+
					"for testing")
		}
	}
}