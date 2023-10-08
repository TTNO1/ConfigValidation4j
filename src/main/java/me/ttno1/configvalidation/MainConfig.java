package me.ttno1.configvalidation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class MainConfig extends BaseConfig {
	
	private static final String IP_WHITELIST = "general.ipWhitelist";

	public MainConfig(File file) throws ConfigSyntaxException, IOException {
		super(file);
	}

	public MainConfig(Path path) throws ConfigSyntaxException, IOException {
		super(path);
	}

	public MainConfig(String path) throws ConfigSyntaxException, IOException {
		super(path);
	}

	@Override
	protected boolean checkConfigSyntax() {
		return allNodeTypesExist(Map.of(IP_WHITELIST, ConfigNode.LIST));
	}

	public List<String> getIpWhitelist() {
		return yamlFile.getm.getStringList(IP_WHITELIST);
	}
	
}
