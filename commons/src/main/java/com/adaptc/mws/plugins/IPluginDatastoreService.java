package com.adaptc.mws.plugins;

import java.util.Map;
import java.util.List;

/**
 * The individual datastore service is provided to allow a plugin to persist 
 * data to the database that is isolated from all other persistent data.  It is 
 * not designed to store Moab data such as nodes, jobs, or virtual machines, 
 * but custom, arbitrary data pertinent only to the individual plugin.
 * <p/>
 * For more information on how to use this service, see the MWS Quick Reference
 * page on Plugin Datastore Service.
 * @author bsaville
 */
public interface IPluginDatastoreService {
	/**
	 * Returns true if the collection exists, false otherwise.
	 * @param collection
	 * @return True if it exists, false otherwise
	 */
	public boolean exists(String collection);
	/**
	 * Retrieves the entire contents of a collection.
	 * @param collection
	 * @return The contents of a collection, or null if it does not exist
	 */
	public List<Map<String, Object>> getCollection(String collection);
	/**
	 * Retrieves an entry in the collection where key = value.
	 * @param collection
	 * @param key
	 * @param value
	 * @return The matched entry or null if it does not exist
	 */
	public Map<String, Object> getData(String collection, String key, Object value);
	/**
	 * Adds an entry to the collection.  The collection will be created automatically if it does
	 * not already exist
	 * @param collection
	 * @param data
	 * @return True on success, false on failure.
	 */
	public boolean addData(String collection, Map<String, Object> data);
	/**
	 * Adds a list of entries to the collection.  The collection will be created automatically 
	 * if it does not already exist
	 * @param collection
	 * @param data
	 * @return True on success, false on failure.
	 */
	public boolean addData(String collection, List<Map<String, Object>> data);
	/**
	 * Updates a record in the datastore where key = value to the new data
	 * @param collection
	 * @param key
	 * @param value
	 * @param data
	 * @return True on success, false on failure
	 */
	public boolean updateData(String collection, String key, Object value, Map<String, Object> data);
	/**
	 * Clears a collection completely and returns the contents.
	 * @param collection
	 * @return The contents of the collection, or null if it does not exist
	 */
	public List<Map<String, Object>> clearCollection(String collection);
	/**
	 * Removes and returns an entry from the collection where key = value.
	 * @param collection
	 * @param key
	 * @param value
	 * @return True on success, false on failure
	 */
	public boolean removeData(String collection, String key, Object value);
}