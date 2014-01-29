// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

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
import org.metawidget.util.simple.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ContactController
	extends SimpleFormController {

	//
	// Protected methods
	//

	@Override
	protected void initBinder( HttpServletRequest request, ServletRequestDataBinder binder )
		throws Exception {

		super.initBinder( request, binder );

		binder.registerCustomEditor( Gender.class, new EnumEditor<Gender>( Gender.class ) );
		binder.registerCustomEditor( Date.class, new DateEditor() );
	}

	@Override
	protected Object formBackingObject( HttpServletRequest request )
		throws Exception {

		String id = request.getParameter( "id" );

		if ( id != null && !"".equals( id ) ) {
			request.getSession().setAttribute( "readOnly", Boolean.TRUE );

			// Look up the ContactsController...

			ContactsController controller = (ContactsController) getWebApplicationContext().getBean( "contacts" );

			// ...and use it to load the Contact...

			return controller.load( Long.parseLong( id ) );
		}

		request.getSession().setAttribute( "readOnly", Boolean.FALSE );

		// Create business

		if ( request.getParameter( "business" ) != null ) {
			return new BusinessContact();
		}

		// Default to personal

		return new PersonalContact();
	}

	@Override
	protected void onBindAndValidate( HttpServletRequest request, Object command, BindException errors )
		throws Exception {

		// Edit

		if ( request.getParameter( "edit" ) != null ) {
			request.getSession().setAttribute( "readOnly", Boolean.FALSE );
			return;
		}

		Contact contact = (Contact) command;

		// Add Communication (if any)

		if ( request.getParameter( "addCommunication" ) != null ) {
			String type = request.getParameter( "communication.type" );
			String value = request.getParameter( "communication.value" );

			try {
				contact.addCommunication( new Communication( type, value ) );
			} catch ( Exception e ) {
				errors.reject( null, e.getMessage() );
				return;
			}
		} else if ( request.getParameter( "deleteCommunication" ) != null ) {
			
			// Delete Communication (if any)

			String id = request.getParameter( "deleteCommunicationId" );

			try {
				contact.removeCommunication( Long.parseLong( id ) );
			} catch ( Exception e ) {
				errors.reject( null, e.getMessage() );
				return;
			}
		}

		// Save

		if ( request.getParameter( "save" ) != null || request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null ) {
			ContactsController controller = (ContactsController) getWebApplicationContext().getBean( "contacts" );

			try {
				controller.save( contact );
			} catch ( Exception e ) {
				String message = e.getMessage();
				
				// Example of inline validation
				
				if ( message.endsWith( " is required" )) {
					String field = StringUtils.substringBefore( message, " is required" );				
					errors.rejectValue( field.toLowerCase(), null, message );
				} else {
					errors.reject( null, message );
				}
			}

			return;
		}

		// Delete

		if ( request.getParameter( "delete" ) != null ) {
			ContactsController controller = (ContactsController) getWebApplicationContext().getBean( "contacts" );
			controller.delete( contact );
		}
	}

	@Override
	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
		throws Exception {

		// Edit/Add Communication/Delete Communication (stay on same page)

		if ( request.getParameter( "edit" ) != null || request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null ) {
			return showForm( request, response, errors );
		}

		// Save/Cancel/Delete

		return new ModelAndView( new RedirectView( "index.html" ) );
	}
}
