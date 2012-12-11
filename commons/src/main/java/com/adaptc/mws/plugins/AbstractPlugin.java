package com.adaptc.mws.plugins;

import java.util.Map;
import java.util.List;

import org.apache.commons.logging.Log;

/**
 * Optional base class for all plugin types and therefore plugin instances which extends
 * {@link AbstractPluginInfo}.  Contains method definitions to facilitate creating
 * new plugin types.
 * @author bsaville
 */
public abstract class AbstractPlugin extends AbstractPluginInfo {
	// Extra info
	/**
	 * Returns a log that can be used throughout the Plugin.
	 * @return A logging instance
	 */
	public Log getLog() {
		return null;
	}

	/**
	 * Returns an i18n message from any file ending in "-messages.properties" included in the project JAR file.
	 * @param parameters The parameters used to retrieve the message, such as code, error, default, and args.
	 * @return The resulting i18n message
	 */
	public String message(Map parameters) {
		return null;
	}
	
	// Lifecycle control
	/**
	 * Hook to do any initialization needed before the plugin starts.  Can be empty.
	 */
	public void beforeStart() {}
	/**
	 * Starts the Plugin, including polling if enabled.
	 * @throws PluginStartException If there is a problem while starting the Plugin
	 */
	public void start() throws PluginStartException {}
	/**
	 * Hook to do any post-start logic.  Can be empty.
	 */
	public void afterStart() {}
	/**
	 * Hook to start any processes needed to teardown the plugin before it stops.  Can be empty.
	 */
	public void beforeStop() {}
	/**
	 * Stops the Plugin, including polling if started.
	 * @throws PluginStopException If there is a problem while stopping the Plugin
	 */
	public void stop() throws PluginStopException {}
	/**
	 * Hook to do any post-stop teardown of the plugin after it stops.  Can be empty.
	 */
	public void afterStop() {}
	/**
	 * Verifies the configuration of the plugin and performs any initial setup needed each time the configuration
	 * is loaded or changed.  This is called when the {@link IPluginControlService#start} method or the
	 * {@link #start()} method is called, as well as any time the configuration of the Plugin
	 * is modified.
	 * 
	 * @throws InvalidPluginConfigurationException Thrown when configuration is invalid.  This exception should contain error messages.
	 *
	 * @see InvalidPluginConfigurationException#InvalidPluginConfigurationException(String)
	 * @see InvalidPluginConfigurationException#InvalidPluginConfigurationException(List)
	 */
	public void configure() throws InvalidPluginConfigurationException {}

	// plugin events
	/**
	 * Cancels the specified job(s).
	 * @param jobs The name(s) of the job(s) to cancel
	 * @return True if successful, false if an error occurred
	 */
	public boolean jobCancel(List<String> jobs) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Modifies the specified job(s) with the properties given.
	 * @param properties The properties to modify on the job(s)
	 * @param jobs The name(s) of the job(s) to modify
	 * @return True if successful, false if an error occurred
	 */
	public boolean jobModify(List<String> jobs, Map<String, String> properties) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Requeues the specified job(s).
	 * @param jobs The name(s) of the job(s) to requeue
	 * @return True if successful, false if an error occurred
	 */
	public boolean jobRequeue(List<String> jobs) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Resumes the specified job(s).
	 * @param jobs The name(s) of the job(s) to resume
	 * @return True if successful, false if an error occurred
	 */
	public boolean jobResume(List<String> jobs) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Starts the specified job with a tasklist and username.
	 * @param jobName The name of the job to start
	 * @param taskList The tasklist of the job
	 * @param username The user starting the job
	 * @return True if successful, false if an error occurred
	 */
	public boolean jobStart(String jobName, String taskList, String username) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Starts the specified job with a tasklist, username, and other properties.
	 * @param jobName The name of the job to start
	 * @param taskList The tasklist of the job
	 * @param username The user starting the job
	 * @param properties The other properties to set when starting the job
	 * @return True if successful, false otherwise
	 */
	public boolean jobStart(String jobName, String taskList, String username, Map<String, String> properties) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Submits a new job specified by the properties given.
	 * @param properties The job's properties
	 * @return True if successful, false if an error occurred
	 */
	public boolean jobSubmit(Map<String, String> properties) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Suspends the specified job.
	 * @param jobs The name(s) of the job(s)
	 * @return True if successful, false if an error occurred
	 */
	public boolean jobSuspend(List<String> jobs) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Modifies the specified node(s) with the properties given.
	 * @param properties The properties to modify on the node(s)
	 * @param nodes The name(s) of the node(s) to modify
	 * @return True if successful, false if an error occurred
	 */
	public boolean nodeModify(List<String> nodes, Map<String, String> properties) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Changes the power state of the specified node(s).
	 * @param state The new requested power state of the node(s)
	 * @param nodes The name(s) of the node(s) to modify
	 * @return True if successful, false if an error occurred
	 */
	public boolean nodePower(List<String> nodes, NodeReportPower state) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Creates a resource with the specified arguments.
	 * @param type The type of the resource (ie vm)
	 * @param name The name of the resource (ie hv1:vm1 for a VM)
	 * @param attributes The attributes used to create the resource
	 * @return True if successful, false if an error occurred
	 */
	public boolean resourceCreate(String type, String name, Map<String, String> attributes) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Modifies system properties as specified.
	 * @param properties The system properties to modify
	 * @return True if successful, false if an error occurred
	 */
	public boolean systemModify(Map<String, String> properties) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Retrieves the specified system attribute(s).
	 * @param attributes The attributes to query for
	 * @return A list of attribute values
	 */
	public List<String> systemQuery(List<String> attributes) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Migrates a virtual machine with the specified ID to the specified
	 * hypervisor.
	 * @param vmName			The name of the virtual machine
	 * @param hypervisorName	The name of the new desired hypervisor for the VM
	 * @param operationName		The name of the operation
	 * @return True if successful, false if an error occurred
	 */
	public boolean virtualMachineMigrate(String vmName, String hypervisorName, String operationName) {
		throw new UnsupportedOperationException();
	}
}
