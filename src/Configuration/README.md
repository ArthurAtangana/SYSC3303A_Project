# README
* Author: Michael De Santis
* CUID: 101213450
* Date: 2024/02/13

## Description
Configuration classes for use in configuring the System at Runtime.

## Procedure: Configuring the System
To use the classes here to provide configuration in code, use the following procedure:
1. Create a valid JSON file in the project's `res/` directory adherent to the format provided in `res/system-config-00.json`, the default configuration file, manipulating values as needed.
2. Pass the file to any classes that require global configuration values. The following code may be used in each class.
```java
    String jsonFilename = "res/<json-config-file>";
    Config config = (new Configurator(jsonFilename).getConfig());
```
3. Extract relevant data using a getter for that field.
```java
    <type> <field> = config.get<Field>();
```
For example, to extract the number of floors that the system will simulate:
```java
    int numFloors = config.getNumFloors();
```
In the future, when Subsystems are run as separate programs on dedicated machines, identical config files should be supplied to each Subsystem, and each Subsystem can extract relevant data as required.



