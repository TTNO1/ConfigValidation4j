package me.ttno1.configvalidation;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

public final class ConfigFilters {

	private static String[] DEFAULT_SCHEMES = new String[] {"http", "https"};
	
	private ConfigFilters() {}
	//TODO cache results of all methods where possible
	/**
	 * Returns a filter that fails if any element in the list fails
	 * @param <T>
	 * @param <U>
	 * @param filter
	 * @return
	 */
	public static <T, U> ConfigFilter<List<T>, List<U>> forEach(ConfigFilter<T, U> filter) {
		return (List<T> input) -> {
			List<U> result = new ArrayList<U>();
			for(T in : input) {
				ConfigFilterResult<U> filterResult = filter.filter(in);
				if(!filterResult.passed()) {
					return ConfigFilterResult.fail(filterResult.getFailMessage());
				}
				result.add(filterResult.getResult());
			}
			return ConfigFilterResult.pass(result);
		};
	}
	
	public static <T extends Enum<T>> ConfigFilter<String, T> validEnum(Class<T> enumClass) {
		return (String string) -> {
			try {
				return ConfigFilterResult.pass(Enum.valueOf(enumClass, string));
			} catch(IllegalArgumentException | NullPointerException e) {
				return ConfigFilterResult.fail();
			}
		};
	}
	
	public static ConfigFilter<String, URL> validURL(String ... schemes) {
		if(schemes == null || schemes.length == 0) {
			schemes = DEFAULT_SCHEMES;
		}
		UrlValidator validator = new UrlValidator(schemes);
		return (String string) -> {
			if(!validator.isValid(string)) {
				return ConfigFilterResult.fail();
			}
			try {
				return ConfigFilterResult.pass(new URL(string));
			} catch (MalformedURLException e) {
				return ConfigFilterResult.fail();
			}
		};
	}
	
	public static ConfigFilter<String, Path> validPath(FileState fileState) {
		return (String string) -> {
			try {
				Path path = Path.of(string);
				if((fileState.isExistent() != null) && ((fileState.isExistent() && !Files.exists(path)) || (!fileState.isExistent() && !Files.notExists(path)))) {
					return ConfigFilterResult.fail();
				}
				if((fileState.isFile() != null) && ((fileState.isFile() && !Files.isRegularFile(path)) || (!fileState.isFile() && !Files.isDirectory(path)))) {
					return ConfigFilterResult.fail();
				}
				return ConfigFilterResult.pass(path);
			} catch(InvalidPathException e) {
				return ConfigFilterResult.fail();
			}
		};
	}
	
	public enum FileState {
		FILE(true, null),
		DIRECTORY(false, null),
		EXISTENT_FILE(true, true),
		EXISTENT_DIRECTORY(false, true),
		NONEXISTENT_FILE(true, false),
		NONEXISTENT_DIRECTORY(false, false),
		PATH(null, null),
		EXISTENT_PATH(null, true),
		NONEXISTENT_PATH(null, false);
		
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
