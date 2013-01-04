<%=packageName ? 'package '+packageName : '' %>

import spock.lang.*
import com.adaptc.mws.plugins.testing.*

@TestFor(<%=className%>)
class <%=testClassName%> extends Specification {
	def "Feature of the translator"() {
		when:
		def result = translator.feature()
		
		then:
		result==true
	}
}