package me.ttno1.configvalidation;

import java.util.HashMap;
import java.util.Map;

public class ConfigSpec<U> extends ConfigNode<ConfigDictionary, U> {
	
	private Map<String, ConfigNode<?, ?>> nodeMap;
	
	private ConfigFilter<ConfigDictionary, ConfigDictionary> defFilter = (configDictionary) -> {
		for(ConfigNode<?, ?> node : nodeMap.values()) {
			if(node.validate(configDictionary) != null) {
				//TODO accumulate fails etc.
			}
		}
		return null;//TODO
	};
	
	private ConfigSpec(String path, ConfigFilter<ConfigDictionary, U> filter) {
		super(path, null, BaseType.MAP);
		this.filter = defFilter.withFilter(filter);
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
	
}
