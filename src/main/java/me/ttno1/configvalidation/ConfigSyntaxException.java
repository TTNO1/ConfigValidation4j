package me.ttno1.configvalidation;

public class ConfigSyntaxException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfigSyntaxException() {
		super();
	}

	public ConfigSyntaxException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConfigSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigSyntaxException(String message) {
		super(message);
	}

	public ConfigSyntaxException(Throwable cause) {
		super(cause);
	}
	
}
