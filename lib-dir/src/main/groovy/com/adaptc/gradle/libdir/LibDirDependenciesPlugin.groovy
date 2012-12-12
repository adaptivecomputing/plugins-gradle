package com.adaptc.gradle.libdir

import org.gradle.api.*

/**
 * @author bsaville
 */
public class LibDirDependenciesPlugin implements Plugin<Project> {
	void apply(Project project) {
		project.extensions.create("libDir", LibDirExtension)

		project.repositories {
			def libDir = project.file(project.libDir.name)
			if (libDir.exists() && libDir.isDirectory())
				flatDir name:'localLib', dirs:'lib'
		}
		// This is run multiple times but the deps are only loaded a single time
		project.dependencies {
			def libDir = project.file(project.libDir.name)
			if (libDir.exists() && libDir.isDirectory()) {
				libDir.eachFile {
					if (!it.name.endsWith(".jar"))
						return
					def version = it.name.replaceAll(/^.*-([\d.]+)\.jar$/, "\$1")
					def name = it.name - ".jar"
					if (version==it.name)
						version = project.libDir.defaultVersion
					else
						name = name - ("-"+version)
					compile "${name}:${name}:${version}"
				}
			}
		}
	}
}

class LibDirExtension {
	String name = "lib"
	String defaultVersion = '0.0.1'	// Some random version
}