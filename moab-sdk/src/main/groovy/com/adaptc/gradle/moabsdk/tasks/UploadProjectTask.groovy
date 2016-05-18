package com.adaptc.gradle.moabsdk.tasks

import com.adaptc.gradle.moabsdk.utils.MoabSdkUtils
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import org.apache.http.entity.FileEntity
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.PUT

/**
 * @author bsaville
 */
public class UploadProjectTask extends DefaultTask {
	@TaskAction void upload() {
		def mwsBaseUrl = MoabSdkUtils.getProperty(project, "mws.url", "http://localhost:8080/mws/").toString()
		if (!mwsBaseUrl.endsWith("/"))
			mwsBaseUrl += "/"
		def mwsPath = "rest/plugin-types"
		def username = MoabSdkUtils.getProperty(project, "mws.username", "moab-admin").toString()
		def password = MoabSdkUtils.getProperty(project, "mws.password", "changeme!").toString()

		def mwsUri = (mwsBaseUrl+mwsPath).toURI()
		def baseUrl = mwsUri.scheme+"://"+mwsUri.authority
		def path = mwsUri.path
		logger.lifecycle "Uploading plugin project ${project.name} to ${baseUrl}${path} using credentials ${username}:${password?.getAt(0)}..."
		def http = new HTTPBuilder(baseUrl)
		http.auth.basic(username, password)
		try {
			def jarFile = project.tasks.jar.outputs.files.singleFile

			http.encoder.'application/x-jar' = { file ->
				return new FileEntity(file, "application/x-jar")
			}

			http.request(PUT, JSON) {
				uri.path = path
				uri.query = ['api-version':'latest',
							 'jar-filename': (MoabSdkUtils.getProperty(project, "projects.artifactId.prefix") ?: '') +
								(MoabSdkUtils.getProperty(project, "project.artifactId") ?: project.name)+".jar"]
				requestContentType = "application/x-jar"
				body = jarFile
			}
		} catch(SocketException e) {
			throw new InvalidUserDataException("Could not connect to host at ${baseUrl}, check your mws.url and other settings in \$HOME/gradle.properties")
		} catch(HttpResponseException e) {
			def entityContent = "No content"
			try {
				entityContent = e.response.entity.content.text
			} catch(IOException e2) {}
			throw new InvalidUserDataException("Error in uploading plugin to ${baseUrl}${path}: ${e.message}/${entityContent}")
		}
		logger.lifecycle "Successfully uploaded project"
	}
}
