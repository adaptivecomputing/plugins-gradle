package com.adaptc.gradle.moabsdk.plugins.mws

import org.gradle.api.*

import com.adaptc.gradle.moabsdk.tasks.ConfigureMavenDeploymentTask
import com.adaptc.gradle.moabsdk.extensions.*

/**
 * @author bsaville
 */
public class MWSProjectPlugin implements Plugin<Project> {
	void apply(Project project) {
		project.plugins.apply MWSProjectBasePlugin
		project.plugins.apply MWSProjectTestingPlugin
		project.plugins.apply 'maven'

		project.compileGroovy {
			source project.tasks['loadPluginProject'].getProjectFile()
		}

		project.artifacts.add("archives", project.tasks.jar)
		project.artifacts.add("archives", project.tasks.sourcesJar)
		project.artifacts.add("archives", project.tasks.javadocJar)

		// Borrowed from grails core build
		project.sourceCompatibility = "1.6"
		project.targetCompatibility = "1.6"

		configureMavenDeployment(project)
	}

	void configureMavenDeployment(Project project) {
		// This line must be present or else the uploadArchives task will not be resolved correct for short
		//	syntax such as uA.  The real config of uA is done in the task itself below.
		project.uploadArchives { }

		// Add extension
		project.moabSdk.extensions.create("maven", SdkMavenExtension)

		// Maven deployment dependencies
		project.configurations {
			deployerJars
		}
		project.dependencies {
			deployerJars "org.apache.maven.wagon:wagon-http:${project.moabSdk.maven.httpJarVersion}"
		}

		// Add task
		def configMaven = project.tasks.add("configureMavenDeployment", ConfigureMavenDeploymentTask)
		project.tasks.compileJava.dependsOn configMaven
		configMaven.dependsOn project.tasks.loadPluginProject
	}
}