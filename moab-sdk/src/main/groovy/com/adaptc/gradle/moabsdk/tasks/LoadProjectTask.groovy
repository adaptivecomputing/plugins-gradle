package com.adaptc.gradle.moabsdk.tasks

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.apache.commons.lang.StringUtils

/**
 * This task loads the project file for version information that can be used later.
 * The information is placed on the project.pluginProject extension.
 * @see com.adaptc.gradle.moabsdk.extensions.PluginProjectExtension
 * @author bsaville
 */
class LoadProjectTask extends DefaultTask {
	@InputFile File getProjectFile() {
		def pjFile
		project.projectDir.eachFile {
			if (it.name.endsWith("Project.groovy")) pjFile = it
		}
		if (!pjFile)
			throw new InvalidUserDataException("Could not build project ${project.name}, the project file is missing!  Please add a file in the project directory ending in Project.groovy")
		return pjFile
	}

	@TaskAction public void load() {
		def projectFile = getProjectFile()
		project.moabSdk.pluginProject.projectFile = projectFile

		Class<?> clazz
		ClassLoader loader = new URLClassLoader(project.configurations.runtime.collect {
			it.toURI().toURL() } as URL[])
		try {
			clazz = new GroovyClassLoader(loader).parseClass(new GroovyCodeSource(projectFile), false)
		} catch(org.codehaus.groovy.control.MultipleCompilationErrorsException e) {
			throw new InvalidUserDataException("Could not build project '${project.name}', the project file (${projectFile}) is invalid: ${e.message}")
		}
		try {
			def projectClassInstance = clazz.newInstance()
			project.moabSdk.pluginProject.projectClassInstance = projectClassInstance
			project.moabSdk.pluginProject.title = getPluginProperty(projectClassInstance, "title") ?: project.name
			project.moabSdk.pluginProject.description = getPluginProperty(projectClassInstance, "description")
			project.moabSdk.pluginProject.author = getPluginProperty(projectClassInstance, "author")
			project.version = projectClassInstance.getVersion()
		} catch(Exception e) {
			throw new InvalidUserDataException("Could not build project '${project.name}', the project file ${projectFile.name} does not contain a valid version property.")
		}
	}

	String getPluginProperty(projectClassInstance, String propertyName) {
		try {
			return projectClassInstance."get${StringUtils.capitalize(propertyName)}"()?.toString()
		} catch(Exception e) {}
		return null
	}
}
