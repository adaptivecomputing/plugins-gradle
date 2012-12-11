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
package com.adaptc.mws.plugins.testing.transformations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.SourceUnit;

import java.lang.reflect.Modifier;
import java.net.URL;

/**
 * Adds a log method to all artifacts.
 * <br /><br />
 * Note that this comes from Grails.
 * @author Graeme Rocher
 */
public class LoggingTransformer implements ClassInjector{
	public static final String LOG_PROPERTY = "log";

	public void performInjection(SourceUnit source, GeneratorContext context, ClassNode classNode) {
		final FieldNode existingField = classNode.getDeclaredField(LOG_PROPERTY);
		if (existingField == null && !classNode.isInterface()) {
			String logName = "plugins." + classNode.getName();
			addLogField(classNode, logName);
		}
	}

	public static void addLogField(ClassNode classNode, String logName) {
		FieldNode logVariable = new FieldNode(LOG_PROPERTY,
											  Modifier.STATIC | Modifier.PRIVATE,
											  new ClassNode(Log.class),
											  classNode,
											  new MethodCallExpression(new ClassExpression(new ClassNode(LogFactory.class)), "getLog", new ArgumentListExpression(new ConstantExpression(logName))));

		classNode.addField(logVariable);
	}

	public void performInjection(SourceUnit source, ClassNode classNode) {
		performInjection(source, null, classNode);
	}

	public boolean shouldInject(URL url) {
		return true; // Add log property to all artifact types
	}
}