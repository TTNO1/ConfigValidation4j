package me.ttno1.configvalidation;

/**
 * Factory class for creating {@linkplain ConfigNode}s, {@linkplain ConfigList}s, and {@linkplain ConfigSpec}s.
 */
public final class Cfg {
	
	private Cfg() {}
	
	public static <W> ConfigSpec<W> newSpec(ConfigFilter<ConfigWrapper, W> filter){
		return new ConfigSpec<W>(filter);
	}
	
	public static ConfigSpec<ConfigWrapper> newSpec(){
		return newSpec(ConfigFilter.nullFilter());
	}
	
	public static final class Node {

		private Node() {}
		
		/**
		 * Creates a ConfigNode for a boolean value with the specified filter.
		 * @param <W> the filter output type
		 * @param filter the filter of this node
		 * @return a ConfigNode for a boolean config option with the provided filter
		 */
		public static <W> ConfigNode<Boolean, W> ofBoolean(ConfigFilter<Boolean, W> filter) {
			return new ConfigNode<Boolean, W>(filter, BaseType.BOOLEAN);
		}

		/**
		 * Creates a ConfigNode for a byte value with the specified filter.
		 * @param <W> the filter output type
		 * @param filter the filter of this node
		 * @return a ConfigNode for a byte config option with the provided filter
		 */
		public static <W> ConfigNode<Byte, W> ofByte(ConfigFilter<Byte, W> filter) {
			return new ConfigNode<Byte, W>(filter, BaseType.BYTE);
		}

		/**
		 * Creates a ConfigNode for a double value with the specified filter.
		 * @param <W> the filter output type
		 * @param filter the filter of this node
		 * @return a ConfigNode for a double config option with the provided filter
		 */
		public static <W> ConfigNode<Double, W> ofDouble(ConfigFilter<Double, W> filter) {
			return new ConfigNode<Double, W>(filter, BaseType.DOUBLE);
		}
		
		/**
		 * Creates a ConfigNode for a float value with the specified filter.
		 * @param <W> the filter output type
		 * @param filter the filter of this node
		 * @return a ConfigNode for a float config option with the provided filter
		 */
		public static <W> ConfigNode<Float, W> ofFloat(ConfigFilter<Float, W> filter) {
			return new ConfigNode<Float, W>(filter, BaseType.FLOAT);
		}

		/**
		 * Creates a ConfigNode for a integer value with the specified filter.
		 * @param <W> the filter output type
		 * @param filter the filter of this node
		 * @return a ConfigNode for a integer config option with the provided filter
		 */
		public static <W> ConfigNode<Integer, W> ofInteger(ConfigFilter<Integer, W> filter) {
			return new ConfigNode<Integer, W>(filter, BaseType.INTEGER);
		}

		/**
		 * Creates a ConfigNode for a long value with the specified filter.
		 * @param <W> the filter output type
		 * @param filter the filter of this node
		 * @return a ConfigNode for a long config option with the provided filter
		 */
		public static <W> ConfigNode<Long, W> ofLong(ConfigFilter<Long, W> filter) {
			return new ConfigNode<Long, W>(filter, BaseType.LONG);
		}

		/**
		 * Creates a ConfigNode for a short value with the specified filter.
		 * @param <W> the filter output type
		 * @param filter the filter of this node
		 * @return a ConfigNode for a short config option with the provided filter
		 */
		public static <W> ConfigNode<Short, W> ofShort(ConfigFilter<Short, W> filter) {
			return new ConfigNode<Short, W>(filter, BaseType.SHORT);
		}

		/**
		 * Creates a ConfigNode for a string value with the specified filter.
		 * @param <W> the filter output type
		 * @param filter the filter of this node
		 * @return a ConfigNode for a string config option with the provided filter
		 */
		public static <W> ConfigNode<String, W> ofString(ConfigFilter<String, W> filter) {
			return new ConfigNode<String, W>(filter, BaseType.STRING);
		}
		
		/**
		 * Creates a ConfigNode for a boolean value with no filter (the filter always passes).
		 * @return a ConfigNode for a boolean config option
		 */
		public static ConfigNode<Boolean, Boolean> ofBoolean() {
			return new ConfigNode<Boolean, Boolean>(ConfigFilter.nullFilter(), BaseType.BOOLEAN);
		}

		/**
		 * Creates a ConfigNode for a byte value with no filter (the filter always passes).
		 * @return a ConfigNode for a byte config option
		 */
		public static ConfigNode<Byte, Byte> ofByte() {
			return new ConfigNode<Byte, Byte>(ConfigFilter.nullFilter(), BaseType.BYTE);
		}

		/**
		 * Creates a ConfigNode for a double value with no filter (the filter always passes).
		 * @return a ConfigNode for a double config option
		 */
		public static ConfigNode<Double, Double> ofDouble() {
			return new ConfigNode<Double, Double>(ConfigFilter.nullFilter(), BaseType.DOUBLE);
		}

		/**
		 * Creates a ConfigNode for a float value with no filter (the filter always passes).
		 * @return a ConfigNode for a float config option
		 */
		public static ConfigNode<Float, Float> ofFloat() {
			return new ConfigNode<Float, Float>(ConfigFilter.nullFilter(), BaseType.FLOAT);
		}

		/**
		 * Creates a ConfigNode for a integer value with no filter (the filter always passes).
		 * @return a ConfigNode for a integer config option
		 */
		public static ConfigNode<Integer, Integer> ofInteger() {
			return new ConfigNode<Integer, Integer>(ConfigFilter.nullFilter(), BaseType.INTEGER);
		}

		/**
		 * Creates a ConfigNode for a long value with no filter (the filter always passes).
		 * @return a ConfigNode for a long config option
		 */
		public static ConfigNode<Long, Long> ofLong() {
			return new ConfigNode<Long, Long>(ConfigFilter.nullFilter(), BaseType.LONG);
		}

		/**
		 * Creates a ConfigNode for a short value with no filter (the filter always passes).
		 * @return a ConfigNode for a short config option
		 */
		public static ConfigNode<Short, Short> ofShort() {
			return new ConfigNode<Short, Short>(ConfigFilter.nullFilter(), BaseType.SHORT);
		}

		/**
		 * Creates a ConfigNode for a string value with no filter (the filter always passes).
		 * @return a ConfigNode for a string config option
		 */
		public static ConfigNode<String, String> ofString() {
			return new ConfigNode<String, String>(ConfigFilter.nullFilter(), BaseType.STRING);
		}

	}
	
	public static final class List {
		
		private List() {}
		
		/**
		 * Creates a ConfigList for a list of boolean values with the specified filter and element filter.
		 * @param <W> the filter output type
		 * @param <X> the element filter output type
		 * @param filter the filter that applies to the list after the element filter has been applied to each element
		 * @param elementFilter the filter that is separately applied to each element
		 * @return a ConfigList of boolean values with the specified filters
		 */
		public static <W, X> ConfigList<Boolean, X, W> ofBoolean(ConfigFilter<java.util.List<X>, W> filter, ConfigFilter<Boolean, X> elementFilter) {
			return new ConfigList<Boolean, X, W>(filter, elementFilter, BaseType.BOOLEAN);
		}

		/**
		 * Creates a ConfigList for a list of byte values with the specified filter and element filter.
		 * @param <W> the filter output type
		 * @param <X> the element filter output type
		 * @param filter the filter that applies to the list after the element filter has been applied to each element
		 * @param elementFilter the filter that is separately applied to each element
		 * @return a ConfigList of byte values with the specified filters
		 */
		public static <W, X> ConfigList<Byte, X, W> ofByte(ConfigFilter<java.util.List<X>, W> filter, ConfigFilter<Byte, X> elementFilter) {
			return new ConfigList<Byte, X, W>(filter, elementFilter, BaseType.BYTE);
		}

		/**
		 * Creates a ConfigList for a list of double values with the specified filter and element filter.
		 * @param <W> the filter output type
		 * @param <X> the element filter output type
		 * @param filter the filter that applies to the list after the element filter has been applied to each element
		 * @param elementFilter the filter that is separately applied to each element
		 * @return a ConfigList of double values with the specified filters
		 */
		public static <W, X> ConfigList<Double, X, W> ofDouble(ConfigFilter<java.util.List<X>, W> filter, ConfigFilter<Double, X> elementFilter) {
			return new ConfigList<Double, X, W>(filter, elementFilter, BaseType.DOUBLE);
		}

		/**
		 * Creates a ConfigList for a list of float values with the specified filter and element filter.
		 * @param <W> the filter output type
		 * @param <X> the element filter output type
		 * @param filter the filter that applies to the list after the element filter has been applied to each element
		 * @param elementFilter the filter that is separately applied to each element
		 * @return a ConfigList of float values with the specified filters
		 */
		public static <W, X> ConfigList<Float, X, W> ofFloat(ConfigFilter<java.util.List<X>, W> filter, ConfigFilter<Float, X> elementFilter) {
			return new ConfigList<Float, X, W>(filter, elementFilter, BaseType.FLOAT);
		}

		/**
		 * Creates a ConfigList for a list of integer values with the specified filter and element filter.
		 * @param <W> the filter output type
		 * @param <X> the element filter output type
		 * @param filter the filter that applies to the list after the element filter has been applied to each element
		 * @param elementFilter the filter that is separately applied to each element
		 * @return a ConfigList of integer values with the specified filters
		 */
		public static <W, X> ConfigList<Integer, X, W> ofInteger(ConfigFilter<java.util.List<X>, W> filter, ConfigFilter<Integer, X> elementFilter) {
			return new ConfigList<Integer, X, W>(filter, elementFilter, BaseType.INTEGER);
		}

		/**
		 * Creates a ConfigList for a list of list values with the specified filter and element filter.
		 * @param <W> the filter output type
		 * @param <X> the element filter output type
		 * @param filter the filter that applies to the list after the element filter has been applied to each element
		 * @param elementFilter the filter that is separately applied to each element
		 * @return a ConfigList of list values with the specified filters
		 */
		public static <W, X> ConfigList<java.util.List<Object>, W, X> ofList(ConfigFilter<java.util.List<W>, X> filter, ConfigFilter<java.util.List<Object>, W> elementFilter) {
			return new ConfigList<java.util.List<Object>, W, X>(filter, elementFilter, BaseType.LIST);
		}

		/**
		 * Creates a ConfigList for a list of long values with the specified filter and element filter.
		 * @param <W> the filter output type
		 * @param <X> the element filter output type
		 * @param filter the filter that applies to the list after the element filter has been applied to each element
		 * @param elementFilter the filter that is separately applied to each element
		 * @return a ConfigList of long values with the specified filters
		 */
		public static <W, X> ConfigList<Long, X, W> ofLong(ConfigFilter<java.util.List<X>, W> filter, ConfigFilter<Long, X> elementFilter) {
			return new ConfigList<Long, X, W>(filter, elementFilter, BaseType.LONG);
		}

		/**
		 * Creates a ConfigList for a list of config subsection values with the specified filter and with the specified 
		 * ConfigSpec that is validated against every element.
		 * @param <W> the filter output type
		 * @param <X> the ConfigSpec filter output type
		 * @param filter the filter that applies to the list after the ConfigSpec has been applied to each element
		 * @param configSpec the ConfigSpec that is applied to each element
		 * @return a ConfigList of ConfigWrapper values with the specified filters
		 */
		public static <W, X> ConfigList<ConfigWrapper, X, W> ofConfigSpec(ConfigFilter<java.util.List<X>, W> filter, ConfigSpec<X> configSpec) {
			return new ConfigList<ConfigWrapper, X, W>(filter, configSpec.getFilter(), BaseType.CONFIG_SECTION);
		}

		/**
		 * Creates a ConfigList for a list of short values with the specified filter and element filter.
		 * @param <W> the filter output type
		 * @param <X> the element filter output type
		 * @param filter the filter that applies to the list after the element filter has been applied to each element
		 * @param elementFilter the filter that is separately applied to each element
		 * @return a ConfigList of short values with the specified filters
		 */
		public static <W, X> ConfigList<Short, X, W> ofShort(ConfigFilter<java.util.List<X>, W> filter, ConfigFilter<Short, X> elementFilter) {
			return new ConfigList<Short, X, W>(filter, elementFilter, BaseType.SHORT);
		}

		/**
		 * Creates a ConfigList for a list of string values with the specified filter and element filter.
		 * @param <W> the filter output type
		 * @param <X> the element filter output type
		 * @param filter the filter that applies to the list after the element filter has been applied to each element
		 * @param elementFilter the filter that is separately applied to each element
		 * @return a ConfigList of string values with the specified filters
		 */
		public static <W, X> ConfigList<String, X, W> ofString(ConfigFilter<java.util.List<X>, W> filter, ConfigFilter<String, X> elementFilter) {
			return new ConfigList<String, X, W>(filter, elementFilter, BaseType.STRING);
		}
		
		/**
		 * Creates a ConfigList for a list of boolean values with no filters (the filters always pass).
		 * @return a ConfigList of boolean values
		 */
		public static ConfigList<Boolean, Boolean, java.util.List<Boolean>> ofBoolean() {
			return List.ofBoolean(ConfigFilter.nullFilter(), ConfigFilter.nullFilter());
		}

		/**
		 * Creates a ConfigList for a list of byte values with no filters (the filters always pass).
		 * @return a ConfigList of byte values
		 */
		public static ConfigList<Byte, Byte, java.util.List<Byte>> ofByte() {
			return List.ofByte(ConfigFilter.nullFilter(), ConfigFilter.nullFilter());
		}

		/**
		 * Creates a ConfigList for a list of double values with no filters (the filters always pass).
		 * @return a ConfigList of double values
		 */
		public static ConfigList<Double, Double, java.util.List<Double>> ofDouble() {
			return List.ofDouble(ConfigFilter.nullFilter(), ConfigFilter.nullFilter());
		}

		/**
		 * Creates a ConfigList for a list of float values with no filters (the filters always pass).
		 * @return a ConfigList of float values
		 */
		public static ConfigList<Float, Float, java.util.List<Float>> ofFloat() {
			return List.ofFloat(ConfigFilter.nullFilter(), ConfigFilter.nullFilter());
		}

		/**
		 * Creates a ConfigList for a list of integer values with no filters (the filters always pass).
		 * @return a ConfigList of integer values
		 */
		public static ConfigList<Integer, Integer, java.util.List<Integer>> ofInteger() {
			return List.ofInteger(ConfigFilter.nullFilter(), ConfigFilter.nullFilter());
		}

		/**
		 * Creates a ConfigList for a list of list values with no filters (the filters always pass).
		 * @return a ConfigList of list values
		 */
		public static ConfigList<java.util.List<Object>, java.util.List<Object>, java.util.List<java.util.List<Object>>> ofList() {
			return List.ofList(ConfigFilter.nullFilter(), ConfigFilter.nullFilter());
		}

		/**
		 * Creates a ConfigList for a list of long values with no filters (the filters always pass).
		 * @return a ConfigList of long values
		 */
		public static ConfigList<Long, Long, java.util.List<Long>> ofLong() {
			return List.ofLong(ConfigFilter.nullFilter(), ConfigFilter.nullFilter());
		}

		/**
		 * Creates a ConfigList for a list of config subsection values with no filters (the filters always pass).
		 * @return a ConfigList of ConfigWrapper values
		 */
		public static ConfigList<ConfigWrapper, ConfigWrapper, java.util.List<ConfigWrapper>> ofConfigSpec() {
			return List.ofConfigSpec(ConfigFilter.nullFilter(), new ConfigSpec<ConfigWrapper>(ConfigFilter.nullFilter()));
		}

		/**
		 * Creates a ConfigList for a list of short values with no filters (the filters always pass).
		 * @return a ConfigList of short values
		 */
		public static ConfigList<Short, Short, java.util.List<Short>> ofShort() {
			return List.ofShort(ConfigFilter.nullFilter(), ConfigFilter.nullFilter());
		}

		/**
		 * Creates a ConfigList for a list of string values with no filters (the filters always pass).
		 * @return a ConfigList of string values
		 */
		public static ConfigList<String, String, java.util.List<String>> ofString() {
			return List.ofString(ConfigFilter.nullFilter(), ConfigFilter.nullFilter());
		}

	}
	
}
