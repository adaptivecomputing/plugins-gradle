package com.adaptc.gradle.moabsdk.plugins

import org.gradle.api.*

/**
 * @author bsaville
 */
public class MoabSdkInitPlugin implements Plugin<Project> {
	void apply(Project project) {
		project.repositories.mavenCentral()
		project.repositories.mavenRepo(url:"https://oss.sonatype.org/content/groups/public")

		project.plugins.apply MoabSdkPlugin
        project.ext.sourceCompatibility = JavaVersion.VERSION_1_6
        project.ext.targetCompatibility = JavaVersion.VERSION_1_6
	}
}

