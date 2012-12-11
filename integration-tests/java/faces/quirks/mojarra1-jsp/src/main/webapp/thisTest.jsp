<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>

		<f:view>

			<h1>Quirks ('this' test)</h1>
					
			<m:metawidget value="#{thisTest}"/>
			
			<hr/>
			
			<m:metawidget value="#{thisTest.me}"/>
			
			<hr/>

			<m:metawidget value="#{thisTest.child.me}"/>
			
		</f:view>
		
	</body>	    
</html>
