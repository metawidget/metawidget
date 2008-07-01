<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>

<f:view>

	<html>
		<body>

			<m:metawidget value="#{allWidgets}" createHiddenFields="true" rendererType="div" readOnly="true" inspectorConfig="config/inspector-config.xml">
				<f:param name="divStyleClasses" value="anOuterClass,aLabelClass,aRequiredClass,aComponentClass"/>					
				<f:param name="outerStyle" value="anOuterStyle"/>					
				<f:param name="labelStyle" value="aLabelStyle"/>
				<f:param name="requiredStyle" value="aRequiredStyle"/>
				<f:param name="componentStyle" value="aComponentStyle"/>
				<m:stub value="#{allWidgets.mystery}"/>
			</m:metawidget>
		
		</body>	    
	</html>

</f:view>
