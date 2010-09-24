<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<html>
	<body>

		<f:view>

			<h1>Quirks (binding test)</h1>
				    
	        <h:form id="form">
	        
	        	<m:metawidget value="#{binding}" binding="#{binding.metawidget}"/>
	        	<h:outputText value="Baz is '#{binding.foo.baz}'"/>
	        	
	        </h:form>

		</f:view>
		
	</body>	    
</html>


