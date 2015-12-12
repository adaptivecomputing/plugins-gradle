package com.adaptc.gradle.moabsdk.tasks

import org.gradle.api.*
import org.gradle.api.tasks.*
import com.adaptc.gradle.moabsdk.utils.MoabSdkUtils

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.JSON

class GenerateTestInstancesTask extends DefaultTask {
	@InputFile File getInstancesFile() {
		return project.file("test-instances.groovy")
	}

	@TaskAction void generateTestInstances() {
		def instancesFile = getInstancesFile()
		def config = new ConfigSlurper().parse(instancesFile.toURI().toURL())
		if (!config.plugins || !(config.plugins instanceof ConfigObject) || config.plugins.size()==0)
			throw new InvalidUserDataException("Could not generate test plugin instances in MWS, please define the 'plugins' property in 'test-instances.groovy'")

		// Get information from config
		config.plugins.each { String pluginId, options ->
			def pluginType = options.pluginType
			def priority = options.priority
			def pollInterval = options.pollInterval
			def pluginConfig = options.config ?: [:]
			if (!pluginId || !pluginType)
				throw new InvalidUserDataException("Could not generate test plugin instance in MWS, insufficient information provided.  Please provide a valid plugin ID and type.")
			def serializedConfig = pluginConfig.collect { "\"${it.key}\":\"${it.value}\"" }.join(",")
			logger.info "Using serialized configuration ${serializedConfig}"

			// Setup MWS paths and auth
			def mwsBaseUrl = MoabSdkUtils.getProperty(project, "mws.url", "http://localhost:8080/mws/").toString()
			if (!mwsBaseUrl.endsWith("/"))
				mwsBaseUrl += "/"
			def mwsPath = "rest/plugins"
			def username = MoabSdkUtils.getProperty(project, "mws.username", "admin").toString()
			def password = MoabSdkUtils.getProperty(project, "mws.password", "adminpw").toString()
			def mwsUri = (mwsBaseUrl+mwsPath).toURI()
			def baseUrl = mwsUri.scheme+"://"+mwsUri.authority
			def path = mwsUri.path
			def http = new HTTPBuilder(baseUrl)
			http.auth.basic(username, password)

			logger.lifecycle "Generating test instance for plugin ${pluginId} (${pluginType})"+(pluginConfig?' with plugin configuration '+pluginConfig:'')+" at ${baseUrl}${path}"
			try {
				http.request(POST, JSON) {
					uri.path = path
					uri.query = ['api-version':'latest']
					body = """{"id":"${pluginId}","pluginType":"${pluginType}","config":{${serializedConfig}}"""+
							(pollInterval?""","pollInterval":${pollInterval}""":"")+
							(priority?""","priority":${priority}""":"")+
							"}"
				}
			} catch(SocketException e) {
				logger.error("Could not connect to host at ${baseUrl}, check your mws.url and other settings in \$HOME/gradle.properties")
			} catch(HttpResponseException e) {
				logger.error("Error in generating test instance at ${baseUrl}${path}: ${e.message}/${e.response.entity.content}")
			}
		}
	}
}
