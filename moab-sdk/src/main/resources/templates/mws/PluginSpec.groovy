<%=packageName ? 'package '+packageName : '' %>

@TestFor(<%=className%>)
class <%=testClassName%> extends Specification {
	def "Feature of the plugin"() {
		given:
		plugin.id = "myId"
		
		when:
		def id = plugin.id
		
		then:
		id=="myId"
	}
}