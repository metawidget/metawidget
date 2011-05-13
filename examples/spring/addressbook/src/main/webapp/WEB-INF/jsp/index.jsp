<%@ page language="java" %>
<%@ page import="org.metawidget.example.shared.addressbook.model.*, org.metawidget.example.shared.addressbook.controller.*, org.springframework.web.context.support.*" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://metawidget.org/spring" prefix="m" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://metawidget.org/example/spring/addressbook" prefix="a" %>

<%
	ContactSearch contactSearch = (ContactSearch) pageContext.findAttribute( "contactSearchCommand" );	
	ContactsController controller = (ContactsController) pageContext.findAttribute( "contacts" );
		
	pageContext.setAttribute( "contactResults", controller.getAllByExample( contactSearch ));
%>

<tags:page>
	
	<div id="page-image">
		<img src="media/addressbook.gif" alt=""/>
	</div>

	<div id="content">

		<form:form commandName="contactSearchCommand">
		
			<m:metawidget path="contactSearchCommand">
				<m:facet name="footer">
					<input type="submit" name="search" value="<spring:message code="search"/>"/>
					<input type="submit" name="addPersonal" value="<spring:message code="addPersonal"/>"/>
					<input type="submit" name="addBusiness" value="<spring:message code="addBusiness"/>"/>
				</m:facet>
			</m:metawidget>
		
		</form:form>
		
		<table class="data-table">
			<thead>
				<tr>
					<th class="column-half">Name</th>
					<th class="column-half">Contact</th>
					<th class="column-tiny">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${contactResults}" var="_contact">
					<tr>
						<td class="column-half">
							<c:choose>
								<c:when test="${_contact['class'].simpleName == 'PersonalContact'}">
									<c:set var="urlContact" value="loadPersonal"/>
								</c:when>
								<c:otherwise>
									<c:set var="urlContact" value="loadBusiness"/>
								</c:otherwise>
							</c:choose>
							<a href="contact.html?id=${_contact.id}">
								${_contact.fullname}
							</a>
						</td>
						<td class="column-half">${a:collectionToString(_contact.communications, ", ")}</td>
						<td class="column-tiny">
							<c:choose>
								<c:when test="${_contact['class'].simpleName == 'PersonalContact'}">
									<img src="media/personal-small.gif" alt="Personal Contact"/>
								</c:when>
								<c:otherwise>
									<img src="media/business-small.gif" alt="Business Contact"/>
								</c:otherwise>							
							</c:choose>
						</td>
					</tr>		
				</c:forEach>
			</tbody>
		</table>
		
	</div>
	
</tags:page>
