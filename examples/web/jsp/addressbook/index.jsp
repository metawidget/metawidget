<%@ page language="java" %>
<%@ page import="java.util.*, org.metawidget.example.shared.addressbook.model.*, org.metawidget.example.shared.addressbook.controller.*" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="http://metawidget.org/html" prefix="m"%>
<%@ taglib uri="http://metawidget.org/example/jsp/addressbook" prefix="a"%>

<tags:page>

	<jsp:useBean id="contactsController" class="org.metawidget.example.shared.addressbook.controller.ContactsController" scope="application"/>
	<jsp:useBean id="contactSearch" class="org.metawidget.example.shared.addressbook.model.ContactSearch"/>

	<fmt:setBundle var="bundle" basename="org.metawidget.example.shared.addressbook.resource.Resources"/>

	<div id="page-image">
		<img src="media/addressbook.gif">
	</div>

	<div id="content">
		
		<html:form action="/search">

			<m:metawidget value="contactSearch" bundle="${bundle}">
				<m:param name="tableStyleClass" value="table-form"/>
				<m:param name="columnStyleClasses" value="table-label-column,table-component-column,required"/>

				<m:facet name="buttons" styleClass="buttons">
					<input type="submit" name="search" value="<fmt:message key="search" bundle="${bundle}"/>"/>
					<input type="submit" name="addPersonal" value="<fmt:message key="addPersonal" bundle="${bundle}"/>"/>
					<input type="submit" name="addBusiness" value="<fmt:message key="addBusiness" bundle="${bundle}"/>"/>
				</m:facet>
			</m:metawidget>

		</html:form>

		<table class="data-table">
			<thead>
				<tr>
					<th class="column-half">Name</th>
					<th class="column-half">Contact</th>
					<th class="column-tiny">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
			
				<c:forEach items="<%= contactsController.getAllByExample( contactSearch ) %>" var="_contact">
					<tr>
						<td class="column-half">
							<a href="load.do?id=${_contact.id}">
								${_contact.fullname}
							</a>
						</td>
						<td class="column-half">${a:collectionToString(_contact.communications,", ")}</td>
						<td class="column-tiny">
							<c:choose>
								<c:when test="${_contact.class.simpleName == 'PersonalContact'}">
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
