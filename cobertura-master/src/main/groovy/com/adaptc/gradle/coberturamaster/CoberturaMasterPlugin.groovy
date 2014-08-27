package com.adaptc.gradle.coberturamaster

import org.gradle.api.*
import org.gradle.api.plugins.cobertura.CoberturaPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.cobertura.CoberturaPluginExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginConvention

/**
 * @author bsaville
 */
public class CoberturaMasterPlugin implements Plugin<Project> {
	void apply(Project project) {
		// Create main source set if it does not already exist
		if (!project.plugins.hasPlugin(JavaPlugin)) {
			project.plugins.apply(JavaBasePlugin)
			project.getConvention().getPlugin(JavaPluginConvention).sourceSets.create(SourceSet.MAIN_SOURCE_SET_NAME)
		}

		// Apply plugins
		project.plugins.apply(CoberturaPlugin)
		project.subprojects.each { it.plugins.apply(CoberturaPlugin) }

		// Add extensions
		CoberturaMasterExtension masterExtension = project.cobertura.extensions.create(CoberturaMasterExtension.NAME, CoberturaMasterExtension)

		// Wire up the convention if the java plugin is present on any of the sub-projects
		if (project.subprojects.any { it.plugins.hasPlugin(JavaPlugin) || it.plugins.hasPlugin(GroovyPlugin) }) {
			def coberturaExtension = project.extensions.getByType(CoberturaPluginExtension)
			masterExtension.addMasterCoverage(project, coberturaExtension)
		}
	}
}

