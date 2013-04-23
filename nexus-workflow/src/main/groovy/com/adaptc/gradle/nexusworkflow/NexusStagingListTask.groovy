package com.adaptc.gradle.nexusworkflow

import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

/**
 *
 * @author bsaville
 */
class NexusStagingListTask extends DefaultTask {
	def url
	def username
	def password

	@TaskAction
	def list() {
		if (!url || !username || !password)
			throw new InvalidUserDataException("The oss-releases.url, oss-releases.username, and oss-releases.password "+
					"properties must be set on the project before this task is run")
		def listStagingURL = url + "service/local/staging/profile_repositories"
		def authString = "${username}:${password}".getBytes().encodeBase64().toString()
		def conn = listStagingURL.toURL().openConnection()
		conn.setRequestProperty("Authorization", "Basic ${authString}")
		conn.setRequestProperty("Accept", "application/json")

		if(conn.responseCode == 200){
			def profiles = new JsonSlurper().parseText( conn.content.text )
			profiles.data.each {
				println it.repositoryId + ", " + it.type
			}
		} else {
			logger.error "Something bad happened."
			logger.error "${conn.responseCode}: ${conn.responseMessage}"
		}
	}
}
