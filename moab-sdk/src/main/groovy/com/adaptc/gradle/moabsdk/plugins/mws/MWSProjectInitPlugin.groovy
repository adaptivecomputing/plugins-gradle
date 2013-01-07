package com.adaptc.gradle.moabsdk.plugins.mws

import org.gradle.api.*

/**
 * @author bsaville
 */
public class MWSProjectInitPlugin implements Plugin<Project> {
	void apply(Project project) {
		project.repositories.mavenCentral()
		project.repositories.mavenRepo(url:"https://oss.sonatype.org/content/groups/public")

		project.plugins.apply MWSProjectPlugin
		project.plugins.apply com.adaptc.gradle.fatjar.FatJarPlugin
		project.plugins.apply com.adaptc.gradle.libdir.LibDirDependenciesPlugin

		configureFatJarExclusions(project)
	}

	/**
	 * These are the defaults for MWS plugin projects
	 * @param project
	 */
	void configureFatJarExclusions(Project project) {
		project.fatJar.excludedDependencies = [
				"spring-",
				"groovy-all",
				"plugins-commons",
				"servlet-api",
				"antlr",
				"asm-3",
				"asm-tree",
				"asm-commons",
				"asm-util",
				"asm-analysis",
				"ezmorph",
				"commons-logging",
				"commons-beanutils",
				"commons-collections",
				"commons-lang",
				"json-lib",
				"aopalliance",
				"not-yet-commons-ssl",
				"xml-apis",
				"http-builder",
		]
	}
}

