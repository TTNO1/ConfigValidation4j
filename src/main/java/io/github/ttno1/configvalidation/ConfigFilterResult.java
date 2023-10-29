package io.github.ttno1.configvalidation;

/**
 * Represents the result of a {@linkplain ConfigFilter}.<br>
 * Specifies whether it passed or failed, the output if it passed, and the fail message if it failed.
 * @param <T> the output type
 */
public class ConfigFilterResult<T> {

	private final boolean pass;
	
	private final T result;
	
	private final String failMsg;
	
	private ConfigFilterResult(boolean pass, T result, String failMsg) {
		this.pass = pass;
		this.result = result;
		this.failMsg = failMsg;
	}
	
	protected boolean passed() {
		return pass;
	}
	
	/**
	 * Always ensure this {@code ConfigFiterResult} passed before attempting to get the result
	 * @return the result
	 * @throws IllegalStateException if this result failed
	 */
	protected T getResult() {
		if(!pass) {
			throw new IllegalStateException("Cannot get result of failed filter");
		}
		return result;
	}
	
	/**
	 * Always ensure this {@code ConfigFiterResult} failed before attempting to get the fail message
	 * @return the fail message
	 * @throws IllegalStateException if this result passed
	 */
	protected String getFailMessage() {
		if(pass) {
			throw new IllegalStateException("Cannot get fail message of passed filter");
		}
		return failMsg;
	}
	
	/**
	 * Creates a new ConfigFilterResult that passed with the specified output.
	 * @param <U> the output type
	 * @param result the output
	 * @return a new ConfigFilterResult that passed with the specified output
	 */
	protected static <U> ConfigFilterResult<U> pass(U result) {
		return new ConfigFilterResult<U>(true, result, null);
	}
	
	/**
	 * Creates a new ConfigFilterResult that failed with the specified fail message.
	 * @param <U> the output type
	 * @param message the fail message
	 * @return a new ConfigFilterResult that failed with the specified fail message
	 */
	protected static <U> ConfigFilterResult<U> fail(String message) {
		return new ConfigFilterResult<U>(false, null, message);
	}
	
	/**
	 * Creates a new ConfigFilterResult that failed with an empty fail message.
	 * @param <U> the output type
	 * @return a new ConfigFilterResult that failed
	 */
	protected static <U> ConfigFilterResult<U> fail() {
		return fail("");
	}

}