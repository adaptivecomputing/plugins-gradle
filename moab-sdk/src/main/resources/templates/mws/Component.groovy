<%=packageName ? 'package '+packageName : '' %>

import org.springframework.stereotype.Component
import org.apache.commons.logging.LogFactory
import org.apache.commons.logging.Log
// Uncomment and use to do annotations by name (recommended)
//import javax.annotation.Resource
// Uncomment and use to do injections by type
//import org.springframework.beans.factory.annotation.Autowired

@Component
class <%=className%> {
	private static Log log = LogFactory.getLog("plugins."+this.name)
	
	// Inject another component, translator, or plugin
	// @Resource(name="myTranslator")
	// MyTranslator myTranslator
}