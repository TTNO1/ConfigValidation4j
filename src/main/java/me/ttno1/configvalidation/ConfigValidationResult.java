package me.ttno1.configvalidation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ConfigValidationResult {

	public static Builder Builder() {
		return new Builder();
	}
	
	private final Map<ConfigNode<?, ?>, ValidationFailReason> failedNodes;
	
	private String message = null;
	
	private ConfigValidationResult(Map<ConfigNode<?, ?>, ValidationFailReason> failedNodes) {
		this.failedNodes = failedNodes;
	}
	
	public boolean passed() {
		return failedNodes.isEmpty();
	}
	
	/**
	 * 
	 * @return An unmodifiable map of the nodes that failed and their fail reason (empty if none)
	 */
	public Map<ConfigNode<?, ?>, ValidationFailReason> getFailedNodes() {
		return failedNodes;
	}
	
	/**
	 * 
	 * @return A message listing each node that failed and why or a success message if all nodes passed
	 */
	public String getMessage() {
		if(message != null) {
			return message;
		}
		if(failedNodes.isEmpty()) {
			return message = "All configuration options are valid.";
		}
		StringBuilder builder = new StringBuilder("The following configuration options are invalid or missing:" + System.lineSeparator());
		for(Entry<ConfigNode<?, ?>, ValidationFailReason> entry : failedNodes.entrySet()) {
			builder.append(entry.getKey().getPath()).append(" : ").append(entry.getValue().getFriendlyName());
		}
		return message = builder.toString();
	}
	
	public enum ValidationFailReason {
		MISSING("Missing Option"),
		INVALID_TYPE("Invalid Type"),
		FAILED_FILTER("Invalid Format");
		
		private final String friendlyName;
		
		private ValidationFailReason(String friendlyName) {
			this.friendlyName = friendlyName;
		}
		
		public String getFriendlyName() {
			return friendlyName;
		}
	}
	
	protected static class Builder {
		
		private Map<ConfigNode<?, ?>, ValidationFailReason> nodeMap;
		
		private boolean built = false;
		
		private Builder() {
			nodeMap = new HashMap<ConfigNode<?,?>, ValidationFailReason>();
		}
		
		/**
		 * 
		 * @param node
		 * @param reason
		 * @return {@code this}
		 * @throws IllegalStateException if builder has already been built
		 */
		protected Builder addNode(ConfigNode<?, ?> node, ValidationFailReason reason) {
			if(built) {
				throw new IllegalStateException("Cannot add node to builder that has already been built");
			}
			nodeMap.put(node, reason);
			return this;
		}
		
		/**
		 * 
		 * @return
		 * @throws IllegalStateException if already built
		 */
		protected ConfigValidationResult build() {
			if(built) {
				throw new IllegalStateException("Cannot build more than once");
			}
			built = true;
			return new ConfigValidationResult(Collections.unmodifiableMap(nodeMap));
		}
		
	}

}
