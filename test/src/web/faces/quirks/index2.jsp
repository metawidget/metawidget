<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>

		<f:view>
		
			<h1>Quirks</h1>

			<m:metawidget value="#{quirks}">
				<f:param name="columns" value="0"/>
				<m:stub action="#{quirks.refresh}"/>
			</m:metawidget>

			<h1>Quirks (Again)</h1>

			<m:metawidget value="#{quirks}">
				<m:stub action="#{quirks.refresh}">
					<h:outputText value="Test whether non-rendered" rendered="false"/>
					<h:outputText value="stub children still stub out" rendered="false"/>
					<h:outputText value="the parent completely" rendered="false"/>
				</m:stub>
			</m:metawidget>

			<h1>Quirks (Yet Again)</h1>

			<m:metawidget value="#{quirks}">
				<m:metawidget value="#{quirks.boolean}" rendererType="simple"/>
				<h:outputText value="#{quirks.large}"/>
				<m:metawidget value="#{quirks.strings}"/>
				<m:stub action="#{quirks.refresh}">
					<h:outputText value="Not rendered" rendered="false"/>
				</m:stub>
			</m:metawidget>

		</f:view>
		
	</body>	    
</html>
