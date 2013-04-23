package com.adaptc.gradle.moabsdk.plugins.mws

import org.gradle.api.*

import org.gradle.api.tasks.bundling.Jar
import com.adaptc.gradle.moabsdk.extensions.*
import com.adaptc.gradle.moabsdk.tasks.*
import com.adaptc.gradle.moabsdk.utils.MoabSdkUtils
import static com.adaptc.gradle.moabsdk.utils.MoabSdkConstants.*
import org.gradle.api.artifacts.Dependency

/**
 * @author bsaville
 */
public class MWSProjectBasePlugin implements Plugin<Project> {
	void apply(Project project) {
		project.plugins.apply 'java'
		project.plugins.apply 'groovy'

		project.extensions.create("moabSdk", SdkExtension)
		project.moabSdk.extensions.create("pluginProject", PluginProjectExtension)

		project.dependencies.add("groovy", project.dependencies.localGroovy())
		Dependency commonsDependency = project.dependencies.create("com.adaptc.mws:plugins-commons:" +
				MoabSdkUtils.getProperty(project, 'commons.version', project.rootProject.getProperty('commons.version'))) {
			exclude module:"groovy"
			exclude module:"groovy-all"
		}
		project.dependencies.add("compile", commonsDependency)

		addTasks(project)

		// TODO Have this updated on changes to readme without a clean
		project.tasks.jar.doFirst {
			from 'README.md'
		}
	}

	void addTasks(Project project) {
		// Sources and javadoc JAR tasks
		def sourcesJar = project.tasks.add("sourcesJar", Jar)
		sourcesJar.classifier = 'sources'
		sourcesJar.from project.sourceSets.main.allSource
		def javadoc = project.tasks['javadoc']
		def javadocJar = project.tasks.add("javadocJar", Jar)
		javadocJar.dependsOn javadoc
		javadocJar.classifier = "javadoc"
		javadocJar.from javadoc.destinationDir

		// Add lifecycle tasks
		def loadPluginProject = project.tasks.add("loadPluginProject", LoadProjectTask)
		project.tasks['compileJava'].dependsOn loadPluginProject

		// Test Template Generation Tasks
		def createPluginTest = project.tasks.add(name:"createPluginTest", type:GenerateArtifactTestTask) {
			artifactName = "Plugin"
		}
		createPluginTest.setGroup(MOAB_SDK_TASK_GROUP)
		createPluginTest.setDescription("Generates a test for the given plugin package and class name")
		def createTranslatorTest = project.tasks.add(name:"createTranslatorTest", type:GenerateArtifactTestTask) {
			artifactName = "Translator"
		}
		createTranslatorTest.setGroup(MOAB_SDK_TASK_GROUP)
		createTranslatorTest.setDescription("Generates a test for the given translator package and class name")
		def createComponentTest = project.tasks.add(name:"createComponentTest", type:GenerateArtifactTestTask) {
			artifactName = "Component"
		}
		createComponentTest.setGroup(MOAB_SDK_TASK_GROUP)
		createComponentTest.setDescription("Generates a test for the given helper component package and class name")

		// Artifact Template Generation Tasks
		def createPlugin = project.tasks.add(name:"createPlugin", type:GenerateArtifactTask) {
			artifactName = "Plugin"
		}
		createPlugin.dependsOn createPluginTest
		createPlugin.setGroup(MOAB_SDK_TASK_GROUP)
		createPlugin.setDescription("Generates a plugin file with the given package and class name")
		def createTranslator = project.tasks.add(name:"createTranslator", type:GenerateArtifactTask) {
			artifactName = "Translator"
		}
		createTranslator.dependsOn createTranslatorTest
		createTranslator.setGroup(MOAB_SDK_TASK_GROUP)
		createTranslator.setDescription("Generates a translator file with the given package and class name")
		def createComponent = project.tasks.add(name:"createComponent", type:GenerateArtifactTask) {
			artifactName = "Component"
		}
		createComponent.dependsOn createComponentTest
		createComponent.setGroup(MOAB_SDK_TASK_GROUP)
		createComponent.setDescription("Generates a helper component file with the given package and class name")

		// Add custom tasks
		def uploadPluginProject = project.tasks.add("upload", UploadProjectTask)
		uploadPluginProject.setDescription("Uploads a plugin project to MWS as a plugin JAR file")
		uploadPluginProject.setGroup(MOAB_SDK_TASK_GROUP)
		uploadPluginProject.dependsOn(project.tasks.jar)
		uploadPluginProject.dependsOn(javadocJar)
		uploadPluginProject.dependsOn(sourcesJar)
		def genTestInstances = project.tasks.add("generateTestInstances", GenerateTestInstancesTask)
		genTestInstances.setDescription("Generates test instances from instances.groovy and creates them in MWS")
		genTestInstances.setGroup(MOAB_SDK_TASK_GROUP)
	}
}

