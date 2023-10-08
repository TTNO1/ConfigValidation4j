package me.ttno1.configvalidation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ttno1.configvalidation.ConfigValidationResult.ValidationFailReason;

public class ConfigSpec<U> extends ConfigNode<ConfigDictionary, U> {
	
	private Map<String, ConfigNode<?, ?>> nodeMap;
	
	private ConfigSpec(String path, ConfigFilter<ConfigDictionary, U> filter) {
		super(path, filter, BaseType.MAP);
		nodeMap = new HashMap<String, ConfigNode<?,?>>();
	}
	
	/**
	 * Adds the node to this ConfigSpec.<br>
	 * Replaces any node that was previously at the path of {@code node}.
	 * @param node
	 * @return
	 * @throws IllegalArgumentException if {@code node} is null
	 */
	public ConfigSpec<U> addNode(ConfigNode<?, ?> node) {
		if(node == null) {
			throw new IllegalArgumentException("Node cannot be null");
		}
		nodeMap.put(node.getPath(), node);
		return this;
	}
	
	/**
	 * Validates each node in this {@code ConfigSpec} against the provided {@link ConfigDictionary}.<br>
	 * This method verifies for each node that it is represented by the appropriate type and that all filters pass. 
	 * This method should not be called more than once.<br>
	 * Note that calling this method also calls this method on any {@code ConfigSpec}s contained by {@code this}.
	 * @return a {@link ConfigValidationResult} containing any failed nodes
	 */
	public ConfigValidationResult validate(ConfigDictionary configDictionary) {
		ConfigValidationResult.Builder builder = ConfigValidationResult.Builder();
		
	}
	
}
