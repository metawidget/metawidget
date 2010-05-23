<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>

		<f:view>
		
			<h1>RichFaces Nested Quirks</h1>

			<h:form>
				<m:metawidget value="#{richQuirks}" config="metawidget-richfaces-nested.xml">
					<m:stub value="#{richQuirks.abc}" attributes="section: 'Not foo'">
						<h:inputText value="#{richQuirks.abc}"/>
					</m:stub>				
				</m:metawidget>
			</h:form>

		</f:view>
		
	</body>	    
</html>
