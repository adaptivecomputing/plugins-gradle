package com.adaptc.mws.plugins;

/**
 * Represents the current state of a plugin.
 * @author bsaville
 */
public enum PluginState {
	/**
	 * The plugin is created and ready for use, but is not 
	 * currently receiving any events
	 */
	STOPPED("Stopped"),
	/**
	 * The plugin is currently receiving events and is working 
	 * correctly.
	 */
	STARTED("Started"),
	/**
	 * The plugin is currently not receiving any events but is
	 * also not stopped.
	 * <p/>
	 * This should be used when polling or other events should
	 * stop only temporarily without firing the stop events.
	 */
	PAUSED("Paused"),
	/**
	 * MWS has detected an error with the plugin and has automatically 
	 * stopped it.  Errors could be due to the following reasons:<br>
	 * <ol>
	 * <li>An invalid configuration was detected when running the 
	 * {@link AbstractPlugin#configure} method.
	 * <li>An unexpected exception was thrown during an event, such 
	 * as during polling.
	 * </ol>
	 */
	ERRORED("Errored");
	
	private String str;
	private PluginState(String str) {
		this.str = str;
	}

	/**
	 * Returns a human-readable string of the state, such as "Paused" for {@link #PAUSED}.
	 * @return A human-readable string
	 */
	@Override
	public String toString() {
		return str;
	}
}
