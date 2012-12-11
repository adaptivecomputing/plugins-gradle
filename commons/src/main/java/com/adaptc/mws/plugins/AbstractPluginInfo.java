package com.adaptc.mws.plugins;

import java.util.Map;

/**
 * Base class for plugin information.  This is used internally and should not be
 * extended directly.
 * @author bsaville
 */
public abstract class AbstractPluginInfo {
	private String id;
	
	/**
	 * Retrieves the unique identifier for the plugin.
	 * @return The unique ID
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets the unique identifier for the plugin.
	 * @param id The new ID to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * Retrieves the plugin type, as in "Native".
	 * @return The plugin type name (without the "Plugin" suffix)
	 */
	public String getPluginType() {
		return null;
	}
	/**
	 * Retrieves the current state of the Plugin.
	 * @return The current state
	 */
	public PluginState getState() {
		return null;
	}
	/**
	 * Retrieves the current substate of the Plugin.  This can signify the reason for an errored state
	 * or a description providing more information than that of the "state" field.
	 * @return The current substate
	 */
	public String getSubstate() {
		return null;
	}
	/**
	 * Retrieves the current polling interval of the plugin in seconds.
	 * @return The polling interval in seconds
	 */
	public Integer getPollInterval() {
		return null;
	}
	/**
	 * Retrieves whether or not the plugin starts automatically on initialization.
	 * @return True if starts automatically, false otherwise
	 */
	public Boolean getAutoStart() {
		return null;
	}
	/**
	 * Retrieves the current configuration of the plugin as a Map of key-value pairs.
	 * @return A map of key-value pairs representing the configuration
	 */
	public Map<String, Object> getConfig() {
		return null;
	}
}
