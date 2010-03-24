<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="aNull" value="${null}" scope="request"/>

<html>
	<body>

		<f:view>

			<h1>Quirks (stub Converter test)</h1>
					
			<m:metawidget value="#{stubConverter}">
			
				<m:stub value="#{stubConverter.foo}">
				
					<h:outputText value="#{stubConverter.foo}" style="color: black"/><br/>
					<h:outputText value="#{stubConverter.bar}" style="color: black"/><br/>
					<h:inputText value="#{stubConverter.foo}" style="color: black"/>
				
				</m:stub>
			
				<m:stub value="#{stubConverter.bar}">
				
					<h:outputText value="#{stubConverter.foo}" style="color: black"/><br/>
					<h:outputText value="#{stubConverter.bar}" style="color: black"/><br/>
					<h:inputText value="#{stubConverter.foo}" style="color: black"/>
				
				</m:stub>

			</m:metawidget>
			
		</f:view>
		
	</body>	    
</html>
