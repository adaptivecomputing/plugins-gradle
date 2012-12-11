package com.adaptc.mws.plugins;

/**
 * Signifies a single resource definition in Moab Workload Manager, including
 * both the {@link #getTotal()} and {@link #getAvailable()} resources.
 * @author bsaville
 */
public class ReportResource {
	private Integer total;
	private Integer available;

	/**
	 * Retrieves the total amount of this resource.
	 * @return The current value
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * Sets the total amount of this resource.
	 * @param total The value to set
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}
	/**
	 * Retrieves the available amount of this resource.
	 * @return The current value
	 */
	public Integer getAvailable() {
		return available;
	}
	/**
	 * Sets the available amount of this resource.
	 * @param available The value to set
	 */
	public void setAvailable(Integer available) {
		this.available = available;
	}

	/**
	 * Returns the available and total amounts as a string, such as "[available:1, total:3]".
	 * @return The total and available amounts in a human-readable string
	 */
	@Override
	public String toString() {
		return "[available:"+available+", total:"+total+"]";
	}
}
