package com.adaptc.mws.plugins;

import java.util.List;

/**
 * The node resource management service consists of methods to retrieve
 * and save node reports.  These reports are consolidated and returned to Moab.
 * <p/>
 * For more information on how to use this service, see the MWS Quick Reference
 * page on Node RM Service.
 * @author bsaville
 */
public interface INodeRMService {
	/**
	 * Retrieves a list of all node reports which were given originally by this plugin.
	 * @return A list of all node reports made by this plugin.
	 */
	public List<NodeReport> list();
	/**
	 * Saves a list of node reports in the cache while at the same time clearing out any and all node
	 * reports made previously by the calling plugin.  In effect, this replaces all node reports
	 * made previously.  These will be reported to Moab through the Cluster Query.
	 * @param nodeReports The list of node reports to save.
	 */
	public void save(List<NodeReport> nodeReports);
	/**
	 * Adds a list of node reports to the cache without clearing out any node reports.  This should be
	 * used to make incremental updates to the reports.  These will be reported to Moab through the
	 * Cluster Query.
	 * @param nodeReports The list of node reports to add.
	 */
	public void update(List<NodeReport> nodeReports);
}
