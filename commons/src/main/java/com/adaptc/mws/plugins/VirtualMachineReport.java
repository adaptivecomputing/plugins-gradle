package com.adaptc.mws.plugins;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * A VM report consists of values for all of the known, changed properties for a specific VM
 * resource.  This typically represents changes in a specific time period (ie between the last
 * poll and the current poll), but can also represent all known information concerning a resource
 * (ie the first time poll is run).
 * <p/>
 * For more information on reporting resources, see the Plugin Reporting section in the
 * MWS documentation.
 * @author bsaville
 */
public class VirtualMachineReport {
	/**
	 * The VM's name. This should be lower-case, but it will be converted
	 * automatically if it is not in {@link #setName(String)}.
	 */
	private String name;
	/**
	 * The VM's current state.
	 */
	private NodeReportState state;
	/**
	 * The VM's current power state.
	 */
	private NodeReportPower power;
	/**
	 * The date that the report was made or for which the report is current.  This will be
	 * set to the current date and time if not provided.  This may also be set as a string
	 * which will use {@link PluginConstants#STANDARD_DATE_FORMAT} to parse into a date.
	 */
	private Date timestamp;
	/**
	 * The image name for the VM. This is used along with the MWS Image Catalog to retrieve operating
	 * system information.
	 */
	private String image;
	/**
	 * The name of the host (hypervisor/node) that the VM resides on.
	 */
	private String host;
	/**
	 * The VM's resources information.  Note that no null checks are needed to act on the resources.
	 */
	private ReportResourceMap resources = new ReportResourceMap();
	/**
	 * The VM's reported metrics.
	 */
	private Map<String, Double> metrics = new HashMap<String, Double>();
	/**
	 * The VM's variables.
	 */
	private Map<String, String> variables = new HashMap<String, String>();
	/**
	 * The ID of the plugin which has generated the report.  NOTE: This is overridden by
	 * the RM services automatically and should not be set by the plugin.
	 */
	private String pluginId;
	/**
	 * The precedence of the plugin which has generated the report.  NOTE: This is overridden by
	 * the RM services automatically and should not be set by the plugin.
	 */
	private Long precedence;

	/**
	 * @see #name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @see #name
	 */
	public void setName(String name) {
		if (name ==null)
			this.name = null;
		else
			this.name = name.toLowerCase();
	}
	/**
	 * @see #state
	 */
	public NodeReportState getState() {
		return state;
	}
	/**
	 * @see #state
	 */
	public void setState(NodeReportState state) {
		this.state = state;
	}
	/**
	 * Calls {@link NodeReportState#parse(String)} to set the {@link #state} field.
	 * @see #state
	 */
	public void setState(String state) {
		this.state = NodeReportState.parse(state);
	}
	/**
	 * @see #power
	 */
	public NodeReportPower getPower() {
		return power;
	}
	/**
	 * @see #power
	 */
	public void setPower(NodeReportPower power) {
		this.power = power;
	}
	/**
	 * Calls {@link NodeReportPower#parse(String)} to set the {@link #power} field.
	 * @see #power
	 */
	public void setPower(String power) {
		this.power = NodeReportPower.parse(power);
	}
	/**
	 * Retrieves the set timestamp for the report.  NOTE: If no timestamp is provided by the plugin, the
	 * timestamp will be assigned a value of the current date and time.
	 * @see #timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * @see #timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @see #timestamp
	 */
	public void setTimestamp(String timestamp) {
		try {
			this.timestamp = PluginConstants.STANDARD_DATE_FORMAT.parse(timestamp);
		} catch(ParseException e) {
			this.timestamp = null;
		} catch(NullPointerException e) {
			this.timestamp = null;
		}
	}
	/**
	 * @see #image
	 */
	public String getImage() {
		return image;
	}
	/**
	 * Sets the image name for the VM. This is used along with the MWS Image Catalog to retrieve operating
	 * system information.
	 * @see #image
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/**
	 * @see #host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @see #host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @see #resources
	 */
	public ReportResourceMap getResources() {
		return resources;
	}
	/**
	 * @see #resources
	 */
	public void setResources(ReportResourceMap resources) {
		this.resources = resources;
	}
	/**
	 * @see #metrics
	 */
	public Map<String, Double> getMetrics() {
		return metrics;
	}
	/**
	 * @see #metrics
	 */
	public void setMetrics(Map<String, Double> metrics) {
		this.metrics = metrics;
	}
	/**
	 * @see #variables
	 */
	public Map<String, String> getVariables() {
		return variables;
	}
	/**
	 * @see #variables
	 */
	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}
	/**
	 * @see #pluginId
	 */
	public String getPluginId() {
		return pluginId;
	}
	/**
	 * Sets the ID of the plugin which has generated the report.  NOTE: This is overridden by
	 * the RM services automatically and should not be set by the plugin.
	 * @see #pluginId
	 */
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}
	/**
	 * @see #precedence
	 */
	public Long getPrecedence() {
		return precedence;
	}
	/**
	 * Sets the precedence, corresponding to the precedence of the plugin which has generated the report.
	 * NOTE: This is overridden by the RM services automatically and should not be set by the plugin.
	 * @see #precedence
	 */
	public void setPrecedence(Long precedence) {
		this.precedence = precedence;
	}

	/**
	 * Creates a new, empty VM report.
	 */
	public VirtualMachineReport() {}
	/**
	 * Creates a new VM report for specified VM.
	 * @param name See {@link #setName}
	 */
	public VirtualMachineReport(String name) {
		setName(name);
	}
}
