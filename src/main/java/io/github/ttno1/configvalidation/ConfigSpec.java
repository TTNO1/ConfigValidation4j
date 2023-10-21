package io.github.ttno1.configvalidation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * A Configuration Specification that specifies various {@linkplain ConfigNode}s that must be present in a configuration.<br>
 * This class is used to validate a config with many nodes.<br>
 * Specifies list of nodes and their corresponding locations (paths) in the configuration.<br>
 * When a ConfigSpec is validated against a {@linkplain ConfigWrapper}, each of its contained nodes are validated as well.
 * @param <U> the return type of the filter of this ConfigSpec
 */
public class ConfigSpec<U> extends ConfigNode<ConfigWrapper, U> {
	
	private Map<String, ConfigNode<?, ?>> nodeMap;
	
	private ConfigFilter<ConfigWrapper, ConfigWrapper> defFilter = (configWrapper) -> {
		boolean failed = false;
		Map<ConfigValidationResult, String> resultMap = new HashMap<ConfigValidationResult, String>();
		for(Entry<String, ConfigNode<?, ?>> entry : nodeMap.entrySet()) {
			ConfigValidationResult result = entry.getValue().validate(configWrapper, entry.getKey());
			if(!result.passed()) {
				failed = true;
			}
			resultMap.put(result, entry.getKey());
		}
		if(failed) {
			return ConfigFilterResult.fail("The following nodes in the config spec failed validation: "
					+ System.lineSeparator() + resultMap.keySet().stream().filter((validationResult) -> {
						return !validationResult.passed();
					}).map((validationResult) -> {
						return resultMap.get(validationResult) + " - " + validationResult.getFailMessage();
					}).collect(Collectors.joining(System.lineSeparator())));
		} else {
			return ConfigFilterResult.pass(configWrapper);
		}
	};
	
	protected ConfigSpec(ConfigFilter<ConfigWrapper, U> filter) {
		super(BaseType.CONFIG_SECTION);
		if(filter == null) {
			throw new NullPointerException("Filter cannot be null");
		}
		this.filter = defFilter.withFilter(filter);
		nodeMap = new HashMap<String, ConfigNode<?,?>>();
	}
	
	/**
	 * Adds the node to this ConfigSpec at the specified path.<br>
	 * Replaces any node that was previously at the specified path.
	 * @param path the path of the provided node relative to the root path of this ConfigSpec
	 * @param node
	 * @return {@code this}
	 * @throws NullPointerException if {@code node} is null
	 */
	public ConfigSpec<U> addNode(String path, ConfigNode<?, ?> node) {
		if(path == null) {
			throw new NullPointerException("Path cannot be null");
		}
		if(node == null) {
			throw new NullPointerException("Node cannot be null");
		}
		nodeMap.put(path, node);
		return this;
	}
	
	/**
	 * Adds the specified nodes to this ConfigSpec at their corresponding paths.<br>
	 * Replaces any nodes that were previously at any specified paths.
	 * @param nodeMap a map of relative paths to {@link ConfigNode}s that should be present in this ConfigSpec
	 * @return {@code this}
	 * @throws NullPointerException if {@code nodeMap} is null or contains null keys or values
	 */
	public ConfigSpec<U> addNodes(Map<String, ConfigNode<?, ?>> nodeMap) {
		if(nodeMap == null) {
			throw new NullPointerException("Node map cannot be null");
		}
		if(nodeMap.containsKey(null) || nodeMap.containsValue(null)) {
			throw new NullPointerException("Node map cannot contain null entries");
		}
		this.nodeMap.putAll(nodeMap);
		return this;
	}
	
	/**
	 * Convenience method that calls {@link ConfigNode#validate(ConfigWrapper, String)} with an empty string as the path argument.<br>
	 * Useful when this {@code ConfigSpec} is at the root of the config.
	 * @param configWrapper
	 * @return
	 */
	public ConfigValidationResult validate(ConfigWrapper configWrapper) {
		return super.validate(configWrapper, "");
	}
	
}
