<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>

<html>
	<body>

		<f:view>
		
			<%-- Test the 'not tri-state if not listbox' in HtmlMetawidget.buildActiveWidget --%>
			
			<m:metawidget value="#{quirks}"/>
			
			<%-- Test the '-1 check' in UIMetawidget.inspect --%>
			
			<m:metawidget value="#{quirks.boolean}"/>
			
		</f:view>
		
	</body>	    
</html>
