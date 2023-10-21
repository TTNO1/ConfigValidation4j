package io.github.ttno1.configvalidation;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import io.github.ttno1.configvalidation.defaultwrappers.CommonsConfigWrapper;
import io.github.ttno1.configvalidation.defaultwrappers.SnakeYamlConfigWrapper;

public class ConfigListTest {

	private static String[] sectionListContent = {"ABC", "a b c", "DEF", "d e f", "GHI", "g h i"};
	
	private static int i, j;
	
	@BeforeEach
	void beforeEach() {
		i = -2;
		j = -1;
	}
	
	@Test
	void commonsConfigListValidationTest() {
		
		ConfigWrapper wrapper = new CommonsConfigWrapper(assertDoesNotThrow(() -> {return new Configurations().xml(ConfigNodeTest.class.getResource("/testListXmlConfig.xml"));}));
		
		ConfigValidationResult result = Cfg.newSpec()
				.addNode("doubleList", Cfg.List.ofDouble(ConfigFilter.run(list -> {
					assertEquals(list, List.of(478.88, 78.33, 1.234567890123456, 999281000.1, 51469.000));
				}), ConfigFilter.nullFilter()))
				.addNode("nestedList.string", Cfg.List.ofString(ConfigFilter.run((list) -> {
					assertEquals(list, List.of("a", "b", "c", "d", "e", "f", "g", "h", "i"));
				}), ConfigFilter.nullFilter()))
				.addNode("configSectionList.section", Cfg.List.ofConfigSpec(ConfigFilter.nullFilter(),
						Cfg.newSpec().addNode("title", Cfg.Node.ofString((s) -> {
							i += 2;
							return s.equals(sectionListContent[i]) ? ConfigFilterResult.pass(s) : ConfigFilterResult.fail(s);
						})).addNode("description", Cfg.Node.ofString((s) -> {
							j += 2;
							return s.equals(sectionListContent[j]) ? ConfigFilterResult.pass(s) : ConfigFilterResult.fail(s);
						}))))
				.validate(wrapper);

		assertTrue(result.passed(), result::getFailMessage);
		
	}
	
	@Test
	void snakeYamlListValidationTest() {
		
		InputStream yamlStream = ConfigNodeTest.class.getResourceAsStream("/testListYamlConfig.yaml");
		ConfigWrapper wrapper = new SnakeYamlConfigWrapper(new Yaml().load(yamlStream));
		assertDoesNotThrow(yamlStream::close);
		
		ConfigValidationResult result = Cfg.newSpec()
				.addNode("doubleList", Cfg.List.ofDouble(ConfigFilter.run(list -> {
					assertEquals(list, List.of(478.88, 78.33, 1.234567890123456, 999281000.1, 51469.000));
				}), ConfigFilter.nullFilter()))
				.addNode("nestedList", Cfg.List.ofList(ConfigFilter.run((list) -> {
					assertEquals(list, List.of(List.of("a", "b", "c"), List.of("d", "e", "f"), List.of("g", "h", "i")));
				}), ConfigFilter.nullFilter()))
				.addNode("configSectionList", Cfg.List.ofConfigSpec(ConfigFilter.nullFilter(),
						Cfg.newSpec().addNode("title", Cfg.Node.ofString((s) -> {
							i += 2;
							return s.equals(sectionListContent[i]) ? ConfigFilterResult.pass(s) : ConfigFilterResult.fail(s);
						})).addNode("description", Cfg.Node.ofString((s) -> {
							j += 2;
							return s.equals(sectionListContent[j]) ? ConfigFilterResult.pass(s) : ConfigFilterResult.fail(s);
						}))))
				.validate(wrapper);

		assertTrue(result.passed(), result::getFailMessage);
		
	}

}
