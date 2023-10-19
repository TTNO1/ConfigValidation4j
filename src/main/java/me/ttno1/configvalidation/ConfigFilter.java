package me.ttno1.configvalidation;

import java.util.function.Consumer;

/**
 * A functional interface for functions that validate config input and optionally convert it to a new output type.<br>
 * ConfigFilters are intended to be chained together with {@linkplain ConfigFilter#withFilter(ConfigFilter)}.<br>
 * See {@linkplain ConfigFilterResult} for information on the return type of this function.
 * @param <T> The input type
 * @param <U> The output type
 */
@FunctionalInterface
public interface ConfigFilter<T, U> {
	
	/**
	 * Checks that the input is valid and optionally converts it to a new type.
	 * @param input the input to filter
	 * @return a {@link ConfigFilterResult} containing whether this filter passed and the new type it was converted to
	 */
	ConfigFilterResult<U> filter(T input);
	
	/**
	 * Returns a filter that takes in the input of {@code this} and returns the output of the supplied filter.<br>
	 * Useful for chaining multiple filters together into one filter.
	 * @param <V> the type that the new filter returns
	 * @param filter a filter that takes the output of {@code this} and returns a new type
	 * @return a filter that takes the input of {@code this} and returns the output of the supplied filter
	 * @throws NullPointerException if filter is null
	 */
	default <V> ConfigFilter<T, V> withFilter(ConfigFilter<U, V> filter) {
		if(filter == null) {
			throw new NullPointerException("Filter cannot be null");
		}
		return (T in) -> {
			ConfigFilterResult<U> result = filter(in);
			if(!result.passed()) {
				return ConfigFilterResult.fail(result.getFailMessage());
			}
			return filter.filter(result.getResult());
		};
	}
	
	/**
	 * Runs a {@link Consumer} with the output of this filter.<br>
	 * Useful for retrieving the value of a filter.
	 * @param consumer the consumer to be run
	 * @return a filter that outputs the same value as {@code this} after running the supplied {@link Consumer}
	 */
	default ConfigFilter<T, U> thenRun(Consumer<U> consumer) {
		return (T in) -> {
			ConfigFilterResult<U> result = filter(in);
			if(result.passed()) {
				consumer.accept(result.getResult());
			}
			return result;
		};
	}
	
	/**
	 * Returns a ConfigFilter that runs the provided {@link Consumer} and passes.
	 * @param consumer the consumer to be run
	 * @return a filter that outputs its input after running the supplied {@link Consumer}
	 */
	static <X> ConfigFilter<X, X> run(Consumer<X> consumer) {
		return ConfigFilter.<X>nullFilter().thenRun(consumer);
	}
	
	/**
	 * A "null" filter that does nothing. It will always pass with its input as its output.
	 * @param <X> the type of the filter input and output
	 * @return a filter that passes with its input as its output
	 */
	static <X> ConfigFilter<X, X> nullFilter(){
		return (X in) -> {return ConfigFilterResult.pass(in);};
	}
	
}
