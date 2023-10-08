package me.ttno1.configvalidation;

@FunctionalInterface
public interface ConfigFilter<T, U> {
	
	ConfigFilterResult<U> filter(T input);
	
	default <V> ConfigFilter<T, V> withFilter(ConfigFilter<U, V> filter) {
		return (T in) -> {
			ConfigFilterResult<U> result = filter(in);
			if(!result.passed()) {
				return ConfigFilterResult.fail();
			}
			return filter.filter(result.getResult());
		};
	}
	
}
