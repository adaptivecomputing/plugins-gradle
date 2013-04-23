package com.adaptc.gradle.nexusworkflow

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction;

/**
 * @author bsaville
 */
public class NexusStagingPromoteTask extends DefaultTask {
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
		URL urlObj = url.toURL()
		def baseUrl = urlObj.getProtocol()+"://"+urlObj.getAuthority()+"/"
		def closeStagingUrl = baseUrl +	"service/local/staging/bulk/promote"
		logger.info("Promoting staging repository ${repoId} with ${closeStagingUrl} using ${username}")
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
			logger.lifecycle "Repository ${repoId} promoted"
		} else {
			try { logger.info("Content: "+conn.content) } catch(Exception e) {}
			throw new Exception("There was an error while promoting the repository '${repoId}': ${conn.responseCode} ${conn.responseMessage}")
		}
	}
}
