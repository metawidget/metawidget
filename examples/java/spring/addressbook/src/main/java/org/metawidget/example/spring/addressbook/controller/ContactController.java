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
// this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation
// and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
// be used to endorse or promote products derived from this software without
// specific prior written permission.
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

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.spring.addressbook.editor.DateEditor;
import org.metawidget.example.spring.addressbook.editor.EnumEditor;
import org.metawidget.util.simple.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Controller
@SessionAttributes( "contact" )
@RequestMapping( "/contact.html" )
public class ContactController {

	//
	// Private members
	//

	@Autowired
	private ContactsController	mContactsController;

	//
	// Protected methods
	//

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {

		binder.registerCustomEditor( Gender.class, new EnumEditor<Gender>( Gender.class ) );
		binder.registerCustomEditor( Date.class, new DateEditor() );
	}

	@RequestMapping( method = RequestMethod.GET )
	public String initForm( ModelMap model, HttpServletRequest request ) {

		String id = request.getParameter( "id" );

		if ( id != null && !"".equals( id ) ) {
			request.getSession().setAttribute( "readOnly", Boolean.TRUE );

			// Look up the ContactsController...

			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext( request.getSession().getServletContext() );
			ContactsController controller = (ContactsController) webApplicationContext.getBean( "contacts" );

			// ...and use it to load the Contact...

			model.put( "contact", controller.load( Long.parseLong( id ) ) );
			return "contact";
		}

		request.getSession().setAttribute( "readOnly", Boolean.FALSE );

		// Create business

		if ( request.getParameter( "business" ) != null ) {
			model.put( "contact", new BusinessContact() );
			return "contact";
		}

		// Default to personal

		model.put( "contact", new PersonalContact() );
		return "contact";
	}

	@RequestMapping( method = RequestMethod.POST, params = { "edit" } )
	public String edit( HttpServletRequest request ) {

		request.getSession().setAttribute( "readOnly", Boolean.FALSE );
		return "contact";
	}

	@RequestMapping( method = RequestMethod.POST, params = { "addCommunication=Add" } )
	public String addCommunication( @ModelAttribute( "contact" ) Contact contact, Errors errors, @RequestParam( "communication.type" ) String type, @RequestParam( "communication.value" ) String value ) {

		try {
			contact.addCommunication( new Communication( type, value ) );
			save( contact, errors );
		} catch ( Exception e ) {
			errors.reject( null, e.getMessage() );
		}

		return "contact";
	}

	@RequestMapping( method = RequestMethod.POST, params = { "deleteCommunication=Delete" } )
	public String deleteCommunication( @ModelAttribute( "contact" ) Contact contact, Errors errors, @RequestParam( "deleteCommunicationId" ) String id ) {

		try {
			contact.removeCommunication( Long.parseLong( id ) );
			save( contact, errors );
		} catch ( Exception e ) {
			errors.reject( null, e.getMessage() );
		}

		return "contact";
	}

	@RequestMapping( method = RequestMethod.POST, params = { "save" } )
	public String save( @ModelAttribute( "contact" ) Contact contact, Errors errors ) {

		// Save

		try {
			mContactsController.save( contact );
		} catch ( Exception e ) {
			String message = e.getMessage();

			// Example of inline validation

			if ( message.endsWith( " is required" ) ) {
				String field = StringUtils.substringBefore( message, " is required" );
				errors.rejectValue( field.toLowerCase(), "required", message );
			} else {
				errors.reject( "other", message );
			}

			return "contact";
		}

		return "redirect:index.html";
	}

	@RequestMapping( method = RequestMethod.POST, params = { "delete" } )
	public String delete( @ModelAttribute( "contact" ) Contact contact ) {

		mContactsController.delete( contact );
		return "redirect:index.html";
	}

	@RequestMapping( method = RequestMethod.POST )
	public String cancel() {

		return "redirect:index.html";
	}
}
