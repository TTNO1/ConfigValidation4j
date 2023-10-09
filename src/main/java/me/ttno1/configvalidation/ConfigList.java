package me.ttno1.configvalidation;

import java.util.List;

import me.ttno1.configvalidation.ConfigValidationResult.ValidationFailReason;
import me.ttno1.configvalidation.ConfigValidationResult.ValidationFailResult;

public class ConfigList<T, U, V> extends ConfigNode<List<T>, V> {

	private ConfigNode<T, U> elementNode;
	
	protected ConfigList(String path, ConfigFilter<List<U>, V> filter, ConfigNode<T, U> elementNode) {
		
		super(path, ConfigFilters.<T, U>forEach((t) -> {
			return elementNode.filter(t);
		}).withFilter(filter), BaseType.LIST);
		
		if(elementNode == null) {
			throw new IllegalArgumentException("Element node cannot be null");
		}
		
		this.elementNode = elementNode;
		
	}
	
	@Override
	protected ValidationFailResult validate(ConfigDictionary configDictionary) {
		
		if(!configDictionary.containsNode(path)) {
			return new ValidationFailResult(ValidationFailReason.MISSING, null);
		}
		
		if(elementNode.getBaseType() != BaseType.LIST) {
			if(!configDictionary.isList(path, elementNode.getBaseType())) {
				return new ValidationFailResult(ValidationFailReason.INVALID_TYPE, null);
			}
			if(!filter(configDictionary.getList(path, elementNode.getBaseType()))) {
				return new ValidationFailResult(ValidationFailReason.FAILED_FILTER, result.getFailMessage());
			}
		} else {
			if(!configDictionary.isList(path, elementNode.getBaseType())) {
				return new ValidationFailResult(ValidationFailReason.INVALID_TYPE, null);
			}
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
			
		}
		
		return null;
		
		
		if(elementNode instanceof ConfigList elementList) {
			
		}
		
	}

}
