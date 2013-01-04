<%=packageName ? 'package '+packageName : '' %>

import spock.lang.*
import com.adaptc.mws.plugins.testing.*

@TestFor(<%=className%>)
class <%=testClassName%> extends Specification {
	def "Feature of the component"() {
		when:
		def result = component.feature()
		
		then:
		result==true
	}
}