package com.adaptc.mws.plugins;

/**
 * This enumeration specifies the flag types of a job. For more information
 * on job flags, see
 * http://www.adaptivecomputing.com/resources/docs/mwm/6-1/Content/jobflagoverview.html
 */
enum JobReportFlag {
	NONE,
	/**
	 * This job is the master of a job array.
	 * @see #ARRAYJOB
	 */
	ARRAYMASTER,
	/**
	 * This job preempted other jobs to start.
	 */
	HASPREEMPTED,
	/**
	 * The {@link #IGNPOLICIES} flag was set by an administrator.
	 */
	ADMINSETIGNPOLICIES,
	/**
	 * The job duration (walltime) was extended at job start.
	 */
	EXTENDSTARTWALLTIME,
	/**
	 * The job will share its memory across nodes.
	 */
	SHAREDMEM,
	/**
	 * The job's generic resource requirement caused the job to start later.
	 */
	BLOCKEDBYGRES,
	/**
	 * The job is requesting only generic resources, no compute resources.
	 */
	GRESONLY,
	/**
	 * The job has had all applicable templates applied to it.
	 */
	TEMPLATESAPPLIED,
	/**
	 * META job, just a container around resources.
	 */
	META,
	/**
	 * This job prefers the wide search algorithm.
	 */
	WIDERSVSEARCHALGO,
	/**
	 * The job is a VMTracking job for an externally-created VM (via job template).
	 */
	VMTRACKING,
	/**
	 * A destroy job has already been created from the template for this job.
	 */
	DESTROYTEMPLATESUBMITTED,
	/**
	 * This array job will only run in one partition.
	 */
	ARRAYJOBPARLOCK,
	/**
	 * This array job will span partitions (default).
	 */
	ARRAYJOBPARSPAN,
	/**
	 * The job is using backfill to run.
	 */
	BACKFILL,
	/**
	 * The job can use resources from multiple resource managers and partitions.
	 */
	COALLOC,
	/**
	 * The job requires use of a reservation.
	 */
	ADVRES,
	/**
	 * The job will attempt to execute immediately or fail.
	 */
	NOQUEUE,
	/**
	 * The job will share reserved resources.
	 */
	ARRAYJOB,
	/**
	 * The job will succeed if even partial resources are available.
	 */
	BESTEFFORT,
	/**
	 * The job is restartable.
	 */
	RESTARTABLE,
	/**
	 * The job is suspendable.
	 */
	SUSPENDABLE,
	/**
	 * The job is a preemptee and therefore can be preempted by other jobs.
	 */
	PREEMPTEE,
	/**
	 * The job is a preemptor and therefore can preempt other jobs.
	 */
	PREEMPTOR,
	/**
	 * The job is based on some reservation.
	 */
	RSVMAP,
	/**
	 * The job was started with a soft policy violation.
	 */
	SPVIOLATION,
	/**
	 * The job will ignore node policies.
	 */
	IGNNODEPOLICIES,
	/**
	 * The job will ignore idle, active, class, partition, and system policies.
	 */
	IGNPOLICIES,
	/**
	 * The job will ignore node state in order to run.
	 */
	IGNNODESTATE,
	/**
	 * The job can ignore idle job reservations. The job granted access to all
	 * idle job reservations.
	 */
	IGNIDLEJOBRSV,
	/**
	 * The job needs to interactive input from the user to run.
	 */
	INTERACTIVE,
	/**
	 * The job was started with a fairshare violation.
	 */
	FSVIOLATION,
	/**
	 * The job is directly submitted without doing any authentication.
	 */
	GLOBALQUEUE,
	/**
	 * The job is a system job that does not need any resources.
	 */
	NORESOURCES,
	/**
	 * The job will not query a resource manager to run.
	 */
	NORMSTART,
	/**
	 * The job is locked into the current cluster and cannot be migrated
	 * elsewhere. This is for grid mode.
	 */
	CLUSTERLOCKED,
	/**
	 * The job can be run across multiple nodes in individual chunks.
	 */
	FRAGMENT,
	/**
	 * The job is a system job which simply runs on the same node that Moab is
	 * running on. This is usually used for running scripts and other
	 * executables in workflows.
	 */
	SYSTEMJOB,
	/**
	 * Job requested processors
	 */
	PROCSPECIFIED,
	/**
	 * Cancel job array on first array job failure
	 */
	CANCELONFIRSTFAILURE,
	/**
	 * Cancel job array on first array job success
	 */
	CANCELONFIRSTSUCCESS,
	/**
	 * Cancel job array on any array job failure
	 */
	CANCELONANYFAILURE,
	/**
	 * Cancel job array on any array job success
	 */
	CANCELONANYSUCCESS,
	/**
	 * Cancel job array on a specific exit code
	 */
	CANCELONEXITCODE,
	/**
	 * VM job cannot be migrated.
	 */
	NOVMMIGRATE;

	/**
	 * Attempts to parse a string and convert it into a corresponding
	 * JobReportFlag enum value.
	 *
	 * @param string The string to parse into a corresponding JobReportFlag enum value.
	 * @return The corresponding JobReportFlag value or {@link #NONE} if not found.
	 */
	static JobReportFlag parse(String string) {
		// A job flag can look like "ADVRES:alice.1"
		if (string.startsWith("ADVRES:"))
			return ADVRES;
		for(JobReportFlag flag : values())
			if (flag.toString().equalsIgnoreCase(string))
				return flag;
		return NONE;
	}
}
