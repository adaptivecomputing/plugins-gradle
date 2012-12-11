package com.adaptc.mws.plugins;

import java.util.LinkedHashMap;

/**
 * A map containing String to {@link ReportResource} values.  If entries are attempted to be
 * accessed that do not exist, they are automatically created, allowing a more fluent usage
 * instead of checking for null values before adding resources.  Additionally, this class
 * may be accessed in one of several ways:
 * <ul>
 *     <li>Using the map methods to access entries: {@link #get(Object)}} and {@link #put(Object, Object)}<br/>
 *     	<code>resources.get('resource1').total = 1<br/>
 *     	resources.get({@link PluginConstants#RESOURCE_PROCESSORS}).total = 4</code>
 *     </li>
 *     <li>Accessing entries using array-like notation: {@link #getAt(String)}<br/>
 *         <code>resources['resource1'].total = 1<br/>
 *         resources[{@link PluginConstants#RESOURCE_PROCESSORS}].total = 4</code>
 *     </li>
 *     <li>Accessing entries as properties: {@link #propertyMissing(String)}<br/>
 *         <code>resources.resource1.total = 1<br/>
 *         resources."${{@link PluginConstants#RESOURCE_PROCESSORS}}".total = 4</code>
 *     </li>
 * </ul>
 * @author bsaville
 */
public class ReportResourceMap extends LinkedHashMap<String, ReportResource> {
	private static final long serialVersionUID = 1L;

	/**
	 * Retrieves an entry and creates it if it does not already exist.
	 * @param key The key to retrieve
	 * @return A {@link ReportResource} value, never null
	 */
	@Override
	public ReportResource get(Object key) {
		if (!containsKey(key))
			put((String)key, new ReportResource());
		return super.get(key);
	}

	/**
	 * Allows array-like access to entries.<br/>
	 *  <code>resources['resource1'].total = 1<br/>
	 *  resources[{@link PluginConstants#RESOURCE_PROCESSORS}].total = 4</code>
	 * @param key The key to retrieve
	 * @return A {@link ReportResource} value, never null
	 */
	public ReportResource getAt(String key) {
		return get(key);
	}

	/**
	 * Allows property access to entries.<br/>
	 *  <code>resources.resource1.total = 1<br/>
	 *  resources."${{@link PluginConstants#RESOURCE_PROCESSORS}}".total = 4</code>
	 * @param key The key to retrieve
	 * @return A {@link ReportResource} value, never null
	 */
	public Object propertyMissing(String key) {
		return get(key);
	}
}
