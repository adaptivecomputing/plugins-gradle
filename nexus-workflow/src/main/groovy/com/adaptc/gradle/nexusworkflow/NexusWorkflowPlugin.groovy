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
		NexusStagingListTask stagingListTask = project.tasks.create("nexusStagingList", NexusStagingListTask)
//		stagingListTask.conventionMapping.map("url") { nexusWorkflowExtension.url }
//		stagingListTask.conventionMapping.map("username") { nexusWorkflowExtension.username }
//		stagingListTask.conventionMapping.map("password") { nexusWorkflowExtension.password }
		stagingListTask.url = getProjectProperty(project, "oss-releases.url")
		stagingListTask.username = getProjectProperty(project, "oss-releases.username")
		stagingListTask.password = getProjectProperty(project, "oss-releases.password")
		stagingListTask.setGroup(NEXUS_WORKFLOW_TASK_GROUP)
		stagingListTask.setDescription("Lists all Nexus staging repositories whether open or closed")

		NexusStagingCloseTask stagingCloseTask = project.tasks.create("nexusStagingClose", NexusStagingCloseTask)
		stagingCloseTask.url = getProjectProperty(project, "oss-releases.url")
		stagingCloseTask.username = getProjectProperty(project, "oss-releases.username")
		stagingCloseTask.password = getProjectProperty(project, "oss-releases.password")
//		stagingCloseTask.conventionMapping.map("url") { nexusWorkflowExtension.url }
//		stagingCloseTask.conventionMapping.map("username") { nexusWorkflowExtension.username }
//		stagingCloseTask.conventionMapping.map("password") { nexusWorkflowExtension.password }
		stagingCloseTask.setGroup(NEXUS_WORKFLOW_TASK_GROUP)
		stagingCloseTask.setDescription("Closes a single Nexus staging repository")

		NexusStagingPromoteTask stagingPromoteTask = project.tasks.create("nexusStagingPromote", NexusStagingPromoteTask)
//		stagingPromoteTask.conventionMapping.map("url") { nexusWorkflowExtension.url }
//		stagingPromoteTask.conventionMapping.map("username") { nexusWorkflowExtension.username }
//		stagingPromoteTask.conventionMapping.map("password") { nexusWorkflowExtension.password }
		stagingPromoteTask.url = getProjectProperty(project, "oss-releases.url")
		stagingPromoteTask.username = getProjectProperty(project, "oss-releases.username")
		stagingPromoteTask.password = getProjectProperty(project, "oss-releases.password")
		stagingPromoteTask.setGroup(NEXUS_WORKFLOW_TASK_GROUP)
		stagingPromoteTask.setDescription("Promotes (releases) a single Nexus staging repository after it has been closed")

		NexusStagingReleaseTask stagingReleaseTask = project.tasks.create("nexusStagingRelease", NexusStagingReleaseTask)
//		stagingReleaseTask.conventionMapping.map("url") { nexusWorkflowExtension.url }
//		stagingReleaseTask.conventionMapping.map("username") { nexusWorkflowExtension.username }
//		stagingReleaseTask.conventionMapping.map("password") { nexusWorkflowExtension.password }
		stagingReleaseTask.url = getProjectProperty(project, "oss-releases.url")
		stagingReleaseTask.username = getProjectProperty(project, "oss-releases.username")
		stagingReleaseTask.password = getProjectProperty(project, "oss-releases.password")
		stagingReleaseTask.setGroup(NEXUS_WORKFLOW_TASK_GROUP)
		stagingReleaseTask.setDescription("Closes and promotes (releases) all open Nexus staging repositories")
	}

	private def getProjectProperty(Project project, String propertyName, defaultValue=null) {
		if (project.hasProperty(propertyName))
			return project."${propertyName}" ?: defaultValue
		return defaultValue
	}
}

