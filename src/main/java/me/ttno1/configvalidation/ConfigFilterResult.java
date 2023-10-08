package me.ttno1.configvalidation;

class ConfigFilterResult<T> {

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
	
	protected static <U> ConfigFilterResult<U> pass(U result) {
		return new ConfigFilterResult<U>(true, result, null);
	}
	
	protected static <U> ConfigFilterResult<U> fail(String message) {
		return new ConfigFilterResult<U>(false, null, message);
	}

}