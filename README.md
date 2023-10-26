<p align="center">
	<img src="https://ttno1.github.io/ConfigValidation4j/logo.png" width="auto" height="150px" alt="Logo"><br><br>
	<a href="https://github.com/ttno1/configvalidation4j/releases/"><img src="https://img.shields.io/github/release/ttno1/configvalidation4j?include_prereleases=&sort=semver" alt="Github Release"></a>
	<a href="https://github.com/TTNO1/ConfigValidation4j/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-MIT-blue" alt="License"></a>
	<a href="https://central.sonatype.com/artifact/io.github.ttno1/configvalidation"><img src="https://img.shields.io/maven-central/v/io.github.ttno1/configvalidation" alt="Maven Central"></a>
	<!--<a href="https://ttno1.github.io/ConfigValidation4j/"><img src="https://img.shields.io/maven-central/v/io.github.ttno1/configvalidation?label=javadoc" alt="Javadoc"></a>-->
	<a href="https://github.com/ttno1/configvalidation4j/issues"><img src="https://img.shields.io/github/issues/ttno1/configvalidation4j" alt="Issues"></a>
	<img src="https://img.shields.io/github/actions/workflow/status/ttno1/configvalidation4j/deploy.yml" alt="Build"><br>
	<img src="https://img.shields.io/github/stars/ttno1/configvalidation4j?color=yellow" alt="Stars">
	<img src="https://img.shields.io/github/forks/ttno1/configvalidation4j?color=yellow" alt="Forks">
</p>

## Overview
A Java library for easily validating configurations before the configuration data is needed.<br><br>
Instead of getting a value from a configuration when you need it and supplying a default value if it is invalid, this library allows you to validate the entire configuration 
at initialization. It supports basic type checking for all Java primitives, lists, and configuration subsections (maps) as well as validating more advanced "filters" that can be 
written by you or selected from a provided set of common filters. Filters also allow you to convert the configuration data into a new type, such as from a String to a URI, to make 
life easier when you need to access the data later.
#### Maven Snippet
```xml
<dependency>
	<groupId>io.github.ttno1</groupId>
	<artifactId>configvalidation</artifactId>
	<version>1.0.0</version>
</dependency>
```
[Javadoc](https://ttno1.github.io/ConfigValidation4j/)
## How to use
### Main Classes
- `ConfigSpec` - Represents a specification that a config must meet in order to be valid.
- `ConfigNode` - Represents an item in a config that must be present and (optionally) must meet certain requirements.
- `ConfigList` - Similar to `ConfigNode` but for lists.
- `ConfigFilter` - A functional interface that takes in a `ConigNode` value and returns whether it is valid or not. Can optionally transform the value into a new type.
- `ConfigWrapper` - An interface that wraps a source of configuration data. This library has default implementations for Apache Commons Config and SnakeYAML.
- `Cfg` - A class with static factory methods for constructing `ConfigSpec`s, `ConfigNode`s, and `ConfigList`s.
- `ConfigFilters` - A utility class with common `ConfigFilters`.
### Examples
#### Basic Config Nodes
```java
ConfigWrapper wrapper = new SnakeYamlConfigWrapper(new Yaml().load(/*config input stream*/));
		
ConfigValidationResult result = Cfg.newSpec()
	.addNode("path.to.boolean.node", Cfg.Node.ofBoolean())
	.addNode("path.to.integer.node", Cfg.Node.ofInteger())
	.addNode("path.to.string.node", Cfg.Node.ofString())
	.addNode("path.to.list.of.strings", Cfg.List.ofString())
	.addNode("path.to.config.subsection", Cfg.newSpec().addNode(/*etc.*/))
	.validate(wrapper);

if(!result.passed()) {
	//do something when config is invalid
	System.out.println(result.getFailMessage());
}
```
#### Config Nodes with Filters
```java
ConfigWrapper wrapper = new SnakeYamlConfigWrapper(new Yaml().load(/*config input stream*/));
		
ConfigValidationResult result = Cfg.newSpec()
	.addNode("path.to.url.node", Cfg.Node.ofString(ConfigFilters.validURL()))
	.addNode("path.to.file.node", Cfg.Node.ofString(ConfigFilters.validPath(FileState.PATH)))
	.addNode("path.to.string.node", Cfg.Node.ofString((string) -> {
			if(string.contains("mySubString")) {
				return ConfigFilterResult.pass(string.toUpperCase());// ConfigFilterResult#pass() takes in the transformed value, it does not have to be the same type
			} else {
				return ConfigFilterResult.fail("My Fail Message");
			}
		}))
	.validate(wrapper);

if(!result.passed()) {
	//do something when config is invalid
	System.out.println(result.getFailMessage());
}
```
#### Getting Values from Nodes
The easiest way to save the value of a `ConfigNode` is with the `ConfigFilter#thenRun` or `ConfigFilter#run` method.
```java
ConfigWrapper wrapper = new SnakeYamlConfigWrapper(new Yaml().load(/*config input stream*/));

String myConfigStringValue = null;// value to be obtained from config (pretend this is a field in a class)
Path myConfigFileValue = null;// value to be obtained from config (pretend this is a field in a class)

ConfigValidationResult result = Cfg.newSpec()
	.addNode("path.to.file.node", Cfg.Node.ofString(ConfigFilters.validPath(FileState.PATH).thenRun((path) -> {myConfigFileValue = path;})))
	.addNode("path.to.string.node", Cfg.Node.ofString(ConfigFilter.run((string) -> {myConfigStringValue = string;})))
	.validate(wrapper);

if(!result.passed()) {
	//do something when config is invalid
	System.out.println(result.getFailMessage());
}
```
### Dependencies
In order to use this library with SnakeYAML, Apache Commons Configuration, or Apache Commons Validator (for URL validation), you must include those dependencies separately.
For your convenience, here are the maven snippets for those dependencies.
```xml
<!--SnakeYAML-->
<dependency>
	<groupId>org.yaml</groupId>
	<artifactId>snakeyaml</artifactId>
	<version>2.2</version>
</dependency>
<!--Commons Configuration-->
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-configuration2</artifactId>
	<version>2.9.0</version>
</dependency>
<!--Commons Validator-->
<dependency>
	<groupId>commons-validator</groupId>
	<artifactId>commons-validator</artifactId>
	<version>1.7</version>
</dependency>
```
### Limitations
#### Underlying Limitations
This library serves only as a validation layer on top of whatever means you use to get your configuration data.
If you choose to use a library like Apache Commons Config or SnakeYAML, then the limitations of that library will still apply.

For example, by default SnakeYAML treats all decimals as `double`s, so if you attempt to validate a `ConfigNode` of type `float`, it will not work.
#### Nested Lists
When validating a list of lists (assuming the underlying config library supports nested lists), the inner list cannot have a specific type and will always be a list of objects. This means that for `ConfigFilter` purposes, the filter will accept a type of `List<List<Object>>`.
## Contributing
Feel free to contribute in any way you please. It is much appreciated.
## Questions
Feel free to ask in an issue or by contacting me.
