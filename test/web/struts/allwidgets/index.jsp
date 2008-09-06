<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://metawidget.org/struts" prefix="m"%>

<html>
	<body>

		<html:form action="/save">

			<html:errors />
			
			<m:metawidget property="allWidgetsForm.allWidgets" style="aStyle" styleClass="aStyleClass" createHiddenFields="true" inspectorConfig="config/inspector-config.xml">
				<m:param name="tableStyle" value="aTableStyle"/>
				<m:param name="tableStyleClass" value="aTableStyleClass"/>
				<m:param name="columnStyleClasses" value="aLabelClass, aComponentClass, aRequiredClass"/>
				
				<m:stub property="mystery"/>
				<m:stub property="collection"/>
				
				<m:facet name="footer" style="aHeaderStyle" styleClass="aHeaderStyleClass">
					<html:submit value="Save"/>
				</m:facet>
			</m:metawidget>

		</html:form>
		
	</body>	    
</html>
