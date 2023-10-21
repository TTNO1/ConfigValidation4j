package io.github.ttno1.configvalidation.defaultwrappers;

import java.util.List;

import io.github.ttno1.configvalidation.BaseType;
import io.github.ttno1.configvalidation.ConfigWrapper;

/**
 * A {@linkplain ConfigWrapper} that gets {@code Object} values from some source for a given path.
 */
public abstract class AbstractConfigWrapper implements ConfigWrapper {

	/**
	 * Should return the object at {@code path} in its most specific type 
	 * (i.e. as a {@code String} or a {@code List<Integer>} not as an {@code Object}.)
	 * @param path
	 * @return the item at the specified path or null if path does not exist
	 */
	public abstract Object get(String path);
	
	@Override
	public boolean containsNode(String path) {
		return get(path) != null;
	}

	@Override
	public Boolean getBoolean(String path) {
		if(get(path) instanceof Boolean result) {
			return result;
		}
		return null;
	}

	@Override
	public Byte getByte(String path) {
		if(get(path) instanceof Byte result) {
			return result;
		}
		return null;
	}
	
	@Override
	public ConfigWrapper getConfigSubsection(String path) {
		if(get(path) instanceof ConfigWrapper result) {
			return result;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getList(String path, BaseType baseType, Class<T> type) {
		try {
			return (List<T>) get(path);
		} catch (ClassCastException e) {
			return null;
		}
	}

	@Override
	public Double getDouble(String path) {
		if(get(path) instanceof Double result) {
			return result;
		}
		return null;
	}

	@Override
	public Float getFloat(String path) {
		if(get(path) instanceof Float result) {
			return result;
		}
		return null;
	}

	@Override
	public Integer getInteger(String path) {
		if(get(path) instanceof Integer result) {
			return result;
		}
		return null;
	}

	@Override
	public Long getLong(String path) {
		if(get(path) instanceof Long result) {
			return result;
		}
		return null;
	}

	@Override
	public Short getShort(String path) {
		if(get(path) instanceof Short result) {
			return result;
		}
		return null;
	}

	@Override
	public String getString(String path) {
		return get(path).toString();
	}

	@Override
	public boolean isBoolean(String path) {
		return get(path) instanceof Boolean;
	}

	@Override
	public boolean isByte(String path) {
		return get(path) instanceof Byte;
	}
	
	@Override
	public boolean isConfigSubsection(String path) {
		return get(path) instanceof ConfigWrapper;
	}

	@Override
	public boolean isDouble(String path) {
		return get(path) instanceof Double;
	}

	@Override
	public boolean isFloat(String path) {
		return get(path) instanceof Float;
	}

	@Override
	public boolean isInteger(String path) {
		return get(path) instanceof Integer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public <T> boolean isList(String path, BaseType baseType, Class<T> type) {
		try {
			List<T> l = (List<T>) get(path);
			return true;
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public boolean isLong(String path) {
		return get(path) instanceof Long;
	}

	@Override
	public boolean isShort(String path) {
		return get(path) instanceof Short;
	}

	@Override
	public boolean isString(String path) {
		return get(path) instanceof String;
	}

	

}
