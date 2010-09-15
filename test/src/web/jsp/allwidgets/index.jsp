<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/html" prefix="m"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<body>

		<form action="saved.jsp" method="post">

			<jsp:useBean id="allWidgets" class="org.metawidget.shared.allwidgets.proxy.AllWidgets$$EnhancerByCGLIB$$1234"/>
			<fmt:setBundle basename="org.metawidget.shared.allwidgets.resource.Resources" var="bundle"/>
			
			<m:metawidget value="allWidgets" bundle="${bundle}" style="aStyle" styleClass="aStyleClass" config="config/metawidget.xml">
				
				<m:stub value="longObject" attributes="dummy-attribute: dummy-value"/>
				
				<m:facet name="footer">
					<input type="submit" value="Save"/>
				</m:facet>
				
			</m:metawidget>

		</form>
		
	</body>	    
</html>
