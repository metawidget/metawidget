<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/html" prefix="m"%>

<html>
	<body>

		<jsp:useBean id="quirks" class="org.metawidget.integrationtest.jsp.quirks.model.JspQuirks"/>

		<h1>Stub Out</h1>
		
		<m:metawidget value="quirks"/>
			
		<m:metawidget value="quirks">
				
			<m:stub value="stubOut" />
				
		</m:metawidget>
		
	</body>	    
</html>
