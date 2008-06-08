<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://metawidget.org/faces" prefix="m" %>

<f:view>

	<html>
		<body>

			<h:form id="form">

				<h:messages />

				<m:metawidget value="#{allWidgets}" style="aStyle" styleClass="aStyleClass" createHiddenFields="true" inspectorConfig="inspector-config.xml">
					<f:param name="tableStyle" value="aTableStyle"/>
					<f:param name="tableStyleClass" value="aTableStyleClass"/>
					<f:param name="columnClasses" value="aLabelClass, aComponentClass, aRequiredClass"/>
					<f:param name="buttonsStyle" value="aButtonsStyle"/>
					<f:param name="buttonsStyleClass" value="aButtonsStyleClass"/>
					
					<m:stub value="#{allWidgets.mystery}"/>

					<f:facet name="buttons">
						<h:commandButton value="Save" action="#{allWidgetsBean.save}"/>
					</f:facet>					
				</m:metawidget>

			</h:form>
		
		</body>	    
	</html>

</f:view>
