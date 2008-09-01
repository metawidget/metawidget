<%@ page language="java" %>
<%@ page import="org.metawidget.example.jsp.addressbook.controller.*, org.metawidget.example.shared.addressbook.model.*, org.metawidget.example.shared.addressbook.controller.*" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://metawidget.org/html" prefix="m"%>
<%@ taglib uri="http://metawidget.org/example/jsp/addressbook" prefix="a"%>

<%
	ContactController contactController = (ContactController) session.getAttribute( "contactController" );
	
	if ( contactController == null )
	{
		contactController = new ContactController( session );
		session.setAttribute( "contactController", contactController );		
	}
		
	// Parse request parameters

	String id = request.getParameter( "id" );
	
	if ( "personal".equals( id ))
	{
		session.setAttribute( "contact", new PersonalContact() );
		contactController.setReadOnly( false );
	}
	else if ( "business".equals( id ))
	{
		session.setAttribute( "contact", new BusinessContact() );
		contactController.setReadOnly( false );
	}
	else if ( id != null )
	{
		ContactsController contactsController = (ContactsController) session.getServletContext().getAttribute( "contactsController" );
		Contact contact = contactsController.load( Long.parseLong( id ));
		session.setAttribute( "contact", contact );
		contactController.setReadOnly( true );
	}

	Communication communication = new Communication();
	session.setAttribute( "communication", communication );
	
	// Parse actions
	
	try
	{
		if ( request.getParameter( "contactController.cancel" ) != null )
		{
			response.sendRedirect( "index.jsp" );
			return;
		}
		else if ( request.getParameter( "contactController.edit" ) != null )
		{
			contactController.edit();
		}
		else if ( request.getParameter( "contactController.save" ) != null )
		{
			// Manual binding
			
			Contact contact = (Contact) session.getAttribute( "contact" );			
			contact.setFirstname( request.getParameter( "contact.firstname" ));
			contact.setSurname( request.getParameter( "contact.surname" ));
			
			if ( contact instanceof BusinessContact )
			{
				BusinessContact businessContact = (BusinessContact) contact;
				businessContact.setNumberOfStaff( Integer.parseInt( request.getParameter( "contact.numberOfStaff" )));
			}

			contactController.save();
			response.sendRedirect( "index.jsp" );
			return;
		}
		else if ( request.getParameter( "contactController.delete" ) != null )
		{
			contactController.delete();
			response.sendRedirect( "index.jsp" );
			return;
		}
		else if ( request.getParameter( "addCommunication" ) != null )
		{
			communication.setType( request.getParameter( "communication.type" ));
			communication.setValue( request.getParameter( "communication.value" ));
			
			contactController.addCommunication();
		}
		else if ( request.getParameter( "deleteCommunication" ) != null )
		{
			contactController.deleteCommunication( Long.parseLong( request.getParameter( "deleteCommunicationId" )));
		}
	}
	catch( Exception e )
	{
		String message = e.getMessage();
		
		if ( message == null )
			message = e.getClass().getSimpleName();
		
		request.setAttribute( "errors", message );
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

			<m:metawidget value="contact" readOnly="${contactController.readOnly}">
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
						<c:if test="${!contactController.readOnly}">
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
							<c:forEach items="${a:sort(contact.communications)}" var="_communication">
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
					<m:metawidget value="contactController" layoutClass=""/>
				</m:facet>

			</m:metawidget>

		</form>
	
	</div>

</tags:page>