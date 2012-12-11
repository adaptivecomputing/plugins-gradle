/*
 * Copyright 2004-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adaptc.mws.plugins.testing.support;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;

import org.springframework.core.io.Resource;

/**
 * Utility methods for resource handling / figuring out class names.
 *
 * @author Graeme Rocher
 * @since Grails 2.0
 */
public class PluginsResourceUtils {
	public static final String REGEX_FILE_SEPARATOR = "[\\\\/]"; // backslashes need escaping in regexes

	/*
	Resources are resolved against the platform specific path and must therefore obey the
	specific File.separator.
	 */
	public static final Pattern PLUGINS_RESOURCE_PATTERN_FIRST_MATCH;
	public static final Pattern PLUGINS_RESOURCE_PATTERN_SECOND_MATCH;

	public static Pattern PLUGIN_PATH_PATTERN = Pattern.compile(".+" + REGEX_FILE_SEPARATOR +
			"(.+Plugin)\\.(groovy|java)");
	public static Pattern TRANSLATOR_PATH_PATTERN = Pattern.compile(".+" + REGEX_FILE_SEPARATOR +
			"(.+Translator)\\.(groovy|java)");

	static {
		String fs = REGEX_FILE_SEPARATOR;

		PLUGINS_RESOURCE_PATTERN_FIRST_MATCH = Pattern.compile(createResourcePattern(fs, "src" + fs + "main" + fs + "java"));
		PLUGINS_RESOURCE_PATTERN_SECOND_MATCH = Pattern.compile(createResourcePattern(fs, "src" + fs + "main" + fs + "groovy"));
	}

	public static final Pattern[] patterns = new Pattern[]{
		PLUGINS_RESOURCE_PATTERN_FIRST_MATCH,
		PLUGINS_RESOURCE_PATTERN_SECOND_MATCH
	};
	private static String createResourcePattern(String separator, String base) {
		return ".+" + separator + base + separator + "(.+)\\.(groovy|java)$";
	}

	/**
	 * Gets the class name of the specified resource
	 *
	 * @param resource The Spring Resource
	 * @return The class name or null if the resource is not a
	 */
	public static String getClassName(Resource resource) {
		try {
			return getClassName(resource.getFile().getAbsolutePath());
		}
		catch (IOException e) {
			 return null;
		}
	}

	/**
	 * Returns the class name for a resource.
	 *
	 * @param path The path to check
	 * @return The class name or null if it doesn't exist
	 */
	public static String getClassName(String path) {
		for (Pattern pattern : patterns) {
			Matcher m = pattern.matcher(path);
			if (m.find()) {
				return m.group(1).replaceAll("[/\\\\]", ".");
			}
		}
		return null;
	}

	/**
	 * Checks whether the file referenced by the given url is a plugin class
	 *
	 * @param url The URL instance
	 * @return True if it is a plugin class
	 */
	public static boolean isPluginClass(URL url) {
		if (url == null) return false;

		return PLUGIN_PATH_PATTERN.matcher(url.getFile()).find();
	}

	/**
	 * Checks whether the file referenced by the given url is a translator class
	 *
	 * @param url The URL instance
	 * @return True if it is a translator class
	 */
	public static boolean isTranslatorClass(URL url) {
		if (url == null) return false;

		return TRANSLATOR_PATH_PATTERN.matcher(url.getFile()).find();
	}
}