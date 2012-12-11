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

import com.adaptc.mws.plugins.testing.TestMixin;
import com.adaptc.mws.plugins.testing.support.MixinMethod;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingMethodException;
import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.junit.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An AST transformation to be applied to tests for adding behavior to a target test class.
 * <br /><br />
 * Note that this comes from Grails.
 * @author Graeme Rocher
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class TestMixinTransformation implements ASTTransformation {
	public static final AnnotationNode MIXIN_METHOD_ANNOTATION = new AnnotationNode(new ClassNode(MixinMethod.class));
	private static final ClassNode MY_TYPE = new ClassNode(TestMixin.class);
	private static final String MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage();
	public static final String OBJECT_CLASS = "java.lang.Object";
	public static final String SPEC_CLASS = "spock.lang.Specification";
	private static final String JUNIT3_CLASS = "junit.framework.TestCase";
	public static final String SET_UP_METHOD = "setUp";
	public static final VariableExpression THIS_EXPRESSION = new VariableExpression("this");
	public static final String TEAR_DOWN_METHOD = "tearDown";
	public static final ClassNode GROOVY_OBJECT_CLASS_NODE = new ClassNode(GroovyObjectSupport.class);
	public static final ClassNode MISSING_METHOD_EXCEPTION = new ClassNode(MissingMethodException.class);
	public static final Token ASSIGNMENT_OPERATOR = Token.newSymbol(Types.ASSIGNMENT_OPERATOR, 0, 0);
	public static final Token EQUALS_OPERATOR = Token.newSymbol("==", 0, 0);
	public static final Token NOT_EQUALS_OPERATOR = Token.newSymbol("!=", 0, 0);
	public static final ConstantExpression NULL_EXPRESSION = new ConstantExpression(null);
	public static final ClassNode[] EMPTY_CLASS_ARRAY = new ClassNode[0];
	public static final Parameter[] ZERO_PARAMETERS = new Parameter[0];
	public static final ArgumentListExpression ZERO_ARGS = new ArgumentListExpression();
	public static final String METHOD_MISSING_METHOD_NAME = "methodMissing";
	public static final String STATIC_METHOD_MISSING_METHOD_NAME = "$static_methodMissing";

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
		String cName = classNode.getName();
		if (classNode.isInterface()) {
			throw new RuntimeException("Error processing interface '" + cName + "'. " +
					MY_TYPE_NAME + " not allowed for interfaces.");
		}

		ListExpression values = getListOfClasses(node);

		weaveMixinsIntoClass(classNode, values);


	}

	protected ListExpression getListOfClasses(AnnotationNode node) {
		Expression value = node.getMember("value");
		ListExpression values = null;
		if (value instanceof ListExpression) {
			values = (ListExpression) value;
		}
		else if (value instanceof ClassExpression) {
			values = new ListExpression();
			values.addExpression(value);
		}

		return values;
	}

	public void weaveMixinsIntoClass(ClassNode classNode, ListExpression values) {
		if (values != null) {
			boolean isJunit3 = isJunit3Test(classNode);
			List<MethodNode> beforeMethods = null;
			List<MethodNode> afterMethods = null;
			if (isJunit3) {
				beforeMethods = new ArrayList<MethodNode>();
				afterMethods = new ArrayList<MethodNode>();
			}
			for (Expression current : values.getExpressions()) {
				if (current instanceof ClassExpression) {
					ClassExpression ce = (ClassExpression) current;

					ClassNode mixinClassNode = ce.getType();

					final String fieldName = '$' + getPropertyNameRepresentation(mixinClassNode.getName());

					addFieldIfNonExistent(classNode, mixinClassNode, fieldName);
					VariableExpression fieldReference = new VariableExpression(fieldName);

					while (!mixinClassNode.getName().equals(OBJECT_CLASS)) {
						final List<MethodNode> mixinMethods = mixinClassNode.getMethods();

						int beforeClassMethodCount = 0;
						int afterClassMethodCount = 0;
						for (MethodNode mixinMethod : mixinMethods) {
							if (isCandidateMethod(mixinMethod) && !hasDeclaredMethod(classNode, mixinMethod)) {
								if (mixinMethod.isStatic()) {
									MethodNode methodNode = addDelegateStaticMethod(classNode, mixinMethod);
									if (methodNode != null) {
										methodNode.addAnnotation(MIXIN_METHOD_ANNOTATION);
									}
								}
								else {
									MethodNode methodNode = addDelegateInstanceMethod(classNode, fieldReference, mixinMethod, false);
									if (methodNode != null) {
										methodNode.addAnnotation(MIXIN_METHOD_ANNOTATION);
									}
								}
								if (isJunit3) {

									if (hasAnnotation(mixinMethod, Before.class)) {
										beforeMethods.add(mixinMethod);
									}
									if (hasAnnotation(mixinMethod, BeforeClass.class)) {
										beforeMethods.add(beforeClassMethodCount++, mixinMethod);
									}
									if (hasAnnotation(mixinMethod, After.class)) {
										afterMethods.add(mixinMethod);
									}
									if (hasAnnotation(mixinMethod, AfterClass.class)) {
										afterMethods.add(afterClassMethodCount++, mixinMethod);
									}
								}
							}
						}

						mixinClassNode = mixinClassNode.getSuperClass();
					}
				}
			}

			if (isJunit3) {
				addMethodCallsToMethod(classNode, SET_UP_METHOD, beforeMethods);
				addMethodCallsToMethod(classNode, TEAR_DOWN_METHOD, afterMethods);
			}
		}
	}

	protected boolean hasDeclaredMethod(ClassNode classNode, MethodNode mixinMethod) {
		return classNode.hasDeclaredMethod(mixinMethod.getName(), mixinMethod.getParameters());
	}

	protected boolean hasAnnotation(MethodNode mixinMethod, Class<?> beforeClass) {
		return !mixinMethod.getAnnotations(new ClassNode(beforeClass)).isEmpty();
	}

	protected void addMethodCallsToMethod(ClassNode classNode, String name, List<MethodNode> methods) {
		if (methods != null && !methods.isEmpty()) {
			BlockStatement setupMethodBody = getOrCreateNoArgsMethodBody(classNode, name);
			for (MethodNode beforeMethod : methods) {
				setupMethodBody.addStatement(new ExpressionStatement(new MethodCallExpression(THIS_EXPRESSION, beforeMethod.getName(), ZERO_ARGS)));
			}
		}
	}

	protected BlockStatement getOrCreateNoArgsMethodBody(ClassNode classNode, String name) {
		MethodNode setupMethod = classNode.getMethod(name, ZERO_PARAMETERS);
		return getOrCreateMethodBody(classNode, setupMethod, name);
	}

	protected BlockStatement getOrCreateMethodBody(ClassNode classNode, MethodNode setupMethod, String name) {
		BlockStatement methodBody;
		if (setupMethod.getDeclaringClass().getName().equals(TestCase.class.getName())) {
			methodBody = new BlockStatement();
			setupMethod = new MethodNode(name, Modifier.PUBLIC,setupMethod.getReturnType(), ZERO_PARAMETERS,null, methodBody);
			classNode.addMethod(setupMethod);
		}
		else {
			final Statement setupMethodBody = setupMethod.getCode();
			if (!(setupMethodBody instanceof BlockStatement)) {
				methodBody = new BlockStatement();
				if (setupMethodBody != null) {
					if (!(setupMethodBody instanceof ReturnStatement)) {
						methodBody.addStatement(setupMethodBody);
					}
				}
				setupMethod.setCode(methodBody);
			}
			else {
				methodBody = (BlockStatement) setupMethodBody;
			}
		}
		return methodBody;
	}

	public static boolean isJunit3Test(ClassNode classNode) {
		return isSubclassOf(classNode, JUNIT3_CLASS);
	}

	public static boolean isSpockTest(ClassNode classNode) {
		return isSubclassOf(classNode, SPEC_CLASS);
	}

	private static boolean isSubclassOf(ClassNode classNode, String testType) {
		ClassNode currentSuper = classNode.getSuperClass();
		while (currentSuper != null && !currentSuper.getName().equals(OBJECT_CLASS)) {
			if (currentSuper.getName().equals(testType)) return true;
			currentSuper = currentSuper.getSuperClass();
		}
		return false;
	}

	protected boolean isCandidateMethod(MethodNode declaredMethod) {
		return isAddableMethod(declaredMethod);
	}

	public static boolean isAddableMethod(MethodNode declaredMethod) {
		ClassNode groovyMethods = GROOVY_OBJECT_CLASS_NODE;
		String methodName = declaredMethod.getName();
		return !declaredMethod.isSynthetic() &&
				!methodName.contains("$") &&
				Modifier.isPublic(declaredMethod.getModifiers()) &&
				!Modifier.isAbstract(declaredMethod.getModifiers()) &&
				!groovyMethods.hasMethod(declaredMethod.getName(), declaredMethod.getParameters());
	}

	protected void error(SourceUnit source, String me) {
		source.getErrorCollector().addError(new SimpleMessage(me,source), true);
	}

	public static void addFieldIfNonExistent(ClassNode classNode, ClassNode fieldType, String fieldName) {
		if (classNode != null && classNode.getField(fieldName) == null) {
			classNode.addField(fieldName, Modifier.PRIVATE, fieldType,
					new ConstructorCallExpression(fieldType, new ArgumentListExpression()));
		}
	}

	/**
	 * Adds a delegate method to the target class node where the first argument
	 * is to the delegate method is 'this'. In other words a method such as
	 * foo(Object instance, String bar) would be added with a signature of foo(String)
	 * and 'this' is passed to the delegate instance
	 *
	 * @param classNode The class node
	 * @param delegate The expression that looks up the delegate
	 * @param declaredMethod The declared method
	 * @return The added method node or null if it couldn't be added
	 */
	public static MethodNode addDelegateInstanceMethod(ClassNode classNode, Expression delegate, MethodNode declaredMethod) {
	   return addDelegateInstanceMethod(classNode, delegate, declaredMethod, true);
	}

	/**
	 * Adds a delegate method to the target class node where the first argument
	 * is to the delegate method is 'this'. In other words a method such as
	 * foo(Object instance, String bar) would be added with a signature of foo(String)
	 * and 'this' is passed to the delegate instance
	 *
	 * @param classNode The class node
	 * @param delegate The expression that looks up the delegate
	 * @param declaredMethod The declared method
	 * @param thisAsFirstArgument Whether 'this' should be passed as the first argument to the method
	 * @return The added method node or null if it couldn't be added
	 */
	public static MethodNode addDelegateInstanceMethod(ClassNode classNode, Expression delegate, MethodNode declaredMethod, boolean thisAsFirstArgument) {
		Parameter[] parameterTypes = thisAsFirstArgument ? getRemainingParameterTypes(declaredMethod.getParameters()) : declaredMethod.getParameters();
		String methodName = declaredMethod.getName();
		if (classNode.hasDeclaredMethod(methodName, parameterTypes)) {
			return null;
		}
		String propertyName = getPropertyForGetter(methodName);
		if(propertyName != null && parameterTypes.length == 0 && classNode.hasProperty(propertyName)) {
			return null;
		}
		propertyName = getPropertyForSetter(methodName);
		if(propertyName != null && parameterTypes.length == 1 && classNode.hasProperty(propertyName)) {
			return null;
		}

		BlockStatement methodBody = new BlockStatement();
		ArgumentListExpression arguments = createArgumentListFromParameters(parameterTypes, thisAsFirstArgument);

		ClassNode returnType = nonGeneric(declaredMethod.getReturnType());

		MethodCallExpression methodCallExpression = new MethodCallExpression(delegate, methodName, arguments);
		methodCallExpression.setMethodTarget(declaredMethod);
		ThrowStatement missingMethodException = createMissingMethodThrowable(classNode, declaredMethod);
		VariableExpression apiVar = addApiVariableDeclaration(delegate, declaredMethod, methodBody);
		IfStatement ifStatement = createIfElseStatementForApiMethodCall(methodCallExpression, apiVar, missingMethodException);

		methodBody.addStatement(ifStatement);
		MethodNode methodNode = new MethodNode(methodName,
				Modifier.PUBLIC, returnType, copyParameters(parameterTypes),
				EMPTY_CLASS_ARRAY, methodBody);
		methodNode.addAnnotations(declaredMethod.getAnnotations());

		classNode.addMethod(methodNode);
		return methodNode;
	}

	private static Parameter[] copyParameters(Parameter[] parameterTypes) {
		Parameter[] newParameterTypes = new Parameter[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			Parameter parameterType = parameterTypes[i];
			Parameter newParameter = new Parameter(nonGeneric(parameterType.getType()), parameterType.getName(), parameterType.getInitialExpression());
			newParameter.addAnnotations(parameterType.getAnnotations());
			newParameterTypes[i] = newParameter;
		}
		return newParameterTypes;
	}

	private static IfStatement createIfElseStatementForApiMethodCall(MethodCallExpression methodCallExpression, VariableExpression apiVar, ThrowStatement missingMethodException) {
		BlockStatement ifBlock = new BlockStatement();
		ifBlock.addStatement(missingMethodException);
		BlockStatement elseBlock = new BlockStatement();
		elseBlock.addStatement(new ExpressionStatement(methodCallExpression));

		return new IfStatement(new BooleanExpression(new BinaryExpression(apiVar, EQUALS_OPERATOR, NULL_EXPRESSION)),ifBlock,elseBlock);
	}

	public static ClassNode nonGeneric(ClassNode type) {
		if (type.isUsingGenerics()) {
			final ClassNode nonGen = ClassHelper.makeWithoutCaching(type.getName());
			nonGen.setRedirect(type);
			nonGen.setGenericsTypes(null);
			nonGen.setUsingGenerics(false);
			return nonGen;
		}

		if (type.isArray()) {
			final ClassNode nonGen = ClassHelper.makeWithoutCaching(Object.class);
			nonGen.setUsingGenerics(false);
			return nonGen.makeArray();
		}

		return type.getPlainNodeReference();
	}

	private static VariableExpression addApiVariableDeclaration(Expression delegate, MethodNode declaredMethod, BlockStatement methodBody) {
		VariableExpression apiVar = new VariableExpression("$api_" + declaredMethod.getName());
		DeclarationExpression de = new DeclarationExpression(apiVar, ASSIGNMENT_OPERATOR, delegate);
		methodBody.addStatement(new ExpressionStatement(de));
		return apiVar;
	}

	private static ThrowStatement createMissingMethodThrowable(ClassNode classNode, MethodNode declaredMethodNode) {
		ArgumentListExpression exceptionArgs = new ArgumentListExpression();
		exceptionArgs.addExpression(new ConstantExpression(declaredMethodNode.getName()));
		exceptionArgs.addExpression(new ClassExpression(classNode));
		return new ThrowStatement(new ConstructorCallExpression(MISSING_METHOD_EXCEPTION, exceptionArgs));
	}

	/**
	 * Gets the remaining parameters excluding the first parameter in the given list
	 *
	 * @param parameters The parameters
	 * @return A new array with the first parameter removed
	 */
	public static Parameter[] getRemainingParameterTypes(Parameter[] parameters) {
		if (parameters.length == 0) {
			return ZERO_PARAMETERS;
		}

		Parameter[] newParameters = new Parameter[parameters.length - 1];
		System.arraycopy(parameters, 1, newParameters, 0, parameters.length - 1);
		return newParameters;
	}

	/**
	 * Creates an argument list from the given parameter types.
	 *
	 * @param parameterTypes The parameter types
	 * @param thisAsFirstArgument Whether to include a reference to 'this' as the first argument
	 *
	 * @return the arguments
	 */
	public static ArgumentListExpression createArgumentListFromParameters(Parameter[] parameterTypes, boolean thisAsFirstArgument) {
		ArgumentListExpression arguments = new ArgumentListExpression();

		if (thisAsFirstArgument) {
			arguments.addExpression(THIS_EXPRESSION);
		}

		for (Parameter parameterType : parameterTypes) {
			arguments.addExpression(new VariableExpression(parameterType.getName()));
		}
		return arguments;
	}

	/**
	 * Returns a property name equivalent for the given getter name or null if it is not a getter
	 *
	 * @param getterName The getter name
	 * @return The property name equivalent
	 */
	public static String getPropertyForGetter(String getterName) {
		if (StringUtils.isBlank(getterName))return null;

		if (getterName.startsWith("get")) {
			String prop = getterName.substring(3);
			return convertPropertyName(prop);
		}
		if (getterName.startsWith("is")) {
			String prop = getterName.substring(2);
			return convertPropertyName(prop);
		}
		return null;
	}

	/**
	 * Returns a property name equivalent for the given setter name or null if it is not a getter
	 *
	 * @param setterName The setter name
	 * @return The property name equivalent
	 */
	public static String getPropertyForSetter(String setterName) {
		if (StringUtils.isBlank(setterName))return null;

		if (setterName.startsWith("set")) {
			String prop = setterName.substring(3);
			return convertPropertyName(prop);
		}
		return null;
	}

	private static String convertPropertyName(String prop) {
		if (Character.isUpperCase(prop.charAt(0)) && Character.isUpperCase(prop.charAt(1))) {
			return prop;
		}
		if (Character.isDigit(prop.charAt(0))) {
			return prop;
		}
		return Character.toLowerCase(prop.charAt(0)) + prop.substring(1);
	}

	/**
	 * Returns the property name representation of the given name.
	 *
	 * @param name The name to convert
	 * @return The property name representation
	 */
	public static String getPropertyNameRepresentation(String name) {
		// Strip any package from the name.
		int pos = name.lastIndexOf('.');
		if (pos != -1) {
			name = name.substring(pos + 1);
		}

		// Check whether the name begins with two upper case letters.
		if (name.length() > 1 && Character.isUpperCase(name.charAt(0)) &&
				Character.isUpperCase(name.charAt(1))) {
			return name;
		}

		String propertyName = name.substring(0,1).toLowerCase(Locale.ENGLISH) + name.substring(1);
		if (propertyName.indexOf(' ') > -1) {
			propertyName = propertyName.replaceAll("\\s", "");
		}
		return propertyName;
	}

	/**
	 * Adds a static method call to given class node that delegates to the given method
	 *
	 * @param classNode The class node
	 * @param delegateMethod The delegate method
	 * @return The added method node or null if it couldn't be added
	 */
	public static MethodNode addDelegateStaticMethod(ClassNode classNode, MethodNode delegateMethod) {
		ClassExpression classExpression = new ClassExpression(delegateMethod.getDeclaringClass());
		return addDelegateStaticMethod(classExpression, classNode, delegateMethod);
	}

	/**
	 * Adds a static method to the given class node that delegates to the given method
	 * and resolves the object to invoke the method on from the given expression.
	 *
	 * @param expression The expression
	 * @param classNode The class node
	 * @param delegateMethod The delegate method
	 * @return The added method node or null if it couldn't be added
	 */
	public static MethodNode addDelegateStaticMethod(Expression expression, ClassNode classNode, MethodNode delegateMethod) {
		Parameter[] parameterTypes = delegateMethod.getParameters();
		String declaredMethodName = delegateMethod.getName();
		if (classNode.hasDeclaredMethod(declaredMethodName, parameterTypes)) {
			return null;
		}

		BlockStatement methodBody = new BlockStatement();
		ArgumentListExpression arguments = new ArgumentListExpression();

		for (Parameter parameterType : parameterTypes) {
		   arguments.addExpression(new VariableExpression(parameterType.getName()));
	   }
		MethodCallExpression methodCallExpression = new MethodCallExpression(
				expression, declaredMethodName, arguments);
		methodCallExpression.setMethodTarget(delegateMethod);

		ThrowStatement missingMethodException = createMissingMethodThrowable(classNode, delegateMethod);
		VariableExpression apiVar = addApiVariableDeclaration(expression, delegateMethod, methodBody);
		IfStatement ifStatement = createIfElseStatementForApiMethodCall(methodCallExpression, apiVar, missingMethodException);

		methodBody.addStatement(ifStatement);
		ClassNode returnType = nonGeneric(delegateMethod.getReturnType());
		if (METHOD_MISSING_METHOD_NAME.equals(declaredMethodName)) {
			declaredMethodName = STATIC_METHOD_MISSING_METHOD_NAME;
		}
		MethodNode methodNode = classNode.getDeclaredMethod(declaredMethodName, parameterTypes);
		if(methodNode == null) {
			methodNode = new MethodNode(declaredMethodName,
				Modifier.PUBLIC | Modifier.STATIC,
				returnType, copyParameters(parameterTypes),
				EMPTY_CLASS_ARRAY, methodBody);
			methodNode.addAnnotations(delegateMethod.getAnnotations());

			classNode.addMethod(methodNode);
		}
		return methodNode;
	}

	/**
	 * Calculate the name for a getter method to retrieve the specified property
	 * @param propertyName
	 * @return The name for the getter method for this property, if it were to exist, i.e. getConstraints
	 */
	public static String getGetterName(String propertyName) {
		final String suffix;
		if (propertyName.length() > 1 &&
				Character.isLowerCase(propertyName.charAt(0)) &&
				Character.isUpperCase(propertyName.charAt(1))) {
			suffix = propertyName;
		} else {
			suffix = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
		}
		return "get" + suffix;
	}
}