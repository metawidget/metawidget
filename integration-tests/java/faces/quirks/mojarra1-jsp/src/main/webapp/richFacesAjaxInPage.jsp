<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>

		<f:view>
		
			<h1>RichFaces AJAX Quirks (In Page)</h1>

			<h:messages/>
			
			<h:form id="form">
				<m:metawidget value="#{richQuirksAjax.select}" inspectFromParent="true">
					<a4j:support event="onclick" onsubmit="alert('Foo')"/>
				</m:metawidget>
				<m:metawidget value="#{richQuirksAjax.label}" inspectFromParent="true"/>
				<h:commandButton value="Update" action="#{richQuirksAjax.updateLabel}"/>
			</h:form>

		</f:view>
		
	</body>	    
</html>
