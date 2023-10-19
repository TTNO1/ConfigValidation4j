package me.ttno1.configvalidation;

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
	
	protected static ConfigValidationResult pass() {
		return new ConfigValidationResult(true, null);
	}
	
	protected static ConfigValidationResult fail(String failMessage) {
		return new ConfigValidationResult(false, failMessage);
	}

}
