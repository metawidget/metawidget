<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<html>
	<body>

		<f:view>

			<h:form id="form">
				<h:messages/>
				<m:metawidget value="#{validatorQuirks.foo}" rendered="#{!validatorQuirks.messageAdded}"/>
				<m:metawidget value="#{validatorQuirks.bar}" rendered="#{validatorQuirks.messageAdded}"/>
				<h:commandButton value="Add Message" action="#{validatorQuirks.addMessage}"/>
			</h:form>

		</f:view>
		
	</body>	    
</html>
