<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>

		<f:view>
		
			<h1>RichFaces Tab Quirks</h1>

			<h:form id="form">
				<m:metawidget value="#{tabQuirks}" config="metawidget-richfaces-nested.xml"/>
			</h:form>

		</f:view>
		
	</body>	    
</html>
