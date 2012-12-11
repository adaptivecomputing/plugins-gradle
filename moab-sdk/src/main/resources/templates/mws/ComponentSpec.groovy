<%=packageName ? 'package '+packageName : '' %>

@TestFor(<%=className%>)
class <%=testClassName%> extends Specification {
	def "Feature of the component"() {
		when:
		def result = component.feature()
		
		then:
		result==true
	}
}