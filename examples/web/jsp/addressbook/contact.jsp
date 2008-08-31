<%@ page language="java" %>
<%@ page import="org.metawidget.example.jsp.addressbook.controller.*, org.metawidget.example.shared.addressbook.model.*, org.metawidget.example.shared.addressbook.controller.*" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://metawidget.org/html" prefix="m"%>
<%@ taglib uri="http://metawidget.org/example/jsp/addressbook" prefix="a"%>

<%
	ContactController contactController = (ContactController) session.getAttribute( ContactController.class.getSimpleName() );
	
	if ( contactController == null )
	{
		contactController = new ContactController( session );
		session.setAttribute( ContactController.class.getSimpleName(), contactController );
		
		session.setAttribute( Contact.class.getName(), new PersonalContact() );
		contactController.setReadOnly( true );
	}
		
	// Parse request parameters

	String id = request.getParameter( "id" );
	ContactsController contactsController = (ContactsController) session.getServletContext().getAttribute( ContactsController.class.getSimpleName() );
	Contact contact = (Contact) session.getAttribute( Contact.class.getSimpleName() );
	
	if ( id != null )
	{
		contact = contactsController.load( Long.parseLong( id ));
		session.setAttribute( Contact.class.getSimpleName(), contact );
		contactController.setReadOnly( true );
	}
	else
	{
		// Manual binding
		
		if ( request.getParameter( "Contact.firstnames" ) != null )
			contact.setFirstnames( request.getParameter( "Contact.firstnames" ));
	}
	
	// Parse actions
	
	try
	{
		if ( request.getParameter( "ContactController.cancel" ) != null )
		{
			response.sendRedirect( "index.jsp" );
			return;
		}
		else if ( request.getParameter( "ContactController.edit" ) != null )
		{
			contactController.edit();
		}
		else if ( request.getParameter( "ContactController.save" ) != null )
		{
			contactController.save();
			response.sendRedirect( "index.jsp" );
			return;
		}
		else if ( request.getParameter( "ContactController.delete" ) != null )
		{
			contactController.delete();
			response.sendRedirect( "index.jsp" );
			return;
		}
	}
	catch( Exception e )
	{
		request.setAttribute( "errors", e.getMessage() );
	}
%>

<tags:page>

	<c:choose>
		<c:when test="${contact.class.simpleName == 'PersonalContact'}">
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

		<c:if test="${!empty errors}">
			<div class="errors">${errors}</div>
		</c:if>
		
		<form action="contact.jsp" method="POST">

			<m:metawidget value="Contact" readOnly="${ContactController.readOnly}">
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
						<c:if test="${!ContactController.readOnly}">
							<tfoot>
								<tr>
									<jsp:useBean id="communication" class="org.metawidget.example.shared.addressbook.model.Communication"/>						
									<td class="column-half"><m:metawidget value="communication.type" style="width: 100%" layoutClass=""/></td>
									<td class="column-half"><m:metawidget value="communication.value" style="width: 100%" layoutClass=""/></td>
									<td class="column-tiny, table-buttons"><input type="submit" name="addCommunication" value="Add"/></td>
								</tr>
							</foot>
						</c:if>
						<tbody>
							<c:forEach items="${a:sort(Contact.communications)}" var="_communication">
								<tr>
									<td class="column-half">${_communication.type}</td>
									<td class="column-half">${_communication.value}</td>
									<td class="column-tiny, table-buttons">
										<c:if test="${!contactController.readOnly}">
											<input type="submit" name="deleteCommunication" value="Delete" onClick="if ( !confirm( 'Are you sure you want to delete this communication?' )) return false; document.getElementById( 'deleteCommunicationId' ).value = '${_communication.id}'"/>
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</m:stub>

				<m:facet name="buttons" styleClass="buttons">
					<m:metawidget value="ContactController" layoutClass=""/>
				</m:facet>

			</m:metawidget>

		</form>
	
	</div>

</tags:page>