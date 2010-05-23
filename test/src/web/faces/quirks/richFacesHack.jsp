<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>

		<f:view>
		
			<h1>RichFaces Restore State Hack</h1>

			<h:messages/>
			
			<h:form>
				<m:metawidget value="#{richQuirksHack.embedded}" config="metawidget-richfaces-hack.xml" rendered="#{richQuirksHack.embedded != null}">
					<h:inputHidden value="#{richQuirksHack.embedded.bar3}"/>
				</m:metawidget>
				<h:commandLink value="Clear Embedded" action="#{richQuirksHack.clearEmbedded}"/>
				<br/>
				<h:commandLink value="New Embedded" action="#{richQuirksHack.newEmbedded}"/>
			</h:form>

		</f:view>
		
	</body>	    
</html>
