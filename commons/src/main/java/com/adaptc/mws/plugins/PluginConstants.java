package com.adaptc.mws.plugins;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Arrays;

/**
 * This class contains constant values that can be used for reporting
 * resources (i.e. any field that starts with "RESOURCE_" or "METRIC")
 * and custom web services (i.e. {@link #WEB_SERVICES_METHOD}.
 * @author bsaville
 */
public class PluginConstants {
	/**
	 * The processors resource name.
	 * @see NodeReport#resources
	 * @see VirtualMachineReport#resources
	 * @see JobReportRequirement#resourcesPerTask
	 */
	public static final String RESOURCE_PROCESSORS = "processors";
	/**
	 * The memory resource name.
	 * @see NodeReport#resources
	 * @see VirtualMachineReport#resources
	 * @see JobReportRequirement#resourcesPerTask
	 */
	public static final String RESOURCE_MEMORY = "memory";
	/**
	 * The disk resource name.
	 * @see NodeReport#resources
	 * @see VirtualMachineReport#resources
	 * @see JobReportRequirement#resourcesPerTask
	 */
	public static final String RESOURCE_DISK = "disk";
	/**
	 * The swap memory resource name.
	 * @see NodeReport#resources
	 * @see VirtualMachineReport#resources
	 * @see JobReportRequirement#resourcesPerTask
	 */
	public static final String RESOURCE_SWAP = "swap";
	/**
	 * A list of known resource names.  Other resources in the {@link ReportResourceMap}
	 * entries are considered to be generic resources in Moab Workload Manager.
	 * @see NodeReport#resources
	 * @see VirtualMachineReport#resources
	 * @see JobReportRequirement#resourcesPerTask
	 */
	public static final List<String> KNOWN_RESOURCES = Arrays.asList(
			RESOURCE_PROCESSORS,
			RESOURCE_MEMORY,
			RESOURCE_DISK,
			RESOURCE_SWAP
		);

	/**
	 * The CPU utilization metric name.  While this is reported as a "generic" metric to MWM,
	 * this is provided as a standard to use.
	 */
	public static final String METRIC_CPU_UTILIZATION = "cpuUtilization";
	/**
	 * The CPU load metric name.
	 * @see NodeReport#metrics
	 * @see VirtualMachineReport#metrics
	 */
	public static final String METRIC_CPULOAD = "cpuLoad";
	/**
	 * The CPU speed metric name.
	 * @see NodeReport#metrics
	 * @see VirtualMachineReport#metrics
	 */
	public static final String METRIC_SPEED = "speed";
	/**
	 * The VM count metric name.  While this is reported as a "generic" metric to MWM,
	 * this is provided as a standard to use.
	 */
	public static final String METRIC_VM_COUNT = "vmcount";

	/**
	 * A list of known metric names.  Other resources in the "metrics" map
	 * entries are considered to be generic metrics in Moab Workload Manager.
	 * @see NodeReport#metrics
	 * @see VirtualMachineReport#metrics
	 */
	public static final List<String> KNOWN_METRICS = Arrays.asList(
			METRIC_CPULOAD,
			METRIC_SPEED
		);

	/**
	 * The key to the map entry for the single custom web services' argument containing
	 * the current HTTP method.  The value is a string such as GET, POST, PUT, or DELETE.
	 */
	public static final String WEB_SERVICES_METHOD = "MWS_PLUGIN_HTTP_METHOD";

	/**
	 * The standard date format String to be used when communicating with Moab Web Services.
	 * This may be used to parse out dates from requests in custom web services.
	 */
	private static final String STANDARD_DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss z";

	/**
	 * The standard date format to be used when communicating with Moab Web Services.
	 * This may be used to parse out dates from requests in custom web services.
	 * <p/>
	 * <code>
	 *	Date date = STANDARD_DATE_FORMAT.parse("2012-01-01 23:11:24 MDT")
	 * </code>
	 */
	public static final SimpleDateFormat STANDARD_DATE_FORMAT =
			new SimpleDateFormat(STANDARD_DATE_FORMAT_STRING);

	/**
	 * The prefix (including the period) used for all loggers configured for plugin types
	 * and translators.  This should be used in custom component loggers to match the
	 * generated logger names.
	 * <p/>
	 * NOTE: The period (.) <b>is</b> appended to the end of this string. i.e.
	 * <code>Log log = LogFactory.getLog(LOGGER_PREFIX+test.MyClass.name)</code> will result in
	 * a logger called "plugins.test.MyClass".
	 */
	public static final String LOGGER_PREFIX = "plugins.";
}
