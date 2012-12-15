package com.adaptc.gradle.moabsdk.plugins

import org.gradle.api.*
import static com.adaptc.gradle.moabsdk.utils.MoabSdkConstants.*
import com.adaptc.gradle.moabsdk.tasks.GenerateMWSProjectTask
import com.adaptc.gradle.moabsdk.utils.MoabSdkUtils
import com.adaptc.gradle.moabsdk.plugins.mws.MWSProjectInitPlugin

/**
 * @author bsaville
 */
public class MoabSdkPlugin implements Plugin<Project> {
	void apply(Project project) {
		def createProject = project.tasks.add("createMwsProject", GenerateMWSProjectTask)
		createProject.setGroup(MOAB_SDK_TASK_GROUP)
		createProject.setDescription("Creates a MWS plugin project")

		if (!MoabSdkUtils.getProperty(project, STANDALONE_PROJECT_PROPERTY, "true").toBoolean()) {
			project.subprojects.each {
				it.plugins.apply(MWSProjectInitPlugin)
			}
		}
	}
}