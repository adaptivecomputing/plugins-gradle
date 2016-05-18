package com.adaptc.gradle.moabsdk.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import static com.adaptc.gradle.moabsdk.utils.MoabSdkConstants.MWS_TEMPLATES_DIRECTORY

/**
 * @author bsaville
 */
public class GenerateArtifactTestTask extends DefaultTask {
	private static final String ARGS_PROPERTY = "args"
	private static final String TEST_DIRECTORY = "src/test/groovy/"

	@Input String artifactName
	@Input boolean spock = true
	String getTestArtifactName() {
		return spock ? "Spec" : "Tests"
	}
	String packageName
	String className
	String testClassName

	@Input File getArtifactFile() {
		def testArtifactName = getTestArtifactName()
		if (!project.hasProperty(ARGS_PROPERTY))
			throw new InvalidUserDataException("Please include the name of the artifact for the tests to be created "+
					"(i.e. ${name} -P${ARGS_PROPERTY}=example.Example${artifactName})")
		def inputName = project."${ARGS_PROPERTY}"
		if (inputName.endsWith(testArtifactName))
			inputName = inputName.substring(0, inputName.size() - testArtifactName.size())
		// Do not add artifact name ending for components
		if (!inputName.endsWith(artifactName) && artifactName!="Component")
			inputName += artifactName
		def filename = inputName.replaceAll('\\.', "/")
		if (!filename.endsWith(testArtifactName))
			filename += testArtifactName
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
		testClassName = className+testArtifactName
		return new File(TEST_DIRECTORY+filename)
	}

	@TaskAction void generateArtifactTest() {
		def testArtifactName = getTestArtifactName()
		def artifactFile = getArtifactFile()
		if (artifactFile.exists())
			throw InvalidUserDataException("The file ${artifactFile} already exists, please specify another file.")
		File templatesDir = File.createTempFile("moab-sdk-templates-", "")
		project.ant.delete(file:templatesDir)
		project.ant.mkdir(dir:templatesDir)
		def file = new File(templatesDir, artifactName+testArtifactName+".groovy")
		file << this.class.getResourceAsStream(MWS_TEMPLATES_DIRECTORY+artifactName+testArtifactName+".groovy")
		project.copy {
			from templatesDir
			include artifactName+testArtifactName+".groovy"
			into artifactFile.getParentFile().path
			rename { artifactFile.name }
			expand(packageName: packageName, className:className, testClassName:testClassName)
		}
		logger.lifecycle "Created tests for ${className} at ${artifactFile}"
		project.ant.delete(dir:templatesDir)
	}
}
