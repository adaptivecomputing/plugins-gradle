/*
 * Copyright 2011 SpringSource
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

package com.adaptc.mws.plugins.testing;

import com.adaptc.mws.plugins.PluginConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory
import com.adaptc.mws.plugins.testing.support.ConstraintsTestBuilder
import com.adaptc.mws.plugins.testing.support.ValidatorTestDelegate;

/**
 * A mixin that can be applied to a unit test in order to test plugins.
 *
 * @author bsaville
 */
class PluginUnitTestMixin extends UnitTestMixin {

	/**
	 * The plugin configuration object
	 */
	Map<String, Object> config = [:]
	Map<String, Object> appConfig = [:]
	Log log
	Map<String, Map<String, Object>> constraints = [:]

	/**
	 * Signifies that the given plugin class is the class under test
	 *
	 * @param pluginClass The plugin class
	 * @return an instance of the plugin
	 */
	def <T> T testFor(Class<T> pluginClass) {
		return mockPlugin(pluginClass)
	}

	/**
	 * Mocks a MWS plugin class, providing the needed behavior
	 *
	 * @param pluginClass The plugin class
	 * @return An instance of the plugin
	 */
	def <T> T mockPlugin(Class<T> pluginClass) {
		log = LogFactory.getLog(PluginConstants.LOGGER_PREFIX+pluginClass.name)
		
		def plugin = pluginClass.newInstance()
		def mc = plugin.metaClass
		mc.getConfig = { -> config }
		mc.getAppConfig = { -> appConfig }
		mc.getLog = { -> log }
		mc.message = { Map map -> return map.code ?: map.error }

		// Mock constraints
		MetaMethod getConstraints = mc.getStaticMetaMethod("getConstraints", [] as Object[])
		if (getConstraints!=null) {
			Closure constraintsClosure = getConstraints.invoke(pluginClass, null)
			if (constraintsClosure!=null && constraintsClosure instanceof Closure) {
				ConstraintsTestBuilder constraintsTestBuilder = new ConstraintsTestBuilder(pluginClass);
				constraintsClosure.setDelegate(constraintsTestBuilder);
				constraintsClosure.setResolveStrategy(Closure.DELEGATE_ONLY);
				constraintsClosure.call();
				constraints = constraintsTestBuilder.getConstraints() ?: [:]
			}
			constraints.each { String propertyName, Map<String, Object> attributes ->
				if (attributes.containsKey("validator") && attributes.validator instanceof Closure)
					attributes.validator.delegate = new ValidatorTestDelegate(propertyName, plugin)
			}
		}

		return plugin
	}
}