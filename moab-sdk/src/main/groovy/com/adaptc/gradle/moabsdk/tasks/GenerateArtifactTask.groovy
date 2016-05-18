package com.adaptc.gradle.moabsdk.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import static com.adaptc.gradle.moabsdk.utils.MoabSdkConstants.MWS_TEMPLATES_DIRECTORY

/**
 * @author bsaville
 */
public class GenerateArtifactTask extends DefaultTask {
	private static final String ARGS_PROPERTY = "args"
	private static final String SOURCE_DIRECTORY = 'src/main/groovy/'

	@Input String artifactName
	String packageName
	String className

	@Input File getArtifactFile() {
		if (!project.hasProperty(ARGS_PROPERTY))
			throw new InvalidUserDataException("Please include the name of the ${artifactName} to be created "+
					"(i.e. ${name} -P${ARGS_PROPERTY}=example.Example${artifactName})")
		def inputName = project."${ARGS_PROPERTY}"
		// Do not add artifact name ending for components
		if (!inputName.endsWith(artifactName) && artifactName!="Component")
			inputName += artifactName
		def filename = inputName.replaceAll('\\.', "/")
		filename += ".groovy"
		def dotPlace = inputName.lastIndexOf('.')
		packageName = ''
		className = ''
		if (dotPlace==-1) {
			className = inputName
		} else {
			packageName = inputName.substring(0, dotPlace)
			className = inputName.substring(dotPlace+1)
		}
		return new File(SOURCE_DIRECTORY+filename)
	}

	@TaskAction void generateArtifact() {
		def artifactFile = getArtifactFile()
		if (artifactFile.exists())
			throw InvalidUserDataException("The file ${artifactFile} already exists, please specify another file.")
		File templatesDir = File.createTempFile("moab-sdk-templates-", "")
		project.ant.delete(file:templatesDir)
		project.ant.mkdir(dir:templatesDir)
		def file = new File(templatesDir, artifactName+".groovy")
		file << this.class.getResourceAsStream(MWS_TEMPLATES_DIRECTORY+artifactName+".groovy")
		project.copy {
			from templatesDir
			include artifactName+".groovy"
			into artifactFile.getParentFile().path
			rename { artifactFile.name }
			expand(packageName: packageName, className:className)
		}
		logger.lifecycle "Created class for ${className} at ${artifactFile}"
		project.ant.delete(dir:templatesDir)
	}
}
