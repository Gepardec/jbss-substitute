Subsitute tool for Freemarker templates
======================================

The usage of templates for configuration steps may ease
a lot of build and deployment authomatization tasks.
Substitute is a tool for processing Freemarker templates, that takes a template and an environment configuration
as an input and returns the processed template.

Prerequisites
-------------
* Java JDK
* Maven
* Unix-style OS. Tested on Linux (Fedora, RHEL)

Quickstart
----------

1. Clone the Git-repository: `git clone https://github.com/Gepardec/jbss-substitute.git`
2. Build project with maven (mvn clean install)
3. Place the jar target/jbss-*-jar-with-dependencies.jar in the same folder with substitute.sh from src/main/resources
4. Rename jbss-*-jar-with-dependencies.jar to substitute
5. Copy test.env and template.ftl from src/test/resources to the folder with scripts

Run the following commands:

	./substitute.sh -p test.env -t template.ftl -o result.properties

How it works
------------

The environment configuration is the set of key-value pairs in format KEY=VALUE.
Every entry is available in the template while processing and can be retrieved via ${KEY} according the Freemarket format.
The bunch of entries with the same basis name and numerical endings in the format
MAP_0=...
MAP_1=...
...
MAP_N=...
is evaluated to the map MAP['0']=..., MAP['1']=... etc.
The map entries are available in template via ${MAP['0']} and can be also iterated:
<#list MAP?keys as key>
${MAP[key]}
</#list>

