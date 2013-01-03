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

	void configureMavenDeployment(Project proj) {
		logger.info "Configuring Maven deployment for version ${proj.version}"

		proj.group = MoabSdkUtils.getProperty(proj, "projects.groupId") ?: proj.group
		proj.uploadArchives {
			repositories.mavenDeployer {
				configuration = proj.configurations.deployerJars
				repository(url:MoabSdkUtils.getProperty(proj, "releases.url")) {
					if (MoabSdkUtils.getProperty(proj, "releases.username"))
						authentication(userName:MoabSdkUtils.getProperty(proj, "releases.username"), 
						password:MoabSdkUtils.getProperty(proj, "releases.password"))
				}
				snapshotRepository(url:MoabSdkUtils.getProperty(proj, "snapshots.url")) {
					if (MoabSdkUtils.getProperty(proj, "snapshots.username"))
						authentication(userName:MoabSdkUtils.getProperty(proj, "snapshots.username"), 
							password:MoabSdkUtils.getProperty(proj, "snapshots.password"))
				}

				pom {
					project {
						artifactId((MoabSdkUtils.getProperty(proj, "projects.artifactId.prefix") ?: '') +
								(MoabSdkUtils.getProperty(proj, "project.artifactId") ?: proj.name))
					}
				}
			}
		}
	}
}
