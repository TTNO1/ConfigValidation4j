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
		
		var result = Cfg.newSpec()
				.addNode("topBoolean", Cfg.Node.ofBoolean(ConfigFilter.run((r) -> assertEquals(r, true))))
				.addNode("topByte", Cfg.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, 127))))
				.addNode("topDouble",
						Cfg.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567890123456D))))
				.addNode("topFloat", Cfg.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567D))))
				.addNode("topInteger",
						Cfg.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, Integer.MAX_VALUE))))
				.addNode("topLong", Cfg.Node.ofLong(ConfigFilter.run((r) -> assertEquals(r, Long.MAX_VALUE))))
				.addNode("topShort", Cfg.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, (int)Short.MAX_VALUE))))
				.addNode("topString",
						Cfg.Node.ofString(
								ConfigFilter.run((r) -> assertEquals(r, "Test Config String value 1234 false"))))
				.addNode("topPath",
						Cfg.Node.ofString(ConfigFilters.validPath(FileState.PATH)
								.thenRun((r) -> assertEquals(r, Path.of("C:\\test\\path\\dir\\file.txt")))))
				.addNode("topUrl", Cfg.Node.ofString(ConfigFilters.validURL().thenRun(r -> {
					URL url = assertDoesNotThrow(() -> {
						return new URI("https://www.example.com").toURL();
					});
					assertEquals(r, url);
				}))).addNode("topEnum", Cfg.Node.ofString(ConfigFilters.validEnum(TestEnum.class)))
				.addNode("node", Cfg.newSpec()
						.addNode("subBoolean", Cfg.Node.ofBoolean(ConfigFilter.run((r) -> assertEquals(r, true))))
						.addNode("subByte", Cfg.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, 127))))
						.addNode("subDouble",
								Cfg.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567890123456D))))
						.addNode("subFloat", Cfg.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567D))))
						.addNode("subInteger",
								Cfg.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, Integer.MAX_VALUE))))
						.addNode("subLong",
								Cfg.Node.ofLong(ConfigFilter.run((r) -> assertEquals(r, Long.MAX_VALUE))))
						.addNode("subShort",
								Cfg.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, (int)Short.MAX_VALUE))))
						.addNode("subString",
								Cfg.Node.ofString(ConfigFilter
										.run((r) -> assertEquals(r, "Test Config String value 1234 false"))))
						.addNode("subPath",
								Cfg.Node.ofString(ConfigFilters.validPath(FileState.PATH)
										.thenRun((r) -> assertEquals(r, Path.of("C:\\test\\path\\dir\\file.txt")))))
						.addNode("subUrl", Cfg.Node.ofString(ConfigFilters.validURL().thenRun(r -> {
							URL url = assertDoesNotThrow(() -> {
								return new URI("https://www.example.com").toURL();
							});
							assertEquals(r, url);
						}))).addNode("subEnum", Cfg.Node.ofString(ConfigFilters.validEnum(TestEnum.class))))
				.validate(wrapper);
		
		assertTrue(result.passed(), result::getFailMessage);

	}

	@Test
	void commonsConfigNodeValidationTest() {
		
		ConfigWrapper wrapper = new CommonsConfigWrapper(assertDoesNotThrow(() -> {return new Configurations().xml(ConfigNodeTest.class.getResource("/testXmlConfig.xml"));}));
		
		var result = Cfg.newSpec()
				.addNode("topBoolean", Cfg.Node.ofBoolean(ConfigFilter.run((r) -> assertEquals(r, true))))
				.addNode("topByte", Cfg.Node.ofByte(ConfigFilter.run((r) -> assertEquals(r, (byte)127))))
				.addNode("topDouble",
						Cfg.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567890123456D))))
				.addNode("topFloat", Cfg.Node.ofFloat(ConfigFilter.run((r) -> assertEquals(r, 1.234567F))))
				.addNode("topInteger",
						Cfg.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, Integer.MAX_VALUE))))
				.addNode("topLong", Cfg.Node.ofLong(ConfigFilter.run((r) -> assertEquals(r, Long.MAX_VALUE))))
				.addNode("topShort", Cfg.Node.ofShort(ConfigFilter.run((r) -> assertEquals(r, Short.MAX_VALUE))))
				.addNode("topString",
						Cfg.Node.ofString(
								ConfigFilter.run((r) -> assertEquals(r, "Test Config String value 1234 false"))))
				.addNode("topPath",
						Cfg.Node.ofString(ConfigFilters.validPath(FileState.PATH)
								.thenRun((r) -> assertEquals(r, Path.of("C:\\test\\path\\dir\\file.txt")))))
				.addNode("topUrl", Cfg.Node.ofString(ConfigFilters.validURL().thenRun(r -> {
					URL url = assertDoesNotThrow(() -> {
						return new URI("https://www.example.com").toURL();
					});
					assertEquals(r, url);
				}))).addNode("topEnum", Cfg.Node.ofString(ConfigFilters.validEnum(TestEnum.class)))
				.addNode("node", Cfg.newSpec()
						.addNode("subBoolean", Cfg.Node.ofBoolean(ConfigFilter.run((r) -> assertEquals(r, true))))
						.addNode("subByte", Cfg.Node.ofByte(ConfigFilter.run((r) -> assertEquals(r, (byte)127))))
						.addNode("subDouble",
								Cfg.Node.ofDouble(ConfigFilter.run((r) -> assertEquals(r, 1.234567890123456D))))
						.addNode("subFloat", Cfg.Node.ofFloat(ConfigFilter.run((r) -> assertEquals(r, 1.234567F))))
						.addNode("subInteger",
								Cfg.Node.ofInteger(ConfigFilter.run((r) -> assertEquals(r, Integer.MAX_VALUE))))
						.addNode("subLong",
								Cfg.Node.ofLong(ConfigFilter.run((r) -> assertEquals(r, Long.MAX_VALUE))))
						.addNode("subShort",
								Cfg.Node.ofShort(ConfigFilter.run((r) -> assertEquals(r, Short.MAX_VALUE))))
						.addNode("subString",
								Cfg.Node.ofString(ConfigFilter
										.run((r) -> assertEquals(r, "Test Config String value 1234 false"))))
						.addNode("subPath",
								Cfg.Node.ofString(ConfigFilters.validPath(FileState.PATH)
										.thenRun((r) -> assertEquals(r, Path.of("C:\\test\\path\\dir\\file.txt")))))
						.addNode("subUrl", Cfg.Node.ofString(ConfigFilters.validURL().thenRun(r -> {
							URL url = assertDoesNotThrow(() -> {
								return new URI("https://www.example.com").toURL();
							});
							assertEquals(r, url);
						}))).addNode("subEnum", Cfg.Node.ofString(ConfigFilters.validEnum(TestEnum.class))))
				.validate(wrapper);
		
		assertTrue(result.passed(), result::getFailMessage);
		
	}

}
