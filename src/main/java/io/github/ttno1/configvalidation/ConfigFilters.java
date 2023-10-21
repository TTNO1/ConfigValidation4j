package io.github.ttno1.configvalidation;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Utility class with methods that provide common {@linkplain ConfigFilter}s.
 */
public final class ConfigFilters {

	private static String[] DEFAULT_SCHEMES = new String[] {"http", "https"};
	
	private ConfigFilters() {}
	//TODO cache results of all methods where possible
	
	/**
	 * Returns a filter that applies the provided filter to a list of elements of the provided filter's input type and
	 * returns a list of elements of the provided filter's output type.<br>
	 * Similar to {@link Stream#map(java.util.function.Function)}.<br>
	 * Note that in the event of a failure, the returned filter will continue to apply the supplied filter to the remaining
	 * elements in the list and the fail message will contain the fail message of all failed list items.
	 * @param <T>
	 * @param <U>
	 * @param filter
	 * @return a filter that maps the provided filter to a list
	 */
	public static <T, U> ConfigFilter<List<T>, List<U>> forEach(ConfigFilter<T, U> filter) {
		return (List<T> input) -> {
			boolean failed = false;
			List<ConfigFilterResult<U>> result = new ArrayList<ConfigFilterResult<U>>();
			for(T in : input) {
				ConfigFilterResult<U> filterResult = filter.filter(in);
				if(!filterResult.passed()) {
					failed = true;
				}
				result.add(filterResult);
			}
			if(failed) {
				return ConfigFilterResult.fail("One or more filters failed while being applied to a list:"
						+ System.lineSeparator() + result.stream().filter((configResult) -> {
							return !configResult.passed();
						}).map((configFilter) -> {
							return configFilter.getFailMessage();
						}).collect(Collectors.joining(System.lineSeparator())));
			} else {
				return ConfigFilterResult.pass(result.stream().map((configResult) -> {return configResult.getResult();}).toList());
			}
		};
	}
	
	/**
	 * Returns a filter that attempts to convert the input string into a value of the provided enum type
	 * and fails if the input string is not a valid enum type.<br>
	 * The filter will call {@link String#toUpperCase()} on the input string and replace all spaces (U+0020) with underscores 
	 * before attempting to call {@link Enum#valueOf(Class, String)}.
	 * @param <T>
	 * @param enumClass the enum type to convert to
	 * @return a filter that attempts to convert the input string into a value of the provided enum type
	 */
	public static <T extends Enum<T>> ConfigFilter<String, T> validEnum(Class<T> enumClass) {
		return (String string) -> {
			try {
				return ConfigFilterResult.pass(Enum.valueOf(enumClass, string.toUpperCase().replace(' ', '_')));
			} catch(IllegalArgumentException | NullPointerException e) {
				return ConfigFilterResult.fail("Invalid string, could not be converted to an enum value: " + string);
			}
		};
	}
	
	/**
	 * Returns a filter that first verifies that the input string is a valid URL using Apache Commons Validator 
	 * {@link UrlValidator#isValid(String)} with the provided schemes, and then attempts to convert the string into a {@link URL} 
	 * with {@link URL#URL(String)}.
	 * @param schemes the schemes to pass into the {@link UrlValidator} (e.g. "http", "https", "ftp") or none to use the default {"http", "https"}
	 * @return a filter that converts a string into a URL if valid
	 */
	public static ConfigFilter<String, URL> validURL(String ... schemes) {
		if(schemes == null || schemes.length == 0) {
			schemes = DEFAULT_SCHEMES;
		}
		UrlValidator validator = new UrlValidator(schemes);
		return (String string) -> {
			if(!validator.isValid(string)) {
				return ConfigFilterResult.fail("Invalid URL: " + string);
			}
			try {
				return ConfigFilterResult.pass(new URI(string).toURL());
			} catch (URISyntaxException | NullPointerException | MalformedURLException | IllegalArgumentException e) {
				return ConfigFilterResult.fail("Invalid URL: " + string + " - " + e.getLocalizedMessage());
			}
		};
	}
	
	/**
	 * Returns a filter that verifies that the input string is a valid file path, and optionally that the file meets the 
	 * conditions specified by the provided {@link FileState}, and that converts the string to a {@link Path}.
	 * @param fileState the state of the file that should be required for the filter to pass, see {@link FileState} for more info
	 * @return a filter that converts a string to a {@link Path}
	 */
	public static ConfigFilter<String, Path> validPath(FileState fileState) {
		return (String string) -> {
			try {
				Path path = Path.of(string);
				if(fileState.isExistent() != null) {
					if(fileState.isExistent() && !Files.exists(path)) {
						return ConfigFilterResult.fail("Invalid path, file or directory must exist: " + string);
					}
					if(!fileState.isExistent() && !Files.notExists(path)) {
						return ConfigFilterResult.fail("Invalid path, file or directory must not exist: " + string);
					}
				}
				if(fileState.isFile() != null) {
					if(fileState.isFile() && !Files.isRegularFile(path)) {
						return ConfigFilterResult.fail("Invalid path, must be a file (not a directory): " + string);
					}
					if(!fileState.isFile() && !Files.isDirectory(path)) {
						return ConfigFilterResult.fail("Invalid path, must be a directory (not a file): " + string);
					}
				}
				return ConfigFilterResult.pass(path);
			} catch(InvalidPathException e) {
				return ConfigFilterResult.fail("Invalid path format: " + string);
			}
		};
	}
	
	/**
	 * Represents the state of a file on the file system.
	 */
	public enum FileState {
		/**
		 * A file or directory that exists.
		 */
		EXISTENT(null, true),
		/**
		 * A file or directory that does not exist.
		 */
		NONEXISTENT(null, false),
		/**
		 * A file that exists.
		 */
		FILE(true, true),
		/**
		 * A directory that exists.
		 */
		DIRECTORY(false, true),
		/**
		 * A valid path to a file or directory that may or may not exist.
		 */
		PATH(null, null);
		
		private Boolean file;
		private Boolean existent;
		
		private FileState(Boolean file, Boolean existent) {
			this.file = file;
			this.existent = existent;
		}
		
		protected Boolean isFile() {
			return file;
		}
		
		protected Boolean isExistent() {
			return existent;
		}
	}
	
}
