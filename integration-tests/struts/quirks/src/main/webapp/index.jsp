<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://metawidget.org/struts" prefix="m"%>

<html>
	<body>

		<h1>Struts Quirks Test</h1>

		<jsp:useBean id="lookupValues" class="org.metawidget.integrationtest.struts.quirks.model.LookupValues"/>
		<jsp:useBean id="lookupLabels" class="org.metawidget.integrationtest.struts.quirks.model.LookupLabels"/>

		<html:form action="/save">

			<html:errors />
			
			<m:metawidget property="quirksForm"/>

		</html:form>
		
	</body>	    
</html>
