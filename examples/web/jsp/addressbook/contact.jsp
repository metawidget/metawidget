<%@ page language="java" %>
<%@ page import="org.metawidget.example.jsp.controller.*, org.metawidget.example.shared.addressbook.model.*, org.metawidget.example.shared.addressbook.controller.*" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://metawidget.org/html" prefix="m"%>
<%@ taglib uri="http://metawidget.org/example/jsp/addressbook" prefix="a"%>

<%
	ContactController contactController = new ContactController();
	pageContext.setAttribute( "contact", contactController );
	
	contactController.setCurrent( new BusinessContact() );
%>

<tags:page>

	<c:choose>
		<c:when test="${contact.class.simpleName == 'Personalcontact'}">
			<div id="page-image">
				<img src="media/personal.gif">
			</div>
			<div id="content">				
			<h1>Personal Contact</h1>
		</c:when>
		<c:otherwise>
			<div id="page-image">
				<img src="media/business.gif">
			</div>
			<div id="content">				
			<h1>Business Contact</h1>
		</c:otherwise>
	</c:choose>

		<form action="/save">

			<m:metawidget value="contact.current" readOnly="${contact.readOnly}">
				<m:param name="tableStyleClass" value="table-form"/>
				<m:param name="columnStyleClasses" value="table-label-column,table-component-column,required"/>
				<m:param name="sectionStyleClass" value="section-heading"/>

				<m:stub value="communications">
					<input type="hidden" name="deleteCommunicationId" id="deleteCommunicationId"/>
					<table class="data-table">
						<thead>
							<tr>
								<th class="column-half">Type</th>
								<th class="column-half">Value</th>
								<th class="column-tiny">&nbsp;</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${a:sort(contact.current.communications)}" var="_communication">
								<tr>
									<td class="column-half">${_communication.type}</td>
									<td class="column-half">${_communication.value}</td>
									<td class="column-tiny, table-buttons">
										<c:if test="${!readOnly}">
											<input type="submit" name="deleteCommunication" value="Delete" onClick="if ( !confirm( 'Are you sure you want to delete this communication?' )) return false; document.getElementById( 'deleteCommunicationId' ).value = '${_communication.id}'"/>
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</tbody>
						<c:if test="${!readOnly}">
							<tfoot>
								<tr>
									<jsp:useBean id="communication" class="org.metawidget.example.shared.addressbook.model.Communication"/>						
									<td class="column-half"><mh:metawidget value="communication.type" style="width: 100%" layoutClass=""/></td>
									<td class="column-half"><mh:metawidget value="communication.value" style="width: 100%" layoutClass=""/></td>
									<td class="column-tiny, table-buttons"><input type="submit" name="addCommunication" value="Add"/></td>
								</tr>
							</foot>
						</c:if>
					</table>
				</m:stub>

				<m:facet name="buttons" styleClass="buttons">
					<m:metawidget value="contact" />
				</m:facet>

			</m:metawidget>

		</form>
	
	</div>

</tags:page>