<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>

		<f:view>
		
			<h1>RichFaces Remove Duplicates Hack</h1>

			<h:messages/>
			
			<h:form>
				<m:metawidget value="#{removeDuplicatesHack.embedded}" config="metawidget-remove-duplicates-hack.xml" rendered="#{removeDuplicatesHack.embedded != null}">
					<h:inputHidden value="#{removeDuplicatesHack.embedded.bar3}"/>
				</m:metawidget>
				<h:commandLink value="Clear Embedded" action="#{removeDuplicatesHack.clearEmbedded}"/>
				<br/>
				<h:commandLink value="New Embedded" action="#{removeDuplicatesHack.newEmbedded}"/>
			</h:form>

		</f:view>
		
	</body>	    
</html>
