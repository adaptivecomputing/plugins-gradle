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
import org.apache.commons.logging.LogFactory;

/**
 * A mixin that can be applied to a unit test in order to test plugins.
 *
 * @author bsaville
 */
class TranslatorUnitTestMixin extends UnitTestMixin {

	Log log
	
	/**
	 * Signifies that the given translator class is the class under test
	 *
	 * @param translatorClass The translator class
	 * @return an instance of the translator
	 */
	def <T> T testFor(Class<T> translatorClass) {
		return mockTranslator(translatorClass)
	}

	/**
	 * Mocks a MWS translator class, providing the needed behavior
	 *
	 * @param translatorClass The translator class
	 * @return An instance of the translator
	 */
	def <T> T mockTranslator(Class<T> translatorClass) {
		log = LogFactory.getLog(PluginConstants.LOGGER_PREFIX+translatorClass.name)
		
		def translator = translatorClass.newInstance()
		def mc = translator.metaClass
		mc.getLog = { -> log }
		
		return translator
	}
}