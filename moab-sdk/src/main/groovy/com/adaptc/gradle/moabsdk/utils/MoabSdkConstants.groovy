package com.adaptc.gradle.moabsdk.utils;

/**
 * @author bsaville
 */
public class MoabSdkConstants {
	public static final String DEFAULT_SDK_VERSION = "7.1.3"
	public static final String MOAB_SDK_TASK_GROUP = "Moab SDK"

	public static final String TEMPLATES_DIRECTORY = "/templates"
	public static final String MWS_TEMPLATES_DIRECTORY = TEMPLATES_DIRECTORY+"/mws/"
	public static final String MWS_TEST_LOG_CONFIG_FILE = "log4j.properties"
	public static final List<String> MWS_TEMPLATE_FILES = ["build.gradle", "test-instances.groovy", "gradle.properties",
			"messages.properties", "libdir-readme.txt", "Project.groovy", "log4j.properties"]

	public static final String STANDALONE_PROJECT_PROPERTY = "sdk.standalone"
}
