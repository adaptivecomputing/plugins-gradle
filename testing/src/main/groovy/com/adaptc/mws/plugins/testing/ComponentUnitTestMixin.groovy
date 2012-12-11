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

/**
 * A mixin that can be applied to a unit test in order to test custom components.
 *
 * @author bsaville
 */
class ComponentUnitTestMixin extends UnitTestMixin {
	/**
	 * Signifies that the given component class is the class under test
	 *
	 * @param componentClass The component class
	 * @return an instance of the component
	 */
	def <T> T testFor(Class<T> componentClass) {
		return mockComponent(componentClass)
	}

	/**
	 * Mocks a MWS component class, providing the needed behavior
	 *
	 * @param componentClass The component class
	 * @return An instance of the component
	 */
	def <T> T mockComponent(Class<T> componentClass) {
		def component = componentClass.newInstance()
		return component
	}
}