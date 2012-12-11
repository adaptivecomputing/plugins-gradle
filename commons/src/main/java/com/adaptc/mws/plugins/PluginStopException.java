package com.adaptc.mws.plugins;

/**
 * Signifies that a plugin was not able to be stopped without errors.<br>
 * Contains an additional property that can be retrieved with {@link #getPluginRunning()}.
 * @author bsaville
 */
public class PluginStopException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 711825330682693002L;

	private boolean pluginRunning;
	
	public PluginStopException() {
		pluginRunning = false;
	}

	public PluginStopException(String message) {
		super(message);
		pluginRunning = false;
	}

	public PluginStopException(String message, Exception ex) {
		super(message, ex);
	}
	
	/**
	 * Creates a new exception with the {@link #getPluginRunning()} property set to the
	 * specified value.
	 * @param pluginRunning True if the plugin is running, false otherwise
	 */
	public PluginStopException(boolean pluginRunning) {
		this.pluginRunning = pluginRunning;
	}

	/**
	 * Creates a new exception with the {@link #getPluginRunning()} property set to the
	 * specified value.
	 * @param message A typical exception message
	 * @param pluginRunning True if the plugin is running, false otherwise
	 */
	public PluginStopException(String message, boolean pluginRunning) {
		super(message);
		this.pluginRunning = pluginRunning;
	}

	/**
	 * Creates a new exception with the {@link #getPluginRunning()} property set to the
	 * specified value.
	 * @param message A typical exception message
	 * @param ex An inner exception
	 * @param pluginRunning True if the plugin is running, false otherwise
	 */
	public PluginStopException(String message, Exception ex, boolean pluginRunning) {
		super(message, ex);
		this.pluginRunning = pluginRunning;
	}
	
	/**
	 * <b>This value is currently unused in plugins, but may be used by custom plugin types
	 * to support possible functionality</b>
	 * <p/>
	 * Retrieves whether or not the plugin is actually running after the exception occurred.
	 * @return True if running, false otherwise
	 */
	public boolean getPluginRunning() {
		return pluginRunning;
	}
}
