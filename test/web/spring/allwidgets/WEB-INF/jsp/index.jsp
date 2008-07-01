<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/spring" prefix="m" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
	<body>

		<form:form commandName="allWidgetsCommand">

			<form:errors />

			<m:metawidget path="allWidgetsCommand" style="aStyle" styleClass="aStyleClass" createHiddenFields="true" inspectorConfig="config/inspector-config.xml">
				<m:param name="tableStyle" value="aTableStyle"/>
				<m:param name="tableStyleClass" value="aTableStyleClass"/>
				<m:param name="columnStyleClasses" value="aLabelClass, aComponentClass, aRequiredClass"/>
				
				<m:stub path="mystery"/>
				
				<m:facet name="buttons" style="aButtonsStyle" styleClass="aButtonsStyleClass">
					<input type="submit" name="save" value="Save"/>
				</m:facet>				
			</m:metawidget>

		</form:form>
		
	</body>	    
</html>
