package com.adaptc.mws.plugins;

import java.util.List;

/**
 * The job resource management service consists of methods to retrieve
 * and save job reports.  These reports are consolidated and returned to Moab.
 * <p/>
 * For more information on how to use this service, see the MWS Quick Reference
 * page on Job RM Service.
 * @author bsaville
 */
public interface IJobRMService {
	/**
	 * Retrieves a list of all job reports which were given originally by this plugin.
	 * @return A list of all job reports made by this plugin.
	 */
	public List<JobReport> list();
	/**
	 * Saves a list of job reports in the cache while at the same time clearing out any and all job
	 * reports made previously by the calling plugin.  In effect, this replaces all job reports
	 * made previously.  These will be reported to Moab through the Cluster Query.
	 * @param jobReports The list of job reports to save.
	 */
	public void save(List<JobReport> jobReports);
	/**
	 * Adds a list of job reports to the cache without clearing out any job reports.  This should be
	 * used to make incremental updates to the reports.  These will be reported to Moab through the
	 * Cluster Query.
	 * @param jobReports The list of job reports to add.
	 */
	public void update(List<JobReport> jobReports);
}
