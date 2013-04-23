package com.adaptc.gradle.nexusworkflow

import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction;

/**
 * @author bsaville
 */
public class NexusStagingReleaseTask extends DefaultTask {
	def url
	def username
	def password

	@TaskAction
	def close() {
		if (!url || !username || !password)
			throw new InvalidUserDataException("The oss-releases.url, oss-releases.username, and oss-releases.password "+
					"properties must be set on the project before this task is run")
		def authString = "${username}:${password}".getBytes().encodeBase64().toString()
		URL urlObj = url.toURL()
		def baseUrl = urlObj.getProtocol()+"://"+urlObj.getAuthority()+"/"
		def listStagingUrl = baseUrl + "service/local/staging/profile_repositories"
		def closeStagingUrl = baseUrl +	"service/local/staging/bulk/close"
		def promoteStagingUrl = baseUrl +	"service/local/staging/bulk/promote"
		logger.lifecycle("Determining all open repositories")
		logger.debug("Using username ${username} and URL ${baseUrl}")

		// First list all repositories and retrieve all open ones
		def repositoryList = getAllRepositories(listStagingUrl, authString)
		def repositoryIds = repositoryList.findAll{
			it.type=="open"
		}.collect { it.repositoryId }

		if (repositoryIds) {
			logger.lifecycle("Closing ${repositoryIds.size()} open repositories")
			logger.debug("Repository IDs: ${repositoryIds}")

			def conn = closeStagingUrl.toURL().openConnection()
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
				logger.lifecycle "${repositoryIds.size()} repositories closed"
			} else {
				try { logger.info("Content: "+conn.content) } catch(Exception e) {}
				throw new Exception("There was an error while closing the repositories: ${conn.responseCode} ${conn.responseMessage}")
			}
		} else {
			logger.lifecycle "No open repositories to close"
		}

		// List all repositories again and retrieve all closed ones
		repositoryList = getAllRepositories(listStagingUrl, authString)
		repositoryIds = repositoryList.findAll{
			it.type=="closed"
		}.collect { it.repositoryId }

		if (repositoryIds) {
			logger.lifecycle("Promoting (releasing) ${repositoryIds.size()} closed repositories")
			logger.debug("Repository IDs: ${repositoryIds}")

			def conn = promoteStagingUrl.toURL().openConnection()
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
				logger.lifecycle "${repositoryIds.size()} repositories promoted"
			} else {
				try { logger.info("Content: "+conn.content) } catch(Exception e) {}
				throw new Exception("There was an error while promoting the repositories: ${conn.responseCode} ${conn.responseMessage}")
			}
		} else {
			logger.lifecycle "No closed repositories to promote (release)"
		}
	}

	private List<Map> getAllRepositories(String url, String authString) {
		def conn = url.toURL().openConnection()
		conn.setRequestProperty("Authorization", "Basic ${authString}")
		conn.setRequestProperty("Accept", "application/json")

		if(conn.responseCode != 200){
			try { logger.info("Content: "+conn.content) } catch(Exception e) {}
			throw new Exception("There was an error while retrieving the repositories from Nexus: ${conn.responseCode} ${conn.responseMessage}")
		}
		def profiles = new JsonSlurper().parseText( conn.content.text )
		return profiles.data
	}
}
