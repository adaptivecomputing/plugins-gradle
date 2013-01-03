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

		proj.group = MoabSdkUtils.getProperty(proj, "projects.groupId")
		proj.uploadArchives {
			repositories.mavenDeployer {
				configuration = proj.configurations.deployerJars
				repository(url:rootProject."releases.url") {
					if (rootProject.hasProperty("releases.username"))
						authentication(userName:rootProject."releases.username", password:rootProject."releases.password")
				}
				snapshotRepository(url:rootProject."snapshots.url") {
					if (rootProject.hasProperty("snapshots.username"))
						authentication(userName:rootProject."snapshots.username", password:rootProject."snapshots.password")
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
