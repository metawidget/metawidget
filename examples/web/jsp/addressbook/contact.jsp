<%@ page language="java" %>
<%@ page import="java.beans.*, java.util.*, org.metawidget.example.jsp.addressbook.controller.*, org.metawidget.example.shared.addressbook.model.*, org.metawidget.example.shared.addressbook.controller.*" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
		ContactsController contactsController = (ContactsController) session.getServletContext().getAttribute( "contacts" );
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
		else if ( request.getParameter( "contactController.save" ) != null || request.getParameter( "contactController.delete" ) != null || request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null )
		{
			// Manual binding
			
			Contact contact = (Contact) session.getAttribute( "contact" );			
			contact.setTitle( request.getParameter( "contact.title" ));
			contact.setFirstname( request.getParameter( "contact.firstname" ));
			contact.setSurname( request.getParameter( "contact.surname" ));
			
			if ( contact instanceof PersonalContact )
			{
				PersonalContact personalContact = (PersonalContact) contact;
				PropertyEditor dateEditor = PropertyEditorManager.findEditor( Date.class );
				dateEditor.setAsText( request.getParameter( "contact.dateOfBirth" ));
				personalContact.setDateOfBirth( (Date) dateEditor.getValue() );
			}
			else if ( contact instanceof BusinessContact )
			{
				BusinessContact businessContact = (BusinessContact) contact;
				businessContact.setCompany( request.getParameter( "contact.company" ));
				businessContact.setNumberOfStaff( Integer.parseInt( request.getParameter( "contact.numberOfStaff" )));
			}

			// Execute action
			
			if ( request.getParameter( "contactController.delete" ) != null )
			{
				contactController.delete();
				response.sendRedirect( "index.jsp" );
				return;
			}

			communication.setType( request.getParameter( "communication.type" ));
			communication.setValue( request.getParameter( "communication.value" ));
			
			if ( request.getParameter( "addCommunication" ) != null )
			{
				contactController.addCommunication();
			}
			else if ( request.getParameter( "deleteCommunication" ) != null )
			{
				contactController.deleteCommunication( Long.parseLong( request.getParameter( "deleteCommunicationId" )));
			}
			
			contactController.save();
			
			if ( request.getParameter( "contactController.save" ) != null )
			{
				response.sendRedirect( "index.jsp" );
				return;
			}			
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

	<fmt:setBundle var="bundle" basename="org.metawidget.example.shared.addressbook.resource.Resources"/>

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

		<form action="contact.jsp" method="POST">

			<c:if test="${!empty errors}">
				<span class="errors">${errors}</span>
			</c:if>
		
			<m:metawidget value="contact" bundle="${bundle}" readOnly="${contactController.readOnly}">
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