package me.ttno1.configvalidation;

import java.util.List;

/**
 * Abstraction over config classes from many libraries.<br>
 * If the underlying config does not contain a value at a given path, the get methods in this interface return null.<br>
 * If any arguments of any method are null, the methods of this interface may throw a NullPointerException.<br>
 * The {@link #getConfigSubsection(String)} method called with an empty string as the argument should return 
 * a ConfigWrapper equal to {@code this}, {@link #isConfigSubsection(String)} with an empty string should return true, 
 * and {@link #containsNode(String)} with an empty string should return true.
 */
public interface ConfigWrapper {
	
	boolean containsNode(String path);
	
	Boolean getBoolean(String path);
	
	Byte getByte(String path);
	
	ConfigWrapper getConfigSubsection(String path);
	
	<T> List<T> getList(String path, BaseType baseType, Class<T> type);
	
	Double getDouble(String path);
	
	Float getFloat(String path);
	
	Integer getInteger(String path);
	
	Long getLong(String path);
	
	Short getShort(String path);
	
	String getString(String path);
	
	boolean isBoolean(String path);
	
	boolean isByte(String path);
	
	boolean isConfigSubsection(String path);
	
	boolean isDouble(String path);
	
	boolean isFloat(String path);
	
	boolean isInteger(String path);
	
	<T> boolean isList(String path, BaseType baseType, Class<T> type);
	
	boolean isLong(String path);
	
	boolean isShort(String path);
	
	boolean isString(String path);
	
}
