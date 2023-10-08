package me.ttno1.configvalidation;

class ConfigFilterResult<T> {

	private boolean pass;
	
	private T result;
	
	private ConfigFilterResult(boolean pass, T result) {
		this.pass = pass;
		this.result = result;
	}
	
	protected boolean passed() {
		return pass;
	}
	
	/**
	 * Always check if this {@code ConfigFiterResult} passed before attempting to get the result
	 * @return the result if any
	 * @throws IllegalStateException if the result failed
	 */
	protected T getResult() {
		if(!pass) {
			throw new IllegalStateException("Cannot get result of failed filter.");
		}
		return result;
	}
	
	protected static <U> ConfigFilterResult<U> pass(U result) {
		return new ConfigFilterResult<U>(true, result);
	}
	
	protected static <U> ConfigFilterResult<U> fail() {
		return new ConfigFilterResult<U>(false, null);
	}

}
