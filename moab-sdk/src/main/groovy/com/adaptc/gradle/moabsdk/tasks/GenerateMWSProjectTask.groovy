package com.adaptc.gradle.moabsdk.tasks

import com.adaptc.gradle.moabsdk.utils.MoabSdkUtils
import org.apache.commons.lang.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import static com.adaptc.gradle.moabsdk.utils.MoabSdkConstants.*

/**
 * @author bsaville
 */
public class GenerateMWSProjectTask extends DefaultTask {
	private static final String ARGS_PROPERTY = "args"

	String className
	String projectName

	@Input File getProjectDirectory() {
		if (!project.hasProperty(ARGS_PROPERTY) || !project."${ARGS_PROPERTY}")
			throw new InvalidUserDataException("Please include the name of the MWS project to be created "+
					"(i.e. ${name} -P${ARGS_PROPERTY}=my-project)")
		projectName = project."${ARGS_PROPERTY}".toString()
		className = StringUtils.capitalize(projectName).
				replaceAll(/-([a-z])/, { wholeMatch, letter -> return letter.toUpperCase() }).
				replaceAll(/-/, '')
		return new File(projectName)
	}

	@TaskAction void generateProject() {
		def dir = getProjectDirectory()
		if (dir.exists())
			throw new InvalidUserDataException("The file ${dir.absolutePath} already exists, please try another name")

		// Make directories
		project.mkdir(new File(dir, "src/main/groovy"))
		project.mkdir(new File(dir, "src/test/groovy"))
		def testResourcesDir = new File(dir, "src/test/resources")
		project.mkdir(testResourcesDir)
		def libDir = new File(dir, "lib")
		project.mkdir(libDir)
		def i18nDir = new File(dir, "src/main/resources/i18n")
		project.mkdir(i18nDir)

		// Copy templates from resources
		File templatesDir = File.createTempFile("moab-sdk-templates-", "")
		project.ant.delete(file:templatesDir)
		project.ant.mkdir(dir:templatesDir)
		MWS_TEMPLATE_FILES.each {
			def file = new File(templatesDir, it)
			file << this.class.getResourceAsStream(MWS_TEMPLATES_DIRECTORY+it)
		}
		project.copy {
			from templatesDir
			include "build.gradle"
			include "test-instances.groovy"
			include "gradle.properties"
			into dir
			expand(standaloneProject:MoabSdkUtils.getProperty(project, STANDALONE_PROJECT_PROPERTY, "true").toBoolean())
		}
		project.copy {
			from templatesDir
			include "messages.properties"
			into i18nDir
		}
		project.copy {
			from templatesDir
			include "log4j.properties"
			into testResourcesDir
		}
		project.copy {
			from templatesDir
			include "libdir-readme.txt"
			rename { "readme.txt" }
			into libDir
		}
		project.copy {
			from templatesDir
			include "Project.groovy"
			rename { className+"Project.groovy" }
			into dir
			expand(
					className:className,
					naturalName:className.replaceAll(/([a-z])([A-Z])/, "\$1 \$2"),
					projectName:projectName,
					commonsVersion:MoabSdkUtils.getProperty(project, 'commons.version') ?: '',
					mwsVersion:MoabSdkUtils.getProperty(project, 'mws.version') ?: '',
					license:MoabSdkUtils.getProperty(project, 'projects.license') ?: '',
					author:MoabSdkUtils.getProperty(project, 'projects.author.name') ?: '',
					website:MoabSdkUtils.getProperty(project, 'projects.author.website') ?: '',
					email:MoabSdkUtils.getProperty(project, 'projects.author.email') ?: ''
			)
		}

		logger.lifecycle "Created project ${projectName} at ${dir.absolutePath}"
		project.ant.delete(dir:templatesDir)
	}
}
