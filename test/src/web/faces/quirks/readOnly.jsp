<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>

		<f:view>

			<h1>Quirks Facelets ('readOnly' test)</h1>
					
			<m:metawidget value="#{quirks.boolean}" readOnly="true"/>
			<m:metawidget value="#{quirks.boolean}" readOnly="#{true}"/>
			
		</f:view>
		
	</body>	    
</html>
