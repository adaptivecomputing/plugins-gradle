package com.adaptc.mws.plugins;

import java.util.List;

/**
 * The VM resource management service consists of methods to retrieve
 * and save VMs reports.  These reports are consolidated and returned to Moab.
 * <p/>
 * For more information on how to use this service, see the MWS Quick Reference
 * page on Virtual Machine RM Service.
 * @author bsaville
 */
public interface IVirtualMachineRMService {
	/**
	 * Retrieves a list of all VM reports which were given originally by this plugin.
	 * @return A list of all VM reports made by this plugin.
	 */
	public List<VirtualMachineReport> list();
	/**
	 * Saves a list of VM reports in the cache while at the same time clearing out any and all VM
	 * reports made previously by the calling plugin.  In effect, this replaces all VM reports
	 * made previously.  These will be reported to Moab through the Cluster Query.
	 * @param virtualMachineReports The list of VM reports to save.
	 */
	public void save(List<VirtualMachineReport> virtualMachineReports);
	/**
	 * Adds a list of VM reports to the cache without clearing out any VM reports.  This should be
	 * used to make incremental updates to the reports.  These will be reported to Moab through the
	 * Cluster Query.
	 * @param virtualMachineReports The list of VM reports to add.
	 */
	public void update(List<VirtualMachineReport> virtualMachineReports);
}
