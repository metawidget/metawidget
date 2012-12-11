<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>

		<f:view>
    
			<h1>Quirks (panelGroup test)</h1>
	    
	    	<h:messages/>
	    	
	        <h:form>
	        	<m:metawidget value="#{richQuirks}" config="metawidget-panelGroup.xml">
					<h:inputText value="#{richQuirks.integer}" style="background-color: red"/>
	        	</m:metawidget>
	        </h:form>
	        
		</f:view>
        
    </body>
    
</html>

