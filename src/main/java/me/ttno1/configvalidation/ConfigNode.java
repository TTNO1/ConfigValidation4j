package me.ttno1.configvalidation;

import me.ttno1.configvalidation.ConfigValidationResult.ValidationFailReason;
import me.ttno1.configvalidation.ConfigValidationResult.ValidationFailResult;

public class ConfigNode<T, U> {
	
	protected final String path;
	
	protected ConfigFilter<T, U> filter;
	
	protected ConfigFilterResult<U> result;
	
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
	protected ValidationFailResult validate(ConfigDictionary configDictionary) {
		if(!configDictionary.containsNode(path)) {
			return new ValidationFailResult(ValidationFailReason.MISSING, null);
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
			throw new UnsupportedOperationException("Cannot validate list outside of ConfigList class");
		case LONG:
			yield configDictionary.isLong(path);
		case MAP:
			yield configDictionary.isConfigDictionary(path);
		case SHORT:
			yield configDictionary.isShort(path);
		case STRING:
			yield configDictionary.isString(path);
		};
		if(!isValidType) {
			return new ValidationFailResult(ValidationFailReason.INVALID_TYPE, null);
		}
		
		@SuppressWarnings("unchecked")
		boolean passedFilter = switch (baseType) {
		case BOOLEAN:
			yield ((ConfigNode<Boolean, U>) this).filter(configDictionary.getBoolean(path));
		case BYTE:
			yield ((ConfigNode<Byte, U>) this).filter(configDictionary.getByte(path));
		case DOUBLE:
			yield ((ConfigNode<Double, U>) this).filter(configDictionary.getDouble(path));
		case FLOAT:
			yield ((ConfigNode<Float, U>) this).filter(configDictionary.getFloat(path));
		case INTEGER:
			yield ((ConfigNode<Integer, U>) this).filter(configDictionary.getInteger(path));
		case LIST:
			throw new UnsupportedOperationException("Cannot validate list outside of ConfigList class");
		case LONG:
			yield ((ConfigNode<Long, U>) this).filter(configDictionary.getLong(path));
		case MAP:
			yield ((ConfigNode<ConfigDictionary, U>) this).filter(configDictionary.getConfigDictionary(path));
		case SHORT:
			yield ((ConfigNode<Short, U>) this).filter(configDictionary.getShort(path));
		case STRING:
			yield ((ConfigNode<String, U>) this).filter(configDictionary.getString(path));
		};
		if(!passedFilter) {
			return new ValidationFailResult(ValidationFailReason.FAILED_FILTER, result.getFailMessage());
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param input
	 * @return true if filter passed false otherwise
	 * @throws IllegalStateException if this {@link ConfigNode} has already been filtered
	 */
	protected ConfigFilterResult<U> filter(T input) {//TODO return value?
		if(filter == null) {
			throw new IllegalStateException("Cannot filter ConfigNode more than once");
		}
		result = filter.filter(input);
		filter = null;
		return result;
	}
	
	protected ConfigFilterResult<U> getFilterResult() {
		return result;
	}
	
	protected BaseType getBaseType() {
		return baseType;
	}
	
	/**
	 * Note: this method should not be called before the node has been filtered. 
	 * The node is filtered when it or its containing {@link ConfigSpec} is validated.
	 * @return the filtered result of this node if it has already been filtered
	 * @throws IllegalStateException if this {@link ConfigNode} has not yet been filtered or the filter failed
	 */
	public U getResult() {
		if(filter != null) {
			throw new IllegalStateException("Cannot get result before filtering ConfigNode");
		}
		return result.getResult();
	}
	
	/**
	 * 
	 * @return the path of this node
	 */
	public String getPath() {
		return path;
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
		STRING;
	}
	
}
