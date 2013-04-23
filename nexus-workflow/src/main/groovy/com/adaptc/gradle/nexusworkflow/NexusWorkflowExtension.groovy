package com.adaptc.gradle.nexusworkflow

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.api.plugins.cobertura.CoberturaPluginExtension

/**
 * @author bsaville
 */
class NexusWorkflowExtension {
	public static final NAME = "nexus"

	String url
	String username
	String password
}
