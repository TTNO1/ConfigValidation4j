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
	
	/**
	 * 
	 * @param map
	 * @throws NullPointerException if map is null
	 */
	public MapConfigWrapper(Map<String, Object> map) {
		if(map == null) {
			throw new NullPointerException("Map cannot be null");
		}
		this.map = map;
	}
	
	@Override
	public Object get(String path) {
		if(path.isEmpty()) {
			return map;
		}
		return map.get(path);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ConfigWrapper getConfigSubsection(String path) {
		try {
			return new MapConfigWrapper((Map<String, Object>) get(path));
		} catch (ClassCastException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getList(String path, BaseType baseType, Class<T> type) {
		try {
			if(baseType.equals(BaseType.CONFIG_SECTION)) {
				return (List<T>) ((List<Map<String, Object>>) get(path)).stream().map(map -> {return new MapConfigWrapper(map);}).toList();
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
