<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>

<f:view>

	<html>
		<body>

			<m:metawidget id="allWidgetsMetawidget" value="#{allWidgets}" createHiddenFields="true" rendererType="div" readOnly="true" config="config/metawidget.xml">
				<f:param name="divStyleClasses" value="anOuterClass,aLabelClass,aRequiredClass,aComponentClass"/>					
				<f:param name="outerStyle" value="anOuterStyle"/>					
				<f:param name="labelStyle" value="aLabelStyle"/>
				<f:param name="requiredStyle" value="aRequiredStyle"/>
				<f:param name="componentStyle" value="aComponentStyle"/>
				<f:param name="sectionStyle" value="aSectionStyle"/>
				<f:param name="sectionStyleClass" value="aSectionStyleClass"/>
				<f:param name="footerStyle" value="aFooterStyle"/>
				<f:param name="footerStyleClass" value="aFooterStyleClass"/>
				<f:facet name="footer"><h:outputText value="Footer"/></f:facet>
				<m:stub value="#{allWidgets.mystery}"/>
			</m:metawidget>
		
		</body>	    
	</html>

</f:view>
