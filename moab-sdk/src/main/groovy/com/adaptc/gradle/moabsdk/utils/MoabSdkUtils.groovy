package com.adaptc.gradle.moabsdk.utils;

import org.gradle.api.*

/**
 * @author bsaville
 */
public class MoabSdkUtils {
	public static def getProperty(Project project, String propertyName, defaultValue=null) {
		if (project.hasProperty(propertyName))
			return project."${propertyName}" ?: defaultValue
		return defaultValue
	}
}
