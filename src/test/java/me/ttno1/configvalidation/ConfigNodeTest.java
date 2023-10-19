package me.ttno1.configvalidation;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import me.ttno1.configvalidation.ConfigFilters.FileState;
import me.ttno1.configvalidation.defaultwrappers.CommonsConfigWrapper;
import me.ttno1.configvalidation.defaultwrappers.SnakeYamlConfigWrapper;

public class ConfigNodeTest {

	@Test
	void snakeYamlNodeValidationTest() {

		InputStream yamlStream = ConfigNodeTest.class.getResourceAsStream("/testYamlConfig.yaml");
		ConfigWrapper wrapper = new SnakeYamlConfigWrapper(new Yaml().load(yamlStream));
		assertDoesNotThrow(yamlStream::close);
		
		var result = Config.newSpec()
				.addNode("topBoolean", Config.Node.ofBoolean(ConfigFilter.run((r) -> assertEquals(r, true))))
				.addNode("topByte", Config.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, 127))))
				.addNode("topDouble",
						Config.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567890123456D))))
				.addNode("topFloat", Config.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567D))))
				.addNode("topInteger",
						Config.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, Integer.MAX_VALUE))))
				.addNode("topLong", Config.Node.ofLong(ConfigFilter.run((r) -> assertEquals(r, Long.MAX_VALUE))))
				.addNode("topShort", Config.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, (int)Short.MAX_VALUE))))
				.addNode("topString",
						Config.Node.ofString(
								ConfigFilter.run((r) -> assertEquals(r, "Test Config String value 1234 false"))))
				.addNode("topPath",
						Config.Node.ofString(ConfigFilters.validPath(FileState.PATH)
								.thenRun((r) -> assertEquals(r, Path.of("C:\\test\\path\\dir\\file.txt")))))
				.addNode("topUrl", Config.Node.ofString(ConfigFilters.validURL().thenRun(r -> {
					URL url = assertDoesNotThrow(() -> {
						return new URI("https://www.example.com").toURL();
					});
					assertEquals(r, url);
				}))).addNode("topEnum", Config.Node.ofString(ConfigFilters.validEnum(TestEnum.class)))
				.addNode("node", Config.newSpec()
						.addNode("subBoolean", Config.Node.ofBoolean(ConfigFilter.run((r) -> assertEquals(r, true))))
						.addNode("subByte", Config.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, 127))))
						.addNode("subDouble",
								Config.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567890123456D))))
						.addNode("subFloat", Config.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567D))))
						.addNode("subInteger",
								Config.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, Integer.MAX_VALUE))))
						.addNode("subLong",
								Config.Node.ofLong(ConfigFilter.run((r) -> assertEquals(r, Long.MAX_VALUE))))
						.addNode("subShort",
								Config.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, (int)Short.MAX_VALUE))))
						.addNode("subString",
								Config.Node.ofString(ConfigFilter
										.run((r) -> assertEquals(r, "Test Config String value 1234 false"))))
						.addNode("subPath",
								Config.Node.ofString(ConfigFilters.validPath(FileState.PATH)
										.thenRun((r) -> assertEquals(r, Path.of("C:\\test\\path\\dir\\file.txt")))))
						.addNode("subUrl", Config.Node.ofString(ConfigFilters.validURL().thenRun(r -> {
							URL url = assertDoesNotThrow(() -> {
								return new URI("https://www.example.com").toURL();
							});
							assertEquals(r, url);
						}))).addNode("subEnum", Config.Node.ofString(ConfigFilters.validEnum(TestEnum.class))))
				.validate(wrapper);
		
		assertTrue(result.passed(), result::getFailMessage);

	}

	@Test
	void commonsConfigNodeValidationTest() {
		
		ConfigWrapper wrapper = new CommonsConfigWrapper(assertDoesNotThrow(() -> {return new Configurations().xml(ConfigNodeTest.class.getResource("/testXmlConfig.xml"));}));
		
		var result = Config.newSpec()
				.addNode("topBoolean", Config.Node.ofBoolean(ConfigFilter.run((r) -> assertEquals(r, true))))
				.addNode("topByte", Config.Node.ofByte(ConfigFilter.run((r) -> assertEquals(r, (byte)127))))
				.addNode("topDouble",
						Config.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567890123456D))))
				.addNode("topFloat", Config.Node.ofFloat(ConfigFilter.run((r) -> assertEquals(r, 1.234567F))))
				.addNode("topInteger",
						Config.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, Integer.MAX_VALUE))))
				.addNode("topLong", Config.Node.ofLong(ConfigFilter.run((r) -> assertEquals(r, Long.MAX_VALUE))))
				.addNode("topShort", Config.Node.ofShort(ConfigFilter.run((r) -> assertEquals(r, Short.MAX_VALUE))))
				.addNode("topString",
						Config.Node.ofString(
								ConfigFilter.run((r) -> assertEquals(r, "Test Config String value 1234 false"))))
				.addNode("topPath",
						Config.Node.ofString(ConfigFilters.validPath(FileState.PATH)
								.thenRun((r) -> assertEquals(r, Path.of("C:\\test\\path\\dir\\file.txt")))))
				.addNode("topUrl", Config.Node.ofString(ConfigFilters.validURL().thenRun(r -> {
					URL url = assertDoesNotThrow(() -> {
						return new URI("https://www.example.com").toURL();
					});
					assertEquals(r, url);
				}))).addNode("topEnum", Config.Node.ofString(ConfigFilters.validEnum(TestEnum.class)))
				.addNode("node", Config.newSpec()
						.addNode("subBoolean", Config.Node.ofBoolean(ConfigFilter.run((r) -> assertEquals(r, true))))
						.addNode("subByte", Config.Node.ofByte(ConfigFilter.run((r) -> assertEquals(r, (byte)127))))
						.addNode("subDouble",
								Config.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567890123456D))))
						.addNode("subFloat", Config.Node.ofFloat(ConfigFilter.run((r) -> assertEquals(r, 1.234567F))))
						.addNode("subInteger",
								Config.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, Integer.MAX_VALUE))))
						.addNode("subLong",
								Config.Node.ofLong(ConfigFilter.run((r) -> assertEquals(r, Long.MAX_VALUE))))
						.addNode("subShort",
								Config.Node.ofShort(ConfigFilter.run((r) -> assertEquals(r, Short.MAX_VALUE))))
						.addNode("subString",
								Config.Node.ofString(ConfigFilter
										.run((r) -> assertEquals(r, "Test Config String value 1234 false"))))
						.addNode("subPath",
								Config.Node.ofString(ConfigFilters.validPath(FileState.PATH)
										.thenRun((r) -> assertEquals(r, Path.of("C:\\test\\path\\dir\\file.txt")))))
						.addNode("subUrl", Config.Node.ofString(ConfigFilters.validURL().thenRun(r -> {
							URL url = assertDoesNotThrow(() -> {
								return new URI("https://www.example.com").toURL();
							});
							assertEquals(r, url);
						}))).addNode("subEnum", Config.Node.ofString(ConfigFilters.validEnum(TestEnum.class))))
				.validate(wrapper);
		
		assertTrue(result.passed(), result::getFailMessage);
		
	}

}
