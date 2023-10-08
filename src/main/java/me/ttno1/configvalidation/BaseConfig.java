package me.ttno1.configvalidation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.validator.routines.UrlValidator;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

public abstract class BaseConfig {

	protected YamlFile yamlFile;

	public BaseConfig(File file) throws ConfigSyntaxException, IOException {
		init(file);
	}
	
	public BaseConfig(Path path) throws ConfigSyntaxException, IOException {
		if(path == null) {
			throw new FileNotFoundException("Config file path cannot be null.");
		}
		try {
			init(path.toFile());
		} catch (UnsupportedOperationException e) {
			throw new IOException(e);
		}
	}
	
	public BaseConfig(String path) throws ConfigSyntaxException, IOException {
		if(path == null) {
			throw new FileNotFoundException("Config file path cannot be null.");
		}
		init(new File(path));
	}
	
	private void init(File file) throws IOException, ConfigSyntaxException {
		if(file == null || !file.exists() || file.isDirectory()) {
			throw new FileNotFoundException("Config file cannot be null, nonexistant, or a directory: " + file);
		}
		yamlFile = new YamlFile(file);
		try {
			yamlFile.load();
		} catch (InvalidConfigurationException e) {
			throw new ConfigSyntaxException(e);
		}
		if(!checkConfigSyntax()) {
			throw new ConfigSyntaxException("Invalid config syntax.");
		}
	}

	protected abstract boolean checkConfigSyntax();
	
	protected boolean allNodeTypesExist(Map<String, ConfigNode> map) {
		
		for(Entry<String, ConfigNode> entry : map.entrySet()) {
			switch (entry.getValue()) {
			case BOOLEAN:
				if(!yamlFile.isBoolean(entry.getKey())) {
					return false;
				}
				break;
			case DICTIONARY:
				if(!yamlFile.isConfigurationSection(entry.getKey())) {
					return false;
				}
				break;
			case DOUBLE:
				if(!yamlFile.isDouble(entry.getKey())) {
					return false;
				}
				break;
			case INT:
				if(!yamlFile.isInteger(entry.getKey())) {
					return false;
				}
				break;
			case LIST:
				if(!yamlFile.isList(entry.getKey())) {
					return false;
				}
				break;
			case STRING:
				if(!yamlFile.isString(entry.getKey())) {
					return false;
				}
				break;
			/*default:
				throw new IllegalStateException("Unsupported YamlType: " + entry.getValue());*/
			}
		}
		
		return true;
	}
	
	protected <T extends Enum<T>> boolean nodeValidEnum(String node, Class<T> enumClass) {
		
		try {
			if(!yamlFile.isString(node)) {
				return false;
			}
			Enum.valueOf(enumClass, yamlFile.getString(node));
		} catch(IllegalArgumentException | NullPointerException e) {
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * 
	 * @param nodes an array of nodes that should be valid URLs
	 * @return true if all nodes in {@code nodes} are valid URLs, false otherwise
	 */
	protected boolean nodesValidUrl(String ... nodes) {
		UrlValidator validator = new UrlValidator(new String[] {"http", "https"});
		for(String node : nodes) {
			if(!yamlFile.isString(node) || !validator.isValid(yamlFile.getString(node))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param nodesMap a map in which the keys are nodes that should point to file paths and the values are the {@link FileState} corresponding to the file state that should be required of the path specified in the key
	 * @return false if any paths are invalid or do not match their corresponding {@link FileState}, true otherwise
	 */
	protected boolean nodesValidPaths(Map<String, FileState> nodesMap) {
		for(Entry<String, FileState> entry : nodesMap.entrySet()) {
			if(!yamlFile.isString(entry.getKey())) {
				return false;
			}
			try {
				Path path = Path.of(yamlFile.getString(entry.getKey()));
				FileState state = entry.getValue();
				if((state.isExistent() != null) && ((state.isExistent() && !Files.exists(path)) || (!state.isExistent() && !Files.notExists(path)))) {
					return false;
				}
				if((state.isFile() != null) && ((state.isFile() && !Files.isRegularFile(path)) || (!state.isFile() && !Files.isDirectory(path)))) {
					return false;
				}
			} catch(InvalidPathException e) {
				return false;
			}
		}
		return true;
	}
	
	protected enum FileState {
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
