package com.adaptc.gradle.nexusworkflow

import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction;

/**
 * @author bsaville
 */
public class NexusStagingAutoCloseTask extends DefaultTask {
	@Input def url
	@Input def username
	@Input def password

	@TaskAction
	def close() {
		def authString = "${username}:${password}".getBytes().encodeBase64().toString()
		def listStagingURL = url + "service/local/staging/profile_repositories"
		def closeStagingUrl = url +	"service/local/staging/bulk/close"

		// First list all repositories
		def conn = listStagingURL.toURL().openConnection()
		conn.setRequestProperty("Authorization", "Basic ${authString}")
		conn.setRequestProperty("Accept", "application/json")

		if(conn.responseCode != 200){
			logger.error "Something bad happened."
			logger.error "${conn.responseCode}: ${conn.responseMessage}"
			return
		}
		def profiles = new JsonSlurper().parseText( conn.content.text )
		def repositoryIds = profiles.data.findAll{
			it.type=="open"
		}
		logger.info("Closing ${repositoryIds.size()} repositories: ${repositoryIds}")

		conn = closeStagingUrl.toURL().openConnection()
		conn.setRequestMethod("POST")
		conn.setRequestProperty("Authorization", "Basic ${authString}")
		conn.setRequestProperty("Accept", "application/json")
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
		conn.setDoOutput( true )

		Writer wr = new OutputStreamWriter(conn.outputStream)
		wr.write("{'data':{'stagedRepositoryIds':[${repositoryIds.collect { "'"+it+"'" }.join(",")}],'description':''}}");
		wr.flush()
		wr.close()

		conn.connect()

		if(conn.responseCode == 201){
			println "Repositories closed"
		} else {
			logger.error "Something bad happened."
			logger.error "${conn.responseCode}: ${conn.responseMessage}"
		}
	}
}
