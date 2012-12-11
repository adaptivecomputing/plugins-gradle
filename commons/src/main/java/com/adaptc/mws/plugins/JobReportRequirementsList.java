package com.adaptc.mws.plugins;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of {@link JobReportRequirement} objects.  This class also has helper methods
 * to act on the it as if it were a JobReportRequirement.  In reality these methods act
 * on the first element in the list.
 * @author bsaville
 */
public class JobReportRequirementsList extends ArrayList<JobReportRequirement> implements IJobReportRequirement {
	/**
	 * Returns the first element of the list.  Creates the element if it does
	 * not exist.
	 * @return The first element of the list.
	 */
	public JobReportRequirement getFirst() {
		if (this.size()==0)
			this.add(new JobReportRequirement());
		return this.get(0);
	}

	/**
	 * Creates a new {@link JobReportRequirement} and adds to the list.  This
	 * element is then returned.
	 * @return A new requirement object which was added to the list.
	 */
	public JobReportRequirement add() {
		JobReportRequirement requirement = new JobReportRequirement();
		this.add(requirement);
		return requirement;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getNodeCount() {
		return getFirst().getNodeCount();
	}
	/**
	 * {@inheritDoc}
	 */
	public void setNodeCount(Integer nodeCount) {
		getFirst().setNodeCount(nodeCount);
	}
	/**
	 * {@inheritDoc}
	 */
	public Integer getTaskCount() {
		return getFirst().getTaskCount();
	}
	/**
	 * {@inheritDoc}
	 */
	public void setTaskCount(Integer taskCount) {
		getFirst().setTaskCount(taskCount);
	}
	/**
	 * {@inheritDoc}
	 */
	public Integer getTasksPerNode() {
		return getFirst().getTasksPerNode();
	}
	/**
	 * {@inheritDoc}
	 */
	public void setTasksPerNode(Integer tasksPerNode) {
		getFirst().setTasksPerNode(tasksPerNode);
	}
	/**
	 * {@inheritDoc}
	 */
	public List<String> getFeatures() {
		return getFirst().getFeatures();
	}
	/**
	 * {@inheritDoc}
	 */
	public void setFeatures(List<String> features) {
		getFirst().setFeatures(features);
	}
	/**
	 * {@inheritDoc}
	 */
	public List<String> getNodes() {
		return getFirst().getNodes();
	}
	/**
	 * {@inheritDoc}
	 */
	public void setNodes(List<String> nodes) {
		getFirst().setNodes(nodes);
	}
	/**
	 * {@inheritDoc}
	 */
	public ReportResourceMap getResourcesPerTask() {
		return getFirst().getResourcesPerTask();
	}
	/**
	 * {@inheritDoc}
	 */
	public void setResourcesPerTask(ReportResourceMap resourcesPerTask) {
		getFirst().setResourcesPerTask(resourcesPerTask);
	}
	/**
	 * {@inheritDoc}
	 */
	public String getArchitecture() {
		return getFirst().getArchitecture();
	}
	/**
	 * {@inheritDoc}
	 */
	public void setArchitecture(String architecture) {
		getFirst().setArchitecture(architecture);
	}
}
