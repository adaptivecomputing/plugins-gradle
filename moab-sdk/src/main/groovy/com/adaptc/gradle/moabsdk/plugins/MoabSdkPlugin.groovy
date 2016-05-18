package com.adaptc.gradle.moabsdk.plugins

import com.adaptc.gradle.moabsdk.plugins.mws.MWSProjectInitPlugin
import com.adaptc.gradle.moabsdk.tasks.GenerateMWSProjectTask
import com.adaptc.gradle.moabsdk.utils.MoabSdkUtils
import org.gradle.api.Plugin
import org.gradle.api.Project

import static com.adaptc.gradle.moabsdk.utils.MoabSdkConstants.MOAB_SDK_TASK_GROUP
import static com.adaptc.gradle.moabsdk.utils.MoabSdkConstants.STANDALONE_PROJECT_PROPERTY

/**
 * @author bsaville
 */
public class MoabSdkPlugin implements Plugin<Project> {
	void apply(Project project) {
		def createProject = project.tasks.create("createMwsProject", GenerateMWSProjectTask)
		createProject.setGroup(MOAB_SDK_TASK_GROUP)
		createProject.setDescription("Creates a MWS plugin project")

		if (!MoabSdkUtils.getProperty(project, STANDALONE_PROJECT_PROPERTY, "true").toBoolean()) {
			project.subprojects.each {
				it.plugins.apply(MWSProjectInitPlugin)
			}
		}
	}
}