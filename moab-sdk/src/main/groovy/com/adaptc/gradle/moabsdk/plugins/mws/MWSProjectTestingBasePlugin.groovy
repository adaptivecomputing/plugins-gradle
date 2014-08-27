package com.adaptc.gradle.moabsdk.plugins.mws

import org.gradle.api.*
import com.adaptc.gradle.moabsdk.utils.MoabSdkUtils
import static com.adaptc.gradle.moabsdk.utils.MoabSdkConstants.*

/**
 * @author bsaville
 */
public class MWSProjectTestingBasePlugin implements Plugin<Project> {
	void apply(Project project) {
		project.dependencies.add("testCompile", project.dependencies.create(
				"com.adaptc.mws:plugins-testing:${MoabSdkUtils.getProperty(project, 'commons.version', DEFAULT_SDK_VERSION)}"
		))
		project.dependencies.add("testCompile", project.dependencies.create("xom:xom:1.2.5"))
		project.dependencies.add("testCompile",
				project.dependencies.create("org.spockframework:spock-core:0.7-groovy-2.0") { exclude module:"groovy-all" }
		)
		project.dependencies.add("testCompile", project.dependencies.create("org.hamcrest:hamcrest-core:1.2"))
		// allows mocking of classes (in addition to interfaces)
		project.dependencies.add("testRuntime", project.dependencies.create("cglib:cglib-nodep:2.2"))
		// allows mocking of classes without default constructor (together with CGLIB)
		project.dependencies.add("testRuntime", project.dependencies.create("org.objenesis:objenesis:1.2"))
	}
}
