package com.adaptc.mws.plugins;

import java.util.ArrayList;
import java.util.List;

/**
 * A single requirement for a job definition.
 * @author bsaville
 */
public class JobReportRequirement implements IJobReportRequirement {
	/**
	 * @see #getNodeCount()
	 */
	private Integer nodeCount;
	/**
	 * @see #getTaskCount()
	 */
	private Integer taskCount;
	/**
	 * @see #getTasksPerNode()
	 */
	private Integer tasksPerNode;
	/**
	 * @see #getFeatures()
	 */
	private List<String> features = new ArrayList<String>();
	/**
	 * @see #getArchitecture()
	 */
	private String architecture;
	/**
	 * @see #getNodes()
	 */
	private List<String> nodes = new ArrayList<String>();
	/**
	 * @see #getResourcesPerTask()
	 */
	private ReportResourceMap resourcesPerTask = new ReportResourceMap();

	/**
	 * {@inheritDoc}
	 */
	public Integer getNodeCount() {
		return nodeCount;
	}
	/**
	 * {@inheritDoc}
	 */
	public void setNodeCount(Integer nodeCount) {
		this.nodeCount = nodeCount;
	}
	/**
	 * {@inheritDoc}
	 */
	public Integer getTaskCount() {
		return taskCount;
	}
	/**
	 * {@inheritDoc}
	 */
	public void setTaskCount(Integer taskCount) {
		this.taskCount = taskCount;
	}
	/**
	 * {@inheritDoc}
	 */
	public Integer getTasksPerNode() {
		return tasksPerNode;
	}
	/**
	 * {@inheritDoc}
	 */
	public void setTasksPerNode(Integer tasksPerNode) {
		this.tasksPerNode = tasksPerNode;
	}
	/**
	 * {@inheritDoc}
	 */
	public List<String> getFeatures() {
		return features;
	}
	/**
	 * {@inheritDoc}
	 */
	public void setFeatures(List<String> features) {
		this.features = features;
	}
	/**
	 * {@inheritDoc}
	 */
	public String getArchitecture() {
		return architecture;
	}
	/**
	 * {@inheritDoc}
	 */
	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}
	/**
	 * {@inheritDoc}
	 */
	public List<String> getNodes() {
		return nodes;
	}
	/**
	 * {@inheritDoc}
	 */
	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}
	/**
	 * {@inheritDoc}
	 */
	public ReportResourceMap getResourcesPerTask() {
		return resourcesPerTask;
	}
	/**
	 * {@inheritDoc}
	 */
	public void setResourcesPerTask(ReportResourceMap resourcesPerTask) {
		this.resourcesPerTask = resourcesPerTask;
	}
}
