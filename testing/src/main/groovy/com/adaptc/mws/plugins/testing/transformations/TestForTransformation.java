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

import com.adaptc.mws.plugins.testing.ComponentUnitTestMixin;
import com.adaptc.mws.plugins.testing.support.PluginsResourceUtils;
import com.adaptc.mws.plugins.testing.TestFor;
import com.adaptc.mws.plugins.testing.PluginUnitTestMixin;
import com.adaptc.mws.plugins.testing.TranslatorUnitTestMixin;
import groovy.util.GroovyTestCase;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

/**
 * Transformation used by the {@link TestFor} annotation to signify the
 * class under test.
 * <br /><br />
 * Note that this comes from Grails.
 * @author Graeme Rocher
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
@SuppressWarnings("rawtypes")
public class TestForTransformation extends TestMixinTransformation {
	private static final ClassNode MY_TYPE = new ClassNode(TestFor.class);
	private static final ClassNode COMPONENT_MIXIN_TYPE = new ClassNode(Component.class);
	private static final String COMPONENT_TYPE = "Component";
	private static final Token ASSIGN = Token.newSymbol("=", -1, -1);
	public static final String FILE_SEPARATOR = File.separator;
	public static final String CLOSURE_MARKER = "$";

	protected static final Map<String, Class> typeToTestMap = new HashMap<String, Class>();
	static {
		typeToTestMap.put("Plugin", PluginUnitTestMixin.class);
		typeToTestMap.put("Translator", TranslatorUnitTestMixin.class);
	}

	public static final ClassNode BEFORE_CLASS_NODE = new ClassNode(Before.class);
	public static final AnnotationNode BEFORE_ANNOTATION = new AnnotationNode(BEFORE_CLASS_NODE);

	public static final AnnotationNode TEST_ANNOTATION = new AnnotationNode(new ClassNode(Test.class));
	public static final ClassNode GROOVY_TEST_CASE_CLASS = new ClassNode(GroovyTestCase.class);

	@Override
	public void visit(ASTNode[] astNodes, SourceUnit source) {
		if (!(astNodes[0] instanceof AnnotationNode) || !(astNodes[1] instanceof AnnotatedNode)) {
			throw new RuntimeException("Internal error: wrong types: $node.class / $parent.class");
		}

		AnnotatedNode parent = (AnnotatedNode) astNodes[1];
		AnnotationNode node = (AnnotationNode) astNodes[0];
		if (!MY_TYPE.equals(node.getClassNode()) || !(parent instanceof ClassNode)) {
			return;
		}

		ClassNode classNode = (ClassNode) parent;
		if (classNode.isInterface() || Modifier.isAbstract(classNode.getModifiers())) {
			return;
		}

		boolean junit3Test = isJunit3Test(classNode);
		boolean spockTest = isSpockTest(classNode);
		boolean isJunit = classNode.getName().endsWith("Tests");

		if(!junit3Test && !spockTest && !isJunit) return;

		Expression value = node.getMember("value");
		ClassExpression ce;
		if (value instanceof ClassExpression) {
			ce = (ClassExpression) value;
			testFor(classNode, ce, true);
		}
		else {
			if (!junit3Test){
				List<AnnotationNode> annotations = classNode.getAnnotations(MY_TYPE);
				if (annotations.size()>0) return; // bail out, in this case it was already applied as a local transform
				// no explicit class specified try by convention
				String fileName = source.getName();
				String className = PluginsResourceUtils.getClassName(new FileSystemResource(fileName));
				if (className != null) {
					boolean isSpock = className.endsWith("Spec");
					String targetClassName = null;

					if (isJunit) {
						targetClassName = className.substring(0, className.indexOf("Tests"));
					}
					else if (isSpock) {
						targetClassName = className.substring(0, className.indexOf("Spec"));
					}

					if (targetClassName != null) {
						Resource targetResource = findResourceForClassName(targetClassName);
						if (targetResource != null) {
							boolean isArtefact = false;
							// Check for other artefact types
							for (String artefactType : typeToTestMap.keySet()) {
								if (targetClassName.endsWith(artefactType)) {
									isArtefact = true;
									testFor(classNode, new ClassExpression(new ClassNode(targetClassName, 0, ClassHelper.OBJECT_TYPE)), true);
									break;
								}
							}
							// Custom component?
							if (!isArtefact)
								testFor(classNode, new ClassExpression(new ClassNode(targetClassName, 0, ClassHelper.OBJECT_TYPE)), false);
						}
					}
				}
			}
		}
	}

	public Resource findResourceForClassName(String className) {
		if (className.contains(CLOSURE_MARKER)) {
			className = className.substring(0, className.indexOf(CLOSURE_MARKER));
		}
		String classNameWithPathSeparator = className.replace(".", FILE_SEPARATOR);
		Resource resource = null;
		for (String pathPattern : getSearchPatternForExtension(classNameWithPathSeparator, ".groovy", ".java")) {
			resource = resolvePathToResource(pathPattern);
			if (resource != null && resource.exists()) {
				break;
			}
		}
		return resource != null && resource.exists() ? resource : null;
	}

	private List<String> getSearchPatternForExtension(String classNameWithPathSeparator, String... extensions) {
		List<String> searchPatterns = new ArrayList<String>();
		String[] classSearchDirectories = new String[] {
			"src"+FILE_SEPARATOR+"main"+FILE_SEPARATOR+"groovy",
			"src"+FILE_SEPARATOR+"main"+FILE_SEPARATOR+"java"
		};
		for (String extension : extensions) {
			String filename = classNameWithPathSeparator + extension;
			for (String classSearchDirectory : classSearchDirectories) {
				searchPatterns.add(classSearchDirectory + FILE_SEPARATOR + filename);
			}
		}

		return searchPatterns;
	}

	private Resource resolvePathToResource(String pathPattern) {
		File file = new File(pathPattern);
		if (file.exists() && !file.isDirectory())
			return new FileSystemResource(file);
		return null;
	}

	/**
	 * Main entry point for the calling the TestForTransformation programmatically.
	 *
	 * @param classNode The class node that represents the test
	 * @param ce The class expression that represents the class to test
	 */
	public void testFor(ClassNode classNode, ClassExpression ce, boolean addLogField) {
		boolean junit3Test = isJunit3Test(classNode);
		boolean isSpockTest = isSpockTest(classNode);

		// make sure the 'log' property is not the one from GroovyTestCase
		if (addLogField) {
			FieldNode log = classNode.getField("log");
			if (log == null || log.getDeclaringClass().equals(GROOVY_TEST_CASE_CLASS)) {
				LoggingTransformer.addLogField(classNode, classNode.getName());
			}
		}

		if (!isSpockTest && !junit3Test) {
			// assume JUnit 4
			Map<String, MethodNode> declaredMethodsMap = classNode.getDeclaredMethodsMap();
			for (String methodName : declaredMethodsMap.keySet()) {
				MethodNode methodNode = declaredMethodsMap.get(methodName);
				if (isCandidateMethod(methodNode) && methodNode.getName().startsWith("test")) {
					if (methodNode.getAnnotations().size()==0) {
						methodNode.addAnnotation(TEST_ANNOTATION);
					}
				}
			}
		}

		final MethodNode methodToAdd = weaveMock(classNode, ce, true);
		if (methodToAdd != null && junit3Test) {
			addMethodCallsToMethod(classNode,SET_UP_METHOD, Arrays.asList(methodToAdd));
		}
	}

	private Map<ClassNode, List<Class>> wovenMixins = new HashMap<ClassNode, List<Class>>();
	protected MethodNode weaveMock(ClassNode classNode, ClassExpression value, boolean isClassUnderTest) {

		ClassNode testTarget = value.getType();
		String className = testTarget.getName();
		MethodNode testForMethod = null;

		// Artefact types
		for (String artefactType : typeToTestMap.keySet()) {
			if (className.endsWith(artefactType)) {
				Class mixinClass = typeToTestMap.get(artefactType);
				if (!isAlreadyWoven(classNode, mixinClass)) {
					weaveMixinClass(classNode, mixinClass);
					return addClassUnderTestMethod(classNode, value, artefactType);
				}
				return null;
			}
		}

		// Component
		Class mixinClass = ComponentUnitTestMixin.class;
		if (!isAlreadyWoven(classNode, mixinClass)) {
			weaveMixinClass(classNode, mixinClass);
			testForMethod = addClassUnderTestMethod(classNode, value, COMPONENT_TYPE);
			return testForMethod;
		}

		return null;
	}

	private boolean isAlreadyWoven(ClassNode classNode, Class mixinClass) {
		List<Class> mixinClasses = wovenMixins.get(classNode);
		if (mixinClasses == null) {
			mixinClasses = new ArrayList<Class>();
			mixinClasses.add(mixinClass);
			wovenMixins.put(classNode, mixinClasses);
		}
		else {
			if (mixinClasses.contains(mixinClass)) {
				return true;
			}

			mixinClasses.add(mixinClass);
		}
		return false;
	}

	protected void weaveMixinClass(ClassNode classNode, Class mixinClass) {
		ListExpression listExpression = new ListExpression();
		listExpression.addExpression(new ClassExpression(new ClassNode(mixinClass)));
		weaveMixinsIntoClass(classNode,listExpression);
	}

	protected MethodNode addClassUnderTestMethod(ClassNode classNode, ClassExpression targetClass, String type) {

		String methodName = "setup" + type + "UnderTest";
		String fieldName = TestMixinTransformation.getPropertyNameRepresentation(type);
		String getterName = TestMixinTransformation.getGetterName(fieldName);
		fieldName = '$' +fieldName;

		if (classNode.getField(fieldName) == null) {
			classNode.addField(fieldName, Modifier.PRIVATE, targetClass.getType(),null);
		}

		MethodNode methodNode = classNode.getMethod(methodName,TestMixinTransformation.ZERO_PARAMETERS);

		VariableExpression fieldExpression = new VariableExpression(fieldName);
		if (methodNode == null) {
			BlockStatement setupMethodBody = new BlockStatement();
			addMockClassUnderTest(type, fieldExpression, targetClass, setupMethodBody);

			methodNode = new MethodNode(methodName, Modifier.PUBLIC, ClassHelper.VOID_TYPE, TestMixinTransformation.ZERO_PARAMETERS,null, setupMethodBody);
			methodNode.addAnnotation(BEFORE_ANNOTATION);
			methodNode.addAnnotation(MIXIN_METHOD_ANNOTATION);
			classNode.addMethod(methodNode);
		}

		MethodNode getter = classNode.getMethod(getterName, TestMixinTransformation.ZERO_PARAMETERS);
		if(getter == null) {
			BlockStatement getterBody = new BlockStatement();
			getter = new MethodNode(getterName, Modifier.PUBLIC, targetClass.getType().getPlainNodeReference(),TestMixinTransformation.ZERO_PARAMETERS,null, getterBody);
			getterBody.addStatement(new ReturnStatement(fieldExpression));
			classNode.addMethod(getter);
		}

		return methodNode;
	}

	protected void addMockCollaborator(String mockType, ClassExpression targetClass, BlockStatement methodBody) {
		ArgumentListExpression args = new ArgumentListExpression();
		args.addExpression(targetClass);
		methodBody.getStatements().add(0, new ExpressionStatement(new MethodCallExpression(THIS_EXPRESSION, "mock" + mockType, args)));
	}

	protected void addMockClassUnderTest(String mockType, VariableExpression field, ClassExpression targetClass, BlockStatement methodBody) {
		ArgumentListExpression args = new ArgumentListExpression();
		args.addExpression(targetClass);
		MethodCallExpression mockMethodCall = new MethodCallExpression(THIS_EXPRESSION, "mock" + mockType, args);
		BinaryExpression assignmentExpression = new BinaryExpression(field, ASSIGN, mockMethodCall);
		methodBody.getStatements().add(0, new ExpressionStatement(assignmentExpression));
	}
}