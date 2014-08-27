package com.adaptc.gradle.coberturamaster

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.*

import org.gradle.api.plugins.GroovyPlugin

/**
 * @author bsaville
 */

class CoberturaMasterReportTask extends ConventionTask {
	@Input String format
	@OutputDirectory File reportDir
	@InputFile File serFile
	@InputFiles FileCollection coberturaClasspath
	@Input List<String> excludedProjects = []
	@InputFiles FileCollection getSourceDirectories() {
		def subProjects = project.subprojects.findAll { !excludedProjects.contains(it.name) }
		return project.files(subProjects.inject([]) { List list, Project subProject ->
			if (subProject.plugins.hasPlugin(JavaPlugin)) {
				subProject.getConvention().getPlugin(JavaPluginConvention).sourceSets.
							getByName(SourceSet.MAIN_SOURCE_SET_NAME).java.srcDirs.each {
					if (it.exists()) list << it
				}
			}
			if (subProject.plugins.hasPlugin(GroovyPlugin)) {
				subProject.getConvention().getPlugin(JavaPluginConvention).sourceSets.
						getByName(SourceSet.MAIN_SOURCE_SET_NAME).groovy.srcDirs.each {
					if (it.exists()) list << it
				}
			}
			return list
		})
	}

	@TaskAction
	def run() {
		def sourceDirectories = getSourceDirectories()
		if (!sourceDirectories.empty) {
			def ant = getServices().get(IsolatedAntBuilder).withClasspath(getCoberturaClasspath())
			ant.execute {
				taskdef(name: 'cobertura-report', classname: "net.sourceforge.cobertura.ant.ReportTask")
				'cobertura-report'(format: getFormat(), destdir: getReportDir(), datafile: getSerFile()) {
					sourceDirectories.addToAntBuilder(delegate, "fileset", FileCollection.AntType.FileSet)
				}
			}
		} else {
			logger.warn 'Cobertura cannot run because no source directories were found.'
		}
	}
}
