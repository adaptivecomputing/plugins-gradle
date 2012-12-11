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


import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass

import com.adaptc.mws.plugins.testing.support.MetaClassRegistryCleaner;

/**
 * A base unit testing mixin that watches for MetaClass changes and unbinds them on tear down.
 * <br /><br />
 * Note that this comes from Grails (GrailsUnitTestMixin).
 * @author Graeme Rocher
 * @author bsaville
 */
class UnitTestMixin {

	static {
		ExpandoMetaClass.enableGlobally()
	}

	private static MetaClassRegistryCleaner metaClassRegistryListener = MetaClassRegistryCleaner.createAndRegister()

	Map validationErrorsMap = new IdentityHashMap()
	Set loadedCodecs = []

	@BeforeClass
	static void initMetaClassWatcher() {
		GroovySystem.metaClassRegistry.addMetaClassRegistryChangeEventListener metaClassRegistryListener
	}

	@After
	void resetMetaClassWatcher() {
		metaClassRegistryListener.clean()
	}

	@AfterClass
	static void deregisterMetaClassCleaner() {
		GroovySystem.metaClassRegistry.removeMetaClassRegistryChangeEventListener(metaClassRegistryListener)
	}
}