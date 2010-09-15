<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://metawidget.org/struts" prefix="m"%>

<html>
	<body>

		<html:form action="/save">

			<html:errors />
			
			<m:metawidget property="allWidgetsForm.allWidgets" style="aStyle" styleClass="aStyleClass" config="config/metawidget.xml">
				
				<m:stub property="mystery" attributes="dummy-attribute: dummy-value"/>
				<m:stub property="collection"/>
				
				<m:facet name="footer">
					<html:submit value="Save"/>
				</m:facet>
			</m:metawidget>

		</html:form>
		
	</body>	    
</html>
