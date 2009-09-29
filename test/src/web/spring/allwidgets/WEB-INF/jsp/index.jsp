<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/spring" prefix="m" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
	<body>

		<form:form commandName="allWidgetsCommand">

			<form:errors />

			<m:metawidget path="allWidgetsCommand" style="aStyle" styleClass="aStyleClass" createHiddenFields="true" config="config/metawidget.xml">
				
				<m:stub path="mystery" attributes="dummy-attribute: dummy-value"/>
				
				<m:facet name="footer">
					<input type="submit" name="save" value="Save"/>
				</m:facet>				
			</m:metawidget>

		</form:form>
		
	</body>	    
</html>
