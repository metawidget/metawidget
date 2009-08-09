<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/spring" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
	<body>

		<form:form commandName="quirksCommand">

			<form:errors />

			<m:metawidget path="quirksCommand">

				<c:if test="${param.stubbed == 'true'}">
					<m:stub path="lookup">
						Stubbed!
					</m:stub>
				</c:if>
				
			</m:metawidget>

		</form:form>
		
	</body>	    
</html>
