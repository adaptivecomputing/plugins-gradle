package com.adaptc.gradle.moabsdk.tasks

import org.gradle.api.*
import org.gradle.api.tasks.*
import com.adaptc.gradle.moabsdk.utils.MoabSdkUtils

/**
 * This task sets the maven deployment configuration for the given project.  This is done in a task
 * to be able to use the actual set project version information from the project file.
 * @see LoadProjectTask
 * @author bsaville
 */
public class ConfigureMavenDeploymentTask extends DefaultTask {
	@TaskAction public void configureMaven() {
		configureMavenDeployment(project)
	}

	void configureMavenDeployment(Project project) {
		logger.info "Configuring Maven deployment for version ${project.version}"

		project.group = MoabSdkUtils.getProperty(project, "projects.groupId") ?: project.group
		project.uploadArchives {
			repositories.mavenDeployer {
				configuration = project.configurations.deployerJars
				repository(url:MoabSdkUtils.getProperty(project, "releases.url")) {
					if (MoabSdkUtils.getProperty(project, "releases.username"))
						authentication(userName:MoabSdkUtils.getProperty(project, "releases.username"),
						password:MoabSdkUtils.getProperty(project, "releases.password"))
				}
				snapshotRepository(url:MoabSdkUtils.getProperty(project, "snapshots.url")) {
					if (MoabSdkUtils.getProperty(project, "snapshots.username"))
						authentication(userName:MoabSdkUtils.getProperty(project, "snapshots.username"),
							password:MoabSdkUtils.getProperty(project, "snapshots.password"))
				}

				pom {
					project {
						artifactId((MoabSdkUtils.getProperty(project, "projects.artifactId.prefix") ?: '') +
								(MoabSdkUtils.getProperty(project, "project.artifactId") ?: project.name))
					}
				}
			}
		}
	}
}
