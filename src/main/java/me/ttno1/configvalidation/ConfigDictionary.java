package me.ttno1.configvalidation;

import java.util.List;

/**
 * Abstraction over config classes from many libraries.<br>
 * If the underlying config does not contain a certain option, the get methods in this interface return null.
 */
public interface ConfigDictionary {
	
	boolean containsNode(String path);
	
	boolean getBoolean(String path);
	
	byte getByte(String path);
	
	ConfigDictionary getConfigDictionary(String path);
	
	<T> List<T> getList(String path);
	
	double getDouble(String path);
	
	float getFloat(String path);
	
	int getInteger(String path);
	
	long getLong(String path);
	
	Object getObject(String path);
	
	short getShort(String path);
	
	String getString(String path);
	
	boolean isBoolean(String path);
	
	boolean isByte(String path);
	
	boolean isConfigDictionary(String path);
	
	boolean isDouble(String path);
	
	boolean isFloat(String path);
	
	boolean isInteger(String path);
	
	<T> boolean isList(String path);
	
	boolean isLong(String path);
	
	boolean isShort(String path);
	
	boolean isString(String path);
	
}
