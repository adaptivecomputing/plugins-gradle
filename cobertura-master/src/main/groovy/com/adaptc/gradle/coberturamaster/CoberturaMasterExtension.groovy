package com.adaptc.gradle.coberturamaster

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.plugins.cobertura.CoberturaPluginExtension

/**
 * @author bsaville
 */
class CoberturaMasterExtension {
	public static final NAME = "master"

	List<String> excludedProjects = []

	public void addMasterCoverage(Project project, CoberturaPluginExtension coberturaExtension) {
		def serFile = project.getConvention().getPlugin(JavaPluginConvention).sourceSets.
				getByName(SourceSet.MAIN_SOURCE_SET_NAME).cobertura.serFile

		def masterMerge = addMasterMergeTask(project, coberturaExtension, serFile)
		project.subprojects.each {
			masterMerge.dependsOn(it.tasks.build)
		}
		def masterReport = addMasterReportTask(project, coberturaExtension, serFile)
		masterReport.dependsOn(masterMerge)
		project.tasks.getByName("check").dependsOn(masterReport)
	}

	public CoberturaMasterMergeTask addMasterMergeTask(Project project, CoberturaPluginExtension coberturaExtension,
				File outputSerFile) {
		def masterMerge = project.tasks.create("coberturaMasterMergeMain", CoberturaMasterMergeTask)
		masterMerge.setGroup("Verification")
		masterMerge.setDescription("Merges cobertura output from sub-projects for the 'main' source set")
		masterMerge.coberturaClasspath = coberturaExtension.classpath
		masterMerge.outputSerFile = outputSerFile
		masterMerge.conventionMapping.map("excludedProjects") {
			getExcludedProjects()
		}
		return masterMerge
	}

	public CoberturaMasterReportTask addMasterReportTask(Project project, CoberturaPluginExtension coberturaExtension,
				File inputSerFile) {
		def masterReport = project.tasks.create("coberturaMasterReportMain", CoberturaMasterReportTask)
		masterReport.setGroup("Verification")
		masterReport.setDescription("Generates merged cobertura report from sub-projects for the 'main' source set")
		masterReport.reportDir = project.extensions.getByType(ReportingExtension).file("cobertura")
		masterReport.setFormat(project.hasProperty("cobertura.format") ? project.getProperties()["cobertura.format"] : "html")
		masterReport.coberturaClasspath = coberturaExtension.classpath
		masterReport.serFile = inputSerFile
		masterReport.conventionMapping.map("excludedProjects") {
			getExcludedProjects()
		}
		return masterReport
	}
}
