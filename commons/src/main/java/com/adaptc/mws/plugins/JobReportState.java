package com.adaptc.mws.plugins;

import java.util.List;
import java.util.Arrays;

/**
 * Represents the various states a job may have at any one time. A job is
 * guaranteed to have at most one state at any given time (but may have many
 * states during the job's lifetime).
 */
public enum JobReportState {
	/**
	 * Eligible according to all resource manager constraints.
	 */
	IDLE ("Idle"),
	/**
	 * Job launching, executing prolog.
	 */
	STARTING ("Starting"),
	/**
	 * Job is executing.
	 */
	RUNNING ("Running"),
	/**
	 * Job canceled before executing.
	 */
	REMOVED ("Removed"),
	/*
	 * Job successfully completed execution.
	 */
	COMPLETED ("Completed"),
	/**
	 * Job is blocked by resource manager hold.
	 */
	HOLD ("Hold"),
	/**
	 * Job temporarily blocked.
	 */
	DEFERRED ("Deferred"),
	SUBMIT_ERROR ("SubmitErr"),
	/**
	 * Job canceled after partial execution.
	 */
	VACATED ("Vacated"),
	NOT_RUN ("NotRun"),
	/**
	 * Job not eligible for execution.
	 */
	NOT_QUEUED ("NotQueued"),
	UNKNOWN ("Unknown"),
	/**
	 * Job has a batch hold in place.
	 */
	BATCH_HOLD ("BatchHold"),
	/**
	 * Job has a user hold in place.
	 */
	USER_HOLD ("UserHold"),
	/**
	 * Job has a system hold in place.
	 */
	SYSTEM_HOLD ("SystemHold"),
	/**
	 * Staging of input/output data is currently underway.
	 */
	STAGING ("Staging"),
	/**
	 * All staging prerequisites are satisfied - waiting for remote resource manager to start.
	 */
	STAGED ("Staged"),
	SUSPENDED ("Suspended"),
	LOST ("Lost"),
	/**
	 * Resources are selected and are being prepared for job.
	 */
	ALLOCATING ("Allocating"),
	/**
	 * State used only by database for job that is idle but has a block reason.
	 */
	BLOCKED ("Blocked");

	/**
	 * The list of states that are "completed" states, meaning the job has errored or has been completed/removed.
	 */
	public static final List<JobReportState> completedStates = Arrays.asList(REMOVED, COMPLETED, SUBMIT_ERROR, VACATED);
	/**
	 * The list of states that are "active" states, meaning the job does not have an error and has not been completed/removed.
	 */
	public static final List<JobReportState> activeStates = Arrays.asList(IDLE, STARTING, RUNNING, HOLD, DEFERRED);

	private String moabLabel;

	private JobReportState(String moabLabel) {
		this.moabLabel = moabLabel;
	}

	/**
	 * Attempts to parse a string and convert it into a corresponding
	 * JobReportState enum value.
	 *
	 * @param string The string to parse into a corresponding JobReportFlag enum value.
	 * @return The corresponding JobReportState value or null if not found.
	 */
	public static JobReportState parse(String string) {
		for(JobReportState state : values())
			if (state.name().equalsIgnoreCase(string) || state.moabLabel.equalsIgnoreCase(string))
				return state;
		return null;
	}
}
