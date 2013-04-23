package com.adaptc.gradle.nexusworkflow

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction;

/**
 * @author bsaville
 */
public class NexusStagingCloseTask extends DefaultTask {
	def url
	def username
	def password

	@TaskAction
	def close() {
		if (!url || !username || !password)
			throw new InvalidUserDataException("The oss-releases.url, oss-releases.username, and oss-releases.password "+
					"properties must be set on the project before this task is run")
		if (!project.hasProperty("repoId")) {
			throw new InvalidUserDataException("Please set the property 'repoId' in order to close a Nexus staging repository")
		}
		def repoId = project.getProperties().repoId
		logger.info("Closing repository '${repoId}'")
		def closeStagingUrl = url +	"service/local/staging/bulk/close"
		def authString = "${username}:${password}".getBytes().encodeBase64().toString()

		def conn = closeStagingUrl.toURL().openConnection()
		conn.setRequestMethod("POST")
		conn.setRequestProperty("Authorization", "Basic ${authString}")
		conn.setRequestProperty("Accept", "application/json")
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
		conn.setDoOutput( true )

		Writer wr = new OutputStreamWriter(conn.outputStream)
		wr.write("{'data':{'stagedRepositoryIds':['${repoId}'],'description':''}}");
		wr.flush()
		wr.close()

		conn.connect()

		if(conn.responseCode == 201){
			println "Repository Closed"
		} else {
			logger.error "Something bad happened."
			logger.error "${conn.responseCode}: ${conn.responseMessage}"
		}
	}
}
