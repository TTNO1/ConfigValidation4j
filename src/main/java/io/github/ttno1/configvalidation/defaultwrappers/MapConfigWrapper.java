package io.github.ttno1.configvalidation.defaultwrappers;

import java.util.List;
import java.util.Map;

import io.github.ttno1.configvalidation.BaseType;
import io.github.ttno1.configvalidation.ConfigWrapper;

/**
 * A {@linkplain ConfigWrapper} that gets its values from a map of strings to objects.<br>
 * This class assumes that all config subsections are contained within the main map as sub-maps.
 */
public class MapConfigWrapper extends AbstractConfigWrapper {

	private final Map<String, Object> map;
	
	private final String separator;
	
	/**
	 * 
	 * @param map
	 * @param separator the regex expression that separates nodes from sub-nodes e.g. "\."
	 * @throws NullPointerException if map is null
	 * @throws IllegalArgumentException if separator is null or empty
	 */
	public MapConfigWrapper(Map<String, Object> map, String separator) {
		if(map == null) {
			throw new NullPointerException("Map cannot be null");
		}
		if(separator == null || separator.isEmpty()) {
			throw new IllegalArgumentException("Separator cannot be null or empty");
		}
		this.map = map;
		this.separator = separator;
	}
	
	@Override
	public Object get(String path) {
		if(path.isEmpty()) {
			return map;
		}
		String[] split = path.split(separator);
		if(split.length > 1) {
			MapConfigWrapper wrapper = this;
			for(int i = 0; i < split.length - 1; i++) {
				wrapper = (MapConfigWrapper) wrapper.getConfigSubsection(split[i]);
				if(wrapper == null) {
					return null;
				}
			}
			return wrapper.get(split[split.length - 1]);
		}
		return map.get(path);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ConfigWrapper getConfigSubsection(String path) {
		try {
			Map<String, Object> subMap = (Map<String, Object>) get(path);
			if(subMap == null) {
				return null;
			}
			return new MapConfigWrapper(subMap, separator);
		} catch (ClassCastException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getList(String path, BaseType baseType, Class<T> type) {
		try {
			if(baseType.equals(BaseType.CONFIG_SECTION)) {
				return (List<T>) ((List<Map<String, Object>>) get(path)).stream().map(map -> {return new MapConfigWrapper(map, separator);}).toList();
			}
			return (List<T>) get(path);
		} catch (ClassCastException e) {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public <T> boolean isList(String path, BaseType baseType, Class<T> type) {
		try {
			if(baseType.equals(BaseType.CONFIG_SECTION)) {
				List<Map<String, Object>> l = (List<Map<String, Object>>) get(path);
			} else {
				List<T> l = (List<T>) get(path);
			}
			return true;
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public boolean isConfigSubsection(String path) {
		try {
			Map<String, Object> m = (Map<String, Object>) get(path);
			return true;
		} catch (ClassCastException e) {
			return false;
		}
	}
	
}
