This is a [Gradle|http://gradle.org] plugin for creating and maintaining Moab plugin projects.  Currently this only includes creating
MWS plugin projects and artifacts, but may include more in the future.

# Introduction to the Moab SDK

The Moab SDK (which the moab-sdk Gradle plugin enables) is utilized internally by
[Adaptive Computing|http://adaptivecomputing.com] and in the open source plugin project
[plugins-mws|http://github.com/adaptivecomputing/plugins-mws].  This may be referred to as an example of how to
organize and build plugins for Moab Web Services.

* Gradle is a build system based on Groovy, just as Grails is a web framework based on Spring and Groovy
* Build properties and tasks are controlled through the build.gradle (and included plugins) and gradle.properties file in the top-level directory (root project), and are overridden by the build.gradle and gradle.properties in each sub-project's directory (ie native). The gradle.properties may also be overridden globally by placing the same file in ~/.gradle/.
* The gradlew wrapper script automatically downloads and uses the latest gradle version available - **you do not need to install gradle to develop MWS plugins if you are using a repository that is already configured**.
* The SDK acts on plugin projects, which are actually gradle subprojects of a root gradle build.  Each project or module is represented by a sub-directory, such as native, reports, etc.
* Tasks may be run as follows (examples given for the native project located in the plugins-mws project described below):
```
./gradlew native:build # compiles, tests, and creates a JAR in native/build/libs for the native module (plugin project)
./gradlew native:jar # compiles and creates a JAR (without testing) in native/build/libs for the native module (plugin project)
./gradlew jar # compiles and creates a JAR (without testing) in each project's build directory
cd native && ../gradlew jar # compiles and creates a JAR (without testing) for the native project only - the gradle command is aware of which directory you are in
```

# Using the Plugins

## Initialization

In order to utilize the Moab SDK, this Gradle plugin (moab-sdk) must be included using the `buildscript`
definition in the build file. The plugin is published to both Maven Central (for released versions) and the OSS Sonatype
repository (for snapshot versions).  Place the following lines at the beginning of your `build.gradle` file:

```groovy
buildscript {
	repositories {
		mavenCentral()
		maven {
			url "https://oss.sonatype.org/content/groups/public"
		}
	}
	dependencies {
		classpath ('com.adaptc.gradle:moab-sdk:0.9-SNAPSHOT')
	}
}

apply plugin: com.adaptc.gradle.moabsdk.plugins.MoabSdkInitPlugin
```

This code adds the necessary repositories and includes the moab-sdk JAR into your build script.  The plugin(s) may then
be applied as shown.  Often this code is all that is needed to create a multi-project build using the Moab SDK.

## Available Plugins

The table below gives a reference of all plugins available in the moab-sdk JAR.  The "Applies To" column tells whether
the plugin is meant to be applied to the root project in a multi-project gradle build or to the child projects.  The
plugins listed as "Subproject" may be used in a single project gradle build, while "Root project" must be used in multi-project
builds on the root project.

Class | Applies To | Description
----- | ---------- | -----------
[MoabSdkPlugin](src/main/groovy/com/adaptc/gradle/moabsdk/plugins/MoabSdkPlugin.groovy) | Root project | Adds functionality for creating MWS subprojects
[MoabSdkInitPlugin](src/main/groovy/com/adaptc/gradle/moabsdk/plugins/MoabSdkInitPlugin.groovy) | Root project | Adds common conventions, including repositories, and applies the MoabSdkPlugin
[MWSProjectBasePlugin](src/main/groovy/com/adaptc/gradle/moabsdk/plugins/mws/MWSProjectBasePlugin.groovy) | Subproject | Configures the project as a java/groovy project, adds tasks for creating MWS plugin project artifacts and adds MWS plugin project dependencies
[MWSProjectTestingBasePlugin](src/main/groovy/com/adaptc/gradle/moabsdk/plugins/mws/MWSProjectTestingBasePlugin.groovy) | Subproject | Adds the dependencies necessary for testing MWS plugin projects
[MWSProjectTestingPlugin](src/main/groovy/com/adaptc/gradle/moabsdk/plugins/mws/MWSProjectTestingPlugin.groovy) | Subproject | Adds some common conventions for MWS plugin project testing and applies the MWSProjectTestingBasePlugin
[MWSProjectPlugin](src/main/groovy/com/adaptc/gradle/moabsdk/plugins/mws/MWSProjectPlugin.groovy) | Subproject | Adds some common conventions for MWS plugin projects and applies the MWSProjectBasePlugin and MWSProjectTestingPlugin
[MWSProjectInitPlugin](src/main/groovy/com/adaptc/gradle/moabsdk/plugins/mws/MWSProjectInitPlugin.groovy) | Subproject | Adds common conventions for MWS plugin projects, including repositories, and applies the fat-jar, lib-dir, and MWSProjectPlugin plugins

# MWS Plugin Projects

## Tasks

The table below is a reference of all tasks available when using the MWS project Gradle plugin.  Again, the "Applies To"
column tells whether the task is meant to be run on the root project or the subproject in a multi-project configuration.

Task Name | Applies To | Description
--------- | ---------- | ---------- | ------- | -----------
createMwsProject | Root project | Creates a directory called `PROJECTNAME` and creates some files in the directory to help understand some basic features of the SDK. For project names containing multiple words, use hypens for seperation (i.e. `./gradlew createMwsProject -Pargs=cool-project-name`).
createPlugin | Subproject | Creates the directory structure for `PLUGINNAME` (including sub-folders for a full package reference such as com.ace.mws.plugins.ExamplePlugin) in src/main/groovy and creates a simple, valid plugin type.
createTranslator | Subproject | Creates the directory structure and file for the `TRANSLATORNAME`, just as it does for "createPlugin".
createComponent | Subproject | Creates the directory structure and file for the `COMPONENTNAME`, just as it does for "createPlugin". The annotations are also added to the class (and comments to automatically inject other components).
upload | Subproject | Uploads the specified plugin project(s) as a JAR file (with POST /rest/plugin-types) to the MWS instance pointed to by the settings in the `gradle.properties` file. See the `gradle.properties` bullet point below for more details.
generateTestInstances | Subproject | Creates plugin instances (with POST /rest/plugins) for each instance defined in the project's `instances.groovy` file. See the `instances.groovy` bullet point below for more details.
test | Subproject | Runs the Spock and JUnit tests for the project located in src/test/groovy and src/test/java. See "Testing" below for more details.

Examples:
```
./gradlew createMwsProject -Pargs=<PROJECTNAME>
./gradlew createPlugin -Pargs=<PLUGINNAME>
./gradlew createTranslator -Pargs=<TRANSLATORNAME>
./gradlew createComponent -Pargs=<COMPONENTNAME>
./gradlew upload
./gradlew generateTestInstances
./gradlew test
```

## Configuration Files

There are several files to be aware of when working with the SDK. Note that most of these files are contained in the
plugin project's sub-directory. The files at the root of the SDK should often not be modified but should be controlled
by each user in their respective `~/.gradle/` directory.

* `build.gradle` - This file can be used to declare additional dependencies beyond plugins-commons and the others needed.  Custom tasks may also be defined here, although that should not be necessary.  The syntax for additional dependencies is documented in the example file created when using `createMwsProject`.
* `instances.groovy` - This file can be used to declare test instances to be used in `generateTestInstances` as shown above.  The syntax is documented in the example created when using `createMwsProject`.
* `PROJECTNAMEProject.groovy` - This file is named based on the project name (with an upper-case first letter) and the format is documented in the MWS plugin's documentation under Plugin Project and Metadata.  Default values are set for most properties, and an commented example is given of creating initial plugins.
* `gradle.properties` - This file can be used to declare properties for the specific plugin, such as another version of the commons dependency (`version.sdk`) or the MWS URL and username/password properties, although these are more likely to go into the `~/.gradle/gradle.properties` file.
* `~/.gradle/gradle.properties` - You can use this file to override any settings globally for your machine

> While the `instances.groovy` file and the `initialPlugins` closure in the project file may seem to fulfill the same
> purpose, they are distinct. The `instances.groovy` file is used only when utilizing the SDK with the
> `generateTestInstances` task to create instances that may be used in testing with specific configuration. The
> initial plugins configuration, however, is used to create plugin instances that are meant to be always present in
> production use cases. See the
> [native plugin project file |https://github.com/adaptivecomputing/plugins-mws/blob/master/native/NativeProject.groovy]
> for an example of how this works.

## Testing

The MWS project Gradle plugin automatically creates a Spock test file for every plugin created with `createPlugin`,
every translator created with `createTranslator`, and every component created with `createComponent`. Just as the
Grails test annotation [TestFor|http://grails.org/doc/2.1.0/guide/testing.html], there is a
`com.ace.mws.plugins.testing.TestFor` annotation that may be used on plugins and translators to mimic the log and
other properties that are typically injected on plugins or other classes. The test files should be located in
`PROJECTNAME/src/test/groovy`.

When the `TestFor` annotation is used, a property will be available on the test class
consisting of a new instance of the class under test.  For plugins, this property is named `plugin`, for translators
it is named `translator`, and for components it is named `component`.  In the case of plugins and translators, a log
property is also automatically available to the class under test, while this must be added manually (with a static
field as given in the example below) to components.

Additionally, when using the `TestFor` annotation on plugin classes, there are three properties injected into the test:
`config`, `appConfig`, and `constraints`.  The first two represent the same properties that are available on the
plugin class itself.  The last is a map of maps that can be used to retrieve all constraints for a specific field name.
For validator constraints, the `getService` method is available for use, and retrieves services directly from the plugin
instance.  If the service does not exist on the plugin type definition, an error will be thrown.  For example, if a
custom validator is used and the validator is wished to be tested:

```groovy
package test
import com.ace.mws.plugins.*
class MyPlugin extends AbstractPlugin {
  ISslService sslService
  static constraints = {
    sum type:Integer, validator:{val, obj ->
      return val==(obj.config.a + obj.config.b)
    }
    sslServiceExists type:Boolean, validator:{val, obj ->
      return val==(getService("sslService")!=null)
  }
}
```

Then these constraints may be tested as following:

```groovy
package test
import com.ace.mws.plugins.*
import com.ace.mws.plugins.testing.*
import spock.lang.*

@TestFor(MyPlugin)
class PluginSpec extends Specification {
  def "Sum constraints"() {
    expect:
    constraints.sum.type==Integer.class
    constraints.sum.validator
    constraints.sum.validator(2, [config:[a:1,b:1]])
    !constraints.sum.validator(3, [config:[a:2,b:2]])
  }
  def "sslService Constraints"() {
    when:
    plugin.sslService = service

    then:
    constraints.sslServiceExists.validator
    constraints.sslServiceExists.validator(true, plugin)

    when:
    plugin.sslService = null

    then:
    constraints.sslServiceExists.validator(false, plugin)
}
```
