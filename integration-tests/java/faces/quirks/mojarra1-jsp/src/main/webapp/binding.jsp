<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<html>
	<body>

		<f:view>

			<h1>Quirks (binding test)</h1>
				    
	        <h:form id="form">
	        
	        	<h1>Normal Binding</h1>
	        	
	        	<m:metawidget value="#{binding}" binding="#{binding.metawidget1}"/>
	        	<h:outputText value="Baz is '#{binding.foo.baz}'"/>
	        	
	        	<h1>Direct Object</h1>
	        	
	        	<m:metawidget binding="#{binding.metawidget2}"/>

	        	<h1>Direct Class</h1>
	        	
	        	<m:metawidget binding="#{binding.metawidget3}"/>

	        </h:form>

		</f:view>
		
	</body>	    
</html>


