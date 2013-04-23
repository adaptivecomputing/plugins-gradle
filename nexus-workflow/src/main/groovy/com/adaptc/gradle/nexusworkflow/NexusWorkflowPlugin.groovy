package com.adaptc.gradle.nexusworkflow

import org.gradle.api.*
import org.gradle.api.plugins.MavenPlugin

/**
 * @author bsaville
 */
// Based on work from
// 	http://www.sonatype.com/people/2012/07/nexus-pro-automating-staging-workflow-with-gradle-using-the-nexus-rest-apis/
public class NexusWorkflowPlugin implements Plugin<Project> {
	void apply(Project project) {
		// Apply plugins
		project.plugins.apply(MavenPlugin)

		// Add extensions
		NexusWorkflowExtension nexusWorkflowExtension = project.extensions.create(
				NexusWorkflowExtension.NAME, NexusWorkflowExtension)

		// Add tasks and mapping
		NexusStagingCloseTask stagingCloseTask = project.tasks.add("nexusStagingClose", NexusStagingCloseTask)
		stagingCloseTask.conventionMapping.map("url") { nexusWorkflowExtension.url }
		stagingCloseTask.conventionMapping.map("username") { nexusWorkflowExtension.username }
		stagingCloseTask.conventionMapping.map("password") { nexusWorkflowExtension.password }
		NexusStagingListTask stagingListTask = project.tasks.add("nexusStagingList", NexusStagingListTask)
		stagingListTask.conventionMapping.map("url") { nexusWorkflowExtension.url }
		stagingListTask.conventionMapping.map("username") { nexusWorkflowExtension.username }
		stagingListTask.conventionMapping.map("password") { nexusWorkflowExtension.password }
		NexusStagingAutoCloseTask stagingAutoCloseTask = project.tasks.add("nexusStagingAutoClose", NexusStagingAutoCloseTask)
		stagingAutoCloseTask.conventionMapping.map("url") { nexusWorkflowExtension.url }
		stagingAutoCloseTask.conventionMapping.map("username") { nexusWorkflowExtension.username }
		stagingAutoCloseTask.conventionMapping.map("password") { nexusWorkflowExtension.password }
	}
}

