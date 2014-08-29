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

	void configureMavenDeployment(Project projectParam) {
		logger.info "Configuring Maven deployment for version ${projectParam.version}"

		projectParam.group = MoabSdkUtils.getProperty(projectParam, "projects.groupId") ?: projectParam.group
		projectParam.uploadArchives {
			repositories.mavenDeployer {
				configuration = projectParam.configurations.deployerJars
				repository(url:MoabSdkUtils.getProperty(projectParam, "releases.url")) {
					if (MoabSdkUtils.getProperty(projectParam, "releases.username"))
						authentication(userName:MoabSdkUtils.getProperty(projectParam, "releases.username"),
						password:MoabSdkUtils.getProperty(projectParam, "releases.password"))
				}
				snapshotRepository(url:MoabSdkUtils.getProperty(projectParam, "snapshots.url")) {
					if (MoabSdkUtils.getProperty(projectParam, "snapshots.username"))
						authentication(userName:MoabSdkUtils.getProperty(projectParam, "snapshots.username"),
							password:MoabSdkUtils.getProperty(projectParam, "snapshots.password"))
				}

				pom {
					project {
						artifactId((MoabSdkUtils.getProperty(projectParam, "projects.artifactId.prefix") ?: '') +
								(MoabSdkUtils.getProperty(projectParam, "project.artifactId") ?: projectParam.name))
					}
				}
			}
		}
	}
}
