package com.adaptc.mws.plugins;

import java.util.Map;
import java.util.List;

/**
 * The control service allows lifecycle management operations to be performed 
 * on plugins.  It also provides methods to create and retrieve plugins.
 * <p/>
 * For more information on how to use this service, see the MWS Quick Reference
 * page on Plugin Control Service.
 * @author bsaville
 */
public interface IPluginControlService {
	/**
	 * Retrieves a plugin by the unique identifier from {@link AbstractPlugin#getId()}.
	 * @param pluginId
	 * @return The plugin or null if it doesn't exist
	 */
	public Object getPluginById(String pluginId);
	/**
	 * Retrieves a list of plugins that are of the specified plugin type.
	 * @param pluginType
	 * @return The plugin list or an empty list if none match the criteria
	 */
	public List<Object> getPlugins(String pluginType);
	/**
	 * Retrieves a list of plugins that are of the specified plugin type and
	 * contain the properties given in {@link AbstractPlugin#getConfig()}.
	 * @param pluginType
	 * @param config
	 * @return The plugin list or an empty list if none match the criteria
	 */
	public List<Object> getPlugins(String pluginType, Map<String, String> config);
	/**
	 * Retrieves a plugin that is of the specified plugin type and contains
	 * the properties given in {@link AbstractPlugin#getConfig()}. If more than
	 * one such plugin exists, return the first such plugin found.
	 * @param pluginType
	 * @param config
	 * @return The first plugin found that matches the criteria or null if no such plugin exists
	 */
	public Object getPlugin(String pluginType, Map<String, String> config);
	/**
	 * Verifies the current configuration of the plugin and notifies the plugin that a configuration change has
	 * occurred.
	 * If errors are encountered, an {@link InvalidPluginConfigurationException} will be
	 * thrown and this will contain the errors in it.
	 * {@link InvalidPluginConfigurationException#getErrors}.
	 * @param pluginId
	 *
	 * @see AbstractPlugin#configure()
	 * @throws InvalidPluginConfigurationException
	 * @throws InvalidPluginException
	 */
	public void configure(String pluginId) throws InvalidPluginException, InvalidPluginConfigurationException;
	/**
	 * Stops a plugin with the specified identifier, including handling of events before
	 * and after stop.
	 * @param pluginId
	 * @throws PluginStopException
	 * @throws InvalidPluginException
	 */
	public void stop(String pluginId) throws PluginStopException, InvalidPluginException;
	/**
	 * Starts a plugin with the specified identifier, including handling of events before
	 * and after start and verifying configuration.
	 * @param pluginId
	 * @throws PluginStartException
	 * @throws InvalidPluginException
	 * @throws InvalidPluginConfigurationException
	 */
	public void start(String pluginId) throws PluginStartException, InvalidPluginException, InvalidPluginConfigurationException;
	/**
	 * Creates a new plugin with the given identifier and plugin type, initializes the bean for it, 
	 * and automatically starts it if {@link AbstractPluginInfo#getAutoStart} is enabled.
	 * @param pluginId
	 * @param pluginType
	 * @return True if successful, false otherwise
	 * @throws InvalidPluginConfigurationException if the id is already taken
	 * @throws PluginStartException if there was a problem starting the plugin after it was created
	 * @throws InvalidPluginTypeException if the plugin type does not exist or is invalid
	 */
	public boolean createPlugin(String pluginId, Class<?> pluginType) throws InvalidPluginConfigurationException, InvalidPluginTypeException, PluginStartException;
	/**
	 * Creates a new plugin with the given identifier, plugin type, and additional properties, initializes bean for it,
	 * and automatically starts it if {@link AbstractPluginInfo#getAutoStart} is enabled.
	 * @param pluginId
	 * @param pluginType
	 * @param properties Map containing configuration properties as defined in {@link AbstractPluginInfo}, including a config Map.
	 * @return True if successful, false otherwise
	 * @throws InvalidPluginConfigurationException if the id is already taken
	 * @throws PluginStartException if there was a problem starting the plugin after it was created
	 * @throws InvalidPluginTypeException if the plugin type does not exist or is invalid
	 */
	public boolean createPlugin(String pluginId, Class<?> pluginType, Map<String, String> properties) throws InvalidPluginConfigurationException, InvalidPluginTypeException, PluginStartException;
	/**
	 * Creates a new plugin with the given identifier and plugin type, initializes bean for it,
	 * and automatically starts it if {@link AbstractPluginInfo#getAutoStart} is enabled.
	 * @param pluginId
	 * @param pluginType
	 * @return True if successful, false otherwise
	 * @throws InvalidPluginConfigurationException if the id is already taken
	 * @throws PluginStartException if there was a problem starting the plugin after it was created
	 * @throws InvalidPluginTypeException if the plugin type does not exist or is invalid
	 */
	public boolean createPlugin(String pluginId, String pluginType) throws InvalidPluginConfigurationException, InvalidPluginTypeException, PluginStartException;
	/**
	 * Creates a new plugin with the given identifier, plugin type, and additional properties, initializes bean for it,
	 * and automatically starts it if {@link AbstractPluginInfo#getAutoStart} is enabled.
	 * @param pluginId
	 * @param pluginType
	 * @param properties Map containing configuration properties as defined in {@link AbstractPluginInfo}, including a config Map.
	 * @return True if successful, false otherwise
	 * @throws InvalidPluginConfigurationException if the id is already taken
	 * @throws PluginStartException if there was a problem starting the plugin after it was created
	 * @throws InvalidPluginTypeException if the plugin type does not exist or is invalid
	 */
	public boolean createPlugin(String pluginId, String pluginType, Map<String, String> properties) throws InvalidPluginConfigurationException, InvalidPluginTypeException, PluginStartException;
	
	//TODO Add async methods with callbacks?
}
