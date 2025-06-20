package io.github.ttno1.configvalidation.defaultwrappers;

import java.util.Collections;
import java.util.Map;

import io.github.ttno1.configvalidation.ConfigWrapper;

/**
 * A {@linkplain ConfigWrapper} that wraps a map produced from any of the {@linkplain org.yaml.snakeyaml.Yaml} load() methods.<br>
 * This class is identical to {@linkplain MapConfigWrapper}.
 */
public class SnakeYamlConfigWrapper extends MapConfigWrapper {
	
	private static Map<String, Object> getMapIfNull(Map<String, Object> map) {
		if(map == null) {
			return Collections.emptyMap();
		}
		return map;
	}
	
	public SnakeYamlConfigWrapper(Map<String, Object> map) {
		super(getMapIfNull(map), "\\.");
	}

}
