package com.adaptc.gradle.nexusworkflow

import org.gradle.api.*
import org.gradle.api.plugins.MavenPlugin

/**
 * @author bsaville
 */
// Based on work from
// 	http://www.sonatype.com/people/2012/07/nexus-pro-automating-staging-workflow-with-gradle-using-the-nexus-rest-apis/
public class NexusWorkflowPlugin implements Plugin<Project> {
	private static final String NEXUS_WORKFLOW_TASK_GROUP = "Upload"

	void apply(Project project) {
		// Apply plugins
		project.plugins.apply(MavenPlugin)

		// Add extensions - this does not work for now, perhaps it will be fixed in the future
//		NexusWorkflowExtension nexusWorkflowExtension = project.extensions.create(
//				NexusWorkflowExtension.NAME, NexusWorkflowExtension)

		// Add tasks and mapping
		NexusStagingCloseTask stagingCloseTask = project.tasks.add("nexusStagingClose", NexusStagingCloseTask)
		stagingCloseTask.url = getProperty(project, "oss-releases.url")
		stagingCloseTask.username = getProperty(project, "oss-releases.username")
		stagingCloseTask.password = getProperty(project, "oss-releases.password")
//		stagingCloseTask.conventionMapping.map("url") { nexusWorkflowExtension.url }
//		stagingCloseTask.conventionMapping.map("username") { nexusWorkflowExtension.username }
//		stagingCloseTask.conventionMapping.map("password") { nexusWorkflowExtension.password }
		stagingCloseTask.setGroup(NEXUS_WORKFLOW_TASK_GROUP)
		stagingCloseTask.setDescription("Closes a single Nexus staging repository")

		NexusStagingListTask stagingListTask = project.tasks.add("nexusStagingList", NexusStagingListTask)
//		stagingListTask.conventionMapping.map("url") { nexusWorkflowExtension.url }
//		stagingListTask.conventionMapping.map("username") { nexusWorkflowExtension.username }
//		stagingListTask.conventionMapping.map("password") { nexusWorkflowExtension.password }
		stagingListTask.url = getProperty(project, "oss-releases.url")
		stagingListTask.username = getProperty(project, "oss-releases.username")
		stagingListTask.password = getProperty(project, "oss-releases.password")
		stagingListTask.setGroup(NEXUS_WORKFLOW_TASK_GROUP)
		stagingListTask.setDescription("Lists all Nexus staging repositories whether open or closed")

		NexusStagingAutoCloseTask stagingAutoCloseTask = project.tasks.add("nexusStagingAutoClose", NexusStagingAutoCloseTask)
//		stagingAutoCloseTask.conventionMapping.map("url") { nexusWorkflowExtension.url }
//		stagingAutoCloseTask.conventionMapping.map("username") { nexusWorkflowExtension.username }
//		stagingAutoCloseTask.conventionMapping.map("password") { nexusWorkflowExtension.password }
		stagingAutoCloseTask.url = getProperty(project, "oss-releases.url")
		stagingAutoCloseTask.username = getProperty(project, "oss-releases.username")
		stagingAutoCloseTask.password = getProperty(project, "oss-releases.password")
		stagingAutoCloseTask.setGroup(NEXUS_WORKFLOW_TASK_GROUP)
		stagingAutoCloseTask.setDescription("Closes all open Nexus staging repositories")
	}

	private def getProperty(Project project, String propertyName, defaultValue=null) {
		if (project.hasProperty(propertyName))
			return project."${propertyName}" ?: defaultValue
		return defaultValue
	}
}

