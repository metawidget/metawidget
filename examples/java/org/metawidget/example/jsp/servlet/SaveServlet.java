// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.example.jsp.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;

// TODO: Pure JSP version of Address Book

public class SaveServlet
	extends HttpServlet
{
	//
	//
	// Private statics
	//
	//

	private final static	DateFormat	DATE_FORMAT = new SimpleDateFormat();
	//
	//
	// Public methods
	//
	//

	@Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response )
		throws IOException
	{
		// Support editing

		if ( request.getParameter( "edit" ) != null )
		{
			//formContact.setReadOnly( false );
			response.sendRedirect( "contact.jsp" );
			return;
		}

		// Look up the ContactsController...

		ServletContext context = request.getSession().getServletContext();
		ContactsController controller = (ContactsController) context.getAttribute( "contacts" );

		// ...map from the request

		try
		{
			Contact contact;

			//if ( contact instanceof PersonalContact )
			{
				contact = new PersonalContact();

				PersonalContact contactPersonal = (PersonalContact) contact;

				String dateOfBirth = request.getParameter( "contact.dateOfBirth" );

				if ( dateOfBirth != null )
				{
					synchronized( DATE_FORMAT )
					{
						contactPersonal.setDateOfBirth( DATE_FORMAT.parse( dateOfBirth ));
					}
				}
			}
			//else
			{
				contact = new BusinessContact();

				BusinessContact contactBusiness = (BusinessContact) contact;

				contactBusiness.setCompany( request.getParameter( "contact.company" ) );

				String numberOfStaff = request.getParameter( "contact.numberOfStaff" );

				if ( numberOfStaff != null )
					contactBusiness.setNumberOfStaff( Integer.parseInt( numberOfStaff ));
			}

			//contact.setId( request.getParameter( "contact.id" ) );
			contact.setTitle( request.getParameter( "contact.title" ));
			contact.setFirstnames( request.getParameter( "contact.firstnames" ));
			contact.setSurname( request.getParameter( "contact.surname" ));
			//contact.setGender( formContact.getGender() );
			//contact.setAddress( formContact.getAddress() );
			//contact.setCommunications( formContact.getCommunications() );
			contact.setNotes( request.getParameter( "contact.notes" ));

			// Add Communication (if any)

			if ( request.getParameter( "addCommunication" ) != null )
			{
				String type = request.getParameter( "communication.type" );
				String value = request.getParameter( "communication.value" );

				contact.addCommunication( new Communication( type, value ));
			}

			// Delete Communication (if any)

			else if ( request.getParameter( "deleteCommunication" ) != null )
			{
				String id = request.getParameter( "deleteCommunicationId" );
				contact.removeCommunication( Long.parseLong( id ) );
			}

			// Save

			if ( request.getParameter( "save" ) != null || request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null )
			{
				controller.save( contact );

				if ( request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null )
				{
					response.sendRedirect( "contact.jsp" );
					return;
				}
			}

			// Delete

			if ( request.getParameter( "delete" ) != null )
			{
				controller.delete( contact.getId() );
			}
		}
		catch( Exception e )
		{
			ActionErrors errors = new ActionErrors();
			errors.add( "save", new ActionMessage( e.getMessage(), false ) );
			//addErrors( request, errors );

			response.sendRedirect( "contact.jsp" );
			return;
		}

		// Cancel

		response.sendRedirect( "index.jsp" );
		return;
	}
}
