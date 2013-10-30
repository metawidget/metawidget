<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/spring" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
	<body>

		<h1>Spring Inline Error Test</h1>

		<form:form commandName="inlineErrorCommand">

			<m:metawidget path="inlineErrorCommand"/>
			<m:metawidget path="inlineErrorCommand" config="metawidget-inlineError.xml"/>
			
			<input type="submit" name="validate" value="Validate"/>

		</form:form>
		
	</body>	    
</html>
