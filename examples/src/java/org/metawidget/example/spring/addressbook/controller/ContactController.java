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

package org.metawidget.example.spring.addressbook.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.spring.addressbook.editor.DateEditor;
import org.metawidget.example.spring.addressbook.editor.EnumEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Richard Kennard
 */

public class ContactController
	extends SimpleFormController
{
	//
	// Protected methods
	//

	@Override
	protected void initBinder( HttpServletRequest request, ServletRequestDataBinder binder )
		throws Exception
	{
		super.initBinder( request, binder );

		binder.registerCustomEditor( Gender.class, new EnumEditor<Gender>( Gender.class ) );
		binder.registerCustomEditor( Date.class, new DateEditor() );
	}

	@Override
	protected Object formBackingObject( HttpServletRequest request )
		throws Exception
	{
		String id = request.getParameter( "id" );

		if ( id != null && !"".equals( id ))
		{
			request.getSession().setAttribute( "readOnly", Boolean.TRUE );

			// Look up the ContactsController...

			ContactsController controller = (ContactsController) getWebApplicationContext().getBean( "contacts" );

			// ...and use it to load the Contact...

			return controller.load( Long.parseLong( id ) );
		}

		request.getSession().setAttribute( "readOnly", Boolean.FALSE );

		// Create business

		if ( request.getParameter( "business" ) != null )
		{
			return new BusinessContact();
		}

		// Default to personal

		return new PersonalContact();
	}

	@Override
	protected void onBindAndValidate( HttpServletRequest request, Object command, BindException errors )
		throws Exception
	{
		// Edit

		if ( request.getParameter( "edit" ) != null )
		{
			request.getSession().setAttribute( "readOnly", Boolean.FALSE );
			return;
		}

		Contact contact = (Contact) command;

		// Add Communication (if any)

		if ( request.getParameter( "addCommunication" ) != null )
		{
			String type = request.getParameter( "communication.type" );
			String value = request.getParameter( "communication.value" );

			try
			{
				contact.addCommunication( new Communication( type, value ));
			}
			catch( Exception e )
			{
				errors.reject( null, e.getMessage() );
				return;
			}
		}

		// Delete Communication (if any)

		else if ( request.getParameter( "deleteCommunication" ) != null )
		{
			String id = request.getParameter( "deleteCommunicationId" );

			try
			{
				contact.removeCommunication( Long.parseLong( id ) );
			}
			catch( Exception e )
			{
				errors.reject( null, e.getMessage() );
				return;
			}
		}

		// Save

		if ( request.getParameter( "save" ) != null || request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null )
		{
			ContactsController controller = (ContactsController) getWebApplicationContext().getBean( "contacts" );

			try
			{
				controller.save( contact );
			}
			catch( Exception e )
			{
				errors.reject( null, e.getMessage() );
			}

			return;
		}

		// Delete

		if ( request.getParameter( "delete" ) != null )
		{
			ContactsController controller = (ContactsController) getWebApplicationContext().getBean( "contacts" );
			controller.delete( contact );
		}
	}

	@Override
	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
		throws Exception
	{
		// Edit/Add Communication/Delete Communication (stay on same page)

		if ( request.getParameter( "edit" ) != null || request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null )
		{
			return showForm( request, response, errors );
		}

		// Save/Cancel/Delete

		return new ModelAndView( new RedirectView( "index.html" ));
	}
}
