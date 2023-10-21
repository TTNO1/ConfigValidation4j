package io.github.ttno1.configvalidation;

/**
 * Represents a node that should exist in a configuration.<br>
 * Specifies a data type and a filter that a configuration node should match.<br>
 * When a node is validated against a ConfigWrapper, the type of the node is verified to be correct 
 * and the filter is confirmed to have passed.
 * @param <T> the data type of this node
 * @param <U> the output type of this ConfigNodes's filter.
 */
public class ConfigNode<T, U> {
	//TODO memory cleanup method?
	
	protected ConfigFilter<T, U> filter;
	
	protected final BaseType baseType;
	
	protected ConfigNode(ConfigFilter<T, U> filter, BaseType baseType) {
		
		if(filter == null) {
			throw new NullPointerException("Filter cannot be null");
		}
		if(baseType == null) {
			throw new NullPointerException("BaseType cannot be null");
		}
		
		this.filter = filter;
		this.baseType = baseType;
		
	}
	
	/**
	 * For use in ConfigSpec where filter must be set later.<br>
	 * <b>Always be sure to set and null-check filter later when using this constructor.</b>
	 * @param baseType
	 */
	protected ConfigNode(BaseType baseType) {
		
		if(baseType == null) {
			throw new NullPointerException("BaseType cannot be null");
		}
		
		this.baseType = baseType;
		
	}
	
	/**
	 * Convenience method that returns a new ConfigNode with the provided filter appended onto the current filter using
	 * {@link ConfigFilter#withFilter(ConfigFilter)}.
	 * @param <V> the return type of the new filter
	 * @param filter the filter to append to the current filter
	 * @return a new ConfigNode with the provided filter appended onto the current filter
	 */
	public <V> ConfigNode<T, V> withFilter(ConfigFilter<U, V> filter) {
		return new ConfigNode<T, V>(this.filter.withFilter(filter), baseType);
	}
	
	/**
	 * Validates this node against the supplied {@link ConfigWrapper} at the specified path.<br>
	 * Checks for existence of the node, valid data type, and filter passage.<br>
	 * @param configWrapper the {@link ConfigWrapper} to validate against
	 * @param path the absolute path of this node in the ConfigWrapper
	 * @return a {@link ConfigValidationResult} containing whether the validation was successful and a fail message if it failed
	 * @throws NullPointerException if {@code configWrapper} or {@code path} are null
	 */
	public ConfigValidationResult validate(ConfigWrapper configWrapper, String path) {
		if(configWrapper == null) {
			throw new NullPointerException("Config wrapper cannot be null");
		}
		if(path == null) {
			throw new NullPointerException("Path cannot be null");
		}
		
		if(!configWrapper.containsNode(path)) {
			return ConfigValidationResult.fail("The node is not contained in the config");
		}
		
		boolean isValidType = switch (baseType) {
		case BOOLEAN:
			yield configWrapper.isBoolean(path);
		case BYTE:
			yield configWrapper.isByte(path);
		case DOUBLE:
			yield configWrapper.isDouble(path);
		case FLOAT:
			yield configWrapper.isFloat(path);
		case INTEGER:
			yield configWrapper.isInteger(path);
		case LIST:
			throw new UnsupportedOperationException("Cannot validate list outside of ConfigList class");
		case LONG:
			yield configWrapper.isLong(path);
		case CONFIG_SECTION:
			yield configWrapper.isConfigSubsection(path);
		case SHORT:
			yield configWrapper.isShort(path);
		case STRING:
			yield configWrapper.isString(path);
		};
		if(!isValidType) {
			return ConfigValidationResult.fail("The node is not of type: " + baseType.toString());
		}
		
		@SuppressWarnings("unchecked")
		ConfigFilterResult<U> filterResult = switch (baseType) {
		case BOOLEAN:
			yield ((ConfigNode<Boolean, U>) this).filter(configWrapper.getBoolean(path));
		case BYTE:
			yield ((ConfigNode<Byte, U>) this).filter(configWrapper.getByte(path));
		case DOUBLE:
			yield ((ConfigNode<Double, U>) this).filter(configWrapper.getDouble(path));
		case FLOAT:
			yield ((ConfigNode<Float, U>) this).filter(configWrapper.getFloat(path));
		case INTEGER:
			yield ((ConfigNode<Integer, U>) this).filter(configWrapper.getInteger(path));
		case LIST:
			throw new UnsupportedOperationException("Cannot validate list outside of ConfigList class");
		case LONG:
			yield ((ConfigNode<Long, U>) this).filter(configWrapper.getLong(path));
		case CONFIG_SECTION:
			yield ((ConfigNode<ConfigWrapper, U>) this).filter(configWrapper.getConfigSubsection(path));
		case SHORT:
			yield ((ConfigNode<Short, U>) this).filter(configWrapper.getShort(path));
		case STRING:
			yield ((ConfigNode<String, U>) this).filter(configWrapper.getString(path));
		};
		if(!filterResult.passed()) {
			return ConfigValidationResult.fail("The node's filter failed with the following message: " + filterResult.getFailMessage());
		}
		
		return ConfigValidationResult.pass();
	}
	
	protected ConfigFilterResult<U> filter(T input) {
		return filter.filter(input);
	}
	
	protected ConfigFilter<T, U> getFilter() {
		return filter;
	}
	
	protected BaseType getBaseType() {
		return baseType;
	}
	
}
