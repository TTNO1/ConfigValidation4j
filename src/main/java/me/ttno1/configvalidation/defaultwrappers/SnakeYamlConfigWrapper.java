package me.ttno1.configvalidation.defaultwrappers;

import java.util.Map;

import me.ttno1.configvalidation.ConfigWrapper;

/**
 * A {@linkplain ConfigWrapper} that wraps a map produced from any of the {@linkplain org.yaml.snakeyaml.Yaml} load() methods.<br>
 * This class is identical to {@linkplain MapConfigWrapper}.
 */
public class SnakeYamlConfigWrapper extends MapConfigWrapper {

	public SnakeYamlConfigWrapper(Map<String, Object> map) {
		super(map);
	}

}
