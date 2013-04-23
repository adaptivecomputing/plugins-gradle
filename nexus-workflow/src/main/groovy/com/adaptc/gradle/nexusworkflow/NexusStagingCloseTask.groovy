package com.adaptc.gradle.nexusworkflow

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction;

/**
 * @author bsaville
 */
public class NexusStagingCloseTask extends DefaultTask {
	@Input def url
	@Input def username
	@Input def password

	@TaskAction
	def close() {
		if (!project.hasProperty("repoId")) {
			logger.error("Please set the property 'repoId' in order to close a Nexus staging repository")
			return
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
