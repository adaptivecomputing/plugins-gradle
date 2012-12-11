package com.adaptc.mws.plugins.testing.support

/**
 * Generates a map of property names to constraints map from the
 * plugin's constraints to aid in testing.
 * @author bsaville
 * @see com.adaptc.mws.plugins.testing.PluginUnitTestMixin
 */
class ConstraintsTestBuilder extends BuilderSupport {
	Map<String, Map<String, Object>> constraints =
		new LinkedHashMap<String, Map<String, Object>>();
	private Class<?> targetClass;

	public ConstraintsTestBuilder(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object createNode(Object name, Map attributes) {
		constraints.put(name, attributes);
		return constraints;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object createNode(Object name, Map attributes, Object value) {
		throw new MissingMethodException((String)name,null, [attributes, value] as Object[]);
	}

	@Override
	protected void setParent(Object parent, Object child) {
		// do nothing
	}

	@Override
	protected Object createNode(Object name) {
		return createNode(name, Collections.EMPTY_MAP);
	}

	@Override
	protected Object createNode(Object name, Object value) {
		return createNode(name,Collections.EMPTY_MAP,value);
	}
}
