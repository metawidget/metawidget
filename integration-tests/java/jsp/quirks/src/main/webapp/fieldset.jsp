<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/html" prefix="m"%>

<html>
	<body>

		<jsp:useBean id="fieldsetLayout" class="org.metawidget.integrationtest.jsp.quirks.model.FieldsetLayoutQuirks"/>

		<m:metawidget value="fieldsetLayout" config="metawidget-fieldsetLayout.xml"/>
		
	</body>	    
</html>
