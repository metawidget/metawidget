<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://metawidget.org/struts" prefix="m"%>

<html>
	<body>

		<h1>Struts Hidden Field Test</h1>
		
		<html:form action="/saveHidden">

			<m:metawidget property="hiddenFieldForm" config="config/metawidget-hiddenfield.xml"/>

		</html:form>
		
	</body>	    
</html>
