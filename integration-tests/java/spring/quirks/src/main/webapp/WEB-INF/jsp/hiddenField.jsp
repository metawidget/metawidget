<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/spring" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
	<body>

		<h1>Spring Hidden Field Test</h1>

		<form:form commandName="hiddenFieldCommand">

			<m:metawidget path="hiddenFieldCommand" config="metawidget-hiddenfield.xml"/>

		</form:form>
		
	</body>	    
</html>
