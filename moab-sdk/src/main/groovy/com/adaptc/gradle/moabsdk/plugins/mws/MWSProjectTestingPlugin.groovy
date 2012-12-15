package com.adaptc.gradle.moabsdk.plugins.mws

import org.gradle.api.*

/**
 * @author bsaville
 */
public class MWSProjectTestingPlugin implements Plugin<Project> {
	void apply(Project project) {
		project.plugins.apply(MWSProjectTestingBasePlugin)

		project.test {
			testLogging {
				// Show standard output and error in test logging
				showStandardStreams = true
				// Filter groovy output
				stackTraceFilters "groovy"
			}
		}
	}
}