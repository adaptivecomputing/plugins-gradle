
class <%=className%>Project {
	// Plugin information
	String title = "<%=naturalName%>"
	String description = ""
	String author = "<%=author%>"
	String website = "<%=website%>"
	String email = "<%=email%>"
	
	// Versioning properties
	String version = "0.1"
	String mwsVersion = "<%=mwsVersion%> > *"
	String commonsVersion = "<%=commonsVersion%> > *"
	String license = "<%=license%>"
	
	// Documentation properties
	String issueManagementLink = ""
	String documentationLink = ""
	String scmLink = ""
	
	// The following defines initial plugins that should be created
	//	after the plugin type has been created in MWS.  This is only
	//	an initial operation and will not affect updates unless the
	//	plugin instance is deleted first.
	/*
	def initialPlugins = {
		sample-plugin {
			pluginType = "<%=className%>Plugin"
			pollInterval = 30
			autoStart = true
			config {
				option = "value"
				option2 = "value2"
			}
		}

		// plugin2Id {...
	}
	*/
}