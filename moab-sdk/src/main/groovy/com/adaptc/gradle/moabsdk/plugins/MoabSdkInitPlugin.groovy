package com.adaptc.gradle.moabsdk.plugins

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author bsaville
 */
public class MoabSdkInitPlugin implements Plugin<Project> {
	void apply(Project project) {
		project.repositories.mavenCentral()
		project.repositories.maven({ url "https://oss.sonatype.org/content/groups/public" })

		project.plugins.apply MoabSdkPlugin
        project.ext.sourceCompatibility = JavaVersion.VERSION_1_6
        project.ext.targetCompatibility = JavaVersion.VERSION_1_6
	}
}

