package com.adaptc.gradle.fatjar

import org.gradle.api.*
import org.gradle.api.artifacts.*

/**
 * @author bsaville
 */
public class FatJarPlugin implements Plugin<Project> {
	void apply(Project project) {
		project.extensions.create("fatJar", FatJarExtension)

		project.jar.doFirst {
			from project.fatJar.filterDependencies(project.configurations.runtime)
		}
		project.jar.doLast {
			def files = project.fatJar.filterDependencies(project.configurations.runtime)
			ant.jar(destfile:archivePath, update:true) {
				delegate.manifest {
					if (files)
						attribute(name: "Class-Path", value: files*.name.join(" "))
				}
			}
		}
	}
}

class FatJarExtension {
	List<String> excludedDependencies

	List<File> filterDependencies(Configuration configuration) {
		return configuration.inject([]) { List list, File jarFile ->
			if (!excludedDependencies.any { jarFile.name.startsWith(it) })
				list << (!jarFile.isDirectory() ? jarFile : zipTree(jarFile))
			return list
		}
	}
}