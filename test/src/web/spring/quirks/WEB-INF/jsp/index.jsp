<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/spring" prefix="m" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
	<body>

		<form:form commandName="quirksCommand">

			<form:errors />

			<m:metawidget path="quirksCommand"/>

		</form:form>
		
	</body>	    
</html>
