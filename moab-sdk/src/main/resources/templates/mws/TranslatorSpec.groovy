<%=packageName ? 'package '+packageName : '' %>

@TestFor(<%=className%>)
class <%=testClassName%> extends Specification {
	def "Feature of the translator"() {
		when:
		def result = translator.feature()
		
		then:
		result==true
	}
}