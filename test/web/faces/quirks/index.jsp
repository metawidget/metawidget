<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="aNull" value="${null}" scope="request"/>

<html>
	<body>

		<f:view>
		
			<m:metawidget value="#{quirks}">
				<f:param name="columns" value="2"/>
			</m:metawidget>
						
			<m:metawidget value="#{aNull}">
				<f:param name="columns" value="0"/>
			</m:metawidget>
			
		</f:view>
		
	</body>	    
</html>
