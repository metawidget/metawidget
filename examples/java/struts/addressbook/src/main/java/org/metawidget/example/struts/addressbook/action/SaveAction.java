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

package org.metawidget.example.struts.addressbook.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.struts.addressbook.form.BusinessContactForm;
import org.metawidget.example.struts.addressbook.form.ContactForm;
import org.metawidget.example.struts.addressbook.form.PersonalContactForm;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SaveAction
	extends Action {

	//
	// Public methods
	//

	@Override
	public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
		throws Exception {

		// Sanity check

		if ( form == null ) {
			return mapping.findForward( "home" );
		}

		// Support editing

		ContactForm formContact = (ContactForm) form;

		if ( request.getParameter( "edit" ) != null ) {
			formContact.setReadOnly( false );
			return mapping.findForward( "contact" );
		}

		// Look up the ContactsController...

		ServletContext context = request.getSession().getServletContext();
		ContactsController controller = (ContactsController) context.getAttribute( "contacts" );

		// ...map from our Struts Form (this manual mapping step is an unfortunate artifact
		// of the way Struts 1.x forms must extend ActionForm, not just be regular POJOs).

		Contact contact;

		if ( formContact instanceof PersonalContactForm ) {
			contact = new PersonalContact();

			PersonalContactForm formPersonalContact = (PersonalContactForm) form;
			PersonalContact contactPersonal = (PersonalContact) contact;

			contactPersonal.setDateOfBirth( formPersonalContact.getDateOfBirth() );
		} else {
			contact = new BusinessContact();

			BusinessContactForm formBusinessContact = (BusinessContactForm) form;
			BusinessContact contactBusiness = (BusinessContact) contact;

			contactBusiness.setCompany( formBusinessContact.getCompany() );
			contactBusiness.setNumberOfStaff( formBusinessContact.getNumberOfStaff() );
		}

		contact.setId( formContact.getId() );
		contact.setTitle( formContact.getTitle() );
		contact.setFirstname( formContact.getFirstname() );
		contact.setSurname( formContact.getSurname() );
		contact.setGender( formContact.getGender() );
		contact.setAddress( formContact.getAddress() );
		contact.setCommunications( formContact.getCommunications() );
		contact.setNotes( formContact.getNotes() );

		// Add Communication (if any)

		if ( request.getParameter( "addCommunication" ) != null ) {
			String type = request.getParameter( "communication.type" );
			String value = request.getParameter( "communication.value" );

			try {
				contact.addCommunication( new Communication( type, value ) );
				formContact.setCommunications( contact.getCommunications() );
			} catch ( Exception e ) {
				ActionErrors errors = new ActionErrors();
				errors.add( "save", new ActionMessage( e.getMessage(), false ) );
				addErrors( request, errors );

				return mapping.findForward( "contact" );
			}
		} else if ( request.getParameter( "deleteCommunication" ) != null ) {
			
			// Delete Communication (if any)

			String id = request.getParameter( "deleteCommunicationId" );
			contact.removeCommunication( Long.parseLong( id ) );
			formContact.setCommunications( contact.getCommunications() );
		}

		// Save

		if ( request.getParameter( "save" ) != null || request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null ) {
			try {
				controller.save( contact );
				formContact.setId( contact.getId() );
			} catch ( Exception e ) {
				ActionErrors errors = new ActionErrors();
				String message = e.getMessage();

				if ( message == null ) {
					message = e.getClass().getSimpleName();
				}

				errors.add( "save", new ActionMessage( message, false ) );
				addErrors( request, errors );

				return mapping.findForward( "contact" );
			}

			if ( request.getParameter( "addCommunication" ) != null || request.getParameter( "deleteCommunication" ) != null ) {
				return mapping.findForward( "contact" );
			}
		}

		// Delete

		if ( request.getParameter( "delete" ) != null ) {
			controller.delete( contact );
		}

		// Cancel

		return mapping.findForward( "home" );
	}
}
