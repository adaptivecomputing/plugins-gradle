package com.adaptc.gradle.moabsdk.mws

import org.gradle.api.*

/**
 * @author bsaville
 */
public class MWSInitPlugin implements Plugin<Project> {
	void apply(Project project) {
		project.repositories.mavenCentral()

		project.plugins.apply MWSPlugin
        project.ext.sourceCompatibility = JavaVersion.VERSION_1_6
        project.ext.targetCompatibility = JavaVersion.VERSION_1_6
	}
}

