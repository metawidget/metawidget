<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>

<html>
	<body>

		<f:view>

			<h:form id="form">

				<h:messages />

				<m:metawidget value="#{allWidgets}" style="aStyle" styleClass="aStyleClass" createHiddenFields="true" inspectorConfig="config/inspector-config.xml">
					<f:param name="tableStyle" value="aTableStyle"/>
					<f:param name="tableStyleClass" value="aTableStyleClass"/>
					<f:param name="columnClasses" value="aLabelClass, aComponentClass, aRequiredClass"/>
					<f:param name="rowClasses" value="aRowClass1, aRowClass2"/>
					<f:param name="headerStyle" value="aHeaderStyle"/>
					<f:param name="headerStyleClass" value="aHeaderStyleClass"/>
					<f:param name="footerStyle" value="aFooterStyle"/>
					<f:param name="footerStyleClass" value="aFooterStyleClass"/>
					<f:param name="componentStyle" value="aComponentStyle"/>
					<f:param name="requiredStyle" value="aRequiredStyle"/>
					<f:param name="sectionStyle" value="aSectionStyle"/>
					<f:param name="labelSuffix" value=""/>
					
					<f:facet name="header">
						<h:outputText value="This page tests all possible widgets"/>
					</f:facet>
					
					<m:stub value="#{allWidgets.mystery}" attributes="dummy-attribute: dummy-value"/>

					<f:facet name="footer">
						<h:commandButton value="Save" action="#{allWidgetsBean.save}"/>
					</f:facet>					
				</m:metawidget>

			</h:form>
		
		</f:view>
		
	</body>	    
</html>		