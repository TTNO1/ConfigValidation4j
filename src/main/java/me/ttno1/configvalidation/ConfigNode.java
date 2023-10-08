package me.ttno1.configvalidation;

import java.util.List;

import me.ttno1.configvalidation.ConfigValidationResult.ValidationFailReason;

public class ConfigNode<T, U> {
	
	protected final String path;
	
	private ConfigFilter<T, U> filter;
	
	private U result;
	
	protected final BaseType baseType;
	
	protected ConfigNode(String path, ConfigFilter<T, U> filter, BaseType baseType) {
		
		if(path == null) {
			throw new IllegalArgumentException("Path cannot be null");
		}
		if(filter == null) {
			throw new IllegalArgumentException("Filter cannot be null");
		}
		if(baseType == null) {
			throw new IllegalArgumentException("BaseType cannot be null");
		}
		
		this.path = path;
		this.filter = filter;
		this.baseType = baseType;
		
	}
	
	/**
	 * Validates this node against the supplied {@link ConfigDictionary}.<br>
	 * Does not validate sub-nodes if this is a {@link ConfigSpec}.<br>
	 * Checks for existence, valid type, and filter pass.<br>
	 * Should only be called once.
	 * @param configDictionary
	 * @return the fail reason if it failed or null if it passed
	 */
	protected ValidationFailReason validate(ConfigDictionary configDictionary) {
		if(!configDictionary.containsNode(path)) {
			return ValidationFailReason.MISSING;
		}
		
		boolean isValidType = switch (baseType) {
		case BOOLEAN:
			yield configDictionary.isBoolean(path);
		case BYTE:
			yield configDictionary.isByte(path);
		case DOUBLE:
			yield configDictionary.isDouble(path);
		case FLOAT:
			yield configDictionary.isFloat(path);
		case INTEGER:
			yield configDictionary.isInteger(path);
		case LIST:
			yield configDictionary.isList(path);
		case LONG:
			yield configDictionary.isLong(path);
		case MAP:
			yield configDictionary.isConfigDictionary(path);
		case SHORT:
			yield configDictionary.isShort(path);
		case STRING:
			yield configDictionary.isString(path);
		case UNKNOWN:
			yield true;
		};
		if(!isValidType) {
			return ValidationFailReason.INVALID_TYPE;
		}
		
		@SuppressWarnings("unchecked")
		boolean passedFilter = switch (baseType) {
		case BOOLEAN:
			yield ((ConfigNode<Boolean, ?>) this).filter(configDictionary.getBoolean(path));
		case BYTE:
			yield ((ConfigNode<Byte, ?>) this).filter(configDictionary.getByte(path));
		case DOUBLE:
			yield ((ConfigNode<Double, ?>) this).filter(configDictionary.getDouble(path));
		case FLOAT:
			yield ((ConfigNode<Float, ?>) this).filter(configDictionary.getFloat(path));
		case INTEGER:
			yield ((ConfigNode<Integer, ?>) this).filter(configDictionary.getInteger(path));
		case LIST:
			yield ((ConfigNode<List<?>, ?>) this).filter(configDictionary.getList(path));
		case LONG:
			yield ((ConfigNode<Long, ?>) this).filter(configDictionary.getLong(path));
		case MAP:
			yield ((ConfigNode<ConfigDictionary, ?>) this).filter(configDictionary.getConfigDictionary(path));
		case SHORT:
			yield ((ConfigNode<Short, ?>) this).filter(configDictionary.getShort(path));
		case STRING:
			yield ((ConfigNode<String, ?>) this).filter(configDictionary.getString(path));
		case UNKNOWN:
			yield ((ConfigNode<Object, ?>) this).filter(configDictionary.getObject(path));
		};
		if(!passedFilter) {
			return ValidationFailReason.FAILED_FILTER;
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param input
	 * @return true if filter passed false otherwise
	 * @throws IllegalStateException if this {@link ConfigNode} has already been filtered
	 */
	protected boolean filter(T input) {
		if(filter == null) {
			throw new IllegalStateException("Cannot filter ConfigNode more than once");
		}
		ConfigFilterResult<U> filterResult = filter.filter(input);
		filter = null;
		if(filterResult.passed()) {
			result = filterResult.getResult();
			return true;
		}
		return false;
	}
	
	/**
	 * Note: this method should not be called before the node has been filtered. 
	 * The node is filtered when it or its containing {@link ConfigSpec} is validated.
	 * @return the filtered result of this node if it has already been filtered
	 * @throws IllegalStateException if this {@link ConfigNode} has not yet been filtered
	 */
	public U getResult() {
		if(filter != null) {
			throw new IllegalStateException("Cannot get result before filtering ConfigNode");
		}
		return result;
	}
	
	/**
	 * 
	 * @return the path of this node
	 */
	public String getPath() {
		return path;
	}
	
	protected BaseType getBaseType() {
		return baseType;
	}

	protected enum BaseType {
		BOOLEAN,
		BYTE,
		DOUBLE,
		FLOAT,
		INTEGER,
		LIST,
		LONG,
		MAP,
		SHORT,
		STRING,
		UNKNOWN;
	}
	
}
