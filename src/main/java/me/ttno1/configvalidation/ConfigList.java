package me.ttno1.configvalidation;

import java.util.List;

/**
 * Represents a list of elements that should exist in a configuration.<br>
 * Specifies a data type, a filter, and an element filter that a list node should match.<br>
 * The element filter is applied to each element separately to produce a list of type U and the filter
 * is applied to the List{@literal <}U{@literal >} produced by applying the element filter to the input.<br>
 * When a list is validated against a ConfigWrapper, the type of the list is verified to be correct 
 * and both filters are confirmed to have passed.
 * @param <T> The data type of the list
 * @param <U> The output type of the element filter
 * @param <V> The output type of the filter
 */
public class ConfigList<T, U, V> extends ConfigNode<List<T>, V> {

	//private final ConfigFilter<T, U> elementFilter;
	
	private final BaseType elementBaseType;
	
	/**
	 * 
	 * @param filter
	 * @param elementFilter
	 * @throws NullPointerException if {@code elementFilter}, {@code elementBaseType}, or {@code filter} are null
	 */
	protected ConfigList(ConfigFilter<List<U>, V> filter, ConfigFilter<T, U> elementFilter, BaseType elementBaseType) {
		
		super(ConfigFilters.<T, U>forEach(elementFilter).withFilter(filter), BaseType.LIST);
		
		if(elementFilter == null) {
			throw new NullPointerException("Element node cannot be null");
		}
		if(filter == null) {
			throw new NullPointerException("Filter cannot be null");
		}
		if(elementBaseType == null) {
			throw new NullPointerException("Element base type cannot be null");
		}
		
		//this.elementFilter = elementFilter;
		
		this.elementBaseType = elementBaseType;
		
	}
	
	@Override
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
		
		boolean isValidType = switch (elementBaseType) {
		case BOOLEAN:
			yield configWrapper.isList(path, elementBaseType, Boolean.class);
		case BYTE:
			yield configWrapper.isList(path, elementBaseType, Byte.class);
		case DOUBLE:
			yield configWrapper.isList(path, elementBaseType, Double.class);
		case FLOAT:
			yield configWrapper.isList(path, elementBaseType, Float.class);
		case INTEGER:
			yield configWrapper.isList(path, elementBaseType, Integer.class);
		case LIST:
			yield configWrapper.isList(path, elementBaseType, List.class);
		case LONG:
			yield configWrapper.isList(path, elementBaseType, Long.class);
		case CONFIG_SECTION:
			yield configWrapper.isList(path, elementBaseType, ConfigWrapper.class);
		case SHORT:
			yield configWrapper.isList(path, elementBaseType, Short.class);
		case STRING:
			yield configWrapper.isList(path, elementBaseType, String.class);
		};
		if(!isValidType) {
			return ConfigValidationResult.fail("The node is not a list of type: " + elementBaseType.toString());
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ConfigFilterResult<V> filterResult = switch (elementBaseType) {
		case BOOLEAN:
			yield ((ConfigList<Boolean, U, V>) this).filter(configWrapper.getList(path, elementBaseType, Boolean.class));
		case BYTE:
			yield ((ConfigList<Byte, U, V>) this).filter(configWrapper.getList(path, elementBaseType, Byte.class));
		case DOUBLE:
			yield ((ConfigList<Double, U, V>) this).filter(configWrapper.getList(path, elementBaseType, Double.class));
		case FLOAT:
			yield ((ConfigList<Float, U, V>) this).filter(configWrapper.getList(path, elementBaseType, Float.class));
		case INTEGER:
			yield ((ConfigList<Integer, U, V>) this).filter(configWrapper.getList(path, elementBaseType, Integer.class));
		case LIST:
			yield ((ConfigList<List, U, V>) this).filter(configWrapper.getList(path, elementBaseType, List.class));
		case LONG:
			yield ((ConfigList<Long, U, V>) this).filter(configWrapper.getList(path, elementBaseType, Long.class));
		case CONFIG_SECTION:
			yield ((ConfigList<ConfigWrapper, U, V>) this).filter(configWrapper.getList(path, elementBaseType, ConfigWrapper.class));
		case SHORT:
			yield ((ConfigList<Short, U, V>) this).filter(configWrapper.getList(path, elementBaseType, Short.class));
		case STRING:
			yield ((ConfigList<String, U, V>) this).filter(configWrapper.getList(path, elementBaseType, String.class));
		};
		if(!filterResult.passed()) {
			return ConfigValidationResult.fail("The node's filter failed with the following message: " + filterResult.getFailMessage());
		}
		
		return ConfigValidationResult.pass();
		
	}

}
