package io.github.ttno1.configvalidation.defaultwrappers;

import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConversionException;

import io.github.ttno1.configvalidation.BaseType;
import io.github.ttno1.configvalidation.ConfigWrapper;

/**
 * A {@linkplain ConfigWrapper} that wraps an Apache Commons Config {@linkplain Configuration}.
 */
public class CommonsConfigWrapper implements ConfigWrapper {

	private final Configuration config;
	
	public CommonsConfigWrapper(Configuration config) {
		this.config = config;
	}
	
	@Override
	public boolean containsNode(String path) {
		return config.containsKey(path) || !config.subset(path).isEmpty();
	}

	@Override
	public Boolean getBoolean(String path) {
		try {
			return config.getBoolean(path);
		} catch (ConversionException e) {
			return null;
		}
	}

	@Override
	public Byte getByte(String path) {
		try {
			return config.getByte(path);
		} catch (ConversionException e) {
			return null;
		}
	}

	@Override
	public ConfigWrapper getConfigSubsection(String path) {
		return new CommonsConfigWrapper(config.subset(path));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getList(String path, BaseType baseType, Class<T> type) {
		try {
			if(baseType.equals(BaseType.CONFIG_SECTION) && config instanceof HierarchicalConfiguration hConfig) {
				return (List<T>) ((List<HierarchicalConfiguration<?>>) hConfig.configurationsAt(path)).stream().map(hCon -> {return new CommonsConfigWrapper(hCon);}).toList();
			}
			return config.getList(type, path);
		} catch (ConversionException e) {
			return null;
		}
	}

	@Override
	public Double getDouble(String path) {
		try {
			return config.getDouble(path);
		} catch (ConversionException e) {
			return null;
		}
	}

	@Override
	public Float getFloat(String path) {
		try {
			return config.getFloat(path);
		} catch (ConversionException e) {
			return null;
		}
	}

	@Override
	public Integer getInteger(String path) {
		try {
			return config.getInt(path);
		} catch (ConversionException e) {
			return null;
		}
	}

	@Override
	public Long getLong(String path) {
		try {
			return config.getLong(path);
		} catch (ConversionException e) {
			return null;
		}
	}

	@Override
	public Short getShort(String path) {
		try {
			return config.getShort(path);
		} catch (ConversionException e) {
			return null;
		}
	}

	@Override
	public String getString(String path) {
		try {
			return config.getString(path);
		} catch (ConversionException e) {
			return null;
		}
	}

	@Override
	public boolean isBoolean(String path) {
		try {
			config.getBoolean(path);
			return true;
		} catch (ConversionException e) {
			return false;
		}
	}

	@Override
	public boolean isByte(String path) {
		try {
			config.getByte(path);
			return true;
		} catch (ConversionException e) {
			return false;
		}
	}

	@Override
	public boolean isConfigSubsection(String path) {
		if(path.isEmpty()) {
			return true;
		}
		return !config.subset(path).isEmpty();
	}

	@Override
	public boolean isDouble(String path) {
		try {
			config.getDouble(path);
			return true;
		} catch (ConversionException e) {
			return false;
		}
	}

	@Override
	public boolean isFloat(String path) {
		try {
			config.getFloat(path);
			return true;
		} catch (ConversionException e) {
			return false;
		}
	}

	@Override
	public boolean isInteger(String path) {
		try {
			config.getInt(path);
			return true;
		} catch (ConversionException e) {
			return false;
		}
	}

	@Override
	public <T> boolean isList(String path, BaseType baseType, Class<T> type) {
		try {
			config.getList(type, path);
			return true;
		} catch (ConversionException e) {
			return false;
		}
	}

	@Override
	public boolean isLong(String path) {
		try {
			config.getLong(path);
			return true;
		} catch (ConversionException e) {
			return false;
		}
	}

	@Override
	public boolean isShort(String path) {
		try {
			config.getShort(path);
			return true;
		} catch (ConversionException e) {
			return false;
		}
	}

	@Override
	public boolean isString(String path) {
		try {
			config.getString(path);
			return true;
		} catch (ConversionException e) {
			return false;
		}
	}

}
