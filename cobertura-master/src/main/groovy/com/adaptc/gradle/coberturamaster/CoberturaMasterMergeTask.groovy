package com.adaptc.gradle.coberturamaster

import org.gradle.api.*
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.*
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.project.IsolatedAntBuilder

/**
 * @author bsaville
 */

class CoberturaMasterMergeTask extends ConventionTask {
	@OutputFile File outputSerFile
	@InputFiles FileCollection coberturaClasspath
	@Input List<String> excludedProjects = []
	@InputFiles FileCollection getInputSerFiles() {
		def subProjects = project.subprojects.findAll { !excludedProjects.contains(it.name) }
		return project.files(subProjects.inject([]) { List list, Project subProject ->
			def testSerFile = new File(subProject.sourceSets.main.cobertura.serFile.absolutePath[0..-5]+"-test.ser")
			if (testSerFile.exists())
				list << testSerFile
			return list
		})
	}

	@TaskAction def run() {
		def inputSerFiles = getInputSerFiles()
		if (!inputSerFiles.empty) {
			def ant = getServices().get(IsolatedAntBuilder).withClasspath(getCoberturaClasspath())
			ant.execute {
				taskdef(name: 'cobertura-merge', classname: "net.sourceforge.cobertura.ant.MergeTask")
				'cobertura-merge'(datafile: getOutputSerFile()) {
					inputSerFiles.addToAntBuilder(delegate, "fileset", FileCollection.AntType.FileSet)
				}
			}
		} else {
			logger.warn 'Cobertura cannot run because no input SER files were found.'
		}
	}
}
