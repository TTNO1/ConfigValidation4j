# ConfigValidation4j
A Java library for easily validating configurations before the configuration data is needed.

[Javadoc](https://todo)

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
