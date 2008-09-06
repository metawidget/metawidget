<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/html" prefix="m"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<body>

		<form action="saved.jsp" method="POST">

			<jsp:useBean id="allWidgets" class="org.metawidget.test.shared.allwidgets.proxy.AllWidgets$$EnhancerByCGLIB$$1234"/>
			<fmt:setBundle basename="org.metawidget.test.shared.allwidgets.resource.Resources" var="bundle"/>
			
			<m:metawidget value="allWidgets" bundle="${bundle}" style="aStyle" styleClass="aStyleClass" createHiddenFields="true" inspectorConfig="config/inspector-config.xml">
				<m:param name="tableStyle" value="aTableStyle"/>
				<m:param name="tableStyleClass" value="aTableStyleClass"/>
				<m:param name="columnStyleClasses" value="aLabelClass, aComponentClass, aRequiredClass"/>
				
				<m:stub value="longObject"/>
				
				<m:facet name="footer" style="aHeaderStyle" styleClass="aHeaderStyleClass">
					<input type="submit" value="Save"/>
				</m:facet>
				
			</m:metawidget>

		</form>
		
	</body>	    
</html>
