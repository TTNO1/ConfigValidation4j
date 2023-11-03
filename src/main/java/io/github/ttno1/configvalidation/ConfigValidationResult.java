package io.github.ttno1.configvalidation;

import java.util.function.Consumer;

/**
 * Represents the result of a config validation operation.<br>
 * Specifies whether it passed or failed and a fail message if it failed.
 */
public class ConfigValidationResult {
	
	private final boolean passed;
	
	private final String failMessage;
	
	private ConfigValidationResult(boolean passed, String failMessage) {
		this.passed = passed;
		this.failMessage = failMessage;
	}
	
	/**
	 * 
	 * @return whether this validation passed
	 */
	public boolean passed() {
		return passed;
	}
	
	/**
	 * 
	 * @return A message providing detail as to why this validation failed if it failed
	 * @throws IllegalStateException if this validation passed
	 */
	public String getFailMessage() {
		if(passed) {
			throw new IllegalStateException("Cannot get fail message of passed validation result");
		}
		return failMessage;
	}
	
	/**
	 * Convenience method that runs the provided consumer with {@code this} as the argument.<br>
	 * Allows for easy handling of this ConfigValidationResult through method chaining. 
	 * @param consumer the consumer to run on {@code this}
	 * @return {@code this}
	 */
	public ConfigValidationResult handle(Consumer<ConfigValidationResult> consumer) {
		consumer.accept(this);
		return this;
	}
	
	/**
	 * Convenience method that runs the provided checked consumer with {@code this} as the argument.<br>
	 * Allows for easy handling of this ConfigValidationResult through method chaining.
	 * @param <E> the exception type that is thrown by the checked consumer
	 * @param consumer the consumer to run on {@code this}
	 * @return {@code this}
	 * @throws E if the checked consumer throws E
	 */
	public <E extends Throwable> ConfigValidationResult handleChecked(CheckedConsumer<ConfigValidationResult, E> consumer) throws E {
		consumer.accept(this);
		return this;
	}
	
	protected static ConfigValidationResult pass() {
		return new ConfigValidationResult(true, null);
	}
	
	protected static ConfigValidationResult fail(String failMessage) {
		return new ConfigValidationResult(false, failMessage);
	}

}
