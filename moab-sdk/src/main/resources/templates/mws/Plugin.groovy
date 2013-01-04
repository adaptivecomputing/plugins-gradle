<%=packageName ? 'package '+packageName : '' %>

import com.adaptc.mws.plugins.*

class <%=className%> extends AbstractPlugin {
	static constraints = {
		// You can insert constraints here on pollInterval or any arbitrary field in
		// the plugin's configuration.  All parameters defined here default to required:true.
		//confidentialParameter password:true
		//optionalBoolean required:false, blank:false, type:Boolean
	}
}