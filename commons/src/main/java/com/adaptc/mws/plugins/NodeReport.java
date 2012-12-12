package com.adaptc.mws.plugins;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * A node report consists of values for all of the known, changed properties for a specific node
 * resource.  This typically represents changes in a specific time period (ie between the last
 * poll and the current poll), but can also represent all known information concerning a resource
 * (ie the first time poll is run).
 * <p/>
 * For more information on reporting resources, see the Plugin Reporting section in the
 * MWS documentation.
 * @author bsaville
 */
public class NodeReport {
	/**
	 * The node's name. This should be lower-case, but it will be converted
	 * automatically if it is not in {@link #setName(String)}.
	 */
	private String name;
	/**
	 * The date that the report was made or for which the report is current.  This will be
	 * set to the current date and time if not provided.  This may also be set as a string
	 * which will use {@link PluginConstants#STANDARD_DATE_FORMAT} to parse into a date.
	 */
	private Date timestamp;
	/**
	 * The job's resources information.  Note that no null checks are needed to act on the resources.
	 */
	private ReportResourceMap resources = new ReportResourceMap();
	/**
	 * The node's reported metrics.
	 */
	private Map<String, Double> metrics = new HashMap<String, Double>();
	/**
	 * The list of the node's features.
	 */
	private List<String> features = new ArrayList<String>();
	/**
	 * The names of the images that this node can be provisioned as.  This relates to the image name
	 * in the MWS Image Catalog.
	 */
	private List<String> imagesAvailable = new ArrayList<String>();
	/**
	 * A list of messages to attach to the node.  Double quotes (whether escaped or not) will be converted into
	 * single quotes.
	 */
	private List<String> messages = new ArrayList<String>();
	/**
	 * The name of the image that this node is currently provisioned as.  This relates to the image name
	 * in the MWS Image Catalog and hypervisor information (including available virtualized images) is
	 * pulled from the catalog and reported to Moab.
	 */
	private String image;
	/**
	 * The node's partition.
	 */
	private String partition;
	/**
	 * The node's variables.
	 */
	private Map<String, String> variables = new HashMap<String, String>();
	/**
	 * The node's IPv4 address.
	 */
	private String ipAddress;
	/**
	 * The current state of the node's power.
	 */
	private NodeReportPower power;
	/**
	 * The current state of the node.
	 */
	private NodeReportState state;
	/**
	 * The node's current sub-state.
	 */
	private String subState;
	/**
	 * The node's architecture.
	 */
	private String architecture;
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
	 * Sets the unique identifier for the node.  This automatically
	 * lower-cases the ID.
	 * @see #name
	 */
	public void setName(String name) {
		if (name ==null)
			this.name = null;
		else
			this.name = name.toLowerCase();
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
	 * @see #features
	 */
	public List<String> getFeatures() {
		return features;
	}
	/**
	 * @see #features
	 */
	public void setFeatures(List<String> features) {
		this.features = features;
	}
	/**
	 * @see #image
	 */
	public String getImage() {
		return image;
	}
	/**
	 * Sets the image name for the node. This is used along with the MWS Image Catalog to retrieve hypervisor
	 * and supported virtual machine information.
	 * @see #image
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/**
	 * @see #partition
	 */
	public String getPartition() {
		return partition;
	}
	/**
	 * @see #partition
	 */
	public void setPartition(String partition) {
		this.partition = partition;
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
	 * @see #ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	/**
	 * @see #ipAddress
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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
	 * @see #subState
	 */
	public String getSubState() {
		return subState;
	}
	/**
	 * @see #subState
	 */
	public void setSubState(String subState) {
		this.subState = subState;
	}
	/**
	 * @see #architecture
	 */
	public String getArchitecture() {
		return architecture;
	}
	/**
	 * @see #architecture
	 */
	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}
	/**
	 * @see #imagesAvailable
	 */
	public List<String> getImagesAvailable() {
		return imagesAvailable;
	}
	/**
	 * @see #imagesAvailable
	 */
	public void setImagesAvailable(List<String> imagesAvailable) {
		this.imagesAvailable = imagesAvailable;
	}
	/**
	 * @see #messages
	 */
	public List<String> getMessages() {
		return messages;
	}
	/**
	 * @see #messages
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
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
	 * Creates a new, empty node report.
	 */
	public NodeReport() {}
	/**
	 * Creates a new node report for specified node.
	 * @param name See {@link #setName}
	 */
	public NodeReport(String name) {
		setName(name);
	}
}
