package io.github.ttno1.configvalidation;

/**
 * Identical to {@linkplain java.util.function.Consumer} except that the {@linkplain #accept(Object)} 
 * method may throw a checked exception.
 * @param <T>
 * @param <E> the exception that is thrown
 */
@FunctionalInterface
public interface CheckedConsumer<T, E extends Throwable> {

	void accept(T t) throws E;
	
	default CheckedConsumer<T, E> andThen(CheckedConsumer<T, E> after) {
		return (T t) -> {
			accept(t);
			after.accept(t);
		};
	}
	
}
