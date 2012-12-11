package com.adaptc.mws.plugins;

import java.util.List;

/**
 * Contains common methods for a job report requirement object.
 * Note that the {@link JobReportRequirementsList} also implements
 * this interface and can be used directly as a single job report requirement.
 * @author bsaville
 */
public interface IJobReportRequirement {
	/**
	 * Retrieves the minimum number of nodes called for by the requirement.
	 * @return The current value
	 */
	public Integer getNodeCount();

	/**
	 * Sets the minimum number of nodes called for by the requirement.
	 * @param nodeCount The value to set
	 */
	public void setNodeCount(Integer nodeCount);

	/**
	 * Retrieves the minimum task count for this requirement.
	 * @return The current value
	 */
	public Integer getTaskCount();

	/**
	 * Sets the minimum task count for this requirement.
	 * @param taskCount The value to set
	 */
	public void setTaskCount(Integer taskCount);

	/**
	 * Retrieves the number of tasks to map to each node.
	 * @return The current value
	 */
	public Integer getTasksPerNode();

	/**
	 * Sets the number of tasks to map to each node.
	 * @param tasksPerNode The value to set
	 */
	public void setTasksPerNode(Integer tasksPerNode);

	/**
	 * Retrieves the list of required node features for this requirement.
	 * @return The current value
	 */
	public List<String> getFeatures();

	/**
	 * Sets the list of required node features.
	 * @param features The value to set
	 */
	public void setFeatures(List<String> features);

	/**
	 * Retrieves the required architecture.
	 * @return The current value
	 */
	public String getArchitecture();

	/**
	 * Sets the required architecture.
	 * @param architecture The value to set
	 */
	public void setArchitecture(String architecture);

	/**
	 * Retrieves the names of the nodes that are actually allocated to the job.
	 * @return The current value
	 */
	public List<String> getNodes();

	/**
	 * Sets the list of node names that are actually allocated to the job.
	 * @param nodes The value to set
	 */
	public void setNodes(List<String> nodes);

	/**
	 * Retrieve the job's resources information.  Note that no null checks are needed to act on the resources.
	 * {@link PluginConstants#RESOURCE_DISK}, {@link PluginConstants#RESOURCE_SWAP}, and {@link PluginConstants#RESOURCE_MEMORY}
	 * are used as key values, {@link PluginConstants#RESOURCE_PROCESSORS} is ignored, and all key values are reported as
	 * generic resources.
	 * <p/>
	 * For each entry, the total value is used if set.  If not set, the available value is used.
	 * @return The current value
	 */
	public ReportResourceMap getResourcesPerTask();

	/**
	 * Sets the job's resources requirements.  {@link PluginConstants#RESOURCE_DISK}, {@link PluginConstants#RESOURCE_SWAP},
	 * and {@link PluginConstants#RESOURCE_MEMORY} are used as key values, {@link PluginConstants#RESOURCE_PROCESSORS}
	 * is ignored, and all key values are reported as generic resources.
	 * <p/>
	 * For each entry, the total value is used if set.  If not set, the available value is used.
	 * @param resourcesPerTask The value to set
	 */
	public void setResourcesPerTask(ReportResourceMap resourcesPerTask);
}
