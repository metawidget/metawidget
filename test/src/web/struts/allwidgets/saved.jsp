<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://metawidget.org/struts" prefix="m"%>

<html>
	<body>

		<html:form action="/save">

			<m:metawidget property="allWidgetsForm.allWidgets" readOnly="true" config="config/metawidget.xml">
				<m:stub property="mystery"/>
			</m:metawidget>
			
		</html:form>

	</body>	    
</html>
